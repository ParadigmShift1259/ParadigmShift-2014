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

    private final int PORT_5 = 5;  // EAC.2014.02.19 - may benefit in compile-size by being static
    //the current value can not possibly be the previous value the first time through
    private final double ILLEGAL_ANGLE = -9999.9; //can't be stopped when it hasn't started // EAC.2014.02.19 - may benefit in compile-size by being static
    OperatorInputs oi = new OperatorInputs();
    private final Joystick xBox = new Joystick(2); // EAC.2014.02.19 - may benefit in compile-size by being static

//   private final Talon kickermotor = new Talon(PORT_5);
    private boolean buttonPressed;
    //private double triggerPressed;
    //put in place for testing on Sturday night 2/8/2014 - E A COBB
    private boolean triggerPressed; // changed for testing on Saturday night
    private final double LEFT_TRIGGER_PRESSED_MAX_VALUE = 1.0; // EAC.2014.02.19 - may benefit in compile-size by being static
    private final double LEFT_TRIGGER_PRESSED_MIN_VALUE = 0.5; // EAC.2014.02.19 - may benefit in compile-size by being static
    private final double RIGHT_TRIGGER_PRESSED_MAX_VALUE = -0.5; // EAC.2014.02.19 - may benefit in compile-size by being static
    private final double RIGHT_TRIGGER_PRESSED_MIN_VALUE = -1.0; // EAC.2014.02.19 - may benefit in compile-size by being static
    //private final Joystick.AxisType RIGHT_TRIGGER = Joystick.AxisType.kZ;
    private final int XBOX_TRIGGERS = 3; //renamed because this is both the left trigger and the right trigger // EAC.2014.02.19 - may benefit in compile-size by being static
    private double motorSpeed = 1.0;
    // private final AnalogChannel analogChannel = new AnalogChannel(1);
    private final DigitalInput digitalInput = new DigitalInput(9); // EAC.2014.02.19 - may benefit in compile-size by being static
    private double previousAngle = ILLEGAL_ANGLE;
    boolean kicking = false;
    private double kickingPos;
    public boolean caliButtPressed = true;
    private double voltage;
    //private final int MAX_ALLOWED_VOLTAGE;
    //private final int MIN_ALLOWED_VOLTAGE;
    private boolean pressed;
    private boolean calibrated = false;
    private boolean settingPos = false;
    private final double MAX_ENCODER_VOLTAGE = 5.0; // EAC.2014.02.19 - may benefit in compile-size by being static
    private static final double DISABLE_TOLERANCE = .05;
    public Timer timer = new Timer();
    public ShooterPID shooterPid = new ShooterPID();

    public Shooter(OperatorInputs _operatorInputs) {
        this.operatorInputs = _operatorInputs;
    }

    public double getVoltage() {
        return shooterPid.getVoltage();
    }

    public void inPositionDisable() {
        if (shooterPid.getPIDController().getSetpoint() > shooterPid.getVoltage() - DISABLE_TOLERANCE && shooterPid.getPIDController().getSetpoint() < shooterPid.getVoltage() + DISABLE_TOLERANCE) {
            shooterPid.disable();
        }
    }

    public void disableToggle() {
        double delay = 0.01;
        double time;
        time = .2;
        timer.start();
        if ((timer.get() > delay) && (timer.get() < time)) {
            shooterPid.toggleDisable();
        }
        if (timer.get() > time) {
            shooterPid.set(0);
            timer.stop();
            timer.reset();
        }
    }

    public double getKickerMotorPower() {
        return shooterPid.get();
    }

    public ShooterPID getPID() {
        return shooterPid;
    }

    public void togglePID() {
        if (operatorInputs.backPressed()) {

            shooterPid.toggleDisable();
        }
    }

    /*
     This method is used to kick.
    
     P.S. It has a dumb name that can go to suckySucky().
     P.P.S. Before 2/8/2014, the above (Post Scriptum) comment applies. ...Latin...
     */
    /*
     public void kick() {
     triggerPressed = RIGHT_TRIGGER_PRESSED_MIN_VALUE <= xBox.getRawAxis(XBOX_TRIGGERS)
     && xBox.getRawAxis(XBOX_TRIGGERS) <= RIGHT_TRIGGER_PRESSED_MAX_VALUE; //changed for testing on Sturday night 2/8/2014 - E A COBB
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
     if (8 == 8) {
     kicking = false;
     kickermotor.set(0);
     buttonPressed = true; //put in place for testing on Sturday night 2/8/2014 - E A COBB
     }
     }
     }
     */

    /*
     public void calibrate() {
     voltage = getVoltage();
     if (caliButtPressed) {
     try {
     if (getVoltage() == kickingPos) {
     kickermotor.set(0);
     caliButtPressed = false;
     } else {
     if (voltage > kickingPos && voltage <= MAX_ALLOWED_VOLTAGE) {
     kickermotor.set(0.1);
     } else if (voltage > kickingPos || voltage <= MIN_ALLOWED_VOLTAGE) {
     kickermotor.set(-0.1);
     }
     }
     } catch (Exception ex) {
     ex.printStackTrace();
     }
     }
     }
     */
    //Added for Saturday Night to program shooter - 2/8/2014 E A Cobb
    public void manualShooterControl() {
        if (!kicking) {
            shooterPid.set(operatorInputs.xboxLeftY()); //Y-axis is up negative, down positive; Map Y-axis up to green, Y-axis down to red
        }
    }

    public void moveToKickPos() {
        if (operatorInputs.xBoxXButton() || operatorInputs.xBoxYButton()) {
            shooterPid.prepKickx();
        }
    }

    public void moveToPickPos() {
        if (operatorInputs.xBoxBButton()) {
            shooterPid.prepPick();
        }
    }

    public void quickButtonShoot(double time, double power, double delay) {
        if (oi.isShooterTriggerPressed() && shooterPid.isDisabled()) {
            kicking = true;
            timer.start();
        }
        //shootTimer.reset();
        //System.out.println("Loop should be starting");

        if ((timer.get() > delay) && (timer.get() < time)) {
            shooterPid.set(power);
        }
        //System.out.println("Motor should be going");
        //ystem.out.println(shootTimer.get());
        if (timer.get() > time) {
            shooterPid.set(0);
            timer.stop();
            timer.reset();
            kicking = false;

        }
    }
    /*
     public void autoShoot(double time, double power) {
     double base = 0.0;
     //        shootTimer.start();
     //System.out.println("Loop should be starting");

     if (base + shootTimer.get() < time) {
     kickermotor.set(power);
     //System.out.println("Motor should be going");
     //ystem.out.println(shootTimer.get());
     } else {
     shooterPid.set(0);
     shootTimer.stop();
     }
     }

     public void setKickingPosition() {
     triggerPressed = LEFT_TRIGGER_PRESSED_MIN_VALUE <= xBox.getRawAxis(XBOX_TRIGGERS)
     && xBox.getRawAxis(XBOX_TRIGGERS) <= LEFT_TRIGGER_PRESSED_MAX_VALUE; //changed for testing on Sturday night 2/8/2014 - E A COBB
     if (calibrated &&triggerPressed && !kicking && !caliButtPressed && isKickerStopped()) { //changed for testing on Sturday night 2/8/2014 - E A COBB
     pressed = true;
     buttonPressed = false;
     settingPos = true;
     }
     if (pressed) {
     try {
     if (getVoltage() == kickingPos) {
     kickermotor.set(0);
     settingPos = false;
     } else {
     if (voltage < kickingPos && voltage >= MAX_ALLOWED_VOLTAGE) {
     kickermotor.set(-0.1);
     } else if (voltage > kickingPos || voltage <= MIN_ALLOWED_VOLTAGE) {
     kickermotor.set(0.1);
     }
     }
     } catch (Exception ex) {
     ex.printStackTrace();
     }
     }
     */
}

   //need to figure out moveable parts on the shooting mechanism
   //before adding buttons/functions 
