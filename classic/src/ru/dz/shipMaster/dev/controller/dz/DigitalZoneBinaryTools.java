package ru.dz.shipMaster.dev.controller.dz;

public class DigitalZoneBinaryTools {

    private static final int CRC_MAGIC_1 = 0x8000;
    private static final int CRC_MAGIC_2 = 0x1021;

	public static int crc16_calc( byte[] packet, int size )
	{
		/* Calculate CRC-16 value; uses The CCITT-16 Polynomial,
	     expressed as X^16 + X^12 + X^5 + 1 */

		int crc = 0xffff;
		int index;
		char b;

		for( index=0; index<size; ++index )
		{
			int theByte = ((int)packet[index]) & 0xFF; 
			crc ^= (theByte << 8);
			for( b=0; b<8; ++b )
			{
				if( (crc & CRC_MAGIC_1) != 0 )
					crc = (crc << 1) ^ CRC_MAGIC_2;
				else
					crc = (crc << 1);
			}
		}
		return crc;
	}

}
