package ca.gkelly.culminating;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ca.gkelly.culminating.loader.Loader;

public class Main extends JPanel{

	public static JFrame window;
	final String NAME = "FLEET";
	
	
	public Main() {
		window = new JFrame();
		window.setName(NAME);
		window.setSize(1280,720);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		window.setContentPane(this);
		
		window.setVisible(true);
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.RED);
		g.fillRect(5,5,100,100);
	}
	
	public static void main(String[] args) {
		Loader.directory = args[0];
		
		System.out.println(Loader.directory);
		
		Loader.load();
		
		System.out.println(Loader.vessels.get(0).name);
		
		new Main();
		
	}
	
}
