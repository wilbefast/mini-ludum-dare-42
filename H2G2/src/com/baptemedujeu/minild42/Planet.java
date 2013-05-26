package com.baptemedujeu.minild42;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.jackamikaz.gameengine.DisplayedEntity;
import com.jackamikaz.gameengine.Engine;
import com.jackamikaz.gameengine.SpatialEntity;
import com.jackamikaz.gameengine.UpdatedEntity;
import com.jackamikaz.gameengine.utils.DisplayOrder;

public class Planet implements DisplayedEntity, UpdatedEntity, SpatialEntity
{
	private Vector2 pos;
	private Sprite sprite;
	private float turnSpeed;
	
	public Planet(float x, float y, float r)
	{
		// register
		Engine.DisplayMaster().Add(this);
		Engine.UpdateMaster().Add(this);
		
		// position
		pos = new Vector2(x, y);
		
		// spin
		turnSpeed = (float)((Math.random() > 0.5 ? - 1 : 1)
								*(10.0f + Math.random()*30.0f));
		
		// sprite
		Texture t = Engine.ResourceManager().GetTexture("planet"+(Math.random()>0.5?"":"2"));
		TextureRegion tr = new TextureRegion(t, 0, 0, 128, 128);
		sprite = new Sprite(tr);
		sprite.setSize(r, r * sprite.getHeight() / sprite.getWidth());
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		sprite.setPosition(pos.x - sprite.getWidth() / 2, pos.y - sprite.getWidth() / 2);
		sprite.rotate((float) (Math.random()*360));
	}
	
	@Override
	public void Update(float deltaT)
	{
		sprite.rotate(turnSpeed * deltaT);
	}

	@Override
	public void Display(float lerp)
	{
		sprite.draw( Engine.Batch());
	}

	@Override
	public int GetDisplayRank()
	{
		return DisplayOrder.Render2D.ordinal();
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
	public float getRotation() { return sprite.getRotation(); }
	
}
