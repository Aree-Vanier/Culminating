package ca.gkelly.engine.tilemaps;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.w3c.dom.Element;

import ca.gkelly.engine.util.Logger;

/** A class used to handle loading and management of a collider layer */
class Tileset {

	/** Flag used to indicate that tile is flipped horizontally */
	@SuppressWarnings("unused")
	private static final long FLIPX = 0x800000000l;
	/** Flag used to indicate that tile is flipped vertically */
	@SuppressWarnings("unused")
	private static final long FLIPY = 0x400000000l;
	/** Flag used to indicate that tile is flipped diagonally */
	@SuppressWarnings("unused")
	private static final long FLIP_DIAG = 0x200000000l;

	/** Flag used to indicate that tile is rotated 90 degrees */
	private static final long ROT_90 = 0xA0000000l;
	/** Flag used to indicate that tile is rotated 180 degrees */
	private static final long ROT_180 = 0xC0000000l;
	/** Flag used to indicate that tile is rotated 270 degrees */
	private static final long ROT_270 = 0x60000000l;
	/** Mask used to remove flags from tile IDs */
	private static final long MASK = 0b00111111;

	/** Width of a tile */
	int tWidth;
	/** Height of a tile */
	int tHeight;
	/** Number of tiles in the set */
	private int tCount;
	/** Number of columns in the source */
	private int columns;
	/** The ID of tile[0] */
	private int offsetID;

	public BufferedImage[] tiles;

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
		offsetID = Integer.parseInt(e.getAttribute("firstgid"));

		String subPath = ((Element) e.getChildNodes().item(1)).getAttribute("source");
		Logger.log(subPath);

		tiles = new BufferedImage[tCount];
		path = path.replace("\\", "/");
		Logger.log(path.substring(0, path.lastIndexOf("/")) + "/" + subPath);
		BufferedImage sheet = ImageIO.read(new File(path.substring(0, path.lastIndexOf("/")) + "/" + subPath));

		// Load tiles from image
		for (int y = 0; y < sheet.getHeight() / tHeight; y++) {
			for (int x = 0; x < columns; x++) {
				Logger.log(Logger.DEBUG, x * tWidth + "\t" + y * tHeight + "\t" + (x + (columns * y)));
				tiles[x + (columns * y)] = sheet.getSubimage(x * tWidth, y * tHeight, tWidth, tHeight);
			}
		}

	}

	/**
	 * Get the image from map data
	 * 
	 * @param mapData The map data for the tile
	 * @return The image associated with the selected tile
	 */
	public BufferedImage getTile(long mapData) {
		// Remove the headers to get the raw ID
		BufferedImage tile = getImage((int) (mapData & MASK));
		// If the tile is null, don't bother
		if (tile == null) {
			return tile;
		}

		// Bitshift to ensure the flags are in the correct bits
		mapData = mapData | mapData << 4 | mapData << 8;

//		Logger.log("Data:\t" + mapData + " " + (Long.toBinaryString(mapData)) + "\tMASK: "
//				+ (Long.toBinaryString(mapData & (MASK))));

		// Create the affine tarnsform
		AffineTransform tx = new AffineTransform();
		// Find correct flag, and apply rotation
		if ((mapData & ROT_180) == ROT_180) {
			System.out.println("180");
			tx.rotate(Math.PI * 1, tile.getWidth() / 2, tile.getHeight() / 2);
		} else if ((mapData & ROT_90) == ROT_90) {
			System.out.println("90");
			tx.rotate(Math.PI * 0.5, tile.getWidth() / 2, tile.getHeight() / 2);
		} else if ((mapData & ROT_270) == ROT_270) {
			System.out.println("270");
			tx.rotate(Math.PI * 1.5, tile.getWidth() / 2, tile.getHeight() / 2);
		} else { // If the tile is not rotated, don't bother with the transformation
			return tile;
		}
		// Apply the transformation
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

		return op.filter(tile, null);
	}

	/**
	 * Get the image associate with a specific tile ID
	 * 
	 * @param ID The ID of the tile
	 * @return The image associated with the selected tile
	 */
	public BufferedImage getImage(int ID) {
//		Logger.log(ID - offsetID);
//		Logger.log(offsetID);
		if (ID - offsetID < tiles.length && ID - offsetID >= 0)
			return tiles[ID - offsetID];
		return null;
	}

}