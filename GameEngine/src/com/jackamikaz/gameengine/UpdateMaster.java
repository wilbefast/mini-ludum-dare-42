package com.jackamikaz.gameengine;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import com.jackamikaz.gameengine.utils.StackOfCollections;

public class UpdateMaster extends StackOfCollections<UpdatedEntity> {
	
	protected LinkedList<UpdatedEntity> NewCollection() {
		return new LinkedList<UpdatedEntity>();
	}
	
	public void Update(float deltaT) {
		if (fakelag > 0.0f) {
			consumedLag -= deltaT;
			if (consumedLag <= 0.0f) {
				consumedLag = fakelag;
				deltaT = fakelag;
			}
			else
				return;
		}
		
		Collection<UpdatedEntity> listEntities = AdjustAndPeek();
		
		Iterator<UpdatedEntity> it = listEntities.iterator();
		while (it.hasNext()) {
			it.next().Update(deltaT*timeStretch);
		}
	}
	
	private float timeStretch = 1.0f;
	private float fakelag = 0.0f;
	private float consumedLag = 0.0f;
	
	public void StretchTime(float stretch) {
		if (stretch > 0.01f)
			timeStretch = stretch;
	}
	
	public float GetTimeStretch() {
		return timeStretch;
	}
	
	public void FakeLag(float deltaT) {
		fakelag = deltaT;
	}
}
