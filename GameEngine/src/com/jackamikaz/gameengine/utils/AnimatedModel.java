package com.jackamikaz.gameengine.utils;

import com.badlogic.gdx.graphics.g3d.model.skeleton.SkeletonAnimation;
import com.badlogic.gdx.graphics.g3d.model.skeleton.SkeletonModel;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.jackamikaz.gameengine.DisplayedEntity;
import com.jackamikaz.gameengine.Engine;
import com.jackamikaz.gameengine.UpdatedEntity;

public class AnimatedModel implements UpdatedEntity, DisplayedEntity {

	public SkeletonModel model;
	public ShaderProgram shader;
	
	public Matrix4 localTransform;
	public float animSpeed;
	
	private String currentAnim;
	private float totalDuration;
	
	float t = 1.0f;
	
	public AnimatedModel(SkeletonModel m, ShaderProgram s) {
		
		model = m;
		shader = s;
		
		currentAnim = "";
		totalDuration = 0.0f;
		
		localTransform = new Matrix4();
		localTransform.idt();
		animSpeed = 1.0f;
	}
	
	@Override
	public int GetDisplayRank() {
		return DisplayOrder.Render3D.ordinal();
	}

	@Override
	public void Display(float lerp) {
		
		if (model != null) {
			MatrixStack mat = Engine.DisplayMaster().matrixStack;
			
			mat.Push();
			mat.Peek().mul(localTransform);
			
			ShaderProgram s = Engine.DisplayMaster().GetCorrectShader(shader);
			s.begin();
			s.setUniformMatrix("view", mat.Peek());
			model.render(s);
			s.end();
			mat.Pop();
		}
	}

	@Override
	public void Update(float deltaT) {
		
		if (totalDuration > 0.0f) {
			t += deltaT * 24.0f * animSpeed;
			if (t > totalDuration)
				t = 1.0f;
			
			model.setAnimation(currentAnim, t, true);
		}
	}
	
	public void SetAnim(String name) {
		
		if (name != currentAnim) {
			
			t = 1.0f;
			SkeletonAnimation anim = model.skeleton.animations.get(name);
			if (anim != null) {
				totalDuration = anim.totalDuration;
				currentAnim = name;
			}
			else {
				totalDuration = 0.0f;
				currentAnim = "";
				model.setBindPose();
			}
		}
	}
}
