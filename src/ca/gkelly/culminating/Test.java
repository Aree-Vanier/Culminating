package ca.gkelly.culminating;

import ca.gkelly.culminating.loader.Loader;

//This is a class used for quick console tests that don't need the graphics
public class Test {

	public static void main(String[] args) {
		System.out.println("TESTING");
		
		Loader.load();
		
		System.out.println(Loader.vessels.get(0).name);
	}

}
