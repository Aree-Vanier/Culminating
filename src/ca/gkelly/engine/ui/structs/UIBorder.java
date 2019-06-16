package ca.gkelly.engine.ui.structs;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class UIBorder {
	public static UIBorder NONE = new UIBorder(0, Color.WHITE);
	
	public int width;
	public Color colour;
	
	public UIBorder (int w, Color c) {
		width = w;
		colour = c;
	}
	
	public void render(Graphics2D g, int x, int y, int width, int height) {
		g.setColor(colour);
		g.setStroke(new BasicStroke(this.width));
		g.drawRect(x, y, width, height);
	}
}
