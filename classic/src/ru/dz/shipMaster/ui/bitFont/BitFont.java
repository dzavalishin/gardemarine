package ru.dz.shipMaster.ui.bitFont;

import java.io.IOException;


public interface BitFont {

	byte[] getCharacter( char c );
	
	int getSizeX();
	int getSizeY();

	public void saveAsAsciiPicture(String fileName) throws IOException;
	public void loadFromAsciiPicture(String fileName) throws IOException;
		
}
