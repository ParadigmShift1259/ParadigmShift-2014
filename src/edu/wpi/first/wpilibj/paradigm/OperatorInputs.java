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

    //static variables can benefit compile size
    private double triggerPressed;
    private static final int A_BUTTON = 1;
    private static final int B_BUTTON = 2;
    private static final int X_BUTTON = 3;
    private static final int Y_BUTTON = 4;
    private static final int RIGHT_BUMPER = 6;
    private static final int LEFT_BUMPER = 5;
    private static final int BACK_BUTTON = 7;
    private static final int START_BUTTON = 8;
    private static final int XBOX_TRIGGER_RAW_AXIS = 3;
    private static final double RIGHT_TRIGGER_MIN = -1.0;
    private static final double RIGHT_TRIGGER_MAX = -0.5;
    private static final double LEFT_TRIGGER_MIN = 0.5;
    private static final double LEFT_TRIGGER_MAX = 1.0;
    private static final double DEADZONE_Y = 0.05;
    private static final double DEADZONE_X = 0.15;
    private Joystick joystick = new Joystick(1);
    private Joystick xBox = new Joystick(2);

//    public OperatorInputs() {
//        this.shiftUp = true;
//    }
    public OperatorInputs() {

    }

    public boolean xBoxStartButton() { //Returns true if start button is pressed
        return xBox.getRawButton(START_BUTTON);
    }

    public boolean isShooterTriggerPressed() {
        triggerPressed = xBox.getRawAxis(XBOX_TRIGGER_RAW_AXIS);
        //System.out.println("Trigger Pressed " + (RIGHT_TRIGGER_MIN <= triggerPressed && triggerPressed <= RIGHT_TRIGGER_MAX));
        return (RIGHT_TRIGGER_MIN <= triggerPressed && triggerPressed <= RIGHT_TRIGGER_MAX);
    }

    public boolean isLeftShooterTriggerPressed() {
        triggerPressed = xBox.getRawAxis(XBOX_TRIGGER_RAW_AXIS);
        return (LEFT_TRIGGER_MIN <= triggerPressed && triggerPressed <= LEFT_TRIGGER_MAX);

    }

    public boolean isSetKickerPositionButtonPressed() {
        triggerPressed = xBox.getRawAxis(XBOX_TRIGGER_RAW_AXIS);
        return (LEFT_TRIGGER_MIN <= triggerPressed && triggerPressed <= LEFT_TRIGGER_MAX);
    }

    public boolean xBoxBackButton() {
        return xBox.getRawButton(BACK_BUTTON);
    }

    public boolean button7() {
        return joystick.getRawButton(7);
    }

    public boolean isPickerLoadingPositionButtonPressed() {
        return xBox.getRawButton(A_BUTTON);
    }

    public boolean xBoxYButton() {
        return xBox.getRawButton(Y_BUTTON);
    }

    public boolean xBoxAButton() {
        return xBox.getRawButton(A_BUTTON);
    }

    public boolean xBoxBButton() {
        return xBox.getRawButton(B_BUTTON);
    }

    public boolean xBoxXButton() {
        return xBox.getRawButton(X_BUTTON);
    }

    public boolean xBoxRightBumper() {
        return xBox.getRawButton(RIGHT_BUMPER);
    }

    public boolean xBoxLeftBumper() {
        return xBox.getRawButton(LEFT_BUMPER);
    }

    public boolean isShootButtonPressed() {
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
        return deadzoneFilterX(this.joystick.getX());
    }

    public double joystickY() {
        return deadzoneFilterY(this.joystick.getY());
    }

    public double joystickZ() {
        return deadzoneFilterX(this.joystick.getZ());
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

    public boolean shifter() {
        return this.joystick.getTrigger();

    }

    public boolean joystickTriggerPressed() {
        return this.joystick.getTrigger();

    }

    public boolean joystickTriggerPressedAgain() {
        return this.joystick.getTrigger();

    }

}
