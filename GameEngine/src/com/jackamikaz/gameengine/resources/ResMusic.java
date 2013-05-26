package com.jackamikaz.gameengine.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class ResMusic extends Resource {

	@Override
	public void Load() {
		Music music = Gdx.audio.newMusic(Gdx.files.internal(files[0]));
		
		resObj = music;
	}

	@Override
	protected void UnLoadImpl() {
		((Music)resObj).dispose();
	}

	@Override
	public String GetResName() {
		return "Music";
	}

}
