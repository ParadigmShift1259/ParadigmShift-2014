
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.paradigm;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Programming
 */
public class Picker {

    OperatorInputs operatorInputs;
    private boolean wasKick = false;
    private static final double LOCK_COEF = 1.0;
    private final double pickPos = -0.86; //change value later, position while loading
    public double kickPos = 0.55; //change value later, position while shooting/aiming
    private boolean buttonPressed = false; //used to indicate if any button is pressed
    private boolean buttonPreviouslyPressed = false;
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
    private boolean settingPos1 = false;
    private boolean settingPos2 = false;
    private boolean settingPos3 = false;
    boolean isPicking;
    private boolean isManual = false;
    private  double PID_DISABLE_TOLERANCE = 0.15;
    private  double STEP = .6;
    private boolean grabberOverride = false;

    public PickerPID pickerPID;
    public Shooter shoot;
    public Talon wheelSpinner = new Talon(3);
    Preferences prefs = Preferences.getInstance();
    //public double setPosKickingCase1 = 0.8;
    public double setPosKickingCase2 = -0.9;
    public double setPosKickingCase3 = 0.3;
    public double setPosKickingCase4 = 0.3;
    public int cycleCount = 0;
    
    /**
     * This is the constructor for the Picker class.
     */
    public Picker(OperatorInputs _operatorInputs, PickerPID _pickerPid, Shooter shoot) {
        this.operatorInputs = _operatorInputs;
        pickerPID = _pickerPid;
        this.shoot = shoot;
        
    }
    
//    public Picker(OperatorInputs _operatorInputs){
//        this.operatorInputs = _operatorInputs;
//    }
    
    
    public void getStepValues(){
        //STEP =  prefs.getDouble("STEP", STEP);
        //PID_DISABLE_TOLERANCE = prefs.getDouble("PID_DISABLE_TOLERANCE", PID_DISABLE_TOLERANCE);
        //setPosKickingCase1 = prefs.getDouble("setPosKickingCase1",setPosKickingCase1);
        //setPosKickingCase2 = prefs.getDouble("setPosKickingCase2",setPosKickingCase2);
        //setPosKickingCase3 = prefs.getDouble("setPosKickingCase3",setPosKickingCase3);
        //setPosKickingCase4 = prefs.getDouble("setPosKickingCase4",setPosKickingCase4);
    }
    
    public Talon returnSpinner() {
        return wheelSpinner;
    }

    public double getVoltage() {
        return pickerPID.getVoltage();
    }

    public void emergencyDisablePid() {
        if (operatorInputs.xBoxAButton()) {
            pickerPID.disable();
            settingPos2 = false;
            pickerPID.pickerMotor.set(0.0);
            isManual = true;
        }
        if(isManual){
            manualPickerControl();
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
            isManual = false;
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
        if ( buttonPressed || isKickingNow ) { //Cannot release and grab at the same time
            isGrabbing = false;
            wheelSpinner.set(-1);
        } else if (!isGrabbing) {
            wheelSpinner.set(0);
        }
    }

    public void manualPickerControl() {
        //check pos's - duluth
        if (!settingPos1 && !settingPos2 && !settingPos3 && Math.abs(operatorInputs.xboxRightY()) > 0) {
            pickerPID.disable();
            pickerPID.set(-operatorInputs.xboxRightY()); //Y-axis is up negative, down positive; Map Y-axis up to green, Y-axis down to red
            isManual = true;
        } else if (isManual && operatorInputs.xboxRightY() == 0) {
            isManual = false;
            pickerPID.set(0);
        }
    }

    public void setPosKicking() {
        buttonPressed = operatorInputs.xBoxXButton();
        if (buttonPressed && !settingPos1 && !settingPos3) {
            wasKick = false;
            settingPos2 = true;
            isManual = false;
            pickerPID.disable();
        }
        if (settingPos2) {
            
            
            /*if (getVoltage() - STEP > kickPos) {
                pickerPID.set(setPosKickingCase1);
            } else */
            if (getVoltage() + STEP < kickPos) {
                System.out.println("Case 1: " + setPosKickingCase2);
                System.out.println("Voltage 1a: " + (getVoltage() + STEP));
                System.out.println("Voltage 1b: " + getVoltage());
                System.out.println("Voltage 1c: " + kickPos);
                pickerPID.set(setPosKickingCase2);
            } else if (getVoltage() + PID_DISABLE_TOLERANCE < kickPos) {
                System.out.println("Case 2: " + setPosKickingCase4);
                System.out.println("Voltage 2a: " + (getVoltage() + PID_DISABLE_TOLERANCE));
                System.out.println("Voltage 2b: " + getVoltage());
                System.out.println("Voltage 2c: " + kickPos);
                pickerPID.set(setPosKickingCase4);
                shoot.getPID().prepKickx();
            } else {
                System.out.println("Case 3: " + 0);
                System.out.println("Voltage 3a: " + (getVoltage() + PID_DISABLE_TOLERANCE));
                System.out.println("Voltage 3b: " + getVoltage());
                System.out.println("Voltage 3c: " + kickPos);
                pickerPID.set(0);
                settingPos2 = false;
                isGrabbing = false;
                wheelSpinner.set(0);
                wasKick = true;
                shoot.getPID().prepKickx();
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

}
