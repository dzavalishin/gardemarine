package ru.dz.gardemarine;

import java.util.logging.Logger;

import javax.swing.UIManager;

import ru.dz.gardemarine.ui.logger.LogWindowLogHandler;

//import ru.dz.shipMaster.config.Configuration;
//import ru.dz.shipMaster.config.ConfigurationFactory;

/*
 * The general idea is that each entity is represented with 3 classes:
 * 
 * xxxConfig - configuration. What is saved/loaded. Bean. Is NOT used in runtime. Can reference other
 *             configs only.
 * 
 * xxxItem - actual object created in runtime to perform some task. Can reference own config
 *           and other items. Item state is not saved.
 *           
 * xxxEditor - tool to edit corresponding config state.
 * 
 * xxxVisual - used to represent item in visual world map.
 */


/**
 * Gardemarine main class.
 * @author dz
 */
public class Main {
	private static final Logger log = Logger.getLogger(Main.class.getName()); 


	//static SplashFrame sf = new SplashFrame();
	
	
	

    
		//TODO runningLine.addMessage("  Р�РґРµС‚ РґРёР°РіРЅРѕСЃС‚РёРєР° СЃРёСЃС‚РµРјС‹");		


	

	
	// --------------------------------------------------------------------
	// Main
	// --------------------------------------------------------------------
    


	//private static Configuration configuration;
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
		
		//try { ConfigurationFactory.load(); }
		//catch (Exception e) {/* ignore load failure */}
/*		
		configuration = ConfigurationFactory.getConfiguration(); // loaded here
		
		if(configuration.getGeneral().isSplashScreenShown())
			sf.showSplash();
		
		ConfigurationFactory.getTransientState().autoLogin();
		
		//Runtime.runFinalizersOnExit(true);
		
		DashBoard db = ConfigurationFactory.getTransientState().getDashBoard();
*/		
		{
			Logger rootLogger = Logger.getLogger(""); // root	
			// TODO fixme
			//rootLogger.addHandler(new LogWindowLogHandler(db.getLogWindow()));
		}
		
		log.severe("Starting");

/*		
    	ConfigurationFactory.startConfig();

		if(configuration.getGeneral().isAutostart())
			ConfigurationFactory.startSystem();

		if(!configuration.getGeneral().isSplashScreenShown())
			sf.hideSplash();
		
*/	
	}


	

}
