package ru.dz.shipMaster.dev.nmeaSentence;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.dz.shipMaster.dev.DriverPort;


public class NMEA0183Sentence {
	private static Logger log = Logger.getLogger(NMEA0183Sentence.class.getName());

	protected String rawData;
	protected String talkerId = null;
	protected String sentenceId = null;
	protected byte checksum = -1;
	protected byte calculatedChecksum = -1;
	protected List<String> dataFields = null;

	private boolean calcChecksum = true;

	/**
	 * Initialize a new NMEA 0183 sentence with the given raw data.
	 * The raw data looks like "$HCHDG,219.5,,,2.5,E*21".
	 * @param raw_data the raw data to be parsed.
	 */

	public NMEA0183Sentence(String raw_data)
	{
		log.log(Level.FINEST,"raw data='"+raw_data+"'");
		int star_pos = raw_data.indexOf('*');
		// tread $PSRFTXT sentences in a special way (no checksum!)
		if(raw_data.startsWith("$PSRFTXT"))
		{
			rawData = raw_data;
		}
		else if(star_pos <= 6)
		{
			throw (new IllegalArgumentException("Invalid NMEA Sentence (no '*'): "
					+new String(raw_data)));
		}
		else
		{
			int len = raw_data.length();
			if( len >= star_pos+3 )
				rawData = raw_data.substring(0,star_pos+3);
			else
			{
				rawData = raw_data.substring(0,star_pos+1);
				calcChecksum  = false;
			}
		}
	}

	//----------------------------------------------------------------------
	/**
	 * Initialize a new NMEA 0183 sentence with the given raw data.
	 * @param raw_data the raw data to be parsed.
	 * @param offset the offset in the buffer
	 * @param length the length
	 */

	public NMEA0183Sentence(byte[] raw_data, int offset, int length)
	{
		this(new String(raw_data, offset, length));
	}


	//----------------------------------------------------------------------
	/**
	 * Initialize a new NMEA 0183 sentence with the given raw data.
	 * @param raw_data the raw data to be parsed.
	 * @param offset the offset in the buffer
	 * @param length the length
	 */

	public NMEA0183Sentence(char[] raw_data, int offset, int length)
	{
		this(new String(raw_data, offset, length));
	}


	//----------------------------------------------------------------------
	/**
	 * Initialize a new NMEA 0183 sentence with the given raw data.
	 * @param raw_data the raw data to be parsed.
	 */

	public NMEA0183Sentence(char[] raw_data)
	{
		this(new String(raw_data));
	}


	//----------------------------------------------------------------------
	/**
	 * Returns the talker id of this NMEA sentence.
	 * @return the talker id of this NMEA sentence.
	 */

	public String getTalkerId()
	{
		if (talkerId == null)
		{
			if(rawData.charAt(1) == 'P')
			{
				// Proprietary
				talkerId = rawData.substring(2,5);
			}
			else
				talkerId = rawData.substring(1,3);
		}
		return talkerId;
	}


	//----------------------------------------------------------------------
	/**
	 * Returns the sentence id of this NMEA sentence.
	 * @return the sentence id of this NMEA sentence.
	 */

	public String getSentenceId()
	{
		if (sentenceId == null)
		{
			int comma_pos = rawData.indexOf(',');
			if(comma_pos < 0)
			{
				comma_pos = 6;
			}
			if(rawData.charAt(1) == 'P')
			{
				// Proprietary
				sentenceId = rawData.substring(1,comma_pos);
			}
			else
				sentenceId = rawData.substring(3,comma_pos);
		}
		return(sentenceId);
	}

	//----------------------------------------------------------------------
	/**
	 * Returns the data fields of this NMEA sentence.
	 * @return the data fields of this NMEA sentence.
	 */

	public List<String> getDataFields()
	{
		if (dataFields == null) retrieveDataFieldsAndChecksum();
		return dataFields;
	}

	//----------------------------------------------------------------------
	/**
	 * Returns the checksum of this NMEA sentence.
	 * @return the checksum of this NMEA sentence.
	 */

	public byte getChecksum()
	{
		if(checksum < 0) retrieveDataFieldsAndChecksum();
		return checksum;
	}


	//----------------------------------------------------------------------
	/**
	 * Returns the calculated checksum of this NMEA sentence.
	 * @return the calculated checksum of this NMEA sentence.
	 */

	public byte getCalculatedChecksum()
	{
		if (calculatedChecksum < 0)
		{
			if(checksum < 0)
				retrieveDataFieldsAndChecksum();
			calculatedChecksum = calcChecksum();
		}
		return calculatedChecksum;
	}


	//----------------------------------------------------------------------
	/**
	 * Returns true if the sentence is valid (by using the checksum).
	 *
	 * @return true if the sentence is valid (by using the checksum).
	 */
	public boolean isValid()
	{
		if(!calcChecksum) return true;
		return getChecksum() == getCalculatedChecksum();
	}

	//----------------------------------------------------------------------
	/**
	 * Parses the raw data and extracts the data fields and the checksum.
	 */

