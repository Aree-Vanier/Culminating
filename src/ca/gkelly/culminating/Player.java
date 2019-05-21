package ca.gkelly.culminating;

import java.awt.Rectangle;

import ca.gkelly.culminating.resources.PlayerResource;
import ca.gkelly.engine.loader.Entity;

public class Player extends Entity{
	PlayerResource resource;

	int MAX_HEALTH;
	int health;
	int speed;
	int damage;
	int rate;

	public Player(PlayerResource r, int x, int y) {
		resource = r;

		image = r.image;
		MAX_HEALTH = r.health;
		speed = r.speed;
		damage = r.damage;
		rate = r.rate;

		rect = new Rectangle(x, y, image.getWidth(), image.getHeight());
	}
	
	public void move(double x, double y) {
		x *= speed;
		y *= speed;
		super.move((int) x, (int) y); 
	}

}
