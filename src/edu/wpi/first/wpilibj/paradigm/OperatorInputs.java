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

    //variables
    private double triggerPressed;
    private final int A_BUTTON = 1;
    private final int B_BUTTON = 2;
    private final int X_BUTTON = 3;
    private final int Y_BUTTON = 4;
    private final int RIGHT_BUMPER = 6;
    private final int LEFT_BUMPER = 5; //this is is the poot butt
    private final int BACK_BUTTON = 7;
    private final int START_BUTTON = 8;
    //private final Joystick.AxisType LEFT_TRIGGER = Joystick.AxisType.kZ;
    private final int XBOX_TRIGGER = 3;

    private final double RIGHT_TRIGGER_MIN = -1.0;
    private final double RIGHT_TRIGGER_MAX = -0.5;
    private final double LEFT_TRIGGER_MIN = 0.5;
    private final double LEFT_TRIGGER_MAX = 1.0;

    private Joystick joystick = new Joystick(1); //Created and initialized the joystick controller
    private Joystick xBox = new Joystick(2); //Created and initialized the xbox controller
    final double DEADZONE_Y = 0.05;
    final double DEADZONE_X = 0.15;

//    public OperatorInputs() {
//        this.shiftUp = true;
//    }
    public OperatorInputs() {

    }

    public void readAll() {

    }
    
    public boolean startPressed()
    {
        return xBox.getRawButton(START_BUTTON);
    }
    public boolean isShooterTriggerPressed() {
        triggerPressed = xBox.getRawAxis(XBOX_TRIGGER);
        System.out.println("Trigger Pressed " + (RIGHT_TRIGGER_MIN <= triggerPressed && triggerPressed <= RIGHT_TRIGGER_MAX));
        return (RIGHT_TRIGGER_MIN <= triggerPressed && triggerPressed <= RIGHT_TRIGGER_MAX);
    }

    public boolean isSetKickerPositionButtonPressed() {
        triggerPressed = xBox.getRawAxis(XBOX_TRIGGER);
        return (LEFT_TRIGGER_MIN <= triggerPressed && triggerPressed <= LEFT_TRIGGER_MAX);
    }

    //change later
    public boolean backPressed() {
        return xBox.getRawButton(BACK_BUTTON);
    }

    public boolean isPickerLoadingPositionButtonPressed() {
        return xBox.getRawButton(A_BUTTON);
    }

    public boolean isPickerKickingPositionButtonPressed() {
        return xBox.getRawButton(Y_BUTTON);
    }

    public boolean isPickingAutoPositionButtonPressed() {
        return xBox.getRawButton(B_BUTTON);
    }

    public boolean xBoxXButton() {
        return xBox.getRawButton(X_BUTTON);
    }

    public boolean isGrabbingButtonPressed() {
        return xBox.getRawButton(RIGHT_BUMPER);
    }

    public boolean isReleaseButtonPressed() {
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

    public boolean shifter() {
        return this.joystick.getTrigger();  //return the value of the joystick trigger

    }

    public boolean joystickTriggerPressed() {
        return this.joystick.getTrigger();  //return the value of the joystick trigger

    }

    public boolean joystickTriggerPressedAgain() {
        return this.joystick.getTrigger();  //return the value of the joystick trigger

    }

    public boolean shiftLow() {
        return this.joystick.getRawButton(3);
    }

    public boolean shiftHigh() {
        return this.joystick.getRawButton(4);
    }

    public boolean getStart() {
        return this.xBox.getRawButton(8);
    }

}
