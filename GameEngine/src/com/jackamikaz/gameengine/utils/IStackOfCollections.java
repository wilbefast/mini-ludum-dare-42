package com.jackamikaz.gameengine.utils;

import java.util.Collection;

public interface IStackOfCollections<obj> {
	public void Add(obj entity);
	public void Remove(obj entity);
	
	public void Push(Collection<obj> entityList);
	public void Push();
	
	public void Pop();
}
