package ru.dz.shipMaster.data.history;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * HIstory record for the alarm events.
 * @author dz
 */
public class HistoryAlarmRecord extends HistoryRecord {
	private String 		alarmName;
	private boolean		start;
	private boolean 	critical;
	
	public HistoryAlarmRecord( boolean critical, String text, boolean start )
	{
		this.critical = critical;
		alarmName = text;
		this.start = start;		
	}
	
	public String getAlarmName() {		return alarmName;	}
	public void setAlarmName(String alarmName) {		this.alarmName = alarmName;	}

	public boolean isStart() {		return start;	}
	public void setStart(boolean start) {		this.start = start;	}

	public boolean isCritical() {		return critical;	}
	public void setCritical(boolean critical) {		this.critical = critical;	}

	@Override
	public String getName() {
		return getDateTimeString() + ": " + alarmName + " " + (start ? "start" : "end");
	}

	public String toCsv() {
		return String.format("%s,\"Alarm\",\"%s\",\"%s\",\"%s\"", 
				super.toCsv(),
				alarmName,
				start ? "Start" : "End",
				critical ? "Critical" : "Waring"
		);
	}

	@Override
	protected void writeXMLStart(XMLStreamWriter xmlWriter) throws XMLStreamException {
		super.writeXMLStart(xmlWriter);
		xmlWriter.writeAttribute("alarm", getAlarmName());
		xmlWriter.writeAttribute("start", ""+isStart());	
		xmlWriter.writeAttribute("severity", isCritical() ? "'critical" : "warning");			
	}

	
}
