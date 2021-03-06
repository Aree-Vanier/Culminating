package ca.gkelly.engine.ui.structs;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import ca.gkelly.engine.ui.UIElement;

/** Used to manage and render borders on {@link UIElement}s */
public class UIBorder {

	/** The width of the border */
	public int width;
	/** The colour of the border */
	public Color colour;

	/**
	 * Create the border
	 * 
	 * @param w The width of the border
	 * @param c The colour of the border
	 */
	public UIBorder(int w, Color c) {
		width = w;
		colour = c;
	}

	/** Create a a 0-width (invisible) border */
	public UIBorder() {
		this(0, Color.WHITE);
	}

	/**
	 * Render the border
	 * 
	 * @param g      The graphics to draw to
	 * @param x      The x position
	 * @param y      The y position
	 * @param width  The width of the contained element
	 * @param height The height of the contained element
	 */
	public void render(Graphics2D g, int x, int y, int width, int height) {
		g.setColor(colour);
		g.setStroke(new BasicStroke(this.width));
		g.drawRect(x, y, width, height);
	}
}
