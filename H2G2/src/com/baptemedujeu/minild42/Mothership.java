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

public class Mothership implements DisplayedEntity, UpdatedEntity, SpatialEntity
{
	private TextureRegion unoccupied, occupied;
	private Sprite sprite;
	private Vector2 pos;
	
	public Mothership(float x, float y)
	{
		// register
		Engine.DisplayMaster().Add(this);
		Engine.UpdateMaster().Add(this);
		
		
		pos = new Vector2(x, y);

		// sprite
		Texture t = Engine.ResourceManager().GetTexture("sprites");
		unoccupied = new TextureRegion(t, 0, 768, 256, 256);
		occupied = new TextureRegion(t, 256, 768, 256, 256);
		sprite = new Sprite(unoccupied);
		
		float size = (sprite.getHeight() / sprite.getWidth());
		sprite.setSize(80, 80 * size);
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		sprite.setPosition(pos.x, pos.y);
	}
	
	
	//! OCCUPATION
	
	public void setOccupied(boolean b)
	{
		sprite.setRegion(b ? occupied : unoccupied);
	}
	
	
	
	//! CALLBACKS
	
	@Override
	public void Update(float deltaT)
	{
		sprite.setPosition(pos.x - sprite.getWidth()/2, pos.y - sprite.getHeight()/2);
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
	
	
	
	//! SPATIAL ENTITY
	
	@Override
	public Vector2 getPosition() { return pos; }

	@Override
	public float getRadius() { return sprite.getWidth()*0.01f; }

	@Override
	public float getWidth() { return sprite.getWidth(); }

	@Override
	public float getHeight() { return sprite.getHeight(); }

	@Override
	public float getRotation() { return 0.0f; }
}
