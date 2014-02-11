
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.paradigm;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.AnalogChannel;

/**
 *
 * @author Programming
 */
public class Picker {

    private int LOADING_POSITION_ANGLE = 135; //change value later, position while loading
    private int KICKING_POSITION_ANGLE = 80; //change value later, position while shooting/aiming
    private int INITIAL_POSITION_ANGLE = 45; //change value later, position at the beginning of the auto/match
    private double currentAngle; //the picker's current pos(ition)
    private boolean buttonPressed = false; //used to indicate if any button is pressed
    private boolean isGrabbing = false;
    private boolean isPooting = false;
    private Talon wheelSpinner = new Talon(8); //used in the SpinGrabber method...also is a Talon
    private Talon pickerMotor = new Talon(4);
    private final AnalogChannel analogChannel = new AnalogChannel(4);
    private double pickerAngleVoltage;
    private double pickerAngleDegree;
    private double MAX_ENCODER_VOLTAGE = 2.0;
    private boolean settingPos1 = false;
    private boolean settingPos2 = false;
    private boolean settingPos3 = false;
    
    /*
    This is the constructor for the Picker class.
    */
    
    public Picker() {
        
    }
    
    /*
    This method spins the picker wheels when the X button is pressed.
    The wheels will load the ball into the picker.
    */
    
    public void spinGrabber() {
        buttonPressed = xBox.getRawButton(RIGHT_BUMPER);
        if (buttonPressed && !isPooting) {
            isGrabbing = true;
            wheelSpinner.set(0.2);
        } else if (!buttonPressed && !isPooting){
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
        if (buttonPressed && !isGrabbing) {
            isPooting = true;
            wheelSpinner.set(-0.3);
        } else if (!buttonPressed && !isGrabbing){
            wheelSpinner.set(0);
            isPooting = false;
        }
    }
    
    
    
    public double getKickerAngle() {
        pickerAngleVoltage = analogChannel.getVoltage();
        pickerAngleDegree = pickerAngleVoltage * (360/MAX_ENCODER_VOLTAGE);
        return pickerAngleDegree;
    } 
    
    public boolean setPosLoading() {
        currentAngle = getKickerAngle();
        boolean inPosition = false;
        
        if (currentAngle == LOADING_POSITION_ANGLE) {  // change this to include some range
            pickerMotor.set(0);
            inPosition = true;
        } else if (currentAngle > LOADING_POSITION_ANGLE) {
            pickerMotor.set(-0.2);
        } else if (currentAngle < LOADING_POSITION_ANGLE) {
            pickerMotor.set(0.2);
        }

        return (inPosition);
    }
    
    public boolean setPosKicking() {
        currentAngle = getKickerAngle();
        boolean inPosition = false;
        
        if (currentAngle == KICKING_POSITION_ANGLE) {  // change this to include some range
            pickerMotor.set(0);
            inPosition = true;
        } else if (currentAngle > KICKING_POSITION_ANGLE) {
            pickerMotor.set(-0.2);
        } else if (currentAngle < KICKING_POSITION_ANGLE) {
            pickerMotor.set(0.2);
        }

        return (inPosition);
    }
   
    public void setPosAuto() {
       buttonPressed = xBox.getRawButton(Y_BUTTON);
       currentAngle = getKickerAngle();
       if(buttonPressed) {
           settingPos3 = true;    
       }
       if(settingPos3 = true) {
           if(currentAngle > autoPos) {
               pickerMotor.set(-0.2);
           }
           if(currentAngle < autoPos) {
               pickerMotor.set(0.2);
           }
           if(currentAngle == autoPos) {
               pickerMotor.set(0);
               settingPos3 = false;
           }
       }
    }
    //need to figure out moveable parts on the picker in order to assign functions
    
}
