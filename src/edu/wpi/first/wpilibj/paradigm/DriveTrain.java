/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.paradigm;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Solenoid;

/**
 *
 * @author Programming
 */
public class DriveTrain {

    DriverControls operatorInputs;

    final int LEFT_PORT = 1; //attributes  defining the class
    final int RIGHT_PORT = 2;
    final int CHANNEL_ONE = 1;
    final int CHANNEL_TWO = 2;

    private Talon leftTalons; //has to motors and motor controllers 
    private Talon rightTalons;
    private Solenoid gearShift; // and a gear shifter
    private Solenoid gearShift2;

    double joyStickX; //controlled with a joystick on the x and y axis
    double joyStickY;

    double leftPow;
    double rightPow;

    double speedMult = 1;
    double fixNum;
    
    //high gear = high speed (and low torque)
    boolean isHighGear = true; //will start in high gear (low torque)
    
    boolean previousTriggerPressed; //what the trigger was before it changed

    public DriveTrain(DriverControls _operatorInputs) {
        this.operatorInputs = _operatorInputs;
        this.previousTriggerPressed = this.operatorInputs.joystickTriggerPressed();
        this.leftTalons = new Talon(LEFT_PORT);
        this.rightTalons = new Talon(RIGHT_PORT);
        this.gearShift = new Solenoid(CHANNEL_ONE);
        this.gearShift2 = new Solenoid(CHANNEL_TWO);
        leftTalons.set(0);
        rightTalons.set(0);
        gearShift.set(isHighGear);
        
    }

    public double fix(double v) {
//        if (v > 1.0) {
//            return ((v - 1.0) * speedMult);
//        } else if (v < -1.0) {
//            return ((v + 1.0) * speedMult);
//        }
        return v / fixNum;
    }

    public double LeftMotor() {
        return fix(leftPow);
    }

    public double RightMotor() {
        return fix(rightPow);
    }
    //fix(),RightMotor(),and LeftMotor() are all used for the tank drive algorithm to correct the value differences of the axis

    public void setPower() {
        joyStickX = operatorInputs.joystickX();
        //System.out.println("joyStickX " +joyStickX);
        joyStickY = operatorInputs.joystickY();
        //System.out.println("joyStickY " +joyStickY);
        //set fixnum = the maxiumum value for this angle on the joystick
        if (joyStickX == 0 || joyStickY == 0) {
            fixNum = 1;
        } else {
            if (Math.abs(joyStickX) > Math.abs(joyStickY)) {
                double fixNumMult = 1 / Math.abs(joyStickX);
                fixNum = Math.abs(joyStickY) * fixNumMult + 1; //1 = math.abs(joyStickX)*fixnum 1
            } else {
                double fixNumMult = 1 / Math.abs(joyStickY);
                fixNum = Math.abs(joyStickX) * fixNumMult + 1; //1 = math.abs(joyStickY)*fixnum 1
            }
        }
        leftPow = -joyStickY + joyStickX; // what is does when joystick is put all the way to the right or left
        rightPow = -joyStickY - joyStickX;
        leftTalons.set(-LeftMotor());
        rightTalons.set(RightMotor());
    }
    
    public void shiftToLow() {//current setting is start in high gear
        boolean triggerPressed = operatorInputs.joystickTriggerPressed();
        if(triggerPressed == true && previousTriggerPressed == false) { //if trigger was just pressed 
            isHighGear = !isHighGear; // high gear becomes not high gear
            gearShift.set(isHighGear); // the gear shifts
            gearShift2.set(!isHighGear);
        }
        previousTriggerPressed = triggerPressed;
    }
    
    public void shiftToHigh(){
        
    }

}
