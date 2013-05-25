package com.jackamikaz.gameengine.assets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import com.badlogic.gdx.files.FileHandle;
import com.jackamikaz.gameengine.utils.LoopType;

public class Curve {

	public class CurveKey {	
		public float t=0.0f;
		public float v=0.0f;
		
		public CurveKey(float time, float value) {
			t = time;
			v = value;
		}
	}
	
	public CurveKey[] keys;
	public LoopType loopType;
	
	public Curve() {
		keys = new CurveKey[0];
	}
	
	public Curve(FileHandle file) {
		try {
			InputStream in = file.read();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			line = reader.readLine();
			String[] tokens = line.split("[ ]+");
			
			if (tokens[0].equalsIgnoreCase("loop"))
				loopType = LoopType.Loop;
			else if (tokens[0].equalsIgnoreCase("pingpong"))
				loopType = LoopType.PingPong;
			else
				loopType = LoopType.None;
			
			Vector<CurveKey> v = new Vector<CurveKey>();
			line = reader.readLine();
			while (line != null) {
				tokens = line.split("\\s");
				
				v.add(new CurveKey(
						Float.parseFloat(tokens[0]),
						Float.parseFloat(tokens[1])));
				
				line = reader.readLine();
			}
			
			keys = (CurveKey[]) v.toArray(new CurveKey[v.size()]);
			
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			keys = new CurveKey[0];
		}
		
	}
	
	public float evaluate(float time) {
		float t=time;
		
		float first=keys[0].t;
		float last=keys[keys.length-1].t;
		
		if (loopType != LoopType.None && (time < first || time > last)) {
			float width = last - first;
			if (loopType == LoopType.PingPong)
				width += width;
			
			t = (time - first) % width;
			if (t < 0.0f)
				t += width;
			t += first;
			if (t > last)
				t = last + (t-last)*(-1.0f);
		}
		
		if (t <= first)
			return keys[0].v;
		if (t >= last)
			return keys[keys.length-1].v;
		
		for(int i=0; i<keys.length-1; ++i) {
			if (keys[i+1].t >= t)
				return keys[i].v + (keys[i+1].v - keys[i].v) *
						( (t - keys[i].t) / (keys[i+1].t - keys[i].t) );
		}
		
		return Float.NaN;
	}
}
