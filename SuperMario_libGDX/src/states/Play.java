package states;

import static handler.B2DVariables.PPM;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import handler.B2DVariables;
import handler.BoundedCamera;
import handler.GameStateManager;
import main.Game;

public class Play extends GameState{

	private boolean debug = true;
	
	private World world;
	private Box2DDebugRenderer debugRenderer;
//	private SMContactListener contactListener;
	private BoundedCamera boundedCam;
	
	private TiledMap tileMap;
	private int tileSize;
	private int tileMapWidth;
	private int tileMapHeight;
	private OrthogonalTiledMapRenderer tileMapRenderer;
	
	public static int level;
	
	public Play(GameStateManager gsm) {
		super(gsm);
		
		world = new World(new Vector2(0, 7f), true);
		
		debugRenderer = new Box2DDebugRenderer();
		
		createWalls();
		cam.setBounds(0, tileMapWidth * tileSize, 0, tileMapHeight * tileSize);
		
		
		boundedCam = new BoundedCamera();
		boundedCam.setToOrtho(false, Game.V_WIDTH / PPM, Game.V_HEIGHT / PPM);
		boundedCam.setBounds(0, (tileMapWidth * tileSize) / PPM, 0, (tileMapHeight * tileSize) / PPM);
	}

	@Override
	public void handleInput() {
		// TODO Auto-generated method stub
	}

	@Override
	public void update(float dt) {
		
		world.step(Game.STEP, 1, 1);
		
	}

	@Override
	public void render() {
		
		tileMapRenderer.setView(cam);
		tileMapRenderer.render();
		
		if(debug){
			debugRenderer.render(world, boundedCam.combined);
		}
	}

	@Override
	public void dispose() {}

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
