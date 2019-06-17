package ca.gkelly.culminating;

import java.util.HashMap;

import ca.gkelly.culminating.managers.GameManager;
import ca.gkelly.culminating.managers.MenuManager;
import ca.gkelly.culminating.resources.PlayerResource;
import ca.gkelly.engine.Window;
import ca.gkelly.engine.graphics.DisplayMode;
import ca.gkelly.engine.loader.Loader;
import ca.gkelly.engine.loader.Resource;

public class Game {
	static Window window;
	public static GameManager gm = new GameManager();
	public static MenuManager mm = new MenuManager();

	public static void main(String[] args) {
		HashMap<String, Class<? extends Resource>> resources = new HashMap<>();
		resources.put("player", PlayerResource.class);

		Loader.init(args[0], resources);
		Loader.load();
		
		window = new Window(new DisplayMode(DisplayMode.WINDOWED, 640, 480), mm);
		window.begin();
	}
}
