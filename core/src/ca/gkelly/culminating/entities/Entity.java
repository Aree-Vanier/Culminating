package ca.gkelly.culminating.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {
	
	public int x;
	public int y;
	
	public Texture texture;
	public Rectangle rect;
	
	public Entity(int x, int y, Texture t) {
		this.x = x;
		this.y = y;
		texture = t;
		rect = new Rectangle(x,y,texture.getWidth(), texture.getHeight());
	}
	
	public boolean isClicked(Vector2 mouse) {
		return rect.contains(mouse);
	}

	public abstract void render(SpriteBatch b);
	public abstract void update();
	
}
