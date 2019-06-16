package ca.gkelly.engine.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import ca.gkelly.engine.ui.structs.UIBorder;
import ca.gkelly.engine.ui.structs.UIDimensions;
import ca.gkelly.engine.ui.structs.UIPosition;

public abstract class UIElement {
	public UIPosition pos;
	public UIDimensions dimens;
	public UIBorder border = UIBorder.NONE;
	Color bgColour;
	
	public UIElement(UIPosition p, UIDimensions dimens, Color bgColour) {
		pos = p;
		this.bgColour = bgColour;
		this.dimens = dimens;
	}
	
	public UIElement(UIPosition p, UIDimensions dimens) {
		this(p,dimens,Color.WHITE);
	}
	
	public UIElement(UIPosition p) {
		this(p, UIDimensions.DEFAULT);
	}
	
	public UIElement() {
		this(UIPosition.DEFAULT);
	}
	
	public void setBorder(UIBorder b) {
		border = b;
	}
	
	public void setBackground(Color c) {
		bgColour = c;
	}
	
	public void setPosition(UIPosition p) {
		pos = p;
	}
	
	public void setDimens(UIDimensions d) {
		dimens = d;
	}
	
	public void render(Graphics2D g, UIContainer c) {
		pos.updatePos(this, c);
		g.setColor(bgColour);
		g.fillRect(pos.x, pos.y, dimens.getTotalWidth(), dimens.getTotalHeight());
		border.render(g, pos.x, pos.y, dimens.getTotalWidth(), dimens.getTotalHeight());
		g.setStroke(new BasicStroke(1));
	}
}
