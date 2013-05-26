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

public class Thumb implements DisplayedEntity, UpdatedEntity {

	private Vector2 pos;
	private Sprite sprite;
	
	public Thumb(float x, float y)
	{
		// register
		Engine.DisplayMaster().Add(this);
		Engine.UpdateMaster().Add(this);
		
		// position
		pos = new Vector2(x, y);
		
		// sprite
		Texture t = Engine.ResourceManager().GetTexture("thumb");
		TextureRegion tr = new TextureRegion(t, 0, 0, 64, 64);
		sprite = new Sprite(tr);
		sprite.setSize(5.0f, 5.0f * sprite.getHeight() / sprite.getWidth());
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		sprite.setPosition(pos.x - sprite.getWidth() / 2, pos.y - sprite.getWidth() / 2);
	}
	
	@Override
	public void Update(float deltaT)
	{
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

	public float getWidth()
	{
		return  sprite.getWidth();
	}

	public float getHeight()
	{
		return  sprite.getHeight();
	}

	public void setPosition(float x, float y) 
	{
		pos.x = x;
		pos.y = y ;
		sprite.setPosition(pos.x - sprite.getWidth() / 2, pos.y - sprite.getWidth() / 2);
	}
}