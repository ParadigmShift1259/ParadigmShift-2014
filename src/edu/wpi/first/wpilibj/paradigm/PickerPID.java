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

    public static double Kp = 1.0; 
    public static double Ki = 0.025; //0.03
    public static double Kd = 4.5; //1.6
    public static double position = 0.0;
    public static double outputBounds = 0.8; // EAC.2014.02.19 - Consider changing to 10 if you also use the suggestion in usePIDOutput()

    private double currentAngle;
    public static double VOLTAGE_CORRECTION = 0.0;//2.62?, 3.64
    private static final double step = .4; //aded by John
    /*
     private double desiredPos; //added by John
     private boolean posSet; //added by John
     private double nextStep; //added by John
     */
    public static double TOLERANCE = 0.3; // EAC.2014.02.19 - may benefit in compile-size by being static, consider changing to 0.05
    private static final int pickerChannel = 2;
    private static final AnalogChannel analogChannel = new AnalogChannel(pickerChannel);
    private static final Talon pickerMotor = new Talon(4);
    private double pickerAngleVoltage;
    private double pickerAngleDegree;
    private static final double MAX_ENCODER_VOLTAGE = 5.0;

    // Initialize your subsystem here
    public PickerPID() {
        super("PickerPID", Kp, Ki, Kd);

        getPIDController().setInputRange(VOLTAGE_CORRECTION - 5, VOLTAGE_CORRECTION);
        getPIDController().setOutputRange(-outputBounds, outputBounds);
        getPIDController().setContinuous(true);
        setAbsoluteTolerance(TOLERANCE);
        setSetpoint(position);//4.4
        // Use these to get going:
        // setSetpoint() -  Sets where the PID controller should move the system
        //                  to
        // enable() - Enables the PID controller.
    }

    public double getVoltage() {
        //return analogChannel.getVoltage();
        return VOLTAGE_CORRECTION - analogChannel.getVoltage();
    }

    public double getTalonValue() {

        return pickerMotor.get();
    }

    public void toggleDisable() {
        if (getPIDController().isEnable()) {
            disable();
        } else {
            enable();
        }
    }
    /*
     public void setNewSetpoint(double desiredPos) //added by John
     {
     this.desiredPos = desiredPos;
     posSet = false;
     }

     public void steppedSetpoint() //added by John
     {
     if (!posSet) {
     double currentPos = VOLTAGE_CORRECTION - analogChannel.getVoltage();
     //determines to go higher or lower
     if ((desiredPos - currentPos) > 0) {
     //if it is out of the step do a step else set to end
     if ((nextStep - currentPos) > TOLERANCE) {//change 0.025 to TOLERANCE variable

     } else if (Math.abs(desiredPos - currentPos) > step) {
     nextStep = currentPos + step;
     setSetpoint(nextStep);
     } else {
     setSetpoint(desiredPos);
     posSet = true;
     }
     } else {
     //if it is out of the step do a step else set to end
     if ((currentPos - nextStep) > TOLERANCE) {//change 0.025 to TOLERANCE variable
     } else if (Math.abs(desiredPos - currentPos) > step) {
     nextStep = currentPos - step;
     setSetpoint(nextStep);
     } else {
     setSetpoint(desiredPos);
     posSet = true;
     }
     }
     }
     }*/

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    protected double returnPIDInput() {
//        // Return your input value for the PID loop
//        // e.g. a sensor, like a potentiometer:
//        // yourPot.getAverageVoltage() / kYourMaxVoltage;
//        System.out.println("Picker Angle = " + getPickerAngle());
        //System.out.println(VOLTAGE_CORRECTION - analogChannel.getVoltage());
        SmartDashboard.putNumber("Picker Voltage Angle = ", VOLTAGE_CORRECTION - analogChannel.getVoltage());
        SmartDashboard.putNumber("Position", getPIDController().getSetpoint());
        return pickerAngleVoltage = VOLTAGE_CORRECTION - analogChannel.getVoltage(); //comment

    }

//    public double getPickerAngle() {
//        pickerAngleVoltage = analogChannel.getVoltage(); //comment
//        pickerAngleDegree = pickerAngleVoltage * (360 / MAX_ENCODER_VOLTAGE); //Converts Voltage to degrees
//        //System.out.println("Picker Angle = " + pickerAngleDegree);
//        return pickerAngleDegree;
//    }

    public void set(double input) {
        //System.out.println("Input :" + input);
        if (!getPIDController().isEnable()) {
            pickerMotor.set(input);

        }
        //System.out.println(getPIDController().isEnable());

    }

    protected void usePIDOutput(double output) {
        // Use output to drive your system, like a motor
        // e.g. yourMotor.set(output);
        pickerMotor.set(-output);  // EAC.2014.02.19 - Consider having the output be in the range of -10 to 10, then divide by the current battery line voltage, we'd have to test to make sure this isn't too dynamic
        //System.out.println("Picker PID Output = " + (-output));
        SmartDashboard.putNumber("Picker PID Output Value = ", (-output));
    }

//    public void disable() {
//        super.disable();
//    }

}
