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

import ca.gkelly.culminating.loader.MountSource;
import ca.gkelly.culminating.loader.VesselSource;
import ca.gkelly.culminating.loader.WeaponSource;
import ca.gkelly.engine.util.Logger;

public class Loader {

	public static HashMap<String, ArrayList<Resource>> resources = new HashMap<>();
	public static HashMap<String, Class> resourceClasses = new HashMap<>();

	public static String directory;

	public static void init(String dir, HashMap<String, Class> classes) {
		directory = dir;
		resourceClasses = classes;
	}

	public static void load() {
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

//		System.out.println(text);

		JSONObject rootJSON;
		rootJSON = (JSONObject) new JSONParser().parse(text);

		String imagePath = (file.getParentFile().getPath()) + "\\" + (String) rootJSON.get("texture");

		Logger.log(Logger.DEBUG, imagePath);
		BufferedImage image = ImageIO.read(new File(imagePath));

		Logger.log("Type: "+type);
		Class<Resource> c = resourceClasses.get(type);
		Logger.log(c.getSimpleName());
		Resource r = c.newInstance();
		r.create(image, rootJSON);
		if (!resources.containsKey(type)) {
			Logger.log("Created resource: " + type);
			resources.put(type, new ArrayList<Resource>());
		}
		resources.get(type).add(r);

		br.close();
	}

	// TODO:Dispose of assets
	public static void dispose() {
	}

}
