package ca.gkelly.engine.ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import ca.gkelly.engine.Manager;
import ca.gkelly.engine.ui.structs.UIDimensions;
import ca.gkelly.engine.ui.structs.UIPosition;
import ca.gkelly.engine.ui.structs.UISet;

/** Special manager for use with UI systems */
public abstract class UIManager extends Manager {

	/** The associated UI layer */
	private UILayer ui;
	/** Used for scaling */
	private double widthPercent, heightPercent;

	@Override
	protected final void init(Container c) {
		ui = new UILayer(0, 0, new UIDimensions(UISet.NONE, c.getWidth(), c.getHeight()), Color.WHITE);
		widthPercent = 1;
		heightPercent = 1;
		init();
	}

	/** Called upon initialization */
	protected abstract void init();

	/**
	 * Set the {@link #ui UILayer} position
	 * 
	 * @param p The new position
	 */
	public void setPosition(UIPosition p) {
		ui.setPosition(p);
	}

	/**
	 * Add a child {@link UIElement} to the {@link #ui UILayer}
	 * 
	 * @param e the element
	 */
	public void addChild(UIElement e) {
		ui.addChild(e);
	}

	/**
	 * Remove a child {@link UIElement} from the {@link #ui UILayer}
	 * 
	 * @param e The element
	 */
	public void removeChild(UIElement e) {
		ui.removeChild(e);
	}

	/**
	 * Set the dimensions of the {@link #ui UILayer}
	 * 
	 * @param d The new dimensions
	 */
	public void setDimens(UIDimensions d) {
		ui.setDimens(d);
		widthPercent = getWindow().getWidth() / d.getTotalWidth();
		heightPercent = getWindow().getHeight() / d.getTotalHeight();
	}

	/**
	 * Set the background colour of the {@link #ui UILayer}
	 * 
	 * @param c The new colour
	 */
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

	/**
	 * Called if a {@link UIElement} is clicked
	 * 
	 * @param c The element that was clicked
	 */
	protected abstract void onClick(Clickable c);

	@Override
	protected void onResize() {
		ui.setWidth((int) (getWindow().getWidth() * widthPercent));
		ui.setHeight((int) (getWindow().getHeight() * heightPercent));
	}

}
