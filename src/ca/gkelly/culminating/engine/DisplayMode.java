package ca.gkelly.culminating.engine;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class DisplayMode{
	public static final int WINDOWED = 0;
	public static final int FULLSCREEN = 1;

	public int mode;
	public int width;
	public int height;
	
	public DisplayMode(int mode, int width, int height) {
		this.mode = mode;
		this.width = width;
		this.height = height;
	}
}
