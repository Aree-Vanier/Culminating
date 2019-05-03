package ca.gkelly.culminating.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import ca.gkelly.culminating.loader.MountSource;
import ca.gkelly.culminating.util.Logger;

public abstract class Mount {
	
	int x;
	int y;
	
	int health;
	final int MAX_HEALTH;
	
	boolean requestRender = true;
	
	BufferedImage texture;
	
	public Mount(MountSource m, int x, int y) {
		this.health = m.MAX_HEALTH;
		this.MAX_HEALTH = health;
		this.x = x;
		this.y = y;
		this.texture = m.texture;
	}
	
	public void render(Graphics g) {
		Logger.log(x+"\t"+y);
		g.drawImage(texture, x, y, null);
		requestRender = false;
	}
	
	public boolean getRenderRequest() {
		return requestRender;
	}
	
	public abstract void onReload();
	public abstract void onFire();
	public abstract void onDestroy();
}
