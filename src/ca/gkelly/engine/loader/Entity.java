package ca.gkelly.engine.loader;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import ca.gkelly.engine.graphics.Camera;

public class Entity {

	protected BufferedImage image;
	public Rectangle rect;

	protected void move(int x, int y) {
		x += rect.x;
		y += rect.y;
		rect.setLocation((int) x, (int) y);
	}

	public void render(Camera c) {
		c.render(image, rect.x, rect.y);
	}

	public boolean collides(Rectangle r) {
		return rect.intersects(r);
	}

	public boolean contains(int x, int y) {
		return rect.contains(x, y);
	}

	public boolean contains(Point p) {
		return contains(p.x, p.y);
	}

	public int getX() {
		return rect.x;
	}

	public int getY() {
		return rect.y;
	}
}
