/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.paradigm;

import edu.wpi.first.wpilibj.Joystick;

/**
 *
 * @author Programming
 */
public class OperatorInputs {

    private Joystick joystick = new Joystick(1);
    private Joystick xBox     = new Joystick(2);
    
    private final int A_BUTTON           = 1;
    private final int B_BUTTON           = 2;
    private final int X_BUTTON           = 3;
    private final int Y_BUTTON           = 4;
    private final int LEFT_BUMPER        = 5;
    private final int RIGHT_BUMPER       = 6;
    private final int BACK_BUTTON        = 7;
    private final int START_BUTTON       = 8;
    private final int LEFT_STICK_BUTTON  = 9;
    private final int RIGHT_STICK_BUTTON = 10;
    
    private final Joystick.AxisType LEFT_TRIGGER  = Joystick.AxisType.kZ;
    private final Joystick.AxisType RIGHT_TRIGGER = Joystick.AxisType.kZ;

    final double DEADZONE_Y = 0.05; // for joystick in Y direction
    final double DEADZONE_X = 0.15; // for joystick in X direction

    private final double LEFT_TRIGGER_PRESSED_MAX_VALUE  =  1.0;
    private final double LEFT_TRIGGER_PRESSED_MIN_VALUE  =  0.5;
    private final double RIGHT_TRIGGER_PRESSED_MAX_VALUE = -0.5;
    private final double RIGHT_TRIGGER_PRESSED_MIN_VALUE = -1.0;

    public OperatorInputs() {

    }
    
    public boolean isXboxRightTriggerPressed() {
        double triggerValue = xBox.getAxis(RIGHT_TRIGGER);
        return ((triggerValue >= RIGHT_TRIGGER_PRESSED_MIN_VALUE) &&
                (triggerValue <= RIGHT_TRIGGER_PRESSED_MAX_VALUE));
    }
    
    public boolean isXboxLeftTriggerPressed() {
        double triggerValue = xBox.getAxis(LEFT_TRIGGER);
        return ((triggerValue >= LEFT_TRIGGER_PRESSED_MIN_VALUE) &&
                (triggerValue <= LEFT_TRIGGER_PRESSED_MAX_VALUE));
    }
    
    public boolean isXboxAButtonPressed() {
        return (xBox.getRawButton(A_BUTTON));
    }
    
    public boolean isXboxBButtonPressed() {
        return (xBox.getRawButton(B_BUTTON));
    }
    
    public boolean isXboxXButtonPressed() {
        return (xBox.getRawButton(X_BUTTON));
    }
        
    public boolean isXboxYButtonPressed() {
        return (xBox.getRawButton(Y_BUTTON));
    }
    
    public boolean isXboxBackButtonPressed() {
        return (xBox.getRawButton(BACK_BUTTON));
    }

    public boolean isXboxRightBumperPressed() {
        return xBox.getRawButton(RIGHT_BUMPER);
    }

    public boolean isXboxLeftBumperPressed() {
        return xBox.getRawButton(LEFT_BUMPER);
    }

    public double xboxRightX() {
        return deadzoneFilterX(this.xBox.getX(Joystick.Hand.kRight));
    }

    public double xboxRightY() {
        return deadzoneFilterY(this.xBox.getY(Joystick.Hand.kRight));
    }

    public double xboxLeftX() {
        return deadzoneFilterX(this.xBox.getX(Joystick.Hand.kLeft));
    }

    public double xboxLeftY() {
        return deadzoneFilterY(this.xBox.getY(Joystick.Hand.kLeft));
    }

    public double joystickX() {
        return deadzoneFilterX(this.joystick.getX()); //return the value of the x-axis of the joystick controller
    }

    public double joystickY() {
        return deadzoneFilterY(this.joystick.getY()); //return the value of the y-axis of the joystick controller
    }

    public double joystickZ() {
        return deadzoneFilterX(this.joystick.getZ()); //return the value of the z-axis of the joystick controller
    }

    /**
     * returns a value 0 if the joystick value is within the dead zone (if the
     * joystick is outside of the dead zone it returns the joystick value)
     *
     * @param joyStickValue
     * @return
     */
    private double deadzoneFilterY(double joyStickValue) {
        if (Math.abs(joyStickValue) <= DEADZONE_Y) {
            return 0;
        }
        return joyStickValue;

    }

    private double deadzoneFilterX(double joyStickValue) {
        if (Math.abs(joyStickValue) <= DEADZONE_X) {
            return 0;
        }
        return joyStickValue;

    }

    public boolean joystickTriggerPressed() {
        return this.joystick.getTrigger();  //return the value of the joystick trigger

    }

    ///////////////////// REMOVE THESE
    
    public boolean shifter() {
        return this.joystick.getTrigger();  //return the value of the joystick trigger
    }
    public boolean shiftLow() {
        return this.joystick.getRawButton(3);
    }
    public boolean shiftHigh() {
        return this.joystick.getRawButton(4);
    }
    public boolean isCaliButtonPressed() {
        return isXboxBackButtonPressed();
    }
    public boolean isPickingAutoPositionButtonPressed() {
        return xBox.getRawButton(Y_BUTTON);
    }

}
