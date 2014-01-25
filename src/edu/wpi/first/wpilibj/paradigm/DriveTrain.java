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

    DriverControls OI;

    final int LP = 1;
    final int RP = 2;

    private Talon Left = new Talon(LP);
    private Talon Right = new Talon(RP);

    double joyStickX  = OI.joystickX();
    double joyStickY  = OI.joystickY();

    double leftPow = joyStickY + joyStickX;
    double rightPow = joyStickX - joyStickY;

    double speedMult = 6;

    public double fix(double v) {
        if (v > 1.0) {
            return -((v - 1.0) * speedMult);
        } else if (v < -1.0) {
            return -((v + 1.0) * speedMult);
        }
        return 0;
    }

    public double LeftMotor() {
        return leftPow + fix(rightPow);
    }

    public double RightMotor() {
        return rightPow + fix(leftPow);
    }
    //fix(),RightMotor(),and LeftMotor() are all used for the tank drive algorithm to correct the value differences of the axis
   
    public void setPower() {
        Left.set(LeftMotor());
        Right.set(RightMotor());
    }

}
