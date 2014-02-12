/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.paradigm;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the
 * IterativeAdventureRick documentation. If you change the name of this class or
 * the package after creating this project, you must also update the manifest
 * file in the resource directory.
 */
public class AdventureRick extends IterativeRobot {

    final int PRESSURE_SWITCH_CHANNEL = 1;
    final int COMPRESSOR_RELAY_CHANNEL = 1;
    private final long KICKING_DURATION = 1000;
    
    OperatorInputs operatorInputs = new OperatorInputs();
    DriveTrain drive = new DriveTrain();
    Compressor compressor = new Compressor(PRESSURE_SWITCH_CHANNEL, COMPRESSOR_RELAY_CHANNEL);
    Shooter shoot = new Shooter();
    Picker pick = new Picker();
    
    private long kickingStartTime;
    private boolean calibrateButtonEnabled;
    private boolean shifterTriggerEnabled;
    private boolean kickerButtonEnabled;
    private boolean kickerToReadyButtonEnabled;
    private boolean pickerToPickButtonEnabled;
    private boolean pickerToKickButtonEnabled;
    private boolean pickerToStartPositionButtonEnabled;
    private boolean pickerWheelReverseButtonEnabled;
    private boolean pickerWheelForwardButtonEnabled;
    private boolean pickerWheelStopButtonEnabled;

    /**
     * Initializes when the robot first starts, (only once at power-up).
     */
    public void robotInit() {

        compressor.start();
        drive.leftEncoder.start();
        drive.rightEncoder.start();
        drive.time.start();
        
        calibrateButtonEnabled = true;
        shifterTriggerEnabled = true;
        kickerButtonEnabled = true;
        kickerToReadyButtonEnabled = true;
        pickerToPickButtonEnabled = true;
        pickerToKickButtonEnabled = true;
        pickerToStartPositionButtonEnabled = true;
        pickerWheelReverseButtonEnabled = true;
        pickerWheelForwardButtonEnabled = true;
        pickerWheelStopButtonEnabled = true;
        
        State.kickerMode = State.KICKER_STOPPED;
        State.pickerMode = State.PICKER_IN_STARTING_POSITION;
        State.pickerWheelsMode = State.PICKER_WHEELS_STOPPED;
        
        updateDashboard();
    }

