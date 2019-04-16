package ru.dz.shipMaster.misc;

@SuppressWarnings("serial")
public class UnitConversionException extends Exception {

	public UnitConversionException() {
	}

	public UnitConversionException(String message) {
		super(message);
	}

	public UnitConversionException(Throwable cause) {
		super(cause);
	}

	public UnitConversionException(String message, Throwable cause) {
		super(message, cause);
	}

}
