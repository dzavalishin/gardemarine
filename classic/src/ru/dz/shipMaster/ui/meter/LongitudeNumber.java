package ru.dz.shipMaster.ui.meter;

import ru.dz.shipMaster.geo.GeographicValue;

@SuppressWarnings("serial")
public class LongitudeNumber extends GeoNumber {

	private GeographicValue geographicValue = new GeographicValue();

	protected String formatValue(double value) {
		geographicValue.setDecimalValue(value);
		
		return geographicValue.toDegMinSecSignString('E', 'W', nDigits);
	}

}
