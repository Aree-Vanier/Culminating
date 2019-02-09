package ca.gkelly.culminating.loader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.badlogic.gdx.graphics.Texture;

import ca.gkelly.culminating.entities.Mount;
import ca.gkelly.culminating.entities.Ship;
import ca.gkelly.culminating.entities.Weapon;

public class VesselSource {
	
	public MountPoint[] mountPoints;
	public String name;
	public Texture texture;
	
	public int health;
	
	
	public VesselSource(String texturePath, JSONObject json) {
		name = (String) json.get("name");
		
		texture = new Texture(texturePath);
		
		health = Math.toIntExact((Long) json.get("health"));
		
		JSONArray mountArray = ((JSONArray) json.get("mountPoints"));
		
		mountPoints = new MountPoint[mountArray.size()];
		for(int i = 0; i<mountArray.size(); i++) {
			JSONObject mountJSON =  (JSONObject) mountArray.get(i);
			
			int x = Math.toIntExact((Long) mountJSON.get("x"));
			int y = Math.toIntExact((Long) mountJSON.get("y"));
			
			MountPoint.Type t = MountPoint.Type.valueOf(((String) mountJSON.get("type")).toUpperCase());
			
			mountPoints[i] = new MountPoint(x, y, t);
		}
		
	}
	
	public Ship build(MountSource[] mounts) {
		
		Mount[] m = new Mount[mounts.length];
		for(int i=0; i<mounts.length; i++) {
			if(mounts[i] instanceof WeaponSource) {
				m[i] = new Weapon(mounts[i], mountPoints[i].x-mounts[i].x, mountPoints[i].y-mounts[i].y);
			}
		}
		
		return new Ship(this, 0, 0, m);
	}
	
}