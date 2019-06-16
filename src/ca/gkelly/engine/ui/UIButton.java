package ca.gkelly.engine.ui;

import java.awt.Color;
import java.awt.Font;

import ca.gkelly.engine.collision.RectCollider;
import ca.gkelly.engine.ui.structs.UIDimens;
import ca.gkelly.engine.ui.structs.UIPosition;
import ca.gkelly.engine.util.Vertex;

public class UIButton extends UIText implements Clickable{

	Color oldBGColour;
	Color hoverColour = Color.DARK_GRAY;
	
	public UIButton(UIPosition p, UIDimens dimens, Color c, Font f, Color fc, String text) {
		super(p, dimens, c, f, fc, text);
		oldBGColour = bgColour;
	}

	@Override
	public boolean isMouseOver(double x, double y) {
		return new RectCollider(pos.x, pos.y, dimens.getTotalWidth(), dimens.getTotalHeight()).contains(x, y);
	}

	@Override
	public void onClick() {
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
