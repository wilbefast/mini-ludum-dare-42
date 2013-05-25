package com.jackamikaz.gameengine.assets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jackamikaz.gameengine.Engine;

public class SpriteLayout {

	private class SpriteExt extends Sprite{
		public String name = "unnamed";
		
		public SpriteExt(Texture tex) {
			super(tex);
		}
		
		public SpriteExt(SpriteExt spr) {
			super(spr);
			name = spr.name;
		}
	}
	
	private SpriteExt[] sprites;
	
	public SpriteLayout(int nbSprites) {
		sprites = new SpriteExt[nbSprites];
	}
	
	public SpriteLayout(FileHandle file) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(file.read()));
		String line;

		Vector<SpriteExt> sprList = new Vector<SpriteExt>();
		
		try {
			line = reader.readLine();
			
			while (line != null) {
				String[] tokens = line.split("[ ]+");
				
				if (!(tokens.length >= 4)) {
					return;
				}
				
				SpriteExt spr = new SpriteExt(Engine.ResourceManager().GetTexture(tokens[1]));
				spr.name = tokens[0];
				spr.setPosition(Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));
				if (tokens.length >= 6)
					spr.setScale(Float.parseFloat(tokens[4]), Float.parseFloat(tokens[5]));
				if (tokens.length >= 8)
					spr.setOrigin(Float.parseFloat(tokens[6]), Float.parseFloat(tokens[7]));
				if (tokens.length >= 9)
					spr.setRotation(Float.parseFloat(tokens[8]));
					
				sprList.add(spr);
				
				line = reader.readLine();
			}
			
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		sprites = sprList.toArray(new SpriteExt[sprList.size()]);
	}
	
	public void draw(SpriteBatch batch) {
		for(Sprite spr : sprites) {
			spr.draw(batch);
		}
	}
	
	public void draw(SpriteBatch batch, float alphaModulation) {
		for(Sprite spr : sprites) {
			spr.draw(batch, alphaModulation);
		}
	}
	
	public Sprite GetByName(String name) {
		for(SpriteExt spr : sprites) {
			if (spr.name.equalsIgnoreCase(name))
				return spr;
		}
		return null;
	}
	
	public SpriteLayout Duplicate() {
		SpriteLayout ret = new SpriteLayout(sprites.length);
		
		for(int i=0; i<sprites.length; ++i) {
			ret.sprites[i] = new SpriteExt(sprites[i]);
		}
		
		return ret;
	}
}
