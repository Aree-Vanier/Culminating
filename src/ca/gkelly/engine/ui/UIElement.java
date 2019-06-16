package ca.gkelly.engine.ui;

import java.awt.Color;
import java.awt.Graphics;

import ca.gkelly.engine.collision.RectCollider;
import ca.gkelly.engine.ui.structs.UIDimens;
import ca.gkelly.engine.ui.structs.UIPosition;

public abstract class UIElement {
	public UIPosition pos;
	public UIDimens dimens;
	Color bgColour;
	
	public UIElement(UIPosition p, UIDimens dimens, Color bgColour) {
		pos = p;
		this.bgColour = bgColour;
		this.dimens = dimens;
	}
	
	public void setBackground(Color c) {
		bgColour = c;
	}
	
	public abstract void render(Graphics g, UIContainer c);
}
