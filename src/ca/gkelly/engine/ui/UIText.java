package ca.gkelly.engine.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import ca.gkelly.engine.ui.structs.UIDimens;
import ca.gkelly.engine.ui.structs.UIPosition;

public class UIText extends UIElement {

	Font font;
	Color fontColour;
	String text;
	FontMetrics fm;

	public UIText(UIPosition p, UIDimens dimens, Color c, Font f, Color fc, String text) {
		super(p, dimens, c);
		font = f;
		this.text = text;
		fontColour = fc;
	}

	public void setFont(Font f) {
		font = f;
	}

	@Override
	public void render(Graphics g, UIContainer c) {
		g.setFont(font);
		fm = g.getFontMetrics();
		dimens.setWidth(fm.stringWidth(text));
		dimens.setHeight(fm.getAscent());
		pos.getPos(g, this, c);

		g.setColor(bgColour);
		g.fillRect(pos.x, pos.y, dimens.getTotalWidth(), dimens.getTotalHeight());

		g.setColor(fontColour);
		g.drawString(text, pos.x + dimens.padding.left, pos.y + dimens.getHeight());
	}

}
