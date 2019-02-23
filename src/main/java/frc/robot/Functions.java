package frc.robot;


public interface Functions 
{
	default double map(double val, double lbound1, double ubound1, double lbound2, double ubound2)
	{
		double multiplier = (ubound2 - lbound2)/(ubound1 - lbound1);
		return (multiplier * val + lbound2 - lbound1);
	}

	default double clamp( double val, double min, double max)
	{
		if (val > max) return max;
		if (val < min) return min;
		return val;
	}

	default double ticksToAngle(int ticks, int TICKS_PER_REV)
  	{
    	double angle = (double) ticks/(double) TICKS_PER_REV;
    	return angle;
	}

	default int angleToTicks(int TICKS_PER_REV, double angle)
	{
		double ticksPerDegree = (double) TICKS_PER_REV/360d;
		double temp = (angle * ticksPerDegree);
		return (int) temp;
	}
	
}