	protected void retrieveDataFieldsAndChecksum()
	{
		if(dataFields == null)
			dataFields = new ArrayList<String>();

		StringTokenizer tokenizer = new StringTokenizer(rawData,",*",true);
		tokenizer.nextElement();  // skip first element (NMEA messageid)
		if (tokenizer.hasMoreElements())
			tokenizer.nextElement();  // skip first delimiter
		String token;
		String element = "";

		while(tokenizer.hasMoreElements())
		{
			token = tokenizer.nextToken();
			if(token.equals(","))
			{
				dataFields.add(element.trim());
				element = "";
			}
			else if(token.equals("*"))
			{
				dataFields.add(element.trim());
				element = "";  // mostly useless, as checksum is the last
				if(calcChecksum)
					checksum = decodeChecksum(tokenizer.nextToken());
				if(tokenizer.hasMoreElements())
					System.err.println("WARNING: too long NMEA sentence, elements after checksum found: "
							+rawData);
			}
			else
			{
				element = token;  // normal token
			}
		}
		// add last element. This is only relevant if there is no checksum
		// (for RFTXT sentences or for devices that do not send checksums)
		// for sentences like (no checksum sent)
		// xxx,a,b,c,
		// an empty string for the fourth element is appended
		if(element != null && element.length() > 0)
			dataFields.add(element);
	}

	public boolean equals(NMEA0183Sentence object)
	{
		return(object.rawData.equals(rawData));
	}

	public boolean equals(Object object)
	{
		if ((object instanceof NMEA0183Sentence))
			return(equals((NMEA0183Sentence)object));
		
		return(false);
	}

	@Override
	public int hashCode() {
		return rawData.hashCode();
	}
	
	//----------------------------------------------------------------------
	/**
	 * Decodes the checksum string of a nmea sentence.
	 *
	 * @param	checksum_string the string representing the checksum (two characters long)
	 * @return the checksum as a byte value
	 */

	protected static byte decodeChecksum(String checksum_string)
	{
		//		byte checksum;
		//
		//		checksum = (byte)((hexCharToByte(checksum_string.charAt(0)) & 0xF ) << 4 );
		//		checksum = (byte)(checksum | hexCharToByte(checksum_string.charAt(1)) & 0xF );
		//		return(checksum);
		// changes added by cedricseah@users.sourceforge.net
		if (checksum_string == null || checksum_string.equals("")) {
			throw new IllegalArgumentException("checksum must not be null or empty!");
		}
		return Byte.parseByte(checksum_string, 16);
	}


	//----------------------------------------------------------------------
	/**
	 * Calculate the checksum of this NMEA sentence
	 *
	 * @return the calculated checksum
	 */

	protected byte calcChecksum()
	{
		//		int		calc = 0;
		//		int		count;
		//		int		len;
		//		char	chr;
		//
		//		len = raw_data_.length();
		//
		//		for(count = 1; count < len - 2; count++)  // ignore '$' at beginning and checksum at the end
		//		{
		//		chr = raw_data_.charAt(count);
		//
		//		if(chr == '*') // just to be sure
		//		break;
		//
		//		if(count == 1)
		//		calc = (chr + 256) & 0xFF;
		//		else
		//		calc ^= (chr + 256) & 0xFF;
		//		}
		//		return((byte)calc);

		// changes proposed by cedric
		int start = rawData.indexOf('$');
		int end = rawData.indexOf('*');
		if(end < 0) {
			end = rawData.length();
		}
		byte csum = (byte) rawData.charAt(start + 1);
		for (int index = start + 2; index < end; ++index) {
			csum ^= (byte) rawData.charAt(index);
		}
		return csum;    
	}

	//----------------------------------------------------------------------
	/**
	 * Get the byte value for a hex character
	 *
	 * @param	hex_char hex character
	 * @return byte value
	 */

	protected static byte hexCharToByte(char hex_char)
	{
		if( hex_char > 57 )
			return((byte)(hex_char - 55));
		else
			return((byte)(hex_char - 48));
	}

	//----------------------------------------------------------------------
	/**
	 * Returns the string representation of this NMEA sentence.
	 * @return the string representation of this NMEA sentence.
	 */

	public String toString()
	{
		//return(rawData);

		return String.format("%s/%s, %s %s", 
				getTalkerId(), getSentenceId(),
				isValid() ? "Ok" : "Err",
						getDataFields()
		);
	}


	public static void main(String[] args)
	{
		if(args.length > 0)
		{
			dump(args[0]);
		}
		else
		{
			//dump("$HCHDG,219.5,,,2.5,E*21");

			// $ERRPM,S,1,251.0,A*53
			// S - ? direction?
			// 1 - engine number
			// 251.0 - RPM value
			dump("$ERRPM,S,1,0.0,A*55");

			dump("$TSSCT,35.6,36.0,34.5,34.9,36.7,37.2,37.8,38.1,A*25");

			dump("$PBRO,CA,2,675.0,A*54");
		}
	}

	public static void dump(String in )
	{
		NMEA0183Sentence s;
		s = new NMEA0183Sentence(in);
		System.out.println("Sentence: "+s);
		System.out.println("talkId: "+s.getTalkerId());
		System.out.println("sentId: "+s.getSentenceId());
		System.out.println("datafields: "+s.getDataFields());
		System.out.println("checksum: "+s.getChecksum());
		System.out.println("calculated checksum: "+s.getCalculatedChecksum());
		System.out.println("valid: "+s.isValid());

		/*try {
			GeneralParser p = GeneralParser.getParser(s);
			System.out.println("Parsed: "+p);
		} catch (InvalidNMEASentenceException e) {
			e.printStackTrace();
		}*/
	}

	/** Return field number i */
	public String getField(int i) {
		return getDataFields().get(i);
	}

	/** 
	 * Send field number i to port port.
	 * Sends zero if field is empty.
	 */
	public void sendNumericField(int i, DriverPort port ) {
		String f = getDataFields().get(i);

		try {
			port.sendDoubleData(  f.length() > 0 ? Double.parseDouble(f) : 0.0 );
		}
		catch(NumberFormatException e) { 
			log.log(Level.SEVERE, "NAN in field "+i+" of "+this, e);
		}
	}

}
