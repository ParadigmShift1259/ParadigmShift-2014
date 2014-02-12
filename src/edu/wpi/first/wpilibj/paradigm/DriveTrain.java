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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Programming
 */
public class DriveTrain {

    final int PWM_LEFT_PORT   = 1; // For talons 3 & 4, driving left side of robot
    final int PWM_RIGHT_PORT  = 2; // For talons 1 & 2, driving right side of robot
    final int SHIFT_PORT_LOW  = 1; // 
    final int SHIFT_PORT_HIGH = 2; //
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
    double totalSpeed;
    long sleeptime = 1000;

    double speedMult = 1;
    double fixNum;
    double maxLeftEncoderRate;
    double maxRightEncoderRate;
    double ratio = 1;
    double rightEncoderFix;
    double leftEncoderFix;
    //double currentLeftRate;
    long sleepTime = 0100;
    //high gear = high speed (and low torque)
    boolean isHighGear = true; //will start in high gear (low torque)
    boolean isLeftHigher = true;
    double leftSpeed = 0;
    double rightSpeed = 0;
    final double encoderDeadzone = 1000;
    final double encoderWaitTime = 168; //0250 is 250 octal = 168 decimal
    double leftChildProofSetter;
    double rightChildProofSetter;
    boolean childProofConfirmed = false;

    boolean previousTriggerPressed; //what the trigger was before it changed

    public DriveTrain() {
        this.leftTalons = new Talon(PWM_LEFT_PORT);
        this.rightTalons = new Talon(PWM_RIGHT_PORT);
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

    //see below
    public double LeftMotor() {
        //moved leftSpeed to class scope, it is being set in setPower()
        double fixLeftPow = fix(leftPow);
        //moved rightSpeed to class scope, it is being set in setPower()
        double fixRightPow = fix(rightPow);

//they see me rollin', and dey hatin'
        if (leftPow != 0 && rightPow != 0) {
            maxLeftEncoderRate = Math.abs(leftSpeed / leftPow);
            //maxRightEncoderRate = rightSpeed / rightPow;
            if (Math.min(Math.abs(leftSpeed), Math.abs(rightSpeed)) > encoderDeadzone) {
                breakTime();
            }
            if (isLeftHigher) {
                //ratio = maxRightEncoderRate / maxLeftEncoderRate;
                fixLeftPow = ratio * fixLeftPow;

            }
            leftChildProofSetter = fixLeftPow;
        }
        //System.out.println("Left Speed = " + leftSpeed);
        //System.out.println("Left Power = " + leftPow);
        //System.out.println("Left Talon Value = " + leftTalons.getSpeed());

        return (fixLeftPow);
    }

    public double RightMotor() {
        //moved leftSpeed to class scope, it is being set in setPower()
        double fixLeftPow = fix(leftPow);
        //moved rightSpeed to class scope, it is being set in setPower()
        double fixRightPow = fix(rightPow);
//they see me rollin', and dey hatin'
        if (leftPow != 0 && rightPow != 0) {
            maxRightEncoderRate = Math.abs(rightSpeed / rightPow);
            //maxLeftEncoderRate = Math.abs(leftSpeed / leftPow);
            if (Math.min(Math.abs(leftSpeed), Math.abs(rightSpeed)) > encoderDeadzone) {
                breakTime();
            }
            if (!isLeftHigher) {
                fixRightPow = ratio * fixRightPow;

            }
            rightChildProofSetter = fixRightPow;
        }

        //System.out.println("Right Speed = " + rightSpeed);
        //System.out.println("Right Power = " + rightPow);
        //System.out.println("Right Talon Value = " + rightTalons.getSpeed());
        return (fixRightPow);//goes to the talon

    }

    public void compareEncoders() {
        if (maxRightEncoderRate > maxLeftEncoderRate) {
            ratio = maxLeftEncoderRate / maxRightEncoderRate;
            leftEncoderFix = maxRightEncoderRate * ratio + .1;
            isLeftHigher = false;

        } else if (maxLeftEncoderRate > maxRightEncoderRate) {
            ratio = maxRightEncoderRate / maxLeftEncoderRate;
            rightEncoderFix = maxLeftEncoderRate * ratio;
            isLeftHigher = true;
        } else {
            ratio = 1;
        }

    }

    public void breakTime() {
        SmartDashboard.putNumber("Ratio", ratio);
        SmartDashboard.putBoolean("Left > Right", isLeftHigher);
        SmartDashboard.putNumber("Timer time", time.get());
        if (time.get() > encoderWaitTime) {
            compareEncoders();
            time.reset();
        }
    }

    public void setPower(double joyStickX, double joyStickY) {
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
        leftPow    = -joyStickY + joyStickX; // what is does when joystick is put all the way to the right or left
        rightPow   = -joyStickY - joyStickX;
        leftSpeed  = leftEncoder.getRate();
        rightSpeed = rightEncoder.getRate();

        leftTalons.set(-LeftMotor()); //Left Motors are forward=negative
        SmartDashboard.putNumber("JoystickX", joyStickX);
        SmartDashboard.putNumber("LeftTalons", -leftTalons.get()); //Left Motors are forward=negative
        SmartDashboard.putNumber("LeftSpeed", -leftSpeed); //Left Motors are forward=negative

        rightTalons.set(RightMotor()); //Right Motors are forward=positive
        SmartDashboard.putNumber("JoystickY", joyStickY);
        SmartDashboard.putNumber("RightTalons", rightTalons.get()); //Right Motors are forward=positive
        SmartDashboard.putNumber("RightSpeed", rightSpeed); //Right Motors are forward=positive
    }

    /**
     * shift()
     * Using the solenoids for the gears in the gear box.  Since there are only two gears (low and high), 
     * each call to this method will switch from one gear to the other.
     */
    public void shift() {
        
        isHighGear = !isHighGear;
        gearShiftHigh.set(isHighGear);
        gearShiftLow.set(!isHighGear);

    }

    public void setSpeedPositive() {
        totalSpeed = (leftPow + rightPow) / 2;
        if (isHighGear = true) {
            totalSpeed = (leftPow + rightPow);
        }
        if (totalSpeed < 0) {
            totalSpeed = -totalSpeed;
        }
    }

    public void childProofing() {//needs to be tested on bot - Allison
        if (rightChildProofSetter < .05 && leftChildProofSetter < .05) {
            childProofConfirmed = true;
        } else {
            childProofConfirmed = false;
        }
    }
}
