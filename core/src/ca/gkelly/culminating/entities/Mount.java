package ca.gkelly.culminating.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ca.gkelly.culminating.loader.MountSource;

public abstract class Mount {
	
	int x;
	int y;
	
	int health;
	final int MAX_HEALTH;
	
	Texture texture;
	
	public Mount(MountSource m, int x, int y) {
		this.health = m.MAX_HEALTH;
		this.MAX_HEALTH = health;
		this.x = x;
		this.y = y;
	}
	
	public void render(SpriteBatch b) {
		b.draw(texture, x, y);
		
	}
	
	
	public abstract void onReload();
	public abstract void onFire();
	public abstract void onDestroy();
}
