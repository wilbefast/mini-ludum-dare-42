package com.jackamikaz.gameengine.utils;

public class InputCondition implements Condition {

	private InputWatcher inputWatcher;
	
	public InputCondition(InputWatcher iw) {
		inputWatcher = iw;
	}
	
	@Override
	public boolean Test() {
		return inputWatcher.wasPressed();
	}
	
}
