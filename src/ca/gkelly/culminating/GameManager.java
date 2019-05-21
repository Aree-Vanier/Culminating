package ca.gkelly.culminating;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import ca.gkelly.culminating.resources.PlayerResource;
import ca.gkelly.engine.Manager;
import ca.gkelly.engine.graphics.Camera;
import ca.gkelly.engine.graphics.TileMap;
import ca.gkelly.engine.loader.Loader;
import ca.gkelly.engine.util.Logger;

public class GameManager extends Manager {

	TileMap map;
	Camera cam;
	Player player;

	public GameManager(String[] args) {
		map = new TileMap(args[0] + "\\maps\\map.tmx");
	}

	@Override
	public void init(Container container) {
		map.load();
		cam = new Camera(container, map);
		cam.setPosition(0, 0, 1.25);

		player = new Player((PlayerResource) Loader.resources.get("player").get(0), 0, 0);
	}

	@Override
	public void render(Graphics g) {
		cam.begin();
		player.render(cam);

		cam.finish(g);
		Logger.newLine(Logger.DEBUG);
	}

	@Override
	public void update() {
		if (keyboard.pressed.contains(KeyEvent.VK_W))
			player.move(0, -1.0);
		if (keyboard.pressed.contains(KeyEvent.VK_S))
			player.move(0, 1.0);
		if (keyboard.pressed.contains(KeyEvent.VK_A))
			player.move(-1.0, 0);
		if (keyboard.pressed.contains(KeyEvent.VK_D))
			player.move(1.0, 0);

		cam.setPosition(player.getX(), player.getY(), 1.25);
	}

	@Override
	public void end() {

	}

}
