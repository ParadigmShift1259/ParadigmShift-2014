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
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the
 * IterativeAdventureRick documentation. If you change the name of this class or
 * the package after creating this project, you must also update the manifest
 * file in the resource directory.
 */
public class AdventureRick extends IterativeRobot {
    //electromagic!

    OperatorInputs inputs;
    DriveTrain drive;
    OperatorInputs operatorInputs;
    Compressor compressor;
    public static Shooter shoot;
    PickerPID pickerPID;
    Timer autoTimer;
    Picker pick;

    Preferences prefs;

    private boolean checkForKickerStop = false;
    private boolean shootTimerBool = true;

    final int PRESSURE_SWITCH_CHANNEL = 1;
    final int COMPRESSOR_RELAY_CHANNEL = 1;

    /**
     * Initializes when the robot first starts, (only once at power-up).
     */
    public void robotInit() {
        inputs = new OperatorInputs();
        operatorInputs = new OperatorInputs();
        drive = new DriveTrain(operatorInputs);
        prefs = Preferences.getInstance();
        //pressureSwitchChannel - The GPIO channel that the pressure switch is attached to.
        //compressorRelayChannel - The relay channel that the compressor relay is attached to.
        compressor = new Compressor(PRESSURE_SWITCH_CHANNEL, COMPRESSOR_RELAY_CHANNEL);
        shoot = new Shooter(operatorInputs);//add parameters as needed
        pickerPID = new PickerPID();
        this.autoTimer = new Timer();
        shoot.caliButtPressed = true;
        pick = new Picker(operatorInputs, pickerPID);//add parameters as needed
        //compressor.start();
        drive.leftEncoder.start();
        drive.rightEncoder.start();
        drive.time.start();
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
         SmartDashboard.putBoolean("Is Picking", pick.isPicking);
         SmartDashboard.putBoolean("Is Pooting", pick.isPicking);
         */
        SmartDashboard.putBoolean("Is Kicking", shoot.kicking);
        SmartDashboard.putBoolean("Is Ready To Kick", shoot.inPosition);

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
        //compressor.start();
        //shoot.shootTimer.start();
        PickerPID.VOLTAGE_CORRECTION = prefs.getDouble("Voltage_Correction", PickerPID.VOLTAGE_CORRECTION);
    }

    public void autonomousPeriodic() {
        //shoot.calibrate();

        final double autoDriveTime = 1;
        shoot.manualShooterControl();
        //pickerPID.enable();//proably not going to be needed
        SmartDashboard.putNumber("Some Voltage", pickerPID.getVoltage());

        SmartDashboard.putNumber("Kp", PickerPID.Kp);
        SmartDashboard.putNumber("Ki", PickerPID.Ki);
        SmartDashboard.putNumber("Kd", PickerPID.Kd);
        /*
         if (autoDriveTime > this.autoTimer.get()) {//drives forward at half power for 1 second
         drive.leftTalons.set(-0.5);
         drive.rightTalons.set(0.5);
         } else {
         drive.leftTalons.set(0.0);
         drive.rightTalons.set(0.0);
         if (shootTimerBool) {
         shoot.shootTimer.start();
         }
         shoot.autoShoot(1, -1.0);//values need to be checked with a ball; time, power
         shootTimerBool = false;
         }
         */

        //SmartDashboard.putData("PID",pickerPID.getPIDController());
    }

    public void teleopInit() {
        compressor.start();
        PickerPID.VOLTAGE_CORRECTION = prefs.getDouble("Voltage_Correction", PickerPID.VOLTAGE_CORRECTION);

        Picker.KP_SOFT = prefs.getDouble("KP_SOFT", Picker.KP_SOFT);
        Picker.KP_MEDIUM = prefs.getDouble("KP_MEDIUM", Picker.KP_MEDIUM);
        Picker.KP_HARD = prefs.getDouble("KP_HARD", Picker.KP_HARD);

        Picker.KI_SOFT = prefs.getDouble("KI_SOFT", Picker.KI_SOFT);
        Picker.KI_MEDIUM = prefs.getDouble("KI_MEDIUM", Picker.KI_MEDIUM);
        Picker.KI_HARD = prefs.getDouble("KI_HARD", Picker.KI_HARD);

        Picker.KD_SOFT = prefs.getDouble("KD_SOFT", Picker.KD_SOFT);
        Picker.KD_MEDIUM = prefs.getDouble("KD_MEDIUM", Picker.KD_MEDIUM);
        Picker.KD_HARD = prefs.getDouble("KD_HARD", Picker.KD_HARD);
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
        pickerPID.enable();
        pickerPID.setSetpoint(pick.kickPos);
    }

