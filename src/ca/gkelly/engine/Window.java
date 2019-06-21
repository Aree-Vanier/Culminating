package ca.gkelly.engine;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import ca.gkelly.engine.graphics.DisplayMode;
import ca.gkelly.engine.util.Logger;

/**
 * Class used to create and manage a JFrame window<br/>
 * Paint, Update and Input will be passed to the active {@link Manager}
 */
public class Window extends JFrame implements Runnable {

	private static final long serialVersionUID = -6870685415903486630L;

	/** Target framerate to be maintained by {@link Window} */
	public int targetFramerate = 60;
	/** The active {@link Manager} */
	Manager manager;
	Thread update;
	boolean runThread = true;

	/** Backbuffer used for rendering */
	BufferedImage buffer;

	/** Deltatime for {@link #update}, can be referenced by any class */
	public int deltaTime = 0;
	/** Used for calculating {@link #deltaTime} */
	private long lastTime = 0;

	/**
	 * Create a new window, a {@link Manager} will need to be set for functionality
	 * 
	 * @param d {@link DisplayMode} specifying window dimensions and mode
	 * @param m {@link Manager} to be used
	 */
	public Window(DisplayMode d, Manager m) {
		if (d.mode == DisplayMode.WINDOWED) {
			setSize(d.width, d.height);
		}

		setVisible(true);

		update = new Thread(this);
		update.start();

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
				if (manager != null)
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

		addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent e) {

			}

			@Override
			public void componentResized(ComponentEvent e) {
				if (manager != null)
					manager.onResize();
			}

			@Override
			public void componentMoved(ComponentEvent e) {

			}

			@Override
			public void componentHidden(ComponentEvent e) {

			}
		});

		if (m != null)
			setManager(m);
	}

	/** Called to start the render process, will hang thread */
	public void begin() {
		while (true) {
			repaint();
		}
	}

	@Override
	public void paint(Graphics g) {
		if (manager == null)
			return;
		buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		manager.render((Graphics2D) buffer.getGraphics());
		g.drawImage(buffer, getInsets().left, getInsets().top, null);
	}

	@Override
	public void run() {
		while (runThread) {
			calculateDeltaTime();
			if (manager != null) {
				manager.update();
			} else {
				Logger.log(Logger.INFO, "Manager is null");
			}
			sleepUntilDeltaTime();
		}
	}

	/**
	 * Set the active manager <br/>
	 * This will call {@link Manager#end end()} on the old manager and
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
		m.init(this);
		manager = m;
		addMouseListener(manager.mouse);
		addMouseMotionListener(manager.mouse);
		addKeyListener(manager.keyboard);
	}

	/**
	 * Calculates new {@link #deltaTime} value<br/>
	 * Called before {@link Manager#update()}
	 */
	private void calculateDeltaTime() {
		deltaTime = (int) (System.currentTimeMillis() - lastTime);
		lastTime = System.currentTimeMillis();
	}

	/**
	 * Pauses thread based on {@link #deltaTime} value to maintain
	 * {@link Manager#targetFramerate}<br/>
	 * Called after {@link Manager#update()}
	 */
	private void sleepUntilDeltaTime() {
		try {
			Thread.sleep((1000 / targetFramerate) - (System.currentTimeMillis() - lastTime));
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			Logger.log(Logger.INFO, "Loop time exceed 20ms");
		}
	}

}
