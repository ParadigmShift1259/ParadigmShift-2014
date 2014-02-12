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

/**
 *
 * @author Doug Dimmadome
 */
public class Shooter {
    
    private final int PWM_TALON_PORT = 5;
    //the current value can not possibly be the previous value the first time through
    private final double ILLEGAL_VOLTAGE = -9999.9; //can't be stopped when it hasn't started
    private final Talon kickermotor = new Talon(PWM_TALON_PORT);

    private double motorSpeed = 1.0;
    private final AnalogChannel analogChannel = new AnalogChannel(1);
    private final DigitalInput digitalInput = new DigitalInput(9);
    private double previousVoltage = ILLEGAL_VOLTAGE;
    private double currentVoltage;
    private double kickingPos = 180;
    private final double MAX_ENCODER_VOLTAGE = 2.0;

    public Shooter() {
        
    }
    
    public double getKickerMotorPower() {
        return kickermotor.get();
    }
    
    public boolean isKickerStopped() {
        currentVoltage = analogChannel.getVoltage(); //Read current voltage
        if (previousVoltage == currentVoltage) { 
            previousVoltage = ILLEGAL_VOLTAGE; //Set back so it can run again 
            return true;
        } else {
            previousVoltage = currentVoltage; //current becomes previous
            return false;
        }
        
    }
    
    /**
     * kick()
     * 
     */
    public void kick() {
        kickermotor.set(-0.7); //negative is kicking forward - 2/8/2014 E A Cobb
    }
    
    public void stopKicker() {
        kickermotor.set(0);
    }
    
    public boolean calibrate() {
        boolean inPosition = digitalInput.get();
        double angle = getKickerAngle();

        try {
            if (inPosition) {
                kickermotor.set(0);
            } else {
                if (angle > kickingPos && angle <= 165) {
                    kickermotor.set(0.1);
                } else if (angle > kickingPos || angle <= 145) {
                    kickermotor.set(-0.1);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return (inPosition);
    }
    
    public double getKickerAngle() {
        double angle = analogChannel.getVoltage();
        //This is the porportion to convert voltage into a degrees angle.
        //There are 360 degree permax encoder voltage.
        angle = angle * (360/MAX_ENCODER_VOLTAGE);
        return angle;
    }
    
    public void manualShooterControl(double motorControlValue) {
        kickermotor.set(motorControlValue);
    }  
    
    /**
     * 
     * @return 
     */
    public boolean setKickingPosition() {
        boolean inPosition = false;
        double angle = getKickerAngle();
        if (angle == kickingPos) {
            inPosition = true;
        }
        try {
            if (inPosition) {
                kickermotor.set(0);
            } else {
                if (angle < kickingPos && angle >= 165) {
                    kickermotor.set(-0.1);
                } else if (angle > kickingPos || angle <= 145) {
                    kickermotor.set(0.1);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return (inPosition);
    }
    
   //need to figure out moveable parts on the shooting mechanism before adding buttons/functions 
}
