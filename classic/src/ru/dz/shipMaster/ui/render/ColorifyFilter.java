package ru.dz.shipMaster.ui.render;

import java.awt.Color;
import java.awt.image.RGBImageFilter;


public class ColorifyFilter extends RGBImageFilter {
	//byte r; byte g; byte b;
	int r, g, b;
    public ColorifyFilter(Color color) {
        canFilterIndexColorModel = true;
        
        //r = (byte)color.getRed();
        //g = (byte)color.getGreen();
        //b = (byte)color.getBlue();

        r = color.getRed();
        g = color.getGreen();
        b = color.getBlue();
    }

    public int filterRGB(int x, int y, int rgb) {
        //if (x == -1) {
            // indexed color
        //}
    	
    	int inR = (rgb >> 16) & 0xFF;
    	int inG = (rgb >> 8) & 0xFF;
    	int inB = rgb & 0xFF;
    	
    	int gray = (inR+inG+inB)/3; 

        int ret = 
        	(rgb&0xff000000) |
        		((0xFF& ((r * gray) / 255) ) << 16) |
       			((0xFF& ((g * gray) / 255) ) << 8) |
       			((0xFF& ((b * gray) / 255) ) ) 
        ;
        
        return ret;
    }
}	
