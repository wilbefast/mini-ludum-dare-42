package com.jackamikaz.gameengine.utils;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class IMRUtils {

	private static Vector3 A = new Vector3(0,0,0);
	private static Vector3 B = new Vector3(1,0,0);
	private static Vector3 C = new Vector3(1,1,0);
	private static Vector3 D = new Vector3(0,1,0);
	
	private static Vector3 E = new Vector3(0,0,1);
	private static Vector3 F = new Vector3(1,0,1);
	private static Vector3 G = new Vector3(1,1,1);
	private static Vector3 H = new Vector3(0,1,1);
	
	public static Matrix4 tmpMat = new Matrix4();
	public static Matrix4 tmpMat2 = new Matrix4();
	
	public static void WireCube(ImmediateModeRenderer renderer, Matrix4 mat, float r, float g, float b, float a) {
		renderer.begin(mat, GL10.GL_LINES);
		
		rendererLine(renderer,A,B,r,g,b,a);
		rendererLine(renderer,B,C,r,g,b,a);
		rendererLine(renderer,C,D,r,g,b,a);
		rendererLine(renderer,D,A,r,g,b,a);
		
		rendererLine(renderer,E,F,r,g,b,a);
		rendererLine(renderer,F,G,r,g,b,a);
		rendererLine(renderer,G,H,r,g,b,a);
		rendererLine(renderer,H,E,r,g,b,a);
		
		rendererLine(renderer,A,E,r,g,b,a);
		rendererLine(renderer,B,F,r,g,b,a);
		rendererLine(renderer,C,G,r,g,b,a);
		rendererLine(renderer,D,H,r,g,b,a);
		
		renderer.end();
	}
	
	private static void rendererLine(ImmediateModeRenderer renderer, Vector3 p1, Vector3 p2, float r, float g, float b, float a) {
		renderer.color(r,g,b,a);
		renderer.vertex(p1.x, p1.y, p1.z);
		
		renderer.color(r,g,b,a);
		renderer.vertex(p2.x, p2.y, p2.z);
	}
	
	public static void Cube(ImmediateModeRenderer renderer, Matrix4 mat, float r, float g, float b, float a) {
		renderer.begin(mat, GL10.GL_TRIANGLES);
		
		rendererFace(renderer, D,C,B,A, r,g,b,a);
		rendererFace(renderer, E,F,G,H, r,g,b,a);
		rendererFace(renderer, A,B,F,E, r,g,b,a);
		rendererFace(renderer, H,G,C,D, r,g,b,a);
		rendererFace(renderer, E,H,D,A, r,g,b,a);
		rendererFace(renderer, B,C,G,F, r,g,b,a);
		
		renderer.end();
	}
	
	private static void rendererFace(ImmediateModeRenderer renderer, Vector3 p1, Vector3 p2, Vector3 p3, Vector3 p4, float r, float g, float b, float a) {
		renderer.color(r,g,b,a);
		renderer.vertex(p1.x, p1.y, p1.z);
		
		renderer.color(r,g,b,a);
		renderer.vertex(p2.x, p2.y, p2.z);
		
		renderer.color(r,g,b,a);
		renderer.vertex(p3.x, p3.y, p3.z);
		
		renderer.color(r,g,b,a);
		renderer.vertex(p1.x, p1.y, p1.z);
		
		renderer.color(r,g,b,a);
		renderer.vertex(p3.x, p3.y, p3.z);
		
		renderer.color(r,g,b,a);
		renderer.vertex(p4.x, p4.y, p4.z);
	}
	
	public static void Square(ImmediateModeRenderer renderer, Vector2 pos2D, float halfSize, Matrix4 viewProj, float r, float g, float b) {
		
		renderer.begin(viewProj, GL10.GL_LINE_STRIP);
		
		renderer.color(r, g, b, 1);
		renderer.vertex(pos2D.x-halfSize, pos2D.y-halfSize, 0.0f);
		
		renderer.color(r, g, b, 1);
		renderer.vertex(pos2D.x+halfSize, pos2D.y-halfSize, 0.0f);
		
		renderer.color(r, g, b, 1);
		renderer.vertex(pos2D.x+halfSize, pos2D.y+halfSize, 0.0f);
		
		renderer.color(r, g, b, 1);
		renderer.vertex(pos2D.x-halfSize, pos2D.y+halfSize, 0.0f);
		
		renderer.color(r, g, b, 1);
		renderer.vertex(pos2D.x-halfSize, pos2D.y-halfSize, 0.0f);
		
		renderer.end();
	}
}
