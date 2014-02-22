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
    private static final int A_BUTTON = 1; // EAC.2014.02.19 - may benefit in compile-size by being static
    private static final int B_BUTTON = 2; // EAC.2014.02.19 - may benefit in compile-size by being static
    private static final int X_BUTTON = 3; // EAC.2014.02.19 - may benefit in compile-size by being static
    private static final int Y_BUTTON = 4; // EAC.2014.02.19 - may benefit in compile-size by being static
    private static final int RIGHT_BUMPER = 6; // EAC.2014.02.19 - may benefit in compile-size by being static
    private static final int LEFT_BUMPER = 5; //this is is the poot butt // EAC.2014.02.19 - may benefit in compile-size by being static
    private static final int BACK_BUTTON = 7; // EAC.2014.02.19 - may benefit in compile-size by being static
    private static final int START_BUTTON = 8; // EAC.2014.02.19 - may benefit in compile-size by being static
    private static final int XBOX_TRIGGER = 3; // EAC.2014.02.19 - may benefit in compile-size by being static
    private static final double RIGHT_TRIGGER_MIN = -1.0; // EAC.2014.02.19 - may benefit in compile-size by being static
    private static final double RIGHT_TRIGGER_MAX = -0.5; // EAC.2014.02.19 - may benefit in compile-size by being static
    private static final double LEFT_TRIGGER_MIN = 0.5; // EAC.2014.02.19 - may benefit in compile-size by being static
    private static final double LEFT_TRIGGER_MAX = 1.0; // EAC.2014.02.19 - may benefit in compile-size by being static
    private Joystick joystick = new Joystick(1); //Created and initialized the joystick controller
    private Joystick xBox = new Joystick(2); //Created and initialized the xbox controller
    private static final double DEADZONE_Y = 0.05; // EAC.2014.02.19 - may benefit in compile-size by being static
    private static final double DEADZONE_X = 0.15; // EAC.2014.02.19 - may benefit in compile-size by being static

//    public OperatorInputs() {
//        this.shiftUp = true;
//    }
    public OperatorInputs() {

    }

    // EAC.2014.02.19 - do we want to remove this?
    public void readAll() {

    }
    
    public boolean startPressed()
    {
        return xBox.getRawButton(START_BUTTON);
    }
    
    // EAC.2014.02.19 - We may want to consider a more generic name
    public boolean isShooterTriggerPressed() {
        triggerPressed = xBox.getRawAxis(XBOX_TRIGGER);
        System.out.println("Trigger Pressed " + (RIGHT_TRIGGER_MIN <= triggerPressed && triggerPressed <= RIGHT_TRIGGER_MAX));
        return (RIGHT_TRIGGER_MIN <= triggerPressed && triggerPressed <= RIGHT_TRIGGER_MAX);
    }

    // EAC.2014.02.19 - We may want to consider a more generic name
    public boolean isSetKickerPositionButtonPressed() {
        triggerPressed = xBox.getRawAxis(XBOX_TRIGGER);
        return (LEFT_TRIGGER_MIN <= triggerPressed && triggerPressed <= LEFT_TRIGGER_MAX);
    }

    //change later
    public boolean backPressed() {
        return xBox.getRawButton(BACK_BUTTON);
    }

    // EAC.2014.02.19 - We may want to consider a more generic name
    public boolean isPickerLoadingPositionButtonPressed() {
        return xBox.getRawButton(A_BUTTON);
    }

    // EAC.2014.02.19 - We may want to consider a more generic name
    public boolean xBoxYButton() {
        return xBox.getRawButton(Y_BUTTON);
    }
    public boolean xBoxAButton() {
        return xBox.getRawButton(A_BUTTON);
    }

    // EAC.2014.02.19 - We may want to consider a more generic name
    public boolean xBoxBButton() {
        return xBox.getRawButton(B_BUTTON);
    }

    public boolean xBoxXButton() {
        return xBox.getRawButton(X_BUTTON);
    }

    // EAC.2014.02.19 - We may want to consider a more generic name
    public boolean xBoxRightBumper() {
        return xBox.getRawButton(RIGHT_BUMPER);
    }

    // EAC.2014.02.19 - We may want to consider a more generic name
    public boolean xBoxLeftBumper() {
        return xBox.getRawButton(LEFT_BUMPER);
    }

    // EAC.2014.02.19 - We may want to consider a more generic name
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
