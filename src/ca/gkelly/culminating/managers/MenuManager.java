package ca.gkelly.culminating.managers;

import java.awt.Color;
import java.awt.Font;

import ca.gkelly.engine.ui.UIButton;
import ca.gkelly.engine.ui.UIContainer;
import ca.gkelly.engine.ui.UIElement;
import ca.gkelly.engine.ui.UIManager;
import ca.gkelly.engine.ui.UIText;
import ca.gkelly.engine.ui.structs.UIBorder;
import ca.gkelly.engine.ui.structs.UIDimensions;
import ca.gkelly.engine.ui.structs.UIPosition;
import ca.gkelly.engine.ui.structs.UISet;
import ca.gkelly.engine.util.Logger;

public class MenuManager extends UIManager {

	Font titleFont = new Font("Consolas", Font.PLAIN, 50);
	Font subFont = new Font("Consolas", Font.PLAIN, 30);
	
	UIText title;
	UIButton play, showInst, hideInst;

	UIContainer instructions;
	
	@Override
	protected void init() {
		title = new UIText(new UIPosition(50, 25), "Graphics Engine Demo", titleFont);
		addChild(title);
		

		showInst = new UIButton(new UIPosition(0, -150, UIPosition.CENTRE, UIPosition.BOTTOM), "Instructions", titleFont);
		addChild(showInst);

		play = new UIButton(new UIPosition(0, -50, UIPosition.CENTRE, UIPosition.BOTTOM), "Start", titleFont);
		addChild(play);
		
		instructions = new UIContainer(new UIPosition(0, 0, UIPosition.CENTRE, UIPosition.CENTRE), new UIDimensions(new UISet(5), 300,250), Color.white);
		instructions.setBorder(new UIBorder(5, Color.BLACK));
		instructions.setVisible(false);
		
		instructions.addChild(new UIText(new UIPosition (0,10,UIPosition.CENTRE, UIPosition.TOP), "Instructions", subFont));
		instructions.addChild(new UIText(new UIPosition (0,90,UIPosition.CENTRE, UIPosition.TOP), "Use WASD to move", subFont));
		instructions.addChild(new UIText(new UIPosition (0,130,UIPosition.CENTRE, UIPosition.TOP), "Click to shoot", subFont));
		
		hideInst = new UIButton(new UIPosition (0,-15, UIPosition.CENTRE, UIPosition.BOTTOM), "Close", subFont);
		instructions.addChild(hideInst);
		
		
		addChild(instructions);
	}
	

	@Override
	protected void onClick(UIElement e) {
		if(e == play) {
			Logger.log(Logger.INFO, "Play");
			getWindow().setManager(new GameManager());
		}
		if(e == showInst) {
			instructions.setVisible(true);
		}
		if(e == hideInst) {
			instructions.setVisible(false);
		}
	}

}
