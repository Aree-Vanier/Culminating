package ca.gkelly.engine.collision;

import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;

import ca.gkelly.engine.util.Logger;
import ca.gkelly.engine.util.Vector;
import ca.gkelly.engine.util.Vertex;

/**
 * Generic class for creating colliders<br/>
 * has functions to handle generic polygon collision detection
 */
public class Collider implements Cloneable {

	/** The x coordinate of the middle of the collider */
	public double x;
	/** The y coordinate of the middle of the collider */
	public double y;
	/** The distance from the middle to the extremity */
	double radius;

	/** the vertices of the collider, in world space */
	Vertex[] vertices;
	/** The y vertices of the collider, relative to {@link x} and {@link y} */
	Vertex[] localVertices;
	/** The number of vertices of the collider */

	/** The largest x vertex of the collider, relative to {@link x} */
	private double maxX = Double.MIN_VALUE;
	/** The smallest x vertex of the collider, relative to {@link x} */
	private double minX = Double.MAX_VALUE;
	/** The largest y vertex of the collider, relative to {@link y} */
	private double maxY = Double.MIN_VALUE;
	/** The smallest y vertex of the collider, relative to {@link y} */
	private double minY = Double.MAX_VALUE;
	/** The bounding width of the collider */
	public double width;
	/** The bounding height of the collider */
	public double height;

	/**
	 * Set the collider vertices
	 * 
	 * @param verticesX   The list of x vertices
	 * @param verticesY   The list of y vertices
	 * @param vertexCount The amount of vertices in the shape
	 */
	public Collider(double[] verticesX, double[] verticesY, int vertexCount) {
		Vertex[] vertices = new Vertex[vertexCount];
		for (int i = 0; i < vertexCount; i++) {
			vertices[i] = new Vertex(verticesX[i], verticesY[i]);
		}
		setVertices(vertices);
	}

	/**
	 * Set the collider vertices
	 * 
	 * @param vertices The list of {@link Vertex} vertices
	 */
	public Collider(Vertex[] vertices) {
		setVertices(vertices);
	}
	
	public Collider(ArrayList<Vertex> vertices) {
		setVertices(vertices.toArray(new Vertex[vertices.size()]));
	}

	/**
	 * Set the collider vertices
	 * 
	 * @param vertices The list of {@link Vertex} vertices
	 */
	public void setVertices(Vertex[] vertices) {
		this.vertices = vertices;

		localVertices = new Vertex[vertices.length];

		double xSum = 0;
		double ySum = 0;
		// Used for determining initial X and Y point
		double worldMaxX = Double.MIN_VALUE;
		double worldMinX = Double.MAX_VALUE;
		double worldMaxY = Double.MIN_VALUE;
		double worldMinY = Double.MAX_VALUE;

		for (int i = 0; i < vertices.length; i++) {
			// Get values for midpoint
			if (i == vertices.length - 1) {
				xSum += (vertices[i].x + vertices[0].x)
						* (vertices[i].x * vertices[0].y - vertices[0].x * vertices[i].y);
				ySum += (vertices[i].y + vertices[0].y)
						* (vertices[i].x * vertices[0].y - vertices[0].x * vertices[i].y);
			} else {
				xSum += (vertices[i].x + vertices[i + 1].x)
						* (vertices[i].x * vertices[i + 1].y - vertices[i + 1].x * vertices[i].y);
				ySum += (vertices[i].y + vertices[i + 1].y)
						* (vertices[i].x * vertices[i + 1].y - vertices[i + 1].x * vertices[i].y);
			}
			if (vertices[i].x > worldMaxX)
				worldMaxX = vertices[i].x;
			if (vertices[i].x < worldMinX)
				worldMinX = vertices[i].x;
			if (vertices[i].y > worldMaxY)
				worldMaxY = vertices[i].y;
			if (vertices[i].y < worldMinY)
				worldMinY = vertices[i].y;
		}
		// Get rough boundaries
		width = Math.abs(worldMaxX - worldMinX);
		height = Math.abs(worldMaxY - worldMinY);
		radius = Math.sqrt(Math.pow(width / 2, 2) + Math.pow(height / 2, 2));

		// Get the midpoint
		this.x = xSum / (6 * getArea());
		this.y = ySum / (6 * getArea());

		// If the polygon is too small, the midpoint may be placed at 0,0.
		// This checks to if that is true, and the midpoint is nowhere near the simple
		// midpoint
		if (Double.isNaN(this.x)) {
			this.x = (worldMinX + worldMaxX) / 2;
		}
		if (Double.isNaN(this.y)) {
			this.y = (worldMinY + worldMaxY) / 2;
		}

		// Setup locals
		for (int i = 0; i < vertices.length; i++) {
			// Set local vertices
			localVertices[i] = new Vertex(vertices[i].x - x, vertices[i].y - y);
			// Get min/max
			if (localVertices[i].x > maxX) {
				maxX = localVertices[i].x;
			}
			if (localVertices[i].x < minX) {
				minX = localVertices[i].x;
			}
			if (localVertices[i].y > maxY) {
				maxY = localVertices[i].y;
			}
			if (localVertices[i].y < minY) {
				minY = localVertices[i].y;
			}
		}

	}

