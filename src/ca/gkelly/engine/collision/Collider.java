package ca.gkelly.engine.collision;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;

import ca.gkelly.engine.util.Logger;
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

	/** The x vertices of the collider, in world space */
	double[] verticesX;
	/** The y vertices of the collider, in world space */
	double[] verticesY;
	/** The x vertices of the collider, relative to {@link x} */
	double[] localVerticesX;
	/** The y vertices of the collider, relative to {@link y} */
	double[] localVerticesY;
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

		double xSum = 0;
		double ySum = 0;

		for(int i = 0;i < vertexCount;i++) {
			// Get min/max
			if(verticesX[i] > maxX) maxX = verticesX[i];
			if(verticesX[i] < minX) minX = verticesX[i];
			if(verticesY[i] > maxY) maxY = verticesY[i];
			if(verticesY[i] < minY) minY = verticesY[i];

			// Get values for midpoint
			if(i == vertexCount - 1) {
				xSum += (verticesX[i] + verticesX[0]) * (verticesX[i] * verticesY[0] - verticesX[0] * verticesY[i]);
				ySum += (verticesY[i] + verticesY[0]) * (verticesX[i] * verticesY[0] - verticesX[0] * verticesY[i]);
			} else {
				xSum += (verticesX[i] + verticesX[i + 1])
						* (verticesX[i] * verticesY[i + 1] - verticesX[i + 1] * verticesY[i]);
				ySum += (verticesY[i] + verticesY[i + 1])
						* (verticesX[i] * verticesY[i + 1] - verticesX[i + 1] * verticesY[i]);
			}
		}
		//Get rough boundaries
		width = Math.abs(maxX - minX);
		height = Math.abs(maxY - minY);
		radius = Math.sqrt(Math.pow(width / 2, 2) + Math.pow(height / 2, 2));
	
		//Get the midpoint
		this.x = xSum/(6 * getArea());
		this.y = ySum/(6 * getArea());
		
		//If the polygon is too small, the midpoint may be placed at 0,0.
		//This checks to if that is true, and the midpoint is nowhere near the simple midpoint
		if(Double.isNaN(this.x)){
			this.x = (minX+maxX)/2;
		}
		if(Double.isNaN(this.y)){
			this.y = (minY+maxY)/2;
		}

		localVerticesX = new double[vertexCount];
		localVerticesY = new double[vertexCount];
		for(int i = 0;i < vertexCount;i++) {
			localVerticesX[i] = verticesX[i] - x;
			localVerticesY[i] = verticesY[i] - x;
		}

	}

	/** Get the area of the polygon */
	public double getArea() {
		double sum = 0;
		for(int i = 0;i < vertexCount;i++) {
			if(i == vertexCount - 1)
				sum += verticesX[i] * verticesY[0] - verticesX[0] * verticesY[i];
			else
				sum += verticesX[i] * verticesY[i + 1] - verticesX[i + 1] * verticesY[i];
		}
		return 0.5 * sum;
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
			verticesX[i] = localVerticesX[i] + x;
			verticesY[i] = localVerticesY[i] + y;
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

	public double[][] getIntersections(Collider c) {
		if(!inRange(c)) return new double[0][];
		ArrayList<double[]> out = new ArrayList<>();

		for(int i = 0;i < vertexCount;i++) {
			Edge e1;
			if(i + 1 < vertexCount) { // If this is just a normal vertex, use the next one
				e1 = new Edge(verticesX[i], verticesY[i], verticesX[i + 1], verticesY[i + 1]);
			} else { // If this is the last vertex, wrap to the first
				e1 = new Edge(verticesX[i], verticesY[i], verticesX[0], verticesY[0]);
			}

			for(int j = 0;j < c.vertexCount;j++) {
				Edge e2;
				if(j + 1 < c.vertexCount) { // If this is just a normal vertex, use the next one
					e2 = new Edge(c.verticesX[j], c.verticesY[j], c.verticesX[j + 1], c.verticesY[j + 1]);
				} else { // If this is the last c.vertex, wrap to the first
					e2 = new Edge(c.verticesX[j], c.verticesY[j], c.verticesX[0], c.verticesY[0]);
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
	 * @return {@link PolyCollider} representing collision area
	 */
	public PolyCollider getCollisionPolygon(Collider c) {
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
			if(c.contains(verticesX[i], verticesY[i])) {
//				Logger.log("Point at " + verticesX[i] + "," + verticesY[i]);
				vertX.add(verticesX[i]);
				vertY.add(verticesY[i]);
				vertCount++;
				vertexFound = true;
			}
		}
		for(int i = 0;i < c.vertexCount;i++) {
			if(contains(c.verticesX[i], c.verticesY[i])) {
//				Logger.log("Point at " + c.verticesX[i] + "," + c.verticesY[i]);
				vertX.add(c.verticesX[i]);
				vertY.add(c.verticesY[i]);
				vertCount++;
				vertexFound = true;
			}
		}

		Logger.log(vertCount);

		ArrayList<Double>[] sorted = getHull(vertX.toArray(new Double[vertX.size()]),
				vertY.toArray(new Double[vertY.size()]), vertCount);
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

		return new PolyCollider(xp, yp, vertCount);
	}

	/**
	 * Get vector representing the required translation to exit collider
	 * 
	 * @param c The {@link Collider} to check against
	 * @return Pushback vector, null if no collision with c
	 */
	public Vector getPushback(Collider c) {
		final int MAX_PASS = 5; // The maximum nuber of times to attempt full removal
		int tries = 0;
		PolyCollider collision;
		Vector out = new Vector(0, 0);

		// If there is an intersection, attempt to remedy, up to MAX_PASS times
		while((collision = getCollisionPolygon(c)) != null && tries < MAX_PASS) {
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

			// If the horizontal is further in, deal with it
			if(horz < vert) {

			} else {

			}
		}
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
	 * @return An ArrayList containing the ordered x and y points that form the hull
	 */
	private ArrayList<Double>[] getHull(Double[] x, Double[] y, int vertCount) {
		// TODO: Allow for inlets, somehow
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

		return new ArrayList[] { vertX, vertY };

	}

	public String printVertices() {
		String out = "";
		for(int i = 0;i < vertexCount;i++) {
			out += "(" + verticesX[i] + "," + verticesY[i] + "), ";
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