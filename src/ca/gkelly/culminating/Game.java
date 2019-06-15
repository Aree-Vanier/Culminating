package ca.gkelly.culminating;

import java.util.HashMap;

import ca.gkelly.culminating.managers.GameManager;
import ca.gkelly.culminating.managers.MenuManager;
import ca.gkelly.culminating.resources.PlayerResource;
import ca.gkelly.engine.Window;
import ca.gkelly.engine.graphics.DisplayMode;
import ca.gkelly.engine.loader.Loader;

public class Game {
	static Window window;
	static GameManager gm = new GameManager();
	static MenuManager mm = new MenuManager();

	public static void main(String[] args) {
		@SuppressWarnings("rawtypes")
		HashMap<String, Class> resources = new HashMap<>();
		resources.put("player", PlayerResource.class);

		Loader.init(args[0], resources);
		Loader.load();
		
		window = new Window(new DisplayMode(DisplayMode.WINDOWED, 640, 480), gm);
		window.begin();
	}
}
