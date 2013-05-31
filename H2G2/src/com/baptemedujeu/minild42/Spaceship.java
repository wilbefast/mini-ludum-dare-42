package com.baptemedujeu.minild42;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader;
import com.jackamikaz.gameengine.DisplayedEntity;
import com.jackamikaz.gameengine.Engine;
import com.jackamikaz.gameengine.UpdatedEntity;
import com.jackamikaz.gameengine.utils.DisplayOrder;

public class Spaceship implements DisplayedEntity, UpdatedEntity, SpatialEntity
{
	private static final float SPEED = 2.0f;
	private static final int ELLIPSE_BASE_SAMPLES = 3;
	
	private Sprite sprite;
	private TextureRegion occupied, unoccupied;
	
	private Vector2 pos;
	private Vector2 dir = new Vector2(1, 0);
	
	private float size;
	
	public static Spaceship parse(XmlReader.Element o, int colour, float level_width, float level_height)
	{
		// Create Spaceship
		float x = o.getFloatAttribute("x")/level_width, 
					y = o.getFloatAttribute("y")/level_height;
		Spaceship newbie = new Spaceship(x, y, colour);
		
		// Push poly-line itinerary
		XmlReader.Element line = o.getChildByName("polyline");
		if (line != null)
		{
			String str_itinerary = line.getAttribute("points");
			String[] str_coordinate_pairs = str_itinerary.split(" ");
			for(String s : str_coordinate_pairs)
			{
				int comma = s.indexOf(",");
				float dest_x = x + Float.parseFloat(s.substring(0, comma))/level_width,
							dest_y = y + Float.parseFloat(s.substring(comma + 1))/level_height;
				newbie.pushDestination(new Vector2(dest_x, dest_y));
			}
		}
		
		// Push elliptical itinerary
		else if(o.getChildByName("ellipse") != null)
		{
			float rx = o.getFloatAttribute("width")/2/level_width,
						ry = o.getFloatAttribute("height")/2/level_height;
			
			int n_spokes = (int)(Math.floor(Math.sqrt(rx*rx)*ELLIPSE_BASE_SAMPLES));
			double spoke = 2*Math.PI/n_spokes;
			for(int i = 0; i < n_spokes; i++)
			{
				float dest_x = x + rx + (float)Math.cos(i*spoke)*rx,
							dest_y = y + ry + (float)Math.sin(i*spoke)*ry;
				newbie.pushDestination(new Vector2(dest_x, dest_y));
			}
		}
		
		// Return the result
		return newbie;
	}

	public Spaceship(float x, float y, int colour)
	{
		// register
		Engine.DisplayMaster().Add(this);
		Engine.UpdateMaster().Add(this);
		
		pos = new Vector2(x, y);
		
		// sprite
		Texture t = Engine.ResourceManager().GetTexture("sprites");
		
		unoccupied = new TextureRegion(t, 0, 256 + 128*colour, 128, 128);
		occupied = new TextureRegion(t, 128, 256 + 128*colour, 128, 128);
		sprite = new Sprite(unoccupied);
		
		float ratio = (sprite.getHeight() / sprite.getWidth());
		size = sprite.getWidth() / 128.0f;
		sprite.setSize(size, size*ratio);
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		sprite.setPosition(pos.x, pos.y);
	}

	
	private ShapeRenderer sr = new ShapeRenderer(20);
	@Override
	public void Display(float lerp)
	{	
		sprite.setPosition(pos.x - sprite.getWidth()/2, pos.y - sprite.getHeight()/2);
		sprite.draw(Engine.Batch());

		/*Vector2 prev = null;
		sr.begin(ShapeRenderer.ShapeType.Line);
			sr.setColor(1, 1, 0, 1);
			for(Vector2 dest : itinerary)
			{
				if (prev != null)
					sr.line(prev.x, prev.y, dest.x, dest.y);
				prev = dest;
			}
		sr.end();*/
		
	}

	@Override
	public int GetDisplayRank()
	{
		return DisplayOrder.Render2D.ordinal();
	}

	@Override
	public void Update(float deltaT)
	{
		// roll the list
		if(pos.dst2(itinerary.peek()) < 10.0f)
			itinerary.addLast(itinerary.poll());
		
		dir.set(itinerary.peek()).sub(pos).nor().scl(SPEED * deltaT);
		pos.add(dir);
		
		//sprite.setRotation((float)(180*orbitAngle / Math.PI) + Math.signum(orbitSpeed)*90.0f-90);
	}
	
	
	public void setOccupied(boolean b) 
	{
		sprite.setRegion(b ? occupied : unoccupied);
	}
	
	//! ITINERARY
	
	private Deque<Vector2> itinerary = new LinkedList<Vector2>();
	
	public void pushDestination(Vector2 newDestination)
	{
		if(itinerary.isEmpty())
			;//pos.set(newDestination);
		itinerary.add(newDestination);
	}
	
	
	//! SPATIAL ENTITY
	
	@Override
	public Vector2 getPosition() { return pos; }

	@Override
	public float getRadius() { return size * 0.1f; }

	@Override
	public float getWidth() { return sprite.getWidth(); }

	@Override
	public float getHeight() { return sprite.getHeight(); }

	@Override
	public float getRotation() { return 0.0f; }
}
