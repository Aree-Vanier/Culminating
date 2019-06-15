package ca.gkelly.engine.util;

public class Vertex {
	public double x;
	public double y;
	
	public Vertex(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public String getString() {
		return "("+x+","+y+")";
	}

}
