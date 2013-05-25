package com.jackamikaz.gameengine;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.jackamikaz.gameengine.utils.StackOfCollections;

public class InputMaster extends StackOfCollections<InputEntity> {

	protected LinkedList<InputEntity> NewCollection() {
		return new LinkedList<InputEntity>();
	}
	
	public void Update() {
		Collection<InputEntity> listEntities = AdjustAndPeek();
		
		Iterator<InputEntity> it = listEntities.iterator();
		while (it.hasNext()) {
			it.next().NewInput(Gdx.input);
		}
	}
	
}
