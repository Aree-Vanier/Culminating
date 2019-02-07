package ca.gkelly.culminating.loader;

import org.json.simple.JSONObject;

public class WeaponSource extends MountSource {

	public int damage;
	public int ammo;
	public int fireRate;
	public int accHit;
	public int accMiss;
	
	public WeaponSource(String texturePath, JSONObject json) {
		super(texturePath, json);

		damage = Math.toIntExact((long) json.get("damage"));
		ammo = Math.toIntExact((long) json.get("ammo"));
		fireRate = Math.toIntExact((long) json.get("fireRate"));
		
		JSONObject accuracy = (JSONObject) json.get("accuracy");
		accHit = Math.toIntExact((long) accuracy.get("hit"));
		accMiss = Math.toIntExact((long) accuracy.get("miss"));
		
	}

}
