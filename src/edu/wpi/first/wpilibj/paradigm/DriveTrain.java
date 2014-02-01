/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.paradigm;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Encoder;

/**
 *
 * @author Programming
 */
public class DriveTrain {

    DriverControls operatorInputs;

    final int LEFT_PORT = 1; //attributes  defining the class
    final int RIGHT_PORT = 2;
    final int SHIFT_PORT_LOW = 1;
    final int SHIFT_PORT_HIGH = 2;
    final int SHIFT_MODULE = 1;

    private Talon leftTalons; //has to motors and motor controllers 
    private Talon rightTalons;
    private Solenoid gearShiftLow; // and a gear shifter
    private Solenoid gearShiftHigh;
    Encoder leftEncoder;
    Encoder rightEncoder;

    double joyStickX; //controlled with a joystick on the x and y axis
    double joyStickY;

    double leftPow;
    double rightPow;

    double speedMult = 1;
    double fixNum;
    double maxLeftEncoderRate;
    //double currentLeftRate;

    //high gear = high speed (and low torque)
    boolean isHighGear = true; //will start in high gear (low torque)
    int shiftItLikeItsHot = 0;

    boolean previousTriggerPressed; //what the trigger was before it changed

    public DriveTrain(DriverControls _operatorInputs) {
        this.operatorInputs = _operatorInputs;
        this.previousTriggerPressed = this.operatorInputs.joystickTriggerPressed();
        this.leftTalons = new Talon(LEFT_PORT);
        this.rightTalons = new Talon(RIGHT_PORT);
        this.gearShiftLow = new Solenoid(SHIFT_MODULE, SHIFT_PORT_LOW);
        this.gearShiftHigh = new Solenoid(SHIFT_MODULE, SHIFT_PORT_HIGH);
        this.leftEncoder = new Encoder(3, 4);
        this.rightEncoder = new Encoder(5, 6);
        leftTalons.set(0);  //Make sure motor is off
        rightTalons.set(0); //Make sure motor is off
        gearShiftLow.set(isHighGear);
        gearShiftHigh.set(!isHighGear);

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
        double leftSpeed = leftEncoder.getRate();
        double fixLeftPow = fix(leftPow);
        double rightSpeed = rightEncoder.getRate();
        double fixRightPow = fix(rightPow);
        leftEncoder.getRate()/leftPow = maxLeftEncoderRate;
        return (fixLeftPow);
    }

    public double RightMotor() {
        double leftSpeed = leftEncoder.getRate();
        double fixLeftPow = fix(leftPow);
        double rightSpeed = rightEncoder.getRate();
        double fixRightPow = fix(rightPow);
        return (fixRightPow);
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

    public void shift() {//current setting is start in high gear
        boolean triggerPressed = operatorInputs.joystickTriggerPressed();
        if (triggerPressed == true && previousTriggerPressed == false) { //if trigger was just pressed 
            isHighGear = !isHighGear; // high gear becomes not high gear
            gearShiftHigh.set(isHighGear); // the gear shifts
            gearShiftLow.set(!isHighGear);
        }
        previousTriggerPressed = triggerPressed;
    }

    public void shiftHigh() {
        boolean pressed = operatorInputs.shiftHigh();
        shiftItLikeItsHot = 0;
        if (pressed) {
            gearShiftLow.set(!isHighGear);
            gearShiftHigh.set(isHighGear);
            shiftItLikeItsHot = 1;
        }
//        boolean triggerPressed = operatorInputs.joystickTriggerPressed();
//        if(!isHighGear && triggerPressed ){
//            isHighGear = !isHighGear; // high gear becomes not high gear
//            gearShift.set(!isHighGear); // the gear shifts
//            gearShift2.set(isHighGear);
//            previousTriggerPressed = !triggerPressed;
//        }
    }

    public void shiftLow() {
        boolean pressed = operatorInputs.shiftLow();
        shiftItLikeItsHot = 1;
        if (pressed) {
            gearShiftLow.set(isHighGear);
            gearShiftHigh.set(!isHighGear);
            shiftItLikeItsHot = 0;
        }
//        boolean triggerPressed = operatorInputs.joystickTriggerPressed();
//        if(isHighGear && triggerPressed){
//            isHighGear = !isHighGear; // high gear becomes not high gear
//            gearShift.set(isHighGear); // the gear shifts
//            gearShift2.set(!isHighGear);
//            previousTriggerPressed = !triggerPressed;
//        }
    }
//
//    public void engageShifter() {
//        if (operatorInputs.joystickTriggerPressed()) {
//            shiftItLikeItsHot = 1;
//        }
//        if (operatorInputs.joystickTriggerPressedAgain()) {
//            shiftItLikeItsHot = 0;
//
//        }

}
