
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.paradigm;

//import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
//import edu.wpi.first.wpilibj.Encoder;
//import edu.wpi.first.wpilibj.AnalogChannel;
//import edu.wpi.first.wpilibj.PIDController;
//import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author Programming
 */
public class Picker {

    OperatorInputs operatorInputs;
    //private Joystick xBox = new Joystick(2);
    private boolean wasKick = false;
    private static final double LOCK_COEF  = 1.0;
    private double pickPos = -1.00; //change value later, position while loading
    public double kickPos = 0.52; //change value later, position while shooting/aiming
    private double middlePos = 0.28; //change value later, position at the beginning of the auto/match
    private double currentAngle; //the picker's current pos(ition)
    private boolean buttonPressed = false; //used to indicate if any button is pressed
    private boolean buttonPreviouslyPressed = false;
    private boolean isKickPos = false;
    private boolean isPickPos = false;
    private boolean isMiddlePos = false;
    private boolean isGrabbing = false;
    public static double KP_SOFT = 1.0;
    public static double KP_MEDIUM = 0.4;
    public static double KP_HARD = 1.3;

    public static double KI_SOFT = 0.01;
    public static double KI_MEDIUM = 0.03;
    public static double KI_HARD = 0.025;

    public static double KD_SOFT = 6.0;
    public static double KD_MEDIUM = 12.0;
    public static double KD_HARD = 2.5;

    private boolean isPooting = false;
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

    /**
     * This is the constructor for the Picker class.
     */
    public Picker(OperatorInputs _operatorInputs, PickerPID pickerPid) {
        this.operatorInputs = _operatorInputs;
        pickerPID = pickerPid;
    }

    public double getVoltage() {
        return pickerPID.getVoltage();
    }

    public void emergencyDisablePid() {
        if (operatorInputs.xBoxAButton()) {
            pickerPID.disable();
        }
    }

    public void kick() {
        buttonPressed = operatorInputs.xBoxXButton();
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

    public void middle() {
        buttonPressed = operatorInputs.xBoxYButton();
        if (buttonPressed) {
            pickerPID.enable();
            //System.out.println(pickerPID.getSetpoint());
            isMiddlePos = true;
            if (pickerPID.getPosition() > middlePos) {
                //to move from kick
                PickerPID.Kp = KP_HARD;
                PickerPID.Ki = KI_HARD;
                PickerPID.Kd = KD_HARD;
            } else if (pickerPID.getPosition() == middlePos) {
            } else {
                // to move from pick
                PickerPID.Kp = KP_SOFT;
                PickerPID.Ki = KI_MEDIUM;
                PickerPID.Kd = KD_MEDIUM;
            }
            pickerPID.getPIDController().setPID(PickerPID.Kp, PickerPID.Ki, PickerPID.Kd);
            pickerPID.getPIDController().reset();
            pickerPID.enable();
            pickerPID.setSetpoint(middlePos);
        } else if (!buttonPressed) {
            isMiddlePos = false;
            //?

        }
    }

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
            //?

        }
    }

    /*
     This method spins the picker wheels when the X button is pressed.
     The wheels will load the ball into the picker.
     */
    /*
     BELOW STUFF WORKS NOW
     */
//    public void spinGrabber() { //Aka suckySucky();
//        buttonPressed = operatorInputs.xBoxRightBumper();
//        if (buttonPressed && !isPooting) { //Cannot commence when it is pooting(releasing)
//            isGrabbing = true; //Boolean so it cannot Grab and Poot at the same time
//            wheelSpinner.set(1);
//        } else if (!buttonPressed && !isPooting && !grabberOverride) { //We don't want the motor stopping when it is pooting
//            wheelSpinner.set(0);
//            isGrabbing = false;
//        }
//    }
     public void spinGrabber() { //Aka suckySucky();
        buttonPressed = operatorInputs.xBoxRightBumper();
        if (buttonPressed && !buttonPreviouslyPressed) { //Cannot commence when it is pooting(releasing)
            if (!isGrabbing) {
                isGrabbing = true; //Boolean so it cannot Grab and Poot at the same time
                wheelSpinner.set(1);
            } else {
                isGrabbing = false;
                wheelSpinner.set(0);
            }
        }
        buttonPreviouslyPressed = buttonPressed;
    }
    

    /*
     This method controls the "pooter". The pooter will make the wheels 
     spin backwards in case the ball gets stuck inside of the picker.\
    
     Possible: May be used for a (weak) pass.
     */
    public void spinPooter() {
        buttonPressed = operatorInputs.xBoxLeftBumper();
        if (buttonPressed && !isGrabbing) { //Cannot poot and grab at the same time
            isPooting = true; //Boolean for so it can not poot and grab at the same time
            wheelSpinner.set(-1);
        } else if (!buttonPressed && !isGrabbing) {
            wheelSpinner.set(0);
            isPooting = false;
        }
    }
    /*
     ABOVE STUFF WORKS
     */

