package ca.gkelly.culminating.loader;

public class Mount {
	public int x;
	public int y;
	
	public int health;
	public final int MAX_HEALTH;
	
	public final int COST;
	
	public Mount(int maxHealth, int cost){
		MAX_HEALTH = maxHealth;
		COST = cost;
	}
}
