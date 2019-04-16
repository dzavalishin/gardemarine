package ru.dz.shipMaster.config.items;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import ru.dz.shipMaster.config.ConfigListItem;
import ru.dz.shipMaster.data.history.HistoryAlarmRecord;
import ru.dz.shipMaster.data.history.HistoryDataRecord;
import ru.dz.shipMaster.data.history.HistoryRecord;
import ru.dz.shipMaster.ui.config.item.CieLogger;

/**
 * Defines a logger, which is used to store history to some safe place. 
 * @author dz
 */
public class CliLogger extends ConfigListItem {
	public enum Type { 		
		/**
		 * Local file, CSV format. 
		 */
		LocalCsvFile, 
		/**
		 * Local file, XML format.
		 */
		LocalXmlFile, 
		/**
		 * Remote (or local) UDP syslogd-style logger. Port 514.
		 */
		SyslogdCsvUDP
	}


	private Type type = Type.LocalCsvFile;
	private String logDestinationName = "Gardemarine";

	private boolean		logData = true;
	private boolean		logAlarms = true;
	
	private boolean active = false;

	private PrintStream s = null;
	private XMLStreamWriter xmlWriter;

	private final Object recordWriteSemaphore = new Object();

	@Override
	public void destroy() {
		s = null;
		dialog = null;
		active = false;
	}


	// TO DO windows event logger
	// TO DO simple UDP sender
	// TO DO no log available alarm?
	// TO DO simple TCP sender

	// TO DO queue, separate store thread - partially solved in highter level code

	private final int CHECK_RECORDS = 1000;
	private int recCnt = CHECK_RECORDS; 

	private boolean oneTry = true;
	public void storeRecord(HistoryRecord r) {

		if (r instanceof HistoryAlarmRecord) {
			if(!logAlarms)
				return;
		}
		
		if (r instanceof HistoryDataRecord) {
			if(!logData)
				return;
		}
		
		synchronized (recordWriteSemaphore) {

			if(recCnt-- <= 0)
			{
				recCnt = CHECK_RECORDS;
				if(isNextDay())
					tryRestartLogger();			}

			if(!active && oneTry)
			{
				oneTry = false;
				tryRestartLogger();
			}

			switch(type)
			{
			case LocalCsvFile:
				storeCSVRecord(r);
				break;

			case LocalXmlFile:
				try {
					storeXMLRecord(r);
				} catch (XMLStreamException e) {
					log.log(Level.SEVERE,"Unable to record to XML log "+logDestinationName,e);
				}
				break;

			case SyslogdCsvUDP:
				sendUdp(CliLogger.syslogdFormat(DEFAULT_SYSLOGD_FACILITY, SEVERITY_WARNING, r.toCsv()));
				break;
			}

		}
	}

	private DatagramSocket socket;
	private InetAddress inetAddress;
	{
		try {
			socket = new DatagramSocket();
			inetAddress = InetAddress.getByName("127.0.0.1");
		} catch (SocketException e) {
			// ignore
		} catch (UnknownHostException e) {
			// ignore
		}
	}

	private void sendUdp(String msg) {

		byte[] pData;
		try {
			pData = msg.getBytes("UTF-8");
			DatagramPacket packet = new DatagramPacket(pData, pData.length, inetAddress , 514);
			socket.send(packet);
		} catch (UnsupportedEncodingException e) {
			log.log(Level.SEVERE,"UDP send error",e); // "can't be"
		} catch (IOException e) {
			log.log(Level.SEVERE,"UDP send error",e);
		}


	}

	private void storeCSVRecord(HistoryRecord r) {
		String csv = r.toCsv();
		if(s != null)
			s.println(csv);
	}

	private void storeXMLRecord(HistoryRecord r) throws XMLStreamException {
		if(xmlWriter != null)
		{
			r.writeXML(xmlWriter);
		}
	}


	private void tryRestartLogger()
	{
		try { restartLogger(); } 
		catch (IOException e) {
			log.log(Level.SEVERE,"Unable to open log file: "+getLogDestinationName(),e);
		}
	}

