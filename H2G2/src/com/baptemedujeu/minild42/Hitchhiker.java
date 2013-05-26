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
	private Texture texture;
	private Vector2 pos;
	
	// camera
	private float desiredCameraAngle = 0.0f;
	private float currentCameraAngle = 0.0f;
	private static final float CAMERA_ROTATE_SPEED = 260.0f;
	
	private SpatialEntity falltowards;

	
	private EntityQueryManager.TypedDistanceQuery planetDistance;
	
	public Hitchhiker(float x, float y) 
	{
		// register
		Engine.DisplayMaster().Add(this);
		Engine.UpdateMaster().Add(this);
		Engine.InputMaster().Add(this);

		pos = new Vector2(x, y);
		H2G2Game.camera.position.set(x, y, 0);
		H2G2Game.camera.update();

		texture = Engine.ResourceManager().GetTexture("hitchhiker");
		TextureRegion region = new TextureRegion(texture, 0, 0, 64, 64);
		sprite = new Sprite(region);
		sprite.setPosition(pos.x, pos.y);
		sprite.setSize(0.7f, 0.7f * sprite.getHeight() / sprite.getWidth());
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		
		// initialise queries
		planetDistance = 
				new EntityQueryManager.TypedDistanceQuery(pos, Planet.class);

	}

	public void setFalltowards(SpatialEntity sp)
	{
		falltowards = sp;
	}
	
	@Override
	public void Display(float lerp)
	{

		SpriteBatch batch = Engine.Batch();
		batch.begin();
		sprite.setPosition(pos.x  -sprite.getWidth() / 2 , pos.y  -sprite.getHeight() / 2 ) ;
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
	
		if (falltowards == null) 
		{
			falltowards = (SpatialEntity)(EntityQueryManager.getMin(planetDistance));
		}
		
		// GRAVITY
		//Planet nearestPlanet = (Planet)(EntityQueryManager.getMin(planetDistance));
		
		Vector2 toPlanet = 
				(new Vector2()).set(falltowards.getPosition()).sub(pos);
		float distanceToPlanet = toPlanet.len();
		if(distanceToPlanet > falltowards.getRadius() + this.getRadius())
		{
			desiredCameraAngle = toPlanet.angle() + 90;
			pos.add(toPlanet.div(distanceToPlanet).scl(0.1f));
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
		}
		H2G2Game.camera.position.set(pos.x, pos.y, 0);
		H2G2Game.camera.update();
			
	}

	@Override
	public void NewInput(Input input)
	{

		if(input.isTouched() && Thumb.canThrow())
		{
			Vector3 in = new Vector3(input.getX(), input.getY(), 0.0f);
			H2G2Game.camera.unproject(in);
			
			Thumb thmb = new Thumb(pos.x, pos.y, in.x - pos.x, in.y - pos.y, 30.0f, this);
		}
	}

	
	//! SPATIAL ENTITY
	
	@Override
	public Vector2 getPosition() { return pos; }

	@Override
	public float getRadius() { return 0.35f; }

	@Override
	public float getWidth() { return 0.7f; }

	@Override
	public float getHeight() { return 0.7f; }

	@Override
	public float getRotation() { return 0.0f; }

}
