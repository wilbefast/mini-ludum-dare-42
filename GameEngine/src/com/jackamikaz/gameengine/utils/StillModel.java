package com.jackamikaz.gameengine.utils;

import com.badlogic.gdx.graphics.g3d.model.Model;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.jackamikaz.gameengine.DisplayedEntity;
import com.jackamikaz.gameengine.Engine;

public class StillModel implements DisplayedEntity {

	public Model model;
	public ShaderProgram shader;
	
	public Matrix4 localTransform;
	
	public StillModel(Model m, ShaderProgram s) {
		model = m;
		shader = s;
		
		localTransform = new Matrix4();
		localTransform.idt();
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
	public int GetDisplayRank() {
		return DisplayOrder.Render3D.ordinal();
	}
	
}
