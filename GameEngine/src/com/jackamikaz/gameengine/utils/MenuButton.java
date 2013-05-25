package com.jackamikaz.gameengine.utils;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Vector2;
import com.jackamikaz.gameengine.DisplayedEntity;
import com.jackamikaz.gameengine.Engine;
import com.jackamikaz.gameengine.InputEntity;

public class MenuButton implements InputWatcher, InputEntity, DisplayedEntity {

	private NinePatch ninepatch;
	private BitmapFont font;
	private String text;
	private Vector2 pos;
	private Vector2 bounds;
	private float padding;
	
	private boolean buttonIsPressed = false;
	private boolean changedThisFrame = false;
	private boolean wasClicking = false;
	
	public MenuButton(NinePatch patch, BitmapFont f, String t, float x, float y, float extrapadding) {
		ninepatch = patch;
		font = f;
		text = t;
		
		TextBounds tb = font.getBounds(text);
		bounds = new Vector2(tb.width, tb.height);
		pos = new Vector2(x-bounds.x*0.5f,y-bounds.y*0.5f);
		
		padding = extrapadding;
	}

	@Override
	public void Display(float lerp) {
		SpriteBatch batch = Engine.Batch();
		
		batch.begin();
		batch.enableBlending();
		
		ninepatch.draw(batch, pos.x - padding, pos.y - padding, bounds.x + padding*2, bounds.y + padding*2);
		font.draw(batch, text, pos.x, pos.y + bounds.y);
		
		batch.end();
	}

	@Override
	public int GetDisplayRank() {
		return DisplayOrder.Render2D.ordinal();
	}

	@Override
	public void NewInput(Input input) {
		boolean isClicking = input.isButtonPressed(Buttons.LEFT);
		
		float x = input.getX();
		float y = Engine.DisplayMaster().GetHeight() - input.getY();
		boolean isInside =
				   x > pos.x - padding
				&& x < pos.x + bounds.x + padding*2
				&& y > pos.y - padding
				&& y < pos.y + bounds.y + padding*2;
		
		boolean isNowPressed = buttonIsPressed;
		
		if (isInside && isClicking && !wasClicking)
			isNowPressed = true;
		if (!isClicking && wasClicking)
			isNowPressed = false;
		
		changedThisFrame = (isNowPressed != buttonIsPressed);
		buttonIsPressed = isNowPressed;
		
		wasClicking = isClicking;
	}
	
	public boolean wasPressed() {return buttonIsPressed && changedThisFrame;}
	public boolean wasReleased() {return !buttonIsPressed && changedThisFrame;}
	
	public boolean isPressed() {return buttonIsPressed;}
	public boolean isReleased() {return !buttonIsPressed;}

}
