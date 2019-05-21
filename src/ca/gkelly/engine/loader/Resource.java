package ca.gkelly.engine.loader;

import java.io.File;

import org.json.simple.JSONObject;

/** Class to be extended for use with {@link Loader} */
public abstract class Resource {

	/**
	 * Called by {@link Loader} when loading the resource
	 * 
	 * @param path The path to the resource
	 * @param json The resource's JSON contents
	 */
	abstract public void load(File f, JSONObject json);

}
