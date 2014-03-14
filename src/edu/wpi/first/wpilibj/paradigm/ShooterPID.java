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
    public static double Kd = 1.0;
    public double VOLTAGE_CORRECTION = 0.0;
    private static double kickPos; //dummy values, need to be edited(3.5)
    private static double passPos;
    public static double pickPos; //dummy values, need to be edited (1.0)
    public static double zeroPosition = 0.35;
    private static final double OUTPUT_BOUNDS = 0.1;
    private static final double TOLERANCE = .025;
    private double pos;

    // Initialize your subsystem here
    public ShooterPID() {
        super("ShooterPID", Kp, Ki, Kd);
        pickPos = 2.57 + VOLTAGE_CORRECTION;
        kickPos = 3.72 + VOLTAGE_CORRECTION;
        passPos = 0.3 + VOLTAGE_CORRECTION;
        getPIDController().setOutputRange(-OUTPUT_BOUNDS, OUTPUT_BOUNDS);
        getPIDController().setInputRange(0.0, 5.0);
        getPIDController().setContinuous(false);
        setAbsoluteTolerance(TOLERANCE);
    }

    public void prepKickx() {
        Ki = 0.002;
        getPIDController().setPID(Kp, Ki, Kd);
        System.out.println("prepKickx called");
        setSetpoint(kickPos);
        System.out.println("kick setpoint set");
        enable();
    }

    public void prepPass() {
        Ki = 0.002;
        getPIDController().setPID(Kp, Ki, Kd);
        System.out.println("prepPass called");
        setSetpoint(passPos);
        System.out.println("pass setpoint set");
        enable();
    }

    public void prepPick() {
        Ki = 0.005;
        getPIDController().setPID(Kp, Ki, Kd);
        System.out.println("prepPick called");
        setSetpoint(pickPos);
        System.out.println("pick setpoint set");
        enable();
    }

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
        // needs to be overridden
    }

    protected double returnPIDInput() {
        return encoder.getVoltage();
    }

    public double getVoltage() {
        return encoder.getVoltage()- VOLTAGE_CORRECTION;
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
