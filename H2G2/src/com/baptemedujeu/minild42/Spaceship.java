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

public class Spaceship implements DisplayedEntity, UpdatedEntity, SpatialEntity
{
	private static final float SPEED = 1.0f;
	
	
	
	private Sprite sprite;
	
	private Vector2 orbitCentre;
	private float orbitRadius;
	private float orbitAngle;
	private float orbitSpeed;
	
	private Vector2 pos;

	public Spaceship(float x, float y, float _orbitRadius)
	{
		// register
		Engine.DisplayMaster().Add(this);
		Engine.UpdateMaster().Add(this);
		
		// orbit
		orbitCentre = new Vector2(x, y);
		orbitRadius = _orbitRadius;
		orbitSpeed = (float)(SPEED * ((Math.random() < 0.5) ? -1 : 1) 
															/ orbitRadius*2*Math.PI);
		pos = new Vector2(x + orbitRadius, y);
		orbitAngle = 0.0f;
		
		// sprite
		Texture t = Engine.ResourceManager().GetTexture("spaceship");
		TextureRegion tr = new TextureRegion(t, 0, 0, 64, 64);
		sprite = new Sprite(tr);
		sprite.setSize(1, sprite.getHeight() / sprite.getWidth());
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		sprite.setPosition(pos.x, pos.y);
	}

	@Override
	public void Display(float lerp)
	{
		SpriteBatch batch = Engine.Batch();
		batch.begin();
			sprite.draw(batch);
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
		orbitAngle += deltaT*orbitSpeed;
		
		pos.x = (float)(orbitCentre.x + Math.cos(orbitAngle)*orbitRadius);
		pos.y = (float)(orbitCentre.y + Math.sin(orbitAngle)*orbitRadius);
		sprite.setPosition(pos.x - sprite.getWidth()/2, pos.y - sprite.getHeight()/2);
		
		sprite.setRotation((float)(180*orbitAngle / Math.PI) + Math.signum(orbitSpeed)*90.0f);
	}
	
	
	//! SPATIAL ENTITY
	
	@Override
	public Vector2 getPosition() { return pos; }

	@Override
	public float getRadius() { return 1.5f; }

	@Override
	public float getWidth() { return 3.0f; }

	@Override
	public float getHeight() { return 3.0f; }

	@Override
	public float getRotation() { return 0.0f; }
}
