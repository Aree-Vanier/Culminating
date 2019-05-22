package ca.gkelly.engine.loader;

import ca.gkelly.engine.graphics.Camera;
import ca.gkelly.engine.util.Logger;
import ca.gkelly.engine.util.Vector;

/** Entity with simple physics calculations */
public abstract class PhysicsEntity extends Entity {

	protected Vector velocity;

	public PhysicsEntity(int width, int height) {
		super(width, height);
	}

	/** Called periodically to apply velocity */
	public void update() {
		move(velocity.getX(), velocity.getY());
		Logger.log("Bullet updated "+velocity.getX()+"\t"+velocity.getY());
	}
}
