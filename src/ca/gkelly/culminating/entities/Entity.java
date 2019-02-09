package ca.gkelly.culminating.entities;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public abstract class Entity {
	
	public int x;
	public int y;
	
	public BufferedImage texture;
	public Rectangle rect;
	
	public Entity(int x, int y, BufferedImage t) {
		this.x = x;
		this.y = y;
		texture = t;
		rect = new Rectangle(x,y,texture.getWidth(), texture.getHeight());
	}
	
	public boolean isClicked(Point mouse) {
		return rect.contains(mouse);
	}

	public abstract void render(Graphics g);
	public abstract void update();
	
}
