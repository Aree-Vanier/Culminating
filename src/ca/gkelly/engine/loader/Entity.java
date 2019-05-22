package ca.gkelly.engine.loader;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import ca.gkelly.engine.graphics.Camera;

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
	public int x;
	/** Y coordinate of the centre of the {@link #rect bounding rectangle} */
	public int y;

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
	protected void move(int x, int y) {
		this.x += x;
		this.y += y;
		rect.setLocation((int) (this.x - rect.getWidth() / 2), (int) (this.y - rect.getHeight() / 2));
	}

	/**
	 * Move to specified position
	 * 
	 * @param x The new x position
	 * @param y The new y position
	 */
	public void setPosition(int x, int y) {
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
