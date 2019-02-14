package ca.gkelly.culminating.graphics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class TiledMap {
	
	BufferedImage[] tiles;
	String src;
	
	int [][] map;
	
	public Document doc;
	
	public Tileset tileset;
	
	BufferedImage image;
	BufferedImage cameraRender;
	
	int lastX;
	int lastY;
	
	public TiledMap(String src) {
		this.src = src;
	}
	
	public boolean load() {
		File file = new File(src);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		
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
		
		doc.getDocumentElement().normalize();
		
		Element tilesetElement = (Element) doc.getElementsByTagName("tileset").item(0);
		Element mapData = (Element) doc.getElementsByTagName("data").item(0);
		
		String mapString = mapData.getTextContent().replaceFirst("\n", "");
		mapString = mapString.substring(0, mapString.length()-1);
		String[] rows = mapString.split(",\n");
		System.out.println(mapString);
		
		map = new int[rows[0].split(",").length][rows.length];
		
		for(int y=0; y<rows.length; y++) {
			String[] tiles = rows[y].split(",");
			for(int x=0; x<tiles.length; x++) {
				System.out.print(tiles[x]);
				map[x][y] = Integer.parseInt(tiles[x]);
			}
			System.out.println();
		}
		
		
		
		try {
			tileset = new Tileset(tilesetElement, src);
		} catch (IOException e) {
			e.printStackTrace();
		}
		

		System.out.println("------\n");
		
		image = new BufferedImage(map.length*tileset.tWidth, map[0].length*tileset.tHeight,BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = image.getGraphics();
		for(int x=0; x<map.length; x++) {
			for(int y=0; y<map[0].length; y++) {
				try {
					g.drawImage(tileset.getImage(map[x][y]), x*tileset.tWidth, y*tileset.tHeight, null);
				} catch (IndexOutOfBoundsException e) {
					
				}
			}
			System.out.println();
		}
		return true;
	}
	
	public BufferedImage render(int centreX, int centreY, int width, int height) {
		if(centreX != lastX || centreY != lastY) reRender(centreX, centreY, width, height);
		
		lastX = centreX;
		lastY = centreY;
		
		return cameraRender;
	}
	
	public void reRender(int centreX, int centreY, int width, int height) {
		cameraRender = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = cameraRender.getGraphics();
		
		int x = centreX-width/2;
		int y = centreY-height/2;
		int offsetX = 0;
		int offsetY = 0;
		
		if(x < 0) {
			offsetX = Math.abs(x);
			x = 0;
		}
		if(y < 0) {
			offsetY = Math.abs(y);
			y=0;
		}
		if(width > image.getWidth()) {
			width=image.getWidth();
		}
		if(height > image.getHeight()) {
			height = image.getHeight();
		}
		
		g.drawImage(image.getSubimage(x, y, width, height), offsetX, offsetY, null);
	}
	
	public BufferedImage getTile(int ID) {
		return(tileset.getImage(ID));
	}
	
}

class Tileset {
	
	int tWidth;
	int tHeight;
	private int tCount;
	private int columns;
	
	private BufferedImage[] tiles;
	
	Tileset(Element e, String path) throws IOException {
		tWidth = Integer.parseInt(e.getAttribute("tilewidth"));
		tHeight= Integer.parseInt(e.getAttribute("tileheight"));
		tCount = Integer.parseInt(e.getAttribute("tilecount"));
		columns = Integer.parseInt(e.getAttribute("columns"));
//		String subPath = ((Element) e.getFirstChild()).getAttribute("source");
		String subPath = "tileset.png";
		
		tiles = new BufferedImage[tCount];
		BufferedImage sheet = ImageIO.read(new File(path.substring(0, path.lastIndexOf("\\"))+"\\"+subPath));
		
		for(int y = 0; y<sheet.getHeight()/tHeight; y++) {
			for(int x=0; x<columns; x++) {
				System.out.println(x*tWidth + "\t" + y*tHeight +"\t"+(x+(columns*y)));
				tiles[x+(columns*y)] = sheet.getSubimage(x*tWidth, y*tHeight, tWidth, tHeight);
			}
		}
		
	}
	
	BufferedImage getImage(int ID) {
		return tiles[ID-1];
	}
}
