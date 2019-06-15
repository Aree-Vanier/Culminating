package ca.gkelly.engine.loader;

import java.awt.image.BufferedImage;

import ca.gkelly.engine.graphics.Camera;

/** Designed to add a basic image rendering to {@link Entity} */
public interface ImageEntity {

	/** Function used to render the image
	 * @param c {@link Camera} to render to */
	default void render(Camera c) {
		c.render(getImage(), getRectX(), getRectY());
	}

	/**
	 * Gets the top-left corner of entity for rendering<br/>
	 * Automatically overridden by {@link Entity}
	 */
	public int getRectX();

	/**
	 * Gets the top-left corner of entity for rendering<br/>
	 * Automatically overridden by {@link Entity}
	 */
	public int getRectY();

	/** Gets the image to be used for rendering */
	public BufferedImage getImage();

}
