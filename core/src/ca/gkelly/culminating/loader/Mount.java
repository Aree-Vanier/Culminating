package ca.gkelly.culminating.loader;

import com.badlogic.gdx.graphics.Texture;

public class Mount {
	public int x;
	public int y;
	
	public Texture texture;
	
	public int health;
	public final int MAX_HEALTH;
	
	public final int COST;
	
	public MountPoint.Type type;
	
	public Mount(String name, int x, int y, String texturePath, int maxHealth, int cost, MountPoint.Type t){
		this.x = x;
		this.y = y;
		texture = new Texture(texturePath);
		MAX_HEALTH = maxHealth;
		COST = cost;
		type = t;
	}
}