	/** Get the area of the polygon */
	public double getArea() {
		double sum = 0;
		for (int i = 0; i < vertices.length; i++) {
			if (i == vertices.length - 1)
				sum += vertices[i].x * vertices[0].y - vertices[0].x * vertices[i].y;
			else
				sum += vertices[i].x * vertices[i + 1].y - vertices[i + 1].x * vertices[i].y;
		}
		return 0.5 * sum;
	}

	public Polygon getPoly() {
		int[] x = new int[vertices.length];
		int[] y = new int[vertices.length];
		for (int i = 0; i < vertices.length; i++) {
			x[i] = (int) vertices[i].x;
			y[i] = (int) vertices[i].y;
		}
		return new Polygon(x, y, vertices.length);
	}

	/** Get the y value of the top most point */
	public double getTop() {
		return minY + y;
	}

	/** Get the y value of the bottom most point */
	public double getBottom() {
		return maxY + y;
	}

	/** Get the x value of the left most point */
	public double getLeft() {
		return minX + x;
	}

	/** Get the x value of the right most point */
	public double getRight() {
		return maxX + x;
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

		for (int i = 0; i < vertices.length; i++) {
			vertices[i].x = localVertices[i].x + x;
			vertices[i].y = localVertices[i].y + y;
		}
	}

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
		for (i = 0, j = vertices.length - 1; i < vertices.length; j = i++) {
			if ((vertices[i].y > y) != (vertices[j].y > y)
					&& (x < (vertices[j].x - vertices[i].x) * (y - vertices[i].y) / (vertices[j].y - vertices[i].y)
							+ vertices[i].x)) {
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
		if (!inRange(c))
			return false;
		for (int i = 0; i < c.vertices.length; i++) {
			if (contains(c.vertices[i].x, c.vertices[i].y)) {
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
		if (!inRange(c))
			return false;
		for (int i = 0; i < c.vertices.length; i++) {
			if (!contains(c.vertices[i].x, c.vertices[i].y)) {
				return false;
			}
		}
		return true;

	}

	public Vertex[] getIntersections(Collider c) {
		if (!inRange(c))
			return new Vertex[0];
		ArrayList<Vertex> out = new ArrayList<>();

		for (int i = 0; i < vertices.length; i++) {
			Edge e1;
			if (i + 1 < vertices.length) { // If this is just a normal vertex, use the next one
				e1 = new Edge(vertices[i], vertices[i + 1]);
			} else { // If this is the last vertex, wrap to the first
				e1 = new Edge(vertices[i], vertices[0]);
			}

			for (int j = 0; j < c.vertices.length; j++) {
				Edge e2;
				if (j + 1 < c.vertices.length) { // If this is just a normal vertex, use the next one
					e2 = new Edge(c.vertices[j], c.vertices[j + 1]);
				} else { // If this is the last c.vertex, wrap to the first
					e2 = new Edge(c.vertices[j], c.vertices[0]);
				}

				Vertex intersect = e1.getIntersect(e2);
				if (intersect != null) {
					out.add(intersect);
				}
			}
		}

		return out.toArray(new Vertex[out.size()]);

	}

	/**
	 * Return true if the given point is contained inside the boundary.<br/>
	 * Override for {@link #contains(double, double)}
	 * 
	 * @param v The {@link vertex} to check
	 * @return true if the point is inside the boundary, false otherwise
	 */
	public boolean contains(Vertex v) {
		return (contains(v.x, v.y));
	}

	/**
	 * Get polygon representing intersection between colliders
	 * 
	 * @param c The {@link Collider} to check against
	 * @return The {@link Hull} representing the collision area
	 */
	public Hull getCollisionHull(Collider c) {
		Vertex[] intersections = c.getIntersections(this);
		// Don't bother with any of this if there are no intersections
		if (intersections.length == 0)
			return null;
		ArrayList<Vertex> vertout = new ArrayList<Vertex>();
		int vertCount = 0;
		for (Vertex v : intersections) {
			vertout.add(v);
			vertCount++;
		}
		boolean vertexFound = false;
		for (int i = 0; i < this.vertices.length; i++) {
			if (c.contains(vertices[i].x, vertices[i].y)) {
//				Logger.log("Point at " + vertices[i].x + "," + vertices[i].y);
				vertout.add(new Vertex(vertices[i].x, vertices[i].y));
				vertCount++;
				vertexFound = true;
			}
		}
		for (int i = 0; i < c.vertices.length; i++) {
			if (contains(c.vertices[i].x, c.vertices[i].y)) {
//				Logger.log("Point at " + c.vertices[i].x + "," + c.vertices[i].y);
				vertout.add(new Vertex(c.vertices[i].x, c.vertices[i].y));
				vertCount++;
				vertexFound = true;
			}
		}

		Logger.log(vertCount);

		return new Hull(vertout.toArray(new Vertex[vertout.size()]));
	}

	/**
	 * Get vector representing the required translation to exit collider
	 * 
	 * @param c The {@link Collider} to check against
	 * @return Pushback vector, null if no collision with c
	 */
	public Vector getPushback(Collider c) {
		final int MAX_TRIES = 5; // The maximum nuber of times to attempt full removal TODO move somewhere better
		int tries = 0;
		Hull raw;
		Collider collision;
		Vector out = new Vector(0, 0);
		double oldX = x;
		double oldY = y;

		// If there is an intersection, attempt to remedy, up to MAX_PASS times
		while ((raw = getCollisionHull(c)) != null && tries < MAX_TRIES) {
			getCollisionHull(c); // TODO: Remove when done debugging, this is just for breakpointing
			collision = (Collider) raw.poly;
			tries++;
			Vector offset = new Vector(collision.x - x, collision.y - y);
			offset.setMag(-offset.getMag());
			// Get the projections of the offset vector (shorter = further in)
			double horz = Math.abs(Vector.dot(offset, Vector.LEFT));
			double vert = Math.abs(Vector.dot(offset, Vector.UP));
			Logger.log(horz + "\t" + vert);
			if (Double.isNaN(horz)) {
				Logger.log("nan");
				offset.setMag(-offset.getMag());
				// Get the projections of the offset vector (shorter = further in)
				horz = Math.abs(Vector.dot(offset, Vector.LEFT));
				vert = Math.abs(Vector.dot(offset, Vector.UP));
			}

			Logger.log(Logger.INFO, collision.getRight() + "\t" + collision.getTop() + "\t" + collision.getLeft() + "\t"
					+ collision.getBottom());
			Logger.log(Logger.INFO, getRight() + "\t" + getTop() + "\t" + getLeft() + "\t" + getBottom());
			Logger.log(Logger.INFO, offset.getX() + "," + offset.getY());
			double deltaX = 0, deltaY = 0;
			// Specaial handling for triangles, as the simple w+h translation wont work
//			if(Integer.signum((int) offset.getX()) > 0) {
//				deltaX = Math.abs(collision.getRight() - getLeft());
//				Logger.log(Logger.INFO, "RL " + deltaX);
//			}
//			if(Integer.signum((int) offset.getX()) < 0) {
//				deltaX = -Math.abs(collision.getLeft() - getRight());
//				Logger.log(Logger.INFO, "LR " + deltaX);
//			}
//			if(Integer.signum((int) offset.getY()) > 0) {
//				deltaY = Math.abs(collision.getBottom() - getTop());
//				Logger.log(Logger.INFO, "TB " + deltaY);
//			}
//			if(Integer.signum((int) offset.getY()) < 0) {
//				deltaY = -Math.abs(collision.getTop() - getBottom());
//				Logger.log(Logger.INFO, "BT " + deltaY);
//			}

			deltaX = collision.width * Integer.signum((int) offset.getX());
			deltaY = collision.height * Integer.signum((int) offset.getY());
			Logger.log("Deltas: " + deltaX + '\t' + deltaY);
			// If the horizontal is further in, deal with it
			if (horz > vert) {
				translate(deltaX, 0);
				out = Vector.add(out, new Vector(deltaX, 0));
			} else {
				translate(0, deltaY);
				out = Vector.add(out, new Vector(0, deltaY));
			}
			Logger.log(Logger.INFO, out.getString(Vector.STRING_RECTANGULAR));
		}
		setPosition(oldX, oldY);
		return out;
	}

	public String printVertices() {
		String out = "";
		for (int i = 0; i < vertices.length; i++) {
			out += "(" + vertices[i].x + "," + vertices[i].y + "), ";
		}
		return out;
	}

}
