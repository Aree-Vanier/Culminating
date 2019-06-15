package ca.gkelly.engine.loader;

import ca.gkelly.engine.util.Vector;

/** Entity with simple physics calculations */
public abstract class PhysicsEntity extends Entity {

	protected Vector velocity;

	public PhysicsEntity(int width, int height) {
		super(width, height);
	}

	/** Call periodically to apply velocity */
	@Override
	public void update() {
		move(velocity.getX(), velocity.getY());
	}

	/**
	 * Gets the stored velocity<br/>
	 */
	@Override
	public Vector getVelocity() {
		return velocity;
	}
}