//    public double getPickerAngle() {
//        pickerAngleVoltage = analogChannel.getVoltage(); //comment
//        pickerAngleDegree = pickerAngleVoltage * (360 / MAX_ENCODER_VOLTAGE); //Converts Voltage to degrees
//        return pickerAngleDegree;
//        //return pickerAngleVoltage;
//    }
    /*
     public void setPosLoading() { //loadPos = 346
     //System.out.println("Beginning of method");
     buttonPressed = xBox.getRawButton(5);
     //currentAngle = getPickerAngle();
     double err = currentAngle - pickPos;//move to class level if needed
     //pickerMotor.set(0.0);
     if (buttonPressed //&& !settingPos2 && !settingPos3) { //Cannot set two at once
     //System.out.println("Button pressed");
     settingPos1 = true;    //Set boolean so you don't have to hold the button down
     }
     if (settingPos1 == true) {
     //            if (err < 5) {
     //                pickerMotor.set(0);
     //                settingPos1 = false;
     //            } else 
     //               if (currentAngle < loadPos) {
     pickerPID.setSetpoint(PickerPID.LOAD_ANGLE);
     //System.out.println("err: " + err);
     //System.out.println("Speed: " + pickerMotor.get());
     //           } else if (currentAngle > loadPos) {
     //               pickerMotor.set(0.51);
     //            if (currentAngle == loadPos) {
     //                settingPos1 = false;
     //                pickerMotor.set(0.0);
            
     //            }
     //          }
            
     }
     //System.out.println("End of method");
     }
     */
    public void manualPickerControl() {

        if (!settingPos1 && !settingPos2 && !settingPos3 && Math.abs(operatorInputs.xboxLeftY()) > 0) {
            //pickerPID.disable();

            pickerPID.set(-operatorInputs.xboxLeftY()); //Y-axis is up negative, down positive; Map Y-axis up to green, Y-axis down to red
            //System.out.println("TALON_OUT " + pickerPID.getTalonValue());
            //pickerPID.getPIDController().reset();
            isManual = true;
        } else if (isManual && operatorInputs.xboxLeftY() == 0) {
            isManual = false;
            pickerPID.set(0);
        }
        //System.out.println("controllerXboxY " + operatorInputs.xboxLeftY());
    }

    public void setPosKicking() {
        buttonPressed = operatorInputs.xBoxXButton();
        // currentAngle = pickerPID.getPickerAngle();
        if (buttonPressed && !settingPos1 && !settingPos3) {
            wasKick = false;
            settingPos2 = true;
        //    grabberOverride = true;
         //   wheelSpinner.set(1);
            pickerPID.disable();
        }
        if (settingPos2) {
            if (getVoltage() - STEP > kickPos) {
                pickerPID.set(0.7);
            } else if (getVoltage() + STEP < kickPos) {
                pickerPID.set(-0.7);
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

    public void setPosAuto() {
        buttonPressed = operatorInputs.xBoxYButton();
        // currentAngle = pickerPID.getPickerAngle();
        if (buttonPressed && !settingPos1 && !settingPos2) {
            settingPos3 = true;
        }
        if (settingPos3 = true) {
            if (currentAngle > middlePos) {
                pickerPID.set(-0.7);
            } else if (currentAngle < middlePos) {
                pickerPID.set(0.7);
            } else if (currentAngle == middlePos) {
                pickerPID.set(0);
                settingPos3 = false;
            }
        }
    }

     //need to figure out moveable parts on the picker in order to assign functions
}
