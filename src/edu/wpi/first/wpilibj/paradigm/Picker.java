
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
    private boolean buttonPressed; //used to indicate if a button is pressed
    private Talon jaguar = new Talon(8); //used in the SpinGrabber method...also is a Talon
    private final AnalogChannel analogChannel = new AnalogChannel(2);
    
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
            jaguar.set(0.2);
        } else {
            jaguar.set(0);
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
            jaguar.set(-0.3);
        } else {
            jaguar.set(0);
        }
    }
    
    public void getPickerAngle() {
        angle = analogChannel.getVoltage();
       
        if (found) {
            //This is the porportion to convert voltage into a degrees angle.
            //There are 360 degree permax encoder voltage.
            kickingPos = angle * (360/MAX_ENCODER_VOLTAGE);
            
            found = false;
        }
    }
    //need to figure out moveable parts on the picker in order to assign functions
    
}
