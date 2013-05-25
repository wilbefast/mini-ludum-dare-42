package com.jackamikaz.gameengine;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.jackamikaz.gameengine.utils.MatrixStack;
import com.jackamikaz.gameengine.utils.SortedList;
import com.jackamikaz.gameengine.utils.StackOfCollections;


public class DisplayMaster extends StackOfCollections<DisplayedEntity> {

	private class DispEntComp implements Comparator<DisplayedEntity> {

		@Override
		public int compare(DisplayedEntity a, DisplayedEntity b) {
			return a.GetDisplayRank() - b.GetDisplayRank();
		}
		
	}
	
	private ShaderProgram replacementShader = null;
	private DispEntComp comparator = null;
	public MatrixStack matrixStack = new MatrixStack();
	
	protected SortedList<DisplayedEntity> NewCollection() {
		if (comparator == null)
			comparator = new DispEntComp();
		return new SortedList<DisplayedEntity>(comparator);
	}
	
	public void Display(float lerp) {
		Collection<DisplayedEntity> listEntities = AdjustAndPeek();
		
		Iterator<DisplayedEntity> it = listEntities.iterator();
		while (it.hasNext()) {
			it.next().Display(lerp);
		}
	}
	
	public void ExecuteCustomLoop(int[] displayTypes, float lerp) {
		Collection<DisplayedEntity> listEntities = Peek();
		
		int curDispType = 0;
		
		Iterator<DisplayedEntity> it = listEntities.iterator();
		DisplayedEntity itnext = null;
		
		while (it.hasNext() && curDispType < displayTypes.length) {
			
			if (itnext == null) // new itnext needs to be processed
				itnext = it.next();
			
			int curDispRank = itnext.GetDisplayRank();
			
			if (displayTypes[curDispType] < curDispRank) {
				++curDispType;
			}
			else if (displayTypes[curDispType] == curDispRank) {
				itnext.Display(lerp);
				itnext = null; //itnext processed;
			}
			else {
				itnext = null; //itnext ignored
			}
		}
	}
	
	public void SetReplacementShader(ShaderProgram s) {
		replacementShader = s;
	}
	
	public ShaderProgram GetCorrectShader(ShaderProgram s) {
		return replacementShader == null ? s : replacementShader;
	}
	
	private float width;
	private float height;
	
	public float GetWidth() {
		return width;
	}
	
	public float GetHeight() {
		return height;
	}
	
	public float GetAspectRatio() {
		return width / height;
	}
	
	public void UdpateWidthHeight(float w, float h) {
		width = w;
		height = h;
	}
}
