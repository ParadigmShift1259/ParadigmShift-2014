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
import edu.wpi.first.wpilibj.networktables.NetworkTable;
/**
 * The VM is configured to automatically run this class, and to call the
 functions corresponding to each mode, as described in the
 IterativeRafiki_Atlas documentation. If you change the name of this class or
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
    private boolean colwel1 = false;
    private boolean colwel2 = true;
    Timer timer = new Timer();
    private boolean hotZoneActive;
    private double visionDistance;
    /**
     * Initializes when the robot first starts, (only once at power-up).
     */
    public void robotInit() {
        //NetworkTable.setTeam(1259); adding the setTeam() method will cause the robot to encounter a thread error
       // NetworkTable.setIPAddress("10.12.59.2");
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
        drive.leftEncoder.start();  // EAC.2014.02.19 - we may want to move this initialization into the DriveTrain constructor
        drive.rightEncoder.start(); // EAC.2014.02.19 - we may want to move this initialization into the DriveTrain constructor
        drive.time.start(); // EAC.2014.02.19 - we may want to move this initialization into the DriveTrain constructor
        autoTimer.start();

       
        
    }

    /**
     * This function is called periodically (every 20-25 ms) during autonomous
     */
    public void autonomousInit() {
        compressor.start();
        colwellContraption1.set(true);
        colwellContraption2.set(false);
        //shoot.shootTimer.start();
        PickerPID.VOLTAGE_CORRECTION = prefs.getDouble("Pick_VC", PickerPID.VOLTAGE_CORRECTION);
        shoot.getPID().VOLTAGE_CORRECTION = prefs.getDouble("Shoot_VC", shoot.getPID().VOLTAGE_CORRECTION);
        drive.resetEncoders();
    }

    public void autonomousPeriodic() {
        if(timer.get()<10.0){
        //hotZoneActive = NetworkTable.getTable("camera").getBoolean("hotZone");
        //visionDistance = NetworkTable.getTable("camera").getNumber("distance");
        //colwellContraption.pistonUp();
        drive.driveStraight(5.0, 6.5 , 0.6, shoot);
        //pickerPID.enable();//proably not going to be needed
        SmartDashboard.putNumber("Some Voltage", pickerPID.getVoltage());
        SmartDashboard.putNumber("Kp", PickerPID.Kp);
        SmartDashboard.putNumber("Ki", PickerPID.Ki);
        SmartDashboard.putNumber("Kd", PickerPID.Kd);

        System.out.println(drive.getLeftEncoderDistance());
        System.out.println(drive.getRightEncoderDistance());
        }else{timer.stop();}
    }

    public void testInit() {//orginally teleop
//        //pickerPID = new PickerPID(); commented out because of too many instances
//        pick = new Picker(operatorInputs, pickerPID, shoot);//add parameters as needed
//        compressor.start();
//        PickerPID.VOLTAGE_CORRECTION = prefs.getDouble("Pick_VC", PickerPID.VOLTAGE_CORRECTION);
//        shoot.getPID().VOLTAGE_CORRECTION = prefs.getDouble("Shoot_VC", shoot.getPID().VOLTAGE_CORRECTION);
//
//        Picker.KP_SOFT = prefs.getDouble("KP_SOFT", Picker.KP_SOFT);
//        Picker.KP_MEDIUM = prefs.getDouble("KP_MEDIUM", Picker.KP_MEDIUM);
//        Picker.KP_HARD = prefs.getDouble("KP_HARD", Picker.KP_HARD);
//
//        Picker.KI_SOFT = prefs.getDouble("KI_SOFT", Picker.KI_SOFT);
//        Picker.KI_MEDIUM = prefs.getDouble("KI_MEDIUM", Picker.KI_MEDIUM);
//        Picker.KI_HARD = prefs.getDouble("KI_HARD", Picker.KI_HARD);
//
//        Picker.KD_SOFT = prefs.getDouble("KD_SOFT", Picker.KD_SOFT);
//        Picker.KD_MEDIUM = prefs.getDouble("KD_MEDIUM", Picker.KD_MEDIUM);
//        Picker.KD_HARD = prefs.getDouble("KD_HARD", Picker.KD_HARD);
//
//        pickerPID.enable();
//        pickerPID.setSetpoint(pick.kickPos);
    }

    /**
     * This function is called periodically during operator control
     */
    public void testPeriodic() {//originally teleopPeriodic
        compressor.start();
        boolean buttonPressed = operatorInputs.button7();
        if (buttonPressed) {
            colwellContraption1.set(!colwel1);
            colwellContraption2.set(!colwel2);
        }
        System.out.println("Shooter position " + shoot.getVoltage());
    }

    public void teleopInit() {//originally test
        //super.testInit();
        compressor.start();
        colwellContraption1.set(true);
        colwellContraption2.set(false);
        //compressor.start();
        drive.resetEncoders();

    }

    /**
     *
     * This function is called periodically during test mode
     */
    public void teleopPeriodic() {//Orginally testPeriodic
        compressor.start();
        SmartDashboard.putBoolean("Is High Gear", drive.isHighGear);
        SmartDashboard.putNumber("Left Power Is", drive.leftPow);
        SmartDashboard.putNumber("Right Power Is", drive.rightPow);
        SmartDashboard.putNumber("Speed", drive.totalSpeed);
        System.out.println("Left Encoder Distance: " + drive.getLeftEncoderDistance());
        System.out.println("Right Encoder Distance: " + drive.getRightEncoderDistance());
        System.out.println("Right Encoder Pulses: " + drive.getRightPulses());
        System.out.println("Left Encoder Pulses: " + drive.getLeftPulses());
        shoot.emergencyDisablePid();
        shoot.moveToKickPos();
        shoot.moveToPickPos();
//        boolean buttonPressed;
//        if((buttonPressed = operatorInputs.xBoxBackButton()) && !previousPressed){
//            colwellContraption1.set(!colwellContraption1.get());
//            colwellContraption2.set(!colwellContraption2.get());
//        }
//        previousPressed = operatorInputs.xBoxBackButton();
        shoot.passKick();
        shoot.quickButtonShoot(1.0, -1.0, 0.1);
        shoot.quickLeftButtonShoot(1.0, -0.75, 0.1);

        System.out.println("Picker_Position " + pick.getVoltage());
        drive.setPower();
        drive.shift();
        drive.childProofing();
        pick.emergencyDisablePid();
        if((pick.getVoltage()>-.75 && pick.getVoltage()<-.7)){
            pickerPID.disable();
        }else{
            pick.pick();
        }
        pick.setPosKicking();
        //pick.lockKick();
        pick.spinGrabber();
        pick.spinReleaser();
        shoot.manualShooterControl();
    }

    public void disabledInit() {
        pickerPID.getPIDController().reset();
        pickerPID.disable();
        colwellContraption1.set(false);
        colwellContraption2.set(true);
        shoot.disablePID();
        super.disabledInit();
        drive.resetEncoders();
    }

}
