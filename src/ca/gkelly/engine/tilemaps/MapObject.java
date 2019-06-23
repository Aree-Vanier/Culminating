package ca.gkelly.engine.tilemaps;

import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ca.gkelly.engine.collision.Collider;
import ca.gkelly.engine.collision.RectCollider;
import ca.gkelly.engine.util.Logger;
import ca.gkelly.engine.util.Vertex;

/** Used to represent {@link TileMap} objects */
public class MapObject {
	/** The object properties */
	HashMap<String, String> properties = new HashMap<>();
	/** The name property of the object */
	public String name;
	/** The type property of the object */
	public String type;
	/** The collider that represents the object */
	Collider collider;
	/** The position of the object */
	public Vertex position;
	/** Flag to indicate if the object is a point, as opposed to shape */
	boolean isPoint = false;

	/**
	 * Create the object
	 * 
	 * @param root The XML containing the information about the object
	 */
	public MapObject(Element root) {
		position = new Vertex(Double.parseDouble(root.getAttribute("x")), Double.parseDouble(root.getAttribute("y")));
		name = root.getAttribute("name").toLowerCase();
		type = root.getAttribute("type").toLowerCase();

		
		// If it's a rectangle (or a circle), then it will have width/height properties
		// TODO: Add support for circle colliders at some point
		if (root.hasAttribute("width") && root.hasAttribute("height")) {
			collider = new RectCollider(position.x, position.y, Double.parseDouble(root.getAttribute("width")),
					Double.parseDouble(root.getAttribute("height")));
		}

		NodeList n = root.getChildNodes();
		for (int i = 0; i < n.getLength(); i++) {
			if (n.item(i).getNodeType() != Node.ELEMENT_NODE)
				continue;
			Element e = (Element) n.item(i);
			// Handle collider creation
			if (e.getTagName().equals("polygon")) {
				// Get position
				int x = Integer.parseInt(root.getAttribute("x"));
				int y = Integer.parseInt(root.getAttribute("y"));

				String[] points = e.getAttribute("points").split(" ");
				// Create int[]s for x and y points
				Vertex[] vertices = new Vertex[points.length];
				for (int s = 0; s < points.length; s++) {
					vertices[s] = new Vertex((Integer.parseInt(points[s].split(",")[0])) + x,
							(Integer.parseInt(points[s].split(",")[1])) + y);
				}
				// Create the corresponding polygon
				collider = new Collider(vertices);
			}
			// If it's a point, it will contain a "point" tag
			if (e.getTagName().equals("point"))
				isPoint = true;

			// Get the custom properties
			if (e.getTagName().equals("properties")) {
				NodeList props = e.getChildNodes();
				for (int j = 0; j < props.getLength(); j++) {
					if (props.item(j).getNodeType() != Node.ELEMENT_NODE)
						continue;
					Element p = (Element) props.item(j);
					properties.put(p.getAttribute("name").toLowerCase(), p.getAttribute("value").toLowerCase());
				}
			}
		}

		// If the trigger flag is set, make it a trigger
		if (isProperty("trigger", "true") && collider != null) {
			collider.isTrigger = true;
			Logger.log("Trigger");
		}
	}

	/**Get the object's collider
	 * @return The collider for shapes, <code>null</code> for points*/
	public Collider getCollider() {
		return collider;
	}
	
	/**
	 * Get the properties
	 * 
	 * @return The HashMap containing the properties
	 */
	public HashMap<String, String> getProperties() {
		return properties;
	}

	/**
	 * Get specified property
	 * 
	 * @param key The name of the property
	 */
	public String getProperty(String key) {
		return properties.get(key.toLowerCase());
	}

	/**
	 * Check against property value
	 * 
	 * @param key   The name of the property
	 * @param value The value to check for
	 * @return true if the property value is equal to passed value
	 */
	public boolean isProperty(String key, String value) {
		if (hasProperty(key))
			return getProperty(key.toLowerCase()).equals(value.toLowerCase());
		return false;
	}

	/**
	 * Check if specified property exists
	 * 
	 * @param key The name of the property
	 */
	public boolean hasProperty(String key) {
		return properties.containsKey(key.toLowerCase());
	}

	/**
	 * Add a property
	 * 
	 * @param key   The name of the property
	 * @param value The property value
	 */
	public void addProperty(String key, String value) {
		properties.put(key.toLowerCase(), value.toLowerCase());
	}

	/**
	 * Edit an existing property
	 * 
	 * @param key   The name of the property
	 * @param value The new value
	 */
	public void editProperty(String key, String value) {
		properties.remove(key.toLowerCase());
		properties.put(key.toLowerCase(), value.toLowerCase());
	}

	/**
	 * Remove specified property
	 * 
	 * @param key The name of the property
	 */
	public void removeProperty(String key) {
		properties.remove(key.toLowerCase());
	}

}
