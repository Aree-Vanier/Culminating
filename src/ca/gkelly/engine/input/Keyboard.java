package ca.gkelly.engine.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import ca.gkelly.engine.Manager;

/** Class used by {@link Manager}s to handle keyboard input */
public class Keyboard implements KeyListener {

	/** List of all currently pressed keys */
	public ArrayList<Integer> pressed = new ArrayList<>();

	Manager m;

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
		m.onKeyPressed(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (pressed.contains(e.getKeyCode())) {
			pressed.remove((Object) e.getKeyCode());
		}
		m.onKeyReleased(e);
	}

}
