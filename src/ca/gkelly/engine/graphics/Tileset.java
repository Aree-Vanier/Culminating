package ca.gkelly.engine.graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.w3c.dom.Element;

import ca.gkelly.engine.util.Logger;

/** A class used to handle loading and management of a collider layer */
public class Tileset {
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
		String subPath = "tiles.png"; // TODO: Why?

		tiles = new BufferedImage[tCount];
		Logger.log(path.substring(0, path.lastIndexOf("\\")) + "\\" + subPath);
		BufferedImage sheet = ImageIO.read(new File(path.substring(0, path.lastIndexOf("\\")) + "\\" + subPath));

		// Load tiles from image
		for(int y = 0;y < sheet.getHeight() / tHeight;y++) {
			for(int x = 0;x < columns;x++) {
				Logger.log(Logger.DEBUG, x * tWidth + "\t" + y * tHeight + "\t" + (x + (columns * y)));
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
	public BufferedImage getImage(int ID) {
		return tiles[ID - 1];
	}
}