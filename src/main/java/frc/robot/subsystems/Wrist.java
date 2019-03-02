package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import frc.robot.Functions;
import frc.robot.controlsystems.Joint;

public class Wrist implements Functions
{
    private static int TICKS_PER_REV =  1680;
    public static int VERTICAL = 1680/4;
    public static int HORIZENTAL = 0;

    private Joint joint;

    public Wrist(int motor, Encoder encoder)
    {
        joint = new Joint(motor, encoder, 0.03, 0.000015, 0.004, 40, TICKS_PER_REV, 10000, 1);
    }

    public void setPosition(int ticks)
    {
        //System.out.println();
        joint.goToPosition(ticks);
    }

    public void goToPosition()
    {
        joint.goToTargetPosition();
    }
}