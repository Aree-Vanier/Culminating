package ca.gkelly.culminating;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ca.gkelly.culminating.loader.Loader;

public class Main extends JFrame{

	public static JPanel panel;
	final String NAME = "FLEET";
	
	
	public Main() {
		
		setName(NAME);
		setSize(1280,720);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel = new JPanel();
		setContentPane(panel);
		
		setVisible(true);
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
