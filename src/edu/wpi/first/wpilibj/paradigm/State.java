/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.paradigm;

/**
 *
 * @author 212006372
 */
public class State {
    public final static int KICKING = 1;
    public final static int KICKER_STOPPING = 2;
    public final static int KICKER_STOPPED = 3;
    public final static int KICKER_CALIBRATING = 4;
    public final static int KICKER_CALIBRATED = 5;
    
    public final static int PICKER_MOVING_TO_PICK = 10;
    public final static int PICKER_IN_PICKING_POSITION = 11;
    public final static int PICKER_MOVING_TO_KICK = 12;
    public final static int PICKER_IN_KICKING_POSITION = 13;
    public final static int PICKER_MOVING_TO_STARTING_POSITION = 14;
    public final static int PICKER_IN_STARTING_POSITION = 15;
    
    public final static int PICKER_WHEELS_STOPPED = 20;
    public final static int PICKER_WHEELS_FORWARD = 21;
    public final static int PICKER_WHEELS_REVERSE = 22;
    
    public final static int BALL_IN_KICKING_POSITION = 30;
    public final static int BALL_NOT_IN_KICKING_POSITION = 31;
    
    public static int kickerMode;
    public static int pickerMode;
    public static int pickerWheelsMode;
    public static int ballLocationMode;
    
}
