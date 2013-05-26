package com.baptemedujeu.minild42;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.jackamikaz.gameengine.Engine;
import com.jackamikaz.gameengine.SpatialEntity;
import com.jackamikaz.gameengine.UpdatedEntity;

public abstract class EntityQueryManager
{
	//! NESTING
	public static abstract class Query
	{
		public abstract float evaluate(UpdatedEntity e);
	}
	
	public static class CollisionQuery extends Query
	{
		Vector2 colliderPos;
		float colliderRadius;
		
		public CollisionQuery(Vector2 _colliderPos, float _colliderRadius)
		{
			colliderPos = _colliderPos;
			colliderRadius = _colliderRadius;
		}
		
		@Override
		public float evaluate(UpdatedEntity e)
		{
			if(e instanceof SpatialEntity)
			{
				SpatialEntity se = (SpatialEntity)e;
				
				Vector2 entityPos = se.getPosition();
				float entityRadius = se.getRadius();
				
				return (entityPos.dst(colliderPos) < colliderRadius + entityRadius ? 
									1.0f : 0.0f);
			}
			else
				return 0;
		}
		
	}
	
	public static class TypedCollisionQuery extends CollisionQuery
	{
		private Class<?> collisionType;

		public TypedCollisionQuery(Vector2 _colliderPos, float _colliderRadius, 
																Class<?> _colliderType)
		{
			super(_colliderPos, _colliderRadius);
			collisionType = _colliderType;
		}
		
		public float evaluate(UpdatedEntity e)
		{
			return (collisionType.isInstance(e)) ? super.evaluate(e) : 0.0f;
		}
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
