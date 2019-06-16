package ca.gkelly.engine.ui;

import java.awt.Color;
import java.awt.Font;

import ca.gkelly.engine.collision.RectCollider;
import ca.gkelly.engine.ui.structs.UIBorder;
import ca.gkelly.engine.ui.structs.UIDimensions;
import ca.gkelly.engine.ui.structs.UIPosition;
import ca.gkelly.engine.util.Vertex;

public class UIButton extends UIText implements Clickable{

	Color oldBGColour;
	Color hoverColour = Color.DARK_GRAY;
	
	public UIButton(UIPosition p, UIDimensions d, String text, Color c, Font f, Color fc, Color hc) {
		super(p, d, text, c, f, fc);
		oldBGColour = c;
		hoverColour = hc;
		border = new UIBorder(2, fc);
	}
	
	public UIButton(UIPosition p, UIDimensions d, String text, Font f, Color hc) {
		this(p,d, text, Color.WHITE, f, Color.BLACK, hc);
	}
	
	public UIButton(UIPosition p, String text, Font f) {
		this(p, UIDimensions.DEFAULT, text, f, Color.LIGHT_GRAY);
	}
	
	public UIButton(String text, Font f) {
		this(UIPosition.DEFAULT, text, f);
	}

	@Override
	public boolean isMouseOver(double x, double y) {
		return new RectCollider(pos.x, pos.y, dimens.getTotalWidth(), dimens.getTotalHeight()).contains(x, y);
	}
	
	public void setHoverColour(Color c) {
		hoverColour = c;
	}

	@Override
	public void onHover() {
		bgColour = hoverColour;
	}

	@Override
	public void onExit() {
		bgColour = oldBGColour;
	}

}
