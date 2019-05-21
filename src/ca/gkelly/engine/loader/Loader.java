package ca.gkelly.engine.loader;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import ca.gkelly.engine.util.Logger;

/**
 * Class used to load .json resource files, and create {@link Resource}
 * instances out of them <br/>
 * All functions are to be used in a static context
 */
public class Loader {

	/** A collection of all loaded resources, sorted by type */
	public static HashMap<String, ArrayList<Resource>> resources = new HashMap<>();
	/** A collection of all resource classes, sorted by type */
	public static HashMap<String, Class> resourceClasses = new HashMap<>();

	/** The directory containing the resource files */
	public static String directory;

	/** Flag to indicate weather the loader is initialized */
	public static boolean initialized = false;

	/**
	 * Initialize the loader, must be called before {@link #load()}
	 * 
	 * @param dir     Directory containing resource files
	 * @param classes Hashmap with the following structure:<br/>
	 *                &nbsp&nbsp&nbsp&nbsp<strong>[String]:</strong> Resource type
	 *                indentifier<br/>
	 *                &nbsp&nbsp&nbsp&nbsp<strong>[Class]:
	 *                </strong>&nbsp<code>.class</code> of
	 *                {@link Resource}-extending class
	 */
	public static void init(String dir, HashMap<String, Class> classes) {
		directory = dir;
		resourceClasses = classes;
		initialized = true;
	}

	/**
	 * Load resources found in {@link #directory} <br/>
	 * {@link #init(String, HashMap) init()} must be called first
	 */
	public static void load() {
		if (!initialized) {
			Logger.log(Logger.ERROR, "Loader not initialised");
			return;
		}
		Logger.log(Logger.INFO, "Loading");
		Logger.epoch("LOAD");
		File[] files = new File(directory + "\\gameData").listFiles();

		BufferedReader reader;
		for (File f : files) {
			Logger.log(Logger.DEBUG, f.getName());
			// We only want the JSON files
			if (!f.getName().endsWith(".json")) {
				continue;
			}

			try {
				reader = new BufferedReader(new FileReader(f));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				continue;
			}

			String declaration = "";

			try {
				declaration = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Get the type from the declaration
			String type = declaration.split(" ")[1].toLowerCase();
			Logger.log(Logger.DEBUG, type);

			try {
				loadResource(f, type);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		files = new File(directory + "\\maps").listFiles();

		// TODO: Figure out maps
//		for(File f : files) {
//			//Only read map files
//			if(f.getName().endsWith(".tmx")) {
//				maps.add(new TmxMapLoader().load(getResourcePath(f.getPath())));
//			}
//		}

		Logger.log(Logger.INFO, "Loaded in {LOAD}");
	}

	/**
	 * Loads specified resource, and creates a {@link Resource} instance
	 * 
	 * @param file The resource to load
	 * @param type The resource type to load
	 */
	private static void loadResource(File file, String type) throws Exception {
		FileReader fr;
		fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);

		String text = "";
		String line;
		while ((line = br.readLine()) != null) {
			// Ignore declaration line
			if (line.contains("//DOCTYPE"))
				continue;
			text += line;
		}

		// Load JSON and image
		JSONObject rootJSON;
		rootJSON = (JSONObject) new JSONParser().parse(text);

		Logger.log("Type: " + type);
		// Get the class to instantiate
		Class<Resource> c = resourceClasses.get(type);
		Logger.log(c.getSimpleName());
		// Create instance
		Resource r = c.newInstance();
		r.load(file, rootJSON);
		// If there is not an entry for this type, add it
		if (!resources.containsKey(type)) {
			Logger.log("Created resource: " + type);
			resources.put(type, new ArrayList<Resource>());
		}
		// Add the resource to resources
		resources.get(type).add(r);

		br.close();
	}

	// TODO:Dispose of assets
	public static void dispose() {
	}

}
