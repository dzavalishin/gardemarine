package ru.dz.shipMaster.config;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JPanel;

import ru.dz.shipMaster.DashBoard;
import ru.dz.shipMaster.Version;
import ru.dz.shipMaster.config.items.CliLogger;
import ru.dz.shipMaster.config.items.CliRight;
import ru.dz.shipMaster.config.items.CliUser;
import ru.dz.shipMaster.data.com.ComPortDispatcher;
import ru.dz.shipMaster.data.com.GnuComPortDispatcher;
import ru.dz.shipMaster.data.history.HistoryAlarmRecord;
import ru.dz.shipMaster.data.history.HistoryRecord;
import ru.dz.shipMaster.data.history.HistoryStorage;
import ru.dz.shipMaster.misc.RightsControl;
import ru.dz.shipMaster.ui.misc.VisualHelpers;

/**
 * This class is supposed to store and make accessible all the state of the program, which is not
 * supposed to be saved in configuration or be static (code-defined).
 * @author dz
 */
@SuppressWarnings({ "serial" })
public class TransientState {
	private static final Logger log = Logger.getLogger(TransientState.class.getName()); 



	// -----------------------------------------------------------------------
	// History
	// -----------------------------------------------------------------------

	private static final int HISTORY_THREAD_POOL_MULTIPLE = 20;

	//private HistoryStorage historyStorage = new HistoryStorage(3*3600);
	private HistoryStorage historyStorage = new HistoryStorage(1200);
	private HistoryStorage alarmStorage = new HistoryStorage(1200);
	
	ExecutorService xec = Executors.newFixedThreadPool(
			Runtime.getRuntime().availableProcessors() * HISTORY_THREAD_POOL_MULTIPLE);


	/**
	 * Stores history record to runtime history window (which will keep records for some time)
	 * and stores record using defined loggers.
	 * @param r record to store.
	 */
	public void addHistoryRecord(HistoryRecord r) 
	{ 
		if( r instanceof HistoryAlarmRecord )
			alarmStorage.addRecord(r);
		else
			historyStorage.addRecord(r);

		for( CliLogger l : ConfigurationFactory.getConfiguration().getLoggerItems() )
		{
			//l.storeRecord(r);
			runInPool(r, l);
		}
	}

	private void runInPool(final HistoryRecord r, final CliLogger l) {
		xec.execute(new Runnable() 
		{
			public void run() {
				l.storeRecord(r);
			}} );
	}

	/**
	 * Get actual list of a system history storage for history records. Supposed to be read only.
	 * Note that this list is updated from one side and cut from other side hundreds times a second.
	 * @return List of history records.
	 */
	public List<HistoryRecord> getHistory() { return historyStorage.getList(); }

	/**
	 * Get actual list of a system storage for alarm records. Supposed to be read only.
	 * Note that this list is possibly updated from one side and cut from other side hundreds times a second.
	 * @return List of alarm records.
	 */
	public List<HistoryRecord> getAlarms() { return alarmStorage.getList(); }


	// -----------------------------------------------------------------------
	// DashBoard
	// -----------------------------------------------------------------------


	private DashBoard db = new DashBoard();
	public DashBoard getDashBoard() {
		return db;
	}


	// -----------------------------------------------------------------------
	// Login
	// -----------------------------------------------------------------------

	private CliUser loggedInUser = null;
	public void login(JPanel referencePanel)
	{
		//loggedInUser = ConfigurationFactory.getConfiguration().selectUser(referencePanel);

		CliUser user = ConfigurationFactory.getConfiguration().login(referencePanel);
		if(user != null)
		{
			loggedInUser = user;
			updateTrayData();
		}
		else
		{
			if(loggedInUser != null)
				VisualHelpers.showMessageDialog(referencePanel, "User "+loggedInUser.getLogin()+" still logged in");
		}
	}

	/**
	 * Perform automatic login action. Must be called during program start.
	 */
	public void autoLogin()
	{
		if(!ConfigurationFactory.getConfiguration().getGeneral().isAutologin())
			return;
		CliUser user = ConfigurationFactory.getConfiguration().getGeneral().getAutologinUser();
		if(user != null)
		{
			loggedInUser = user;
			updateTrayData();
		}
	}

