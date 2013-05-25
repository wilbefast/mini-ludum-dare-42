package com.jackamikaz.gameengine.resources;

public abstract class Resource {

	protected String[] files;
	protected Object resObj;
	
	public Resource() {
		files = null;
		resObj = null;
	}
	
	public void SetFiles(String[] f) {
		files = f;
	}
	
	public abstract void Load();
	public void UnLoad() {
		if (resObj != null) {
			UnLoadImpl();
			resObj = null;
		}
	}
	
	protected abstract void UnLoadImpl();
	
	public Object Get() {
		if (resObj == null)
			Load();
		return resObj;
	}
	
	public abstract String GetResName();
}
