package ca.gkelly.culminating.graphics;

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
	
	public Document doc;
	
	public Tileset t;

	
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
		
		Element tileElement = (Element) doc.getElementsByTagName("tileset").item(0);
		
		try {
			t = new Tileset(tileElement, src);
		} catch (IOException e) {
			e.printStackTrace();
		}
		

		System.out.println("------\n");
		return true;
	}
	
	public void render(int x, int y, int distance) {
		
	}
	
	public BufferedImage getTile(int ID) {
		return(t.getImage(ID));
	}
	
}

class Tileset {
	
	private int tWidth;
	private int tHeight;
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
	
	BufferedImage getImage(String ID) {
		return null;
	}
	
	BufferedImage getImage(int ID) {
		return tiles[ID];
	}
	
	
}
