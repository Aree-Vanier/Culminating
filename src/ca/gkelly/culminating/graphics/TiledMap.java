package ca.gkelly.culminating.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TiledMap {

	BufferedImage[] tiles;
	String src;

	int[][] map;

	public Document doc;

	public Tileset tileset;

	/** Complete render of the map, rendered on load then saved */
	BufferedImage image;
	/** Chunk removed from <code>image</code> used for rendering */
	BufferedImage cameraRender;

	/** Last X position of camera, used to check need for re-render */
	int lastX;
	/** Last Y position of camera, used to check need for re-render */
	int lastY;
	/** Last zoom level of camera, used to check need for re-render */
	double lastZoom;

	/** Polygon colliders used on map, each inner list is a separate layer */
	Polygon[][] colliders;

	/**
	 * Prepare a new tiled map
	 * 
	 * @param src The path to the <code>.tmx</code> file
	 */
	public TiledMap(String src) {
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
		Element tilesetElement = (Element) doc.getElementsByTagName("tileset").item(0);
		Element mapData = (Element) doc.getElementsByTagName("data").item(0);

		// Prepare the string that contains the map details
		String mapString = mapData.getTextContent().replaceFirst("\n", "");
		mapString = mapString.substring(0, mapString.length() - 1);
		String[] rows = mapString.split(",\n");
		System.out.println(mapString);

		map = new int[rows[0].split(",").length][rows.length];

		// Get integers values for each tile
		for (int y = 0; y < rows.length; y++) {
			String[] tiles = rows[y].split(",");
			for (int x = 0; x < tiles.length; x++) {
				System.out.print(tiles[x]);
				map[x][y] = Integer.parseInt(tiles[x]);
			}
			System.out.println();
		}

		// Load the tileset
		try {
			tileset = new Tileset(tilesetElement, src);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("------\n");

		// Create full render image
		image = new BufferedImage(map.length * tileset.tWidth, map[0].length * tileset.tHeight,
				BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = image.getGraphics();
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[0].length; y++) {
				try {
					g.drawImage(tileset.getImage(map[x][y]), x * tileset.tWidth, y * tileset.tHeight, null);
				} catch (IndexOutOfBoundsException e) {

				}
			}
		}

		// Get collider elements
		NodeList colliderLayers = (NodeList) doc.getElementsByTagName("objectgroup");
		this.colliders = new Polygon[colliderLayers.getLength()][];

		System.out.println(colliderLayers.getLength());
		
		for (int i = 0; i < colliderLayers.getLength(); i++) {
			Element e = (Element) colliderLayers.item(i);
			NodeList polyNodes = (NodeList) e.getElementsByTagName("object");
			Polygon[] polyLayer = new Polygon[polyNodes.getLength()];
			System.out.println(polyNodes.getLength());
			for (int j = 0; j < polyNodes.getLength(); j++) {
				Element poly = (Element) polyNodes.item(j);
				int x = Integer.parseInt(poly.getAttribute("x"));
				int y = Integer.parseInt(poly.getAttribute("y"));
				
				poly = (Element) poly.getElementsByTagName("polygon").item(0);
				String[] points = poly.getAttribute("points").split(" ");
				// Create int[]s for x and y points
				int[] xPoints = new int[points.length];
				int[] yPoints = new int[points.length];
				for (int s = 0; s < points.length; s++) {
					xPoints[s] = (Integer.parseInt(points[s].split(",")[0]))+x;
					yPoints[s] = (Integer.parseInt(points[s].split(",")[1]))+y;
				}
				// Create the corresponding polygon
				polyLayer[j] = new Polygon(xPoints, yPoints, xPoints.length);
			}
			colliders[i] = polyLayer;
		}

		return true;
	}

	/**
	 * Get a buffered image of the map to display
	 * 
	 * @param cam The camera to be rendered to
	 */
	public void render(Camera cam) {
		// If the camera has moved, re-render
		if (cam.x != lastX || cam.y != lastY || cam.zoom != lastZoom)
			reRender(cam.x, cam.y, cam.getWidth(), cam.getHeight());

		lastX = cam.x;
		lastY = cam.y;
		lastZoom = cam.zoom;

		cam.render(image, 0, 0);
		
		for(Polygon[] layer : colliders) {
			for(Polygon collider:layer) {
				cam.drawPoly(collider, Color.RED);
			}
		}
	}

	/**
	 * Get a new render of the map to display
	 * 
	 * @param centreX X value at centre of screen
	 * @param centreY Y value at centre of screen
	 * @param width   Render width
	 * @param height  Render height
	 */
	public void reRender(int centreX, int centreY, int width, int height) {
		// Reinstantiate the image
		cameraRender = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = cameraRender.getGraphics();

		int x = centreX - width / 2;
		int y = centreY - height / 2;
		int offsetX = 0;
		int offsetY = 0;

		if (x < 0) {
			offsetX = Math.abs(x);
			x = 0;
		}
		if (y < 0) {
			offsetY = Math.abs(y);
			y = 0;
		}
		if (width > image.getWidth()) {
			width = image.getWidth();
		}
		if (height > image.getHeight()) {
			height = image.getHeight();
		}

		g.drawImage(image.getSubimage(x, y, width, height), offsetX, offsetY, null);
	}

	/**
	 * Get the image associate with a specific tile ID
	 * 
	 * @param ID The ID of the tile
	 * @return The image associated with the selected tile
	 */
	public BufferedImage getTile(int ID) {
		return (tileset.getImage(ID));
	}

}

/** A class used to handle loading and management of a tileset */
class Tileset {
	/** Width of a tile */
	int tWidth;
	/** Height of a tile */
	int tHeight;
	/** Number of tiles in the set */
	private int tCount;
	/** Number of columns in the source */
	private int columns;

	private BufferedImage[] tiles;

	/**
	 * Load a tileset
	 * 
	 * @param e    The tileset xml element
	 * @param path The path to the tileset image
	 */
	Tileset(Element e, String path) throws IOException {
		// Get attributes from xml
		tWidth = Integer.parseInt(e.getAttribute("tilewidth"));
		tHeight = Integer.parseInt(e.getAttribute("tileheight"));
		tCount = Integer.parseInt(e.getAttribute("tilecount"));
		columns = Integer.parseInt(e.getAttribute("columns"));
//		String subPath = ((Element) e.getFirstChild()).getAttribute("source");
		String subPath = "tileset.png"; // TODO: Why?

		tiles = new BufferedImage[tCount];
		BufferedImage sheet = ImageIO.read(new File(path.substring(0, path.lastIndexOf("\\")) + "\\" + subPath));

		// Load tiles from image
		for (int y = 0; y < sheet.getHeight() / tHeight; y++) {
			for (int x = 0; x < columns; x++) {
				System.out.println(x * tWidth + "\t" + y * tHeight + "\t" + (x + (columns * y)));
				tiles[x + (columns * y)] = sheet.getSubimage(x * tWidth, y * tHeight, tWidth, tHeight);
			}
		}

	}

	/**
	 * Get the image associate with a specific tile ID
	 * 
	 * @param ID The ID of the tile
	 * @return The image associated with the selected tile
	 */
	BufferedImage getImage(int ID) {
		return tiles[ID - 1];
	}
}
