package ca.gkelly.culminating.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.imageio.ImageTypeSpecifier;

import ca.gkelly.culminating.graphics.Camera;
import ca.gkelly.culminating.loader.VesselSource;

public class Ship extends Entity{

	Mount[] mounts;
	
	BufferedImage texture;
	

	
	public Ship(VesselSource v, int x, int y, Mount[] m) {
		super(x,y,v.texture);
		mounts = m;
	}

	@Override
	public void render(Camera c) {
		if(texture == null) reRender();
		for(Mount m : mounts) {
			if(m.getRenderRequest()) {
				reRender();
			}
		}
		c.render(texture, x, y);		
	}
	
	private void reRender() {
		
		texture = new BufferedImage(baseTexture.getWidth(), baseTexture.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = texture.getGraphics();
		g.drawImage(baseTexture, 0,0, null);
		for(Mount m : mounts) {
			m.render(g);
		}

		rect = new Rectangle(x, y, texture.getWidth(null), texture.getHeight(null));
		
	}
 
	@Override
	public void update() {
		rect.setLocation(x, y);
	}
	
	
	//TODO: Figure out terrain stuff
//	public void move(int x, int y, MapObjects terrain) {
//		boolean canMove = true;
//		for(PolygonMapObject p : terrain.getByType(PolygonMapObject.class)) {
//			if(p.getPolygon().contains(new Vector2(x, y))) {
//				canMove = false;
//			}
//		}
//		
//	}
	
}
