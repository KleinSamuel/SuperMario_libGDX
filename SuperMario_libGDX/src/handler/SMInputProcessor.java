package handler;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;

public class SMInputProcessor extends InputAdapter{

	public boolean mouseMoved(int x, int y){
		SMInput.x = x;
		SMInput.y = y;
		return true;
	}
	
	public boolean touchDragged(int x, int y, int pointer){
		SMInput.x = x;
		SMInput.y = y;
		SMInput.down = true;
		return true;
	}
	
	public boolean touchDown(int x, int y, int pointer, int button){
		SMInput.x = x;
		SMInput.y = y;
		SMInput.down = true;
		return true;
	}
	
	public boolean touchUp(int x, int y, int pointer, int button){
		SMInput.x = x;
		SMInput.y = y;
		SMInput.down = false;
		return true;
	}
	
	public boolean keyDown(int keycode) {
		if(keycode == Keys.W){
			SMInput.setkey(SMInput.BUTTON_UP, true);
		}
		if(keycode == Keys.A){
			SMInput.setkey(SMInput.BUTTON_LEFT, true);
		}
		if(keycode == Keys.D){
			SMInput.setkey(SMInput.BUTTON_RIGHT, true);
		}
		return true;
	}
	
	public boolean keyUp(int keycode) {
		if(keycode == Keys.W){
			SMInput.setkey(SMInput.BUTTON_UP, false);
		}
		if(keycode == Keys.A){
			SMInput.setkey(SMInput.BUTTON_LEFT, false);
		}
		if(keycode == Keys.D){
			SMInput.setkey(SMInput.BUTTON_RIGHT, false);
		}
		return true;
	}
	
}
