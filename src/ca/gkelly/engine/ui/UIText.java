package ca.gkelly.engine.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import ca.gkelly.engine.ui.structs.UIDimensions;
import ca.gkelly.engine.ui.structs.UIPosition;

/** UIElement for rendering text */
public class UIText extends UIElement {

	/** The font to use */
	private Font font;
	/** The colour of the font */
	private Color fontColour;
	/** The text to render */
	String text;
	/** The {@link FontMetrics} to use */
	FontMetrics fm;

	/**
	 * Create the element
	 * 
	 * @param p    The position data
	 * @param d    The dimensional data
	 * @param text The text to render
	 * @param c    The background colour
	 * @param f    The font to use
	 * @param fc   The colour of the font
	 */
	public UIText(UIPosition p, UIDimensions d, String text, Color c, Font f, Color fc) {
		super(p, d, c);
		font = f;
		this.text = text;
		fontColour = fc;
	}

	/**
	 * Create the element, with white background and black font
	 * 
	 * @param p    The position data
	 * @param d    The dimensional data
	 * @param text The text to render
	 * @param f    The font to use
	 */
	public UIText(UIPosition p, UIDimensions d, String text, Font f) {
		this(p, d, text, Color.WHITE, f, Color.BLACK);
	}

	/**
	 * Create the element, with {@link UIDimensions#DEFAULT}, a white background and
	 * black font
	 * 
	 * @param p    The position data
	 * @param text The text to render
	 * @param f    The font to use
	 */
	public UIText(UIPosition p, String text, Font f) {
		this(p, UIDimensions.DEFAULT, text, f);
	}

	/**
	 * Create the element, with {@link UIPosition#DEFAULT},
	 * {@link UIDimensions#DEFAULT}, a white background and black font
	 * 
	 * @param text The text to render
	 * @param f    The font to use
	 */
	public UIText(String text, Font f) {
		this(UIPosition.DEFAULT, text, f);
	}

	/**
	 * Set the font
	 * 
	 * @param f The new font
	 * @param c The font colour
	 */
	public void setFont(Font f, Color c) {
		font = f;
		fontColour = c;
	}

	/** Get the font */
	public Font getFont() {
		return font;
	}

	/**
	 * Set the text
	 * 
	 * @param text The new text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/** Get the text */
	public String getText() {
		return text;
	}

	@Override
	public void render(Graphics2D g, UIContainer c) {
		// Get the metrics
		g.setFont(font);
		fm = g.getFontMetrics();
		// Set the width/height
		dimens.setWidth(fm.stringWidth(text));
		dimens.setHeight(fm.getAscent());

		// Render background and border
		super.render(g, c);

		// Draw the text
		g.setColor(fontColour);
		g.drawString(text, pos.x + dimens.padding.left, pos.y + dimens.getHeight());
	}

}
