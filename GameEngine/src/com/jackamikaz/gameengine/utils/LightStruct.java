package com.jackamikaz.gameengine.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;

public class LightStruct {
	
	public Color color = new Color(1.0f,1.0f,1.0f,1.0f);
	public Vector3 position = new Vector3();
	public Vector3 direction = new Vector3(0.0f,0.0f,1.0f);
	
	public boolean enabled = false;
	public boolean hasShadowMap = false;
	public float radius = 0.0f;
	public float attenuation = 0.0f;
	public float spotCosAngle = 0.0f;
	public float spotCosAttenuation = 0.0f;
	
	public void SetUniforms(ShaderProgram s, int index) {
		s.begin();
		
		String prefix = "lights["+((char)('0'+index))+"].";
		s.setUniformf(prefix+"color", color);
		s.setUniformf(prefix+"position", position);
		s.setUniformf(prefix+"direction", direction);
		s.setUniformi(prefix+"enabled", enabled ? 1 : 0);
		s.setUniformf(prefix+"radius", radius);
		s.setUniformf(prefix+"attenuation", attenuation);
		s.setUniformf(prefix+"spotCosAngle", spotCosAngle);
		s.setUniformf(prefix+"spotCosAttenuation", spotCosAttenuation);
		s.setUniformi(prefix+"hasShadowMap", hasShadowMap ? 1 : 0);
		s.setUniformf(prefix+"shadowMap", 1+index);
		
		s.end();
	}
	
	public void BindShadowMap(Texture sm, int index) {
		sm.bind(1+index);
		Gdx.gl.glActiveTexture(GL10.GL_TEXTURE0);
	}
}
