package ca.gkelly.culminating.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.math.Vector2;

import ca.gkelly.culminating.loader.VesselSource;

public class Ship extends Entity{

	Mount[] mounts;
	
	Sprite sprite;
	

	
	SpriteBatch b;
	FrameBuffer fbo;
	OrthographicCamera bCam;
	
	public Ship(VesselSource v, int x, int y, Mount[] m) {
		super(x,y,v.texture);
		mounts = m;
		
		b = new SpriteBatch();
		fbo = new FrameBuffer(Format.RGBA8888, texture.getWidth(),texture.getHeight(),false);
		bCam = new OrthographicCamera(fbo.getWidth(), fbo.getHeight());
		bCam.position.set(fbo.getWidth()/2, fbo.getHeight()/2, 0);
		bCam.update();
		
		
	}

	@Override
	public void render(SpriteBatch b) {
		if(sprite == null) reRender();
		for(Mount m : mounts) {
			if(m.getRenderRequest()) {
				reRender();
			}
		}
		b.draw(sprite, x, y);		
	}
	
	private void reRender() {
		
		fbo.begin();
		Gdx.gl.glClearColor(0, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		b.setProjectionMatrix(bCam.combined);
        b.begin();
        b.setColor(1, 1, 1, 1);
		b.draw(texture, 0,0);
		for(Mount m : mounts) {
			m.render(b);
		}
		b.end();
		
		fbo.end();
		
		sprite = new Sprite(fbo.getColorBufferTexture());
		sprite.flip(false,  true);
		
		//TODO: Find proper fix to bounding box
		rect = sprite.getBoundingRectangle();//new Rectangle(x, y, sprite.getWidth()/2, sprite.getHeight()/3);
		
		System.out.println("SPRITE:"+sprite.getWidth() +"\t"+ sprite.getHeight());
	}
 
	@Override
	public void update() {
		rect.setPosition(new Vector2(x,y));
	}
	
	public void move(int x, int y, MapObjects terrain) {
		boolean canMove = true;
		for(PolygonMapObject p : terrain.getByType(PolygonMapObject.class)) {
			if(p.getPolygon().contains(new Vector2(x, y))) {
				canMove = false;
			}
		}
		
	}
	
}
