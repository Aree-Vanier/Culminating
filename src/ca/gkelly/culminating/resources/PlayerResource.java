package ca.gkelly.culminating.resources;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.json.simple.JSONObject;

import ca.gkelly.engine.loader.Resource;

public class PlayerResource extends Resource {

	public BufferedImage image;

	public String name;
	public int health;
	public int speed;
	public int damage;
	public int rate;

	@Override
	public void load(File f, JSONObject json) {
		String imagePath = (f.getParentFile().getPath()) + "\\" + (String) json.get("texture");

		try {
			this.image = ImageIO.read(new File(imagePath));
		} catch (IOException e) {
			e.printStackTrace();
		}

		name = (String) json.get("name");
		speed = Integer.parseInt((String) json.get("speed"));
		damage = Integer.parseInt((String) json.get("damage"));
		rate = Integer.parseInt((String) json.get("rate"));

	}

}
