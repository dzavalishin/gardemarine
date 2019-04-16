package ru.dz.shipMaster.dev.controller;

import ru.dz.shipMaster.data.misc.CommunicationsException;

public class OwenProtocolFrame {

	private int address = 0;
	private int addrLen = 8;
	private boolean request = false;
	private int hash = 0;
	//private int dataSize;
	private byte[] data = null; // = new byte[15];
	private short crc;
	private boolean crcOk = false;




	static byte [] packFrameToAscii(byte [] frame)
	{
		int i, j;

		int frameSize = frame.length;
		
		int frameAsciiSize = frameSize*2+2+1;
		byte [] frameAscii = new byte[frameAsciiSize];

		frameAscii[0] = '#';
		for (i = 0, j = 1; i < frameSize; ++i, j += 2)
		{
			frameAscii[j] = (byte)('G' + ((frame[i] >> 4) & 0x0f));
			frameAscii[j + 1] = (byte)('G' + (frame[i] & 0x0f));
		}
		frameAscii[frameSize*2+1] = 0xD;
		frameAscii[frameSize*2+2] = 0;

		return frameAscii;
	}

	static byte[] unpackAsciiFrame(byte[] frameAscii )
	{
		int i, j;

		int frameAsciiSize = frameAscii.length;
		
		assert(frameAscii[0] == '#' && frameAscii[frameAsciiSize - 1] == 0xD );
		assert((frameAsciiSize & 1) == 0);

		int frameSize = (frameAsciiSize - 2)/2;
		byte [] frame = new byte[frameSize];

		for (i = 1, j = 0; i < frameAsciiSize-2;  i += 2, ++j)
		{
			assert('G' <= frameAscii[i]&&frameAscii[i] <= 'V');
			assert('G' <= frameAscii[i+1]&&frameAscii[i+1] <= 'V');

			frame[j] = (byte)((frameAscii[i] - 'G') << 4 | (frameAscii[i+1] - 'G'));
		}

		return frame;
	}



	byte [] packFrame()
	{	
		int dataSize = data == null ? 0 : data.length;

		int frameSize = 6 + dataSize;
		byte [] frame = new byte[frameSize];

		if (addrLen == 8)
		{
			frame[0] = (byte)(address & 0xff);
			frame[1] = 0;
		}
		else
		{
			frame[0] = (byte)((address >> 3)  & 0xff);
			frame[1] = (byte)((address & 0x07) << 5);
		}

		if(request)	frame[1] |= 0x10;

		assert(dataSize <= 15);
		frame[1] |= dataSize;

		frame[2] = (byte)((hash >> 8) & 0xff);
		frame[3] = (byte)(hash & 0xff);

		if(dataSize > 0)
			System.arraycopy(data, 0, frame, 4, dataSize);

		crc = owenCRC16(frame, 4 + dataSize);
		crcOk = true;

		frame[4 + dataSize] = (byte)((crc >> 8) & 0xff);
		frame[5 + dataSize] = (byte)(crc & 0xff);

		return frame;
	}


	void unpackFrame(byte [] frame)
	{
		/* ВНИМАНИЕ: невозможно отличить 11-битые адреса кратные 8 от 8-битных */
		if( (frame[1] & 0xe0) != 0)
		{
			address = (short)((frame[0] << 3) | (frame[1] >> 5));
			addrLen = 11;
		}
		else
		{
			address = frame[0];
			addrLen = 8;
		}

		request = (frame[1] & 0x10) != 0;
		hash = (frame[2] << 8) | frame[3];
		int dataSize = frame[1] & 0x0F;

		if(dataSize > 0)
		{
			assert(dataSize == frame.length - 6);

			data = new byte[dataSize];
			System.arraycopy(frame, 4, data, 0, dataSize);
		}
		else
		{
			data = null;
		}
		crc = (short)((frame[frame.length-2] << 8) | frame[frame.length-1]);
		crcOk = crc == owenCRC16(frame, frame.length-2);
	}




