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
    //electromagic!
    private final double autoDriveTime = 1.0;
    DriveTrain drive;
    OperatorInputs operatorInputs;
    Compressor compressor;
    Shooter shoot;
    ShooterPID shooterPID;
    PickerPID pickerPID;
    Timer autoTimer;
    Picker pick;
    DriverStation station;
    Preferences prefs;
    private boolean checkForKickerStop = false;
    private boolean shootTimerBool = true;
    final int PRESSURE_SWITCH_CHANNEL = 1; // EAC.2014.02.19 - may benefit in compile-size by being static
    final int COMPRESSOR_RELAY_CHANNEL = 1; // EAC.2014.02.19 - may benefit in compile-size by being static

    /**
     * Initializes when the robot first starts, (only once at power-up).
     */
    public void robotInit() {
        operatorInputs = new OperatorInputs();
        drive = new DriveTrain(operatorInputs);
        prefs = Preferences.getInstance();
        pickerPID = new PickerPID();
        pick = new Picker(operatorInputs, pickerPID, shoot);
        //pressureSwitchChannel - The GPIO channel that the pressure switch is attached to.
        //compressorRelayChannel - The relay channel that the compressor relay is attached to.
        compressor = new Compressor(PRESSURE_SWITCH_CHANNEL, COMPRESSOR_RELAY_CHANNEL);
        //shoot = new Shooter(operatorInputs);//add parameters as needed
        shoot = new Shooter(operatorInputs, pick);
        shooterPID = new ShooterPID();

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
        //SmartDashboard.putNumber("Left Encoder Value Is", drive.leftEncoderFix);
        //SmartDashboard.putNumber("Right Encoder Value Is", drive.rightEncoderFix);
        /*
         SmartDashboard.putBoolean("Is Picking", picker.isPicking);
         SmartDashboard.putBoolean("Is Pooting", picker.isPicking);
         */
        //SmartDashboard.putBoolean("Is Kicking", shoot.kicking);
//        SmartDashboard.putBoolean("Is Ready To Kick", shoot.inPosition);

        SmartDashboard.putNumber("Speed", drive.totalSpeed);
//      SmartDashboard.putNumber("Kicker Angle", shoot.angle); -> Don't need to display, not sure what will be displayed.
        //autonomousCommand = (Command) testChooser.getSelected();
        //autonomousCommand.start();
        //drive.leftPow = prefs.getDouble("TestingCoolThings", 1.0);

        //operatorInputs.shiftHigh = false;
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
        //shoot.caliButtPressed = true;
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

        /*
         PickerPID.Kp = prefs.getDouble("Kp", PickerPID.Kp);
         PickerPID.Ki = prefs.getDouble("Ki", PickerPID.Ki);
         PickerPID.Kd = prefs.getDouble("Kd", PickerPID.Kd);
         pickerPID.getPIDController().setPID(PickerPID.Kp, PickerPID.Ki, PickerPID.Kd);
         PickerPID.position = prefs.getDouble("Position", PickerPID.position);
         pickerPID.setSetpoint(PickerPID.position);
         PickerPID.TOLERANCE = prefs.getDouble("Tolerance", PickerPID.TOLERANCE);
         pickerPID.setAbsoluteTolerance(PickerPID.TOLERANCE);
         PickerPID.outputBounds = prefs.getDouble("output_bound", PickerPID.outputBounds);
         pickerPID.getPIDController().setOutputRange(-PickerPID.outputBounds, PickerPID.outputBounds);
         */
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

        pick.kick();
       // picker.middle();
        pick.pick();
        shoot.moveToKickPos();
        shoot.moveToPickPos();

//shift when the trigger is pressed
        drive.shift();

        //drive.childProofing();
        shoot.manualShooterControl();

        //shoot.quickButtonShoot(1.0, -1.0, 0.2);
        pick.spinGrabber();//works 2/12/14
        pick.spinPooter();//works 2/12/14

        SmartDashboard.putBoolean("Compressor Trying To Come On: ", compressor.enabled());

        //all of these positions make the picker move forward, need to test the encoder value to reset values
        //pick.setPosAuto();
        //pick.setPosKicking();
        //pick.setPosLoading();
        SmartDashboard.putNumber("kicker Motor Power", shoot.getKickerMotorPower());

        //System.out.println("Trigger " + operatorInputs.joystickTriggerPressed());
        SmartDashboard.putBoolean("Is High Gear", drive.isHighGear);
        SmartDashboard.putNumber("Left Power Is", drive.leftPow);
        SmartDashboard.putNumber("Right Power Is", drive.rightPow);
        //SmartDashboard.putNumber("Left Encoder Value Is", drive.leftEncoderFix);
        //SmartDashboard.putNumber("Right Encoder Value Is", drive.rightEncoderFix);

        /*
         SmartDashboard.putBoolean("Is Picking", picker.isPicking);
         SmartDashboard.putBoolean("Is Pooting", picker.isPicking);
         */
        SmartDashboard.putBoolean("Is Kicking", shoot.kicking);
//        SmartDashboard.putBoolean("Is Ready To Kick", shoot.inPosition);

        SmartDashboard.putNumber("Speed", drive.totalSpeed);
        //SmartDashboard.putNumber("Kicker Angle", shoot.angle); *Don't need to display, not sure what will be displayed.

        SmartDashboard.putNumber("Kp", PickerPID.Kp);
        SmartDashboard.putNumber("Ki", PickerPID.Ki);
        SmartDashboard.putNumber("Kd", PickerPID.Kd);
    }

    public void teleopInit() {//originally test
        super.testInit();
//        pickerPID.disable();
        compressor.start();
        //pickerPID.enable();
        //pickerPID.setSetpoint(picker.kickPos);

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
        //station = station.getInstance();
        //System.out.println("Kicker Set Speed: " + shoot.get());
        //System.out.println("Kicker Encoder Voltage: " + shoot.getVoltage());
        shoot.emergencyDisablePid();
        //shoot.inPositionDisable();

        shoot.moveToKickPos();
        shoot.moveToPickPos();
        shoot.quickButtonShoot(1.0, -1.0, 0.1);
        shoot.quickLeftButtonShoot(1.0, -0.75, 0.1);

        SmartDashboard.putNumber("Shooter_Position", shoot.getVoltage());
//        SmartDashboard.putNumber("Picker_Position", picker.getVoltage());
//        SmartDashboard.putNumber("Battery Voltage: ", station.getBatteryVoltage());
//        System.out.println("Battery Voltage: " + station.getBatteryVoltage());
        drive.setPower();
        drive.shift();
        drive.childProofing();
        //compressor.start();

        //System.out.println("Picker Encoder Value Is " + pickerPID.getVoltage());
        //System.out.println("Picker Voltage Correction Is " + PickerPID.VOLTAGE_CORRECTION);

        //System.out.println("Is High Gear " + drive.isHighGear);
//        System.out.println("Left Power Is "+ drive.leftPow);
//        System.out.println("Right Power Is " + drive.rightPow);
         //pickerPID.enable();
        //        System.out.println("Picker Encoder Value Is " + picker.getPickerAngle());
        //
        //System.out.println("Shooter Encoder Value Is :" + shooterPID.getVoltage());
                         //System.out.println(picker.pickerPID.getPickerAngle());
        //
        //        shoot.manualShooterControl();
        //        shoot.quickButtonShoot(5, -.1);
                 /*
         picker.manualPickerControl();
         SmartDashboard.putNumber("Kp", PickerPID.Kp);
         SmartDashboard.putNumber("Ki", PickerPID.Ki);
         SmartDashboard.putNumber("Kd", PickerPID.Kd);
         //System.out.println("voltage " + pickerPID.getVoltage());
         */
        pick.emergencyDisablePid();
        pick.pick();
        //System.out.println("Calling picker.setPosKicking();");
        pick.setPosKicking();
        //System.out.println("Called picker.setPosKicking();");
        //pickerPID.disable();
        //System.out.println(pickerPID.getPIDController().isEnable());
        pick.lockKick();
        pick.spinGrabber();
        pick.spinPooter();
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
