package ca.gkelly.culminating.managers;

import java.awt.Font;

import ca.gkelly.engine.ui.Clickable;
import ca.gkelly.engine.ui.UIButton;
import ca.gkelly.engine.ui.UIManager;
import ca.gkelly.engine.ui.UIText;
import ca.gkelly.engine.ui.structs.UIPosition;
import ca.gkelly.engine.util.Logger;

public class MenuManager extends UIManager {

	Font titleFont = new Font("Consolas", Font.PLAIN, 50);

	UIText title;
	UIButton play;

	@Override
	protected void init() {
		title = new UIText(new UIPosition(50, 25), "Graphics Engine Demo", titleFont);
		addChild(title);

		play = new UIButton(new UIPosition(0, -50, UIPosition.CENTRE, UIPosition.BOTTOM), "Start", titleFont);
		addChild(play);
	}

	@Override
	protected void onClick(Clickable c) {
		if (c == play) {
			Logger.log(Logger.INFO, "Play");
			getWindow().setManager(new GameManager());
		}
	}

}
