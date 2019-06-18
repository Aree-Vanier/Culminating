package ca.gkelly.engine.ui;

import java.awt.Color;
import java.awt.Font;

import ca.gkelly.engine.ui.structs.UIBorder;
import ca.gkelly.engine.ui.structs.UIDimensions;
import ca.gkelly.engine.ui.structs.UIPosition;

/** UI element for buttons */
public class UIButton extends UIText {

	/** Normal background colour */
	Color normalBGColour;
	/** Hover background colour */
	Color hoverBGColour = Color.DARK_GRAY;

	/**
	 * Create the element
	 * 
	 * @param p    The position data
	 * @param d    The dimensional data
	 * @param text The text to render
	 * @param c    The background colour
	 * @param f    The font to use
	 * @param fc   The colour of the font
	 * @param hc   The hover colour
	 */
	public UIButton(UIPosition p, UIDimensions d, String text, Color c, Font f, Color fc, Color hc) {
		super(p, d, text, c, f, fc);
		normalBGColour = c;
		hoverBGColour = hc;
		border = new UIBorder(2, fc);
	}

	/**
	 * Create the element, with white background and black font
	 * 
	 * @param p    The position data
	 * @param d    The dimensional data
	 * @param text The text to render
	 * @param f    The font to use
	 * @param hc   The hover colour
	 */
	public UIButton(UIPosition p, UIDimensions d, String text, Font f, Color hc) {
		this(p, d, text, Color.WHITE, f, Color.BLACK, hc);
	}

	/**
	 * Create the element, with {@link UIDimensions#UIDimensions()}, a white background,
	 * light-gray hover and black font
	 * 
	 * @param p    The position data
	 * @param text The text to render
	 * @param f    The font to use
	 */
	public UIButton(UIPosition p, String text, Font f) {
		this(p, new UIDimensions(), text, f, Color.LIGHT_GRAY);
	}

	/**
	 * Create the element, with {@link UIPosition#UIPosition()},
	 * {@link UIDimensions#UIDimensions()}, a white background, light-gray hover and black
	 * font
	 * 
	 * @param text The text to render
	 * @param f    The font to use
	 */
	public UIButton(String text, Font f) {
		this(new UIPosition(), text, f);
	}

	/**
	 * Set the hover colour
	 * 
	 * @param c The new hover colour
	 */
	public void setHoverColour(Color c) {
		hoverBGColour = c;
	}


	@Override
	public void onHover() {
		bgColour = hoverBGColour;
	}

	@Override
	public void onNotHover() {
		bgColour = normalBGColour;
	}

}
