/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.paradigm;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Programming
 */
public class PickerPID extends PIDSubsystem {

    private static final double Kp = 2.0;
    private static final double Ki = 0.03;
    private static final double Kd = 1.6;
    

    private double currentAngle;
    public static final double LOAD_ANGLE = 346;
    public static final double KICKING_ANGLE = 226;
    public static final double AUTO_ANGLE = 255;
    public static final double TRUSS_ANGLE = 250;
    private final int pickerChannel = 2;
    private final AnalogChannel analogChannel = new AnalogChannel(pickerChannel);
    private final Talon pickerMotor = new Talon(4);
    private double pickerAngleVoltage;
    private double pickerAngleDegree;
    private final double MAX_ENCODER_VOLTAGE = 5.0;

    // Initialize your subsystem here
    public PickerPID() {
        super("PickerPID", Kp, Ki, Kd);
        
        getPIDController().setInputRange(0, 5);
        getPIDController().setOutputRange(-1.0, 1.0);
        getPIDController().setContinuous(false);
        setAbsoluteTolerance(0.025);
        setSetpoint(4.4);     
        getPIDController().startLiveWindowMode();
        // Use these to get going:
        // setSetpoint() -  Sets where the PID controller should move the system
        //                  to
        // enable() - Enables the PID controller.
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    protected double returnPIDInput() {
//        // Return your input value for the PID loop
//        // e.g. a sensor, like a potentiometer:
//        // yourPot.getAverageVoltage() / kYourMaxVoltage;
//        System.out.println("Picker Angle = " + getPickerAngle());
        System.out.println(analogChannel.getVoltage());
        return pickerAngleVoltage = analogChannel.getVoltage(); //comment
//return getPickerAngle();
    }
    public double getPickerAngle() {
        pickerAngleVoltage = analogChannel.getVoltage(); //comment
        pickerAngleDegree = pickerAngleVoltage * (360 / MAX_ENCODER_VOLTAGE); //Converts Voltage to degrees
        System.out.println("Picker Angle = " + pickerAngleDegree);
        return pickerAngleDegree;
    }

    protected void usePIDOutput(double output) {
        // Use output to drive your system, like a motor
        // e.g. yourMotor.set(output);
        pickerMotor.set(output);
        System.out.println("Picker PID Output = " + output);
        //System.out.println("Period: " + getPIDController().)
    }

}
