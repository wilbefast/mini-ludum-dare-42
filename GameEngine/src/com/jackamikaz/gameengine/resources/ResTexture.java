package com.jackamikaz.gameengine.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;

public class ResTexture extends Resource {

	/*public ResTexture(String[] f) {
		super(f);
	}*/

	@Override
	public void Load() {
		Texture texture = new Texture(Gdx.files.internal(files[0]));
		
		TextureFilter filter = TextureFilter.Linear;
		TextureWrap wrap = TextureWrap.Repeat;
		
		if (files.length >= 2) {
			for (TextureFilter tf : TextureFilter.values()) {
				if (files[1].equalsIgnoreCase(tf.name())) {
					filter = tf;
					break;
				}
			}
		}
		
		if (files.length >= 3) {
			for (TextureWrap tw : TextureWrap.values()) {
				if (files[2].equalsIgnoreCase(tw.name())) {
					wrap = tw;
					break;
				}
			}
		}
		
		texture.setFilter(filter,filter);
		texture.setWrap(wrap,wrap);
		
		resObj = texture;
	}

	@Override
	public void UnLoadImpl() {
		((Texture)resObj).dispose();
	}

	@Override
	public String GetResName() {
		return "Texture";
	}


}
