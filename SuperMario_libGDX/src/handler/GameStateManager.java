package handler;

import java.util.Stack;

import main.Game;
import states.GameState;
import states.Play;

public class GameStateManager {

	private Game game;
	
	private Stack<GameState> gameStates;
	
	public static final int MENU = 12345678;
	public static final int PLAY = 22345678;
	public static final int LEVEL_SELECT = 32345678;
	
	
	public GameStateManager(Game game) {
		this.game = game;
		gameStates = new Stack<>();
		pushState(MENU);
	}
	
	public void update(float dt) {
		gameStates.peek().update(dt);
	}
	
	public void render() {
		gameStates.peek().render();
	}
	
	private GameState getState(int state) {
//		if(state == MENU){
//			return new Menu(this);
//		}
		if(state == PLAY){
			return new Play(this);
		}
//		if(state == LEVEL_SELECT){
//			return new LevelSelect(this);
//		}
		return null;
	}
	
	public Game game(){ 
		return game; 
	}
	
	public void setState(int state) {
		popState();
		pushState(state);
	}
	
	public void pushState(int state) {
		gameStates.push(getState(state));
	}
	
	public void popState() {
		GameState g = gameStates.pop();
		g.dispose();
	}
}
