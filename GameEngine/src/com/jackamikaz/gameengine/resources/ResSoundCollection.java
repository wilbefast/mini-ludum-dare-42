package com.jackamikaz.gameengine.resources;

import java.util.Vector;

import com.badlogic.gdx.audio.Sound;
import com.jackamikaz.gameengine.Engine;
import com.jackamikaz.gameengine.assets.SoundCollection;

public class ResSoundCollection extends Resource {

	/*public ResSoundCollection(String[] f) {
		super(f);
	}*/
	
	@Override
	public void Load() {
		Vector<Sound> sounds = new Vector<Sound>();
		
		for(int i=0; i<files.length; ++i) {
			Sound snd = Engine.ResourceManager().GetSound(files[i]);
			if (snd != null) {
				sounds.add(snd);
			}
		}
		
		resObj = new SoundCollection(sounds.toArray(new Sound[sounds.size()]));
	}

	@Override
	protected void UnLoadImpl() {
	}

	
	@Override
	public String GetResName() {
		return "SoundCollection";
	}


}
