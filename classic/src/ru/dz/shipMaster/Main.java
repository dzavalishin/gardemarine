package ru.dz.shipMaster;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.UIManager;

import ru.dz.shipMaster.config.Configuration;
import ru.dz.shipMaster.config.ConfigurationFactory;
import ru.dz.shipMaster.misc.SplashFrame;
import ru.dz.shipMaster.ui.logger.LogWindowLogHandler;

/**
 * Gardemarine main class.
 * @author dz
 */
public class Main {
	private static final Logger log = Logger.getLogger(Main.class.getName()); 


	static SplashFrame sf = new SplashFrame();
	
	
	

    
	/**
	 * Create the GUI and show it.  For thread safety,
	 * this method should be invoked from the
	 * event-dispatching thread.
	 */
    protected static void createAndShowGUI() {

    	
		//TODO runningLine.addMessage("  Идет диагностика системы");		
		
		
    }


	

	
	// --------------------------------------------------------------------
	// Main
	// --------------------------------------------------------------------
    


	private static Configuration configuration = null;
    /**
     * Well. Gardemarine main.
     * @param args
     */
    public static void main(String[] args) {
		try { 
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			//UIManager.setLookAndFeel("org.jvnet.substance.SubstanceLookAndFeel");
			} 
		catch (Exception e) {/* ignore inability to set l&f */}

		boolean disableAutoStart = false;

		for( String arg : args )
		{
			if( arg.equalsIgnoreCase("-help"))
			{
				System.out.println(
						"\t-help\t\t\t- this text\n"+
						"\t-nostart\t\t- disable autostart\n"
						);
				System.exit(0);
			}
			if( arg.equalsIgnoreCase("-nostart"))
				disableAutoStart = true;
		}
		
		configuration = ConfigurationFactory.getConfiguration(); // loaded here
		
		reopenLogFile();
		
		if(configuration.getGeneral().isSplashScreenShown())
			sf.showSplash();
		
		ConfigurationFactory.getTransientState().autoLogin();
		
		//Runtime.runFinalizersOnExit(true);
		
		DashBoard db = ConfigurationFactory.getTransientState().getDashBoard();
		
		{
			Logger rootLogger = Logger.getLogger(""); // root	
			rootLogger.addHandler(new LogWindowLogHandler(db.getLogWindow()));
		}
		
		log.severe("Starting");

		
    	ConfigurationFactory.startConfig();

		if(configuration.getGeneral().isAutostart() && !disableAutoStart)
			ConfigurationFactory.startSystem();

		if(!configuration.getGeneral().isSplashScreenShown())
			sf.hideSplash();
		
	
	}
    
    static FileHandler logFileHandler = null;
    
	public static void reopenLogFile() {
		
	    Logger logger = Logger.getLogger("ru.dz");
		if(logFileHandler != null) 
			logger.removeHandler(logFileHandler);

		String logFileName = ConfigurationFactory.getConfiguration().getGeneral().getLogFileName();
		if( logFileName == null || logFileName.trim().isEmpty())
			return;
		
		log.log(Level.SEVERE, "Open log file "+logFileName);
		
		try {			
		    // Create an appending file handler
		    boolean append = false;
		    FileHandler handler = new FileHandler(logFileName, append);

		    handler.setFormatter(new SimpleFormatter());
		    
		    // Add to the desired logger
		    logger.addHandler(handler);
		} catch (IOException e) {
			log.log(Level.SEVERE, "Can't open log file "+logFileName, e);
		}
 
		
	}


	

}
