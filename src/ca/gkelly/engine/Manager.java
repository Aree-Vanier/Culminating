package ca.gkelly.engine;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import ca.gkelly.engine.graphics.Window;
import ca.gkelly.engine.input.Keyboard;
import ca.gkelly.engine.input.Mouse;

/**
 * Abstract class to be extended to create managers<br/>
 * <strong>Example:</strong> One manager for main menu, another for game, then
 * switch between them as needed
 */
public abstract class Manager {

	/** Target framerate to be maintained by {@link Window} */
	public int targetFramerate = 60;

	/** Mouse event handler */
	public Mouse mouse = new Mouse(this);
	/** Keyboard event handler */
	public Keyboard keyboard = new Keyboard(this);

	/** Called when the manager is initialized */
	public abstract void init(Container container);

	/** Called periodically by {@link Window} on main thread */
	public abstract void render(Graphics g);

	/** Called periodically by {@link Window} on separate thread */
	public abstract void update();

	/** Called when the manager is ended */
	public abstract void end();

	/** Called when the window is closed */
	public void onClose() {
		// By default call normal end code
		end();
	}

	/** Called by {@link #mouse} when the mouse is clicked */
	public void onClick(MouseEvent e) {

	}

	/** Called by {@link #mouse} when the mouse is pressed */
	public void onMousePress(MouseEvent e) {

	}

	/** Called by {@link #mouse} when the mouse is released */
	public void onMouseRelease(MouseEvent e) {

	}

	/** Called by {@link #mouse} when the mouse is moved */
	public void onMouseMoved(MouseEvent e) {

	}

	/** Called by {@link #keyboard} when a key is typed */
	public void onKeyTyped(KeyEvent e) {

	}

	/** Called by {@link #keyboard} when a key is pressed */
	public void onKeyPressed(KeyEvent e) {

	}

	/** Called by {@link #keyboard} when a key is released */
	public void onKeyReleased(KeyEvent e) {

	}

}