    /**
     * This function is called periodically (every 20-25 ms) during autonomous
     */
    public void autonomousPeriodic() {
        
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        reenableButtons();
        
        //********************************** DRIVETRAIN CONTROLS ***********************************
        
        //  Drivetrain Gear Shifting
        if (shifterTriggerEnabled && operatorInputs.joystickTriggerPressed()) {
            drive.shift();
            shifterTriggerEnabled = false; // do not enable more shifting unless trigger is first released
        }
        
        // Speed control
        drive.setPower(operatorInputs.joystickX(), operatorInputs.joystickY());
        
        // ??
        drive.childProofing();

        //************************************ KICKER CONTROLS *************************************
        
        if (    (State.kickerMode != State.KICKER_CALIBRATING)
             && (State.kickerMode != State.KICKING)
             && (State.kickerMode != State.KICKER_STOPPING)
            ) {
            //Y-axis is up negative, down positive; Map Y-axis up to green, Y-axis down to red)
            shoot.manualShooterControl(-operatorInputs.xboxLeftY());
        }
        
        // Determine if we need to move kicking arm into "ready" position
        if (kickerToReadyButtonEnabled && requestToMoveKickerToReady()) {
            if (    (State.kickerMode != State.KICKER_CALIBRATING)
                 && (State.kickerMode != State.KICKING)
                 && (State.kickerMode != State.KICKER_STOPPING)
               ) {
                State.kickerMode = State.KICKER_MOVING_TO_READY_POSITION;
                kickerToReadyButtonEnabled = false;
            }
        }
        if (State.kickerMode == State.KICKER_MOVING_TO_READY_POSITION) {
            if (shoot.setKickingPosition() == true) {
                State.kickerMode = State.KICKER_IN_READY_POSITION;
            }
        }
        
        // Determine if we need to kick
        if (kickerButtonEnabled && requestToKick()) {
            if (    (State.kickerMode != State.KICKER_CALIBRATING)
                 && (State.kickerMode != State.KICKING)
                 && (State.kickerMode != State.KICKER_STOPPING)
                 //&& (State.kickerMode != State.KICKER_MOVING_TO_READY_POSITION)
                 //&& (State.kickerMode == State.KICKER_IN_READY_POSITION)
               ) {
                
                State.kickerMode = State.KICKING;
                kickingStartTime = System.currentTimeMillis();
                kickerButtonEnabled = false;
            }
        }
        if (State.kickerMode == State.KICKING) {
            if ((System.currentTimeMillis() - kickingStartTime) < KICKING_DURATION) {
                shoot.kick();
            } else {
                shoot.stopKicker();
                State.kickerMode = State.KICKER_STOPPING;
            }
        }
        
        // Wait for kicker arm to stop, if in the KICKER_STOPPING mode
        if (State.kickerMode == State.KICKER_STOPPING) {
            if (shoot.isKickerStopped()) {
                State.kickerMode = State.KICKER_STOPPED;  // use this to only use button to manually move it back to ready
                // State.kickerMode = State.KICKER_MOVING_TO_READY_POSITION; //use this to have it move back automatically
            }
        }
        
        //************************************ PICKER CONTROLS *************************************
        
        // Determine if operator wants to move to picker to picking position
        if (pickerToPickButtonEnabled && requestToMovePickerToPick()) {
            if ((State.kickerMode != State.KICKING)) {
                State.pickerMode = State.PICKER_MOVING_TO_PICK;
                pickerToPickButtonEnabled = false;
            }
        }
        if (State.pickerMode == State.PICKER_MOVING_TO_PICK) {
            if (pick.setPosLoading()) {
                State.pickerMode = State.PICKER_IN_PICKING_POSITION;
            }
        }
        
        // Determine if operator wants to move to picker to kicking position
        if (pickerToKickButtonEnabled && requestToMovePickerToKick()) {
            State.pickerMode = State.PICKER_MOVING_TO_KICK;
            pickerToKickButtonEnabled = false;
        }
        if (State.pickerMode == State.PICKER_MOVING_TO_KICK) {
            if (pick.setPosKicking()) {
                State.pickerMode = State.PICKER_IN_KICKING_POSITION;
            }
        }
        
        // Determine if operator wants to move to picker to starting position
        if (pickerToStartPositionButtonEnabled && requestToMovePickerToStartPosition()) {
            State.pickerMode = State.PICKER_MOVING_TO_STARTING_POSITION;
            pickerToStartPositionButtonEnabled = false;
        }
        if (State.pickerMode == State.PICKER_MOVING_TO_STARTING_POSITION) {
            if (pick.setPosAuto()) {
                State.pickerMode = State.PICKER_IN_STARTING_POSITION;
            }
        }
        
        // Spin the picker wheels in reverse to grab the ball
        if (pickerWheelReverseButtonEnabled & requestToSpinPickerWheelsInReverse()) {
            State.pickerWheelsMode = State.PICKER_WHEELS_REVERSE;
            pick.spinPickerWheelsReverse();
        }
        
        // Spin the picker wheels forward to release the ball
        if (pickerWheelForwardButtonEnabled & requestToSpinPickerWheelsForward()) {
            State.pickerWheelsMode = State.PICKER_WHEELS_FORWARD;
            pick.spinPickerWheelsForward();
        }
        
        // Stop the spinning of the picker wheels
        if (pickerWheelStopButtonEnabled & requestToStopPickerWheels()) {
            State.pickerWheelsMode = State.PICKER_WHEELS_STOPPED;
            pick.stopPickerWheels();
        }
        

        //************************************* MISCELLANEOUS **************************************
        if (calibrateButtonEnabled & requestToCalibrate()) {
            if (    (State.kickerMode != State.KICKER_CALIBRATING)
                 && (State.kickerMode != State.KICKING)
                 && (State.kickerMode != State.KICKER_STOPPING)
               ) {
                State.kickerMode = State.KICKER_CALIBRATING;
                calibrateButtonEnabled = false;
            }
        }
        if (State.kickerMode == State.KICKER_CALIBRATING) {
            if (shoot.setKickingPosition() == true) {
                State.kickerMode = State.KICKER_IN_READY_POSITION;
            }
        }
        
        updateDashboard();
    }

