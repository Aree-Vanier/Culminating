package ca.gkelly.engine.graphics;

import java.awt.Graphics;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import ca.gkelly.engine.Manager;
import ca.gkelly.engine.util.Logger;

/**
 * Class used to create and manage a JFrame window<br/>
 * Paint, Update and Input will be passed to the active {@link Manager}
 */
public class Window extends JFrame implements Runnable {
	Manager manager;
	Thread t;
	boolean runThread = true;

	public static int deltaTime = 0;
	private long lastTime = 0;

	/**
	 * Create a new window, a {@link Manager} will need to be set for functionality
	 * 
	 * @param d {@link DisplayMode} specifying window dimensions and mode
	 */
	public Window(DisplayMode d) {
		if (d.mode == DisplayMode.WINDOWED) {
			setSize(d.width, d.height);
		}

		setVisible(true);

		t = new Thread(this);
		t.start();

		// Set up this way to call manager.close() before closing
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
				manager.onClose();
				runThread = false;
				System.exit(0);
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowActivated(WindowEvent e) {
			}
		});
	}

	/**
	 * Create a new window, with an associated {@link Manager}
	 * 
	 * @param d {@link DisplayMode} specifying window dimensions and mode
	 * @param m {@link Manager} to be used
	 */
	public Window(DisplayMode d, Manager m) {
		this(d);
		setManager(m);
	}

	@Override
	public void paint(Graphics g) {
		if (manager == null)
			return;

		manager.render(g);
	}

	@Override
	public void run() {
		while (runThread) {
			calculateDeltaTime();
			if (manager != null) {
				manager.update();
				// TODO: Find a better way
				repaint();
			} else {
				Logger.log(Logger.INFO, "Manager is null");
			}
			sleepUntilDeltaTime();
		}
	}

	/**
	 * Set the active manager <br/>
	 * This will call {@link Manager#interrupt interrupt()} on the old manager and
	 * {@link Manager#init init()} on the new one
	 * 
	 * @param m New manager
	 */
	public void setManager(Manager m) {
		if (manager != null) {
			manager.end();
			removeMouseListener(manager.mouse);
			removeMouseMotionListener(manager.mouse);
			removeKeyListener(manager.keyboard);
		}
		m.init(getContentPane());
		manager = m;
		addMouseListener(manager.mouse);
		addMouseMotionListener(manager.mouse);
		addKeyListener(manager.keyboard);
	}

	/**
	 * Calculates new {@link #deltaTime} value<br/>
	 * Called before {@link Manager#update()}
	 */
	public final void calculateDeltaTime() {
		deltaTime = (int) (System.currentTimeMillis() - lastTime);
		lastTime = System.currentTimeMillis();
	}

	/**
	 * Pauses thread based on {@link #deltaTime} value to maintain
	 * {@link Manager#targetFramerate}<br/>
	 * Called after {@link Manager#update()}
	 */
	public final void sleepUntilDeltaTime() {
		if (manager == null)
			return;
		try {
			Thread.sleep((1000 / manager.targetFramerate) - (System.currentTimeMillis() - lastTime));
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			Logger.log(Logger.INFO, "Loop time exceed 20ms");
		}
	}

}
