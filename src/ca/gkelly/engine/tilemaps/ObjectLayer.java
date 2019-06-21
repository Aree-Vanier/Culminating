package ca.gkelly.engine.tilemaps;

import java.awt.Color;
import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import ca.gkelly.engine.collision.Collider;
import ca.gkelly.engine.graphics.Camera;

/** A class used to handle the colliders on a map */
public class ObjectLayer {
	public MapObject[] objects;
	public String name;

	/**
	 * Create the collider layer
	 * 
	 * @param polys The list of polygons
	 * @param name  The name to used for identification
	 */
	public ObjectLayer(NodeList children, String name) {
		this.name = name;
		objects = new MapObject[children.getLength()];
		for (int i = 0; i < objects.length; i++) {
			Element e = (Element) children.item(i);

			objects[i] = new MapObject(e);
		}
	}

	/**
	 * Draw the objects to the screen
	 * 
	 * @param cam The camera to use
	 * @param c   The colour to use
	 */
	public void render(Camera cam, Color c) {
		for (MapObject o : objects) {
			if (o.isPoint)
				cam.drawPoint(o.position, 5, c);
			else
				cam.drawPoly(o.collider.getPoly(), c);
		}
	}

	/**
	 * Get the collider that contains the point
	 * 
	 * @param x The x value of the point
	 * @param y The y value of the point
	 * @return The {@link Collider} that contains the point<br/>
	 *         <strong>null</strong> if no {@link Collider} contains the point
	 */
	public Collider getCollider(double x, double y) {
		for (MapObject o : objects) {
			if (o.collider != null && o.collider.contains(x, y))
				return o.collider;
		}
		return null;
	}

	/**
	 * Get all the colliders from the layer
	 * 
	 * @return An array containing the colliders
	 */
	public Collider[] getColliders() {
		ArrayList<Collider> c = new ArrayList<>();
		for (MapObject o : objects) {
			if (o.collider != null)
				c.add(o.collider);
		}
		return c.toArray(new Collider[c.size()]);
	}
}