    /**
     *
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        shoot.manualShooterControl(-operatorInputs.xboxLeftY());
    }
    
    /**
     * reenableButtons()    
     * For many buttons used in the control of the robot, it's important that we react to the initial press of the button, 
     * but then ignore it until the operator releases it and presses it again in the future.
     * We'll use boolean variables, such as joystickTriggerEnabled, that will be disabled until the trigger is actually
     * released.  Then it will be re-enabled, and so a subsequent press of the trigger will allow the function to proceed.
     * 
     */
    private void reenableButtons() {
        if (!operatorInputs.joystickTriggerPressed()) { shifterTriggerEnabled              = true; }
        if (!requestToKick())                         { kickerButtonEnabled                = true; }
        if (!requestToMoveKickerToReady())            { kickerToReadyButtonEnabled         = true; }
        if (!requestToMovePickerToPick())             { pickerToPickButtonEnabled          = true; }
        if (!requestToMovePickerToKick())             { pickerToKickButtonEnabled          = true; }
        if (!requestToMovePickerToStartPosition())    { pickerToStartPositionButtonEnabled = true; }
        if (!requestToSpinPickerWheelsInReverse())    { pickerWheelReverseButtonEnabled    = true; }
        if (!requestToSpinPickerWheelsForward())      { pickerWheelForwardButtonEnabled    = true; }
        if (!requestToStopPickerWheels())             { pickerWheelStopButtonEnabled       = true; }
    }
    
    private void updateDashboard() {
        SmartDashboard.putNumber("kicker Motor Power",shoot.getKickerMotorPower());
        SmartDashboard.putBoolean("Is High Gear", drive.isHighGear);
        SmartDashboard.putNumber("Left Power Is", drive.leftPow);
        SmartDashboard.putNumber("Right Power Is", drive.rightPow);
        // SmartDashboard.putNumber("Left Encoder Value Is", drive.leftEncoderFix);
        // SmartDashboard.putNumber("Right Encoder Value Is", drive.rightEncoderFix);
        /*
          SmartDashboard.putBoolean("Is Picking", pick.isPicking);
          SmartDashboard.putBoolean("Is Pooting", pick.isPicking);
        */
        SmartDashboard.putString("Kicker Mode: ",        State.printKickerMode());
        SmartDashboard.putString("Picker Mode: ",        State.printPickerMode());
        SmartDashboard.putString("Picker Wheels Mode: ", State.printPickerWheelsMode());
        SmartDashboard.putString("Ball Location Mode: ", State.printBallLocationMode());

        SmartDashboard.putNumber("Speed", drive.totalSpeed);
        // SmartDashboard.putNumber("Kicker Angle", shoot.angle); *Don't need to display, not sure what will be displayed.
    }
    
    private boolean requestToMoveKickerToReady() {
        return (operatorInputs.isXboxLeftTriggerPressed());
    }
    
    private boolean requestToKick() {
        return (operatorInputs.isXboxRightTriggerPressed());
    }
    
    private boolean requestToMovePickerToPick() {
        return (operatorInputs.isXboxAButtonPressed());
    }
    
    private boolean requestToMovePickerToKick() {
        return (operatorInputs.isXboxBButtonPressed());
    }
    
    private boolean requestToMovePickerToStartPosition() {
        return (operatorInputs.isXboxYButtonPressed());
    }
    
    private boolean requestToSpinPickerWheelsInReverse() {
        return (operatorInputs.isXboxRightBumperPressed());
    }

    private boolean requestToSpinPickerWheelsForward() {
        return (operatorInputs.isXboxLeftBumperPressed());
    }
    
    private boolean requestToCalibrate() {
        return (operatorInputs.isXboxBackButtonPressed());
    }
    
    private boolean requestToStopPickerWheels() {
        return (operatorInputs.isXboxXButtonPressed());
    }
}
