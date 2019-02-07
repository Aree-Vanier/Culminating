package ca.gkelly.culminating.entities;

import ca.gkelly.culminating.loader.VesselSource;

public class Ship extends Entity{

	Mount[] mounts;
	
	public Ship(VesselSource v, int x, int y, Mount[] m) {
		super(x,y,v.texture);
		mounts = m;
	}

	@Override
	public void render() {
		for(Mount m : mounts) {
			m.render();
		}
	}

	@Override
	public void update() {
		
	}

}
