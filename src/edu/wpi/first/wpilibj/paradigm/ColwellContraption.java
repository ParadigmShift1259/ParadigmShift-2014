/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.paradigm;

import edu.wpi.first.wpilibj.Solenoid;

/**
 *
 * @author Programming
 */
public class ColwellContraption {
    /*this the pneumatic piston that keep the picker arm up for autonomous, can be toggled in teleop,
     need to add it to autonomous code to deactivate. Also just needs to be tested and the solenoid 
     port needs to be changed to the actual one and not just the current placeholder value - 2/18/14 (Allison)
     */

    Solenoid colwellContraptionUp;//NEED ACTUAL SOLENOID NUMBER HERE THIS IS A PLACEHOLDER - 2/18/14 (Allison
    Solenoid colwellContraptionDown;
    //4
    public ColwellContraption(){
        colwellContraptionDown =  new Solenoid(1,4);
        colwellContraptionUp = new Solenoid(1,3);
    }
    
    public void pistonUp() {
        colwellContraptionUp.set(true);
        colwellContraptionDown.set(false);
    }
    public void pistonDown(){
        colwellContraptionUp.set(false);
        colwellContraptionDown.set(true);
        
    }
}
