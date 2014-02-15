/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.paradigm;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Doug Dimmadome
 */
public class Shooter {

    OperatorInputs operatorInputs;

    private final int PORT_5 = 5;
    //the current value can not possibly be the previous value the first time through
    private final double ILLEGAL_ANGLE = -9999.9; //can't be stopped when it hasn't started
    OperatorInputs oi = new OperatorInputs();
    private final Joystick xBox = new Joystick(2);

    private final Talon kickermotor = new Talon(PORT_5);
    private boolean buttonPressed;
    //private double triggerPressed;
    //put in place for testing on Sturday night 2/8/2014 - E A COBB
    private boolean triggerPressed; // changed for testing on Saturday night
    private final double LEFT_TRIGGER_PRESSED_MAX_VALUE = 1.0;
    private final double LEFT_TRIGGER_PRESSED_MIN_VALUE = 0.5;
    private final double RIGHT_TRIGGER_PRESSED_MAX_VALUE = -0.5;
    private final double RIGHT_TRIGGER_PRESSED_MIN_VALUE = -1.0;
    //private final Joystick.AxisType RIGHT_TRIGGER = Joystick.AxisType.kZ;
    private final int BACK_BUTTON = 7;
    private final int XBOX_TRIGGERS = 3; //renamed because this is both the left trigger and the right trigger
    private double motorSpeed = 1.0;
    private final AnalogChannel analogChannel = new AnalogChannel(1);
    private final DigitalInput digitalInput = new DigitalInput(9);
    private double previousAngle = ILLEGAL_ANGLE;
    //private double previousAngle = -1;
    private double currentAngle;
    //private 
    boolean inPosition; //made protected for use in AdventureRick
    boolean kicking = false;
    private double kickingPos = 180;
    public boolean caliButtPressed = true;
    double angle;
    private final int MAX_ALLOWED_ANGLE = 165;
    private final int MIN_ALLOWED_ANGLE = 145;
    private boolean pressed;
    private boolean calibrated = false;
    private boolean settingPos = false;
    private final double MAX_ENCODER_VOLTAGE = 5.0;
    private Timer timer = new Timer();

    public Shooter(OperatorInputs _operatorInputs) {
        this.operatorInputs = _operatorInputs;
    }

    public double getKickerMotorPower() {
        return kickermotor.get();
    }

    public boolean isKickerStopped() {
        //currentAngle = analogChannel.getVoltage(); //Read current voltage
        currentAngle = getKickerAngle();
        if (previousAngle >= currentAngle - (5) && previousAngle <= currentAngle + (5)) {
            previousAngle = ILLEGAL_ANGLE; //Set back so it can run again 
            return true;
        } else {
            previousAngle = currentAngle; //current becomes previous
            return false;
        }

    }

    /*
     This method is used to kick.
    
     P.S. It has a dumb name that can go to suckySucky().
     P.P.S. Before 2/8/2014, the above (Post Scriptum) comment applies. ...Latin...
     */
    public void kick() {
        triggerPressed = RIGHT_TRIGGER_PRESSED_MIN_VALUE <= xBox.getRawAxis(XBOX_TRIGGERS)
                && xBox.getRawAxis(XBOX_TRIGGERS) <= RIGHT_TRIGGER_PRESSED_MAX_VALUE; //changed for testing on Sturday night 2/8/2014 - E A COBB
        inPosition = digitalInput.get();
        SmartDashboard.putBoolean("triggerPressed", triggerPressed);
        SmartDashboard.putBoolean("!caliButtPressed", !caliButtPressed);
        SmartDashboard.putBoolean("!settingPos", !settingPos);
        SmartDashboard.putBoolean("isKickerStopped", isKickerStopped());
        if (triggerPressed && !caliButtPressed && !settingPos) { //changed for testing on Sturday night 2/8/2014 - E A COBB
            kicking = true;
            buttonPressed = false;
        }
        if (kicking) {
            kickermotor.set(-0.7); //negative is kicking forward - 2/8/2014 E A Cobb
            if (inPosition) {
                kicking = false;
                kickermotor.set(0);
                buttonPressed = true; //put in place for testing on Sturday night 2/8/2014 - E A COBB
            }
        }
    }

    public void calibrate() {
        inPosition = digitalInput.get();
        buttonPressed = xBox.getRawButton(BACK_BUTTON);
        angle = getKickerAngle();
        if (buttonPressed && !settingPos && isKickerStopped()) {
            caliButtPressed = true;
        }
        if (caliButtPressed) {
            try {
                if (inPosition) {
                    kickermotor.set(0);
                    caliButtPressed = false;
                    calibrated = true;
                } else {
                    if (angle > kickingPos && angle <= MAX_ALLOWED_ANGLE) {
                        kickermotor.set(0.1);
                    } else if (angle > kickingPos || angle <= MIN_ALLOWED_ANGLE) {
                        kickermotor.set(-0.1);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public double getKickerAngle() {
        angle = analogChannel.getVoltage();
        //This is the porportion to convert voltage into a degrees angle.
        //There are 360 degree permax encoder voltage.
        angle = angle * (360 / MAX_ENCODER_VOLTAGE);
        return angle;

    }

    //Added for Saturday Night to program shooter - 2/8/2014 E A Cobb
    public void manualShooterControl() {
        if (!kicking) {
            kickermotor.set(-operatorInputs.xboxLeftY()); //Y-axis is up negative, down positive; Map Y-axis up to green, Y-axis down to red
        }
    }

    public void autoShoot(double time, double power) {
        double base = 0.0;
        if (oi.isShooterTriggerPressed()) {

            timer.start();
            //System.out.println("Loop should be starting");
            while (base + timer.get() < time) {
                kickermotor.set(power);
                //System.out.println("Motor should be going");
                //ystem.out.println(timer.get());
            }
            kickermotor.set(0);
        }

    }

    public void setKickingPosition() {
        triggerPressed = LEFT_TRIGGER_PRESSED_MIN_VALUE <= xBox.getRawAxis(XBOX_TRIGGERS)
                && xBox.getRawAxis(XBOX_TRIGGERS) <= LEFT_TRIGGER_PRESSED_MAX_VALUE; //changed for testing on Sturday night 2/8/2014 - E A COBB
        if (/*calibrated &&*/triggerPressed && !kicking && !caliButtPressed && isKickerStopped()) { //changed for testing on Sturday night 2/8/2014 - E A COBB
            pressed = true;
            buttonPressed = false;
            settingPos = true;
        }
        if (pressed) {
            if (getKickerAngle() == kickingPos) {
                inPosition = true;
            }
            try {
                if (inPosition) {
                    kickermotor.set(0);
                    pressed = false;
                    settingPos = false;
                } else {
                    if (angle < kickingPos && angle >= MAX_ALLOWED_ANGLE) {
                        kickermotor.set(-0.1);
                    } else if (angle > kickingPos || angle <= MIN_ALLOWED_ANGLE) {
                        kickermotor.set(0.1);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

   //need to figure out moveable parts on the shooting mechanism before adding buttons/functions 
}
