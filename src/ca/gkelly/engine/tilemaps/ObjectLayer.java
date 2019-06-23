package ca.gkelly.engine.tilemaps;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import ca.gkelly.engine.collision.Collider;
import ca.gkelly.engine.graphics.Camera;

/** A class used to handle the {@link MapObjects} on a {@link TileMap} */
public class ObjectLayer {
	/** A list of all {@link MapObjects} on this layer */
	public MapObject[] objects;
	/**
	 * A list of {@link MapObject}s, sorted by type<br/>
	 * Used to optimise {@link #findByType(String) findByType()}
	 */
	private HashMap<String, ArrayList<MapObject>> types = new HashMap<>();
	/** The name of the layer */
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

			MapObject o = new MapObject(e);
			objects[i] = o;

			// Populate the types map
			if (types.containsKey(o.type)) {
				types.get(o.type).add(o);
			} else {
				types.put(o.type, new ArrayList<MapObject>());
				types.get(o.type).add(o);
			}
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

	/**
	 * Find a {@link MapObject} using it's name
	 * 
	 * @param name The name of the object
	 * @return The first object with the specified name
	 */
	public MapObject findByName(String name) {
		for (MapObject o : objects)
			if (o.name.equals(name))
				return o;

		return null;
	}

	/**
	 * Find {@link MapObject}s by type
	 * 
	 * @param name The tag to search for
	 * @return All objects with the specified type
	 */
	public MapObject[] findByType(String type) {
		if (types.containsKey(type))
			return types.get(type).toArray(new MapObject[types.get(type).size()]);
		return null;
	}
}