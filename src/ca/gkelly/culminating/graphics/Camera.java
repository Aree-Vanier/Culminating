package ca.gkelly.culminating.graphics;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Camera {
	
	double zoom;
	int x;
	int y;
	
	double renderDist = 1.1;
	BufferedImage buffer;
	Graphics2D g;
	Container window;
	TiledMap map;
	
	int maxX, maxY, minX, minY;
	
	/**Create a new Camera
	 * @param window The container that will hold the camera
	 * @param map The {@link TiledMap} that will be used as the background*/
	public Camera(Container window, TiledMap map){
		this.window = window;
		this.map = map;
	}
	
	/**First step in rendering process, prepares buffer, graphics and min/max values<br/>
	 * Draws map to buffer*/
	public void begin() {
		buffer = new BufferedImage(window.getWidth(), window.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		g = (Graphics2D) buffer.getGraphics();

		minX = (int) (x-(window.getWidth()*(renderDist-0.5)));
		maxX = (int) (x+(window.getWidth()*(renderDist-0.5)));

		minY = (int) (y-(window.getHeight()*(renderDist-0.5)));
		maxY = (int) (y+(window.getHeight()*(renderDist-0.5)));
		
		map.render(this);
	}
	
	/**Render an image to the camera
	 * @param image The image to render
	 * @param x The x position of the image
	 * @param y The y position of the image*/
	public void render(BufferedImage image, int x, int y) {
		if(x+image.getWidth() > minX && x < maxX && y+image.getHeight() > minY && y < maxY) {
			g.drawImage(image, x, y, null);
		}
	}
	
	/**The final step in rendering, draws the buffer to the graphics, offset for camera position
	 * @param g The graphics to draw buffer to*/
	public void finish(Graphics g) {
		g.drawImage(buffer, (int) x, (int) y, null);
	}
	
	/**Project a pair of coordinates to World space
	 * @param x The x coordinate, in screen space
	 * @param y The y coordinate, in screen space
	 * @return The coordinates, in world space*/
	public int[] project(int x, int y) {
		return null;
	}

	/**Unproject a pair of coordinates to window space
	 * @param x The x coordinate, in world space
	 * @param y The y coordinate, in world space
	 * @return The coordinates, in screen space*/
	public int[] unProject(int x, int y) {
		return null;
	}
	
	/**Get the width of the camera buffer*/
	public int getWidth() {
		return(buffer.getWidth());
	}
	
	/**Get the height of the camera buffer*/
	public int getHeight() {
		return(buffer.getHeight());
	}
	
	public void translate(double x, double y) {
		x+=x;
		y+=y;
	}
	
	public void setPosition(int x, int y, double zoom) {
		this.x = x;
		this.y = y;
		this.zoom = zoom;
	}
}
