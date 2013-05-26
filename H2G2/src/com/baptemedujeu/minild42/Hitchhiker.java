package com.baptemedujeu.minild42;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.jackamikaz.gameengine.DisplayedEntity;
import com.jackamikaz.gameengine.Engine;
import com.jackamikaz.gameengine.InputEntity;
import com.jackamikaz.gameengine.SpatialEntity;
import com.jackamikaz.gameengine.UpdatedEntity;
import com.jackamikaz.gameengine.utils.DisplayOrder;

public class Hitchhiker implements DisplayedEntity, UpdatedEntity, InputEntity,
		SpatialEntity
{

	private Sprite sprite;
	private Texture var_tex;
	private Vector2 var_playerpos;
	
	// camera
	private float desiredCameraAngle = 0.0f;
	private float currentCameraAngle = 0.0f;
	private static final float CAMERA_ROTATE_SPEED = 260.0f;
	private float var_thumb_time_since;
	private float var_thumb_cooldown = 0.3f;
	
	private SpatialEntity var_falltowards;

	
	private EntityQueryManager.TypedDistanceQuery planetDistance;
	
	public Hitchhiker(float x, float y) 
	{
		// register
		Engine.DisplayMaster().Add(this);
		Engine.UpdateMaster().Add(this);
		Engine.InputMaster().Add(this);

		var_playerpos = new Vector2(x, y);
		H2G2Game.camera.position.set(x, y, 0);
		H2G2Game.camera.update();

		var_tex = Engine.ResourceManager().GetTexture("hitchhiker");
		TextureRegion region = new TextureRegion(var_tex, 0, 0, 64, 64);
		sprite = new Sprite(region);
		sprite.setPosition(var_playerpos.x, var_playerpos.y);
		sprite.setSize(1, sprite.getHeight() / sprite.getWidth());
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);

		var_thumb_cooldown = 0.3f;
		var_thumb_time_since = 0;
		
		// initialise queries
		planetDistance = 
				new EntityQueryManager.TypedDistanceQuery(var_playerpos, Planet.class);
		
		var_falltowards = (SpatialEntity)(EntityQueryManager.getMin(planetDistance));
		System.out.println(var_falltowards);

	}

	@Override
	public void Display(float lerp)
	{

		SpriteBatch batch = Engine.Batch();
		batch.begin();
		sprite.setPosition(var_playerpos.x  -sprite.getWidth() / 2 ,var_playerpos.y  -sprite.getHeight() / 2 ) ;
		sprite.draw(batch);				//draw the player
		batch.end();
	}

	@Override
	public int GetDisplayRank()
	{
		return DisplayOrder.Render2D.ordinal();
	}

	private static float minabs(float v, float positivevalue)
	{
		return (float)((v > 0) ? Math.min(v, positivevalue) : 
															Math.max(v, -positivevalue));
	}
	
	private static float maxabs(float v, float positivevalue)
	{
		return (float)((v > 0) ? Math.max(v, positivevalue) : 
															Math.min(v, -positivevalue));
	}
	
	@Override
	public void Update(float deltaT)
	{
	
		if (var_falltowards==null) {
			var_falltowards = (SpatialEntity)(EntityQueryManager.getMin(planetDistance));
		}
		var_thumb_time_since=Math.max(0, var_thumb_time_since-deltaT);
		
		// GRAVITY
		//Planet nearestPlanet = (Planet)(EntityQueryManager.getMin(planetDistance));
		
		Vector2 toPlanet = 
				(new Vector2()).set(var_falltowards.getPosition()).sub(var_playerpos);
		float distanceToPlanet = toPlanet.len();
		if(distanceToPlanet > var_falltowards.getRadius() + this.getRadius())
		{
			desiredCameraAngle = toPlanet.angle() + 90;
			var_playerpos.add(toPlanet.div(distanceToPlanet).scl(0.1f));
		}
		
		// reset camera angle
		if(currentCameraAngle != desiredCameraAngle)
		{
			float rotateAmount = 
					((Math.abs(desiredCameraAngle - currentCameraAngle) > 0.1f)
					? minabs((desiredCameraAngle - currentCameraAngle)*10 / distanceToPlanet, CAMERA_ROTATE_SPEED)
					: desiredCameraAngle - currentCameraAngle) * deltaT;
			
			currentCameraAngle += rotateAmount;
			H2G2Game.camera.rotate(rotateAmount);
			sprite.rotate(rotateAmount);
			
			H2G2Game.camera.update();
		}
			
	}

	@Override
	public void NewInput(Input input)
	{

		if(input.isTouched() && var_thumb_time_since<=0)
		{
			var_thumb_time_since = var_thumb_cooldown;

			Vector3 in = new Vector3(input.getX(), input.getY(), 0.0f);
			H2G2Game.camera.unproject(in);
			
			Thumb thmb = new Thumb(var_playerpos.x, var_playerpos.y);
			thmb.setDirection(in.x - var_playerpos.x, in.y - var_playerpos.y);
			
			
		}
	}

	
	//! SPATIAL ENTITY
	
	@Override
	public Vector2 getPosition() { return var_playerpos; }

	@Override
	public float getRadius() { return 0.5f; }

	@Override
	public float getWidth() { return 1.0f; }

	@Override
	public float getHeight() { return 1.0f; }

	@Override
	public float getRotation() { return 0.0f; }

}
