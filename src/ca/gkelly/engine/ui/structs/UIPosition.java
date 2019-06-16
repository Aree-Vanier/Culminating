package ca.gkelly.engine.ui.structs;

import java.awt.Graphics;

import ca.gkelly.engine.ui.UIContainer;
import ca.gkelly.engine.ui.UIElement;

public class UIPosition {
	
	public static UIPosition DEFAULT = new UIPosition(0,0);
	
	public static final int LEFT = 0;
	public static final int TOP = 0;
	public static final int CENTRE = 1;
	public static final int RIGHT = 2;
	public static final int BOTTOM = 2;

	public int offsetX, offsetY;
	public int horizontal, vertical;
	public int x, y;

	public UIPosition(int offX, int offY, int horz, int vert) {
		horizontal = horz;
		vertical = vert;
		offsetX = offX;
		offsetY = offY;
	}

	public UIPosition(int x, int y) {
		this(x, y, LEFT, TOP);
	}

	public void getPos(Graphics g, UIElement e, UIContainer c) {
		// Get horizontal position
		if (horizontal == LEFT)
			x = offsetX;
		if (horizontal == CENTRE)
			x = c.dimens.getTotalWidth() / 2 - e.dimens.getTotalWidth() / 2 + offsetX;
		if (horizontal == RIGHT)
			x = c.dimens.getTotalWidth() - e.dimens.getTotalWidth() - offsetX;
		// Get vertical position
		if (vertical == TOP)
			y = offsetY;
		if (vertical == CENTRE)
			y = c.dimens.getTotalHeight() / 2 - e.dimens.getTotalHeight() / 2 + offsetY;
		if (vertical == BOTTOM)
			y = c.dimens.getTotalHeight() - e.dimens.getTotalHeight() - offsetY;

		x += c.pos.x;
		y += c.pos.y;
	}

}
