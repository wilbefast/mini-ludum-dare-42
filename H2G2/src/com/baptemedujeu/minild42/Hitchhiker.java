package com.baptemedujeu.minild42;


import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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
	private static final float CAMERA_ROTATE_SPEED = 180.0f;
	
	private SpatialEntity falltowards;
	private float landAngle;

	
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
		if ((!(falltowards instanceof Spaceship)) || pos.dst(falltowards.getPosition())>0.5) {
			sprite.setPosition(pos.x  -sprite.getWidth() / 2 , pos.y  -sprite.getHeight() / 2 ) ;
			sprite.draw(Engine.Batch());				//draw the player
		}
	}

	@Override
	public int GetDisplayRank()
	{
		return DisplayOrder.Render2DLast.ordinal();
	}

	private static float minabs(float v, float positivevalue)
	{
		return (float)((v > 0) ? Math.min(v, positivevalue) : 
															Math.max(v, -positivevalue));
	}
	
	/*private static float maxabs(float v, float positivevalue)
	{
		return (float)((v > 0) ? Math.max(v, positivevalue) : 
															Math.min(v, -positivevalue));
	}*/
	
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
			if ((falltowards instanceof Spaceship)) {
				Spaceship sp = (Spaceship) falltowards;
				int sign = (int) Math.signum(sp.orbitSpeed);
				desiredCameraAngle = (float) (sp.getRotation()*(180/Math.PI)) + 90*sign - 90;
			} else {
				desiredCameraAngle = toPlanet.angle() + 90;
				landAngle = falltowards.getRotation() - desiredCameraAngle - 90;
			}
			pos.add(toPlanet.div(distanceToPlanet).scl(0.1f));
		}
		else if (falltowards instanceof Planet)
		{
			float r = falltowards.getRotation() - landAngle;
			float d = (falltowards.getRadius() + this.getRadius()) * 0.999f;
			pos.set(falltowards.getPosition()).add(MathUtils.cosDeg(r)*d,MathUtils.sinDeg(r)*d);
			desiredCameraAngle = r - 90;
		}
		
		// reset camera angle
		currentCameraAngle = (currentCameraAngle + 360) % 360;
		desiredCameraAngle = (desiredCameraAngle + 360) % 360;
		if(currentCameraAngle != desiredCameraAngle)
		{
			float delta_angle = desiredCameraAngle - currentCameraAngle;
			
			if(delta_angle > 180)
				delta_angle -= 360;
			else if(delta_angle < -180)
				delta_angle += 360;
			
			float abs_angle = Math.abs(delta_angle);
			float rotateAmount;
			
			if(abs_angle > 0.1f)
			{
				rotateAmount = 
						minabs((delta_angle)*10 / distanceToPlanet, CAMERA_ROTATE_SPEED) * deltaT;
			}
			else
				rotateAmount = delta_angle * deltaT;
			
			currentCameraAngle += rotateAmount;
			//H2G2Game.camera.rotate(-rotateAmount);
			sprite.rotate(rotateAmount);
		}
		
		
		// home camera
		Vector2 p = ((falltowards instanceof Spaceship) 
				? falltowards.getPosition()
				: this.pos);
		H2G2Game.camera.position.set(
				H2G2Game.camera.position.x*0.95f + p.x*0.05f, 
				H2G2Game.camera.position.y*0.95f + p.y*0.05f, 0);
		
		// update camera
		H2G2Game.camera.update();
			
	}

	@Override
	public void NewInput(Input input)
	{

		if(input.isTouched() && Thumb.canThrow())
		{
			Vector3 in = new Vector3(input.getX(), input.getY(), 0.0f);
			H2G2Game.camera.unproject(in);
			if ((falltowards instanceof Spaceship)) {
				new Thumb(falltowards.getPosition().x, falltowards.getPosition().y, in.x - falltowards.getPosition().x, in.y - falltowards.getPosition().y, 15.0f, this, falltowards);
			} else {
				new Thumb(pos.x, pos.y, in.x - pos.x, in.y - pos.y, 15.0f, this, falltowards);
			}
		}
	}

	
	//! SPATIAL ENTITY
	
	@Override
	public Vector2 getPosition() { return pos; }

	@Override
	public float getRadius() { return sprite.getWidth()*0.5f; }

	@Override
	public float getWidth() { return sprite.getWidth(); }

	@Override
	public float getHeight() { return sprite.getHeight(); }

	@Override
	public float getRotation() { return 0.0f; }

}
