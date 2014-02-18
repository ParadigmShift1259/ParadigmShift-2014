
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.paradigm;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
//import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;

/**
 *
 * @author Programming
 */
public class Picker {

    OperatorInputs operatorInputs;
    private Joystick xBox = new Joystick(2);
    private double pickPos = -1.00; //change value later, position while loading
    public double kickPos = 0.56; //change value later, position while shooting/aiming
    private double trussPos = 0.28; //change value later, position at the beginning of the auto/match
    private double currentAngle; //the picker's current pos(ition)
    private final int RIGHT_BUMPER = 6; //this is the x butt on the controller
    private final int BUTTON_LB = 5; //this is is the poot butt
    private boolean buttonPressed = false; //used to indicate if any button is pressed
    private boolean isKickPos = false;
    private boolean isPickPos = false;
    private boolean isTrussPos = false;
    private boolean isGrabbing = false;
    public static double KP_SOFT = 1.0;
    public static double KP_MEDIUM = 0.4;
    public static double KP_HARD = 1.3;

    public static double KI_SOFT = 0.005;
    public static double KI_MEDIUM = 0.03;
    public static double KI_HARD = 0.025;

    public static double KD_SOFT = 4.5;
    public static double KD_MEDIUM = 12.0;
    public static double KD_HARD = 2.5;
    
    private boolean isPooting = false;
    private Talon wheelSpinner = new Talon(3); //used in the SpinGrabber method...also is a Talon
    // private Talon pickerMotor = new Talon(4);
//    private final int pickerChannel = 2;
//    private final AnalogChannel analogChannel = new AnalogChannel(pickerChannel);
    private double pickerAngleVoltage;
    private double pickerAngleDegree;
    // private double MAX_ENCODER_VOLTAGE = 5.0;
    // private boolean settingPos1 = false;
    // private boolean settingPos2 = false;
    // private boolean settingPos3 = false;
    private final int A_BUTTON = 1;
    private final int B_BUTTON = 2;
    private final int Y_BUTTON = 4;
    boolean isPicking;
    private boolean isManual;

    public PickerPID pickerPID;

//boolean isPooting; already defined

    /*
     This is the constructor for the Picker class.
     */
    public Picker(OperatorInputs _operatorInputs, PickerPID pickerPid) {
        this.operatorInputs = _operatorInputs;
        pickerPID = pickerPid;
    }

    public void kick() {
        buttonPressed = operatorInputs.xBoxXButton();
        if (buttonPressed) {
            isKickPos = true;
            PickerPID.Kp = KP_SOFT;
            PickerPID.Ki = KI_MEDIUM;
            PickerPID.Kd = KD_MEDIUM;
            pickerPID.getPIDController().setPID(PickerPID.Kp, PickerPID.Ki, PickerPID.Kd);
            pickerPID.getPIDController().reset();
            pickerPID.enable();
            pickerPID.setSetpoint(kickPos);
            AdventureRick.shoot.moveToKickPos();
        } else if (!buttonPressed) {
            isKickPos = false;
            //?

        }
    }

    public void truss() {
        buttonPressed = xBox.getRawButton(Y_BUTTON);
        if (buttonPressed) {
            //System.out.println(pickerPID.getSetpoint());
            isTrussPos = true;
            if (pickerPID.getPosition() > trussPos) {
                //to move from kick
                PickerPID.Kp = KP_HARD;
                PickerPID.Ki = KI_HARD;
                PickerPID.Kd = KD_HARD;
            } else if (pickerPID.getPosition() == trussPos) {
            } else {
                // to move from pick
                PickerPID.Kp = KP_SOFT;
                PickerPID.Ki = KI_MEDIUM;
                PickerPID.Kd = KD_MEDIUM;
            }
            pickerPID.getPIDController().setPID(PickerPID.Kp, PickerPID.Ki, PickerPID.Kd);
            pickerPID.getPIDController().reset();
            pickerPID.enable();
            pickerPID.setSetpoint(trussPos);
            AdventureRick.shoot.moveToKickPos();
        } else if (!buttonPressed) {
            isTrussPos = false;
            //?

        }
    }

    public void pick() {
        buttonPressed = xBox.getRawButton(B_BUTTON);
        if (buttonPressed) {
            isPickPos = true;
            PickerPID.Kp = KP_MEDIUM;
            PickerPID.Ki = KI_SOFT;
            PickerPID.Kd = KD_SOFT;
            pickerPID.getPIDController().setPID(PickerPID.Kp, PickerPID.Ki, PickerPID.Kd);
            pickerPID.getPIDController().reset();
            pickerPID.enable();
            pickerPID.setSetpoint(pickPos);
            AdventureRick.shoot.moveToPickPos();
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
    public void spinGrabber() { //Aka suckySucky();
        buttonPressed = xBox.getRawButton(RIGHT_BUMPER);
        if (buttonPressed && !isPooting) { //Cannot commence when it is pooting(releasing)
            isGrabbing = true; //Boolean so it cannot Grab and Poot at the same time
            wheelSpinner.set(1);
        } else if (!buttonPressed && !isPooting) { //We don't want the motor stopping when it is pooting
            wheelSpinner.set(0);
            isGrabbing = false;
        }
    }

    /*
     This method controls the "pooter". The pooter will make the wheels 
     spin backwards in case the ball gets stuck inside of the picker.\
    
     Possible: May be used for a (weak) pass.
     */
    public void spinPooter() {
        buttonPressed = xBox.getRawButton(BUTTON_LB);
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
     buttonPressed = operatorInputs.isPickerKickingPositionButtonPressed();
     currentAngle = pickerPID.getPickerAngle();
     if (buttonPressed && !settingPos1 && !settingPos3) {
     settingPos2 = true;
     }
     if (settingPos2 = true) {
     if (currentAngle > kickPos) {
     pickerPID.set(-0.7);
     } else if (currentAngle < kickPos) {
     pickerPID.set(0.7);
     } else if (currentAngle == kickPos) {
     pickerPID.set(0);
     settingPos2 = false;
     }
     }
     }
     

     public void setPosAuto() {
     buttonPressed = xBox.getRawButton(Y_BUTTON);
     currentAngle = pickerPID.getPickerAngle();
     if (buttonPressed && !settingPos1 && !settingPos2) {
     settingPos3 = true;
     }
     if (settingPos3 = true) {
     if (currentAngle > trussPos) {
     pickerPID.set(-0.7);
     } else if (currentAngle < trussPos) {
     pickerPID.set(0.7);
     } else if (currentAngle == trussPos) {
     pickerPID.set(0);
     settingPos3 = false;
     }
     }
     }

     //need to figure out moveable parts on the picker in order to assign functions
     */
}
