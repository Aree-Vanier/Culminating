package ca.gkelly.culminating;

import java.awt.Shape;

import ca.gkelly.culminating.resources.PlayerResource;
import ca.gkelly.engine.collision.Collider;
import ca.gkelly.engine.collision.RectCollider;
import ca.gkelly.engine.loader.Entity;

public class Player extends Entity{
	PlayerResource resource;

	int MAX_HEALTH;
	int health;
	int speed;
	int damage;
	int rate;
	RectCollider rc;

	public Player(PlayerResource r, int x, int y) {
		super(r.image.getWidth(), r.image.getHeight());
		resource = r;
		
		image = r.image;
		MAX_HEALTH = r.health;
		speed = r.speed;
		damage = r.damage;
		rate = r.rate;
		setPosition(x,y);
		
		rc = new RectCollider(rect);
	}
	
	public void move(double x, double y, Collider[] colliders) {
		x *= speed;
		y *= speed;
		super.move((int) x, (int) y, colliders); 
		rc.setPosition(this.x, this.y);
	}

}
