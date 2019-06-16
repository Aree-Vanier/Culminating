package ca.gkelly.engine.ui;

import java.awt.Color;
import java.awt.Graphics;

import ca.gkelly.engine.ui.structs.UIDimens;
import ca.gkelly.engine.ui.structs.UIPosition;

public class UILayer extends UIContainer{

	public UILayer(int x, int y, UIDimens dimens, Color bgColour) {
		super(new UIPosition(x, y), dimens, bgColour);
	}
	
	public void render(Graphics g) {
		g.setColor(bgColour);
		g.fillRect(pos.x, pos.y, dimens.getWidth(), dimens.getHeight());
		for(UIElement e : children) {
			e.render(g, this);
		}
	}

}
