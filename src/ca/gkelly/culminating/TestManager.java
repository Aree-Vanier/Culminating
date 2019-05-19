package ca.gkelly.culminating;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import ca.gkelly.culminating.engine.Manager;
import ca.gkelly.culminating.entities.Mount;
import ca.gkelly.culminating.entities.Ship;
import ca.gkelly.culminating.entities.Weapon;
import ca.gkelly.culminating.graphics.Camera;
import ca.gkelly.culminating.graphics.TiledMap;
import ca.gkelly.culminating.loader.Loader;
import ca.gkelly.culminating.util.Logger;

public class TestManager extends Manager {
	TiledMap m;
	int x = 256;
	int y = 256;
	double zoom = 1;

	Camera cam;
	Ship s;
	Polygon selectedPoly = null;
	String[] args;

	public TestManager(String[] args) {
		this.args = args;
	}
	
	@Override
	public void init(Container container) {
		m = new TiledMap(args[0] + "\\maps\\test.tmx");
		Logger.log(Logger.INFO, m.load());

		Logger.log(Logger.DEBUG, m.doc.getDocumentElement().getNodeName());

		Loader.init(args[0]);
		Loader.load();

		cam = new Camera(container, m);
		Mount[] m = { new Weapon(Loader.mounts.get(0), 0, 0), new Weapon(Loader.mounts.get(0), 20, 10) };
		s = new Ship(Loader.vessels.get(0), 100, 100, m);

	}

	@Override
	public void render(Graphics g) {
		if (m == null)
			return;
		if (m.tileset == null)
			return;
		g.setColor(Color.black);
//		g.fillRect(0, 0, getWidth(), getHeight());

		cam.begin();

		Logger.epoch("BOAT");
		s.render(cam);
		Logger.log("Ship drawn in {BOAT}");
		cam.drawRect(s.rect.x, s.rect.y, s.rect.width, s.rect.height, Color.red);

		if (selectedPoly != null) {
			cam.drawPoly(selectedPoly, Color.RED);
		}

		cam.finish(g);
		Logger.newLine(Logger.DEBUG);
	}

	@Override
	public void update() {
		Logger.log("Update");
		if (keyboard.pressed.contains(KeyEvent.VK_W))
			y--;
		if (keyboard.pressed.contains(KeyEvent.VK_S))
			y++;
		if (keyboard.pressed.contains(KeyEvent.VK_A))
			x--;
		if (keyboard.pressed.contains(KeyEvent.VK_D))
			x++;

		if (keyboard.pressed.contains(KeyEvent.VK_E))
			zoom += 0.1;
		if (keyboard.pressed.contains(KeyEvent.VK_Q))
			zoom -= 0.1;

		cam.setPosition(x, y, zoom);
	}

	@Override
	public void interrupt() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void onMouseMoved(MouseEvent e) {
		selectedPoly = m.getPoly(cam.worldSpace(e.getX(), 0)[0], cam.worldSpace(0, e.getY())[1], "land");
	}

}
