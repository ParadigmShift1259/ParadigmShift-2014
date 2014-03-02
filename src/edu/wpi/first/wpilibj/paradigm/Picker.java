
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.paradigm;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author Programming
 */
public class Picker {

    OperatorInputs operatorInputs;
    private boolean wasKick = false;
    private static final double LOCK_COEF = 1.0;
    private final double pickPos = -1.00; //change value later, position while loading
    public double kickPos = 0.45; //change value later, position while shooting/aiming
    private final double middlePos = 0.28; //change value later, position at the beginning of the auto/match
    private double currentAngle; //the picker's current pos(ition)
    private boolean buttonPressed = false; //used to indicate if any button is pressed
    private boolean buttonPreviouslyPressed = false;
    //private boolean isKickPos = false;
    private boolean isPickPos = false;
    private boolean isGrabbing = false;
    public static boolean isKickingNow = false;
    public static double KP_SOFT = 1.0;
    public static double KP_MEDIUM = 0.4;
    public static double KP_HARD = 1.3;

    public static double KI_SOFT = 0.01;
    public static double KI_MEDIUM = 0.03;
    public static double KI_HARD = 0.025;

    public static double KD_SOFT = 6.0;
    public static double KD_MEDIUM = 12.0;
    public static double KD_HARD = 2.5;
    private Talon wheelSpinner = new Talon(3); //used in the SpinGrabber method...also is a Talon

    // private Talon pickerMotor = new Talon(4);
//    private final int pickerChannel = 2;
//    private final AnalogChannel analogChannel = new AnalogChannel(pickerChannel);
    private double pickerAngleVoltage;
    private double pickerAngleDegree;
    private double MAX_ENCODER_VOLTAGE = 5.0;
    private boolean settingPos1 = false;
    private boolean settingPos2 = false;
    private boolean settingPos3 = false;
    private final int A_BUTTON = 1; // EAC.2014.02.19 - may benefit in compile-size by being static
    private final int B_BUTTON = 2; // EAC.2014.02.19 - may benefit in compile-size by being static
    private final int Y_BUTTON = 4; // EAC.2014.02.19 - may benefit in compile-size by being static
    boolean isPicking;
    private boolean isManual;
    private static final Timer timer = new Timer();
    private static final double PID_DISABLE_TOLERANCE = 0.4;
    private static final double STEP = 0.9;
    private boolean grabberOverride = false;

    public PickerPID pickerPID;
    public Shooter shoot;

    /**
     * This is the constructor for the Picker class.
     */
    public Picker(OperatorInputs _operatorInputs, PickerPID _pickerPid, Shooter shoot) {
        this.operatorInputs = _operatorInputs;
        pickerPID = _pickerPid;
        this.shoot = shoot;
        //shooter = new Shooter(_operatorInputs);
    }

    public double getVoltage() {
        return pickerPID.getVoltage();
    }

    public void emergencyDisablePid() {
        if (operatorInputs.xBoxAButton()) {
            pickerPID.disable();
        }
    }
/*
    public void kick() {
        buttonPressed = operatorInputs.xBoxXButton();
        if (settingPos2 && pickerPID.getPIDController().onTarget()) {
            settingPos2 = false;
            pickerPID.disable();
            if (buttonPressed) {
                pickerPID.enable();
                isKickPos = true;
                PickerPID.Kp = KP_SOFT;
                PickerPID.Ki = KI_MEDIUM;
                PickerPID.Kd = KD_MEDIUM;
                pickerPID.getPIDController().setPID(PickerPID.Kp, PickerPID.Ki, PickerPID.Kd);
                pickerPID.getPIDController().reset();
                pickerPID.enable();
                pickerPID.setSetpoint(kickPos);
            } else if (!buttonPressed) {
                isKickPos = false;
                //?

            }
        }
    }
*/

    public void inPositionDisable() {
        if (pickerPID.getPIDController().getSetpoint() > pickerPID.getVoltage() - PID_DISABLE_TOLERANCE && pickerPID.getPIDController().getSetpoint() < pickerPID.getVoltage() + PID_DISABLE_TOLERANCE) {
            pickerPID.disable();
        }
    }

