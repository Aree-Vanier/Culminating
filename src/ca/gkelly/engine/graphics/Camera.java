package ca.gkelly.engine.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

import ca.gkelly.engine.util.Logger;
import ca.gkelly.engine.util.Tools;
import ca.gkelly.engine.util.Vertex;

/**
 * 2D world-space camera, allows translation and zoom<br/>
 * To use place all draw calls between {@link #begin()} and
 * {@link #finish(Graphics) finish()}
 */
public class Camera {

	static final double MIN_ZOOM = 0.1;
	static final double MAX_ZOOM = 10;

	double zoom = 1;
	int x = 0;
	int y = 0;
	int rawX = 0;
	int rawY = 0;

	int centreX;
	int centreY;

	int renderDist = 10;
	BufferedImage buffer;
	Graphics2D g;
	Container window;
	TileMap map;

	int maxX, maxY, minX, minY;

	/**
	 * Create a new Camera
	 * 
	 * @param window The container that will hold the camera
	 * @param map    The {@link TileMap} that will be used as the background
	 */
	public Camera(Container window, TileMap map) {
		this.window = window;
		this.map = map;
		// Set to initial position
		buffer = new BufferedImage(window.getWidth(), window.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		setPosition(rawX, rawY, 1);
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

		centreX = buffer.getWidth() / 2;
		centreY = buffer.getHeight() / 2;

		// Create a logger epoch
		Logger.epoch("REND");

		// Render the map
		Object[] mapRender = map.render(worldSpace(0, 0), worldSpace(buffer.getWidth(), buffer.getHeight()));
		render((BufferedImage) mapRender[0], (int) (double) mapRender[1], (int) (double) mapRender[2]);
//		Logger.log("Map rendered in {REND}");

	}

	/**
	 * Render an image to the camera
	 * 
	 * @param image The image to render
	 * @param x     The x position of the image
	 * @param y     The y position of the image
	 */
	public void render(BufferedImage image, int x, int y) {
		if (onScreen(x, y, image.getWidth(), image.getHeight())) {
//			drawRect(x, y, x + image.getWidth(), x + image.getHeight(), Color.PINK);
			Vertex pos = screenSpace(x, y);
			g.drawImage(image.getScaledInstance((int) (image.getWidth() * zoom), (int) (image.getHeight() * zoom),
					Image.SCALE_FAST), (int) pos.x, (int) pos.y, null);
		}
	}

	/**
	 * Draw a point to the world
	 * 
	 * @param x      The x position
	 * @param y      The y position
	 * @param radius The radius
	 * @param c      The color to use
	 */
	public void drawPoint(int x, int y, int radius, Color c) {
		// TODO: Onscreen check
		g.setColor(c);
		Vertex pos = screenSpace(x, y);
		g.fillOval((int) pos.x - radius / 2, (int) pos.y - radius / 2, (int) (radius), (int) (radius));
	}

	/**
	 * Draw a line to the world
	 * 
	 * @param x1    The start x position
	 * @param y1    The start y position
	 * @param x2    The stop x position
	 * @param y2    The stop y position
	 * @param width The width of the line
	 * @param c     The color to use
	 */
	public void drawLine(int x1, int y1, int x2, int y2, float width, Color c) {
		// TODO: Onscreen check
		g.setColor(c);
		Vertex pos1 = screenSpace(x1, y1);
		Vertex pos2 = screenSpace(x2, y2);
		Stroke oldStroke = g.getStroke();
		g.setStroke(new BasicStroke((float) (width * zoom)));
		g.drawLine((int) pos1.x, (int) pos1.y, (int) pos2.x, (int) pos2.y);
		g.setStroke(oldStroke);
	}

	/**
	 * Draw a line to the world
	 * 
	 * @param v1    The start {@link Vertex}
	 * @param v2    The stop {@link Vertex}
	 * @param width The width of the line
	 * @param c     The color to use
	 */
	public void drawLine(Vertex v1, Vertex v2, float width, Color c) {
		drawLine((int) v1.x, (int) v1.y, (int) v2.x, (int) v2.y, width, c);
	}

	/**
	 * Draw a rectangle to the world
	 * 
	 * @param x      The x position
	 * @param y      The y position
	 * @param width  The width of the rectangle
	 * @param height The height of the rectangle
	 * @param c      The color to use
	 */
	public void drawRect(int x, int y, int width, int height, Color c) {
		if (onScreen(x, y, width, height)) {
			g.setColor(c);
			Vertex pos = screenSpace(x, y);
			g.drawRect((int) pos.x, (int) pos.y, (int) (width * zoom), (int) (height * zoom));
		}
	}

	/**
	 * Draw a polygon to the world
	 * 
	 * @param p The polygon
	 * @param c The color to use
	 */
	public void drawPoly(Polygon p, Color c) {
		int[] xPoints = new int[p.xpoints.length];
		int[] yPoints = new int[p.ypoints.length];
		int nPoints = p.npoints;

		boolean onScreen = false;

		for (int i = 0; i < nPoints; i++) {
			if (onScreen(xPoints[i], yPoints[i]))
				onScreen = true;
			xPoints[i] = (int) screenSpace(p.xpoints[i], p.ypoints[i]).x;
			yPoints[i] = (int) screenSpace(p.xpoints[i], p.ypoints[i]).y;
		}
		if (onScreen) {
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
		Logger.log("Rendered in {REND}");
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
	public boolean onScreen(int x, int y, int width, int height) {
		return onScreen(x, y) || onScreen(x + width, y) || onScreen(x, y + height) || onScreen(x + width, y + height);
	}

	/**
	 * Determines if a point is on the screen
	 * 
	 * @param x X value of the point
	 * @param y Y value of the point
	 * @return Whether the point is on the screen
	 */
	public boolean onScreen(double x, double y) {
		return (x > minX || x < maxX) && (y > minY || y < minY);
	}

	/**
	 * Determines if a {@link Vertex} is on the screen
	 * 
	 * @param v The {@link Vertex} to check
	 * @return Whether the {@link Vertex} is on the screen
	 */
	public boolean onScreen(Vertex v) {
		return onScreen(v.x, v.y);
	}

	/**
	 * Project a pair of coordinates to World space
	 * 
	 * @param x The x coordinate, in screen space
	 * @param y The y coordinate, in screen space
	 * @return The coordinates, in world space
	 */
	public Vertex worldSpace(double x, double y) {
		return new Vertex((x - this.x) / zoom, (y - this.y) / zoom);
	}

	/**
	 * Project a {@link Vertex} to World space
	 * 
	 * @param v The {@link Vertex}, in screen space
	 * @return The coordinates, in world space
	 */
	public Vertex worldSpace(Vertex v) {
		return worldSpace(v.x, v.y);
	}

	/**
	 * Unproject a pair of coordinates to window space
	 * 
	 * @param x The x coordinate, in world space
	 * @param y The y coordinate, in world space
	 * @return The coordinates, in screen space
	 */
	public Vertex screenSpace(double x, double y) {
		return new Vertex((x * zoom) + this.x, (y * zoom) + this.y);
	}

	/***
	 * Unproject a {@link Vertex} to window space
	 * 
	 * @param v The {@link Vertex}, in world space
	 * @return The coordinates, in screen space
	 */
	public Vertex screenSpace(Vertex v) {
		return screenSpace(v.x, v.y);
	}

	/** Get the width of the camera buffer */
	public int getWidth() {
		return (buffer.getWidth());
	}

	/** Get the height of the camera buffer */
	public int getHeight() {
		return (buffer.getHeight());
	}

	/**
	 * Translate the camera
	 * 
	 * @param x The x offset
	 * @param y The y offset
	 */
	public void translate(int x, int y) {
		rawX += x;
		rawY += y;
		setPosition(rawX, rawY, this.zoom);
	}

	/**
	 * Change the camera's zoom
	 * 
	 * @param zoom The amount to change
	 */
	public void zoom(double zoom) {
		setPosition(rawX, rawY, this.zoom + zoom);
	}

	/**
	 * Set the camera position
	 * 
	 * @param x    The new x position
	 * @param y    The new y position
	 * @param zoom The new zoom
	 */
	public void setPosition(int x, int y, double zoom) {
		rawX = x;
		rawY = y;
		if (buffer == null)
			return;
		this.zoom = Tools.minmax(zoom, MIN_ZOOM, MAX_ZOOM);
		this.x = (int) ((-x + buffer.getWidth() / 2));
		this.y = (int) ((-y + buffer.getHeight() / 2));
		this.x -= (x * (zoom - 1));
		this.y -= (y * (zoom - 1));
	}

	/**
	 * Set the camera position, without changing the zoom
	 * 
	 * @param x The new x position
	 * @param y The new y position
	 */
	public void setPosition(int x, int y) {
		setPosition(x, y, zoom);
	}
}
