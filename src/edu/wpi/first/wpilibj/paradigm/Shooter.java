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
    
    DriverControls operatorInputs;
    
    private final int PORT_5 = 5;
    //the current value can not possibly be the previous value the first time through
    private final double ILLEGAL_VOLTAGE = -9999.9; //can't be stopped when it hasn't started
    
    private final Joystick xBox = new Joystick(2);
    private final Talon kickermotor = new Talon(PORT_5);
    private boolean buttonPressed;
    private final int A_BUTTON = 1;
    private final int SELECT_BUTTON = 9;
    private final int RIGHT_BUMPER = 6;
    private double motorSpeed = 1.0;
    private final AnalogChannel analogChannel = new AnalogChannel(1);
    private final DigitalInput digitalInput = new DigitalInput(9);
    private double previousVoltage = ILLEGAL_VOLTAGE;
    private double currentVoltage;
    private boolean inPosition;
    public boolean caliButtPressed = true;
    private boolean kicking;
    private double kickingPos;
    private boolean found;
    private boolean pressed;
    private double angle;
    private final double MAX_ENCODER_VOLTAGE = 2.0;

    public Shooter(DriverControls _operatorInputs) {
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
    
    public boolean checkToKick() {
        buttonPressed = xBox.getRawButton(A_BUTTON);
        if (buttonPressed) {
            kicking = true;
            kickermotor.set(motorSpeed);
        } else {
            kicking = false;
            kickermotor.set(0);
        }
        return buttonPressed; //Return value of button to know whether the robot had just kicked.
    }
    
    public void calibrate() {
        inPosition = digitalInput.get();
        buttonPressed = xBox.getRawButton(SELECT_BUTTON);
        if (buttonPressed) {
            caliButtPressed = true;
            buttonPressed = false;
        }
        if (caliButtPressed) {
            if (inPosition) {
                kickermotor.set(0);
                caliButtPressed = false;
                found = true;
            } else {
                kickermotor.set(0.1);
            }
        }   
    }
    
    public void getKickerAngle() {
        angle = analogChannel.getVoltage();
       
        if (found) {
            //This is the porportion to convert voltage into a degrees angle.
            //There are 360 degree permax encoder voltage.
            kickingPos = angle * (360/MAX_ENCODER_VOLTAGE);
            
            found = false;
        }
    }
    
    public void setKickingPosition() {
        buttonPressed = xBox.getRawButton(RIGHT_BUMPER);
        if (buttonPressed){
            pressed = true;
            buttonPressed = false;
        }
        if (pressed && !kicking && !caliButtPressed){
            inPosition = digitalInput.get();
            try {
                if (inPosition) {
                    kickermotor.set(0);
                    pressed = false;
                } else {
                    kickermotor.set(0.1);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
   //need to figure out moveable parts on the shooting mechanism before adding buttons/functions 
}
