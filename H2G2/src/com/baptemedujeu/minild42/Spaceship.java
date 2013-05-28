package com.baptemedujeu.minild42;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.jackamikaz.gameengine.DisplayedEntity;
import com.jackamikaz.gameengine.Engine;
import com.jackamikaz.gameengine.UpdatedEntity;
import com.jackamikaz.gameengine.utils.DisplayOrder;

public class Spaceship implements DisplayedEntity, UpdatedEntity, SpatialEntity
{
	private static final float SPEED = 0.3f;
	
	
	
	private Sprite sprite;
	//private Sprite trail;
	
	private Vector2 orbitCentre;
	private float orbitRadius;
	private float orbitAngle;
	public float orbitSpeed;
	
	private Vector2 pos;
	
	private Texture normalTex;
	private Texture occupiedTex;
	
	private float size;

	public Spaceship(float x, float y, float _orbitRadius, int type)
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
		normalTex = Engine.ResourceManager().GetTexture("spaceship"+type);
		occupiedTex = Engine.ResourceManager().GetTexture("spaceship_occupied"+type);
		sprite = new Sprite(normalTex);
		float ratio = (sprite.getHeight() / sprite.getWidth());
		size = sprite.getWidth() / 128.0f;
		sprite.setSize(size, size*ratio);
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		sprite.setPosition(pos.x, pos.y);
		
		
		
		/*t = Engine.ResourceManager().GetTexture("trail");
		tr = new TextureRegion(t, 0, 0, 256, 256);
		trail = new Sprite(tr);
		size = (trail.getHeight() / trail.getWidth());
		trail.setSize(1, -size*Math.signum(orbitSpeed));
		trail.setOrigin(trail.getWidth() / 2, trail.getHeight() / 2);
		trail.scale(orbitRadius*2-sprite.getWidth()+0.25f);
		trail.setPosition(orbitCentre.x-trail.getWidth()/2, orbitCentre.y-trail.getHeight()/2);
		trail.setColor(0, 0.5f, 1, 1);*/
	}

	@Override
	public void Display(float lerp)
	{	
		
		//trail.draw(Engine.Batch());
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
		//trail.setPosition(pos.x - trail.getWidth()/2, pos.y - trail.getHeight()/2);
		//trail.setRotation((float)(180*orbitAngle / Math.PI) + 180);
		sprite.setPosition(pos.x - sprite.getWidth()/2, pos.y - sprite.getHeight()/2);
		sprite.setRotation((float)(180*orbitAngle / Math.PI) + Math.signum(orbitSpeed)*90.0f-90);
	}
	
	
	public void setOccupied(boolean b) 
	{
		sprite.setTexture(b ? occupiedTex : normalTex);
	}
	
	
	//! SPATIAL ENTITY
	
	@Override
	public Vector2 getPosition() { return pos; }

	@Override
	public float getRadius() { return size * 0.1f; }

	@Override
	public float getWidth() { return sprite.getWidth(); }

	@Override
	public float getHeight() { return sprite.getHeight(); }

	@Override
	public float getRotation() { return orbitAngle; }
}
