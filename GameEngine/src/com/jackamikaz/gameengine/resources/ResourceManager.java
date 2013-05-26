package com.jackamikaz.gameengine.resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.model.Model;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.jackamikaz.gameengine.assets.Curve;
import com.jackamikaz.gameengine.assets.SoundCollection;
import com.jackamikaz.gameengine.assets.SpriteLayout;

public class ResourceManager {

	private HashMap<String, Resource> map;
	private String prefix = "data/";
	
	public ResourceManager() {
		map = new HashMap<String, Resource>();
	}
	
	private Object GetImpl(String name) {
		Resource res = map.get(name);
		if (res != null)
			return res.Get();
		
		System.out.println("Resource "+name+" was not found.");
		return null;
	}
	
	public Texture GetTexture(String name) {return (Texture)(GetImpl(name));}
	public TextureRegion[] GetTextureSheet(String name) {return (TextureRegion[])(GetImpl(name));}
	public BitmapFont GetFont(String name) {return (BitmapFont)(GetImpl(name));}
	public ShaderProgram GetShader(String name) {return (ShaderProgram)(GetImpl(name));}
	public Model GetModel(String name) {return (Model)(GetImpl(name));}
	public Curve GetCurve(String name) {return (Curve)(GetImpl(name));}
	public SpriteLayout GetLayout(String name) {return (SpriteLayout)(GetImpl(name));}
	public Sound GetSound(String name) {return (Sound)(GetImpl(name));}
	public SoundCollection GetSoundCollection(String name) {return (SoundCollection)(GetImpl(name));}
	public Music GetMusic(String name) {return (Music)(GetImpl(name));}
	public float GetFloat(String name) {return (Float)(GetImpl(name));}
	
	public String[] ExtractResourceNameOfType(Class<?> cl) {
		
		Vector<String> vec = new Vector<String>();
		
		for(String name : map.keySet()) {
			Resource res = map.get(name);
			if (res.getClass() == cl)
				vec.add(name);
		}
		
		Collections.sort(vec);
		
		return vec.toArray(new String[1]);
	}
	
	public void UnloadResources() {
		for(String name : map.keySet()) {
			map.get(name).UnLoad();
		}
	}
	
	public void ClearResources() {
		UnloadResources();
		map.clear();
	}
	
	public void LoadResourcesFile(String filename) {
		LoadResourcesFile(filename, false);
	}
	
	public void LoadResourcesFile(String filename, boolean preloadAll) {
		InputStream in = Gdx.files.internal(prefix+filename).read();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		try {
			line = reader.readLine();
			while (line != null) {
				String[] tokens = line.split("[ ]+");
				
				if (!AddResource(new ResTexture()			, tokens, 1))
				if (!AddResource(new ResTextureSheet()		, tokens, 0))
				if (!AddResource(new ResFont()				, tokens, 2))
				if (!AddResource(new ResShader()			, tokens, 2))
				if (!AddResource(new ResModel()				, tokens, 1))
				if (!AddResource(new ResCurve()				, tokens, 1))
				if (!AddResource(new ResSpriteLayout()		, tokens, 1))
				if (!AddResource(new ResSound()				, tokens, 1))
				if (!AddResource(new ResSoundCollection()	, tokens, 0))
				if (!AddResource(new ResMusic()				, tokens, 1))
					 AddResource(new ResFloat()				, tokens, 0);
				
				line = reader.readLine();
			}
			
			reader.close();
		} catch (IOException e) {
			
		}
	}
	
	private boolean AddResource(Resource res, String[] tokens, int prefixes) {
		if (tokens[0].equalsIgnoreCase(res.GetResName())) {
			String[] args = new String[tokens.length-2];
			
			for(int i=0; i<args.length; ++i) {
				if (i < prefixes)
					args[i] = prefix+tokens[i+2];
				else
					args[i] = tokens[i+2];
			}
			
			res.SetFiles(args);
			
			map.put(tokens[1], res);
			return true;
		}
		return false;
	}
}
