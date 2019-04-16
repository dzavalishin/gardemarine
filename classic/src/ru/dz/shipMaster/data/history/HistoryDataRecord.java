package ru.dz.shipMaster.data.history;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class HistoryDataRecord extends HistoryRecord {
	private double			value;
	protected String		parameterName;

	public HistoryDataRecord() {	}
	
	public HistoryDataRecord(String parameterName, double value )
	{
		this.parameterName = parameterName;
		this.value = value;		
	}

	public String toCsv() {
		return String.format("%s,\"Data\",\"%s\",\"%.3f\"", 
				super.toCsv(),
				parameterName,
				value
		);
	}
	
	@Override
	protected void writeXMLStart(XMLStreamWriter xmlWriter) throws XMLStreamException {
		super.writeXMLStart(xmlWriter);
		xmlWriter.writeAttribute("parameter", getParameterName());
		xmlWriter.writeAttribute("value", ""+getValue());		
	}
	
	
	public double getValue() {		return value;	}
	public void setValue(float value) {		this.value = value;	}

	public String getParameterName() {		return parameterName;	}
	public void setParameterName(String parameterName) {		this.parameterName = parameterName;	}
	
	
	@Override
	public String getName() {
		return  getDateTimeString() + ": " + parameterName + " = " + value;
	}
}
