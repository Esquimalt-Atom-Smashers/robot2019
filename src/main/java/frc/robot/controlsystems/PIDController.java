package frc.robot.controlsystems;
import edu.wpi.first.wpilibj.Encoder;

import edu.wpi.first.wpilibj.Talon;
import frc.robot.Functions;
public class PIDController implements Functions
{
    //CONSTANTS

  private double Ki = 0;
  private double Kd = 0;
  private double Kp = 0;

  private int targetPosition = 0;
  private int currentPosition = 0;
  private int maxError;
  private double maxPower = 0.2;
  private int maxRate;
  private double lastError = 0;
  private double Kover = 1.5;

  private double integral = 0d;

  private long lastT = 0;


  public PIDController(double Kp, double Ki, double Kd, int maxError, int maxRate)
  {
    this.Kp = Kp;
    this.Ki = Ki;
    this.Kd = Kd;
    this.maxError = maxError;
    this.maxRate = maxRate;
  }
  
  public void setKp( double newKp ) 
  {
    this.Kp = newKp;
  }

  public void setKi( double newKi ) 
  {
    this.Ki = newKi;
  }

  public void setKd( double newKd ) 
  {
   this.Kd = newKd;
  }
     
  public void setTargetPosition(int pos) 
  {
    this.targetPosition = pos;
    integral = 0d;
    lastError = 0.0;
  }

  public double calculate(int newPos) {
      try {
        this.currentPosition = newPos;           // calculate error, error integral and error derivative
        double error = targetPosition - currentPosition;          
        long now = System.currentTimeMillis();
        double deltaT = (double) (now - this.lastT)/1000;
        this.integral += (error * deltaT);
        
        double derivative = (error - this.lastError)/deltaT; //gets the average rate of change over the last period
        //store the current error for the next calculations

        this.lastError = error;            
        //calculate the output power
        double power = this.Kp*error + this.Ki*this.integral + this.Kd*derivative;
          
        //limits the output power of the motor is may be overrided by the rate limiting code below 
        power = clamp(power, -maxPower, maxPower);
        /*
        This is too stop the motors from moving too fast, if this PID is controlling the arm 
        it would be bad for it slam down, below checks the current rate of the PID and compares
        it to the max rate spificed if it is over this will reduce the power output to the motor
        to try and compensate.
        */
        if (Math.abs(derivative) > maxRate)
        {
            power = maxPower * (maxRate/(derivative * Kover));
        }   
        // why "-power"?  Because the power goes the opposite direction to the encoder?
        this.lastT = now;
        return -power;

      } catch (Exception e) { //this is incase of divide by zero erros 
        e.printStackTrace();
        return 0;
      }
  }   
  
  public int getPosition() {
    return currentPosition;
  }   
  //this might be helpful for autonomous programming, just retuns if the PID has reached a point a given range
  public boolean reachedPosition()
  {
    return Math.abs(currentPosition - targetPosition) < maxError;
  }
}