package ca.gkelly.culminating;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ca.gkelly.culminating.entities.Ship;
import ca.gkelly.culminating.loader.Loader;
import ca.gkelly.culminating.loader.MountSource;

public class Main extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	
	Ship test;
	
	OrthographicCamera camera;
	
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	
	@Override
	public void create () {
		
		camera = new OrthographicCamera(WIDTH, HEIGHT);
		camera.setToOrtho(false, WIDTH, HEIGHT);
		
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		
		
		Loader.load();
		
		MountSource m = Loader.mounts.get(0);
		
		MountSource[] mounts = {m,m,m};
		
		test = Loader.vessels.get(0).build(mounts);
		
	}

	@Override
	public void render () {
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
//		batch.draw(img, 0, 0);
		
		test.render(batch);
		
		batch.end();
		
		int cameraX = 0;
		int cameraY = 0;
		float cameraZoom = 0;
		
		if(getKey(Input.Keys.LEFT) || getKey(Input.Keys.A))
			cameraX --;
		if(getKey(Input.Keys.RIGHT) || getKey(Input.Keys.D))
			cameraX ++;
		if(getKey(Input.Keys.UP) || getKey(Input.Keys.W))
			cameraY ++;
		if(getKey(Input.Keys.DOWN) || getKey(Input.Keys.S))
			cameraY --;
		if(getKey(Input.Keys.PAGE_UP) || getKey(Input.Keys.Q))
			cameraZoom +=0.01;
		if(getKey(Input.Keys.PAGE_DOWN) || getKey(Input.Keys.E))
			cameraZoom -=0.01;
		
		camera.translate(cameraX*camera.zoom, cameraY*camera.zoom);
		camera.zoom += cameraZoom;
		if(camera.zoom < 0.5) camera.zoom = 0.5f;
		camera.update();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
	
	boolean getKey(int key) {
		return(Gdx.input.isKeyPressed(key));
	}
}
