
public class BattedBall {
	private String bbType;
	double exitVelocity = 0;
	double azimuth = 0;
	double distance = 0;
	double time = 0;
	double launchAngle = 0;
	boolean foul = false;
	
	public String getType() {
		return bbType;
	}
	
	public double getVelo() {
		return exitVelocity;
	}
	
	public double getAzimuth() {
		return azimuth;
	}
	
	public double getDistance() {
		return distance;
	}
	
	public double getTime() {
		return time;
	}
	
	public double getLaunch() {
		return launchAngle;
	}
	
	public boolean getFoul() {
		return foul;
	}
	
	public BattedBall(String type, double velocity, double az, double dist, double t, double launch, boolean f) {
		bbType = type;
		exitVelocity = velocity;
		azimuth = az;
		launchAngle = launch;
		distance = dist;
		time = t;
		foul = f;
	}
}
