package com.baptemedujeu.minild42;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jackamikaz.gameengine.Engine;

public class H2G2Game implements ApplicationListener 
{
	public static OrthographicCamera camera;
	
	@Override
	public void create()
	{
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		Engine.Init();
		Engine.ResourceManager().LoadResourcesFile("resources.txt");

		// Create player character
		Hitchhiker player = new Hitchhiker();
		
		// camera
		camera = new OrthographicCamera(25.0f, 25.0f * h / w);

		// Create ships
		new Spaceship(5.0f, 0, 5.0f);
		new Spaceship(-8.0f, 4.0f, 3.0f);
		new Spaceship(2.0f, -3.0f, 7.0f);
		
		// Create planets
		new Planet(6.0f, 2.0f);
		new Planet(-1.0f, 4.0f);
		new Planet(-5.0f, -5.0f);
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
