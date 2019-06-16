package ca.gkelly.engine.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import ca.gkelly.engine.ui.structs.UIDimensions;
import ca.gkelly.engine.ui.structs.UIPosition;

public class UIText extends UIElement {

	Font font;
	Color fontColour;
	String text;
	FontMetrics fm;

	public UIText(UIPosition p, UIDimensions d, String text, Color c, Font f, Color fc) {
		super(p, d, c);
		font = f;
		this.text = text;
		fontColour = fc;
	}
	
	public UIText(UIPosition p, UIDimensions d, String text, Font f) {
		this(p,d, text, Color.WHITE, f, Color.BLACK);
	}
	
	public UIText(UIPosition p, String text, Font f) {
		this(p, UIDimensions.DEFAULT, text, f);
	}
	
	public UIText(String text, Font f) {
		this(UIPosition.DEFAULT, text, f);
	}
	
	public void setFont(Font f, Color c) {
		font = f;
		fontColour = c;
	}
	
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public void render(Graphics2D g, UIContainer c) {
		g.setFont(font);
		fm = g.getFontMetrics();
		dimens.setWidth(fm.stringWidth(text));
		dimens.setHeight(fm.getAscent());

		super.render(g, c);
		
		g.setColor(fontColour);
		g.drawString(text, pos.x + dimens.padding.left, pos.y + dimens.getHeight());
	}

}
