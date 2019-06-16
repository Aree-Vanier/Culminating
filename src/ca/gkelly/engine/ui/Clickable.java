package ca.gkelly.engine.ui;

import ca.gkelly.engine.util.Vertex;

/** Interface to add mouse interaction functions to {@link UIElement}s */
public interface Clickable {
	/**
	 * Determine if the mouse if over the element
	 * 
	 * @param x The x position
	 * @param y the y position
	 * @return true if the element contains the mouse
	 */
	public boolean isMouseOver(double x, double y);

	/**
	 * Determine if the mouse if over the element
	 * 
	 * @param v The {@link Vertex} position of the mouse
	 * @return true if the element contains the mouse
	 */
	public default boolean isMouseOver(Vertex v) {
		return isMouseOver(v.x, v.y);
	}

	/** Called when the mouse is over the element */
	public void onHover();

	/** Called when the mouse is not over the element */
	public void onExit();
}
