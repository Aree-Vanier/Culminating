package ca.gkelly.culminating;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ca.gkelly.culminating.entities.Ship;
import ca.gkelly.culminating.graphics.Camera;
import ca.gkelly.culminating.loader.Loader;
import ca.gkelly.culminating.loader.MountSource;

public class Main extends JPanel{

	public static JFrame window;
	final String NAME = "FLEET";
	Ship s;
	Camera c;
	
	public Main() {
		window = new JFrame();
		window.setName(NAME);
		window.setSize(1280,720);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		window.setContentPane(this);
		
		window.setVisible(true);
		
		MountSource m = Loader.mounts.get(0);
		
		MountSource [] mounts = {m,m,m};
		
		s=Loader.vessels.get(0).build(mounts);
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
	}
	
	public static void main(String[] args) {
		Loader.directory = args[0];
		
		System.out.println(Loader.directory);
		
		Loader.load();
		
		System.out.println(Loader.vessels.get(0).name);
		
		new Main();
		
	}
	
}
