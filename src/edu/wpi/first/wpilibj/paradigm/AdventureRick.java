/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.paradigm;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Compressor;
//import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;

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
    Shooter shoot;
    PickerPID pickerPID;
    //Picker pick;

    //Preferences prefs;
    private boolean checkForKickerStop = false;

    final int PRESSURE_SWITCH_CHANNEL = 1;
    final int COMPRESSOR_RELAY_CHANNEL = 1;

    /**
     * Initializes when the robot first starts, (only once at power-up).
     */
    public void robotInit() {
        inputs = new OperatorInputs();
        operatorInputs = new OperatorInputs();
        drive = new DriveTrain(operatorInputs);
        //pressureSwitchChannel - The GPIO channel that the pressure switch is attached to.
        //compressorRelayChannel - The relay channel that the compressor relay is attached to.
        compressor = new Compressor(PRESSURE_SWITCH_CHANNEL, COMPRESSOR_RELAY_CHANNEL);
        shoot = new Shooter(operatorInputs);//add parameters as needed
        pickerPID = new PickerPID();
        shoot.caliButtPressed = true;
        //pick = new Picker(operatorInputs);//add parameters as needed
        //compressor.start();
        drive.leftEncoder.start();
        drive.rightEncoder.start();
        drive.time.start();
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
    public void autonomousPeriodic() {
        //shoot.calibrate();
        pickerPID.enable();
       
        //SmartDashboard.putData("PID",pickerPID.getPIDController());
    }

    /**
     * This function is called periodically during operator control
     */
    public void disabledInit(){
        pickerPID.disable();
        pickerPID.getPIDController().reset();
        super.disabledInit();
    }
    public void teleopPeriodic() {
        drive.setPower();
        //remove if not needed
        compressor.start();
//shift when the trigger is pressed
        drive.shift();
        drive.childProofing();
//        drive.shiftHigh();
//        drive.shiftLow();
        shoot.kick();
        shoot.calibrate();
        shoot.setKickingPosition();
        //shoot.manualShooterControl();
//        pick.spinGrabber();//works 2/12/14
//        pick.spinPooter();//works 2/12/14
        //all of these positions make the picker move forward, need to test the encoder value to reset values
        //pick.setPosAuto();
        //pick.setPosKicking();
        //pick.setPosLoading();
        SmartDashboard.putNumber("kicker Motor Power", shoot.getKickerMotorPower());
        SmartDashboard.putBoolean("Blah:", shoot.kicking);

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
    }

    /**
     *
     * This function is called periodically during test mode
     */
    public void testPeriodic() {

        //pickerPID.enable();
//        System.out.println("Picker Encoder Value Is " + pick.getPickerAngle());
//
//        System.out.println("Shooter Encoder Value Is :" + shoot.getKickerAngle());

        //System.out.println(pick.pickerPID.getPickerAngle());
//
//        shoot.manualShooterControl();
//        shoot.autoShoot(5, -.1);
    }

}
