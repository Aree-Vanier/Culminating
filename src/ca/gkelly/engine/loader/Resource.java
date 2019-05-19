package ca.gkelly.engine.loader;

import java.awt.image.BufferedImage;

import org.json.simple.JSONObject;

/** Class to be extended for use with {@link Loader} */
public abstract class Resource {

	/**
	 * Called by {@link Loader} when loading the resource
	 * 
	 * @param image The resource's image
	 * @param json  The resource's JSON contents
	 */
	abstract public void load(BufferedImage image, JSONObject json);

}
