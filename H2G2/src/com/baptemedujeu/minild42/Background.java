package com.baptemedujeu.minild42;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.jackamikaz.gameengine.DisplayedEntity;
import com.jackamikaz.gameengine.Engine;
import com.jackamikaz.gameengine.utils.DisplayOrder;

public class Background implements DisplayedEntity
{
	private static final float xoff = -560.5f, yoff = -560.5f;
	
	private Sprite sprite1;
	private Sprite sprite2;
	private Sprite sprite3;

	public Background()
	{

		//Engine.DisplayMaster().Add(this);

		int xsize = 35;
		int ysize = 35;

		Texture t = Engine.ResourceManager().GetTexture("bg1");
		TextureRegion tr = new TextureRegion(t, 0, 0, 2048, 2048);
		sprite1 = new Sprite(tr);
		sprite1.setSize(480, 480 * sprite1.getHeight() / sprite1.getWidth());

		t = Engine.ResourceManager().GetTexture("bg2");
		tr = new TextureRegion(t, 0, 0, 2048, 2048);
		sprite2 = new Sprite(tr);
		sprite2.setSize(xsize, ysize * sprite1.getHeight() / sprite1.getWidth());

		t = Engine.ResourceManager().GetTexture("bg3");
		tr = new TextureRegion(t, 0, 0, 2048, 2048);
		sprite3 = new Sprite(tr);
		sprite3.setSize(xsize, ysize * sprite1.getHeight() / sprite1.getWidth());

		// sprite.setPosition(0, 0);

	}

	@Override
	public void Display(float lerp)
	{
		// TODO Auto-generated method stub

		Vector3 campos = H2G2Game.camera.position;

		sprite1.setPosition(-240.0f + campos.x, -240.0f + campos.y);
		sprite2.setPosition(xoff + campos.x * 30.4f, yoff + campos.y * 30.4f);
		sprite3.setPosition(xoff + campos.x * 27.2f, yoff + campos.y * 27.2f);
		// SpriteBatch batch = Engine.Batch();
		// batch.begin();
		sprite1.draw(Engine.Batch());
		sprite2.draw(Engine.Batch());
		sprite3.draw(Engine.Batch());
		// batch.end();

	}

	@Override
	public int GetDisplayRank()
	{
		// TODO Auto-generated method stub
		return DisplayOrder.Render2DFirst.ordinal();
	}

}
