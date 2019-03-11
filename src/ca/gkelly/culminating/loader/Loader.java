package ca.gkelly.culminating.loader;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Loader {
	public static ArrayList<VesselSource> vessels = new ArrayList<VesselSource>();
	public static ArrayList<MountSource> mounts = new ArrayList<MountSource>();
//	public static ArrayList<TiledMap> maps = new ArrayList<TiledMap>();
	
	
	public static String directory;
	
	public enum ResourceType{
		VESSEL,
		MOUNT,
		WEAPON
	}
	
	public static void init(String dir) {
		directory = dir;
	}
	
	public static void load() {
		System.out.println("Loading");
		File[] files = new File(directory+"\\gameData").listFiles();
		
		
		BufferedReader reader;
		System.out.println(files);
		for(File f : files) {
			System.out.println(f.getName());
			//We only want the JSON files
			if(!f.getName().endsWith(".json")) {
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
			
			//Get the type from the declaration
			ResourceType t = ResourceType.valueOf(declaration.split(" ")[1]);
			System.out.println(t);
			
			try {
				loadResource(f, t);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}
		
		
		files = new File(directory+"\\maps").listFiles();
		
		//TODO: Figure out maps
//		for(File f : files) {
//			//Only read map files
//			if(f.getName().endsWith(".tmx")) {
//				maps.add(new TmxMapLoader().load(getResourcePath(f.getPath())));
//			}
//		}
		
		System.out.println("Loaded");
	}
	
	private static void loadResource(File file, ResourceType t) throws Exception {
		FileReader fr;
		fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
	 
		String text = "";
		String line;
		while((line=br.readLine()) != null) {
			//Ignore declaration line
			if(line.contains("//DOCTYPE")) continue;
			text += line;
		}
		
//		System.out.println(text);
		
		JSONObject rootJSON;
		rootJSON = (JSONObject) new JSONParser().parse(text);
		
		String imagePath = (file.getParentFile().getPath())+"\\"+(String) rootJSON.get("texture");
		
		System.out.println(imagePath);
		BufferedImage image  = ImageIO.read(new File(imagePath));
		
		switch(t) {
		case VESSEL:
			VesselSource v = new VesselSource(image, rootJSON);
			vessels.add(v);
			break;
		case MOUNT:
			MountSource m = new MountSource(image, rootJSON);
			mounts.add(m);
			break;
		case WEAPON:
			WeaponSource w = new WeaponSource(image, rootJSON);
			mounts.add(w);
			break;
		
		}
	}
	
	//TODO:Dispose of assets
	public static void dispose() {
	}
	
}
