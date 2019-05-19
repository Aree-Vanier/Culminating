package ca.gkelly.culminating;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;

import ca.gkelly.culminating.engine.DisplayMode;
import ca.gkelly.culminating.entities.Mount;
import ca.gkelly.culminating.entities.Ship;
import ca.gkelly.culminating.entities.Weapon;
import ca.gkelly.culminating.graphics.Camera;
import ca.gkelly.culminating.graphics.TiledMap;
import ca.gkelly.culminating.graphics.Window;
import ca.gkelly.culminating.loader.Loader;
import ca.gkelly.culminating.util.Logger;

//This is a class used for quick console tests that don't need the graphics
public class Test {

	TestManager manager;
	
	public static void main(String[] args) {
		Logger.log(Logger.INFO, "TESTING");
		new Test(args);
	}

	public Test(String[] args) {
		manager = new TestManager(args);
		
		new Window(new DisplayMode(DisplayMode.WINDOWED, 640, 480), manager);
	}

}
