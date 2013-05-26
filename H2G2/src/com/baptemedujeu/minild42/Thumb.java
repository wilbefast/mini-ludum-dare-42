package com.baptemedujeu.minild42;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.baptemedujeu.minild42.EntityQueryManager.Query;
import com.jackamikaz.gameengine.DisplayedEntity;
import com.jackamikaz.gameengine.Engine;
import com.jackamikaz.gameengine.SpatialEntity;
import com.jackamikaz.gameengine.UpdatedEntity;
import com.jackamikaz.gameengine.utils.DisplayOrder;

public class Thumb implements DisplayedEntity, UpdatedEntity {

	private Vector2 pos;
	private Vector2 direction;
	private SpatialEntity var_falltowards;
	private Hitchhiker var_origin;
	private Sprite sprite;
	
	private Query thingDistance;
	
	private static int thumbCount = 0;
	public static Boolean canThrow()
	{
		return thumbCount==0;
	}
	
	float time;
	
	public Thumb(float x, float y, float tx, float ty, float power, SpatialEntity fallto)
	{
		// register
		thumbCount++;
		Engine.DisplayMaster().Add(this);
		Engine.UpdateMaster().Add(this);
		
		// position
		pos = new Vector2(x, y);
		direction = new Vector2(tx,ty);
		direction.nor();
		direction.scl(power);
		var_falltowards = fallto;
		var_origin = (Hitchhiker) fallto;
		//Temporary
		time = 0;
		// sprite
		Texture t = Engine.ResourceManager().GetTexture("thumb");
		TextureRegion tr = new TextureRegion(t, 0, 0, 128, 128);
		sprite = new Sprite(tr);
		sprite.setSize(1.0f, 1.0f * sprite.getHeight() / sprite.getWidth());
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		sprite.setPosition(pos.x - sprite.getWidth() / 2, pos.y - sprite.getHeight() / 2);
		sprite.rotate(direction.angle());
		
		thingDistance = 
				new EntityQueryManager.Query(){
					@Override
					public float evaluate(UpdatedEntity e) {
						// TODO Auto-generated method stub
						if (e instanceof Spaceship || e instanceof Planet){
							SpatialEntity se = (SpatialEntity)e;
							return se.getPosition().dst(pos);
						}
						
						
						return Float.MAX_VALUE;
					}};
				//new EntityQueryManager.TypedDistanceQuery(pos, SpatialEntity.class);
		
	}
	
	@Override
	public void Update(float deltaT)
	{
		time = Math.min(time+deltaT, 1);
		if (time > 0.1 && pos.dst(var_falltowards.getPosition())<2) {
			thumbCount--;
			Engine.DisplayMaster().Remove(this);
			Engine.UpdateMaster().Remove(this);
		}
		
		SpatialEntity closest = (SpatialEntity) (EntityQueryManager.getMin(thingDistance));
		if (time > 0.1 && pos.dst(closest.getPosition())<2) {
			var_origin.setFalltowards(closest);
			thumbCount--;
			Engine.DisplayMaster().Remove(this);
			Engine.UpdateMaster().Remove(this);
		}
		
		Vector2 cpos = new Vector2(pos);
		Vector2 tpos = new Vector2(var_falltowards.getPosition());
		tpos.sub(cpos).scl(deltaT*10);
		//System.out.println(tpos);
		direction.add(tpos);
		direction.scl(1-deltaT);
		
		Vector2 theDir = new Vector2(direction);
		pos = pos.add(theDir.scl(deltaT));
	}
	

	@Override
	public void Display(float lerp)
	{
		SpriteBatch batch = Engine.Batch();
		batch.begin();
			sprite.setPosition(pos. x- sprite.getWidth() / 2,pos.y - sprite.getHeight() / 2);
			sprite.draw(batch);
		batch.end();
	}

	@Override
	public int GetDisplayRank()
	{
		return DisplayOrder.Render2D.ordinal();
	}

	public float getWidth()
	{
		return  sprite.getWidth();
	}

	public float getHeight()
	{
		return  sprite.getHeight();
	}

	public void setPosition(float x, float y) 
	{
		pos.x = x;
		pos.y = y ;
		sprite.setPosition(pos.x - sprite.getWidth() / 2, pos.y - sprite.getWidth() / 2);
	}
}