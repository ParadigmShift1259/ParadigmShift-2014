
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
    
    DriverControls operatorInputs;
    private Joystick xBox = new Joystick(2);
    private int loadPos = 757; //change value later, position while loading
    private int shootPos = 2136; //change value later, position while shooting/aiming
    private int autoPos = 2048; //change value later, position at the beginning of the auto/match
    private int currentPos; //the picker's current pos(ition)
    private final int BUTTON_X = 3; //this is the x butt on the controller
    private final int BUTTON_LB = 5; //this is is the poot butt
    private boolean buttonPressed; //used to indicate if any button is pressed
    private Talon wheelSpinner = new Talon(8); //used in the SpinGrabber method...also is a Talon
    private final AnalogChannel analogChannel = new AnalogChannel(4);
    private double angle = analogChannel.getVoltage();
    
    /*
    This is the constructor for the Picker class.
    */
    
    public Picker(DriverControls _operatorInputs) {
        this.operatorInputs = _operatorInputs;
    }
    
    /*
    This method spins the picker wheels when the X button is pressed.
    The wheels will load the ball into the picker.
    */
    
    public void spinGrabber() {
        buttonPressed = xBox.getRawButton(BUTTON_X);
        if (buttonPressed) {
            wheelSpinner.set(0.2);
        } else {
            wheelSpinner.set(0);
        }
    }
    
    /*
    This method controls the "pooter". The pooter will make the wheels 
    spin backwards in case the ball gets stuck inside of the picker.\
    
    Possible: May be used for a (weak) pass.
    */
    
    public void spinPooter() {
        buttonPressed = xBox.getRawButton(BUTTON_LB);
        if (buttonPressed) {
            wheelSpinner.set(-0.3);
        } else {
            wheelSpinner.set(0);
        }
    }
    

    //need to figure out moveable parts on the picker in order to assign functions
    
}
