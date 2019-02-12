package ca.gkelly.culminating.graphics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
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
	
	BufferedImage render;
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
		return true;
	}
	
	public BufferedImage render(int centreX, int centreY, int distance) {
		//Re-render map if the camera has moved TODO:take distance into account to reduce render frequency
		if(!(lastX==centreX && lastY==centreY))
			reRender(centreX, centreY, distance);
		//Update last position
		lastX = centreX;
		lastY = centreY;
		
		return render;
	}
	
	public void reRender(int centreX, int centreY, int distance) {
		render = new BufferedImage(distance*2*tileset.tWidth, distance*2*tileset.tHeight, BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = render.getGraphics();
		for(int x=centreX-distance; x<centreX+distance; x++) {
			for(int y=centreY-distance; y<centreY+distance; y++) {
				try {
					g.drawImage(tileset.getImage(map[x][y]), (x-(centreX-distance))*tileset.tWidth, (y-(centreY-distance))*tileset.tHeight, null);
				} catch(IndexOutOfBoundsException e) {
					continue;
				}
				System.out.print(map[x][y]);
			}
			System.out.println();
		}
		System.out.println("\n-----\n");
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
