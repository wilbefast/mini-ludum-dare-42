package com.baptemedujeu.minild42;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.jackamikaz.gameengine.Engine;

public class H2G2Game implements ApplicationListener 
{
	public static OrthographicCamera camera;
	public static Level level;
	
	// GUI
	private SpriteBatch gui;
	private Texture gui_texture;
	private Sprite mothership_arrow;
	
	@Override
	public void create()
	{
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		Engine.Init();
		Engine.ResourceManager().LoadResourcesFile("resources.txt");
		
		// GUI sprites
		gui = new SpriteBatch();
		gui_texture = Engine.ResourceManager().GetTexture("mothership_arrow");
		// ... mothership arrow
		TextureRegion region = new TextureRegion(gui_texture, 0, 0, 64, 64);
		mothership_arrow = new Sprite(region);
		mothership_arrow.setPosition(0.0f, 0.0f);
		mothership_arrow.setSize(1, mothership_arrow.getHeight() / mothership_arrow.getWidth());
		mothership_arrow.setOrigin(mothership_arrow.getWidth() / 2, mothership_arrow.getHeight() / 2);

		// camera
		camera = new OrthographicCamera(13.0f, 13.0f * h / w);
		
		// level
		level = new Level("test.tmx");
	}

	@Override
	public void dispose()
	{
		Engine.Quit();
	}

	private static final Matrix4 IDENTITY = new Matrix4();
	
	@Override
	public void render()
	{
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// draw and update game world
		SpriteBatch world = Engine.Batch();
		world.begin();
			world.setProjectionMatrix(camera.combined);
			Engine.ClassicLoop();
		world.end();
		
		// gui
		gui.begin();
			mothership_arrow.draw(gui);
		gui.end();
		
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
