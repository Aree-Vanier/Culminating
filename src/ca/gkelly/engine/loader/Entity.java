package ca.gkelly.engine.loader;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;

import ca.gkelly.engine.graphics.Camera;
import ca.gkelly.engine.util.Logger;
import ca.gkelly.engine.util.Vector;

/** Class used to manage basic entities */
public abstract class Entity {

	/**
	 * Image to be rendered<br/>
	 * If null, the default {@link #render(Camera) render()} will do nothing
	 */
	protected BufferedImage image;

	/**
	 * Bounding rectangle used for position and collision <br/>
	 * <strong>Must be defined for functionality</strong>
	 */
	public Rectangle rect;
	
	/** X coordinate of the centre of the {@link #rect bounding rectangle} */
	public double x;
	/** Y coordinate of the centre of the {@link #rect bounding rectangle} */
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

		rect = new Rectangle(-width / 2, -height / 2, width, height);
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
		rect.setLocation((int) (this.x - rect.getWidth() / 2), (int) (this.y - rect.getHeight() / 2));
	}
	
	/**
	 * Move by the specified distance, with collision detection
	 * 
	 * @param x The x distance
	 * @param y The y distance
	 * @param s The shapes to check for collision against, requires {@link #rect}
	 */
	protected void move(double x, double y, Shape[] colliders) {
		this.x += x;
		this.y += y;
		

		rect.setLocation((int) (this.x - rect.getWidth() / 2), (int) (this.y - rect.getHeight() / 2));
		boolean tl = false;
		boolean tr = false;
		boolean bl = false;
		boolean br = false;
		
		for(Shape s: colliders) {
			if(s.contains(rect.x, rect.y)) {
				tl = true;
			}
			if(s.contains(rect.x+rect.getWidth(), rect.y)) {
				tr = true;
			}
			if(s.contains(rect.x, rect.y+rect.getHeight())) {
				bl = true;
			}
			if(s.contains(rect.x+rect.getWidth(), rect.y+rect.getHeight())) {
				br = true;
			}
		}
		if(tl&&tr)
			this.y = (this.y - y);
		if(bl&&br)
			this.y = (this.y - y);
		if(tr&&br)
			this.x = (this.x - x);
		if(tl&&bl)
			this.x = (this.x - x);
		

		rect.setLocation((int) (this.x - rect.getWidth() / 2), (int) (this.y - rect.getHeight() / 2));
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
		rect.setLocation((int) (this.x - rect.getWidth() / 2), (int) (this.y - rect.getHeight() / 2));
	}

	/**
	 * Render the image
	 * 
	 * @param c the camera to be used
	 */
	public void render(Camera c) {
		if(image != null) {
			c.render(image, getRectX(), getRectY());
		}
	}

	/**
	 * Check for collision with rectangle
	 * 
	 * @param r Rectangle to check against
	 * @return True if there is a collision
	 */
	public boolean collides(Rectangle r) {
		return rect.intersects(r);
	}

	/**
	 * Check for collision with polygon
	 * 
	 * @param p Polygon to check against
	 * @return True if there is a collision
	 */
	public boolean collides(Polygon p) {
		return p.intersects(rect);
	}

	/**
	 * Check if the rectangle contains a point
	 * 
	 * @param x X position of point
	 * @param y Y position of point
	 * @return True if the point is contained
	 */
	public boolean contains(int x, int y) {
		return rect.contains(x, y);
	}

	/**
	 * Check if the rectangle contains a point
	 * 
	 * @param p The point to check
	 * @return True if the point is contained
	 */
	public boolean contains(Point p) {
		return contains(p.x, p.y);
	}

	/** Returns the integer equivalent of X */
	public int getX() {
		return (int) x;
	}

	/** Returns the integer equivalent of X */
	public int getY() {
		return (int) y;
	}

	/**
	 * Get the x position of the {@link #rect bounding rectangle} This will be in
	 * the top-left corner, as opposed to centered like {@link #x}
	 */
	public int getRectX() {
		return rect.x;
	}

	/**
	 * Get the y position of the {@link #rect bounding rectangle} <br/>
	 * This will be in the top-left corner, as opposed to centered like {@link #y}
	 */
	public int getRectY() {
		return rect.y;
	}

	/**
	 * Get the width of the {@link #rect bounding rectangle} <br/>
	 */
	public int getWidth() {
		return rect.width;
	}

	/**
	 * Get the height of the {@link #rect bounding rectangle} <br/>
	 */
	public int getHeight() {
		return rect.height;
	}
}
