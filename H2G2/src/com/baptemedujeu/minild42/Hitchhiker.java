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
import com.jackamikaz.gameengine.SpatialEntity;
import com.jackamikaz.gameengine.UpdatedEntity;
import com.jackamikaz.gameengine.utils.DisplayOrder;

public class Hitchhiker implements DisplayedEntity, UpdatedEntity, InputEntity,
		SpatialEntity
{

	private Sprite sprite;
	private Texture var_tex;
	private Vector2 var_playerpos;

	private Thumb var_thumb;
	private Vector2 var_thumbTarget;
	private Vector2 var_thumbPos;
	private float var_ThumbSpeed = 1.0f;
	private float var_ThumbDelta = 0;
	private boolean var_isThumbing = false;

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

		var_playerpos = new Vector2(0, 0);
		var_thumbTarget = new Vector2(0, 0);
		var_thumbPos = new Vector2(0, 0);

		sprite.setSize(1, sprite.getHeight() / sprite.getWidth());
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);

		var_playerpos.x = -sprite.getWidth() / 2;
		var_playerpos.y = -sprite.getHeight() / 2;

		var_thumb = new Thumb(0, 0);

		var_thumbTarget.x = var_thumb.getWidth() / 2;
		var_thumbTarget.y = var_thumb.getHeight() / 2;

		sprite.setPosition(var_playerpos.x, var_playerpos.y);
		var_thumb.setPosition(var_thumbTarget.x, var_thumbTarget.y);

	}

	@Override
	public void Display(float lerp)
	{

		SpriteBatch batch = Engine.Batch();
		batch.begin();
		sprite.setPosition(var_playerpos.x - sprite.getWidth() / 2, var_playerpos.y
				- sprite.getHeight() / 2);
		var_thumb.setPosition(var_thumbPos.x, var_thumbPos.y);
		// System.out.println("sprite.getHeight()" + sprite.getHeight());

		// draw
		sprite.draw(batch); // draw the player
		batch.end();
	}

	@Override
	public int GetDisplayRank()
	{
		return DisplayOrder.Render2D.ordinal();
	}

	@Override
	public void Update(float deltaT)
	{

		if (var_isThumbing)
		{
			var_ThumbDelta += deltaT * var_ThumbSpeed;
			// System.out.println("Stopped thumb" + var_ThumbDelta);
			Vector2 tempVector = new Vector2(var_playerpos);
			var_thumbPos = tempVector.lerp(var_thumbTarget, var_ThumbDelta);
			if (var_ThumbDelta >= 1)
			{
				System.out.println("Stopped thumb");
				var_isThumbing = false;
				var_ThumbDelta = 0;
			}
		}
	}

	@Override
	public void NewInput(Input input)
	{
		// TODO Auto-generated method stub

		if (input.isTouched() && !var_isThumbing)
		{
			var_thumbTarget.x = input.getX();
			var_thumbTarget.y = input.getY();

			Ray r = H2G2Game.camera.getPickRay(var_thumbTarget.x, var_thumbTarget.y);
			// System.out.println("PlayerPosWorld "+r.origin);

			var_thumbTarget.x = r.origin.x;
			var_thumbTarget.y = r.origin.y;
			var_isThumbing = true;
		}
	}

	
	//! SPATIAL ENTITY
	
	@Override
	public Vector2 getPosition() { return var_playerpos; }

	@Override
	public float getRadius() { return 10.0f; }

	@Override
	public float getWidth() { return 20.0f; }

	@Override
	public float getHeight() { return 20.0f; }

	@Override
	public float getRotation() { return 0.0f; }

}
