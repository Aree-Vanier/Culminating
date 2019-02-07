package ca.gkelly.culminating.loader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.badlogic.gdx.graphics.Texture;

public class Vessel {
	
	public MountPoint[] mountPoints;
	public String name;
	public Texture texture;
	
	
	public Vessel(String name, JSONObject json) {
		name = (String) json.get("name");
		
		JSONArray mountArray = ((JSONArray) json.get("mountPoints"));
		
		mountPoints = new MountPoint[mountArray.size()];
		for(int i = 0; i<mountArray.size(); i++) {
			JSONObject mountJSON =  (JSONObject) mountArray.get(i);
			
			int x = Math.toIntExact((long) mountJSON.get("x"));
			int y = Math.toIntExact((long) mountJSON.get("y"));
			
			MountPoint.Type t = null;
			
			switch((String) mountJSON.get("type")) {
			case "light":
				t=MountPoint.Type.LIGHT;
				break;

			case "medium":
				t=MountPoint.Type.MEDIUM;
				break;

			case "heavy":
				t=MountPoint.Type.HEAVY;
				break;
			
			}
			
			mountPoints[i] = new MountPoint(x, y, t);
		}
		
	}
	
}
