package ca.gkelly.culminating.loader;

import com.badlogic.gdx.graphics.Texture;

public class Vessel {
	
	public MountPoint[] mountPoints;
	public String name;
	public Texture texture;
	
	
	public Vessel(String name, MountPoint[] mountPoints, String texturePath) {
		this.name = name;
		this.mountPoints = mountPoints;
		System.out.println(texturePath);
		this.texture = new Texture(texturePath);
		
	}
	
}
