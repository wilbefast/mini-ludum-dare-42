package com.jackamikaz.gameengine.utils;

import com.badlogic.gdx.Input;
import com.jackamikaz.gameengine.InputEntity;

public class KeyWatcher implements InputEntity, InputWatcher {

	private boolean keyIsPressed = false;
	private boolean changedThisFrame = false;
	private int key;
	
	public KeyWatcher(int k) {
		key = k;
	}
	
	@Override
	public void NewInput(Input input) {
		NewInput(input.isKeyPressed(key));
	}
	
	public void NewInput(boolean press) {
		changedThisFrame = (press != keyIsPressed);
		keyIsPressed = press;
	}

	public boolean wasPressed() {return keyIsPressed && changedThisFrame;}
	public boolean wasReleased() {return !keyIsPressed && changedThisFrame;}
	
	public boolean isPressed() {return keyIsPressed;}
	public boolean isReleased() {return !keyIsPressed;}
}
