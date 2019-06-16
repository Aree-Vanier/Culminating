package ca.gkelly.engine;

import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Abstract class to be extended to create managers<br/>
 * <strong>Example:</strong> One manager for main menu, another for game, then
 * switch between them as needed
 */
public abstract class Manager {

	/** Mouse event handler */
	protected Mouse mouse = new Mouse(this);
	/** Keyboard event handler */
	protected Keyboard keyboard = new Keyboard(this);
	/** Parent window instance, cannot be changed without re-init */
	private Window window;

	/**
	 * Called when the manager is initialized
	 * 
	 * @param w The {@link Window} parent
	 */
	public final void init(Window w) {
		window = w;
		init(w.getContentPane());
	}

	/**
	 * The overridden initializer, called by {@link #init(Window)}
	 * 
	 * @param c The container parent
	 */
	protected abstract void init(Container c);

	/** Called periodically by {@link Window} on main thread */
	protected abstract void render(Graphics2D g);

	/** Called periodically by {@link Window} on separate thread */
	protected abstract void update();

	/** Called when the manager is ended */
	protected abstract void end();

	/** Called when the window is closed */
	protected void onClose() {
		// By default call normal end code
		end();
	}

	/** Called by {@link #mouse} when the mouse is clicked */
	protected void onClick(MouseEvent e) {

	}

	/** Called by {@link #mouse} when the mouse is pressed */
	protected void onMousePress(MouseEvent e) {

	}

	/** Called by {@link #mouse} when the mouse is released */
	protected void onMouseRelease(MouseEvent e) {

	}

	/** Called by {@link #mouse} when the mouse is moved */
	protected void onMouseMoved(MouseEvent e) {

	}

	/** Called by {@link #keyboard} when a key is typed */
	protected void onKeyTyped(KeyEvent e) {

	}

	/** Called by {@link #keyboard} when a key is pressed */
	protected void onKeyPress(KeyEvent e) {

	}

	/** Called by {@link #keyboard} when a key is released */
	protected void onKeyRelease(KeyEvent e) {

	}

	/** Get the parent window instance */
	public final Window getWindow() {
		return window;
	}

	/** Called when the window is resized */
	protected void onResize() {
		
	}

}
