package ca.gkelly.engine.collision;

import java.util.ArrayList;

import ca.gkelly.engine.util.Logger;
import ca.gkelly.engine.util.Vector;
import ca.gkelly.engine.util.Vertex;

/**
 * Generic collider class<br/>
 * has functions to handle generic polygon collision detection
 */
public class Collider extends Poly {

	/** The distance from the middle to the extremity */
	double radius;

	/**
	 * Create the collider with passed vertices
	 * 
	 * @param verticesX   The list of x vertices
	 * @param verticesY   The list of y vertices
	 * @param vertexCount The amount of vertices in the shape
	 */
	public Collider(Vertex[] vertices) {
		super(vertices);
		radius = Math.sqrt(Math.pow(width / 2, 2) + Math.pow(height / 2, 2));
	}

	/**
	 * Create the collider with passed vertices
	 * 
	 * @param vertices The list of {@link Vertex} vertices
	 */
	public Collider(ArrayList<Vertex> vertices) {
		super(vertices);
		radius = Math.sqrt(Math.pow(width / 2, 2) + Math.pow(height / 2, 2));
	}

	/**
	 * Create the collider with passed vertices
	 * 
	 * @param vertices The list of {@link Vertex} vertices
	 */
	public Collider(double[] verticesX, double[] verticesY, int vertexCount) {
		super(verticesX, verticesY, vertexCount);
		radius = Math.sqrt(Math.pow(width / 2, 2) + Math.pow(height / 2, 2));
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

	/**
	 * Get all intersections between the edges of colliders
	 * 
	 * @param c The other collider to check against
	 * @return An array containing the intersection {@link Vertex Vertices}
	 */
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
			tries++;
			Vector offset = new Vector(raw.poly.x - x, raw.poly.y - y);
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

			// Get the deltas to apply
			Vector delta = getDeltas(c, raw.poly, offset, raw.extra);
			Logger.log("Deltas: " + delta.getX() + '\t' + delta.getY());

			// If the horizontal is further in, deal with it
			if (horz > vert) {
				translate(delta.getX(), 0);
				out = Vector.add(out, new Vector(delta.getX(), 0));
			} else {
				translate(0, delta.getY());
				out = Vector.add(out, new Vector(0, delta.getY()));
			}
			Logger.log(Logger.INFO, out.getString(Vector.STRING_RECTANGULAR));
		}
		setPosition(oldX, oldY);
		return new Vector(out.getX(), out.getY());
	}

	private Vector getDeltas(Collider other, Collider collision, Vector offset, Vertex extra) {
		Logger.log(Logger.INFO, collision.getRight() + "\t" + collision.getTop() + "\t" + collision.getLeft() + "\t"
				+ collision.getBottom());
		Logger.log(Logger.INFO, getRight() + "\t" + getTop() + "\t" + getLeft() + "\t" + getBottom());
		Logger.log(Logger.INFO, offset.getX() + "," + offset.getY());
		double deltaX = 0, deltaY = 0;
		Hull h;
		// Specaial handling for hulls with a unused midpoint, as the simple w/h
		// translation wont work
		Logger.log(Logger.ERROR, collision.vertices.length);
		if ((collision.vertices.length == 4 || collision.vertices.length == 5) && extra != null) {
			Logger.log(Logger.ERROR, collision.getString() + ", " + extra.getString());
		}
		if (extra != null) {
			Logger.log(Logger.ERROR, "Extra");
			if (offset.getX() > 0) {
				deltaX = Math.abs(collision.getLeft() - extra.x);
				Logger.log(Logger.INFO, "RL " + deltaX);
			}
			if (offset.getX() < 0) {
				deltaX = -Math.abs(collision.getRight() - extra.x);
				Logger.log(Logger.INFO, "LR " + deltaX);
			}
			if (offset.getY() > 0) {
				deltaY = Math.abs(collision.getTop() - extra.y);
				Logger.log(Logger.INFO, "TB " + deltaY);
			}
			if (offset.getY() < 0) {
				deltaY = -Math.abs(collision.getBottom() - extra.y);
				Logger.log(Logger.INFO, "BT " + deltaY);
			}
		} else if (collision.vertices.length % 2 == 1 && (h = new Hull(getIntersections(other))).vertices.size() > 2) { 
			// If there are an odd number of vertices, and the hull isn't just a line, then extra handling may be required
			// Get the hull formed by the intersection points only, not the contained points (see if statement)
			
			// Move by the distance between the two hulls
			if (offset.getX() > 0) {
				deltaX = Math.abs(collision.getLeft() - h.poly.getLeft());
				Logger.log(Logger.INFO, "RL " + deltaX);
			}
			if (offset.getX() < 0) {
				deltaX = -Math.abs(collision.getRight() - h.poly.getRight());
				Logger.log(Logger.INFO, "LR " + deltaX);
			}
			if (offset.getY() > 0) {
				deltaY = Math.abs(collision.getTop() - h.poly.getTop());
				Logger.log(Logger.ERROR, "TB " + collision.getTop() + "," + h.poly.getTop());
			}
			if (offset.getY() < 0) {
				deltaY = -Math.abs(collision.getBottom() - h.poly.getBottom());
				Logger.log(Logger.INFO, "BT " + deltaY);
			}
		} else {
			//If none of the edge cases are true, the just move by the dimensions of the collision polygon
			deltaX = collision.width * Integer.signum((int) offset.getX());
			deltaY = collision.height * Integer.signum((int) offset.getY());
		}
		return new Vector(deltaX, deltaY);
	}

}
