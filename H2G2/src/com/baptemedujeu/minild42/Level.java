package com.baptemedujeu.minild42;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader;

public class Level
{
	public Hitchhiker hiker;
	public Mothership mother;
	
	private void parseMotherships(XmlReader.Element og)
	{
		for(XmlReader.Element o : og.getChildrenByName("object"))
		{
			float x = o.getFloatAttribute("x"), 
						y = o.getFloatAttribute("y");
			mother = new Mothership(x, y);
		}
	}
	
	private void parseSpaceships(XmlReader.Element og, int colour)
	{
		for(XmlReader.Element o : og.getChildrenByName("object"))
			Spaceship.parse(o, colour);
	}
	
	private void parsePlanets(XmlReader.Element og, int colour)
	{
		for(XmlReader.Element o : og.getChildrenByName("object"))
		{
			float x = o.getFloatAttribute("x"), 
						y = o.getFloatAttribute("y"),
						r = o.getFloatAttribute("width")/2;
			new Planet(x + r, y + r, r, colour);
		}
	}
	
	private void parseHitchhikers(XmlReader.Element og)
	{
		for(XmlReader.Element o : og.getChildrenByName("object"))
		{
			float x = o.getFloatAttribute("x"), 
						y = o.getFloatAttribute("y"), 
						r = o.getFloatAttribute("width")/2;
			hiker = new Hitchhiker(x + r, y + r);
		}
	}
	
	
	public Level(String xml_file)
	{
		try
		{
			XmlReader xml = new XmlReader();
			FileHandle handle = Gdx.files.internal("data/" + xml_file);
			XmlReader.Element m = xml.parse(handle);
			
			for(XmlReader.Element og : m.getChildrenByName("objectgroup"))
			{
				String groupename = og.getAttribute("name");
				
				if("ships_green".equals(groupename))
					parseSpaceships(og, 0);
				else if("ships_orange".equals(groupename))
					parseSpaceships(og, 1);
				else if("ships_pink".equals(groupename))
					parseSpaceships(og, 2);

				else if("planets_green".equals(groupename))
					parsePlanets(og, 0);
				else if("planets_orange".equals(groupename))
					parsePlanets(og, 1);
				else if("planets_pink".equals(groupename))
					parsePlanets(og, 2);

				
				else if("hitchhikers".equals(groupename))
					parseHitchhikers(og);
				
				else if("motherships".equals(groupename))
					parseMotherships(og);
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
