package ca.gkelly.engine.collision;

import java.awt.Color;
import java.util.ArrayList;

import ca.gkelly.engine.graphics.Camera;
import ca.gkelly.engine.util.Logger;
import ca.gkelly.engine.util.Vector;
import ca.gkelly.engine.util.Vertex;

public class Hull {
	public Collider poly;
	ArrayList<Vertex> vertices = new ArrayList<Vertex>();
	Vertex extra = null;

	public Hull(ArrayList<Vertex> vertices, Vertex extra) {
		this.vertices = vertices;
		this.extra = extra;
		poly = new Collider(vertices);
	}

	/**
	 * Get the convex hull that contains the passed points, using an approximation
	 * of the gift wrapping algorithm
	 * 
	 * @param vertices The list of {@link Vertices}
	 * @return The convex {@link Hull} made from the points
	 */
	public Hull(Vertex[] vertices) {
		if(vertices.length > 4) {
			Logger.log("break");
		}
		Vertex ignored = null;

		ArrayList<Vertex> vertout = new ArrayList<>();

		// Join duplicate points, as they break system
		// The arrayLists will be used temporarily to avoid excess variables
		for (int i = 0; i < vertices.length; i++) {
			boolean add = true;
			for (int j = 0; j < vertout.size(); j++) {
				if (vertout.get(j).x == vertices[i].x && vertout.get(j).y == vertices[i].y)
					add = false;
			}
			if (add) {
				vertout.add(vertices[i]);
			}
		}

		// If there are 2 or less vertices, then the rest of the math is redundant
		if (vertout.size() > 2) {
			// Transfer data back to a Double[]
			vertices = vertout.toArray(new Vertex[vertout.size()]);

			// Reset the ArrayList
			vertout = new ArrayList<>();

			int start = 0; // Index of the leftmost x vertex

			// Get the leftmost vertex (using top y as tiebreak)
			for (int i = 0; i < vertices.length; i++) {
				if (vertices[i].x < vertices[start].x)
					start = i;
				else if (vertices[i].x == vertices[start].x && vertices[i].y < vertices[start].y) {
					start = i;
				}
			}

			// Add the initial vertex
			vertout.add(vertices[start]);

			int currentVertex = start;
			int bestVertex = 0;
			double bestAngle;
			double angle;
			ArrayList<Integer> usedVertices = new ArrayList<>();

			// Counter to break loop if iterations exceeeds number of vertices
			int lim = 0;
			while (lim < vertices.length) {
				lim++;
				bestAngle = Double.MAX_VALUE;
				for (int i = 0; i < vertices.length; i++) {
					// Don't pair with itself
					if (i == currentVertex)
						continue;
					// Get the angle from down
					angle = Vector.DOWN.getAngle(new Vector(vertices[i].x - vertices[currentVertex].x,
							vertices[i].y - vertices[currentVertex].y), false);

					// Make directly down always 0
					if (angle == Math.PI * 2) {
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

					// Don't reuse vertices
					if (usedVertices.contains(i))
						continue;

					// We want preferences towards angles that are to the left, so greater (but not
					// equal) to pi
					if (bestAngle > Math.PI && angle < Math.PI && bestAngle != Double.MAX_VALUE)
						continue;

					// If it's a better angle, save it
					if (bestAngle < Math.PI && angle > Math.PI) { // Better left than right
						bestAngle = angle;
						bestVertex = i;
					} else if (angle == 0 && bestAngle < Math.PI) { // An angle to the left is better than one straight
																	// down
						bestAngle = angle;
						bestVertex = i;
					} else if (bestAngle == 0 && angle > Math.PI) {
						bestAngle = angle;
						bestVertex = i;
					} else if (angle < bestAngle) {
						bestAngle = angle;
						bestVertex = i;
					}
				} 
				// If the best vertex is the initial, then we have completed the hull
				if (bestVertex == start) {
					usedVertices.add(start);
					break;
				}
				// Make the best vertex the next one in the list
				vertout.add(vertices[bestVertex]);
				currentVertex = bestVertex;
				usedVertices.add(bestVertex);
			}

			if (vertices.length > vertout.size()) {
				for (int i = 0; i < vertices.length; i++) {
					if (!usedVertices.contains(i))
						ignored = vertices[i];
				}
			}
		}
		this.vertices = vertout;
		this.extra = ignored;
		poly = new Collider(vertout);
	}

	public void render(Camera c) {
		c.drawPoly(poly.getPoly(), Color.green);
		c.drawPoint((int) poly.x, (int) poly.y, 10, Color.red);
		if (extra != null) {
			c.drawPoint((int) extra.x, (int) extra.y, 10, Color.magenta);
		}
		for (Vertex v : vertices) {
			c.drawPoint((int) v.x, (int) v.y, 5, Color.BLUE);
		}
	}

}
