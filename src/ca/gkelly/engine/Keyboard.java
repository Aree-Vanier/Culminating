package ca.gkelly.engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/** Class used by {@link Manager}s to handle keyboard input */
public class Keyboard implements KeyListener {

	/** List of all currently pressed keys */
	public ArrayList<Integer> pressed = new ArrayList<>();

	Manager m;

	/**
	 * @param m The manager to use for callbacks
	 */
	public Keyboard(Manager m) {
		this.m = m;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		m.onKeyTyped(e);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (!pressed.contains(e.getKeyCode())) {
			pressed.add(e.getKeyCode());
		}
		m.onKeyPress(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (pressed.contains(e.getKeyCode())) {
			pressed.remove((Object) e.getKeyCode());
		}
		m.onKeyRelease(e);
	}

}
