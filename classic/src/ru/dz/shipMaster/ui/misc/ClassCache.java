package ru.dz.shipMaster.ui.misc;

import java.util.HashMap;
import java.util.Map;

import ru.dz.shipMaster.ui.DashComponent;

@SuppressWarnings("rawtypes")
public class ClassCache 
{

	private static Map <String,Class> c = new HashMap<String, Class>();

	public static Class find(String shortName) {
		return c.get(shortName);
	}

	public static  void put(String shortName, Class cl) {
		//System.out.println("ClassCache put to cache "+shortName);
		c.put(shortName, cl);		
	}

	@SuppressWarnings("unchecked")
	public static  Class<? extends DashComponent> get(String iClassName) throws ClassNotFoundException
	{
		Class<? extends DashComponent> iClass = (Class<? extends DashComponent>) find(iClassName);
		
		if(iClass == null)
		{
			iClass = (Class<? extends DashComponent>) Class.forName(iClassName);
			put(iClassName,iClass);
		}
		//else System.out.println("ClassCache.get from cache "+iClassName);
		
		return iClass;
	}
	
	
}
