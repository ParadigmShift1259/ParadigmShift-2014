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
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    DriveTrain drive;
    DriverControls controls;
    Compressor compressor;
    Shooter shoot;
    Picker pick;

    public void robotInit() {
        controls = new DriverControls();
        drive = new DriveTrain(controls);
        compressor = new Compressor(1, 1);
        //pressureSwitchChannel - The GPIO channel that the pressure switch is attached to.
        //compressorRelayChannel - The relay channel that the compressor relay is attached to.
        shoot = new Shooter();//add parameters as needed
        pick = new Picker();//add parameters as needed
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        compressor.start();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        //drive.setPower();
        //compressor.start();
        drive.shift();
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {

    }

}
