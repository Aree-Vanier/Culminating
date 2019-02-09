package ca.gkelly.culminating;

import javax.swing.JFrame;

import ca.gkelly.culminating.graphics.Window;
import ca.gkelly.culminating.loader.Loader;

public class Main extends JFrame{

	public static final String NAME = "FLEET";
	public static Window window;
	
	public static void main(String[] args) {
		Loader.directory = args[0];
		
		System.out.println(Loader.directory);
		
		Loader.load();
		
		System.out.println(Loader.vessels.get(0).name);
		
		window = new Window(NAME, 1280,720);
		
	}
	
}
