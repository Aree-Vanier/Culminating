package ca.gkelly.culminating;

import java.util.HashMap;

import ca.gkelly.culminating.resources.PlayerResource;
import ca.gkelly.engine.graphics.DisplayMode;
import ca.gkelly.engine.graphics.Window;
import ca.gkelly.engine.loader.Loader;

public class Game {
	static Window w;
	static GameManager gm;

	public static void main(String[] args) {
		@SuppressWarnings("rawtypes")
		HashMap<String, Class> resources = new HashMap<>();
		resources.put("player", PlayerResource.class);

		Loader.init(args[0], resources);
		Loader.load();

		gm = new GameManager(args);
		w = new Window(new DisplayMode(DisplayMode.WINDOWED, 640, 480), gm);
	}
}
