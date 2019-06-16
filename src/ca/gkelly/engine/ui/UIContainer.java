package ca.gkelly.engine.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import ca.gkelly.engine.ui.structs.UIDimens;
import ca.gkelly.engine.ui.structs.UIPosition;

public class UIContainer extends UIElement {

	ArrayList<UIElement> children = new ArrayList<>();

	public UIContainer(UIPosition p, UIDimens pad, Color bgColour) {
		super(p, pad, bgColour);
	}

	public void addChild(UIElement e) {
		children.add(e);
	}

	public void removeChild(UIElement e) {
		if (children.contains(e))
			children.remove(e);
	}

	@Override
	public void render(Graphics g, UIContainer c) {
		dimens.setWidth(c.dimens.getWidth());
		dimens.setHeight(c.dimens.getHeight());
		g.setColor(bgColour);
		g.fillRect(pos.x, pos.y, dimens.getWidth(), dimens.getHeight());
		for (UIElement e : children) {
			e.render(g, this);
		}
	}

}
