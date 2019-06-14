package ca.gkelly.engine.collision;

import java.awt.Point;
import java.util.ArrayList;

import ca.gkelly.engine.util.Logger;
import ca.gkelly.engine.util.Vector;
import ca.gkelly.engine.util.Vertex;

/**
 * Generic class for creating colliders<br/>
 * has functions to handle generic polygon collision detection
 */
public abstract class Collider implements Cloneable {

	/** The x coordinate of the middle of the collider */
	public double x;
	/** The y coordinate of the middle of the collider */
	public double y;
	/** The distance from the middle to the extremity */
	double radius;

	/** the vertices of the collider, in world space*/
	Vertex[] vertices;
	/** The y vertices of the collider, relative to {@link x} and {@link y} */
	Vertex[] localVertices;
	/** The number of vertices of the collider */
	int vertexCount;

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
	public void setVertices(double[] verticesX, double[] verticesY, int vertexCount) {
		Vertex[] vertices = new Vertex[vertexCount];
		for(int i = 0; i<vertexCount; i++) {
			vertices[i] = new Vertex(vertices[i].x, vertices[i].y);
		}
	}
	/**
	 * Set the collider vertices
	 * 
	 * @param vertices   The list of {@link Vertex} vertices
	 */
	public void setVertices(Vertex[] vertices) {
		this.vertices = vertices;
		this.vertexCount = vertices.length;

		localVertices = new Vertex[vertexCount];

		double xSum = 0;
		double ySum = 0;
		// Used for determining initial X and Y point
		double worldMaxX = Double.MIN_VALUE;
		double worldMinX = Double.MAX_VALUE;
		double worldMaxY = Double.MIN_VALUE;
		double worldMinY = Double.MAX_VALUE;

		for(int i = 0;i < vertexCount;i++) {
			// Get values for midpoint
			if(i == vertexCount - 1) {
				xSum += (vertices[i].x + vertices[0].x) * (vertices[i].x * vertices[0].y - vertices[0].x * vertices[i].y);
				ySum += (vertices[i].y + vertices[0].y) * (vertices[i].x * vertices[0].y - vertices[0].x * vertices[i].y);
			} else {
				xSum += (vertices[i].x + vertices[i+1].x)
						* (vertices[i].x * vertices[i+1].y - vertices[i+1].x * vertices[i].y);
				ySum += (vertices[i].y + vertices[i+1].y)
						* (vertices[i].x * vertices[i+1].y - vertices[i+1].x * vertices[i].y);
			}
			if(vertices[i].x > worldMaxX) worldMaxX = vertices[i].x;
			if(vertices[i].x < worldMinX) worldMinX = vertices[i].x;
			if(vertices[i].y > worldMaxY) worldMaxY = vertices[i].y;
			if(vertices[i].y < worldMinY) worldMinY = vertices[i].y;
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
		if(Double.isNaN(this.x)) {
			this.x = (worldMinX + worldMaxX) / 2;
		}
		if(Double.isNaN(this.y)) {
			this.y = (worldMinY + worldMaxY) / 2;
		}

		// Setup locals
		for(int i = 0;i < vertexCount;i++) {
			// Set local vertices
			localVertices[i].x = vertices[i].x - x;
			localVertices[i].y = vertices[i].y - y;
			// Get min/max
			if(localVertices[i].x > maxX) {
				maxX = localVertices[i].x;
			}
			if(localVertices[i].x < minX) {
				minX = localVertices[i].x;
			}
			if(localVertices[i].y > maxY) {
				maxY = localVertices[i].y;
			}
			if(localVertices[i].y < minY) {
				minY = localVertices[i].y;
			}
		}

	}

	/** Get the area of the polygon */
	public double getArea() {
		double sum = 0;
		for(int i = 0;i < vertexCount;i++) {
			if(i == vertexCount - 1)
				sum += vertices[i].x * vertices[0].y - vertices[0].x * vertices[i].y;
			else
				sum += vertices[i].x * vertices[i + 1].y - vertices[i + 1].x * vertices[i].y;
		}
		return 0.5 * sum;
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

		for(int i = 0;i < vertexCount;i++) {
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
		for(i = 0, j = vertexCount - 1;i < vertexCount;j = i++) {
			if((vertices[i].y > y) != (vertices[j].y > y)
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
		if(!inRange(c)) return false;
		for(int i = 0;i < c.vertexCount;i++) {
			if(contains(c.vertices[i].x, c.vertices[i].y)) {
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
			if(!contains(c.vertices[i].x, c.vertices[i].y)) {
				return false;
			}
		}
		return true;

	}

	public double[][] getIntersections(Collider c) {
		if(!inRange(c)) return new double[0][];
		ArrayList<double[]> out = new ArrayList<>();

		for(int i = 0;i < vertexCount;i++) {
			Edge e1;
			if(i + 1 < vertexCount) { // If this is just a normal vertex, use the next one
				e1 = new Edge(vertices[i].x, vertices[i].y, vertices[i+1].x, vertices[i+1].y);
			} else { // If this is the last vertex, wrap to the first
				e1 = new Edge(vertices[i].x, vertices[i].y, vertices[0].x, vertices[0].y);
			}

			for(int j = 0;j < c.vertexCount;j++) {
				Edge e2;
				if(j + 1 < c.vertexCount) { // If this is just a normal vertex, use the next one
					e2 = new Edge(c.vertices[j].x, c.vertices[j].y, c.vertices[j+1].x, c.vertices[j+1].y);
				} else { // If this is the last c.vertex, wrap to the first
					e2 = new Edge(c.vertices[j].x, c.vertices[j].y, c.vertices[0].x, c.vertices[0].y);
				}

				double[] intersect = e1.getIntersect(e2);
				if(intersect != null) {
					out.add(intersect);
				}
			}
		}

		return out.toArray(new double[out.size()][]);

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
	 * Get polygon representing intersection between colliders
	 * 
	 * @param c The {@link Collider} to check against
	 * @return A object array containing: <br/>
	 *         - {@link PolyCollider} representing collision area <br/>
	 *         - The ignored point, should one exist
	 */
	public Hull getCollisionPolygon(Collider c) {
		double[][] intersections = c.getIntersections(this);
		// Don't bother with any of this if there are no intersections
		if(intersections.length == 0) return null;
		ArrayList<Double> vertX = new ArrayList<Double>();
		ArrayList<Double> vertY = new ArrayList<Double>();
		int vertCount = 0;
		for(double[] d: intersections) {
			vertX.add(d[0]);
			vertY.add(d[1]);
			vertCount++;
		}
		boolean vertexFound = false;
		for(int i = 0;i < vertexCount;i++) {
			if(c.contains(vertices[i].x, vertices[i].y)) {
//				Logger.log("Point at " + vertices[i].x + "," + vertices[i].y);
				vertX.add(vertices[i].x);
				vertY.add(vertices[i].y);
				vertCount++;
				vertexFound = true;
			}
		}
		for(int i = 0;i < c.vertexCount;i++) {
			if(contains(c.vertices[i].x, c.vertices[i].y)) {
//				Logger.log("Point at " + c.vertices[i].x + "," + c.vertices[i].y);
				vertX.add(c.vertices[i].x);
				vertY.add(c.vertices[i].y);
				vertCount++;
				vertexFound = true;
			}
		}

		Logger.log(vertCount);

		Object[] hullData = getHull(vertX.toArray(new Double[vertX.size()]), vertY.toArray(new Double[vertY.size()]),
				vertCount);
		ArrayList<Double>[] sortchromeed = (ArrayList<Double>[]) hullData[0];
		vertX = sorted[0];
		vertY = sorted[1];
		// Update the vertex count
		vertCount = vertX.size();

		double[] xp = new double[vertCount];
		double[] yp = new double[vertCount];
		for(int i = 0;i < vertCount;i++) {
			// Must be casted from Double to double, then to int
			xp[i] = (double) vertX.get(i);
			yp[i] = (double) vertY.get(i);
		}
		return new Hull(new PolyCollider(xp, yp, vertCount), hullData[1]);
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
		Object[] raw;
		Collider collision;
		Vector out = new Vector(0, 0);
		double oldX = x;
		double oldY = y;

		// If there is an intersection, attempt to remedy, up to MAX_PASS times
		while((raw = getCollisionPolygon(c)) != null && tries < MAX_TRIES) {
			getCollisionPolygon(c); // TODO: Remove when done debugging, this is just for breakpointing
			collision = (Collider) raw[0];
			tries++;
			Vector offset = new Vector(collision.x - x, collision.y - y);
			offset.setMag(-offset.getMag());
			// Get the projections of the offset vector (shorter = further in)
			double horz = Math.abs(Vector.dot(offset, Vector.LEFT));
			double vert = Math.abs(Vector.dot(offset, Vector.UP));
			Logger.log(horz + "\t" + vert);
			if(Double.isNaN(horz)) {
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
			if(horz > vert) {
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
	

	@SuppressWarnings("unchecked") // Issues with ArrayList[]s
	/**
	 * Get the convex hull that contains the passed points, using an approximation
	 * of the gift wrapping algorithm
	 * 
	 * @param x         The list of x points
	 * @param y         The list of y points
	 * @param vertCount The amount of vertices
	 * @return
	 * @return A object array containing: <br/>
	 *         - {@link PolyCollider} An ArrayList containing the ordered x and y
	 *         points that form the hull <br/>
	 *         - {@link Vertex} Representing the ignored point, should one exist
	 */
	private Object[] getHull(Double[] x, Double[] y, int vertCount) {
		Vertex ignored = null;

		ArrayList<Double> vertX = new ArrayList<>();
		ArrayList<Double> vertY = new ArrayList<>();

		// Join duplicate points, as they break system
		// The arrayLists will be used temporarily to avoid excess variables
		for(int i = 0;i < vertCount;i++) {
			boolean add = true;
			for(int j = 0;j < vertX.size();j++) {
				if(vertX.get(j).equals(x[i]) && vertY.get(j).equals(y[i])) add = false;
			}
			if(add) {
//				Logger.log(Logger.DEBUG, x[i] + "," + y[i], "\t", true);
				vertX.add(x[i]);
				vertY.add(y[i]);
			}
		}
//		Logger.log(Logger.DEBUG, "", "\n", true);

		// If there are 2 or less vertices, then the rest of the math is redundant
		if(vertX.size() <= 2) {
			return (new ArrayList[] { vertX, vertY });
		}

		// Transfer data back to Double[]s
		x = vertX.toArray(new Double[vertX.size()]);
		y = vertY.toArray(new Double[vertY.size()]);
		vertCount = x.length;

		// Reset the ArrayLists
		vertX = new ArrayList<>();
		vertY = new ArrayList<>();

		int start = 0; // Index of the leftmost x vertex
		// Get the leftmost vertex (using top y as tiebreak)
		for(int i = 0;i < vertCount;i++) {
			if(x[i] < x[start])
				start = i;
			else if(x[i].equals(x[start]) && y[i] < y[start]) {
				start = i;
			}
		}

		// Add the initial vertex
		vertX.add(x[start]);
		vertY.add(y[start]);

		int currentVertex = start;
		int bestVertex = 0;
		double bestAngle;
		double angle;
		ArrayList<Integer> usedVertices = new ArrayList<>();

		// Counter to break loop if iterations exceeeds number of vertices
		int lim = 0;
		while(lim < vertCount) {
			lim++;
			bestAngle = Double.MAX_VALUE;
			for(int i = 0;i < vertCount;i++) {
				// Don't pair with itself
				if(i == currentVertex) continue;
				// Get the angle from down
				angle = Vector.DOWN.getAngle(new Vector(x[i] - x[currentVertex], y[i] - y[currentVertex]), false);

				// Make directly down always 0
				if(angle == Math.PI * 2) {
					angle = 0;
				}

				// If we are looking from the top-left vertex, we can start from straight up
				// This prevents us from accidentally selecting a vertex directly below, which
				// would skip all others
				if(currentVertex == start) {
					angle -= Math.PI;
				}

				// Shift negative values into target range
				if(angle < 0) {
					angle += Math.PI * 2;
				}

				// Don't reuse vertices
				if(usedVertices.contains(i)) continue;

				// We want preferences towards angles that are to the left, so greater (but not
				// equal) to pi
				if(bestAngle > Math.PI && angle < Math.PI && bestAngle != Double.MAX_VALUE) continue;

				// If it's a better angle, save it
				if(bestAngle < Math.PI && angle > Math.PI) { // Better left than right
					bestAngle = angle;
					bestVertex = i;
				} else if(angle == 0 && bestAngle < Math.PI) { // An angle to the left is better than one straight down
					bestAngle = angle;
					bestVertex = i;
				} else if(bestAngle == 0 && angle > Math.PI) {
					bestAngle = angle;
					bestVertex = i;
				} else if(angle < bestAngle) {
					bestAngle = angle;
					bestVertex = i;
				}
			}
			if(bestVertex == start) // If the best vertex is the initial, then we have completed the hull
				break;
			// Make the best vertex the next one in the list
			vertX.add(x[bestVertex]);
			vertY.add(y[bestVertex]);
			currentVertex = bestVertex;
			usedVertices.add(bestVertex);
		}

		if(vertexCount > vertX.size()) {
			for(int i = 0;i < vertexCount;i++) {
				if(!usedVertices.contains(i)) ignored = new Vertex(x[i], y[i]);
			}
		}
		return new Object[] { new ArrayList[] { vertX, vertY }, ignored };

	}

	public String printVertices() {
		String out = "";
		for(int i = 0;i < vertexCount;i++) {
			out += "(" + vertices[i].x + "," + vertices[i].y + "), ";
		}
		return out;
	}

}

class Edge {
	double slope;
	double b;
	double x1, x2, y1, y2;
	boolean undefined = false;

	Edge(double x1, double y1, double x2, double y2) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;

		if((x2 - x1) != 0) {// If the line isn't vertical
			slope = ((y2 - y1) / (x2 - x1));
			b = y1 - slope * (x1);
		} else { // If the line is vertical
			undefined = true;
		}
	}

	public double[] getIntersect(Edge e) {
		double x, y;

		if(undefined) { // If this line is vertical, the x is it's
			x = x1;
			y = e.slope * x + e.b;
		} else if(e.undefined) { // If the other line is vertical
			x = e.x1;
			y = slope * x + b;
		} else {
			x = (e.b - b) / (slope - e.slope);
			y = slope * x + b;
		}

		// If the point exists within the span of the lines
		if(inRange(x, y) && e.inRange(x, y)) {
			return new double[] { x, y };
		} else {
			return null;
		}

	}

	public boolean inRange(double x, double y) {
		return ((x <= x1 && x >= x2) || (x >= x1 && x <= x2)) && ((y <= y1 && y >= y2) || (y >= y1 && y <= y2));
	}
}