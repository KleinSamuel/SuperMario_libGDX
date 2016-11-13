package handler;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class SMContactListener implements ContactListener{

	private int numFootContacts;
	private boolean playerDead;
	
	public SMContactListener() {
		super();
	}
	
	@Override
	public void beginContact(Contact c) {
		
		Fixture fa = c.getFixtureA();
		Fixture fb = c.getFixtureB();
		
		if(fa == null || fb == null) return;
		
		if(fa.getUserData() != null && fa.getUserData().equals("foot")){
			numFootContacts++;
		}
		if(fb.getUserData() != null && fb.getUserData().equals("foot")){
			numFootContacts++;
		}
		
	}

	@Override
	public void endContact(Contact c) {
		
		Fixture fa = c.getFixtureA();
		Fixture fb = c.getFixtureB();
		
		if(fa == null || fb == null) return;
		
		if(fa.getUserData() != null && fa.getUserData().equals("foot")){
			numFootContacts--;
		}
		if(fb.getUserData() != null && fb.getUserData().equals("foot")){
			numFootContacts--;
		}
		
	}
	
	public boolean isPlayerOnGround(){
		return numFootContacts > 0;
	}
	
	public boolean isPlayerDead(){
		return playerDead;
	}

	@Override
	public void postSolve(Contact arg0, ContactImpulse arg1) {}
	@Override
	public void preSolve(Contact arg0, Manifold arg1) {}

}
