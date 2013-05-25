package com.jackamikaz.gameengine.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class ResFont extends Resource {

	/*public ResFont(String[] f) {
		super(f);
	}*/

	@Override
	public void Load() {
		BitmapFont font = new BitmapFont(
			Gdx.files.internal(files[0]),
			Gdx.files.internal(files[1]), false);
		
		resObj = font;
	}

	@Override
	public void UnLoadImpl() {
		((BitmapFont)resObj).dispose();
	}

	@Override
	public String GetResName() {
		return "Font";
	}


}
