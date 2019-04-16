package ru.dz.shipMaster.geo;

/**
 * Base class used by LatitudeValue and LongitudeValue for translating between
 * a float and deg/min/sec.
 */
public class GeographicValue {

	//enum Sign { Negative, Positive };
	
	protected int degrees;
	protected int minutes;
	protected int seconds;
	
	protected int sign;
	
	protected double decimalValue;

	// ---------------------------------------------
	//
	public GeographicValue() {
		this.decimalValue = 0;
		decimalToDegMinSec();
	}

	// ---------------------------------------------
	//
	public GeographicValue( double decVal ) {
		this.decimalValue = decVal;
		decimalToDegMinSec();
	}

	// ---------------------------------------------
	//
	public void decimalToDegMinSec() {
		sign = 1;
		if (decimalValue < 0) sign = -1;

		double num1 = Math.abs( decimalValue );
		degrees = (new Double( num1)).intValue();
		double f1 = Math.abs( num1 - degrees);
		double f2 = (f1 * 60);

		double num2 = Math.abs( f2 );
		minutes = new Double( Math.floor( (new Double(num2)).doubleValue() )).intValue();
		double f3 = Math.abs( num2 - minutes);
		double dd = f3 * 60;
		seconds = new Long( Math.round( dd )).intValue();
		if (seconds == 60) {
			seconds = 0;
			minutes++;
		}


	}


	// ---------------------------------------------
	//
	public void degMinSecToDecimal() {
		float minVal = minutes;
		float degVal = degrees;
		float secVal = seconds;
		if (seconds == 0) secVal = 0.1f;
		minVal += (secVal/60f);
		degVal += (minVal/60f);
		degVal = degVal * sign;
		decimalValue = degVal;
	}

	// ---------------------------------------------
	//
	public void setDegrees( int degrees ) {
		this.degrees = degrees;
	}
	// ---------------------------------------------
	//
	public void setMinutes( int minutes ) {
		this.minutes = minutes;
	}
	// ---------------------------------------------
	//
	public void setSeconds( int seconds ) {
		this.seconds = seconds;
	}
	// ---------------------------------------------
	//
	public void setSign( int sign ) {
		this.sign = sign;
	}
	// ---------------------------------------------
	//
	public void setDecimalValue( double decimalValue ) {
		this.decimalValue = decimalValue;
		decimalToDegMinSec();
	}

	// ---------------------------------------------
	//
	public int getDegrees( ) {
		return degrees;
	}
	// ---------------------------------------------
	//
	public int getMinutes( ) {
		return minutes;
	}
	// ---------------------------------------------
	//
	public int getSeconds( ) {
		return seconds;
	}
	// ---------------------------------------------
	//
	public int getSign( ) {
		return sign;
	}
	// ---------------------------------------------
	//
	public double getDecimalValue( ) {
		return decimalValue;
	}

	// ---------------------------------------------
	//
	public String toString( ) {
		return String.format("%d° %d' %d\" (%f)", degrees, minutes, seconds, decimalValue);
	}

	/**
	 * 
	 * @return String in x° y' x" format.
	 */
	public String toDegMinSecString( ) {
		return String.format("%d° %d' %d\"", degrees, minutes, seconds );
	}

	/**
	 * @param posChar char to append for positive value
	 * @param negChar char to append for negative value
	 * @param nDigits 
	 * @return String in x° y' x" C format.
	 */
	public String toDegMinSecSignString( char posChar, char negChar, int nDigits ) {
		
		/*
		return String.format("%3d°%02d'%03d\"%s", degrees, minutes, nDigits, seconds,
				sign < 0 ? negChar : posChar
		);*/
		
		// Very dumb, but...	
		if(nDigits > 2)
			return String.format("%3d°%02d'%03d\"%s", degrees, minutes, seconds,
					sign < 0 ? negChar : posChar
			);
		else
			return String.format("%3d°%02d'%02d\"%s", degrees, minutes, seconds,
				sign < 0 ? negChar : posChar
		);
	}

	
	// ---------------------------------------------
	//
	public static void main( String args[] ) {
		GeographicValue l1 = new GeographicValue( -154.7002716064453f);
		System.out.println(" toString: " + l1.toString());
		System.out.println(" toDegMinSecSignString 2: " + l1.toDegMinSecSignString('N','S',2));
		System.out.println(" toDegMinSecSignString 3: " + l1.toDegMinSecSignString('N','S',3));
	}

}
