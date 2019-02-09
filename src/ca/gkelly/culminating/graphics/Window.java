package ca.gkelly.culminating.graphics;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window extends JFrame{
	
	public JPanel panel;
	
	public Window(String name, int width, int height) {
		
		setName(name);
		setSize(width, height);
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
}
