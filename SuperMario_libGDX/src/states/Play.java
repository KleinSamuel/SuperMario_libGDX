package states;

import static handler.B2DVariables.PPM;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import entities.Player;
import handler.B2DVariables;
import handler.BoundedCamera;
import handler.GameStateManager;
import handler.SMContactListener;
import handler.SMInput;
import main.Game;

public class Play extends GameState{

	private boolean debug = true;
	
	private World world;
	private Box2DDebugRenderer debugRenderer;
	private SMContactListener contactListener;
	private BoundedCamera boundedCam;
	
	private TiledMap tileMap;
	private int tileSize;
	private int tileMapWidth;
	private int tileMapHeight;
	private OrthogonalTiledMapRenderer tileMapRenderer;
	
	private Player player;
	
	public static int level;
	
	public Play(GameStateManager gsm) {
		super(gsm);
		
		world = new World(new Vector2(0, -7f), true);
		contactListener = new SMContactListener();
		world.setContactListener(contactListener);
		debugRenderer = new Box2DDebugRenderer();
		
		createPlayer();
		
		createWalls();
		cam.setBounds(0, tileMapWidth * tileSize, 0, tileMapHeight * tileSize);
		
		boundedCam = new BoundedCamera();
		boundedCam.setToOrtho(false, Game.V_WIDTH / PPM, Game.V_HEIGHT / PPM);
		boundedCam.setBounds(0, (tileMapWidth * tileSize) / PPM, 0, (tileMapHeight * tileSize) / PPM);
	}

	@Override
	public void handleInput() {
	
		if(SMInput.isPressed(SMInput.BUTTON_UP)){
			playerJump();
		}
		if(SMInput.isPressed(SMInput.BUTTON_LEFT)){
			playerMove(true);
		}
		if(SMInput.isPressed(SMInput.BUTTON_RIGHT)){
			playerMove(false);
		}
	}

	@Override
	public void update(float dt) {
		
		handleInput();
		
		world.step(Game.STEP, 1, 1);
		
		player.update(dt);
	}

	@Override
	public void render() {
		
		Gdx.gl20.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		cam.setPosition(player.getPosition().x / PPM + Game.V_WIDTH / 4, Game.V_HEIGHT / 2);
		cam.update();
		
		tileMapRenderer.setView(cam);
		tileMapRenderer.render();
		
		/* draw player */
		sb.setProjectionMatrix(cam.combined);
		player.render(sb);
		
		if(debug){
			debugRenderer.render(world, boundedCam.combined);
		}
	}

	@Override
	public void dispose() {}
	
	
	
	
	private void createPlayer(){
		
		BodyDef bdef = new BodyDef();
		bdef.type = BodyType.DynamicBody;
		bdef.position.set(100 / PPM, 200 / PPM);
		bdef.fixedRotation = true;
		bdef.linearVelocity.set(0,0);
		
		Body body = world.createBody(bdef);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(13 / PPM, 13 / PPM);
		
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.density = 1;
		fdef.friction = 0;
		fdef.filter.categoryBits = B2DVariables.BIT_PLAYER;
		fdef.filter.maskBits = B2DVariables.BIT_GROUND;
		
		body.createFixture(fdef).setUserData("player");
		shape.dispose();
		
		shape = new PolygonShape();
		shape.setAsBox(13 / PPM, 2 / PPM, new Vector2(0, -13 / PPM), 0);
		
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVariables.BIT_PLAYER;
		fdef.filter.maskBits = B2DVariables.BIT_GROUND;
		fdef.isSensor = true;
		
		body.createFixture(fdef).setUserData("foot");
		shape.dispose();
		
		player = new Player(body);
		body.setUserData("player");
		
		MassData md = body.getMassData();
		md.mass = 1;
		body.setMassData(md);
	}
	
	
	private void playerJump(){
		if(contactListener.isPlayerOnGround()){
			player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, 0);
			player.getBody().applyForceToCenter(0,  200, true);
		}
	}
	
	private void playerMove(boolean left){
		if(left){
			player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, 0);
			player.getBody().applyForceToCenter(-10,  0, true);
		}else{
			player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, 0);
			player.getBody().applyForceToCenter(10,  0, true);
		}
	}

	private void createWalls(){
		
		tileMap = new TmxMapLoader().load("res/level/testLevel.tmx");
		tileMapRenderer = new OrthogonalTiledMapRenderer(tileMap);
		
		tileMapWidth = (int) tileMap.getProperties().get("width");
		tileMapHeight = (int) tileMap.getProperties().get("height");
		tileSize = (int) tileMap.getProperties().get("tilewidth");
		
		TiledMapTileLayer layer;
		
		layer = (TiledMapTileLayer) tileMap.getLayers().get("ground");
		createGroundBlocks(layer, B2DVariables.BIT_GROUND);
		
	}
	
	private void createGroundBlocks(TiledMapTileLayer layer, short bits){
		
		float ts = layer.getTileWidth();
		
		for (int col = 0; col < layer.getHeight(); col++) {
			for (int row = 0; row < layer.getWidth(); row++) {
				
				Cell cell = layer.getCell(row, col);
				
				if(cell == null) continue;
				if(cell.getTile() == null) continue;
				
				BodyDef bdef = new BodyDef();
				bdef.type = BodyType.StaticBody;
				bdef.position.set((row + 0.5f) * ts / PPM, (col + 0.5f) * ts / PPM);
				
				ChainShape cshape = new ChainShape();
				Vector2[] v = new  Vector2[3];
				v[0] = new Vector2(-tileSize / 2 / PPM, -tileSize / 2 / PPM);
				v[1] = new Vector2(-tileSize / 2 / PPM, tileSize / 2 / PPM);
				v[2] = new Vector2(tileSize / 2 / PPM, tileSize / 2 / PPM);
				
				cshape.createChain(v);
				
				FixtureDef fdef = new FixtureDef();
				fdef.friction = 0;
				fdef.shape = cshape;
				fdef.filter.categoryBits = bits;
				fdef.filter.maskBits = B2DVariables.BIT_PLAYER;
				fdef.isSensor = false;
				
				world.createBody(bdef).createFixture(fdef);
				cshape.dispose();
			}
		}
		
	}
}
