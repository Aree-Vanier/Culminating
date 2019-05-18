package ca.gkelly.culminating.engine;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import ca.gkelly.culminating.engine.input.Keyboard;
import ca.gkelly.culminating.engine.input.Mouse;

/**
 * Abstract class to be extended to create managers<br/>
 * <strong>Example:</strong> One manager for main menu, another for game, then
 * switch between them as needed
 */
public abstract class Manager {

	private int deltaTime = 0;
	private long lastTime = 0;

	/** Mouse event handler */
	public Mouse mouse;
	/** Keyboard event handler */
	public Keyboard keyboard;

	/** Called when the manager is initialized */
	public abstract void init();

	/** Called periodically by {@link Window} on main thread */
	public abstract void render();

	/** Called periodically by {@link Window} on separate thread */
	public abstract void update();

	/** Called when the manager is interrupted */
	public abstract void interrupt();

	/** Called when the window is closed */
	public void onClose() {
		// By default call interrupt
		interrupt();
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

	/**
	 * Calculates new {@link #deltaTime} value<br/>
	 * Called automatically by {@link Window} before {@link #update()}
	 */
	public final void calculateDeltaTime() {
		deltaTime = (int) (System.currentTimeMillis() - lastTime);
		lastTime = System.currentTimeMillis();
	}
}
