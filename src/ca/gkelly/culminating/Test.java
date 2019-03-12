package ca.gkelly.culminating;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;

import ca.gkelly.culminating.entities.Mount;
import ca.gkelly.culminating.entities.Ship;
import ca.gkelly.culminating.entities.Weapon;
import ca.gkelly.culminating.graphics.Camera;
import ca.gkelly.culminating.graphics.TiledMap;
import ca.gkelly.culminating.loader.Loader;

//This is a class used for quick console tests that don't need the graphics
public class Test extends JFrame implements KeyListener, MouseMotionListener{
	TiledMap m;
	int x = 256;
	int y = 256;
	double zoom = 1;
	Camera cam;
	Ship s;
	Polygon selectedPoly = null;
	
	public Test(String[] args) {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(640, 480);
		setVisible(true);
		m = new TiledMap(args[0]+"\\maps\\test.tmx");
		System.out.println(m.load());
		
		System.out.println(m.doc.getDocumentElement().getNodeName());
		addKeyListener(this);
		addMouseMotionListener(this);
		
		Loader.init(args[0]);
		Loader.load();
		
		cam = new Camera(getContentPane(), m);
		Mount[] m = {new Weapon(Loader.mounts.get(0), 0,0), new Weapon(Loader.mounts.get(0), 20, 10)};
		s = new Ship(Loader.vessels.get(0), 100, 100, m);
	}
	
	public static void main(String[] args) {
		System.out.println("TESTING");
		new Test(args);
	}
	
	@Override
	public void paint(Graphics g) {
		if(m==null)return;
		if(m.tileset == null) return;
		g.setColor(Color.black);
//		g.fillRect(0, 0, getWidth(), getHeight());
		
		cam.begin();
		
		s.render(cam);
		cam.drawRect(0,0,10,10,Color.RED);

		if(selectedPoly!=null) {
			cam.drawPoly(selectedPoly, Color.RED);
		}
		
		cam.finish(g);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_W) y++;
		if(e.getKeyCode() == KeyEvent.VK_S) y--;
		if(e.getKeyCode() == KeyEvent.VK_A) x++;
		if(e.getKeyCode() == KeyEvent.VK_D) x--;
		
		if(e.getKeyCode() == KeyEvent.VK_E) zoom += 0.1;
		if(e.getKeyCode() == KeyEvent.VK_Q) zoom -= 0.1;
		
		cam.setPosition(x, y, zoom);
		
		repaint();
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		selectedPoly = m.getPoly(cam.project(e.getX(), 0)[0], cam.project(0, e.getY())[1], "land");
		System.out.println(cam.project(e.getX(), e.getY())[0]+"\t"+cam.project(e.getX(), e.getY())[1]);
	}

}
