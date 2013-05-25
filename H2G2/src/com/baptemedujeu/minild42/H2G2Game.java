package com.baptemedujeu.minild42;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jackamikaz.gameengine.Engine;

public class H2G2Game implements ApplicationListener 
{
	public static OrthographicCamera camera;
	private Hitchhiker var_hitchhiker ;
	@Override
	public void create()
	{
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		Engine.Init();
		Engine.ResourceManager().LoadResourcesFile("resources.txt");

		camera = new OrthographicCamera(1, h / w);
		
		// Create player character
		var_hitchhiker = new Hitchhiker();
		Engine.DisplayMaster().Add(var_hitchhiker);
		Engine.UpdateMaster().Add(var_hitchhiker);
		Engine.InputMaster().Add(var_hitchhiker);
		
		// Create ships
		Spaceship s = new Spaceship(0, 0);
		Engine.DisplayMaster().Add(s);
		Engine.UpdateMaster().Add(s);
	}

	@Override
	public void dispose()
	{
		Engine.Quit();
	}

	@Override
	public void render()
	{
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		SpriteBatch batch = Engine.Batch();
		batch.setProjectionMatrix(camera.combined);

		Engine.ClassicLoop();
	}

	@Override
	public void resize(int width, int height)
	{
	//	camera.setToOrtho(false);
		camera.update();
		Engine.DisplayMaster().UdpateWidthHeight(width, height);
	}

	@Override
	public void pause()
	{
	}

	@Override
	public void resume()
	{
	}
}
