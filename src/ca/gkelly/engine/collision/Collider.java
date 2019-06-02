package ca.gkelly.engine.collision;

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

		for (double d : verticesX) {
			if (d > maxX)
				maxX = d;
			if (d < minX)
				minX = d;
		}
		for (double d : verticesY) {
			if (d > maxY)
				maxY = d;
			if (d < minY)
				minY = d;
		}
		width = Math.abs(maxX - minX);
		height = Math.abs(maxY - minY);

		radius = Math.sqrt(Math.pow(width / 2, 2) + Math.pow(height / 2, 2));
		this.x = minX + width / 2;
		this.y = minY + height / 2;

		localVerticesX = new double[vertexCount];
		localVerticesY = new double[vertexCount];
		for (int i = 0; i < vertexCount; i++) {
			localVerticesX[i] = verticesX[i] - x;
			localVerticesY[i] = verticesY[i] - x;
		}

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

		for (int i = 0; i < vertexCount; i++) {
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
		for (i = 0, j = vertexCount - 1; i < vertexCount; j = i++) {
			if ((verticesY[i] > y) != (verticesY[j] > y)
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
		if (!inRange(c))
			return false;
		for (int i = 0; i < c.vertexCount; i++) {
			if (contains(c.verticesX[i], c.verticesY[i])) {
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
		for (int i = 0; i < c.vertexCount; i++) {
			if (!contains(c.verticesX[i], c.verticesY[i])) {
				return false;
			}
		}
		return true;

	}

	public double[][] getIntersections(Collider c) {
		if (!inRange(c))
			return new double[0][];
		if (!intersects(c))
			return new double[0][];
		ArrayList<double[]> out = new ArrayList<>();

		for (int i = 0; i < vertexCount; i++) {
			Edge e1;
			if (i + 1 < vertexCount) { // If this is just a normal vertex, use the next one
				e1 = new Edge(verticesX[i], verticesY[i], verticesX[i + 1], verticesY[i + 1]);
			} else { // If this is the last vertex, wrap to the first
				e1 = new Edge(verticesX[i], verticesY[i], verticesX[0], verticesY[0]);
			}

			for (int j = 0; j < c.vertexCount; j++) {
				Edge e2;
				if (j + 1 < c.vertexCount) { // If this is just a normal vertex, use the next one
					e2 = new Edge(c.verticesX[j], c.verticesY[j], c.verticesX[j + 1], c.verticesY[j + 1]);
				} else { // If this is the last c.vertex, wrap to the first
					e2 = new Edge(c.verticesX[j], c.verticesY[j], c.verticesX[0], c.verticesY[0]);
				}

				double[] intersect = e1.getIntersect(e2);
				if (intersect != null) {
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
	 * Get vector representing the required translation to exit collider
	 * 
	 * @param c The {@link Collider} to check against
	 * @return Pushback vector, null if no collision with c
	 */
	public Polygon getPushback(Collider c) {
		double[][] intersections = c.getIntersections(this);
		// Don't bother with any of this if there are no intersections
		Logger.log(intersections.length);
		if (intersections.length == 0)
			return null;// new Vector(0, 0);
		ArrayList<Double> vertX = new ArrayList<Double>();
		ArrayList<Double> vertY = new ArrayList<Double>();
		int vertCount = 0;
		for (double[] d : intersections) {
			vertX.add(d[0]);
			vertY.add(d[1]);
			vertCount++;
		}
		boolean vertexFound = false;
		for (int i = 0; i < vertexCount; i++) {
			if (c.contains(verticesX[i], verticesY[i])) {
				Logger.log("Point at " + verticesX[i] + "," + verticesY[i]);
				vertX.add(verticesX[i]);
				vertY.add(verticesY[i]);
				vertCount++;
				vertexFound = true;
			}
		}
		for (int i = 0; i < c.vertexCount; i++) {
			if (contains(c.verticesX[i], c.verticesY[i])) {
				Logger.log("Point at " + c.verticesX[i] + "," + c.verticesY[i]);
				vertX.add(c.verticesX[i]);
				vertY.add(c.verticesY[i]);
				vertCount++;
				vertexFound = true;
			}
		}

		ArrayList<Double>[] sorted = getHull(vertX.toArray(new Double[vertX.size()]),
				vertY.toArray(new Double[vertY.size()]), vertCount);
		vertX = sorted[0];
		vertY = sorted[1];
		// Update the vertex count
		vertCount = vertX.size();

		int[] xp = new int[vertCount];
		int[] yp = new int[vertCount];
		for (int i = 0; i < vertCount; i++) {
			// Must be casted from Double to double, then to int
			xp[i] = (int) (double) vertX.get(i);
			yp[i] = (int) (double) vertY.get(i);
		}

		return new Polygon(xp, yp, vertCount);
	}

	private ArrayList<Double>[] getHull(Double[] x, Double[] y, int vertCount) {
		// TODO: Allow for inlets, somehow
		ArrayList<Double> vertX = new ArrayList<>();
		ArrayList<Double> vertY = new ArrayList<>();

		// Join duplicate points, as they break system
		// The arrayLists will be used temporarily to avoid excess variables
		for (int i = 0; i < vertCount; i++) {
			boolean add = true;
			for (int j = 0; j < vertX.size(); j++) {
				if (vertX.get(j).equals(x[i]) && vertY.get(j).equals(y[i]))
					add = false;
			}
			if (add) {
				Logger.log(Logger.DEBUG, x[i]+","+y[i], "\t", true);
				vertX.add(x[i]);
				vertY.add(y[i]);
			}
		}
		Logger.log(Logger.DEBUG, "", "\n", true);
		Logger.log(vertX.size());
		

		// If there are 2 or less vertices, then the rest of the math is redundant
		if (vertX.size() <= 2) {
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
		for (int i = 0; i < vertCount; i++) {
			if (x[i] < x[start])
				start = i;
			else if (x[i].equals(x[start]) && y[i] < y[start]) {
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

		while (true) {
			bestAngle = Double.MAX_VALUE;
			for (int i = 0; i < vertCount; i++) {
				// Don't pair with itself
				if (i == currentVertex)
					continue;
				// Get the angle from down
				angle = Vector.DOWN.getAngle(new Vector(x[i] - x[currentVertex], y[i] - y[currentVertex]), false);

				// Make directly down always 0
				if (angle == Math.PI*2) {
					angle = 0;
				}

				// If we are looking from the top-left vertex, we can start from straight up
				// This prevents us from accidentally selecting a vertex directly below, which
				// would skip all others
				if (currentVertex == start) {
					angle -= Math.PI;
				}

				// Shift negative values into target range
				if (angle < 0) {
					angle += Math.PI * 2;
				}

				// If it's a better angle, save it
				if (angle < bestAngle) {
					bestAngle = angle;
					bestVertex = i;
				}
			}
			if (bestVertex == start) // If the best vertex is the initial, then we have completed the hull
				break;
			// Make the best vertex the next one in the list
			vertX.add(x[bestVertex]);
			vertY.add(y[bestVertex]);
			currentVertex = bestVertex;
		}

//		//Convert to double arrays
//		double[] xout = new double[vertX.size()];
//		for(int i = 0; i<vertX.size(); i++) {
//			xout[i] = vertX.get(i);
//		}
//		double[] yout = new double[vertY.size()];
//		for(int i = 0; i<vertY.size(); i++) {
//			yout[i] = vertY.get(i);
//		}

		return new ArrayList[] { vertX, vertY };

	}

	public String printVertices() {
		String out = "";
		for (int i = 0; i < vertexCount; i++) {
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

		if ((x2 - x1) != 0) {// If the line isn't vertical
			slope = ((y2 - y1) / (x2 - x1));
			b = y1 - slope * (x1);
		} else { // If the line is vertical
			undefined = true;
		}
	}

	public double[] getIntersect(Edge e) {
		double x, y;

		if (undefined) { // If this line is vertical, the x is it's
			x = x1;
			y = e.slope * x + e.b;
		} else if (e.undefined) { // If the other line is vertical
			x = e.x1;
			y = slope * x + b;
		} else {
			x = (e.b - b) / (slope - e.slope);
			y = slope * x + b;
		}

		// If the point exists within the span of the lines
		if (inRange(x, y) && e.inRange(x, y)) {
			return new double[] { x, y };
		} else {
			return null;
		}

	}

	public boolean inRange(double x, double y) {
		return ((x <= x1 && x >= x2) || (x >= x1 && x <= x2)) && ((y <= y1 && y >= y2) || (y >= y1 && y <= y2));
	}
}