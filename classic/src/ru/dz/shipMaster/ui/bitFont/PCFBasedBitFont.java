package ru.dz.shipMaster.ui.bitFont;

import java.util.logging.Logger;

import ru.dz.shipMaster.ui.pcfFont.PCFFont;
import ru.dz.shipMaster.ui.pcfFont.PCFMetrics;

public class PCFBasedBitFont extends GeneralBitFont {
    @SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(PCFBasedBitFont.class.getName()); 


    public int getSizeX() { return 6; }
	public int getSizeY() { return 8; }

	// MSB is low end of picture
	// first byte is left side
	
	public PCFBasedBitFont(PCFFont source)
	{
		int bytesPerChar = 6;
		
		for(char c = ' '; c <= '9'; c++ )			loadGlyph(source,c,bytesPerChar);
		for(char c = '0'; c <= '9'; c++ )			loadGlyph(source,c,bytesPerChar);
		
		for(char c = 'a'; c <= 'z'; c++ )			loadGlyph(source,c,bytesPerChar);
		for(char c = 'A'; c <= 'Z'; c++ )			loadGlyph(source,c,bytesPerChar);
		for(char c = 'а'; c <= 'я'; c++ )			loadGlyph(source,c,bytesPerChar);
		for(char c = 'А'; c <= 'Я'; c++ )			loadGlyph(source,c,bytesPerChar);
	}

	private char utfTo1251(char c)
	{
		int ci = c;
		if( ci >= 1040) ci = (char)(ci - 1040 + 192);
		return (char)ci;
	}
	
	private void loadGlyph(PCFFont source, char inC, int bytesPerChar) {
		char c = utfTo1251(inC);
		
		PCFMetrics bbox = source.charMetrics( c );

		// Create the image buffer

		int xpos = (bbox.leftSideBearing<0 ? -bbox.leftSideBearing : 0);
		int w = xpos + bbox.rightSideBearing;
		int h = bbox.ascent + bbox.descent;

		if (w == 0) w = 1;	
		if (h == 0) h =1;

		int ypos = bbox.ascent;

		// go
				
		byte[] bits = new byte[bytesPerChar];

		int[] ints = new int[w*h];//[bytesPerChar*8*100];

		for( int i = 0; i < ints.length; i++ )
			ints[i] = 0;
		
		source.drawCharBuffer(c, ints, xpos, ypos, w);

		/*
		if(false){					
			try {
				FileOutputStream fos = new FileOutputStream("c:/tmp/char-"+Integer.toString(c));
				//FileOutputStream fos = new FileOutputStream("c:/tmp/char-"+c);

                for (int anInt : ints) fos.write((anInt != 0) ? '*' : ' ');

                fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		*/
		//int seq = 0;
		for( int x = 0; x < bbox.characterWidth; x++ )
		{
			for( int y = 0; y < w; y++ )
			{
				int bitVal = ints[y*w+x];
				//int bitVal = ints[x*8+y];
				//int bitVal = ints[seq++];
				if(bitVal != 0)
					bits[x] |= (0x1 << y);
			}
			//seq+=1;
		}
		font.put(inC,bits);		
	}
	
}
