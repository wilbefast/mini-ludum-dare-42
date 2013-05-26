package com.baptemedujeu.minild42;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.jackamikaz.gameengine.DisplayedEntity;
import com.jackamikaz.gameengine.Engine;
import com.jackamikaz.gameengine.utils.DisplayOrder;

public class Background implements DisplayedEntity {
	Sprite sprite1;
	Sprite sprite2;
	Sprite sprite3;
	
	public Background(){
		
		Engine.DisplayMaster().Add(this);
		
		int xsize = 30;
		int ysize = 30;
		
		Texture t = Engine.ResourceManager().GetTexture("bg1");
		TextureRegion tr = new TextureRegion(t, 0, 0, 2048, 2048);
		sprite1 = new Sprite(tr);
		sprite1.setSize(15, 15 * sprite1.getHeight() / sprite1.getWidth());
		
		t = Engine.ResourceManager().GetTexture("bg2");
		tr = new TextureRegion(t, 0, 0, 2048, 2048);
		sprite2 = new Sprite(tr);
		sprite2.setSize(xsize, ysize * sprite1.getHeight() / sprite1.getWidth());
		
		t = Engine.ResourceManager().GetTexture("bg3");
		tr = new TextureRegion(t, 0, 0, 2048, 2048);
		sprite3 = new Sprite(tr);
		sprite3.setSize(xsize, ysize * sprite1.getHeight() / sprite1.getWidth());
		
		//sprite.setPosition(0, 0);
		
	}
	
	@Override
	public void Display(float lerp) {
		// TODO Auto-generated method stub
	
		Vector3 campos = H2G2Game.camera.position;
		float xoff = -15f;
		float yoff = -15f;
		sprite1.setPosition(-7.5f+campos.x, -7.5f+campos.y);
		sprite2.setPosition(xoff+campos.x*0.95f, yoff+campos.y*0.95f);
		sprite3.setPosition(xoff+campos.x*0.85f, yoff+campos.y*0.85f);
		//SpriteBatch batch = Engine.Batch();
		//batch.begin();
			sprite1.draw(Engine.Batch());
			sprite2.draw(Engine.Batch());
			sprite3.draw(Engine.Batch());
		//batch.end();

	}

	@Override
	public int GetDisplayRank() {
		// TODO Auto-generated method stub
		return DisplayOrder.Render2DFirst.ordinal();
	}

}
