package com.jackamikaz.gameengine.utils;

import com.badlogic.gdx.Input;
import com.jackamikaz.gameengine.InputEntity;

public class ButtonWatcher implements InputEntity, InputWatcher {

	private boolean buttonIsPressed = false;
	private boolean changedThisFrame = false;
	private int button;
	
	public ButtonWatcher(int b) {
		button = b;
	}
	
	@Override
	public void NewInput(Input input) {
		
		boolean isNowPressed = input.isButtonPressed(button);
		
		changedThisFrame = (isNowPressed != buttonIsPressed);
		
		buttonIsPressed = isNowPressed;
	}

	public boolean wasPressed() {return buttonIsPressed && changedThisFrame;}
	public boolean wasReleased() {return !buttonIsPressed && changedThisFrame;}
	
	public boolean isPressed() {return buttonIsPressed;}
	public boolean isReleased() {return !buttonIsPressed;}
}
