package ca.gkelly.culminating.vessels;

public class Vessel {
	
	public MountPoint[] mountPoints;
	public String name;
	
	public Vessel(String name, MountPoint[] mountPoints) {
		this.name = name;
		this.mountPoints = mountPoints;
		
	}
	
}
