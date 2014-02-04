/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.paradigm;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.AnalogChannel;

/**
 *
 * @author Doug Dimmadome
 */
public class Shooter {
    
    DriverControls operatorInputs;
    
    private final int PORT_5 = 5;
    //the current value can not possibly be the previous value the first time through
    private final double ILLEGAL_VOLTAGE = -9999.9; 
    
    private Joystick xBox = new Joystick(2);
    private Talon kickermotor = new Talon(PORT_5);
    private boolean buttonPressed;
    private final int A_BUTTON = 1;
    private double motorSpeed = 1.0;
    private AnalogChannel analogChannel = new AnalogChannel(1);
    private double previousVoltage = ILLEGAL_VOLTAGE;
    private double currentVoltage;
    
    
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
        
    
    public boolean checkToKick() {
        buttonPressed = xBox.getRawButton(A_BUTTON);
        if (buttonPressed = true) {
            kickermotor.set(motorSpeed);
        } else {
            kickermotor.set(0);
        }
        return buttonPressed; //Return value of button to know whether the robot had just kicked.
    }
    
    
   //need to figure out moveable parts on the shooting mechanism before adding buttons/functions 
}
