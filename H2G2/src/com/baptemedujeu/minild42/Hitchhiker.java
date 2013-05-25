package com.baptemedujeu.minild42;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.Ray;
import com.jackamikaz.gameengine.DisplayedEntity;
import com.jackamikaz.gameengine.Engine;
import com.jackamikaz.gameengine.InputEntity;
import com.jackamikaz.gameengine.UpdatedEntity;
import com.jackamikaz.gameengine.utils.DisplayOrder;

public class Hitchhiker implements DisplayedEntity,UpdatedEntity, InputEntity {

	private Sprite sprite;
	private Texture var_tex;
	private Texture var_thumb;
	private Vector2 var_playerpos;
	private Vector2 var_thumbTarget;
	
	public Hitchhiker() 
	{
		// register
		Engine.DisplayMaster().Add(this);
		Engine.UpdateMaster().Add(this);
		Engine.InputMaster().Add(this);
		
		// TODO Auto-generated method stub
		var_tex = Engine.ResourceManager().GetTexture("hitchhiker");
		TextureRegion region = new TextureRegion(var_tex, 0, 0, 64, 64);
		sprite = new Sprite(region);

		var_thumb = Engine.ResourceManager().GetTexture("thumb");
		TextureRegion regionthumb = new TextureRegion(var_thumb, 0, 0, 64, 64);
		sprite = new Sprite(region);
		

		var_playerpos = new Vector2(0,0) ;
		var_thumbTarget = new Vector2(0,0) ;
		
		sprite.setSize(1, sprite.getHeight() / sprite.getWidth());
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);

		var_playerpos.x = -sprite.getWidth() / 2;
		var_playerpos.y = -sprite.getHeight() / 2 ;
		
		sprite.setPosition(var_playerpos.x , var_playerpos.y);
	}

	@Override
	public void Display(float lerp) {
		
		SpriteBatch batch = Engine.Batch();
		batch.begin();
		sprite.setPosition(var_playerpos.x  -sprite.getWidth() / 2 ,
												var_playerpos.y  -sprite.getHeight() / 2 ) ;
		//System.out.println("sprite.getHeight()" + sprite.getHeight());
		
		//draw
		sprite.draw(batch);		
		batch.end();
	}
	
	@Override
	public int GetDisplayRank() {
		return DisplayOrder.Render2D.ordinal();
	}

	@Override
	public void Update(float deltaT) {
		
	}

	@Override
	public void NewInput(Input input) {
		// TODO Auto-generated method stub
		
		if(input.isTouched())
		{
			var_playerpos.x = input.getX() ;
			var_playerpos.y = input.getY() ;
			
			Ray r = H2G2Game.camera.getPickRay(var_playerpos.x, var_playerpos.y);

			var_playerpos.x =r.origin.x ;
			var_playerpos.y =r.origin.y ;
		}
	}
	
}
