package ca.gkelly.culminating.managers;

import java.awt.Color;
import java.awt.Font;

import ca.gkelly.engine.ui.Clickable;
import ca.gkelly.engine.ui.UIButton;
import ca.gkelly.engine.ui.UIContainer;
import ca.gkelly.engine.ui.UIManager;
import ca.gkelly.engine.ui.UIText;
import ca.gkelly.engine.ui.structs.UIBorder;
import ca.gkelly.engine.ui.structs.UIDimensions;
import ca.gkelly.engine.ui.structs.UIPosition;
import ca.gkelly.engine.ui.structs.UISet;
import ca.gkelly.engine.util.Logger;

public class MenuManager extends UIManager {

	Font titleFont = new Font("Consolas", Font.PLAIN, 50);

	UIText title;
	UIButton play, inst;

	UIContainer instructions;
	
	@Override
	protected void init() {
		title = new UIText(new UIPosition(50, 25), "Graphics Engine Demo", titleFont);
		addChild(title);
		

		inst = new UIButton(new UIPosition(0, -150, UIPosition.CENTRE, UIPosition.BOTTOM), "Instructions", titleFont);
		addChild(inst);

		play = new UIButton(new UIPosition(0, -50, UIPosition.CENTRE, UIPosition.BOTTOM), "Start", titleFont);
		addChild(play);
		
		instructions = new UIContainer(new UIPosition(0, 0, UIPosition.CENTRE, UIPosition.CENTRE), new UIDimensions(UISet.DEFAULT, 300,250), Color.white);
		instructions.setBorder(new UIBorder(5, Color.BLACK));
		addChild(instructions);
	}

	@Override
	protected void onClick(Clickable c) {
		if(c == play) {
			Logger.log(Logger.INFO, "Play");
			getWindow().setManager(new GameManager());
		}
	}

}
