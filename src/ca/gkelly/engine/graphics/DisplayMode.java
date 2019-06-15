package ca.gkelly.engine.graphics;

import ca.gkelly.engine.Window;

/** Class to be used to set display options for {@link Window}s */
public class DisplayMode {
	/** Indicates that the window will be windowed */
	public static final int WINDOWED = 0;
	/** Indicates that the window will be fullscreen */
	public static final int FULLSCREEN = 1;

	public int mode;
	public int width;
	public int height;

	/**
	 * Create the DisplayMode
	 * 
	 * @param mode   Mode ({@link #WINDOWED} or {@link #FULLSCREEN})
	 * @param width  Window width
	 * @param height Window height
	 */
	public DisplayMode(int mode, int width, int height) {
		this.mode = mode;
		this.width = width;
		this.height = height;
	}
}
