package ru.dz.shipMaster.ui.logger;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Used to redirect Java log output to log window.
 * @author dz
 */
public class LogWindowLogHandler extends Handler {
    //private static final Logger log = Logger.getLogger(LogWindowLogHandler.class.getName()); 


    private final LogWindow destination;

	public LogWindowLogHandler(LogWindow destination)
	{
		this.destination = destination;		
	}
	
	@Override
	public void publish(LogRecord logRecord) {
		StringBuilder out = new StringBuilder(); 
		
        //out.append(logRecord.getLevel());
		//out.append(": ");

        out.append(logRecord.getSourceClassName());
        out.append(".");

        out.append(logRecord.getSourceMethodName());
        out.append("() ");
		//out.append("<" + logRecord.getMessage() + ">");
        
		//destination.addMessage(out.toString());
		destination.addMessage(logRecord.getMessage());
	}

	@Override
	public void close() throws SecurityException { }

	@Override
	public void flush() { }

}
