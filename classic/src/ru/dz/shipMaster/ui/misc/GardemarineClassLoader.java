package ru.dz.shipMaster.ui.misc;

public class GardemarineClassLoader extends ClassLoader {

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException 
	{
		
		@SuppressWarnings("rawtypes")
		Class c = ClassCache.find(name);
		
		if( c == null )
		{
			c = super.loadClass(name);
			ClassCache.put(name, c);
		}
		
		return c;
	}
	
}
