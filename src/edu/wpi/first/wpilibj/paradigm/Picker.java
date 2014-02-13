
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.paradigm;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
//import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.AnalogChannel;

/**
 *
 * @author Programming
 */
public class Picker {
    
    OperatorInputs operatorInputs;
    private Joystick xBox = new Joystick(2);
    private int loadPos = 346; //change value later, position while loading
    private int shootPos = 225; //change value later, position while shooting/aiming
    private int autoPos = 36; //change value later, position at the beginning of the auto/match
    private double currentAngle; //the picker's current pos(ition)
    private final int RIGHT_BUMPER = 6; //this is the x butt on the controller
    private final int BUTTON_LB = 5; //this is is the poot butt
    private boolean buttonPressed = false; //used to indicate if any button is pressed
    private boolean isGrabbing = false;
    private boolean isPooting = false;
    private Talon wheelSpinner = new Talon(3); //used in the SpinGrabber method...also is a Talon
    private Talon pickerMotor = new Talon(4);
    private final int pickerChannel = 2;
    private final AnalogChannel analogChannel = new AnalogChannel(pickerChannel);
    private double pickerAngleVoltage;
    private double pickerAngleDegree;
    private double MAX_ENCODER_VOLTAGE = 5.0;
    private boolean settingPos1 = false;
    private boolean settingPos2 = false;
    private boolean settingPos3 = false;
    private final int A_BUTTON = 1;
    private final int B_BUTTON = 2;
    private final int Y_BUTTON = 4;
    boolean isPicking;
    //boolean isPooting; already defined
    
    
    /*
    This is the constructor for the Picker class.
    */
    
    public Picker(OperatorInputs _operatorInputs) {
        this.operatorInputs = _operatorInputs;
    }
    
    /*
    This method spins the picker wheels when the X button is pressed.
    The wheels will load the ball into the picker.
    */
   
    /*
    BELOW STUFF WORKS NOW
    */
    public void spinGrabber() { //Aka suckySucky();
        buttonPressed = xBox.getRawButton(RIGHT_BUMPER); 
        if (buttonPressed && !isPooting) { //Cannot commence when it is pooting(releasing)
            isGrabbing = true; //Boolean so it cannot Grab and Poot at the same time
            wheelSpinner.set(1);
        } else if (!buttonPressed && !isPooting){ //We don't want the motor stopping when it is pooting
            wheelSpinner.set(0);
            isGrabbing = false;
        }
    }
    
    /*
    This method controls the "pooter". The pooter will make the wheels 
    spin backwards in case the ball gets stuck inside of the picker.\
    
    Possible: May be used for a (weak) pass.
    */
    
    public void spinPooter() {
        buttonPressed = xBox.getRawButton(BUTTON_LB);
        if (buttonPressed && !isGrabbing) { //Cannot poot and grab at the same time
            isPooting = true; //Boolean for so it can not poot and grab at the same time
            wheelSpinner.set(-1);
        } else if (!buttonPressed && !isGrabbing){ 
            wheelSpinner.set(0);
            isPooting = false;
        }
    }
   /*
    ABOVE STUFF WORKS
    */ 
    
    
    public double getPickerAngle() {
        pickerAngleVoltage = analogChannel.getVoltage(); //comment
        pickerAngleDegree = pickerAngleVoltage * (360/MAX_ENCODER_VOLTAGE); //Converts Voltage to degrees
        return pickerAngleDegree;
        //return pickerAngleVoltage;
    } 
    
    public void setPosLoading() { //loadPos = 346
        System.out.println("Beginning of method");
       buttonPressed = xBox.getRawButton(5);
       currentAngle = getPickerAngle();
       if(buttonPressed /*&& !settingPos2 && !settingPos3*/) { //Cannot set two at once
           System.out.println("Button pressed");
           settingPos1 = true;    //Set boolean so you don't have to hold the button down
       }
       if(settingPos1 == true) {
           if(Math.abs(currentAngle - loadPos) < 5) {
               pickerMotor.set(0);
           }else if(currentAngle < loadPos) {
               pickerMotor.set(-0.51);
           }else if(currentAngle > loadPos) {
               pickerMotor.set(0.51);
               settingPos1 = false;
           }
       }
       System.out.println("End of method");
    }
    
    public void setPosKicking() {
       buttonPressed = xBox.getRawButton(B_BUTTON);
       currentAngle = getPickerAngle();
       if(buttonPressed && !settingPos1 && !settingPos3) { 
           settingPos2 = true;    
       }
       if(settingPos2 = true) {
           if(currentAngle > shootPos) {
               pickerMotor.set(-0.7);
           }else if(currentAngle < shootPos) {
               pickerMotor.set(0.7);
           }else if(currentAngle == shootPos) {
               pickerMotor.set(0);
               settingPos2 = false;
           }
       }
    }
   
    public void setPosAuto() {
       buttonPressed = xBox.getRawButton(Y_BUTTON);
       currentAngle = getPickerAngle();
       if(buttonPressed && !settingPos1 && !settingPos2) {
           settingPos3 = true;    
       }
       if(settingPos3 = true) {
           if(currentAngle > autoPos) {
               pickerMotor.set(-0.7);
           }else if(currentAngle < autoPos) {
               pickerMotor.set(0.7);
           }else if(currentAngle == autoPos) {
               pickerMotor.set(0);
               settingPos3 = false;
           }
       }
    }
    //need to figure out moveable parts on the picker in order to assign functions
    
}
