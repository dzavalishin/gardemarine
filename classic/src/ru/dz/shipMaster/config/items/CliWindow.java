package ru.dz.shipMaster.config.items;

import java.awt.PopupMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import ru.dz.shipMaster.ScreenManager;
import ru.dz.shipMaster.config.ConfigListItem;
import ru.dz.shipMaster.config.ConfigurationFactory;
import ru.dz.shipMaster.dev.system.ScreenDimmer;
import ru.dz.shipMaster.ui.MainFrame;
import ru.dz.shipMaster.ui.config.item.CieWindow;


/**
 * This is one of the dashboard windows. It can contain instruments (indicators),
 * buttons, etc.
 * @author dz
 *
 */

public class CliWindow extends ConfigListItem {


	private String name = "group.name";
	private boolean autostart = true;
	private boolean fullscreen = false;
	private int screenNumber = 0;
	private int windowNumber = 0;
	private Vector<CliInstrumentPanel> panels = new Vector<CliInstrumentPanel>();

	private CliWindowStructure		structure;

	@Override
	public void destroy() {
		panels.removeAllElements();
		structure = null;
		dialog = null;			
	}



	private CieWindow dialog = null;

	@Override
	public void displayPropertiesDialog() {
		if(!hasEditRight()) return;
		if(dialog == null) dialog = new CieWindow(this);
		dialog.displayPropertiesDialog(); 
	}

	// Start/stop routive work

	private MainFrame frame = new MainFrame();
	{
		//frame.setVisible(false); // X XX remove setVisible(true) from MainFrame init!
		windowNumber = maxWN++;
	}

	//private DashBoard testDashBoard = new DashBoard(); 
	public void startWindow() {
		CieWindow tempDialog = new CieWindow(this);
		tempDialog.discardSettings();

		frame.setContentPane(tempDialog.composeWindow( 
				ConfigurationFactory.getTransientState().getDashBoard(), false ));
		frame.pack();
		frame.setTitle(name);

		final PopupMenu popupMenu = ScreenManager.getMainPopupMenu();
		//frame.add(popupMenu);
		
		frame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if( e.isPopupTrigger())
					popupMenu.show(frame, e.getX(), e.getY());
				else
					super.mouseClicked(e);
			}
		});


		tempDialog = null;

		//frame.setVisible(false); 

		if(isFullscreen())
			ScreenManager.goFullScreen(frame, getScreenNumber()); 
		else
		{
			ScreenManager.moveToScreen(frame, getScreenNumber() );
			frame.setVisible(true); 
		}

		ScreenDimmer.registerFrame(frame, windowNumber );
	}

	public void stopWindow() {
		frame.setVisible(false);
		
		/*
			try {
				frame.getContentPane().removeAll();
			}
			catch(Throwable e)
			{
				log.log(Level.SEVERE,"Window ecompose failure",e);
			}
		*/
		//frame.dispose();
		//frame = null;
	}

	public void toFront() {
		frame.setVisible(true);
		frame.toFront();
	}



	// Getters/setters

	public String getName() {		return name;	}
	public void setName(String name) {		this.name = name;	}

	public boolean isAutostart() {		return autostart; 	}
	public void setAutostart(boolean autoStart) { this.autostart = autoStart; }

	public boolean isFullscreen() {		return fullscreen;	}
	public void setFullscreen(boolean fullscreen) {		this.fullscreen = fullscreen;	}

	public int getScreenNumber() {		return screenNumber;	}
	public void setScreenNumber(int screenNumber) {		this.screenNumber = screenNumber;	}

	public CliWindowStructure getStructure() {		return structure;	}
	public void setStructure(CliWindowStructure structure) {		this.structure = structure;	}

	public Vector<CliInstrumentPanel> getPanels() {		return panels;	}
	public void setPanels(Vector<CliInstrumentPanel> panels) {		
		//this.panels = panels;
		this.panels.clear();
		this.panels.addAll(panels);
	}

	public int getWindowNumber() {		return windowNumber;	}
	public void setWindowNumber(int windowNumber) {		this.windowNumber = windowNumber; updateMaxWN();	}


	private static int maxWN = 0; 
	private void updateMaxWN() {
		if( maxWN <= windowNumber )
			maxWN = windowNumber+1;
	}



}
