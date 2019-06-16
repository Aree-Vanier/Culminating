package ca.gkelly.engine.ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import ca.gkelly.engine.Manager;
import ca.gkelly.engine.ui.structs.UIDimensions;
import ca.gkelly.engine.ui.structs.UIPosition;
import ca.gkelly.engine.ui.structs.UISet;

public abstract class UIManager extends Manager {

	private UILayer ui;
	private double widthPercent, heightPercent;

	@Override
	protected final void init(Container c) {
		ui = new UILayer(0, 0, new UIDimensions(UISet.NONE, c.getWidth(), c.getHeight()), Color.WHITE);
		widthPercent = 1;
		heightPercent = 1;
		init();
	}
	
	protected abstract void init();

	public void setPosition(UIPosition p) {
		ui.setPosition(p);
	}
	
	public void addChild(UIElement e) {
		ui.addChild(e);
	}
	
	public void removeChild(UIElement e) {
		ui.removeChild(e);
	}
	
	public void setDiemns(UIDimensions d) {
		ui.setDimens(d);
		widthPercent = getWindow().getWidth()/d.getTotalWidth();
		heightPercent = getWindow().getHeight()/d.getTotalHeight();
	}
	
	public void setColour(Color c) {
		ui.setBackground(c);
	}
	
	@Override
	protected void render(Graphics2D g) {
		ui.render(g);
	}

	@Override
	protected void update() {

	}

	@Override
	protected void end() {

	}

	@Override
	protected final void onMouseMoved(MouseEvent e) {
		ui.onMouseMove(mouse.pos);
	}

	@Override
	protected final void onMouseRelease(MouseEvent e) {
		Clickable c = ui.onMouseClick(mouse);
		if (c != null)
			onClick(c);
	}

	protected abstract void onClick(Clickable c);

	@Override
	protected void onResize() {
		ui.setWidth((int) (getWindow().getWidth()*widthPercent));
		ui.setHeight((int) (getWindow().getHeight()*heightPercent));
	}

}
