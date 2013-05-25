package com.jackamikaz.gameengine.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class ResSound extends Resource {

	/*public ResSound(String[] f) {
		super(f);
	}*/

	@Override
	public void Load() {
		Sound sound = Gdx.audio.newSound(Gdx.files.internal(files[0]));
		
		resObj = sound;
	}

	@Override
	protected void UnLoadImpl() {
		((Sound)resObj).dispose();
	}

	@Override
	public String GetResName() {
		return "Sound";
	}

}
