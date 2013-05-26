package com.jackamikaz.gameengine;

import com.badlogic.gdx.math.Vector2;

public interface SpatialEntity
{
	public Vector2 getPosition();
	
	public float getRadius();
	
	public float getWidth();
	
	public float getHeight();
	
	public float getRotation();
}
