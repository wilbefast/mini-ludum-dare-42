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
		
		int xsize = 50;
		int ysize = 50;
		
		Texture t = Engine.ResourceManager().GetTexture("bg1");
		TextureRegion tr = new TextureRegion(t, 0, 0, 2048, 2048);
		sprite1 = new Sprite(tr);
		sprite1.setSize(xsize, ysize * sprite1.getHeight() / sprite1.getWidth());
		
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
		int xoff = -25;
		int yoff = -25;
		sprite1.setPosition(xoff+campos.x*0.8f, yoff+campos.y*0.8f);
		sprite2.setPosition(xoff+campos.x*0.6f, yoff+campos.y*0.6f);
		sprite3.setPosition(xoff+campos.x*0.4f, yoff+campos.y*0.4f);
		SpriteBatch batch = Engine.Batch();
		//batch.begin();
			sprite1.draw(batch);
			sprite2.draw(batch);
			sprite3.draw(batch);
		//batch.end();

	}

	@Override
	public int GetDisplayRank() {
		// TODO Auto-generated method stub
		return DisplayOrder.Render2DFirst.ordinal();
	}

}
