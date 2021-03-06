package ca.gkelly.culminating.managers;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import ca.gkelly.culminating.Player;
import ca.gkelly.culminating.resources.Bullet;
import ca.gkelly.culminating.resources.PlayerResource;
import ca.gkelly.engine.Manager;
import ca.gkelly.engine.collision.Collider;
import ca.gkelly.engine.collision.Hull;
import ca.gkelly.engine.graphics.Camera;
import ca.gkelly.engine.loader.Loader;
import ca.gkelly.engine.tilemaps.TileMap;
import ca.gkelly.engine.util.Logger;
import ca.gkelly.engine.util.Vector;
import ca.gkelly.engine.util.Vertex;

public class GameManager extends Manager {

	TileMap map;
	Camera cam;
	Player player;
	ArrayList<Bullet> bullets = new ArrayList<Bullet>();

	@Override
	public void init(Container container) {
		map = Loader.maps.get("map");
		map.load();
		cam = new Camera(container, map);
		cam.setPosition(0, 0, 1.25);

		Vertex spawnPoint = map.getLayer("colliders").getPoints()[0].position;
		
		// Create a player, from the loaded resource
		player = new Player((PlayerResource) Loader.resources.get("player").get(0), (int) spawnPoint.x, (int) spawnPoint.y);
	}

	@Override
	public void render(Graphics2D g) {
		cam.begin();
		player.render(cam);

		for (Bullet b : new ArrayList<Bullet>(bullets)) {
			b.render(cam);
		}

		// This code is used to demonstrate collision detection polygons, it does not
		// affect positions
		{
//			map.getLayer("colliders").render(cam, Color.RED);
			cam.drawPoly(map.getLayer("colliders").findByType("trigger")[0].getCollider().getPoly(), Color.blue);
			Vertex pos = cam.worldSpace(mouse.pos);
			Collider p = map.getCollider(pos.x, pos.y, "colliders");
			if (p != null) {
				cam.drawPoly(p.getPoly(), Color.black);
			}

			Collider[] colliders = map.getColliders("colliders");
			if (colliders != null) {
				for (Collider c : colliders) {
					Hull raw = player.collider.getCollisionHull(c);
					Collider p2 = raw != null ? raw.poly : null;
					if (p2 != null) {
						raw.render(cam, new Hull(player.collider.getIntersections(c)));
						Vector offset = new Vector(p2.x - player.x, p2.y - player.y);
						offset.setMag(-offset.getMagnitude());
						cam.drawLine((int) player.x, (int) player.y, (int) (player.x + offset.getX()),
								(int) (player.y + offset.getY()), 5, Color.red);
					}
				}
			}
		}

		cam.finish(g);
		Logger.newLine(Logger.DEBUG);
	}

	@Override
	public void update() {
		player.update();
		for (Bullet b : new ArrayList<Bullet>(bullets)) {
			b.update();
			Polygon p = map.getCollider(b.getX(), b.getY(), "colliders").getPoly();
			if (p != null) {
				bullets.remove(b);
			}
		}

		if (keyboard.pressed.contains(KeyEvent.VK_W))
			player.move(0, -1.0, map.getColliders("colliders"));
		if (keyboard.pressed.contains(KeyEvent.VK_S))
			player.move(0, 1.0, map.getColliders("colliders"));
		if (keyboard.pressed.contains(KeyEvent.VK_A))
			player.move(-1.0, 0, map.getColliders("colliders"));
		if (keyboard.pressed.contains(KeyEvent.VK_D))
			player.move(1.0, 0, map.getColliders("colliders"));

		if (keyboard.pressed.contains(KeyEvent.VK_Q))
			cam.zoom(0.05);
		if (keyboard.pressed.contains(KeyEvent.VK_E))
			cam.zoom(-0.05);

		cam.setPosition((int) player.x, (int) player.y);
	}

	@Override
	public void end() {

	}

	@Override
	public void onMousePress(MouseEvent e) {
		Vertex pos = cam.worldSpace(e.getX(), e.getY());

		if (player.contains(pos)) {
			return;
		}

		Vector vel = new Vector(pos.x - player.x, pos.y - player.y);
		vel.setMag(3);

		Vector extraVel = player.getVelocity().getAtAngle(vel.getAngle(false));
		bullets.add(new Bullet((int) player.x, (int) player.y, vel, extraVel));
	}

}
