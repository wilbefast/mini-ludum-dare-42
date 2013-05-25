package com.jackamikaz.gameengine.utils;

import com.jackamikaz.gameengine.UpdatedEntity;

public class ActionOnInput implements UpdatedEntity {

	private Action action;
	private InputWatcher inputWatcher;
	
	public ActionOnInput(Action a, InputWatcher iw, float f) {
		action = a;
		inputWatcher = iw;
	}
	
	@Override
	public void Update(float deltaT) {
		
		if (inputWatcher.wasPressed()) {
			action.DoAction();
		}
		
	}

}
