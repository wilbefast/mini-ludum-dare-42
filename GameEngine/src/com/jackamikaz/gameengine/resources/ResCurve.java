package com.jackamikaz.gameengine.resources;

import com.badlogic.gdx.Gdx;
import com.jackamikaz.gameengine.assets.Curve;

public class ResCurve extends Resource {

	/*public ResCurve(String[] f) {
		super(f);
	}*/

	@Override
	public void Load() {
		Curve curve = new Curve(Gdx.files.internal(files[0]));
		
		resObj = curve;
	}

	@Override
	public void UnLoadImpl() {
	}

	@Override
	public String GetResName() {
		return "Curve";
	}

}
