package ca.gkelly.engine.tilemaps;

import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ca.gkelly.engine.collision.Collider;
import ca.gkelly.engine.collision.RectCollider;
import ca.gkelly.engine.util.Logger;
import ca.gkelly.engine.util.Vertex;

public class MapObject {
	HashMap<String, String> properties = new HashMap<>();
	public String name;
	public String type;
	Collider collider;
	public Vertex position;
	boolean isPoint = false;

	public MapObject(Element root) {
		position = new Vertex(Double.parseDouble(root.getAttribute("x")), Double.parseDouble(root.getAttribute("y")));
		name = root.getAttribute("name");
		type = root.getAttribute("type");

		// If it's a rectangle (or a circle), then it will have width/height properties
		// TODO: Add support for circle colliders at some point
		if (root.hasAttribute("width") && root.hasAttribute("height")) {
			collider = new RectCollider(position.x, position.y, Double.parseDouble(root.getAttribute("width")),
					Double.parseDouble(root.getAttribute("height")));
		}

		NodeList n = root.getChildNodes();
		for (int i = 0; i < n.getLength(); i++) {
			if(n.item(i).getNodeType() != Node.ELEMENT_NODE)
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
				NodeList props = root.getChildNodes();
				for (int j = 0; j < props.getLength(); j++) {
					if(props.item(j).getNodeType() != Node.ELEMENT_NODE)
						continue;
					Element p = (Element) props.item(j);
					properties.put(p.getAttribute("name"), p.getAttribute("value"));
				}
			}
		}

	}

	public HashMap<String, String> getProperties() {
		return properties;
	}

	public String getProperty(String key) {
		return properties.get(key);
	}

	public void putProperty(String key, String value) {
		properties.put(key, value);
	}

	public void editProperty(String key, String value) {
		properties.remove(key);
		properties.put(key, value);
	}

	public void removeProperty(String key) {
		properties.remove(key);
	}

}
