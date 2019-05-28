package ca.gkelly.engine.collision;

import java.awt.Point;

import ca.gkelly.engine.util.Vector;

public abstract class Collider {

	double x;
	double y;
	
	public void translate(double x, double y) {
		this.x+=x;
		this.y+=y;
	}
	
	
	public void setPosition(double x, double y) {
		this.x+=x;
		this.y+=y;
	}
	
	public abstract boolean cointains(Collider c);
	public abstract boolean contains(double x, double y);
	public boolean contains(Point p) {
		return(contains(p.x, p.y));
	}
	public abstract boolean intersects(Collider c);
	
	/**Get the normalized vector representing the required pushback to exit collider
	 * @param c The {@link Collider} to check against
	 * @return Pushback vector, null if no collision with c*/
	public abstract Vector getPushback(Collider c);
	
}
