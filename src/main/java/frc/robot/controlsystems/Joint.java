package frc.robot.controlsystems;

import edu.wpi.first.wpilibj.Encoder;

import edu.wpi.first.wpilibj.Talon;

import frc.robot.Functions;
import frc.robot.controlsystems.PIDController;

public class Joint implements Functions
{

  //CONSTANTS

  private int TICKS_PER_REV;
  private double TICKS_PER_DEGREE;
  private PIDController PID;

  private Talon motor;
  private Encoder encoder;
  public int referencePos = 0;
  public int targetPosition = 0;
  public static final int VERTICAL = 90;
  public static final int GROUND = 0;

  public static final int IN = 0;

  public Joint(int pin, Encoder encoder, double Kp, double Ki, double Kd, int maxError, int TICKS_PER_REV, int maxRate, double maxPower)
  {
    this.TICKS_PER_REV = TICKS_PER_REV;
    TICKS_PER_DEGREE = (double)TICKS_PER_REV/360d;
    motor = new Talon(pin);
    this.encoder = encoder;
    encoder.reset();
    PID = new PIDController(Kp, Ki, Kd, maxError, maxRate, maxPower);
    PID.setTargetPosition(0);
  }
  public int angleToTicks(double angle)
  {
    double ticks = TICKS_PER_DEGREE * angle;
    return (int) ticks;
  }

  public boolean reachedPosition()
  {
    return PID.reachedPosition();
  }

  public void goToPosition(double angle)
  {
    targetPosition = angleToTicks(angle);
    PID.setTargetPosition(referencePos + targetPosition);
    motor.set(PID.calculate(encoder.get()));
  }
  /*
  Set a reference angle for the joint, this should be used on the wrist so that it stays vertical
   when the shoulder moves.
  */
  public void setReferenceAngle(double angle)
  {
    referencePos = angleToTicks(angle);
    goToPosition(referencePos + targetPosition);
    motor.set(PID.calculate(encoder.get()));
  }
  public void setReferencePos(int pos)
  {
    referencePos = pos;
    goToPosition(referencePos + targetPosition);
    motor.set(PID.calculate(encoder.get()));
  }

  public int getReferencePos()
  {
    return referencePos;
  }

  public void goToTargetPosition()
  {
      System.out.print(PID.calculate(encoder.get()));
      System.out.print("\t");
      System.out.println(encoder.get());

      motor.set(-PID.calculate(encoder.get()));
  }


  public double ticksToAngle(int ticks)
  {
    double angle = (double) TICKS_PER_REV/(double) ticks;
    return angle;
  }
}