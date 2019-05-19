package ca.gkelly.culminating.loader;

import java.awt.image.BufferedImage;

import org.json.simple.JSONObject;

import ca.gkelly.engine.loader.Resource;

public class MountSource extends Resource{
	public int x;
	public int y;

	public BufferedImage texture;

	public int health;
	public int MAX_HEALTH;

	public int BASE_COST;

	public MountPoint.Type type;

	public String name;

	public void load(BufferedImage image, JSONObject json) {
		name = (String) json.get("name");

		texture = image;

		JSONObject mountPoint = ((JSONObject) json.get("mountPoint"));
		x = Math.toIntExact((Long) mountPoint.get("x"));
		y = Math.toIntExact((Long) mountPoint.get("y"));

		MAX_HEALTH = Math.toIntExact((Long) json.get("maxHealth"));
		BASE_COST = Math.toIntExact((Long) json.get("cost"));

		type = MountPoint.Type.valueOf(((String) json.get("type")).toUpperCase());
	}
}
