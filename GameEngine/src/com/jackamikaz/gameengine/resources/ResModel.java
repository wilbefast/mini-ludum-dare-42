package com.jackamikaz.gameengine.resources;

import java.io.IOException;
import java.io.InputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.g3d.materials.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.model.Model;
import com.jackamikaz.gameengine.Engine;
import com.jackamikaz.gameengine.utils.GmfLoader;

public class ResModel extends Resource {

	/*public ResModel(String[] f) {
		super(f);
	}*/

	@Override
	public void Load() {
		
		Model model = null;
		
		try {
			InputStream in = Gdx.files.internal(files[0]).read();
			model = GmfLoader.loadGmf(in);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		resObj = model;
		
		if (model != null) {
			
			Material mat = new Material("texMat"+files[1],
				new TextureAttribute(Engine.ResourceManager().GetTexture(files[1]), 0, "tex"));
			
			model.setMaterial(mat);
		}
	}

	@Override
	protected void UnLoadImpl() {
		((Model)resObj).dispose();		
	}

	@Override
	public String GetResName() {
		return "Model";
	}


}
