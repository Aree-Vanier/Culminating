package ca.gkelly.culminating;

import ca.gkelly.culminating.vessels.MountPoint;
import ca.gkelly.culminating.vessels.Vessel;

//This is a class used for quick console tests that don't need the graphics
public class Test {

	public static void main(String[] args) {
		System.out.println("TESTING");
		
		MountPoint[] mountPoints = {new MountPoint(0,0,MountPoint.Type.LIGHT)};
		
		
		
		
		Vessel ptBoat = new Vessel("Battleship", mountPoints);
	}

}
