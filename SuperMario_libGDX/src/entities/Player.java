package entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

import main.Game;

public class Player extends B2DSprite{

	private int numCoins;
	private int totalCoins;
	
	public Player(Body body) {
		super(body);
		
		numCoins = 0;
		totalCoins = 0;
		
		Texture tex = Game.res.getTexture("mario_small_right");
		TextureRegion[] sprites = new TextureRegion[4];
		for (int i = 0; i < sprites.length; i++) {
			sprites[i] = new TextureRegion(tex, i * 32, 0, 32, 32);
		}
		
		animation.setFrames(sprites, 1 / 12f);

		width = sprites[0].getRegionWidth();
		height = sprites[0].getRegionHeight();
	}

	public void collectCoin(){
		this.numCoins++;
	}
	
	public int getNumCoins(){
		return this.numCoins;
	}
	
	public void setTotalCoins(int i){
		this.totalCoins = i;
	}
	
	public int getTotalCoins(){
		return this.totalCoins;
	}
	
}
