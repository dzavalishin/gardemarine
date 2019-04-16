package ru.dz.shipMaster.ui.misc;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class ImageCache {
	Map <String,BufferedImage> c = new HashMap<String, BufferedImage>();

	public BufferedImage find(String shortName) {
		return c.get(shortName);
	}

	public void put(String shortName, BufferedImage img) {
		c.put(shortName, img);		
	}

}
