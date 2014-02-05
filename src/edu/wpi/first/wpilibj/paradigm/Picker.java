
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.paradigm;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Encoder;

/**
 *
 * @author Programming
 */
public class Picker {
    
    DriverControls operatorInputs;
    private Joystick xBox = new Joystick(2);
    private int loadPos = 757; //change value later, position while loading
    private int shootPos = 2136; //change value later, position while shooting/aiming
    private int autoPos = 2048; //change value later, position at the beginning of the auto/match
    private int currentPos; //the picker's current pos(ition)
    private int selfDestruct = 0;
    private final int BUTTON_X = 3; //this is the x butt on the controller
    private boolean buttonPressed = false;
    private Talon jaguar = new Talon(8); //used in the SpinGrabber method...
    
    public Picker(DriverControls _operatorInputs) {
        this.operatorInputs = _operatorInputs;
    }
    
    public void spinGrabber() {
        buttonPressed = xBox.getRawButton(BUTTON_X);
        if (buttonPressed == true) {
            jaguar.set(0.2);
        } else {
            jaguar.set(0);
        }
    }
    //need to figure out moveable parts on the picker in order to assign functions
    
}
