package com.jackamikaz.gameengine.utils;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.jackamikaz.gameengine.Engine;

public class Math3D {
	
	private static Matrix4 tmpMat = new Matrix4();
	
	public static boolean IsMouseAtPos(final Vector3 pos, final Vector2 mousePos, final Matrix4 viewProj)
	{
		Vector2.tmp2.set(SpaceToScreen(pos,viewProj)).sub(mousePos);
		return Vector2.tmp2.len2() < 25.0f;
	}
	
	public static Vector2 SpaceToScreen(final Vector3 spacepos, final Matrix4 viewProj)
	{
		Vector3 tr = spacepos.cpy().prj(viewProj);
		
		Vector2.tmp3.x = (tr.x + 1.0f)*0.5f*Engine.DisplayMaster().GetWidth();
		Vector2.tmp3.y = (tr.y + 1.0f)*0.5f*Engine.DisplayMaster().GetHeight();
		
		return Vector2.tmp3;
	}
	
	public static void FollowMouseAlongLine(Matrix4 proj, Matrix4 view, Vector2 mousePos, Vector3 origPos, Vector3 dirPos, Vector3 out)
	{
		float normMX = (mousePos.x / Engine.DisplayMaster().GetWidth()) * 2.0f - 1.0f;
		float normMY = (mousePos.y / Engine.DisplayMaster().GetHeight()) * 2.0f - 1.0f;
		
		tmpMat.set(view).inv();
		
		float factor = 1.0f;
		
		Vector3 mouseray = Vector3.tmp2.set(normMX*(proj.val[Matrix4.M11]/proj.val[Matrix4.M00])*factor, normMY*factor, -proj.val[Matrix4.M11]*factor);
		mouseray.mul(tmpMat);
		
		Vector3 campos = Vector3.tmp3.set(tmpMat.val[Matrix4.M03],tmpMat.val[Matrix4.M13],tmpMat.val[Matrix4.M23]);
		
		GetClosestPointBetweenTwoLines(origPos, dirPos, campos, mouseray, out);
	}
	
	//http://paulbourke.net/geometry/lineline3d/
	static public void GetClosestPointBetweenTwoLines(Vector3 p1, Vector3 p2, Vector3 p3, Vector3 p4, Vector3 out)
	{
		//float dxxxx = (p.x - p.x)*(p.x - p.x) + (p.y - p.y)*(p.y - p.y) + (p.z - p.z)*(p.z - p.z);
		float d1343 = (p1.x - p3.x)*(p4.x - p3.x) + (p1.y - p3.y)*(p4.y - p3.y) + (p1.z - p3.z)*(p4.z - p3.z);
		float d4321 = (p4.x - p3.x)*(p2.x - p1.x) + (p4.y - p3.y)*(p2.y - p1.y) + (p4.z - p3.z)*(p2.z - p1.z);
		float d1321 = (p1.x - p3.x)*(p2.x - p1.x) + (p1.y - p3.y)*(p2.y - p1.y) + (p1.z - p3.z)*(p2.z - p1.z);
		float d4343 = (p4.x - p3.x)*(p4.x - p3.x) + (p4.y - p3.y)*(p4.y - p3.y) + (p4.z - p3.z)*(p4.z - p3.z);
		float d2121 = (p2.x - p1.x)*(p2.x - p1.x) + (p2.y - p1.y)*(p2.y - p1.y) + (p2.z - p1.z)*(p2.z - p1.z);
		
		float mua = ( d1343 * d4321 - d1321 * d4343 ) / ( d2121 * d4343 - d4321 * d4321 );
		
		// out = p1 + mua ( p2 - p1 )
		out.set(p2).sub(p1).mul(mua).add(p1);
	}
}
