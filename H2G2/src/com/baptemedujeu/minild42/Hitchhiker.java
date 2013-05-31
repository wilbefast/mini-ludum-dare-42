package com.baptemedujeu.minild42;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.jackamikaz.gameengine.DisplayedEntity;
import com.jackamikaz.gameengine.Engine;
import com.jackamikaz.gameengine.InputEntity;
import com.jackamikaz.gameengine.UpdatedEntity;
import com.jackamikaz.gameengine.utils.ButtonWatcher;
import com.jackamikaz.gameengine.utils.DisplayOrder;
import com.jackamikaz.gameengine.utils.InputWatcher;

public class Hitchhiker implements DisplayedEntity, UpdatedEntity, InputEntity,
		SpatialEntity
{

	private Sprite sprite;
	private Vector2 pos;

	// thumb related
	private InputWatcher clic;
	private TextureRegion[] arrowSheet;
	private Sprite arrow;
	private float thumbLoad;
	private Vector2 mousePos;
	private static final float thumbLoadSpeed = 1.0f;
	private static final float thumbMinPower = 144.0f;
	private static final float thumbMaxPower = 704.0f;
	private static final float gravity = 160.0f;

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
		mousePos = new Vector2(pos);

		clic = new ButtonWatcher(Buttons.LEFT);

		H2G2Game.camera.position.set(x, y, 0);
		H2G2Game.camera.update();

		// hitchhiker sprite
		Texture t = Engine.ResourceManager().GetTexture("sprites");
		TextureRegion tr = new TextureRegion(t, 0, 0, 64, 64);
		sprite = new Sprite(tr);
		sprite.setPosition(pos.x, pos.y);
		sprite.setSize(23, 23);
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);

		// arrow sprites
		arrowSheet = Engine.ResourceManager().GetTextureSheet("arrow");
		arrow = new Sprite(arrowSheet[0]);
		arrow.setSize(16, 16 * arrow.getHeight() / arrow.getWidth());
		arrow.setOrigin(arrow.getWidth() / 2, 0);

		// initialize queries
		planetDistance = new EntityQueryManager.TypedDistanceQuery(pos,
				Planet.class);
	}

	public void setFalltowards(SpatialEntity sp)
	{
		if (falltowards instanceof Spaceship)
			((Spaceship) falltowards).setOccupied(false);
		falltowards = sp;
	}

	@Override
	public void Display(float lerp)
	{
		if (((falltowards instanceof Mothership) || (falltowards instanceof Spaceship))
				&& pos.dst(falltowards.getPosition()) < 0.5)
		{

		}
		else
		{
			sprite.setPosition(pos.x - sprite.getOriginX(),
					pos.y - sprite.getOriginY());
			sprite.draw(Engine.Batch()); // draw the player
		}

		if (thumbLoad > 0)
			arrow.draw(Engine.Batch());
	}

	@Override
	public int GetDisplayRank()
	{
		return DisplayOrder.Render2DLast.ordinal();
	}

	private static float minabs(float v, float positivevalue)
	{
		return (float) ((v > 0) ? Math.min(v, positivevalue) : Math.max(v,
				-positivevalue));
	}

	/*
	 * private static float maxabs(float v, float positivevalue) { return
	 * (float)((v > 0) ? Math.max(v, positivevalue) : Math.min(v,
	 * -positivevalue)); }
	 */

	@Override
	public void Update(float deltaT)
	{
		// thumb
		if (Thumb.canThrow())
		{
			if (clic.isPressed())
			{
				thumbLoad += deltaT * thumbLoadSpeed;
				if (thumbLoad > 1.0f)
					thumbLoad = 1.0f;
			}

			if (clic.wasReleased())
			{
				float power = thumbMinPower + (thumbMaxPower - thumbMinPower)
						* thumbLoad;
				thumbLoad = 0.0f;
				new Thumb(pos.x, pos.y, mousePos.x - pos.x, mousePos.y - pos.y, power,
						this, falltowards);
			}
		}

		// thumb graphics
		if (thumbLoad > 0)
		{
			Vector2 pToM = new Vector2(mousePos.x - pos.x, mousePos.y - pos.y);
			pToM.nor().scl(0.5f);
			arrow
					.setRegion(arrowSheet[(int) (thumbLoad * (float) arrowSheet.length - 1)]);
			arrow.setPosition(pos.x + pToM.x - arrow.getOriginX(), pos.y + pToM.y
					- arrow.getOriginY());
			arrow.setRotation(pToM.angle() - 90);
		}

		/*
		 * FALLING TOWARDS NOTHING
		 */
		if (falltowards == null)
			falltowards = (SpatialEntity) (EntityQueryManager.getMin(planetDistance));

		/*
		 * LANDED ON OBJECT
		 */
		Vector2 toDestination = (new Vector2()).set(falltowards.getPosition()).sub(pos);
		float distanceToDestination = toDestination.len();
		if (distanceToDestination > (falltowards.getRadius() + this.getRadius()))
		{
			if ((falltowards instanceof Spaceship))
			{
				Spaceship sp = (Spaceship) falltowards;
				int sign = 1; // TODO (int) Math.signum(sp.orbitSpeed);
				desiredCameraAngle = (float) (sp.getRotation() * (180 / Math.PI)) + 90
						* sign - 90;
			}
			else
			{
				desiredCameraAngle = toDestination.angle() + 90;
				landAngle = falltowards.getRotation() - desiredCameraAngle - 90;
			}
			pos.add(toDestination.div(distanceToDestination).scl(gravity * deltaT));
		}		
		
		/*
		 * FALLING TOWARDS PLANET
		 */
		
		else if (falltowards instanceof Planet)
		{
			float r = falltowards.getRotation() - landAngle;
			float d = (falltowards.getRadius() + this.getRadius()) * 0.999f;
			pos.set(falltowards.getPosition()).add(MathUtils.cosDeg(r) * d,
					MathUtils.sinDeg(r) * d);
			desiredCameraAngle = r - 90;
		}
		
		
		/*
		 * FALLING TOWARDS SPACESHIP OR MOTHERSHIP
		 */
		
		else if ((falltowards instanceof Spaceship)
				|| (falltowards instanceof Mothership))
		{
			pos.set(falltowards.getPosition());

			if (falltowards instanceof Spaceship) // ! FIXME: mothership and spaceship
																						// should have the same parent
				((Spaceship) falltowards).setOccupied(true);
			else
				((Mothership) falltowards).setOccupied(true);
		}

		if ((falltowards instanceof Spaceship))
		{
			Spaceship sp = (Spaceship) falltowards;
			int sign = 1; // TODO (int) Math.signum(sp.orbitSpeed);
			desiredCameraAngle = (float) (sp.getRotation() * (180 / Math.PI)) + 90
					* sign - 90;
		}
		else if ((falltowards instanceof Mothership))
		{
			desiredCameraAngle += deltaT * 15;
		}
		else
		{
			desiredCameraAngle = toDestination.angle() + 90;
		}

		// reset camera angle
		currentCameraAngle = (currentCameraAngle + 360) % 360;
		desiredCameraAngle = (desiredCameraAngle + 360) % 360;
		// desiredCameraAngle = 0;
		if (currentCameraAngle != desiredCameraAngle)
		{
			float delta_angle = desiredCameraAngle - currentCameraAngle;

			if (delta_angle > 180)
				delta_angle -= 360;
			else if (delta_angle < -180)
				delta_angle += 360;

			float abs_angle = Math.abs(delta_angle);
			float rotateAmount;

			if (abs_angle > 0.1f)
			{
				rotateAmount = minabs((delta_angle) * 320.0f / distanceToDestination,
						CAMERA_ROTATE_SPEED) * deltaT;
			}
			else
				rotateAmount = delta_angle * deltaT;

			currentCameraAngle += rotateAmount;
			// H2G2Game.camera.rotate(-rotateAmount);
			sprite.rotate(rotateAmount);
		}

		// home camera
		Vector2 p = ((falltowards instanceof Spaceship) ? falltowards.getPosition()
				: this.pos);
		H2G2Game.camera.position.set(H2G2Game.camera.position.x * 0.95f + p.x
				* 0.05f, H2G2Game.camera.position.y * 0.95f + p.y * 0.05f, 0);

		// update camera
		H2G2Game.camera.update();

	}

	@Override
	public void NewInput(Input input)
	{
		clic.NewInput(input);

		Vector3 in = new Vector3(input.getX(), input.getY(), 0.0f);
		H2G2Game.camera.unproject(in);

		mousePos.x = in.x;
		mousePos.y = in.y;
	}

	// ! SPATIAL ENTITY

	@Override
	public Vector2 getPosition()
	{
		return pos;
	}

	@Override
	public float getRadius()
	{
		return sprite.getWidth() * 0.5f;
	}

	@Override
	public float getWidth()
	{
		return sprite.getWidth();
	}

	@Override
	public float getHeight()
	{
		return sprite.getHeight();
	}

	@Override
	public float getRotation()
	{
		return 0.0f;
	}

}
