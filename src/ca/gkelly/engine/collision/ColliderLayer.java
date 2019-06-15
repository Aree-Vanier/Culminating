package ca.gkelly.engine.collision;

import java.awt.Color;
import java.awt.Polygon;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import ca.gkelly.engine.graphics.Camera;
import ca.gkelly.engine.util.Vertex;

/** A class used to handle the colliders on a map */
public class ColliderLayer {
	public Collider[] polygons;
	public String name;

	/**
	 * Create the collider layer
	 * 
	 * @param polys The list of polygons
	 * @param name  The name to used for identification
	 */
	public ColliderLayer(NodeList polys, String name) {
		this.name = name;
		polygons = new Collider[polys.getLength()];
		for(int j = 0;j < polys.getLength();j++) {
			Element poly = (Element) polys.item(j);
			int x = Integer.parseInt(poly.getAttribute("x"));
			int y = Integer.parseInt(poly.getAttribute("y"));

			poly = (Element) poly.getElementsByTagName("polygon").item(0);
			String[] points = poly.getAttribute("points").split(" ");
			// Create int[]s for x and y points
			Vertex[] vertices = new Vertex[points.length];
			for(int s = 0;s < points.length;s++) {
				vertices[s] = new Vertex((Integer.parseInt(points[s].split(",")[0])) + x,
						(Integer.parseInt(points[s].split(",")[1])) + y);
			}
			// Create the corresponding polygon
			polygons[j] = new Collider(vertices);
		}
	}

	/**
	 * Draw the polygons to the screen
	 * 
	 * @param cam The camera to use
	 * @param c   The colour to use
	 */
	public void render(Camera cam, Color c) {
		for(Collider collider: polygons) {
			cam.drawPoly(collider.getPoly(), c);
		}
	}

	/**
	 * Get the polygon that contains the point
	 * 
	 * @param x The x value of the point
	 * @param y The y value of the point
	 * @return The polygon that contains the point<br/>
	 *         <strong>null</strong> if no polygon contains the point
	 */
	public Polygon getPoly(int x, int y) {
		for(Collider p: polygons) {
			if(p.contains(x, y)) return p.getPoly();
		}
		return null;
	}
}