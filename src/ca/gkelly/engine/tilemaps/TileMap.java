package ca.gkelly.engine.tilemaps;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ca.gkelly.engine.collision.Collider;
import ca.gkelly.engine.util.Logger;
import ca.gkelly.engine.util.Tools;
import ca.gkelly.engine.util.Vertex;

/**
 * Class used to load and render a .tmx tilemap
 * 
 * @see <a href=
 *      "https://doc.mapeditor.org/en/stable/reference/tmx-map-format/">https://doc.mapeditor.org/en/stable/reference/tmx-map-format/</a>
 */
public class TileMap {

	BufferedImage[] tiles;
	String src;

	long[][] map;

	public Document doc;

	public Tileset[] tilesets;

	/** Complete render of the map, rendered on load then saved */
	BufferedImage image;
	/**
	 * Chunk removed from <code>image</code> used for rendering
	 */
	BufferedImage cameraRender;

	/** Last Top-Left position of camera, used to check need for re-render */
	Vertex lastTL = new Vertex(-1, -1);
	/** Last Bottom-Right position of camera, used to check need for re-render */
	Vertex lastBR = new Vertex(-1, -1);

	/** Polygon collider layers used on map */
	HashMap<String, ObjectLayer> layers = new HashMap<>();

	/**
	 * Prepare a new tiled map
	 * 
	 * @param src The path to the <code>.tmx</code> file
	 */
	public TileMap(String src) {
		this.src = src;
	}

	/**
	 * Load the tiled map, and all associate resources, into memory This can be used
	 * to create many maps on startup, but only load them as needed
	 */
	public boolean load() {
		File file = new File(src);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;

		// Load the file, if it fails, return false
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(file);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return false;
		} catch (SAXException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		// Prepare document
		doc.getDocumentElement().normalize();

		// Get important elements from map
		NodeList tilesetElements = doc.getElementsByTagName("tileset");
		Element mapData = (Element) doc.getElementsByTagName("data").item(0);

		// Prepare the string that contains the map details
		String mapString = mapData.getTextContent().replaceFirst("\n", "");
		mapString = mapString.substring(0, mapString.length() - 1);
		String[] rows = mapString.split(",\n");
		Logger.log(Logger.DEBUG, mapString);

		map = new long[rows[0].split(",").length][rows.length];

		Logger.newLine(Logger.DEBUG);

		// Get integers values for each tile
		for (int y = 0; y < rows.length; y++) {
			String[] tiles = rows[y].split(",");
			for (int x = 0; x < tiles.length; x++) {
				map[x][y] = Long.parseLong(tiles[x]);
				Logger.log(Logger.DEBUG, map[x][y], ",", true);
			}
			Logger.newLine(Logger.DEBUG);
		}

		tilesets = new Tileset[tilesetElements.getLength()];

		for (int i = 0; i < tilesets.length; i++) {
			// Load the tileset
			try {
				tilesets[i] = new Tileset((Element) tilesetElements.item(i), src);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Logger.newLine(Logger.DEBUG);
		Logger.newLine(Logger.DEBUG);

		// Create full render image
		image = new BufferedImage(map.length * tilesets[0].tWidth, map[0].length * tilesets[0].tHeight,
				BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = image.getGraphics();
		BufferedImage tile;
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[0].length; y++) {
				tile = null;
				int i = -1;
				while (tile == null && (i++) < tilesets.length) {
					tile = tilesets[i].getTile(map[x][y]);
				}
				try {
					g.drawImage(tile, x * tilesets[i].tWidth, y * tilesets[i].tHeight, null);
				} catch (IndexOutOfBoundsException e) {

				}
			}
		}
//		System.exit(0);

		// Get collider elements
		NodeList l = (NodeList) doc.getElementsByTagName("objectgroup");

		Logger.log(Logger.DEBUG, l.getLength());

		for (int i = 0; i < l.getLength(); i++) {
			Element e = (Element) l.item(i);
			NodeList polyNodes = (NodeList) e.getElementsByTagName("object");
			layers.put(e.getAttribute("name"), new ObjectLayer(polyNodes, e.getAttribute("name")));
		}

		return true;
	}

	/**
	 * Get a cropped version of the map
	 * 
	 * @param tl Top-left {@link Vertex}
	 * @param br Bottom-right {@link Vertex}
	 * 
	 * @return Array containing:<br/>
	 *         - <strong>[0]:</strong> {@link BufferedImage} Cropped Image<br/>
	 *         - <strong>[1]:</strong> {@link int} X offset<br/>
	 *         - <strong>[2]:</strong> {@link int} Y offset
	 */
	public Object[] render(Vertex tl, Vertex br) {

		int margin = 10;
		tl.x = Tools.minmax(tl.x, margin, image.getWidth() - margin);
		br.x = Tools.minmax(br.x, margin, image.getWidth() - margin);
		tl.y = Tools.minmax(tl.y, margin, image.getHeight() - margin);
		br.y = Tools.minmax(br.y, margin, image.getHeight() - margin);

		// If the values have changed, re-crop the image
		// Otherwise just use existing
		if (!(lastTL.equals(tl) && lastBR.equals(br))) {
			lastTL = tl;
			lastBR = br;
			cameraRender = image.getSubimage((int) (tl.x - margin), (int) (tl.y - margin),
					(int) (br.x - tl.x + 2 * margin), (int) (br.y - tl.y + 2 * margin));
		}

		return (new Object[] { cameraRender, tl.x - margin, tl.y - margin });
	}

	/**
	 * Get the image associate with a specific tile ID
	 * 
	 * @param ID The ID of the tile
	 * @return The image associated with the selected tile
	 */
	public BufferedImage getTile(int ID) {
		BufferedImage tile = null;
		int i = -1;

		while (tile == null && (i++) > tilesets.length)
			tile = tilesets[i].getImage(ID);
		return (tile);
	}

	/**
	 * Get the {@link Collider} that contains the point, from the selected {@link ObjectLayer}
	 * 
	 * @param x     The x value of the point
	 * @param y     The y value of the point
	 * @param layer The layer to search
	 * @return The collider that contains the point<br/>
	 *         <strong>null</strong> if no collider contains the point<br/>
	 *         <strong>null</strong> if the layer doesn't exist
	 */
	public Collider getCollider(double x, double y, String layer) {
		ObjectLayer l;
		if ((l = layers.get(layer)) != null)
			return (l.getCollider(x, y));
		return null;
	}

	/**
	 * Get the polygon that contains the {@link Vertex}, from the selected {@link ObjectLayer}
	 * 
	 * @param v     The {@link Vertex} to check
	 * @param layer The layer to search
	 * @return The {@link Collider} that contains the point<br/>
	 *         <strong>null</strong> if no collider contains the point<br/>
	 *         <strong>null</strong> if the layer doesn't exist
	 */
	public Collider getCollider(Vertex v, String layer) {
		return getCollider(v.x, v.y, layer);
	}

	/**Get the colliders from the selected {@link ObjectLayer}
	 * @oaram layer The name of the layer*/
	public Collider[] getColliders(String layer) {
		ObjectLayer l;
		if ((l = layers.get(layer)) != null)
			return l.getColliders();
		return null;
	}

	/**Get the selected object {@link ObjectLayer}
	 * @param layer The name of the layer*/
	public ObjectLayer getLayer(String layer) {
		return layers.get(layer);
	}
}
