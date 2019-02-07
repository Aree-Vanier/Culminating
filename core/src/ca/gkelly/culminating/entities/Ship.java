package ca.gkelly.culminating.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

import ca.gkelly.culminating.loader.VesselSource;

public class Ship extends Entity{

	Mount[] mounts;
	
	Sprite sprite;
	
	public Ship(VesselSource v, int x, int y, Mount[] m) {
		super(x,y,v.texture);
		mounts = m;
	}

	@Override
	public void render(SpriteBatch b) {
		if(sprite == null) reRender();
		
		b.draw(sprite, x, y);
	}
	
	private void reRender() {
		
		SpriteBatch b = new SpriteBatch();
		FrameBuffer fbo = new FrameBuffer(Format.RGBA8888, 256*4,128*4,false);
		
		fbo.begin();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        b.begin();
		b.draw(texture, 0,0);
		for(Mount m : mounts) {
			m.render(b);
		}
		b.end();
		
		fbo.end();
		
		sprite = new Sprite(fbo.getColorBufferTexture());
		sprite.flip(false,  true);
	}

	@Override
	public void update() {
		
	}

}
