package ca.gkelly.culminating.resources;

import java.awt.Color;

import ca.gkelly.engine.graphics.Camera;
import ca.gkelly.engine.loader.PhysicsEntity;
import ca.gkelly.engine.util.Logger;
import ca.gkelly.engine.util.Vector;

public class Bullet extends PhysicsEntity{

	int length = 15;

	public Bullet(int x, int y, Vector velocity) {
		super(15, 5);
		this.x = x;
		this.y = y;
		this.velocity = velocity;
	}

	@Override
	public void render(Camera c) {
		Logger.log(x + "," + y + "\t" + x+Vector.multiply(velocity.normalized(), length).getX() + ","
				+ y+Vector.multiply(velocity.normalized(), length).getX());
		c.drawLine(x, y, x+Vector.multiply(velocity.normalized(), length).getX(),
				y+Vector.multiply(velocity.normalized(), length).getY(), 5, Color.RED);
	}

}