	/**
	 * If current logged in user has this right.
	 * @param r Right to check.
	 * @return True if current user has this right.
	 * /
	public boolean hasRight(CliRight r)
	{
		if(loggedInUser == null)
			return false;

		return loggedInUser.hasRight(r);
	}

	/**
	 * Same as hasRight(), but shows warning message if user has no
	 * requested right.
	 * 
	 * @see ru.dz.shipMaster.config.TransientState#hasRight(CliRight)
	 * @param r Right to check.
	 * @return True if currently logged used has this right.
	 * /
	public boolean assertRight(CliRight r)
	{
		if(hasRight(r))
			return true;
		VisualHelpers.showMessageDialog(null, "You have no '"+r.getName()+"' right");
		return false;
	}*/

	
	public boolean hasEditRight(Class<? extends RightsControl> target) {
		if(loggedInUser == null)
			return false;

		return loggedInUser.canEdit(target);
	}

	public boolean assertEditRight(Class<? extends RightsControl> target)
	{
		if(hasEditRight(target))
			return true;
		VisualHelpers.showMessageDialog(null, "You have no right to edit this");
		return false;
	}
	
	
	public String getLoggedInUserLogin()
	{
		return loggedInUser  == null ? "none" : loggedInUser.getLogin();		
	}


	// -----------------------------------------------------------------------
	// Tray icon
	// -----------------------------------------------------------------------


	private SystemTray tray = SystemTray.getSystemTray();
	private TrayIcon tIcon; 
	//private String toolTip = "Gardemarine";

	{
		PopupMenu menu = new PopupMenu();
		MenuItem loginItem = new MenuItem("User login"); menu.add(loginItem);

		MenuItem startItem = new MenuItem("Start Gardemarine"); menu.add(startItem);
		MenuItem stopItem = new MenuItem("Stop Gardemarine"); menu.add(stopItem);

		MenuItem startConfig = new MenuItem("Setup"); menu.add(startConfig);
		//MenuItem stopConfig = new MenuItem("Hide setup"); menu.add(stopItem);

		loginItem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				ConfigurationFactory.getTransientState().login(null);
			}});

		startItem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				ConfigurationFactory.startSystem();
			}});

		stopItem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				ConfigurationFactory.stopSystem();
			}});

		startConfig.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				ConfigurationFactory.startConfig();
			}});
		
		Image image = VisualHelpers.getApplicationIconImage(); //loadImage("lock.png");
		tIcon = new TrayIcon(image,"",menu);

		try {
			tray.add(tIcon);			
		} catch (AWTException e) {
			log.log(Level.SEVERE,"Error adding tray icon",e);
		}

		setupTrayTooltip();
	}

	private Vector<String> trayMessages = new Vector<String>(); 
	private void setupTrayTooltip()
	{
		StringBuilder tooltip = new StringBuilder(String.format("Gardemarine ver. %s\n%s\n%s", 
				Version.VERSION, 
				ConfigurationFactory.isStarted() ? "System is running" : "System is stopped",
						(loggedInUser == null) ? "Nobody logged in" : "User '"+loggedInUser.getLogin()+"' logged in"
		));

		
		if(ConfigurationFactory.isNetworkActive())
		{
			tooltip.append("\nHostname: ");
			tooltip.append(ConfigurationFactory.getConfiguration().getGeneral().getNetNodeName());
		}
		else
			tooltip.append("\nNetwork is not running");

		if(trayMessages != null)
		{
			for( String msg : trayMessages )
			{
				if(msg != null)
				{
					tooltip.append('\n');
					tooltip.append(msg);
				}
			}
		}

		tIcon.setToolTip( tooltip.toString() );
	}

	public void updateTrayData() {
		setupTrayTooltip();
	}

	public void setTrayMessage(int i, String message) {
		trayMessages.setSize(Math.max(trayMessages.size(),i+1));
		trayMessages.set(i, message);
		setupTrayTooltip();
	}

	// -----------------------------------------------------------------------
	// Com ports
	// -----------------------------------------------------------------------
	
	private ComPortDispatcher comPortDispatcher = new GnuComPortDispatcher();
	public ComPortDispatcher getComPortDispatcher() {
		return comPortDispatcher;
	}


}
