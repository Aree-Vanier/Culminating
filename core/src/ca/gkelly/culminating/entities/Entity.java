package ca.gkelly.culminating.entities;

import com.badlogic.gdx.graphics.Texture;

public abstract class Entity {
	
	public int x;
	public int y;
	
	public Texture texture;
	
	public Entity(int x, int y, Texture t) {
		this.x = x;
		this.y = y;
		texture = t;
	}

	public abstract void render();
	public abstract void update();
	
}
