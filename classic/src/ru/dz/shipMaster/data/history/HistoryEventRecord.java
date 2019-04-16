package ru.dz.shipMaster.data.history;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * Event record for history storage. It is supposed that events are more or less rare thing.
 * @author dz
 */
public class HistoryEventRecord extends HistoryRecord {
	private String source;
	private String shortName;
	private String description;

	
	/**
	 * Create new event record.
	 * @param source Short name of event source. User, class, package, etc. Not a network node, though!
	 * Network node can be set for history event separately. 
	 * @param shortName Short name of event. One word.
	 * @param description Description and details.
	 */
	public HistoryEventRecord(String source, String shortName, String description )
	{
		this.source = source;
		this.shortName = shortName;
		this.description = description;		
	}
	
	@Override
	public String toCsv() {
		return String.format("%s,\"Event\",\"%s\",\"%s\",\"%s\"", 
				super.toCsv(),
				source,
				shortName,
				description	);
	}
	
	@Override
	protected void writeXMLStart(XMLStreamWriter xmlWriter)
			throws XMLStreamException {
		super.writeXMLStart(xmlWriter);
		xmlWriter.writeAttribute("source", getSource());
		xmlWriter.writeAttribute("shortName", getShortName());	
		xmlWriter.writeAttribute("description", getDescription());			
	}

	
	public String getSource() {		return source;	}
	/**
	 * Source of the event. Free formed. Class name for code, user name for user, etc.
	 * @param source Source identifier.
	 */
	public void setSource(String source) {		this.source = source;	}

	public String getShortName() {		return shortName;	}
	/**
	 * Short name of event. To be used for filtering.
	 * @param shortName One word name about what happened.
	 */
	public void setShortName(String shortName) {		this.shortName = shortName;	}

	public String getDescription() {		return description;	}
	/**
	 * Description of event. Can be long (but more or less like one line of text).
	 * @param description Text of description.
	 */
	public void setDescription(String description) {		this.description = description;	}

	@Override
	public String getName() { return source + ": "+shortName; }

}
