package ca.gkelly.culminating.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ca.gkelly.culminating.loader.VesselSource;

public class Ship extends Entity{

	Mount[] mounts;
	
	SpriteBatch batch;
	
	public Ship(VesselSource v, int x, int y, Mount[] m) {
		super(x,y,v.texture);
		mounts = m;
	}

	@Override
	public void render() {
		batch.begin();
		batch.draw(texture, 0,0);
		for(Mount m : mounts) {
			m.render(batch);
		}
		batch.end();
	}

	@Override
	public void update() {
		
	}

}
