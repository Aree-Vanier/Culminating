package ca.gkelly.culminating.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

import ca.gkelly.culminating.util.Logger;

public class Camera {

	public double zoom = 1;
	int x;
	int y;

	int renderDist = 10;
	BufferedImage buffer;
	Graphics2D g;
	Container window;
	TiledMap map;

	int maxX, maxY, minX, minY;

	/**
	 * Create a new Camera
	 * 
	 * @param window The container that will hold the camera
	 * @param map    The {@link TiledMap} that will be used as the background
	 */
	public Camera(Container window, TiledMap map) {
		this.window = window;
		this.map = map;
	}

	/**
	 * First step in rendering process, prepares buffer, graphics and min/max values
	 */
	public void begin() {
		minX = (int) ((x - (window.getWidth() * (renderDist - 0.5))) * zoom);
		maxX = (int) ((x + (window.getWidth() * (renderDist - 0.5))) * zoom);

		minY = (int) ((y - (window.getHeight() * (renderDist - 0.5))) * zoom);
		maxY = (int) ((y + (window.getHeight() * (renderDist - 0.5))) * zoom);

		buffer = new BufferedImage(window.getWidth(), window.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		g = (Graphics2D) buffer.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());

		Logger.epoch("REND");

		// Render the map
		Object[] mapRender = map.render(worldSpace(0, 0), worldSpace(buffer.getWidth(), buffer.getHeight()));
		render((BufferedImage) mapRender[0], (int) mapRender[1], (int) mapRender[2]);
//		Logger.log("Map rendered in {REND}");

		drawPoint(minX, minY, 15, Color.CYAN);
		drawPoint(maxX, maxY, 15, Color.BLACK);
	}

	/**
	 * Render an image to the camera
	 * 
	 * @param image The image to render
	 * @param x     The x position of the image
	 * @param y     The y position of the image
	 */
	public void render(BufferedImage image, int x, int y) {
		if(onScreen(x, y, image.getWidth(), image.getHeight())) {
			drawRect(x, y, x + image.getWidth(), x + image.getHeight(), Color.PINK);
			int[] pos = screenSpace(x, y);
			g.drawImage(image.getScaledInstance((int) (image.getWidth() * zoom), (int) (image.getHeight() * zoom),
					Image.SCALE_FAST), pos[0], pos[1], null);
		}
	}

	/** Draw a point to the world */
	public void drawPoint(int x, int y, int radius, Color c) {
		// Todo: Onscreen check
		g.setColor(c);
		int[] pos = screenSpace(x, y);
		g.fillOval(pos[0], pos[1], radius, radius);
	}

	/** Draw a rectangle to the world */
	public void drawRect(int x, int y, int width, int height, Color c) {
		if(onScreen(x, y, width, height)) {
			g.setColor(c);
			int[] pos = screenSpace(x, y);
			g.drawRect(pos[0], pos[1], (int) (width * zoom), (int) (height * zoom));
		}
	}

	/** Draw a polygon to the world */
	public void drawPoly(Polygon p, Color c) {
		int[] xPoints = new int[p.xpoints.length];
		int[] yPoints = new int[p.ypoints.length];
		int nPoints = p.npoints;

		boolean onScreen = false;

		for(int i = 0;i < nPoints;i++) {
			if(onScreen(xPoints[i], yPoints[i]))
				onScreen = true;
			xPoints[i] = screenSpace(p.xpoints[i], p.ypoints[i])[0];
			yPoints[i] = screenSpace(p.xpoints[i], p.ypoints[i])[1];
		}
		if(onScreen) {
			g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 175));
			g.fillPolygon(xPoints, yPoints, nPoints);
			g.setColor(c);
			g.setStroke(new BasicStroke(5));
			g.drawPolygon(xPoints, yPoints, nPoints);
		}
	}

	/**
	 * The final step in rendering, draws the buffer to the graphics, offset for
	 * camera position
	 * 
	 * @param g The graphics to draw the buffer to
	 */
	public void finish(Graphics g) {
		g.drawImage(buffer.getSubimage(0, 0, window.getWidth(), window.getHeight()), 0, 0, null);
//		Logger.log("Rendered in {REND}");
	}

	/**
	 * Determines if a rectangle is on screen
	 * 
	 * @param x      Top-left corner
	 * @param y      Bottom-left corner
	 * @param width  Rectangle width
	 * @param height Rectangle height
	 * @return Whether the rectangle is on the screen
	 */
	private boolean onScreen(int x, int y, int width, int height) {
		return onScreen(x, y) || onScreen(x + width, y) || onScreen(x, y + height) || onScreen(x + width, y + height);
	}

	/**
	 * Determines if a point is on the screen
	 * 
	 * @param x X value of the point
	 * @param y Y value of the point
	 * @return Whether the point is on the screen
	 */
	private boolean onScreen(int x, int y) {
//		Logger.log(minX + "\t" + maxX + "\t" + minY + "\t" + maxY);
//		Logger.log(screenSpace(x, 0)[0] + "\t" + screenSpace(0, y)[1]);
		return (x > minX || x < maxX) && (y > minY || y < minY);
	}

	/**
	 * Project a pair of coordinates to World space
	 * 
	 * @param x The x coordinate, in screen space
	 * @param y The y coordinate, in screen space
	 * @return The coordinates, in world space
	 */
	public int[] worldSpace(int x, int y) {
		int newX = (int) ((x - this.x) / zoom);
		int newY = (int) ((y - this.y) / zoom);

		return new int[] { newX, newY };
	}

	/**
	 * Unproject a pair of coordinates to window space
	 * 
	 * @param x The x coordinate, in world space
	 * @param y The y coordinate, in world space
	 * @return The coordinates, in screen space
	 */
	public int[] screenSpace(int x, int y) {
		int newX = (int) (x * zoom) + this.x;
		int newY = (int) (y * zoom) + this.y;

		return new int[] { newX, newY };
	}

	/** Get the width of the camera buffer */
	public int getWidth() {
		return (buffer.getWidth());
	}

	/** Get the height of the camera buffer */
	public int getHeight() {
		return (buffer.getHeight());
	}

	public void translate(double x, double y) {
		x += x;
		y += y;
	}

	public void setPosition(int x, int y, double zoom) {
		this.x = x;
		this.y = y;
		this.zoom = zoom > 0 ? zoom : 0.1;
	}
}
