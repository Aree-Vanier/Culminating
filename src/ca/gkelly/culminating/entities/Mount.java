package ca.gkelly.culminating.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ca.gkelly.culminating.loader.MountSource;

public abstract class Mount {
	
	int x;
	int y;
	
	int health;
	final int MAX_HEALTH;
	
	boolean requestRender = true;
	
	Texture texture;
	
	public Mount(MountSource m, int x, int y) {
		this.health = m.MAX_HEALTH;
		this.MAX_HEALTH = health;
		this.x = x;
		this.y = y;
		this.texture = m.texture;
	}
	
	public void render(SpriteBatch b) {
		System.out.println(x+"\t"+y);
		b.draw(texture, x, y);
		requestRender = false;
	}
	
	public boolean getRenderRequest() {
		return requestRender;
	}
	
	public abstract void onReload();
	public abstract void onFire();
	public abstract void onDestroy();
}
