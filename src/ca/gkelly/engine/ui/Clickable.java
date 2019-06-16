package ca.gkelly.engine.ui;

import ca.gkelly.engine.util.Vertex;

public interface Clickable {
	public boolean isMouseOver(double x, double y);
	public default boolean isMouseOver(Vertex v) {
		return isMouseOver((int) v.x, (int) v.y);
	}
	public void onHover();
	public void onExit();
}