    public void pick() {
        buttonPressed = operatorInputs.xBoxBButton();
        if (settingPos1 && pickerPID.getPIDController().onTarget()) {
            settingPos1 = false;
            pickerPID.disable();
        }
        if (buttonPressed) {
            shoot.getPID().prepPick();
            wasKick = false;
            pickerPID.enable();
            settingPos1 = true;
            isPickPos = true;
            PickerPID.Kp = KP_MEDIUM;
            PickerPID.Ki = KI_SOFT;
            PickerPID.Kd = KD_SOFT;
            pickerPID.getPIDController().setPID(PickerPID.Kp, PickerPID.Ki, PickerPID.Kd);
            pickerPID.getPIDController().reset();
            pickerPID.enable();
            pickerPID.setSetpoint(pickPos);
        } else if (!buttonPressed) {
            isPickPos = false;

        }
    }

    public void spinGrabber() {
        buttonPressed = operatorInputs.xBoxRightBumper();
        if (buttonPressed && !buttonPreviouslyPressed) { //Cannot commence when it is releasing
            if (!isGrabbing) {
                isGrabbing = true; //Boolean so it cannot Grab and Release at the same time
                wheelSpinner.set(1);
            } else {
                isGrabbing = false;
                wheelSpinner.set(0);
            }
        }
        buttonPreviouslyPressed = buttonPressed;
    }


    /*
     This method controls the "releaser". The releaser will make the wheels 
     spin backwards in case the ball gets stuck inside of the picker.\
    
     Possible: May be used for a (weak) pass.
     */
    public void spinReleaser() {
        buttonPressed = operatorInputs.xBoxLeftBumper();
        if (buttonPressed) { //Cannot release and grab at the same time
            isGrabbing = false;
            wheelSpinner.set(-1);
        } else if (!buttonPressed && !isGrabbing) {
            wheelSpinner.set(0);
        }
    }
    /*
    public void manualPickerControl() {

        if (!settingPos1 && !settingPos2 && !settingPos3 && Math.abs(operatorInputs.xboxRightY()) > 0) {
            pickerPID.set(-operatorInputs.xboxRightY()); //Y-axis is up negative, down positive; Map Y-axis up to green, Y-axis down to red
            isManual = true;
        } else if (isManual && operatorInputs.xboxRightY() == 0) {
            isManual = false;
            pickerPID.set(0);
        }
    }
    */

    public void setPosKicking() {
        System.out.println("setPosKicking called");
        buttonPressed = operatorInputs.xBoxXButton();
        if (buttonPressed && !settingPos1 && !settingPos3) {
            wasKick = false;
            settingPos2 = true;
            System.out.println("picker kick set called");
            pickerPID.disable();
        }
        if (settingPos2) {
            if (getVoltage() - STEP > kickPos) {
                pickerPID.set(0.8);
            } else if (getVoltage() + STEP < kickPos) {
                pickerPID.set(-0.8);
            } else if (getVoltage() - PID_DISABLE_TOLERANCE > kickPos) {
                pickerPID.set(-0.1);
            } else if (getVoltage() + PID_DISABLE_TOLERANCE < kickPos) {
                pickerPID.set(0.1);
            } else {
                pickerPID.set(0);
                settingPos2 = false;
                isGrabbing = false;
                wheelSpinner.set(0);
                wasKick = true;
                shoot.getPID().prepKickx();
                System.out.println("picker pick set called");
            }
        }
    }

    public void lockKick() {
        if (wasKick) {
            double error = getVoltage() - kickPos;
            double setSpeed = error * LOCK_COEF;
            if (setSpeed > 0.3) {
                setSpeed = 0.3;
            } else if (setSpeed < -0.3) {
                setSpeed = -0.3;
            }
            pickerPID.set(setSpeed);
        }
    }
     //need to figure out moveable parts on the picker in order to assign functions
}
