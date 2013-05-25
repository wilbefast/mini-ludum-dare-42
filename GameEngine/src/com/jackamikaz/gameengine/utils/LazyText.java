package com.jackamikaz.gameengine.utils;

import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.jackamikaz.gameengine.DisplayedEntity;
import com.jackamikaz.gameengine.Engine;

public class LazyText implements DisplayedEntity {

	private Matrix4 mat;
	
	private class FrameText {
		public Vector2 position;
		public String text;
		
		public FrameText(Vector2 pos, String txt) {
			position = pos.cpy();
			text = txt;
		}
	}
	
	private Vector<FrameText> texts;
	private BitmapFont font;
	
	public LazyText(BitmapFont f) {
		texts = new Vector<LazyText.FrameText>();
		font = f;
		mat = new Matrix4();
	}
	
	@Override
	public void Display(float lerp) {
		
		if (!texts.isEmpty()) {
			Gdx.gl.glDisable(GL10.GL_DEPTH_TEST);
			Gdx.gl.glDisable(GL10.GL_CULL_FACE);
			Gdx.gl.glDisable(GL10.GL_LIGHTING);
			Gdx.gl.glDisable(GL10.GL_LIGHT0);
			Gdx.gl.glDisable(GL11.GL_TEXTURE_2D);
			Gdx.gl.glDisable(GL10.GL_BLEND);
			
			SpriteBatch batch = Engine.Batch();
			
			mat.setToOrtho2D(0, 0, Engine.DisplayMaster().GetWidth(), Engine.DisplayMaster().GetHeight());
			batch.setProjectionMatrix(mat);
			batch.setTransformMatrix(mat.idt());
			
			batch.begin();
			batch.enableBlending();
			
			for(FrameText t : texts) {
				font.drawMultiLine(batch, t.text, t.position.x, t.position.y);
			}
			
			batch.end();
			
			texts.clear();
		}
	}

	@Override
	public int GetDisplayRank() {
		return DisplayOrder.Render2DUltimate.ordinal();
	}
	
	public void Print(String text, Vector2 position) {
		texts.add(new FrameText(position, text));
	}

	public void Print(String text, float x, float y) {
		Print(text, Vector2.tmp.set(x,y));
	}
}
