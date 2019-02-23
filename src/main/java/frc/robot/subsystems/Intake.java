package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;

public class Intake
{
    public static final int IN = 1;
    public static final int ZREO = 0;
    public static final int OUT = -1;
    private double power  = 0;
    private boolean hasBall;


    private Talon motor;
    private DigitalInput limitSwitch;

    public Intake(int MotorPin, int limitPin)
    {
        motor = new Talon(MotorPin);
        limitSwitch = new DigitalInput(limitPin);
    }

    public void setMode(int mode)
    {
        this.power = mode;
    }

    public void update()
    {
        hasBall = !limitSwitch.get(); //flip the value beacuse the roborio gets it backwards for some reason
        motor.set(power);
        if (hasBall) motor.set(0);
    }

    public boolean hasBall()
    {
        return hasBall;
    }

}