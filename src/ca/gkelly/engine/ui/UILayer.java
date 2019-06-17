package ca.gkelly.engine.ui;

import java.awt.Color;
import java.awt.Graphics2D;

import ca.gkelly.engine.ui.structs.UIDimensions;
import ca.gkelly.engine.ui.structs.UIPosition;

/** The base container for a UI system */
public class UILayer extends UIContainer {

	/**
	 * Create the layer
	 * 
	 * @param x        The x position
	 * @param y        The y position
	 * @param dimens   The dimensions
	 * @param bgColour The background colour
	 */
	public UILayer(int x, int y, UIDimensions dimens, Color bgColour) {
		super(new UIPosition(x, y), dimens, bgColour);
	}

	/**
	 * Render the layer
	 * 
	 * @param g The graphics to draw to
	 */
	public void render(Graphics2D g) {
		g.setColor(bgColour);
		g.fillRect(pos.x, pos.y, dimens.getWidth(), dimens.getHeight());
		// Render children
		for (UIElement e : children) {
			if(e.visible) // Only render if visible
				e.render(g, this);
		}
	}

	/**
	 * Set the width, this will override fixed width
	 * 
	 * @param width The new width
	 */
	public void setWidth(int width) {
		dimens.setFixedWidth(width);
	}

	/**
	 * Set the height, this will override fixed height
	 * 
	 * @param height The new height
	 */
	public void setHeight(int height) {
		dimens.setFixedHeight(height);
	}

}
