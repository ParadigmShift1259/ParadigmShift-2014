
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.paradigm;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.AnalogChannel;

/**
 *
 * @author Programming
 */
public class Picker {

    private final int    LOADING_POSITION_ANGLE    = 135; //change value later, position while loading
    private final int    KICKING_POSITION_ANGLE    = 80;  //change value later, position while shooting/aiming
    private final int    INITIAL_POSITION_ANGLE    = 45;  //change value later, position at the beginning of the auto/match
    private final double NORMAL_PICKER_SPEED       = 0.2;
    private final double NORMAL_PICKER_WHEEL_SPEED = 0.2;
    private final double MAX_ENCODER_VOLTAGE       = 2.0; //maximum value expected from output of encoder/filter circuit
            
    private final Talon wheelSpinner = new Talon(4);   //used in the SpinGrabber method...also is a Talon
    private final Talon pickerMotor = new Talon(3);
    private final AnalogChannel analogChannel = new AnalogChannel(4);
    private double pickerAngleVoltage;
    private double pickerAngleDegree;
    
    /*
    This is the constructor for the Picker class.
    */
    
    public Picker() {
        
    }
    
    /**
     *  The wheels will load the ball into the picker.
     */
    public void spinPickerWheelsReverse() {
        wheelSpinner.set(NORMAL_PICKER_WHEEL_SPEED);
    }
    
    /**
     * This method spins the picker wheels in the forward direction to either
     * release or pass the ball. 
     * May be used for a (weak) pass.
    */
    public void spinPickerWheelsForward() {
        wheelSpinner.set(-NORMAL_PICKER_WHEEL_SPEED);
    }
    
    public void stopPickerWheels() {
        wheelSpinner.set(0);
    }
    
    public double getKickerAngle() {
        pickerAngleVoltage = analogChannel.getVoltage();
        pickerAngleDegree = pickerAngleVoltage * (360/MAX_ENCODER_VOLTAGE);
        return pickerAngleDegree;
    } 
    
    public boolean setPosLoading() {
        double currentAngle = getKickerAngle();
        boolean inPosition = false;
        
        if (currentAngle == LOADING_POSITION_ANGLE) {  // change this to include some range
            pickerMotor.set(0);
            inPosition = true;
        } else if (currentAngle > LOADING_POSITION_ANGLE) {
            pickerMotor.set(-NORMAL_PICKER_SPEED);
        } else if (currentAngle < LOADING_POSITION_ANGLE) {
            pickerMotor.set(NORMAL_PICKER_SPEED);
        }

        return (inPosition);
    }
    
    public boolean setPosKicking() {
        double currentAngle = getKickerAngle();
        boolean inPosition = false;
        
        if (currentAngle == KICKING_POSITION_ANGLE) {  // change this to include some range
            pickerMotor.set(0);
            inPosition = true;
        } else if (currentAngle > KICKING_POSITION_ANGLE) {
            pickerMotor.set(-NORMAL_PICKER_SPEED);
        } else if (currentAngle < KICKING_POSITION_ANGLE) {
            pickerMotor.set(NORMAL_PICKER_SPEED);
        }

        return (inPosition);
    }
   
    public boolean setPosAuto() {
        double currentAngle = getKickerAngle();
        boolean inPosition = false;
        
        if (currentAngle == KICKING_POSITION_ANGLE) {  // change this to include some range
            pickerMotor.set(0);
            inPosition = true;
        } else if (currentAngle > KICKING_POSITION_ANGLE) {
            pickerMotor.set(-NORMAL_PICKER_SPEED);
        } else if (currentAngle < KICKING_POSITION_ANGLE) {
            pickerMotor.set(NORMAL_PICKER_SPEED);
        }

        return (inPosition);
    }
    
}
