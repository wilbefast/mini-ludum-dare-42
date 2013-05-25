package com.jackamikaz.gameengine.utils;

import com.jackamikaz.gameengine.UpdatedEntity;

public class ActionOnCondition implements UpdatedEntity {

	private Action action;
	private Condition condition;
	
	public ActionOnCondition(Action a, Condition c) {
		action = a;
		condition = c;
	}
	
	@Override
	public void Update(float deltaT) {
		
		if (condition.Test()) {
			action.DoAction();
		}
		
	}

}
