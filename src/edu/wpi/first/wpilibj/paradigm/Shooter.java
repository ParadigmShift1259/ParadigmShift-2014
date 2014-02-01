/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.paradigm;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;

/**
 *
 * @author Programming
 */
public class Shooter {
    
    DriverControls operatorInputs;
    
    private final int PORT_5 = 5;
    private Joystick xBox = new Joystick(2);
    private Talon kickermotor = new Talon(PORT_5);
    private boolean buttonPressed;
    private final int A_BUTTON = 1;
    private double motorSpeed = 1.0;
    
    public Shooter(DriverControls _operatorInputs) {
        this.operatorInputs = _operatorInputs;
        
    }
    
    public void kick() {
        buttonPressed = xBox.getRawButton(A_BUTTON);
        if (buttonPressed = true) {
            kickermotor.set(motorSpeed);
        } else {
            kickermotor.set(0);
        }
        
    }
    
   //need to figure out moveable parts on the shooting mechanism before adding buttons/functions 
}
