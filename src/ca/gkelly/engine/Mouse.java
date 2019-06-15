package ca.gkelly.engine;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import ca.gkelly.engine.util.Vertex;

/** Class used by {@link Manager}s to handle mouse input */
public class Mouse implements MouseListener, MouseMotionListener {

	public boolean onScreen;
	public boolean left;
	public boolean right;
	public boolean middle;
	public Vertex pos = new Vertex(0,0);

	Manager m;

	/**
	 * @param m The manager to use for callbacks
	 */
	public Mouse(Manager m) {
		this.m = m;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		m.onClick(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1)
			left = true;
		if (e.getButton() == MouseEvent.BUTTON2)
			right = true;
		if (e.getButton() == MouseEvent.BUTTON3)
			middle = true;
		m.onMousePress(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1)
			left = false;
		if (e.getButton() == MouseEvent.BUTTON2)
			right = false;
		if (e.getButton() == MouseEvent.BUTTON3)
			middle = false;
		m.onMouseRelease(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		onScreen = true;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		onScreen = false;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		pos.x = e.getX();
		pos.y = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		pos.x = e.getX() - m.getWindow().getInsets().left;
		pos.y = e.getY() - m.getWindow().getInsets().top;
		m.onMouseMoved(e);
	}

}
