/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.paradigm;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;

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
    Timer time;

    double joyStickX; //controlled with a joystick on the x and y axis
    double joyStickY;

    double leftPow;
    double rightPow;
    long sleeptime = 1000;

    double speedMult = 1;
    double fixNum;
    double maxLeftEncoderRate;
    double maxRightEncoderRate;
    double ratio;
    double rightEncoderFix;
    double leftEncoderFix;
    //double currentLeftRate;
    long sleepTime = 0100;
    //high gear = high speed (and low torque)
    boolean isHighGear = true; //will start in high gear (low torque)
    boolean nemo = false;

    boolean previousTriggerPressed; //what the trigger was before it changed

    public DriveTrain(DriverControls _operatorInputs) {
        this.operatorInputs = _operatorInputs;
        //this.previousTriggerPressed = this.operatorInputs.joystickTriggerPressed();
        this.leftTalons = new Talon(LEFT_PORT);
        this.rightTalons = new Talon(RIGHT_PORT);
        this.gearShiftLow = new Solenoid(SHIFT_MODULE, SHIFT_PORT_LOW);
        this.gearShiftHigh = new Solenoid(SHIFT_MODULE, SHIFT_PORT_HIGH);
        this.leftEncoder = new Encoder(3, 4);
        this.rightEncoder = new Encoder(5, 6);
        this.time = new Timer();
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

//they see me rollin', and dey hatin'
        if (leftPow != 0 && rightPow != 0) {
            maxLeftEncoderRate = leftSpeed / leftPow;
            maxRightEncoderRate = rightSpeed / rightPow;
            compareEncoders();
            if (maxLeftEncoderRate > maxRightEncoderRate) {
                ratio = maxRightEncoderRate / maxLeftEncoderRate;
                fixLeftPow = ratio * fixLeftPow;

            }
        }
        System.out.println("Left Speed = " + leftSpeed);
        System.out.println("Left Power = " + leftPow);
        System.out.println("Left Talon Value = " + leftTalons.getSpeed());

        return (fixLeftPow);
    }

    public double RightMotor() {
        double leftSpeed = leftEncoder.getRate();
        double fixLeftPow = fix(leftPow);
        double rightSpeed = rightEncoder.getRate();
        double fixRightPow = fix(rightPow);
//they see me rollin', and dey hatin'
        if (leftPow != 0 && rightPow != 0) {
            maxRightEncoderRate = rightSpeed / rightPow;
            maxLeftEncoderRate = leftSpeed / leftPow;
            compareEncoders();
            if (maxRightEncoderRate > maxLeftEncoderRate) {
                fixRightPow = ratio * fixRightPow;
            }
        }

        System.out.println("Right Speed = " + rightSpeed);
        System.out.println("Right Power = " + rightPow);
        System.out.println("Right Talon Value = " + rightTalons.getSpeed());
        return (fixRightPow);//goes to the talon
    }

    public void compareEncoders() {
        if (maxRightEncoderRate > maxLeftEncoderRate) {
            ratio = maxLeftEncoderRate / maxRightEncoderRate;
            leftEncoderFix = maxRightEncoderRate * ratio;

        } else if (maxLeftEncoderRate > maxRightEncoderRate) {
            ratio = maxRightEncoderRate / maxLeftEncoderRate;
            rightEncoderFix = maxLeftEncoderRate * ratio;

        }

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
        // boolean pressed = operatorInputs.shiftHigh();
        boolean pressed = operatorInputs.shifter();

        if (nemo == true && pressed) {
            gearShiftLow.set(!isHighGear);
            gearShiftHigh.set(isHighGear);
            nemo = false;

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
        boolean pressed = operatorInputs.shifter();
        if (nemo == false && pressed) {
            gearShiftLow.set(isHighGear);
            gearShiftHigh.set(!isHighGear);
            nemo = true;

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
