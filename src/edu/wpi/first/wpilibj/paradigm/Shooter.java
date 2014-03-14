/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.paradigm;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;

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
    public ShooterPID shooterPid = new ShooterPID();
    //private final Joystick xBox = new Joystick(2); // EAC.2014.02.19 - may benefit in compile-size by being static

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
    //private final int XBOX_TRIGGERS = 3; //renamed because this is both the left trigger and the right trigger // EAC.2014.02.19 - may benefit in compile-size by being static
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
    public Timer shootTimer = new Timer();
    double coasting = 1;

    public Shooter(OperatorInputs _operatorInputs) {
        this.operatorInputs = _operatorInputs;
    }

    public double getVoltage() {
        return shooterPid.getVoltage();
    }

    public double get() {
        return shooterPid.get();
    }

    public void inPositionDisable() {
        if (shooterPid.getPIDController().getSetpoint() > shooterPid.getVoltage() - DISABLE_TOLERANCE && shooterPid.getPIDController().getSetpoint() < shooterPid.getVoltage() + DISABLE_TOLERANCE) {
            shooterPid.disable();
        }
    }

    public double getKickerMotorPower() {
        return shooterPid.get();
    }

    public ShooterPID getPID() {
        return shooterPid;
    }

    public void emergencyDisablePid() {
        if (operatorInputs.xBoxAButton()) {
            shooterPid.disable();
        }
    }

    //Added for Saturday Night to program shooter - 2/8/2014 E A Cobb
    public void manualShooterControl() {
        if (!kicking) {
            //shooterPid.disable();
            shooterPid.set(operatorInputs.xboxLeftY()); //Y-axis is up negative, down positive; Map Y-axis up to green, Y-axis down to red
        }
    }

    public void moveToKickPos() {
        if (operatorInputs.xBoxBackButton()) {
            shooterPid.prepKickx();
        }
    }

    public void passKick() {
        quickShoot(2.0, 0.05, 0.01, operatorInputs.xBoxYButton());
    }

    public void moveToPickPos() {
        if (operatorInputs.xBoxStartButton()) {
            shooterPid.prepPick();
        }
    }

    public void disablePID() {
        shooterPid.disable();
    }

    public void quickShoot(double time, double power, double delay, boolean needsShoot) {
        if (needsShoot) {
            shooterPid.disable();
            kicking = true;
            timer.start();
            //System.out.println("Loop should be starting");
        }
        //shootTimer.reset();
        if ((timer.get() < delay) && (timer.get() < time)) {
            Picker.isKickingNow = true;
        }

        if ((timer.get() > delay) && (timer.get() < time)) {
            shooterPid.set(-power);
            Picker.isKickingNow = false;
            //System.out.println("Motor should be going");
        }

        System.out.println(shootTimer.get());
        if (timer.get() > time) {
            //System.out.println("Motor should be stopping");
            shooterPid.set(0);
            timer.stop();
            timer.reset();
            kicking = false;
            //shooterPid.enable();

        }
    }

    public void quickButtonShoot(double time, double power, double delay) {
        //pickerPID.disable();
//        Picker pick = new Picker(this.operatorInputs);
//        Talon wheelSpinner = pick.wheelSpinner;
//        wheelSpinner.set(0.5);
        
        if (oi.isShooterTriggerPressed() /*&& shooterPid.isDisabled()*/) {
            shooterPid.disable();
            kicking = true;
            timer.start();
            
        }
        //shootTimer.reset();
        //System.out.println("Loop should be starting");
        if ((timer.get() < delay) && (timer.get() < time)) {
            Picker.isKickingNow = true;
        }
        
        if ((timer.get() > delay) && (timer.get() < time)) {
            System.out.println("Before quickShoot: " + DriverStation.getInstance().getBatteryVoltage());
            shooterPid.set(power);
            coasting = .5;
            Picker.isKickingNow = false;
        }

        //System.out.println("Motor should be going");
        //ystem.out.println(shootTimer.get());
        if (timer.get() > time) {
            System.out.println("After quickShoot: " + DriverStation.getInstance().getBatteryVoltage());
            shooterPid.set(0);
            coasting = 1;
            timer.reset();
            timer.stop();
            kicking = false;
            //shooterPid.enable();
            
        }
//        wheelSpinner.set(0.0);
    }

    public void quickLeftButtonShoot(double time, double power, double delay) {

        if (oi.isLeftShooterTriggerPressed() /*&& shooterPid.isDisabled()*/) {
            shooterPid.disable();
            kicking = true;
            shootTimer.start();
        }
        //shootTimer.reset();
        //System.out.println("Loop should be starting");

        if ((shootTimer.get() > delay) && (shootTimer.get() < time)) {
            shooterPid.set(power);
        }
        //System.out.println("Motor should be going");
        //ystem.out.println(shootTimer.get());
        if (shootTimer.get() > time) {
            //shooterPid.set(0);
            shooterPid.set(0);
            shootTimer.reset();
            shootTimer.stop();
            kicking = false;
            //shooterPid.enable();
            System.out.println(DriverStation.getInstance().getBatteryVoltage());
        }
    }

    
    /*
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
    public double getCoastingValue() {
        return coasting;
    }
}


   //need to figure out moveable parts on the shooting mechanism
   //before adding buttons/functions 