	public void restartLogger() throws IOException {

		switch(type)
		{
		case SyslogdCsvUDP:
			inetAddress = InetAddress.getByName(logDestinationName);
			break;

		case LocalCsvFile:
			if( s != null )
			{
				s.flush();
				s.close();
				s = null;
			}
			s = new PrintStream(new BufferedOutputStream(new FileOutputStream(getLogFileName(),true)));	
			break;

		case LocalXmlFile:
			if(xmlWriter != null)
			{
				try {
					xmlWriter.writeEndDocument();
					xmlWriter.close();
				} catch (XMLStreamException e) {
					log.log(Level.SEVERE,"Can't stop XML writer", e);
				}
			}
			try {
				xmlWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(
						new BufferedOutputStream(new FileOutputStream(getLogFileName(),true)),
				"UTF-8");
				xmlWriter.writeStartDocument();
			} catch (XMLStreamException e) {
				log.log(Level.SEVERE,"Can't start XML writer", e);
			} catch (FactoryConfigurationError e) {
				log.log(Level.SEVERE,"Can't configure XML writer factory", e);
			}
			break;
		}

		active = true;


	}



	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		if(xmlWriter != null)
		{
			xmlWriter.writeEndDocument();
		}
	}


	private CieLogger dialog = null;

	@Override
	public void displayPropertiesDialog() {
		if(!hasEditRight()) return;
		if(dialog == null) dialog = new CieLogger(this);
		dialog.displayPropertiesDialog(); 
	}

	@Override
	public String getName() {
		switch(type)
		{
		case LocalCsvFile:
			return "CSV file:/"+getLogFileName();

		case LocalXmlFile:
			return "XML file:/"+getLogFileName();

		case SyslogdCsvUDP:
			return "CSV udp://"+getLogFileName();
		}
		return "? "+getLogFileName();
	}





	public String getLogDestinationName() {		return logDestinationName;	}
	public void setLogDestinationName(String logFileName) 
	{		
		this.logDestinationName = logFileName;

		try { restartLogger(); } 
		catch (IOException e) {
			log.log(Level.SEVERE,"Unable to reopen log file: "+logFileName,e);
		}
	}

	
	public boolean isLogData() {		return logData;	}
	public void setLogData(boolean logData) {		this.logData = logData;	}

	public boolean isLogAlarms() {		return logAlarms;	}
	public void setLogAlarms(boolean logAlarms) {		this.logAlarms = logAlarms;	}


	// -----------------------------------------------------------------------
	// syslogd support
	// -----------------------------------------------------------------------

	//private final static int DEFAULT_SYSLOGD_FACILITY = 16; // local 0 
	private final static int DEFAULT_SYSLOGD_FACILITY = 14; // Alert 
	private final static DateFormat df = new SimpleDateFormat("MMM d HH:mm:ss Z yyyy",Locale.PRC);
	private static String host = "unknownHost";

	static {
		try {
			host = InetAddress.getLocalHost().toString();
		} catch (UnknownHostException e) {
		}
	}

	public final static int SEVERITY_EMERGENCY = 0; 	//       Emergency: system is unusable
	public final static int SEVERITY_ALERT = 1;	 	//       Alert: action must be taken immediately
	public final static int SEVERITY_CRITICAL = 2; 	//       Critical: critical conditions
	public final static int SEVERITY_ERROR = 3; 		//       Error: error conditions
	public final static int SEVERITY_WARNING = 4;	 	//       Warning: warning conditions
	public final static int SEVERITY_NOTICE = 5; 		//       Notice: normal but significant condition
	public final static int SEVERITY_INFO = 6; 		//       Informational: informational messages
	public final static int SEVERITY_DEBUG = 7; 		//       Debug: debug-level messages

	public static String syslogdFormat(int facility, int severity, String msg )
	{

		if(facility < 0) facility = 0;
		if(facility > 99) facility = 99;
		if(severity < 0) severity = 0;
		if(severity > 9) severity = 9;

		String time = df.format(new Date(System.currentTimeMillis()));

		return String.format(Locale.PRC, "<%d%d> %s %s %s", facility, severity, time, host, msg);
	}

	public Type getType() {		return type;	}
	public void setType(Type type) {		this.type = type;	}

	private int lastDayNo = -1;
	private boolean isNextDay()
	{
		Calendar c = Calendar.getInstance();

		int dayNo = c.get(Calendar.DAY_OF_YEAR);

		if(dayNo != lastDayNo)
		{
			lastDayNo = dayNo;
			return true;
		}

		return false;
	}

	private String getLogFileName()
	{
		String suffix = "log";

		switch(type)
		{
		case SyslogdCsvUDP: return logDestinationName;
		case LocalXmlFile: suffix = "xml"; break;
		case LocalCsvFile: suffix = "csv"; break;

		default: suffix = "log"; break;
		}

		Calendar c = Calendar.getInstance();

		int day = c.get(Calendar.DAY_OF_MONTH);
		int mon = 1 + c.get(Calendar.MONTH);

		return String.format("%s.%02d.%02d.%s", logDestinationName, day, mon, suffix );
	}


}
