package com.jackamikaz.gameengine;

import java.util.Vector;

public class EntitySet {

	public Vector<InputEntity> input;
	public Vector<UpdatedEntity> updated;
	public Vector<DisplayedEntity> displayed;
	
	public EntitySet() {
		input = new Vector<InputEntity>();
		updated = new Vector<UpdatedEntity>();
		displayed = new Vector<DisplayedEntity>();
	}
	
	public void Push() {
		Engine.InputMaster().Push(input);
		Engine.UpdateMaster().Push(updated);
		Engine.DisplayMaster().Push(displayed);
	}
}
