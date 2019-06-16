package ca.gkelly.engine.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import ca.gkelly.engine.Mouse;
import ca.gkelly.engine.ui.structs.UIDimensions;
import ca.gkelly.engine.ui.structs.UIPosition;
import ca.gkelly.engine.util.Vertex;

public class UIContainer extends UIElement {

	ArrayList<UIElement> children = new ArrayList<>();
	ArrayList<Clickable> clickables = new ArrayList<>();

	public UIContainer(UIPosition p, UIDimensions pad, Color bgColour) {
		super(p, pad, bgColour);
	}

	public void addChild(UIElement e) {
		children.add(e);
		if (e instanceof Clickable) {
			clickables.add((Clickable) e);
		}
	}

	public void removeChild(UIElement e) {
		if (children.contains(e))
			children.remove(e);
		if(e instanceof Clickable) {
			Clickable c = (Clickable) e;
			if (clickables.contains(c))
				clickables.remove(c);
		}
	}

	@Override
	public void render(Graphics2D g, UIContainer c) {
		dimens.setWidth(c.dimens.getWidth());
		dimens.setHeight(c.dimens.getHeight());
		g.setColor(bgColour);
		g.fillRect(pos.x, pos.y, dimens.getWidth(), dimens.getHeight());
		for (UIElement e : children) {
			e.render(g, this);
		}
	}

	public void onMouseMove(Vertex v) {
		for (int i = children.size() - 1; i >= 0; i--) {
			UIElement e = children.get(i);
			if (e instanceof UIContainer) {
				((UIContainer) e).onMouseMove(v);
			}
			if (e instanceof Clickable) {
				if(((Clickable) e).isMouseOver(v))
					((Clickable)e).onHover();
				else {
					((Clickable) e).onExit();
				}
			}
		}
	}

	public Clickable onMouseClick(Mouse m) {
		// Iterate backward to start at highest-rendered level
		for (int i = children.size() - 1; i >= 0; i--) {
			UIElement e = children.get(i);
			if (e instanceof UIContainer) {
				Clickable c = ((UIContainer) e).onMouseClick(m);
				if (c != null)
					return c;
			}
			if (e instanceof Clickable && ((Clickable) e).isMouseOver(m.pos)) {
				return (Clickable) e;
			}
		}
		return null;
	}

}
