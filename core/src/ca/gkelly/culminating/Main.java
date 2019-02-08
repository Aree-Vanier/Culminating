package ca.gkelly.culminating;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import ca.gkelly.culminating.entities.Ship;
import ca.gkelly.culminating.loader.Loader;
import ca.gkelly.culminating.loader.MountSource;

public class Main extends ApplicationAdapter implements InputProcessor{
	SpriteBatch batch;
	Texture img;
	
	Ship test;
	
	OrthographicCamera camera;
	
	TiledMap map;
	TiledMapRenderer mapRenderer;
	
	ShapeRenderer sr; 
	
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	
	boolean showRect = false;
	
	@Override
	public void create () {
		
		camera = new OrthographicCamera(WIDTH, HEIGHT);
		camera.setToOrtho(false, WIDTH, HEIGHT);
		camera.update();
		
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		
		Loader.load();
		
		MountSource m = Loader.mounts.get(0);
		
		MountSource[] mounts = {m,m,m};
		
		test = Loader.vessels.get(0).build(mounts);
		
		map = Loader.maps.get(0);
		
		mapRenderer = new OrthogonalTiledMapRenderer(map);
		
		Gdx.input.setInputProcessor(this);
		
		sr = new ShapeRenderer();
	}

	@Override
	public void render () {
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		mapRenderer.setView(camera);
		mapRenderer.render();
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		if(showRect) {
			sr.begin(ShapeType.Filled);
			sr.setColor(1,0,0,0.1f);
			
			Vector3 pos = camera.project(new Vector3(test.rect.x, test.rect.y, 0));
			
			sr.rect(pos.x, pos.y, test.rect.getWidth(), test.rect.getHeight());
			sr.end();

			System.out.println(test.rect.getWidth() +"\t"+ test.rect.getHeight());
		}
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
		if(camera.zoom < 0.1) camera.zoom = 0.1f;
		
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		Loader.dispose();
	}
	
	boolean getKey(int key) {
		return(Gdx.input.isKeyPressed(key));
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector2 mousePos = new Vector2(camera.unproject(new Vector3(screenX, screenY, 0)).x, camera.unproject(new Vector3(screenX, screenY, 0)).y);
		System.out.println(mousePos);
		
		System.out.println(test.isClicked(mousePos));
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {

		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		showRect = !showRect;
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}
}
