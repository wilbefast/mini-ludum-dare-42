package com.baptemedujeu.minild42;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.jackamikaz.gameengine.DisplayedEntity;
import com.jackamikaz.gameengine.Engine;
import com.jackamikaz.gameengine.UpdatedEntity;
import com.jackamikaz.gameengine.utils.DisplayOrder;

public class Spaceship implements DisplayedEntity, UpdatedEntity, SpatialEntity
{
	private static final float SPEED = 0.3f;
	
	
	
	private Sprite sprite;
	
	private Vector2 orbitCentre;
	private float orbitRadius;
	private float orbitAngle;
	public float orbitSpeed;
	
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
		TextureRegion tr = new TextureRegion(t, 0, 0, 128, 128);
		sprite = new Sprite(tr);
		float size = (sprite.getHeight() / sprite.getWidth());
		sprite.setSize(1, size);
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		sprite.setPosition(pos.x, pos.y);
	}

	@Override
	public void Display(float lerp)
	{
		sprite.draw(Engine.Batch());
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
		
		sprite.setRotation((float)(180*orbitAngle / Math.PI) + Math.signum(orbitSpeed)*90.0f-90);
	}
	
	
	//! SPATIAL ENTITY
	
	@Override
	public Vector2 getPosition() { return pos; }

	@Override
	public float getRadius() { return 0.1f; }

	@Override
	public float getWidth() { return sprite.getWidth(); }

	@Override
	public float getHeight() { return sprite.getHeight(); }

	@Override
	public float getRotation() { return orbitAngle; }
}
