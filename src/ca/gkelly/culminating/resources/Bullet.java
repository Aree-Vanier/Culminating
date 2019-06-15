package ca.gkelly.culminating.resources;

import java.awt.Color;

import ca.gkelly.engine.graphics.Camera;
import ca.gkelly.engine.loader.PhysicsEntity;
import ca.gkelly.engine.util.Vector;

public class Bullet extends PhysicsEntity {

	int length = 15;
	private Vector targetVel;
	int life = 0;

	public Bullet(int x, int y, Vector velocity, Vector extraVelocity) {
		super(15, 5);
		this.x = x;
		this.y = y;
		targetVel = velocity;
		this.velocity = Vector.add(velocity, extraVelocity);
	}

	@Override
	public void update() {
		super.update();
		Vector err = Vector.subtract(targetVel, velocity);
		velocity = Vector.add(velocity, Vector.divide(err, 20));
		life++;
	}

	@Override
	public void render(Camera c) {
//		Logger.log(x + "," + y + "\t" + x+Vector.multiply(velocity.normalized(), length).getX() + ","
//				+ y+Vector.multiply(velocity.normalized(), length).getX());
		c.drawLine(getX(), getY(), (int) (x + Vector.multiply(velocity.normalized(), length).getX()),
				(int) (y + Vector.multiply(velocity.normalized(), length).getY()), 5, Color.RED);
	}

}
