
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.paradigm;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Encoder;

/**
 *
 * @author Programming
 */
public class Picker {
    
    DriverControls operatorInputs;
    int loadPos = 757; //change value later, position wile loading
    int shootPos = 2136; //change value later, position while shooting/aiming
    int autoPos = 2048; //change value later, position at the beginning of the auto/match
    int currentPos; //the picker's current pos(ition)
    
    public Picker(DriverControls _operatorInputs) {
        this.operatorInputs = _operatorInputs;
    }
    //need to figure out moveable parts on the picker in order to assign functions
    
}
