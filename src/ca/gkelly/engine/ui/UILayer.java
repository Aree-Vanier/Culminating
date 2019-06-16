package ca.gkelly.engine.ui;

import java.awt.Color;
import java.awt.Graphics2D;

import ca.gkelly.engine.ui.structs.UIDimensions;
import ca.gkelly.engine.ui.structs.UIPosition;

public class UILayer extends UIContainer{

	public UILayer(int x, int y, UIDimensions dimens, Color bgColour) {
		super(new UIPosition(x, y), dimens, bgColour);
	}
	
	public void render(Graphics2D g) {
		g.setColor(bgColour);
		g.fillRect(pos.x, pos.y, dimens.getWidth(), dimens.getHeight());
		for(UIElement e : children) {
			e.render(g, this);
		}
	}
	
	public void setWidth(int width) {
		setWidth(width);
	}
	
	public void setHeight(int height) {
		setHeight(height);
	}

}
