package ru.dz.shipMaster.data.history;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import ru.dz.shipMaster.config.ConfigListItem;
import ru.dz.shipMaster.config.ConfigurationFactory;
import ru.dz.shipMaster.ui.config.item.CieHistoryRecord;

/**
 * Record object to be recorded in a system log file or database or whatever.
 * @author dz
 */
public abstract class HistoryRecord extends ConfigListItem {
	protected String		sourceNodeName;
	protected long			time;
	
	/**
	 * Default constructor will set time stamp to current time/date.
	 */
	public HistoryRecord()
	{
		time = System.currentTimeMillis();
	}
	
	@Override
	public void destroy() {
		dialog = null;			
	}

	
	/**
	 * Pass this record to system history storage facility for storing in log files and elsewhere. 
	 */
	public void store()
	{
		ConfigurationFactory.getTransientState().addHistoryRecord(this);
	}
	
	public String getSourceNodeName() {		return sourceNodeName;	}
	public void setSourceNodeName(String sourceNodeName) {		this.sourceNodeName = sourceNodeName;	}

	public long getTime() {		return time;	}
	public void setTime(long time) {		this.time = time;	cache = null; }

	private final static SimpleDateFormat f = new SimpleDateFormat();
	public String getDateTimeString()
	{
		return f.format(new Date(time));
	}
	
	public boolean isOlderThan(int sec)
	{
		return ((int)((System.currentTimeMillis()-time)/1000)) > sec;
	}
	
	// Dialog
	
	private CieHistoryRecord dialog = null;
	
	@Override
	public void displayPropertiesDialog() {
		if(dialog == null) dialog = new CieHistoryRecord(this);
		dialog.displayPropertiesDialog(); 
		}

	private final DateFormat csvDateFormat = DateFormat.getDateTimeInstance(
			DateFormat.SHORT, 
			DateFormat.MEDIUM);//new SimpleDateFormat();
	
	private String cache = null;
	public String toCsv() {
		if(cache == null)
		{
			String dt = csvDateFormat.format(new Date(time));
			cache = String.format("\"%s\",\"%s\"", sourceNodeName == null ? "localhost" : sourceNodeName, dt);
		}
		return cache;
	}


	// XML write
	public final void writeXML(XMLStreamWriter w) throws XMLStreamException
	{
		writeXMLStart(w);
		writeXMLEnd(w);
	}

	/**
	 * Override in child, call super.writeXMLEnd(xmlWriter) in last line of method.
	 * @param xmlWriter XML writer.
	 * @throws XMLStreamException On io error.
	 */
	protected void writeXMLEnd(XMLStreamWriter xmlWriter) throws XMLStreamException  {
		xmlWriter.writeEndElement();
	}

	/**
	 * Override in child, call super.writeXMLEnd(xmlWriter) in first line of method.
	 * @param xmlWriter XML writer.
	 * @throws XMLStreamException On io error.
	 */
	protected void writeXMLStart(XMLStreamWriter xmlWriter) throws XMLStreamException {
		xmlWriter.writeStartElement("record");

		xmlWriter.writeAttribute("time", getDateTimeString());
		xmlWriter.writeAttribute("msec", ""+getTime());
		
		if(getSourceNodeName() != null)
			xmlWriter.writeAttribute("node", getSourceNodeName());		
	}

}
