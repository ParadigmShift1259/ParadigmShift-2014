/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.paradigm;

import edu.wpi.first.wpilibj.Talon;

/**
 *
 * @author Programming
 */
public class DriveTrain {

    DriverControls operatorInputs;

    final int LEFT_PORT = 1;
    final int RIGHT_PORT = 2;

    private Talon leftTalons = new Talon(LEFT_PORT);
    private Talon rightTalons = new Talon(RIGHT_PORT);

    double joyStickX;
    double joyStickY;

    double leftPow;
    double rightPow;

    double speedMult = 1;
    double fixNum;

    public DriveTrain(DriverControls _operatorInputs) {
        this.operatorInputs = _operatorInputs;
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
        joyStickY = operatorInputs.joystickY();
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
        leftPow = -joyStickY - joyStickX;
        rightPow = -joyStickY + joyStickX;
        leftTalons.set(LeftMotor());
        rightTalons.set(RightMotor());
    }

}
