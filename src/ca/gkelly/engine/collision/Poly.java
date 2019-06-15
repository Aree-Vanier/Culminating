package ca.gkelly.engine.collision;

import java.awt.Polygon;
import java.util.ArrayList;

import ca.gkelly.engine.util.Vertex;

/**
 * Class used to handle basic polygon functions<br/>
 * Used to reduce clutter in {@link Collider}
 */
class Poly {
	/** The x coordinate of the middle of the polygon */
	public double x;
	/** The y coordinate of the middle of the polygon */
	public double y;

	/** the vertices of the polygon, in world space */
	Vertex[] vertices;
	/** The y vertices of the polygon, relative to {@link x} and {@link y} */
	Vertex[] localVertices;
	/** The number of vertices of the polygon */

	/** The largest x vertex of the polygon, relative to {@link x} */
	private double maxX = Double.MIN_VALUE;
	/** The smallest x vertex of the polygon, relative to {@link x} */
	private double minX = Double.MAX_VALUE;
	/** The largest y vertex of the polygon, relative to {@link y} */
	private double maxY = Double.MIN_VALUE;
	/** The smallest y vertex of the polygon, relative to {@link y} */
	private double minY = Double.MAX_VALUE;
	/** The bounding width of the polygon */
	public double width;
	/** The bounding height of the polygon */
	public double height;

	/**
	 * Create the polygon with passed vertices
	 * 
	 * @param verticesX   The list of x vertices
	 * @param verticesY   The list of y vertices
	 * @param vertexCount The amount of vertices in the shape
	 */
	public Poly(double[] verticesX, double[] verticesY, int vertexCount) {
		Vertex[] vertices = new Vertex[vertexCount];
		for (int i = 0; i < vertexCount; i++) {
			vertices[i] = new Vertex(verticesX[i], verticesY[i]);
		}
		setVertices(vertices);
	}

	/**
	 * Create the polygon with passed vertices
	 * 
	 * @param vertices The list of {@link Vertex} vertices
	 */
	public Poly(Vertex[] vertices) {
		setVertices(vertices);
	}

	/**
	 * Create the polygon with passed vertices
	 * 
	 * @param vertices The list of {@link Vertex} vertices
	 */
	public Poly(ArrayList<Vertex> vertices) {
		setVertices(vertices.toArray(new Vertex[vertices.size()]));
	}

	/**
	 * Create the polygon with passed vertices
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

	/** Get the {@link java.awt.Polygon} with the same points */
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
	 * Translate the position of the polygon
	 * 
	 * @param x The x translation
	 * @param y the y translation
	 */
	public void translate(double x, double y) {
		setPosition(this.x + x, this.y + y);
	}

	/**
	 * Set the position of the polygon
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

	/** Get a string representation of the vertices */
	public String printVertices() {
		String out = "";
		for (int i = 0; i < vertices.length; i++) {
			out += "(" + vertices[i].x + "," + vertices[i].y + "), ";
		}
		return out;
	}
}
