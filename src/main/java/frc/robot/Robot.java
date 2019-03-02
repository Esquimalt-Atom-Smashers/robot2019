package frc.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.cscore.*;

import edu.wpi.first.cameraserver.CameraServer;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import frc.robot.sensors.DistanceSensor;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Wrist;
import frc.robot.controlsystems.Joint;

import frc.robot.vision.*;
import frc.robot.controllers.*;
/**
* The Robot class is the master control class for Spike the robot.
*
* @author  Esquimalt Robotics
* @since   2018-02-01 
*/
public class Robot extends IterativeRobot implements Functions {
	private DifferentialDrive spike;
	private Joystick driveStick;
	private Joystick buttonStick;
	private Joystick wristStick;
	private ButtonPanel panel;
	private Joint shoulder;
	private Intake intake;
	//private Talon testymotor = new Talon(2);
	//private Encoder testyencoder = new Encoder(0, 1);
  
	UsbCamera camera;
	
	private Thread cameraThread;
	private CvSink imageGetter;
	private ObjectFinder ballFinder;
	
	private static final int CAMERA_WIDTH = 160;
	private static final int CAMERA_HEIGHT = 120;

	private Spark m_left;
	private Spark m_right;

	private Wrist wrist;
	
	
	@Override
	public void robotInit() {
		//wristEncoder = new Encoder(0, 1);
		//wrist = new Joint(2, wristEncoder, 0.004d, 0, 0, 5, 20*64, 600);

		m_left = new Spark(0);
		m_right = new Spark(1);
		spike = new DifferentialDrive(m_left, m_right); // the base and wheels

		driveStick = new Joystick(0); // controls spike
		//wristStick = new Joystick(2);
		//ballFinder = new ObjectFinder(ObjectFinder.Objects.BALL);
		//panel = new ButtonPanel(9, 1);
		//intake = new Intake(3, 2);
		wrist = new Wrist(2, new Encoder(8, 9));
		//System.out.println("Initializing robot");

		CameraServer.getInstance().startAutomaticCapture().setResolution(CAMERA_WIDTH, CAMERA_HEIGHT);
		CameraServer.getInstance().startAutomaticCapture().setResolution(CAMERA_WIDTH, CAMERA_HEIGHT);
		imageGetter = CameraServer.getInstance().getVideo();
	}
	
	
	@Override
	public void teleopPeriodic() {
		
		//spike.arcadeDrive(stick.getRawAxis(1), stick.getRawAxis(0));
		//double mapVal = map(wristStick.getRawAxis(1), -1, 1, 0, 360);
		//wrist.goToPosition(mapVal);
		//if (panel.isDown(ButtonPanel.TOP_LEFT)) wrist.setPosition(90);
		//if (panel.isDown(ButtonPanel.TOP_RIGHT)) wrist.setPosition(0);
		//wrist.goToPosition();
		//panel.update();
		spike.arcadeDrive(clamp(driveStick.getRawAxis(1), -0.7, 0.7), clamp(driveStick.getRawAxis(2), -0.7, 0.7));
	

}
	
}// end of Robot Class