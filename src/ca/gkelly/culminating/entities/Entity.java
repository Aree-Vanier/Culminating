package ca.gkelly.culminating.entities;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import ca.gkelly.culminating.graphics.Camera;

public abstract class Entity {

	public int x;
	public int y;

	public BufferedImage baseTexture;
	public Rectangle rect;

	public Entity(int x, int y, BufferedImage t) {
		this.x = x;
		this.y = y;
		baseTexture = t;
		rect = new Rectangle(x, y, baseTexture.getWidth(), baseTexture.getHeight());
	}

	public boolean isClicked(Point mouse) {
		return rect.contains(mouse);
	}

	public abstract void render(Camera c);

	public abstract void update();

}
