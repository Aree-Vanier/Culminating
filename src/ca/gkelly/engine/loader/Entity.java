package ca.gkelly.engine.loader;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import ca.gkelly.engine.graphics.Camera;
import ca.gkelly.engine.util.Logger;

/** Class used to manage basic entities */
public class Entity {

	/** Image to be rendered */
	protected BufferedImage image;
	/**
	 * Bounding rectangle used for position and collision <br/>
	 * <strong>Must be defined for functionality</strong>
	 */
	public Rectangle rect;

	public int x;
	public int y;

	/**
	 * Instantiate the entity, assigns image and creates basic rectangle
	 * 
	 * @param image The image to be used
	 */
	protected Entity(BufferedImage image) {
		this.image = image;

		rect = new Rectangle(0 - image.getWidth() / 2, 0 - image.getWidth() / 2, image.getWidth() / 2,
				image.getHeight() / 2);
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
		rect.setLocation((int) (x - rect.getWidth() / 2), (int) (y - rect.getHeight() / 2));
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
		rect.setLocation((int) (x - rect.getWidth() / 2), (int) (y - rect.getHeight() / 2));
	}

	/**
	 * Render the image
	 * 
	 * @param c the camera to be used
	 */
	public void render(Camera c) {
		Logger.log(x+"\t"+y);
		c.render(image, rect.x, rect.y);
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

	/** Get the x position of the rectangle */
	public int getX() {
		return rect.x;
	}

	/** Get the y position of the rectangle */
	public int getY() {
		return rect.y;
	}
}
