package ca.gkelly.culminating.graphics;

import java.awt.Graphics;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import ca.gkelly.culminating.engine.DisplayMode;
import ca.gkelly.culminating.engine.Manager;

public class Window extends JFrame implements Runnable {
	Manager manager;
	Thread t;
	boolean runThread = true;

	public Window(DisplayMode d) {
		if (d.mode == DisplayMode.WINDOWED) {
			setSize(d.width, d.height);
		}

		setVisible(true);

		t = new Thread(this);
		t.start();

		// Set up this way to call manager.close() before closing
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addMouseListener(manager.mouse);
		addMouseMotionListener(manager.mouse);
		addKeyListener(manager.keyboard);
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

	@Override
	public void paint(Graphics g) {
		if (manager == null)
			return;

		manager.render();
	}

	@Override
	public void run() {
		while (runThread) {
			manager.calculateDeltaTime();
			manager.update();
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
		if (manager != null)
			manager.interrupt();
		m.init();
		manager = m;
	}

}
