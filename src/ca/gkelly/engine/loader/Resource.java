package ca.gkelly.engine.loader;

import java.awt.image.BufferedImage;

import org.json.simple.JSONObject;

public abstract class Resource {

	abstract public void create(BufferedImage image, JSONObject json);

}
