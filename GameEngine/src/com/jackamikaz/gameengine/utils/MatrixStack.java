package com.jackamikaz.gameengine.utils;

import com.badlogic.gdx.math.Matrix4;

public class MatrixStack {

	private Matrix4[] stack;
	private int position;
	
	public MatrixStack() {
		stack = new Matrix4[16];
		
		for(int i=0; i<16; ++i) {
			stack[i] = new Matrix4();
		}
		
		position = 0;
	}
	
	//TODO out of bound checks (I'm such a lazy pig)
	public void Push() {
		++position;
		stack[position].set(stack[position-1]);
	}
	
	public void Pop() {
		--position;
	}
	
	public Matrix4 Peek() {
		return stack[position];
	}
	
	public void Reset() {
		position = 0;
		stack[0].idt();
	}
}
