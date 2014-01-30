/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.paradigm;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Compressor;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the
 * IterativeAdventureRick documentation. If you change the name of this class or
 * the package after creating this project, you must also update the manifest
 * file in the resource directory.
 */
public class AdventureRick extends IterativeRobot {
    //electromagic!
    
    DriveTrain drive;
    DriverControls operatorInputs;
    Compressor compressor;
    Shooter shoot;
    Picker pick;
    
    final int PRESSURE_SWITCH_CHANNEL = 1;
    final int COMPRESSOR_RELAY_CHANNEL = 1;

    /**
     * Initializes when the robot first starts, (only once at power-up).
     */
    public void robotInit() {
        operatorInputs = new DriverControls();
        drive = new DriveTrain(operatorInputs);
        //pressureSwitchChannel - The GPIO channel that the pressure switch is attached to.
        //compressorRelayChannel - The relay channel that the compressor relay is attached to.
        compressor = new Compressor(PRESSURE_SWITCH_CHANNEL, COMPRESSOR_RELAY_CHANNEL);
        shoot = new Shooter(operatorInputs);//add parameters as needed
        pick = new Picker(operatorInputs);//add parameters as needed
        compressor.start();
    }

    /**
     * This function is called periodically (every 20-25 ms) during autonomous
     */
    public void autonomousPeriodic() {
        
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        drive.setPower();
        //remove if not needed
        //compressor.start();
        drive.shift();  //shift when the trigger is pressed
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {

    }

}
