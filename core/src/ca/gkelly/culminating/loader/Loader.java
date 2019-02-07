package ca.gkelly.culminating.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Loader {
	public static ArrayList<VesselSource> vessels = new ArrayList<VesselSource>();
	public static ArrayList<MountSource> mounts = new ArrayList<MountSource>();
	private static final String directory = "C:\\Users\\Greg\\Documents\\Workspaces\\Eclipse\\Culminating\\core\\assets\\gameData";
	
	public enum ResourceType{
		VESSEL,
		MOUNT,
		WEAPON
	}
	
	public static void load() {
		System.out.println("Loading");
		File[] files = new File(directory).listFiles();
		
		
		BufferedReader reader;
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
		
		String texture = file.getParentFile().getPath().split("assets")[1].replace("\\", "")+"\\"+(String) rootJSON.get("texture");
		
		switch(t) {
		case VESSEL:
			VesselSource v = new VesselSource(texture, rootJSON);
			vessels.add(v);
			break;
		case MOUNT:
			MountSource m = new MountSource(texture, rootJSON);
			mounts.add(m);
			break;
		case WEAPON:
			WeaponSource w = new WeaponSource(texture, rootJSON);
			mounts.add(w);
			break;
		
		}
	}
	
}
