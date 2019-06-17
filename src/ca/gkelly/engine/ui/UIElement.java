package ca.gkelly.engine.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import ca.gkelly.engine.ui.structs.UIBorder;
import ca.gkelly.engine.ui.structs.UIDimensions;
import ca.gkelly.engine.ui.structs.UIPosition;

/** Base-level class for UI elements */
public abstract class UIElement {
	/** The position of the element */
	public UIPosition pos;
	/** The dimensions of the element */
	public UIDimensions dimens;
	/** The border around the element, defaults to {@link UIBorder#NONE} */
	public UIBorder border = UIBorder.NONE;

	/** The background colour of the element */
	Color bgColour;

	/** If false, the element will not be rendered and will ignore clicks */
	boolean visible = true;

	/**
	 * Create the element
	 * 
	 * @param p        The position data
	 * @param dimens   The dimensional data
	 * @param bgColour The background colour
	 */
	public UIElement(UIPosition p, UIDimensions dimens, Color bgColour) {
		pos = p;
		this.bgColour = bgColour;
		this.dimens = dimens;
	}

	/**
	 * Create the element, with white background
	 * 
	 * @param p      The position data
	 * @param dimens The dimensional data
	 */
	public UIElement(UIPosition p, UIDimensions dimens) {
		this(p, dimens, Color.WHITE);
	}

	/**
	 * Create the element, with white background and {@link UIDimensions#DEFAULT}
	 * 
	 * @param p The position data
	 */
	public UIElement(UIPosition p) {
		this(p, UIDimensions.DEFAULT);
	}

	/**
	 * Create the element, with white background, {@link UIDimensions#DEFAULT}, and
	 * {@link UIPosition#DEFAULT}
	 */
	public UIElement() {
		this(UIPosition.DEFAULT);
	}

	/**
	 * Changes the visibility of the element
	 * 
	 * @param vis The new visibility state
	 */
	public void setVisible(Boolean vis) {
		visible = vis;
	}

	/**
	 * Get the visibility of the element
	 */
	public boolean getVisible() {
		return visible;
	}

	/**
	 * Set the border
	 * 
	 * @param b The new {@link UIBorder}
	 */
	public void setBorder(UIBorder b) {
		border = b;
	}

	/**
	 * Set the background colour
	 * 
	 * @param c The new colour
	 */
	public void setBackground(Color c) {
		bgColour = c;
	}

	/**
	 * Set the position
	 * 
	 * @param p The new {@link UIPosition}
	 */
	public void setPosition(UIPosition p) {
		pos = p;
	}

	/**
	 * Set the dimensions
	 * 
	 * @param d The new {@link UIDimensions}
	 */
	public void setDimens(UIDimensions d) {
		dimens = d;
	}

	/**
	 * Render the element<br/>
	 * Draws the background and border Will not work if visible is false
	 * 
	 * @param g The graphics to draw to
	 * @param c The parent container
	 */
	public void render(Graphics2D g, UIContainer c) {
		// Update the position values
		pos.updatePos(this, c);

		// Draw the background
		g.setColor(bgColour);
		g.fillRect(pos.x, pos.y, dimens.getTotalWidth(), dimens.getTotalHeight());

		// Draw the border
		border.render(g, pos.x, pos.y, dimens.getTotalWidth(), dimens.getTotalHeight());
		// Reset the stroke
		g.setStroke(new BasicStroke(1));
	}
}