	/** расчет контрольной суммы */
	static public short owenCRC16(byte [] packet, int length)
	{
		int i, j;
		short crc;

		crc = 0;
		for (i = 0; i < length; ++i)
		{
			byte b = packet[i];
			for (j = 0; j < 8; ++j, b <<= 1)
			{
				if ( ((b ^ (crc >> 8)) & 0x80) != 0 )
				{
					crc <<= 1;
					crc ^= 0x8F57;
				}
				else
					crc <<= 1;
			}
		}
		return crc;
	}


	/** свертка локального идентификатора */
	static short id2hash(byte [] id)
	{
		int i, j;
		short hash;

		hash = 0;
		for (i = 0; i < 4; ++i)
		{
			byte b = id[i];
			b <<= 1; /* используются только младшие 7 бит */
			for (j = 0; j < 7; ++j, b <<= 1)
			{
				if ( ((b ^ (hash >> 8)) & 0x80) != 0 )
				{
					hash <<= 1;
					hash ^= 0x8F57;
				}
				else
					hash <<= 1;
			}
		}
		return hash;
	}



	/** 
	 * преобразование локального идентификатора в двоичный вид
	 * 
	 *  @param name - локальный идентификатор
	 *  @param id - идентификатор в двоичном виде (выход), 4 байта
	 */
	static void name2id(String name, byte [] id)
	{
		int i, j;

		for (i = 0, j = 0; i < name.length() && j <= 4; ++i)
		{
			byte b = 0;
			char c = name.charAt(i);

			if ('0' <= c && c <= '9')
			{
				b = (byte)(c - '0');
			}
			else if ('a' <= c && c <= 'z')
			{
				b = (byte)(10 + c - 'a');
			}
			else if ('A' <= c && c <= 'Z')
			{
				b = (byte)(10 + c - 'A');
			}
			else if ('-' == c)
			{
				b = 10 + 26 + 0;
			}
			else if ('_' == c)
			{
				b = 10 + 26 + 1;
			}
			else if ('/' == c)
			{
				b = 10 + 26 + 2;
			}
			else if ('.' == c)
			{
				assert(i > 0); /* модификатор не может быть первым символом */
				assert(name.charAt(i-1) != '.'); /* не может быть двух модификаторов подряд */

				++id[j - 1];

				continue;
			}
			else if (' ' == c)
			{
				break; /* пробел может находиться только в конце имени */
			}
			else
				assert(false); /* недопустимый символ */

			id[j++] = (byte)(b*2);
		}

		if (j == 4)
		{
			/* заполнены все байты идентификатора */
			assert(i == name.length()); /* обработаны все символы имени */
		}
		else
		{
			/* встречен первый пробел или обработаны все символы имени */
			for (; i < name.length(); ++i)
			{
				assert(name.charAt(i) == ' '); /* после пробела могут находиться только пробелы */
				assert(j < 4);

				id[j++] = 78;
			}

			/* дополняем пробелами до четырех символов */
			for (; j < 4; ++j)
				id[j] = 78;
		}
	}






	byte[] htonl(int x)
	{
		byte[] res = new byte[4];
		for (int i = 0; i < 4; i++)
		{
			res[i] = (new Integer(x >>> 24)).byteValue();
			x <<= 8;
		}
		return res;
	}

	//You can only ask for a byte array 4 bytes long to be converted. Rest everything will be ignored.
	int ntohl(byte[] x)
	{
		int res = 0;
		for (int i = 0; i < 4; i++)
		{
			res <<= 8;
			res |= (int) (x[i] & 0xFF);
		}
		return res;
	} 

	float unpackIEEE32Float()
	{
		assert(data.length == 4 || data.length == 6);
		return Float.intBitsToFloat( ntohl(data) );
	}

