package ca.gkelly.culminating.managers;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import ca.gkelly.culminating.Game;
import ca.gkelly.engine.Manager;
import ca.gkelly.engine.ui.UIButton;
import ca.gkelly.engine.ui.UILayer;
import ca.gkelly.engine.ui.UIText;
import ca.gkelly.engine.ui.structs.UIDimens;
import ca.gkelly.engine.ui.structs.UIPosition;
import ca.gkelly.engine.ui.structs.UISet;

public class MenuManager extends Manager {

	Font titleFont = new Font("Consolas", Font.PLAIN, 50);
	
	UILayer l;
	UIText title;
	UIButton play;

	@Override
	protected void init(Container c) {
		l = new UILayer(0, 0, new UIDimens(UISet.NONE, c.getWidth(), c.getHeight()), Color.BLACK);
		title = new UIText(new UIPosition(50, 25), new UIDimens(UISet.DEFAULT), Color.BLACK, titleFont, Color.WHITE, "Test Title");
		l.addChild(title);
		
		play = new UIButton(new UIPosition(50, 25, UIPosition.CENTRE, UIPosition.BOTTOM), new UIDimens(UISet.DEFAULT), Color.white, titleFont, Color.black, "Play");
		l.addChild(play);
	}

	@Override
	protected void render(Graphics2D g) {
		l.render(g);
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
	
	@Override
	protected void onMouseMoved(MouseEvent e) {
		if(play.isMouseOver(mouse.pos))
			play.onHover();
		else {
			play.onExit();
		}
	}
	
	@Override
	protected void onMousePress(MouseEvent e) {
		if(play.isMouseOver(mouse.pos)) {
			getWindow().setManager(new GameManager());
		}
	}
}
