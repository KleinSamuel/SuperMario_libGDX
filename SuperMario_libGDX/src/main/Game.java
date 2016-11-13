package main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import handler.BoundedCamera;
import handler.Content;
import handler.GameStateManager;

public class Game implements ApplicationListener{

	public static final String TITLE = "SUPER MARIO";
	public static final int V_WIDTH = 640;
	public static final int V_HEIGHT = 480;
	public static final int SCALE = 2;
	public static final float STEP = 1 / 60f;
	
	private SpriteBatch sb;
	private BoundedCamera cam;
	private OrthographicCamera hudCam;
	
	private GameStateManager gsm;
	
	public static Content res;
	
	@Override
	public void create() {
		
		
		res = new Content();
		
		cam = new BoundedCamera();
		cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		hudCam = new OrthographicCamera();
		hudCam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		
		sb = new SpriteBatch();
		gsm = new GameStateManager(this);
		
	}
	
	@Override
	public void render() {
		
		Gdx.graphics.setTitle(TITLE + " --FPS: "+Gdx.graphics.getFramesPerSecond());
		
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render();
		
	}
	
	@Override
	public void dispose() {
		res.removeAll();
	}
	
	@Override
	public void resize(int arg0, int arg1) {}
	
	@Override
	public void pause() {}
	
	@Override
	public void resume() {}
	
	public SpriteBatch getSpriteBatch(){
		return this.sb;
	}
	
	public BoundedCamera getCam(){
		return this.cam;
	}
	
	public OrthographicCamera getHudCam(){
		return this.hudCam;
	}
	
}
