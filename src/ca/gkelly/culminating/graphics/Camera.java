package ca.gkelly.culminating.graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Camera {
	
	double zoom;
	double x;
	double y;
	
	double renderDist = 1.1;
	BufferedImage buffer;
	Graphics2D g;
	JPanel window;
	
	int maxX, maxY, minX, minY;
	
	public Camera(JPanel window){
		this.window = window;
	}
	
	/**First step in rendering process, prepares buffer, graphics and min/max values*/
	public void begin() {
		buffer = new BufferedImage(window.getWidth(), window.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		g = (Graphics2D) buffer.getGraphics();

		minX = (int) (x-(window.getWidth()*(renderDist-0.5)));
		maxX = (int) (x+(window.getWidth()*(renderDist-0.5)));

		minY = (int) (y-(window.getHeight()*(renderDist-0.5)));
		maxY = (int) (y+(window.getHeight()*(renderDist-0.5)));
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
	
	public void translate(double x, double y) {
		x+=x;
		y+=y;
	}
}
