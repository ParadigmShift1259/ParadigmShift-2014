/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.paradigm;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the
 * IterativeRafiki_Atlas documentation. If you change the name of this class or
 * the package after creating this project, you must also update the manifest
 * file in the resource directory.
 */
public class Rafiki_Atlas extends IterativeRobot {

    private DriveTrain drive;
    private OperatorInputs operatorInputs;
    private Compressor compressor;
    private Shooter shoot;
    private ShooterPID shooterPID;
    private PickerPID pickerPID;
    private Timer autoTimer;
    private Picker pick;
    private DriverStation station;
    private Preferences prefs;
    private final int PRESSURE_SWITCH_CHANNEL = 1;
    private final int COMPRESSOR_RELAY_CHANNEL = 1;
    private Solenoid colwellContraption1, colwellContraption2;
    private boolean previousPressed = false;
    private boolean colwell1 = false;
    private boolean colwell2 = true;
    Timer timer = new Timer();
    private boolean hotZoneActive;
    private double visionDistance;

    /**
     * Called when the robot first starts, (only once at power-up).
     */
    public void robotInit() {
        //NetworkTable.setTeam(1259); adding the setTeam() method will cause the robot to encounter a thread error
        //NetworkTable.setIPAddress("10.12.59.2");
        operatorInputs = new OperatorInputs();
        drive = new DriveTrain(operatorInputs);
        prefs = Preferences.getInstance();
        pickerPID = new PickerPID();
        shoot = new Shooter(operatorInputs);
        pick = new Picker(operatorInputs, pickerPID, shoot);
        compressor = new Compressor(PRESSURE_SWITCH_CHANNEL, COMPRESSOR_RELAY_CHANNEL);
        colwellContraption1 = new Solenoid(1, 3);
        colwellContraption2 = new Solenoid(1, 4);
        colwellContraption2.set(true);

        this.autoTimer = new Timer();
        drive.leftEncoder.start();  
        drive.rightEncoder.start(); 
        drive.timer.start(); 
        autoTimer.start();
    }
    
    //Called when you hit disable in the driver station
    public void disabledInit() {
        pickerPID.getPIDController().reset();
        pickerPID.disable();
        colwellContraption1.set(false);
        colwellContraption2.set(true);
        shoot.disablePID();
        super.disabledInit();
        drive.resetEncoders();
    }

    public void teleopInit() {
        compressor.start();
        colwellContraption1.set(true);
        colwellContraption2.set(false);
        drive.resetEncoders();
        pick.pickerPID.disable();
        
    }

    public void teleopPeriodic() {
        //System.out.println("Voltage :" + DriverStation.getInstance().getBatteryVoltage());
        //System.out.println("Negative:"+(-6.0%5.0));
        //System.out.println("Positive:"+(6.0%5.0));
        NetworkTable.getTable("camera").putNumber("team", 1259);
        compressor.start();
        boolean buttonPressed = operatorInputs.button7();
        if (buttonPressed) {
            colwellContraption1.set(!colwell1);
            colwellContraption2.set(!colwell2);
        }
        
        SmartDashboard.putBoolean("Is High Gear", drive.isHighGear);
        SmartDashboard.putNumber("Left Power Is", drive.leftPow);
        SmartDashboard.putNumber("Right Power Is", drive.rightPow);
        SmartDashboard.putNumber("Speed", drive.totalSpeed);
        shoot.emergencyDisablePid();
        shoot.moveToKickPos();
        shoot.moveToPickPos();
        shoot.quickButtonShoot(1.0, -0.95, 0.1);
        drive.setCoasting(shoot.getCoastingValue());
        drive.setPower();
        drive.shift();
        //drive.childProofing();
        pick.emergencyDisablePid();
        pick.getStepValues();
        if ((pick.getVoltage() > -.75 && pick.getVoltage() < -.7)) {
            pickerPID.disable();
        } else {
            pick.pick();
        }
        pick.setPosKicking();
        pick.lockKick();
        pick.spinGrabber();
        pick.spinReleaser();
       // System.out.println(pick.getVoltage());
        shoot.manualShooterControl();
    }

    
    public void autonomousInit() {

        compressor.start();
        colwellContraption1.set(true);
        colwellContraption2.set(false);
        //shoot.shootTimer.start();
        PickerPID.VOLTAGE_CORRECTION = prefs.getDouble("Pick_VC", PickerPID.VOLTAGE_CORRECTION);
        shoot.getPID().VOLTAGE_CORRECTION = prefs.getDouble("Shoot_VC", shoot.getPID().VOLTAGE_CORRECTION);
        drive.resetEncoders();
        Talon wheelSpin = pick.returnSpinner();
        wheelSpin.set(1.0);
        timer.start();
    }
    
    /**
     * This function is called periodically (every 20-25 ms) during autonomous
     */
    public void autonomousPeriodic() {
        Talon wheelSpin = pick.returnSpinner();
        wheelSpin.set(0);
        boolean hotZone = NetworkTable.getTable("camera").getBoolean("hotZone", false);
        System.out.println(hotZone);
        if (timer.get() < 10.0) {
            if(hotZone){
            //hotZoneActive = NetworkTable.getTable("camera").getBoolean("hotZone");
            //visionDistance = NetworkTable.getTable("camera").getNumber("distance");
            drive.driveStraight(5.0, 6.5, 7.2/DriverStation.getInstance().getBatteryVoltage(), shoot);
            SmartDashboard.putNumber("Some Voltage", pickerPID.getVoltage());
            SmartDashboard.putNumber("Kp", PickerPID.Kp);
            SmartDashboard.putNumber("Ki", PickerPID.Ki);
            SmartDashboard.putNumber("Kd", PickerPID.Kd);
            System.out.println("Case 1");
            }else{
                if(timer.get()>2.0){
                drive.driveStraight(5.0, 6.5, 7.2/DriverStation.getInstance().getBatteryVoltage(), shoot);
                System.out.println("Case 2");
                }
            System.out.println("Case 3");
            }
        } else {
            timer.stop();
            System.out.println("Case 4");
        }
    }

    public void testInit() {

    }

    public void testPeriodic() {
        /*
        boolean buttonPressed = operatorInputs.button7();
        if (buttonPressed) {
            colwellContraption1.set(!colwell1);
            colwellContraption2.set(!colwell2);
        }
        System.out.println("Picker position " + pickerPID.getVoltage());
        */
        /*try {
        hotZoneActive = NetworkTable.getTable("camera").getBoolean("hotZone");
        } catch (Exception e) {
            hotZoneActive = true;
        }
        System.out.println("hotZoneActive "+hotZoneActive);*/
        System.out.println("Picker Encoder Voltage: " + pick.getVoltage());
        System.out.println("Shooter Encoder Voltage: " + shoot.getVoltage());
    }

}