    public void teleopPeriodic() {
        drive.setPower();
        pick.kick();
        pick.pick();
        pick.truss();
        shoot.disableShooterPIDIfInPos();

        //remove if not needed
        compressor.start();
//shift when the trigger is pressed
        drive.shift();
        // pickerPID.steppedSetpoint();
        drive.childProofing();
//        drive.shiftHigh();
//        drive.shiftLow();
        //shoot.kick();
        shoot.calibrate();
        shoot.setKickingPosition();
        shoot.manualShooterControl();
        shoot.quickButtonShoot(1.0, -0.7, 0.5);
        pick.spinGrabber();//works 2/12/14
        pick.spinPooter();//works 2/12/14
        compressor.start();
        SmartDashboard.putBoolean("Compressor Trying To Come On: ", compressor.enabled());
        //all of these positions make the picker move forward, need to test the encoder value to reset values
        //pick.setPosAuto();
        //pick.setPosKicking();
        //pick.setPosLoading();
        SmartDashboard.putNumber("kicker Motor Power", shoot.getKickerMotorPower());

        //drive.engageShifter();
        //System.out.println("Trigger " + operatorInputs.joystickTriggerPressed());
        //After the robot has kicked, check to see if it has stopped
//        checkForKickerStop = shoot.checkToKick();
//        if (checkForKickerStop == true) {
//            shoot.isKickerStopped();
//        }
        SmartDashboard.putBoolean("Is High Gear", drive.isHighGear);
        SmartDashboard.putNumber("Left Power Is", drive.leftPow);
        SmartDashboard.putNumber("Right Power Is", drive.rightPow);
        //SmartDashboard.putNumber("Left Encoder Value Is", drive.leftEncoderFix);
        //SmartDashboard.putNumber("Right Encoder Value Is", drive.rightEncoderFix);
        /*
         SmartDashboard.putBoolean("Is Picking", pick.isPicking);
         SmartDashboard.putBoolean("Is Pooting", pick.isPicking);
         */
        SmartDashboard.putBoolean("Is Kicking", shoot.kicking);
        SmartDashboard.putBoolean("Is Ready To Kick", shoot.inPosition);

        SmartDashboard.putNumber("Speed", drive.totalSpeed);
        //SmartDashboard.putNumber("Kicker Angle", shoot.angle); *Don't need to display, not sure what will be displayed.
        //drive.leftPow = prefs.getDouble("TestingCoolThings", 1.0);

        SmartDashboard.putNumber("Kp", PickerPID.Kp);
        SmartDashboard.putNumber("Ki", PickerPID.Ki);
        SmartDashboard.putNumber("Kd", PickerPID.Kd);
    }

    /**
     * This function is called periodically during operator control
     */
    /**
     *
     * This function is called periodically during test mode
     */
    public void testInit() {
        super.testInit();
//        pickerPID.disable();
        compressor.start();

    }

    public void testPeriodic() {
        compressor.start();
         SmartDashboard.putNumber("Shooter_Position", shoot.getVoltage());
         SmartDashboard.putNumber("Picker_Position", pick.getVoltage());
        /*

         //pickerPID.enable();
         //        System.out.println("Picker Encoder Value Is " + pick.getPickerAngle());
         //
         //        System.out.println("Shooter Encoder Value Is :" + shoot.getKickerAngle());
         //System.out.println(pick.pickerPID.getPickerAngle());
         //
         //        shoot.manualShooterControl();
         //        shoot.quickButtonShoot(5, -.1);
         pick.manualPickerControl();
         SmartDashboard.putNumber("Kp", PickerPID.Kp);
         SmartDashboard.putNumber("Ki", PickerPID.Ki);
         SmartDashboard.putNumber("Kd", PickerPID.Kd);
         //System.out.println("voltage " + pickerPID.getVoltage());
         */
    }

    public void disabledInit() {
        pickerPID.getPIDController().reset();
        pickerPID.disable();
        super.disabledInit();
    }

}
