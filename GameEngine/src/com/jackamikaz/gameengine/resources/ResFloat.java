package com.jackamikaz.gameengine.resources;

public class ResFloat extends Resource {

	@Override
	public void Load() {
		Float f = Float.parseFloat(files[0]);
		
		resObj = f;
	}

	@Override
	protected void UnLoadImpl() {

	}

	@Override
	public String GetResName() {
		return "Float";
	}

}
