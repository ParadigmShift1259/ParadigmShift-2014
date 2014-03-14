/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.paradigm;

import edu.wpi.first.wpilibj.DriverStation;
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

    //static variables can benefit compile size
    final int LEFT_PORT = 1;
    final int RIGHT_PORT = 2;
    final int SHIFT_PORT_LOW = 1;
    final int SHIFT_PORT_HIGH = 2;
    final int SHIFT_MODULE = 1;
    boolean needsShoot = false;

    double joyStickX;
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
    long sleepTime = 0100;
    boolean isHighGear = false; //Robot starts in low gear
    boolean nemo = false;
    boolean isLeftHigher = true;
    double leftSpeed = 0;
    double rightSpeed = 0;
    final double encoderDeadzone = 1000;
    final double encoderWaitTime = 168;
    double leftChildProofSetter;
    double rightChildProofSetter;
    boolean childProofConfirmed = false;
    private static final double DISTANCE_PER_PULSE = 0.0006708;
    boolean previousTriggerPressed = false; //what the trigger value was before the current press, allows for trigger to stay pressed w/o flipping
    double previousLeftPow = 0;
    double previousRightPow = 0;

    public Talon leftTalons;
    public Talon rightTalons;
    private Solenoid gearShiftLow;
    private Solenoid gearShiftHigh;
    Encoder leftEncoder;
    Encoder rightEncoder;
    Timer timer;
    OperatorInputs operatorInputs;

    public DriveTrain(OperatorInputs _operatorInputs) {
        this.operatorInputs = _operatorInputs;
        this.leftTalons = new Talon(LEFT_PORT);
        this.rightTalons = new Talon(RIGHT_PORT);
        this.gearShiftLow = new Solenoid(SHIFT_MODULE, SHIFT_PORT_LOW);
        this.gearShiftHigh = new Solenoid(SHIFT_MODULE, SHIFT_PORT_HIGH);
        this.leftEncoder = new Encoder(3, 4);
        this.rightEncoder = new Encoder(5, 6);
        this.timer = new Timer();
        //Start all wheels off
        leftTalons.set(0);
        rightTalons.set(0);
        //Starts in low gear
        gearShiftLow.set(isHighGear);
        gearShiftHigh.set(!isHighGear);
        leftEncoder.setDistancePerPulse(-DISTANCE_PER_PULSE);
        rightEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);

    }

    public void rampLeftPower(double desiredPow) { //Makes it so that robot can't go stop to full
        if (Math.abs(desiredPow - previousLeftPow) < 0.1) {
            previousLeftPow = desiredPow;
        } else if (previousLeftPow < desiredPow) {
            previousLeftPow += 0.1;
        } else if (previousLeftPow > desiredPow) {
            previousLeftPow -= 0.1;
        }
        leftTalons.set(-previousLeftPow);

    }

    public void rampRightPower(double desiredPow) { //Makes it so that robot can't go stop to full
        if (Math.abs(desiredPow - previousRightPow) < 0.1) {
            previousRightPow = desiredPow;
        } else if (previousRightPow < desiredPow) {
            previousRightPow += 0.1;
        } else if (previousRightPow > desiredPow) {
            previousRightPow -= 0.1;
        }
        rightTalons.set(previousRightPow);

    }

    public void resetEncoders() { //Resets current raw encoder value to 0
        leftEncoder.reset();
        rightEncoder.reset();
        gearShiftHigh.set(isHighGear);
        gearShiftLow.set(!isHighGear);
    }

    public void driveStraight(double distance, double firingDistance, double speed, Shooter shoot) { //Controls robot during autonomous

        double batteryVoltage = DriverStation.getInstance().getBatteryVoltage();
        if (rightEncoder.getDistance() < distance) {
            rampLeftPower(speed);
            rampRightPower(speed);
            needsShoot = true;
        } else if (rightEncoder.getDistance() < firingDistance) {
            shoot.quickShoot(1.0, (10.0 / batteryVoltage) > 1 ? 1.0 : (10.0 / batteryVoltage), 0.01, needsShoot);
            needsShoot = false;
        } else {
            rampLeftPower(0);
            rampRightPower(0);
            shoot.quickShoot(1.0, (10.0 / batteryVoltage) > 1 ? 1.0 : (10.0 / batteryVoltage), 0.01, needsShoot);
            needsShoot = false;
        }
    }

    public double getRightPulses() { //Returns raw value of right talon encoder
        return rightEncoder.getRaw();
    }

    public double getLeftPulses() { //Returns raw value of left talon encoder
        return leftEncoder.getRaw();
    }

    public double getRightEncoderDistance() { //Returns the distance per pulse of the right encoder
        return rightEncoder.getDistance();
    }

    public double getLeftEncoderDistance() { //Returns the distance per pulse of the left encoder
        return leftEncoder.getDistance();
    }

    public double fix(double v) {
        return v / fixNum;
    }

    public double LeftMotor() {
        //moved leftSpeed to class scope, it is being set in setPower()
        double fixLeftPow = fix(leftPow);
        //moved rightSpeed to class scope, it is being set in setPower()
        double fixRightPow = fix(rightPow);

        if (leftPow != 0 && rightPow != 0) {
            maxLeftEncoderRate = Math.abs(leftSpeed / leftPow);
            if (Math.min(Math.abs(leftSpeed), Math.abs(rightSpeed)) > encoderDeadzone) {
                breakTime();
            }
            if (isLeftHigher) {
                fixLeftPow = ratio * fixLeftPow;
            }
            leftChildProofSetter = Math.abs(fixLeftPow);
        }

        return (fixLeftPow);
    }

    public double RightMotor() {
        //moved leftSpeed to class scope, it is being set in setPower()
        double fixLeftPow = fix(leftPow);
        //moved rightSpeed to class scope, it is being set in setPower()
        double fixRightPow = fix(rightPow);

        if (leftPow != 0 && rightPow != 0) {
            maxRightEncoderRate = Math.abs(rightSpeed / rightPow);
            if (Math.min(Math.abs(leftSpeed), Math.abs(rightSpeed)) > encoderDeadzone) {
                breakTime();
            }
            if (!isLeftHigher) {
                fixRightPow = ratio * fixRightPow;
            }
            rightChildProofSetter = Math.abs(fixRightPow);
        }

        return (fixRightPow);

    }

    public void compareEncoders() { //If left motor speed is bigger than the right motor speed return true, else false
        if (maxRightEncoderRate > maxLeftEncoderRate) {
            ratio = maxLeftEncoderRate / maxRightEncoderRate;
            leftEncoderFix = maxRightEncoderRate * ratio;
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
        SmartDashboard.putNumber("Timer time", timer.get());
        if (timer.get() > encoderWaitTime) {
            compareEncoders();
            timer.reset();
        }
    }

    public void setPower() {
        joyStickX = operatorInputs.joystickX();
        joyStickY = operatorInputs.joystickY();
        //set fixnum = the maxiumum value for this angle on the joystick
        if (joyStickX == 0 || joyStickY == 0) {
            fixNum = 1;
        } else {
            if (Math.abs(joyStickX) > Math.abs(joyStickY)) {
                double fixNumMult = 1 / Math.abs(joyStickX);
                fixNum = Math.abs(joyStickY) * fixNumMult + 1;
            } else {
                double fixNumMult = 1 / Math.abs(joyStickY);
                fixNum = Math.abs(joyStickX) * fixNumMult + 1;
            }
        }
        leftPow = -joyStickY + joyStickX;
        rightPow = -joyStickY - joyStickX;
        leftSpeed = leftEncoder.getRate();
        rightSpeed = rightEncoder.getRate();

        rampLeftPower(LeftMotor()); //Left Motors are forward=negative
        SmartDashboard.putNumber("JoystickX", joyStickX);
        SmartDashboard.putNumber("LeftTalons", -leftTalons.get()); //Left Motors are forward=negative
        SmartDashboard.putNumber("LeftSpeed", -leftSpeed); //Left Motors are forward=negative

        rampRightPower(RightMotor()); //Right Motors are forward=positive
        SmartDashboard.putNumber("JoystickY", joyStickY);
        SmartDashboard.putNumber("RightTalons", rightTalons.get()); //Right Motors are forward=positive
        SmartDashboard.putNumber("RightSpeed", rightSpeed); //Right Motors are forward=positive
        System.out.println("High gear :" + isHighGear);
    }

    public void shift() {//current setting is start in high gear
        boolean triggerPressed = operatorInputs.joystickTriggerPressed();
        System.out.println("Trigger Pressed :" + triggerPressed);
            if (triggerPressed && !previousTriggerPressed) {
                isHighGear = !isHighGear;
                //Shifts gear
                gearShiftHigh.set(isHighGear);
                gearShiftLow.set(!isHighGear);
            }

        
        previousTriggerPressed = triggerPressed;
    
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

    public void childProofing() { //Low to high and speed, High to low when speed is under a certain value

        if (rightChildProofSetter < .75 && leftChildProofSetter < .75) {
            childProofConfirmed = true;
        } else {
            childProofConfirmed = false;
        }
    }

}
