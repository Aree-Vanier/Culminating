package ca.gkelly.engine.collision;

import java.awt.Point;
import java.util.ArrayList;

import ca.gkelly.engine.util.Vector;

/**
 * Generic class for creating colliders<br/>
 * has functions to handle generic polygon collision detection
 */
public abstract class Collider {

	/** The x coordinate of the middle of the collider */
	public double x;
	/** The y coordinate of the middle of the collider */
	public double y;
	/** The distance from the middle to the extremity */
	double radius;

	/** The x vertices of the collider */
	double[] verticesX;
	/** The y vertices of the collider */
	double[] verticesY;
	/** The number of vertices of the collider */
	int vertexCount;

	/** The largest x vertex of the collider */
	double maxX = Double.MIN_VALUE;
	/** The smallest x vertex of the collider */
	double minX = Double.MAX_VALUE;
	/** The largest y vertex of the collider */
	double maxY = Double.MIN_VALUE;
	/** The smallest y vertex of the collider */
	double minY = Double.MAX_VALUE;
	/** The bounding width of the collider */
	double width;
	/** The bounding height of the collider */
	double height;

	/**
	 * Set the collider vertices
	 * 
	 * @param verticesX   The list of x vertices
	 * @param verticesY   The list of y vertices
	 * @param vertexCount The amount of vertices in the shape
	 */
	public void setVertices(double[] verticesX, double[] verticesY, int vertexCount) {
		this.verticesX = verticesX;
		this.verticesY = verticesY;
		this.vertexCount = vertexCount;
		
		
		for(double d: verticesX) {
			if(d > maxX) maxX = d;
			if(d < minX) minX = d;
		}
		for(double d: verticesY) {
			if(d > maxY) maxY = d;
			if(d < minY) minY = d;
		}
		width = Math.abs(maxX - minX);
		height = Math.abs(maxY - minY);
		
		radius = Math.sqrt(Math.pow(width / 2, 2) + Math.pow(height / 2, 2));
		this.x = minX + width / 2;
		this.y = minY + height / 2;
	}

	/**
	 * Translate the position of the collider
	 * 
	 * @param x The x translation
	 * @param y the y translation
	 */
	public void translate(double x, double y) {
		setPosition(this.x + x, this.y + y);
	}

	/**
	 * Set the position of the collider
	 * 
	 * @param x The new {@link #x}
	 * @param y The new {@link #y}
	 */
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}

	// TODO: A funciton that gets line collision see
	// https://math.stackexchange.com/questions/2188507/how-do-you-find-the-point-of-intersection-of-2-vectors

	/**
	 * Return true if the given point is contained inside the boundary.<br/>
	 * See: <a href=
	 * "https://web.archive.org/web/20161108113341/https://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html">https://web.archive.org/web/20161108113341/https://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html</a>
	 * 
	 * @param x The x position of the point
	 * @param y The y position of the point
	 * @return true if the point is inside the boundary, false otherwise
	 */
	public boolean contains(double x, double y) {
		int i;
		int j;
		boolean result = false;
		for(i = 0, j = vertexCount - 1;i < vertexCount;j = i++) {
			if((verticesY[i] > y) != (verticesY[j] > y)
					&& (x < (verticesX[j] - verticesX[i]) * (y - verticesY[i]) / (verticesY[j] - verticesY[i])
							+ verticesX[i])) {
				result = !result;
			}
		}
		return result;
	}

	/**
	 * Checks if 2 colliders are close enough for collision events to occur
	 * 
	 * @param c The {@link Collider} to check against
	 * @return True if the distance between the midpoints is less than the combined
	 *         radii
	 */
	public boolean inRange(Collider c) {
		double dist = Math.sqrt(Math.pow(c.x - x, 2) + Math.pow(c.y - y, 2));
		return dist < (radius + c.radius) * 1.25;
	}

	/**
	 * Checks if any point is contained
	 * 
	 * @param c The {@link Collider} to check against
	 * @return True if all any point is contained
	 */
	public boolean intersects(Collider c) {
		if(!inRange(c)) return false;
		for(int i = 0;i < c.vertexCount;i++) {
			if(contains(c.verticesX[i], c.verticesY[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if all points are contained
	 * 
	 * @param c The {@link Collider} to check against
	 * @return True if all points are contained
	 */
	public boolean cointains(Collider c) {
		if(!inRange(c)) return false;
		for(int i = 0;i < c.vertexCount;i++) {
			if(!contains(c.verticesX[i], c.verticesY[i])) {
				return false;
			}
		}
		return true;

	}

	/**
	 * Return true if the given point is contained inside the boundary.<br/>
	 * Override for {@link #contains(double, double)}
	 * 
	 * @param p The point to check
	 * @return true if the point is inside the boundary, false otherwise
	 */
	public boolean contains(Point p) {
		return (contains(p.x, p.y));
	}

	/**
	 * Get the normalized vector representing the required pushback to exit collider
	 * 
	 * @param c The {@link Collider} to check against
	 * @return Pushback vector, null if no collision with c
	 */
	public abstract Vector getPushback(Collider c);

}
