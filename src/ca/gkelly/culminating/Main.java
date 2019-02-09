package ca.gkelly.culminating;

import ca.gkelly.culminating.loader.Loader;

public class Main{

	public static void main(String[] args) {
		Loader.directory = args[0];
		
		System.out.println(Loader.directory);
		
		Loader.load();
		
		System.out.println(Loader.vessels.get(0).name);
		
	}
	
}
