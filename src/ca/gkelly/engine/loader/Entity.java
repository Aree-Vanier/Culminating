package ca.gkelly.engine.loader;

import java.awt.image.BufferedImage;

import ca.gkelly.engine.collision.Collider;
import ca.gkelly.engine.collision.RectCollider;
import ca.gkelly.engine.graphics.Camera;
import ca.gkelly.engine.util.Vector;
import ca.gkelly.engine.util.Vertex;

/** Class used to manage basic entities */
public abstract class Entity {

	/**
	 * Image to be rendered<br/>
	 * If null, the default {@link #render(Camera) render()} will do nothing
	 */
	protected BufferedImage image;

	/**
	 * Bounding collider used for position and collision <br/>
	 * <strong>Must be defined for functionality</strong>
	 */
	public Collider collider;

	/** X coordinate of the centre of the {@link #collider} */
	public double x;
	/** Y coordinate of the centre of the {@link #collider} */
	public double y;

	/** Previous X coordinate, used to calculate velocity */
	private double lastX;
	/** Previous Y coordinate, used to calculate velocity */
	private double lastY;

	/**
	 * Instantiate the entity, assigns image and creates basic rectangle
	 * 
	 * @param image The image to be used
	 */
	protected Entity(int width, int height) {
		collider = new RectCollider(-width / 2, -height / 2, width, height);
	}

	/**
	 * Move by the specified distance
	 * 
	 * @param x The x distance
	 * @param y The y distance
	 */
	protected void move(double x, double y) {
		this.x += x;
		this.y += y;
		collider.translate(x, y);
	}

	/**
	 * Move by the specified distance, with collision detection
	 * 
	 * @param x The x distance
	 * @param y The y distance
	 * @param s The shapes to check for collision against, requires {@link #rect}
	 */
	protected void move(double x, double y, Collider[] colliders) {
		this.x += x;
		this.y += y;
		collider.translate(x, y);

		for(Collider c: colliders) {
			Vector pushback = collider.getPushback(c);
			move(pushback.getX(), pushback.getY());
		}

	}

	/** Call periodically so that {@link #getVelocity()} is accurate */
	public void update() {
		lastX = x;
		lastY = y;
	}

	/**
	 * Gets an approximate velocity, for most accurate result, call after movement
	 * or before {@link #update()}<br/>
	 * Calculated by getting the difference between {@link #x}/{@link #x} and
	 * {@link #lastX}/{@link #lastY}
	 */
	public Vector getVelocity() {
		return new Vector(x - lastX, y - lastY);
	}

	/**
	 * Move to specified position
	 * 
	 * @param x The new x position
	 * @param y The new y position
	 */
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
		collider.setPosition(x, y);
	}

	/**
	 * Render the image
	 * 
	 * @param c the camera to be used
	 */
	public void render(Camera c) {
		if(image != null) {
			c.render(image, (int) (collider.x - collider.width / 2), (int) (collider.y - collider.height / 2));
		}
	}

	/**
	 * Check for collision with {@link Collider}
	 * 
	 * @param c {@link Collider} to check against
	 * @return True if there is a collision
	 */
	public boolean collides(Collider c) {
		return collider.intersects(c);
	}

	/**
	 * Check if the rectangle contains a point
	 * 
	 * @param x X position of point
	 * @param y Y position of point
	 * @return True if the point is contained
	 */
	public boolean contains(double x, double y) {
		return collider.contains(x, y);
	}
	
	/**
	 * Check if the rectangle contains a point
	 * 
	 * @param v The {@link Vertex} to check
	 * @return True if the point is contained
	 */
	public boolean contains(Vertex v) {
		return collider.contains(v);
	}

	/** Returns the integer equivalent of X */
	public int getX() {
		return (int) x;
	}

	/** Returns the integer equivalent of X */
	public int getY() {
		return (int) y;
	}

}
