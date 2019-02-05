package ca.gkelly.culminating.vessels;

public class MountPoint {
	public enum Type{
		LIGHT,MEDIUM,HEAVY
	}
	
	int x;
	int y;
	Type type;
	
	
	public MountPoint(int x, int y, Type t){
		this.x=x;
		this.y=y;
		type = t;
	}
	
}
