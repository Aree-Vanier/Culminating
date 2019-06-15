package ca.gkelly.culminating.managers;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import ca.gkelly.engine.Manager;

public class MenuManager extends Manager {

	@Override
	protected void init(Container c) {

	}

	@Override
	protected void render(Graphics2D g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, getWindow().getWidth(), getWindow().getHeight());
		g.setColor(Color.white);
		g.drawString("Graphics Engine Demo", 50, 10);

		g.drawString("Press SPACE to begin", 50, 500);
	}

	@Override
	protected void update() {
		
	}

	@Override
	public void end() {

	}

	@Override
	protected void onKeyPress(KeyEvent e) {
		super.onKeyPress(e);
	}
}
