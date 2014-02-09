/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.paradigm;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DigitalInput;

/**
 *
 * @author Doug Dimmadome
 */
public class Shooter {
    
    OperatorInputs operatorInputs;
    
    private final int PORT_5 = 5;
    //the current value can not possibly be the previous value the first time through
    private final double ILLEGAL_VOLTAGE = -9999.9; //can't be stopped when it hasn't started
    
    private final Joystick xBox = new Joystick(2);
    private final Talon kickermotor = new Talon(PORT_5);
    private boolean buttonPressed;
    //private double triggerPressed;
    //put in place for testing on Sturday night 2/8/2014 - E A COBB
        private boolean triggerPressed; // changed for testing on Saturday night
        private final double LEFT_TRIGGER_PRESSED_MAX_VALUE = 1.0;
        private final double LEFT_TRIGGER_PRESSED_MIN_VALUE = 0.5;
        private final double RIGHT_TRIGGER_PRESSED_MAX_VALUE = -0.5;
        private final double RIGHT_TRIGGER_PRESSED_MIN_VALUE = -1.0;
    //private final Joystick.AxisType RIGHT_TRIGGER = Joystick.AxisType.kZ;
    private final int BACK_BUTTON = 7;
    private final Joystick.AxisType XBOX_TRIGGERS = Joystick.AxisType.kZ; //renamed because this is both the left trigger and the right trigger
    private double motorSpeed = 1.0;
    private final AnalogChannel analogChannel = new AnalogChannel(1);
    private final DigitalInput digitalInput = new DigitalInput(9);
    private double previousVoltage = ILLEGAL_VOLTAGE;
    private double currentVoltage;
    //private 
    boolean inPosition; //made protected for use in AdventureRick
    private boolean caliButtPressed = true;
    boolean kicking;
    private double kickingPos;
    double angle;
    private boolean pressed;
    private final double MAX_ENCODER_VOLTAGE = 2.0;

    public Shooter(OperatorInputs _operatorInputs) {
        this.operatorInputs = _operatorInputs;       
    }
    
    public boolean isKickerStopped() {
        currentVoltage = analogChannel.getVoltage(); //Read current voltage
        if (previousVoltage == currentVoltage) { 
            previousVoltage = ILLEGAL_VOLTAGE; //Set back so it can run again 
            return true;
        } else {
            previousVoltage = currentVoltage; //current becomes previous
            return false;
        }
        
    }
    
    /*
    This method is used to kick.
    
    P.S. It has a dumb name that can go to suckySucky().
    */
    
    public void kick() {
        triggerPressed = RIGHT_TRIGGER_PRESSED_MIN_VALUE <= xBox.getAxis(XBOX_TRIGGERS) && 
                xBox.getAxis(XBOX_TRIGGERS) <= RIGHT_TRIGGER_PRESSED_MAX_VALUE; //changed for testing on Sturday night 2/8/2014 - E A COBB
        inPosition = digitalInput.get();
        if (triggerPressed){ //changed for testing on Sturday night 2/8/2014 - E A COBB
            kicking = true;
            buttonPressed = false;
        }
        if (kicking) {
            kickermotor.set(0.7);
            if (inPosition) {
                kicking = false;
                kickermotor.set(0);
            }
        }
    }
    
    public void calibrate() {
        inPosition = digitalInput.get();
        buttonPressed = xBox.getRawButton(BACK_BUTTON);
        if (buttonPressed) {
            caliButtPressed = true;
            buttonPressed = false;
        }
        if (caliButtPressed) {
            try {
                if (inPosition) {
                    kickermotor.set(0);
                    caliButtPressed = false;
                } else {
                    if (angle > kickingPos && angle <= 165) {
                        kickermotor.set(0.1);
                    } else if (angle > kickingPos || angle <= 145){
                        kickermotor.set(-0.1);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }   
    }
    
    public double getKickerAngle() {
        angle = analogChannel.getVoltage();
        //This is the porportion to convert voltage into a degrees angle.
        //There are 360 degree permax encoder voltage.
        kickingPos = angle * (360/MAX_ENCODER_VOLTAGE);
        return angle;
    }
    
    
    public void setKickingPosition() {
        triggerPressed = LEFT_TRIGGER_PRESSED_MIN_VALUE <= xBox.getAxis(XBOX_TRIGGERS) && 
                xBox.getAxis(XBOX_TRIGGERS) <= LEFT_TRIGGER_PRESSED_MAX_VALUE; //changed for testing on Sturday night 2/8/2014 - E A COBB
        if (triggerPressed){ //changed for testing on Sturday night 2/8/2014 - E A COBB
            pressed = true;
            buttonPressed = false;
        }
        if (pressed && !kicking && !caliButtPressed){
            if (getKickerAngle() == kickingPos) {
                inPosition = true;
            }
            try {
                if (inPosition) {
                    kickermotor.set(0);
                    pressed = false;
                } else {
                    if (angle > kickingPos && angle <= 165) {
                        kickermotor.set(0.1);
                    } else if (angle > kickingPos || angle <= 145){
                        kickermotor.set(-0.1);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
   //need to figure out moveable parts on the shooting mechanism before adding buttons/functions 
}
