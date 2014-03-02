/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.paradigm;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

/**
 *
 * @author Programming
 */
public class ShooterPID extends PIDSubsystem {

    public static final double Kp = 0.12;//previous value 0.0
    private static final AnalogChannel encoder = new AnalogChannel(1);
    public static final Talon shooter = new Talon(5);
    public static double Ki = 0.005;
    public static double Kd = 1.5;
    public double VOLTAGE_CORRECTION = 0.0;
    private static double kickPos; //dummy values, need to be edited(3.5)
    public static double pickPos; //dummy values, need to be edited (1.0)
    public static double zeroPosition = 0.35;
    private static final double OUTPUT_BOUNDS = 0.1;
    private static final double TOLERANCE = .025;
    private double pos;
    private static final double TOLERANCE_DISABLE = 0.1;

    // Initialize your subsystem here
    public ShooterPID() {
        super("ShooterPID", Kp, Ki, Kd);
        pickPos = 1.0 + VOLTAGE_CORRECTION;
        kickPos = 3.65 + VOLTAGE_CORRECTION;
        getPIDController().setOutputRange(-OUTPUT_BOUNDS, OUTPUT_BOUNDS);
        getPIDController().setInputRange(0.0, 5.0);
        getPIDController().setContinuous(false);
        setAbsoluteTolerance(TOLERANCE);
    }

    public void prepKickx() {
        Ki = 0.001;
        getPIDController().setPID(Kp, Ki, Kd);
        System.out.println("prepKickx called");
        setSetpoint(kickPos);
        System.out.println("kick setpoint set");
        enable();
        //getPIDController().reset();
    }

    public void prepPick() {
        Ki = 0.005;
        getPIDController().setPID(Kp, Ki, Kd);
        System.out.println("prepPick called");
        setSetpoint(pickPos);
        System.out.println("pick setpoint set");
        enable();
        //getPIDController().reset();
    }
//pos is not being assigned a 
//    public boolean checkPos() {
//        return (encoder.getVoltage() - TOLERANCE_DISABLE < pos
//                && encoder.getVoltage() + TOLERANCE_DISABLE > pos);
//    }
//
//    public void disableIfInPos() {
//        if (checkPos()) {
//           // disable();
//        }
//    }

    public void toggleDisable() {
        if (getPIDController().isEnable()) {
            disable();
        } else {
            enable();
        }
    }

    public boolean isDisabled() {
        return !getPIDController().isEnable();
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    protected double returnPIDInput() {
        return encoder.getVoltage();
    }

    public double getVoltage() {
        return encoder.getVoltage();
    }

    protected void usePIDOutput(double output) {
        shooter.set(output);
    }

    public void set(double speed) {
        if (!getPIDController().isEnable()) {
            shooter.set(speed);
        }
    }

    public double get() {
        return shooter.get();
    }
}
