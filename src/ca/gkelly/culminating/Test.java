package ca.gkelly.culminating;

import ca.gkelly.engine.graphics.DisplayMode;
import ca.gkelly.engine.graphics.Window;
import ca.gkelly.engine.util.Logger;

//This is a class used for quick console tests that don't need the graphics
public class Test {

	TestManager manager;
	
	public static void main(String[] args) {
		Logger.log(Logger.INFO, "TESTING");
		new Test(args);
	}

	public Test(String[] args) {
		manager = new TestManager(args);
		
		new Window(new DisplayMode(DisplayMode.WINDOWED, 640, 480), manager);
	}

}
