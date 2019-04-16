package ru.dz.shipMaster.net.protocol;

import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

public class PacketItem {
    @SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(PacketItem.class.getName()); 

	private static final String ITEM_STRING_CHARSET = "UTF-8";
	
	private final String itemName;
	private String itemDataTypeName;
	private byte[] data = null;
	private String stringVal = "";
	//private double numericVal = 0;

	// TO DO move name/type parsers here?
	
	/**
	 * Construct item from binary data.
	 * @param itemTypeName Type of item.
	 * @param itemDataTypeName Data type name (NDBL, SUTF, what else? Define!).
	 * @param rawData Data to send.
	 * @param srcPos Start position in rawData.
	 * @param length Bytes to send.
	 * @throws UnsupportedEncodingException
	 */
	public PacketItem(String itemTypeName, String itemDataTypeName, byte[] rawData, int srcPos, int length) throws UnsupportedEncodingException {
		this.itemName = itemTypeName;
		this.itemDataTypeName = itemDataTypeName;
		data = new byte[length];
		System.arraycopy(rawData, srcPos, data, 0, length);
		stringVal = new String(data,ITEM_STRING_CHARSET);
	}

	public PacketItem(String itemName, String value) {
		this.itemName = itemName;
		setStringValue(value);
	}

	public PacketItem(String itemName, double value) {
		this.itemName = itemName;
		setNumericValue(value);
	}

	public String getStringValue() { return stringVal; }
	public double getNumericValue() { return Double.parseDouble(stringVal); }

	public void setStringValue(String val) { stringVal = val; itemDataTypeName = "NDBL"; }
	public void setNumericValue(double val) { stringVal = Double.toString(val); itemDataTypeName = "SUTF"; }
	
	public byte[] assemble() throws UnsupportedEncodingException
	{
		return stringVal.getBytes(ITEM_STRING_CHARSET);
	}

	public String getName() { return itemName; }

	public String getTypeName() { return itemDataTypeName; }
	
	@Override
	public String toString()
	{
		return "Item "+itemName+"=\""+stringVal+"\"";
	}
}
