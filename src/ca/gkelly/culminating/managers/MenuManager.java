package ca.gkelly.culminating.managers;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import ca.gkelly.culminating.Game;
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
		g.drawString("Graphics Engine Demo", 50, 25);

		g.drawString("Press SPACE to begin", getWindow().getHeight()/2 - g.getFontMetrics().stringWidth("Press SPACE to begin")/2, getWindow().getHeight()-50);
	}

	@Override
	protected void update() {
		
	}

	@Override
	public void end() {

	}

	@Override
	protected void onKeyPress(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			getWindow().setManager(Game.gm);
		}
	}
}
