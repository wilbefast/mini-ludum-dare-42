package com.baptemedujeu.minild42;

import com.badlogic.gdx.math.Vector2;

public abstract class Useful
{
	public static final float lerp(float a, float b, float amount)
	{
		amount = (amount < 0) ? 0 : (amount > 1) ? 1 : amount;
		return(a*(1 - amount) + b*amount);
	}
	
	public static final void lerp(Vector2 a, Vector2 b, float amount)
	{
		a.set(lerp(a.x, b.x, amount), lerp(a.y, b.y, amount));
	}
}
