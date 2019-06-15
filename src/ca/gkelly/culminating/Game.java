package ca.gkelly.culminating;

import java.util.HashMap;

import ca.gkelly.culminating.resources.PlayerResource;
import ca.gkelly.engine.graphics.DisplayMode;
import ca.gkelly.engine.graphics.Window;
import ca.gkelly.engine.loader.Loader;
import ca.gkelly.engine.util.Logger;

public class Game {
	static Window w;
	static GameManager gm = new GameManager();

	public static void main(String[] args) {
		@SuppressWarnings("rawtypes")
		HashMap<String, Class> resources = new HashMap<>();
		resources.put("player", PlayerResource.class);

		Loader.init(args[0], resources);
		Loader.load();
		
		w = new Window(new DisplayMode(DisplayMode.WINDOWED, 640, 480), gm);
		w.begin();
	}
}
