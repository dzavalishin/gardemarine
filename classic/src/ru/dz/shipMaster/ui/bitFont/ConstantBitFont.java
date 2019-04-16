package ru.dz.shipMaster.ui.bitFont;

//import java.util.logging.Logger;
// TO DO this one must die
public class ConstantBitFont extends GeneralBitFont {
    //private static final Logger log = Logger.getLogger(ConstantBitFont.class.getName()); 


    public byte[] getCharacter(char c) 
	{
		byte[] out = font.get(c);
		if( out == null ) out = font.get(' ');
		return out;
		}
	
	public int getSizeX() { return 6; }
	public int getSizeY() { return 8; }

	// MSB is low end of picture
	// first byte is left side
	
	public ConstantBitFont()
	{
		byte[] bits;
		{
			bits = new byte[] {
					(byte)0x00,
					(byte)0x00,
					(byte)0x00,
					(byte)0x00,
					(byte)0x00,

					(byte)0x00,
			};
			font.put(' ',bits);
		}

		{
			bits = new byte[] {
					(byte)0x7C,
					(byte)0x82,
					(byte)0x82,
					(byte)0x82,
					(byte)0x7C,

					(byte)0x00,
			};
			font.put('0',bits);
		}
		
		{
			bits = new byte[] {
					(byte)0x00,
					(byte)0x84, // *000 0*00
					(byte)0xFE, // **** ***0
					(byte)0x80, // *000 0000
					(byte)0x00,

					(byte)0x00,
			};
			font.put('1',bits);
		}
		
		{
			bits = new byte[] {
					(byte)0xC4,
					(byte)0xA2,
					(byte)0x92,
					(byte)0x8A,
					(byte)0xC4,

					(byte)0x00,
			};
			font.put('2',bits);
		}
		
		{
			bits = new byte[] {
					(byte)0x44, // 0*00 0*00
					(byte)0x82, // *000 00*0
					(byte)0x82, // *000 00*0
					(byte)0x92, // *00* 00*0
					(byte)0x6C, // 0**0 **00

					(byte)0x00,
			};
			font.put('3',bits);
		}
		
		{
			bits = new byte[] {
					(byte)0x1E, // 000* ***0
					(byte)0x10, // 000* 0000
					(byte)0x10, // 000* 0000
					(byte)0x10, // 000* 0000
					(byte)0xFE, // **** ***0

					(byte)0x00,
			};
			font.put('4',bits);
		}
		
		{
			bits = new byte[] {
					(byte)0x9E, // *00* ***0
					(byte)0x92, // *00* 00*0
					(byte)0x92, // *00* 00*0
					(byte)0x92, // *00* 00*0
					(byte)0x62, // 0**0 00*0

					(byte)0x00,
			};
			font.put('5',bits);
		}
		
		{
			bits = new byte[] {
					(byte)0x7C, // 0*** **00
					(byte)0x92, // *00* 00*0
					(byte)0x92, // *00* 00*0
					(byte)0x92, // *00* 00*0
					(byte)0x64, // 0**0 0*00

					(byte)0x00,
			};
			font.put('6',bits);
		}
		
		{
			bits = new byte[] {
					(byte)0x06, // 0000 0**0
					(byte)0x02, // 0000 00*0
					(byte)0x02, // 0000 00*0
					(byte)0xE2, // ***0 00*0
					(byte)0x1E, // 000* ***0

					(byte)0x00,
			};
			font.put('7',bits);
		}
		
		{
			bits = new byte[] {
					(byte)0x6C, // 0**0 **00
					(byte)0x92, // *00* 00*0
					(byte)0x92, // *00* 00*0
					(byte)0x92, // *00* 00*0
					(byte)0x6C, // 0**0 **00

					(byte)0x00,
			};
			font.put('8',bits);
		}
		
		{
			bits = new byte[] {
					(byte)0x0C, // 0000 **00
					(byte)0x92, // *00* 00*0
					(byte)0x92, // *00* 00*0
					(byte)0x92, // *00* 00*0
					(byte)0x7C, // 0*** **00

					(byte)0x00,
			};
			font.put('9',bits);
		}
		
		{
			bits = new byte[] {
					(byte)0x00, // 0000 0000
					(byte)0x00, // 0000 0000
					(byte)0x44, // 0*00 0*00
					(byte)0x00, // 0000 0000
					(byte)0x00, // 0000 0000

					(byte)0x00,
			};
			font.put(':',bits);
		}

		{
			bits = new byte[] {
					(byte)0x00, // 0000 0000
					(byte)0x10, // 000* 0000
					(byte)0x10, // 000* 0000
					(byte)0x10, // 000* 0000
					(byte)0x00, // 0000 0000

					(byte)0x00,
			};
			font.put('-',bits);
		}
		
		
	}
	
}
