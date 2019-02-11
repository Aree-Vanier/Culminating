package ca.gkelly.culminating;

import java.awt.Graphics;

import javax.swing.JFrame;

import ca.gkelly.culminating.graphics.TiledMap;

//This is a class used for quick console tests that don't need the graphics
public class Test extends JFrame{
	TiledMap m;
	
	public Test(String[] args) {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(640, 480);
		setVisible(true);
		m = new TiledMap(args[0]+"\\maps\\test.tmx");
		System.out.println(m.load());
		
		System.out.println(m.doc.getDocumentElement().getNodeName());
		
	}
	
	public static void main(String[] args) {
		System.out.println("TESTING");
		new Test(args);
		
	}
	
	@Override
	public void paint(Graphics g) {
		if(m==null)return;
		if(m.tileset == null) return;
		g.drawImage(m.getTile(1), 10,10,null);
	}

}
