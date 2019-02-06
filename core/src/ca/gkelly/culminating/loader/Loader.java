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
	public static ArrayList<Vessel> vessels = new ArrayList<Vessel>();
	public static ArrayList<Mount> mounts = new ArrayList<Mount>();
	private static final String directory = "C:\\Users\\Greg\\Documents\\Workspaces\\Eclipse\\Culminating\\core\\assets\\gameData";
	
	
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
			
			if(declaration.contains("vessel")) {
				loadVessel(f);
			}
			
			
		}
		
		System.out.println("Loaded");
	}
	
	private static void loadVessel(File file) {
		FileReader fr;
		try {
			fr = new FileReader(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		BufferedReader br = new BufferedReader(fr);
	 
		String text = "";
		String line;
		try {
			while((line=br.readLine()) != null) {
				//Ignore declaration line
				if(line.contains("//DOCTYPE")) continue;
				text += line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(text);
		
		JSONObject rootJSON;
		
		try {
			rootJSON = (JSONObject) new JSONParser().parse(text);
		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
			return;
		}
		
		String name = (String) rootJSON.get("name");
		
		String texture = file.getParentFile().getPath().split("assets")[1].replace("\\", "")+"\\"+(String) rootJSON.get("texture");
		
		JSONArray mountArray = ((JSONArray) rootJSON.get("mountPoints"));
		
		MountPoint[] mounts = new MountPoint[mountArray.size()];
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
			
			mounts[i] = new MountPoint(x, y, t);
		}
		
		Vessel v = new Vessel(name, mounts, texture);
		
		vessels.add(v);
	}
	
	private static void loadMounts() {
		
	}
}
