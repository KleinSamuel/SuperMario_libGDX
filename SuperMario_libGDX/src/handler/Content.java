package handler;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Content {

	private HashMap<String, Texture> textures;
	
	public Content() {
		textures = new HashMap<>();
	}
	
	public void loadTexture(String path){
		int slashIndex = path.lastIndexOf("/");
		
		String key;
		
		if(slashIndex == -1){
			key = path.substring(0, path.lastIndexOf("."));
		}else{
			key = path.substring(slashIndex + 1, path.lastIndexOf("."));
		}
		
		loadTexture(path, key);
	}
	
	public void loadTexture(String path, String key){
		Texture tex = new Texture(Gdx.files.internal(path));
		textures.put(key, tex);
	}
	
	public Texture getTexture(String key){
		return textures.get(key);
	}
	
	public void disposeTexture(String key){
		Texture tex = textures.get(key);
		if(tex != null){
			tex.dispose();
		}
	}
	
	public void removeAll() {
		for(Object o : textures.values()) {
			Texture tex = (Texture) o;
			tex.dispose();
		}
		textures.clear();
	}
}
