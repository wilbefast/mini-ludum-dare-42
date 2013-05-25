package com.jackamikaz.gameengine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jackamikaz.gameengine.resources.ResourceManager;

public class Engine {

	private static Engine instance = null;
	
	private ResourceManager resourceManager;
	
	private InputMaster inputMaster;
	private UpdateMaster updateMaster;
	private DisplayMaster displayMaster;
	private SpriteBatch batch;
	
	public Engine() {
		resourceManager = new ResourceManager();
		inputMaster = new InputMaster();
		updateMaster = new UpdateMaster();
		displayMaster = new DisplayMaster();
		batch = new SpriteBatch();
	}
	
	public static ResourceManager ResourceManager() {return instance.resourceManager;}
	public static InputMaster InputMaster() {return instance.inputMaster;}
	public static UpdateMaster UpdateMaster() {return instance.updateMaster;}
	public static DisplayMaster DisplayMaster() {return instance.displayMaster;}
	public static SpriteBatch Batch() {return instance.batch;}
	
	public static void Init() {
		instance = new Engine();
	}
	
	public static void Quit() {
		instance.resourceManager.ClearResources();
		instance.batch.dispose();
		instance = null;
	}
	
	public static void PushAll() {
		instance.inputMaster.Push();
		instance.updateMaster.Push();
		instance.displayMaster.Push();
	}
	
	public static void PopAll() {
		instance.inputMaster.Pop();
		instance.updateMaster.Pop();
		instance.displayMaster.Pop();
	}
	
	public static void Loop(float deltaT) {
		instance.inputMaster.Update();
		instance.updateMaster.Update(deltaT);
		instance.displayMaster.Display(0.0f);
	}
	
	public static void ClassicLoop() {
		Loop(Gdx.graphics.getDeltaTime());
	}
}
