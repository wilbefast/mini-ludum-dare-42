package com.jackamikaz.gameengine.utils;

import com.jackamikaz.gameengine.EntitySet;

public class PushAction implements Action {

	private EntitySet entitySet;
	
	public PushAction(EntitySet entities) {
		entitySet = entities;
	}

	@Override
	public void DoAction() {
		entitySet.Push();
	}

}
