package ru.dz.shipMaster.ui.bitFont;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import ru.dz.shipMaster.ui.misc.VisualHelpers;

public abstract class GeneralBitFont implements BitFont {
    private static final Logger log = Logger.getLogger(GeneralBitFont.class.getName()); 

	protected Map<Character, byte[]> font = new HashMap<Character,byte[]>();

	public byte[] getCharacter(char c) {
	byte[] out = font.get(c);
	if( out == null ) { out = font.get(' '); }
	return out;
	}

	
	public void saveAsAsciiPicture(String fileName) throws IOException
	{
		FileWriter fw = null;;
		
		try {
			fw = new FileWriter(fileName);
			for(Character c : font.keySet())
			{
				byte[] bits = getCharacter(c);

				fw.append("--");
				fw.append(c);
				fw.append("-- 0x");
				fw.append(Integer.toString((int)c,16));
				//fw.append("\n");
				fw.append(System.getProperty("line.separator"));
				saveAsAsciiPicture(bits, fw);
			}
		}
		finally {
			fw.close();
		}
	}


	void saveAsAsciiPicture(byte[] bits, FileWriter fw) throws IOException {
		for( int y = 0; y < getSizeY(); y++ )
		{
			for( int x = 0; x < getSizeX(); x++ )
			{
				int bit = bits[x] & (0x1 << y);
				fw.append(bit != 0 ? '*' : '.');
			}
			fw.append('\n');
		}
		//fw.append("----\n\n");
		fw.append("----");
		fw.append(System.getProperty("line.separator"));
		fw.append(System.getProperty("line.separator"));
	}

	
	public void loadFromAsciiPicture(String fileName) throws IOException
	{
		font.clear();
		BufferedReader fr = null;;
		
		try {
			InputStream in = VisualHelpers.class.getClassLoader().getResourceAsStream(fileName);

			fr = new BufferedReader(new InputStreamReader(in));
			//fr = new BufferedReader(new FileReader(fileName));

			while(fr.ready())
			{
				String line = fr.readLine();
				
				if( line == null || line.length() < 1)
				{
					continue;
				}
				
				if(line.charAt(0) != '-')
				{
					log.severe("Unparsable line: "+line);
					//continue;
					throw new IOException("Unparsable line: "+line);
				}
				
				Character c = line.charAt(2);
				//log.severe("Character: "+c);
				
				loadFromAsciiPicture(c, fr);
				
				line = fr.readLine();
				if(line.charAt(0) != '-')
				{
					log.severe("line of '-' supposed to be here: "+line);
					//continue;
					throw new IOException("Unparsable line: "+line);
				}
				
			}
			
		}
		finally {
			fr.close();
		}
	}


	void loadFromAsciiPicture(Character c, BufferedReader fr) throws IOException {

		byte[] bits = new byte[getSizeX()];
		
		for( int y = 0; y < getSizeY(); y++ )
		{
			String line = fr.readLine();
			for( int x = 0; x < getSizeX(); x++ )
			{
				boolean isPixel = line.charAt(x) == '*';
				
				if(isPixel)
				{
					bits[x] |= (0x1 << y);
				}
			}
		}
		
		font.put(c, bits);		
	}
	
}
