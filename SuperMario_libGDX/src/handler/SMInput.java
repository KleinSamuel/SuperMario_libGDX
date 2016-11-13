package handler;

public class SMInput {

	public static int x;
	public static int y;
	public static boolean down;
	public static boolean pdown;
	
	public static boolean[] keys;
	public static boolean[] pkeys;
	public static final int NUM_KEYS = 3;
	public static final int BUTTON_LEFT = 0;
	public static final int BUTTON_RIGHT = 1;
	public static final int BUTTON_UP = 2;
	
	static {
		keys = new boolean[NUM_KEYS];
		pkeys = new boolean[NUM_KEYS];
	}
	
	public static void update(){
		pdown = down;
		for (int i = 0; i < NUM_KEYS; i++) {
			pkeys[i] = keys[i];
		}
	}
	
	public static boolean isDown(){
		return down;
	}
	
	public static boolean isPressed(){
		return down && !pdown;
	}
	
	public static boolean isReleased(){
		return !down && pdown;
	}
	
	public static boolean isDown(int i){
		return keys[i];
	}
	
	public static boolean isPressed(int i){
		return keys[i] && !pkeys[i];
	}
	
	public static void setkey(int i, boolean b){
		keys[i] = b;
	}
}