	int unpackIEEE32Time()
	{
		/* если есть время измерения */
		if (data.length == 6)
			return (((data[4] & 0xff)<< 8) | (data[5] & 0xff)) & 0xffff;
		else
			return -1;
	}


	float unpackFloat24()
	{
		assert(data.length == 3);

		byte [] buf = new byte[4];
		System.arraycopy(data, 0, buf, 0, 3);
		buf[3] = 0;
		
		return Float.intBitsToFloat( ntohl(buf) );
	}



	/**
	 * 
	 * Разбор поля данных для параметров DCNT и DSPD прибора СИ8.
	 * 
	 */

	public OwenBCD unpackBCD()
	{
		int i = 0;
		int val, sign;

		assert(data.length == 4);

		sign = (data[0] & 0x80) != 0 ? -1 : 1;

		OwenBCD bcd = new OwenBCD();

		bcd.point = (data[0] & 0x70) >> 4;

			val = data[0] & 0x0F;
			for (i = 1; i < data.length ; ++i)
			{
				val *= 10;
				val += ((data[i] & 0xF0) >> 4);
				val *= 10;
				val += data[i] & 0x0F;
			}
			bcd.value = sign*val;

			return bcd;
	}




	
	void appendIndexAndTime(short index, short time)
	{
		assert(
				(index != -1 && time != -1 && data.length <= 11) 
				||
				((index != -1 || time != -1) && data.length <= 13));

		int nbytes = 0;
		byte[] append = new byte[4];
		
		if(time != -1)
		{
			append[nbytes++] = (byte) ((time >> 8) & 0x0f);
			append[nbytes++] = (byte) (time & 0x0f);
		}
		if (index != -1)
		{
			append[nbytes++] = (byte) ((index >> 8) & 0x0f);
			append[nbytes++] = (byte) (index & 0x0f);
		}
		
		int nlen = nbytes + data.length;
		
		byte [] ndata = new byte[nlen];
		System.arraycopy(data, 0, ndata, 0, data.length);
		System.arraycopy(append, 0, ndata, data.length, nbytes );
		
		data = ndata;
	}








	public int getAddress() {		return address;	}
	public void setAddress(int address) {		this.address = address;	}

	public int getAddrLen() {		return addrLen;	}
	public void setAddrLen(int addrLen) throws CommunicationsException 
	{
		if( addrLen != 8 && addrLen != 11 )
			throw new CommunicationsException("Invalid Owen address length");
		this.addrLen = addrLen;	
	}

	public boolean isRequest() {		return request;	}
	public void setRequest(boolean request) {		this.request = request;	}

	public int getHash() {		return hash;	}
	public void setHash(int hash) {		this.hash = hash;	}

	public byte[] getData() {		return data;	}
	public void setData(byte[] data) {		this.data = data;	}

	public boolean isCrcOk() {		return crcOk;	}
	//public void setCrcOk(boolean crc_ok) {		this.crcOk = crc_ok;	}


}





/* TODO fast CRC

// Быстрый алгоритм расчета контрольной суммы
// с использованием таблицы.

// таблица 
short fastCRC16Table[256];

// инициализация таблицы
static void initFastCRC16Table()
{
	unsigned short i, j;
	unsigned short crc;

	for (i = 0; i < 256; ++i)
	{
		unsigned short b = i;
		crc = 0;
		for (j = 0; j < 8; ++j, b <<= 1)
		{
			if ((b ^ (crc >> 8)) & 0x80)
			{
				crc <<= 1;
				crc ^= 0x8F57;
			}
			else
				crc <<= 1;
		}
		fastCRC16Table[i] = crc;
	}
}


// быстрый расчет контрольной суммы 
unsigned short owenFastCRC16(unsigned char* packet, size_t length)
{
	size_t i;
	unsigned short crc;

	assert(packet);

	crc = 0;
    for (i = 0; i < length; ++i)
    {
        crc = (crc << 8) ^ fastCRC16Table[(crc >> 8) ^ packet[i]];
    }
   
	return crc;
}



*/