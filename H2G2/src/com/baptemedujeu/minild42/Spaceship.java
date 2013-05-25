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

public class Spaceship implements DisplayedEntity, UpdatedEntity
{

	private Sprite sprite;
	private Vector2 pos;

	public Spaceship(float x, float y)
	{
		pos = new Vector2(x, y);
		
		Texture t = Engine.ResourceManager().GetTexture("spaceship");
		TextureRegion tr = new TextureRegion(t, 0, 0, 64, 64);
		sprite = new Sprite(tr);
		sprite.setSize(0.1f, 0.1f * sprite.getHeight() / sprite.getWidth());
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		sprite.setPosition(pos.x, pos.y);
	}

	@Override
	public void Display(float lerp)
	{

		sprite.setPosition(pos.x, pos.y);
		
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
		sprite.setPosition(pos.x, pos.y);
	}
}
