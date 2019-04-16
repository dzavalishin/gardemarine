package ru.dz.gardemarine.ui.logger;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
//import java.util.logging.Logger;

/**
 * Used to redirect Java log output to log window.
 * @author dz
 */
public class LogWindowLogHandler extends Handler {
    //private static final Logger log = Logger.getLogger(LogWindowLogHandler.class.getName()); 

	// TODO fixme

	/*
    private final LogWindow destination;

	public LogWindowLogHandler(LogWindow destination)
	{
		this.destination = destination;		
	}
	*/
	
	
	
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
        
    	// TODO fixme
		//destination.addMessage(logRecord.getMessage());
	}

	@Override
	public void close() throws SecurityException { }

	@Override
	public void flush() { }

}
