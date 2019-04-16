package ru.dz.shipMaster.dev.controller;

/**
 * Modbus protocol replied with error.
 * @author dz
 *
 */
@SuppressWarnings("serial")
public class ModBusProtocolException extends ModBusException {

	/*
	public ModBusProtocolException() {	}

	public ModBusProtocolException(String message, Throwable cause) {
		super(message, cause);
	}

	public ModBusProtocolException(String message) {
		super(message);
	}

	public ModBusProtocolException(Throwable cause) {
		super(cause);
	}
	 */

	static String codeToString(int code)
	{
		String errText = "?";
		switch(code & 0xFF)
		{
		case 0x01:		errText = "Illegal function"; break;
		case 0x02:		errText = "Illegal data address"; break;
		case 0x03:		errText = "Illegal data value"; break;
		case 0x04:		errText = "Slave device failure"; break;
		case 0x05:		errText = "Acknowledge"; break;
		case 0x06:		errText = "Server Busy"; break;
		case 0x08:		errText = "Memory parily error"; break;
		case 0x0A:		errText = "Gateway Path Unavailable"; break;
		case 0x0B:		errText = "Gateway target device failed to respond"; break;
		}
		return errText;
	}

	public ModBusProtocolException(int code) {
		super(codeToString(code), code);
	}

}
