package ru.dz.shipMaster;

import ru.dz.shipMaster.ui.logger.LogWindow;
import ru.dz.shipMaster.ui.logger.MessageWindow;

/**
 * General class for all the internal dashboard communications;
 * @author dz
 *
 */
public class DashBoard {
    //private static final Logger log = Logger.getLogger(DashBoard.class.getName()); 

	protected LogWindow dashLog = new LogWindow();
	public LogWindow getLogWindow() { return dashLog; }
	
	protected MessageWindow dashMessages = new MessageWindow();
	public MessageWindow getMessageWindow() { return dashMessages; }
}
