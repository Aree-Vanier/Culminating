package ca.gkelly.culminating;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import ca.gkelly.culminating.graphics.TiledMap;
import sun.security.krb5.internal.ktab.KeyTabConstants;

//This is a class used for quick console tests that don't need the graphics
public class Test extends JFrame implements KeyListener{
	TiledMap m;
	int x = 256;
	int y = 256;
	
	public Test(String[] args) {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(640, 480);
		setVisible(true);
		m = new TiledMap(args[0]+"\\maps\\test.tmx");
		System.out.println(m.load());
		
		System.out.println(m.doc.getDocumentElement().getNodeName());
		this.addKeyListener(this);
	}
	
	public static void main(String[] args) {
		System.out.println("TESTING");
		new Test(args);
		
	}
	
	@Override
	public void paint(Graphics g) {
		if(m==null)return;
		if(m.tileset == null) return;
		g.drawImage(m.render(x, y, getWidth(), getHeight(), 10), 0, 0, null);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_W) y++;
		if(e.getKeyCode() == KeyEvent.VK_S) y--;
		if(e.getKeyCode() == KeyEvent.VK_A) x--;
		if(e.getKeyCode() == KeyEvent.VK_D) x++;
		
		repaint();
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}

}
