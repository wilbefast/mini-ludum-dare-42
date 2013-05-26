package com.baptemedujeu.minild42;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;

public class Level
{
	private int level_width = 64, level_height = 64;
	
	
	private void parseShips(XmlReader.Element og)
	{
		for(XmlReader.Element o : og.getChildrenByName("object"))
		{
			float x = o.getFloatAttribute("x")/level_width, 
						y = o.getFloatAttribute("y")/level_height, 
						r = o.getFloatAttribute("width")/2/level_width;
			new Spaceship(x + r, y + r, r);
		}
	}
	
	private void parsePlanets(XmlReader.Element og)
	{
		for(XmlReader.Element o : og.getChildrenByName("object"))
		{
			float x = o.getFloatAttribute("x")/level_width, 
						y = o.getFloatAttribute("y")/level_height;
			new Planet(x, y);
		}
	}
	
	private void parseHitchhikers(XmlReader.Element og)
	{
		for(XmlReader.Element o : og.getChildrenByName("object"))
		{
			float x = o.getFloatAttribute("x")/level_width, 
						y = o.getFloatAttribute("y")/level_height, 
						r = o.getFloatAttribute("width")/2/level_width;
			new Hitchhiker(x + r, y + r);
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
				if("ships".equals(groupename))
					parseShips(og);
				else if("planets".equals(groupename))
					parsePlanets(og);
				else if("hitchhikers".equals(groupename))
					parseHitchhikers(og);
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
