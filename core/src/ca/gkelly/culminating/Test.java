package ca.gkelly.culminating;

import ca.gkelly.culminating.vessels.VesselLoader;

//This is a class used for quick console tests that don't need the graphics
public class Test {

	public static void main(String[] args) {
		System.out.println("TESTING");
		
		VesselLoader.load();
		
		System.out.println(VesselLoader.vessels.get(0).name);
	}

}
