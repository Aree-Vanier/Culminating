package ca.gkelly.engine.ui.structs;

import ca.gkelly.engine.ui.UIContainer;
import ca.gkelly.engine.ui.UIElement;

/** Handles the position of a {@link UIElement} */
public class UIPosition {

	/** Shorthand for top-left position */
	public static UIPosition DEFAULT = new UIPosition(0, 0);

	/** Align left */
	public static final int LEFT = 0;
	/** Align top */
	public static final int TOP = 0;
	/** Align centre */
	public static final int CENTRE = 1;
	/** Align right */
	public static final int RIGHT = 2;
	/** Align bottom */
	public static final int BOTTOM = 2;

	/** Positional Offset */
	public int offsetX, offsetY;
	/** Horizontal alignment */
	private int horizontal;
	/** Vertical alignment */
	private int vertical;
	/** Actual position */
	public int x, y;

	/**
	 * Create the position
	 * 
	 * @param offX The x offset
	 * @param offY The y offset
	 * @param horz The horizontal alignment
	 * @param vert The vertical alignment
	 */
	public UIPosition(int offX, int offY, int horz, int vert) {
		horizontal = horz;
		vertical = vert;
		offsetX = offX;
		offsetY = offY;
	}

	/**
	 * Create the position, with top-left alignment
	 * 
	 * @param x The x offset
	 * @param y The y offset
	 */
	public UIPosition(int x, int y) {
		this(x, y, LEFT, TOP);
	}

	/**Update the {@link #x} and {@link y} values
	 * @param e The {@link UIElement} that uses this position
	 * @param c The parent {@link UIContainer}*/
	public void updatePos(UIElement e, UIContainer c) {
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
			y = c.dimens.getTotalHeight() - e.dimens.getTotalHeight() + offsetY;

		//Add the parent's position
		x += c.pos.x;
		y += c.pos.y;
	}

}
