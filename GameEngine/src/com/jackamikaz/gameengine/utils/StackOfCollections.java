package com.jackamikaz.gameengine.utils;

import java.util.Collection;
import java.util.Stack;

public abstract class StackOfCollections<obj> implements IStackOfCollections<obj> {

	private class Collec<objj> {
		
		public Collection<objj> collection;
		
		public Collection<objj> toBeAdded;
		public Collection<objj> toBeRemoved;
		
		public Collec(Collection<objj> c, Collection<objj> tba, Collection<objj> tbr) {
			collection = c;
			toBeAdded = tba;
			toBeRemoved = tbr;
		}
		
		public void AdjustCollection() {
			collection.addAll(toBeAdded);
			collection.removeAll(toBeRemoved);
			
			toBeAdded.clear();
			toBeRemoved.clear();
		}
	}
	
	private Stack<Collec<obj>> entities;

	protected StackOfCollections() {
		entities = new Stack<Collec<obj>>();
		Push();
	}
	
	public void Add(obj entity) {
		entities.peek().toBeAdded.add(entity);
	}
	public void Remove(obj entity) {
		entities.peek().toBeRemoved.add(entity);
	}
	
	public void Push(Collection<obj> entityList) {
		entities.push(
				new Collec<obj>(entityList,NewCollection(),NewCollection()));
	}
	public void Push() {Push(NewCollection());}
	protected abstract Collection<obj> NewCollection();
	
	public void Pop() {entities.pop();}
	
	protected Collection<obj> AdjustAndPeek() {
		Collec<obj> collec = entities.peek();
		collec.AdjustCollection();
		return collec.collection;
	}
	
	public Collection<obj> Peek() {
		return entities.peek().collection;
	}
}
