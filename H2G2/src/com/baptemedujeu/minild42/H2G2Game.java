package com.baptemedujeu.minild42;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.jackamikaz.gameengine.Engine;

public class H2G2Game implements ApplicationListener 
{
	public static OrthographicCamera camera;
	public static Level level;
	
	// GUI
	private SpriteBatch gui;
	private Sprite mothership_arrow;
	
	// Background
	private Background bg;
	
	
	@Override
	public void create()
	{
		
		try
		{
		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		Engine.Init();
		Engine.ResourceManager().LoadResourcesFile("resources.txt");
		
		bg = new Background();
		// GUI sprites
		gui = new SpriteBatch();
		gui.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, w, h));
		Texture t = Engine.ResourceManager().GetTexture("sprites");
		// ... mothership arrow
		TextureRegion tr = new TextureRegion(t, 256, 0, 128, 128);
		mothership_arrow = new Sprite(tr);
		mothership_arrow.setSize(50.0f, 50.0f*mothership_arrow.getHeight() / mothership_arrow.getWidth());
		mothership_arrow.setOrigin(mothership_arrow.getWidth() / 2, mothership_arrow.getHeight() / 2);

		// camera
		camera = new OrthographicCamera(416.0f, 416.0f * h / w);
		//camera.rotate(180);
		
		// level
		level = new Level("hike1.tmx");
		
		// music
		Music music = Engine.ResourceManager().GetMusic("music");
		music.setLooping(true);
		//music.play();
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void dispose()
	{
		Engine.Quit();
	}

	private static final Vector2 toMothership = new Vector2();
	
	@Override
	public void render()
	{
		try
		{
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// draw and update game world
		SpriteBatch world = Engine.Batch();

			world.begin();
				world.setProjectionMatrix(camera.combined);
				bg.Display(0.0f);
				Engine.ClassicLoop();
			world.end();

		
		// gui
		
		// ... compass
		toMothership.set(level.mother.getPosition()).sub(camera.position.x, camera.position.y);
		boolean drawCompass = (toMothership.len2() > 48);
		if(drawCompass)
		{
			// ... ... position
			toMothership.nor().scl(Gdx.graphics.getWidth()*0.45f, Gdx.graphics.getHeight()*0.45f);
			mothership_arrow.setPosition(
					Gdx.graphics.getWidth()/2 - mothership_arrow.getWidth()/2 + toMothership.x,
					Gdx.graphics.getHeight()/2  - mothership_arrow.getHeight()/2 + toMothership.y);
			// ... ... angle
			mothership_arrow.setRotation(toMothership.angle() - 90);
		}
		gui.begin();
			// draw the compass showing where the objective is
			if(drawCompass)
				mothership_arrow.draw(gui);
		gui.end();
		
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}

	@Override
	public void resize(int width, int height)
	{
	//	camera.setToOrtho(false);
		camera.update();
		Engine.DisplayMaster().UdpateWidthHeight(width, height);
		gui.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, width, height));
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
