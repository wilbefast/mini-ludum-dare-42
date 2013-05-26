package com.jackamikaz.gameengine.resources;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.jackamikaz.gameengine.Engine;

public class ResTextureSheet extends Resource {

	@Override
	public void Load() {
		Texture tex = Engine.ResourceManager().GetTexture(files[0]);
		
		int w = Integer.parseInt(files[1]);
		int h = Integer.parseInt(files[2]);
		
		TextureRegion[][] tmp = TextureRegion.split(tex,
		tex.getWidth() / w, tex.getHeight() / h);
		TextureRegion[] frames = new TextureRegion[w * h];
		int index = 0;
		for (int j = 0; j < w; j++) {
			for (int i = 0; i < h; i++) {
				frames[index++] = tmp[i][j];
			}
		}
		
		resObj = frames;
	}

	@Override
	protected void UnLoadImpl() {
	}

	@Override
	public String GetResName() {
		return "TextureSheet";
	}

}
