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

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the
 * IterativeAdventureRick documentation. If you change the name of this class or
 * the package after creating this project, you must also update the manifest
 * file in the resource directory.
 */
public class AdventureRick extends IterativeRobot {
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

    /**
     * Initializes when the robot first starts, (only once at power-up).
     */
    public void robotInit() {
        operatorInputs = new OperatorInputs();
        drive = new DriveTrain(operatorInputs);
        prefs = Preferences.getInstance();
        pickerPID = new PickerPID();
        shoot = new Shooter(operatorInputs);
        pick = new Picker(operatorInputs, pickerPID, shoot);
        compressor = new Compressor(PRESSURE_SWITCH_CHANNEL, COMPRESSOR_RELAY_CHANNEL);

        this.autoTimer = new Timer();
        drive.leftEncoder.start();  // EAC.2014.02.19 - we may want to move this initialization into the DriveTrain constructor
        drive.rightEncoder.start(); // EAC.2014.02.19 - we may want to move this initialization into the DriveTrain constructor
        drive.time.start(); // EAC.2014.02.19 - we may want to move this initialization into the DriveTrain constructor
        autoTimer.start();
        prefs.getDouble("Kp", PickerPID.Kp);
        prefs.getDouble("Ki", PickerPID.Ki);
        prefs.getDouble("Kd", PickerPID.Kd);
        SmartDashboard.putBoolean("Is High Gear", drive.isHighGear);
        SmartDashboard.putNumber("Left Power Is", drive.leftPow);
        SmartDashboard.putNumber("Right Power Is", drive.rightPow);
        SmartDashboard.putNumber("Speed", drive.totalSpeed);
    }

    /**
     * This function is called periodically (every 20-25 ms) during autonomous
     */
    public void autonomousInit() {
        compressor.start();
        //shoot.shootTimer.start();
        PickerPID.VOLTAGE_CORRECTION = prefs.getDouble("Pick_VC", PickerPID.VOLTAGE_CORRECTION);
        shoot.getPID().VOLTAGE_CORRECTION = prefs.getDouble("Shoot_VC", shoot.getPID().VOLTAGE_CORRECTION);
        drive.resetEncoders();
    }  

    public void autonomousPeriodic() {

        //colwellContraption.pistonUp();
        drive.driveStraight(4.5, 6.5 , 0.6, shoot);
        //pickerPID.enable();//proably not going to be needed
        SmartDashboard.putNumber("Some Voltage", pickerPID.getVoltage());
        SmartDashboard.putNumber("Kp", PickerPID.Kp);
        SmartDashboard.putNumber("Ki", PickerPID.Ki);
        SmartDashboard.putNumber("Kd", PickerPID.Kd);

        System.out.println(drive.getLeftEncoderDistance());
        System.out.println(drive.getRightEncoderDistance());
        
    }

    public void testInit() {//orginally teleop
        pickerPID = new PickerPID();
        pick = new Picker(operatorInputs, pickerPID, shoot);//add parameters as needed
        compressor.start();
        PickerPID.VOLTAGE_CORRECTION = prefs.getDouble("Pick_VC", PickerPID.VOLTAGE_CORRECTION);
        shoot.getPID().VOLTAGE_CORRECTION = prefs.getDouble("Shoot_VC", shoot.getPID().VOLTAGE_CORRECTION);

        Picker.KP_SOFT = prefs.getDouble("KP_SOFT", Picker.KP_SOFT);
        Picker.KP_MEDIUM = prefs.getDouble("KP_MEDIUM", Picker.KP_MEDIUM);
        Picker.KP_HARD = prefs.getDouble("KP_HARD", Picker.KP_HARD);

        Picker.KI_SOFT = prefs.getDouble("KI_SOFT", Picker.KI_SOFT);
        Picker.KI_MEDIUM = prefs.getDouble("KI_MEDIUM", Picker.KI_MEDIUM);
        Picker.KI_HARD = prefs.getDouble("KI_HARD", Picker.KI_HARD);

        Picker.KD_SOFT = prefs.getDouble("KD_SOFT", Picker.KD_SOFT);
        Picker.KD_MEDIUM = prefs.getDouble("KD_MEDIUM", Picker.KD_MEDIUM);
        Picker.KD_HARD = prefs.getDouble("KD_HARD", Picker.KD_HARD);

        pickerPID.enable();
        pickerPID.setSetpoint(pick.kickPos);
    }

    /**
     * This function is called periodically during operator control
     */
    public void testPeriodic() {//originally teleopPeriodic
        //next 2 lines to add manual disable for PIDs
        shoot.emergencyDisablePid();
        pick.emergencyDisablePid();
        //next 2 lines disable PID if in tolerance of position
        shoot.inPositionDisable();
        pick.inPositionDisable();

        drive.setPower();
        pick.pick();
        shoot.moveToKickPos();
        shoot.moveToPickPos();

//shift when the trigger is pressed
        drive.shift();

        shoot.manualShooterControl();

        pick.spinGrabber();//works 2/12/14
        pick.spinReleaser();//works 2/12/14

        SmartDashboard.putBoolean("Compressor Trying To Come On: ", compressor.enabled());
        SmartDashboard.putNumber("kicker Motor Power", shoot.getKickerMotorPower());
        SmartDashboard.putBoolean("Is High Gear", drive.isHighGear);
        SmartDashboard.putNumber("Left Power Is", drive.leftPow);
        SmartDashboard.putNumber("Right Power Is", drive.rightPow);
        SmartDashboard.putBoolean("Is Kicking", shoot.kicking);
        SmartDashboard.putNumber("Speed", drive.totalSpeed);
        SmartDashboard.putNumber("Kp", PickerPID.Kp);
        SmartDashboard.putNumber("Ki", PickerPID.Ki);
        SmartDashboard.putNumber("Kd", PickerPID.Kd);
    }

    public void teleopInit() {//originally test
        super.testInit();
        compressor.start();
        drive.resetEncoders();

    }

    /**
     *
     * This function is called periodically during test mode
     */
    public void teleopPeriodic() {//Orginally testPeriodic
        
        System.out.println("Left Encoder Distance: " + drive.getLeftEncoderDistance());
        System.out.println("Right Encoder Distance: " + drive.getRightEncoderDistance());
        System.out.println("Right Encoder Pulses: " + drive.getRightPulses());
        System.out.println("Left Encoder Pulses: " + drive.getLeftPulses());
        shoot.emergencyDisablePid();
        shoot.moveToKickPos();
        shoot.moveToPickPos();
        shoot.passKick();
        shoot.quickButtonShoot(1.0, -1.0, 0.1);
        shoot.quickLeftButtonShoot(1.0, -0.75, 0.1);

        SmartDashboard.putNumber("Shooter_Position", shoot.getVoltage());
        drive.setPower();
        drive.shift();
        drive.childProofing();
        pick.emergencyDisablePid();
        pick.pick();
        pick.setPosKicking();
        pick.lockKick();
        pick.spinGrabber();
        pick.spinReleaser();
        shoot.manualShooterControl();
    }

    public void disabledInit() {
        pickerPID.getPIDController().reset();
        pickerPID.disable();
        shoot.disablePID();
        super.disabledInit();
        drive.resetEncoders();
    }

}
