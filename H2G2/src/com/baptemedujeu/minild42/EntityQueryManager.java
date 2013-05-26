package com.baptemedujeu.minild42;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.jackamikaz.gameengine.Engine;
import com.jackamikaz.gameengine.UpdatedEntity;

public abstract class EntityQueryManager
{
	//! NESTING
	public static abstract class Query
	{
		public abstract float evaluate(UpdatedEntity e);
	}
	
	
	//! STATIC VARIABLES
	private static List<UpdatedEntity> queryResult = new LinkedList<UpdatedEntity>();
	
	
	//! STATIC METHODS
	public static Collection<UpdatedEntity> getAll()
	{
		queryResult.clear();
		queryResult.addAll(Engine.UpdateMaster().AdjustAndPeek());
		
		return queryResult;
	}
	
	public static Collection<UpdatedEntity> getMatching(Query qry)
	{
		Collection<UpdatedEntity> all = Engine.UpdateMaster().AdjustAndPeek();
		queryResult.clear();
		
		for(UpdatedEntity e : all)
			if(qry.evaluate(e) > 0)
				queryResult.add(e);
		
		return queryResult;
	}
	
	public static int countMatching(Query qry)
	{
		return getMatching(qry).size();
	}
	
	private static UpdatedEntity getMinMax(Query qry, boolean max)
	{
		Collection<UpdatedEntity> all = Engine.UpdateMaster().AdjustAndPeek();
		
		float minmax_val = (max ? -Float.MAX_VALUE : Float.MAX_VALUE) ;
		UpdatedEntity minmax_entity = null;
		
		for(UpdatedEntity e : all)
		{
			float entity_val = qry.evaluate(e);
			if((max && entity_val > minmax_val) || (!max && entity_val < minmax_val))
			{
				minmax_entity = (e);
				minmax_val = entity_val;
			}
		}
		
		return minmax_entity;
	}
	
	public static UpdatedEntity getMax(Query qry)
	{
		return getMinMax(qry, true);
	}
	
	public static UpdatedEntity getMin(Query qry)
	{
		return getMinMax(qry, false);
	}
}
