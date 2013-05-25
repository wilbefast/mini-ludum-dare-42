package com.jackamikaz.gameengine.utils;

import com.jackamikaz.gameengine.InputEntity;

public interface InputWatcher extends InputEntity {
	boolean wasPressed();
	boolean wasReleased();
	
	boolean isPressed();
	boolean isReleased();
}
