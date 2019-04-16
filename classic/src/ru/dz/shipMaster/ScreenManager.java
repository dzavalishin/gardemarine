package ru.dz.shipMaster;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;

import ru.dz.shipMaster.config.ConfigurationFactory;

public class ScreenManager {
	private static final Logger log = Logger.getLogger(ScreenManager.class.getName()); 

	private static final GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();

	

	public static Vector<GraphicsDevice> getScreens()
	{
		Vector<GraphicsDevice> out = new Vector<GraphicsDevice>(4);
		GraphicsDevice[] screenDevices = localGraphicsEnvironment.getScreenDevices();
		for(int i = 0; i < screenDevices.length; i++)
		{
			if( screenDevices[i].getType() != GraphicsDevice.TYPE_RASTER_SCREEN )
				continue;
			out.add(screenDevices[i]);
		}

		return out;
	}

	public static int countScreens() { return getScreens().size(); }

	/*
	public static int countScreens()
	{
		GraphicsDevice[] screenDevices = localGraphicsEnvironment.getScreenDevices();
		int nScreens = 0;
		for(int i = 0; i < screenDevices.length; i++)
		{
			if( screenDevices[i].getType() != GraphicsDevice.TYPE_RASTER_SCREEN )
				continue;
			nScreens++;
		}
	return nScreens;	
	}*/


	/**
	 * Sets window as fullscreen window in given screen (display device).
	 * Returns size of screen for that device.
	 * @param window Windo to make fullscreen
	 * @param targetScreen Number of screen for that window
	 * @return Size of screen.
	 */
	public static Dimension goFullScreen(JFrame window, int targetScreen) {
		//GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		//Rectangle maximumWindowBounds = localGraphicsEnvironment.getMaximumWindowBounds();
		//log.severe(maximumWindowBounds.toString());

		Vector<GraphicsDevice> screens = getScreens();


		int nScreens = screens.size();
		if(targetScreen >= nScreens)
			targetScreen = nScreens-1; // Use last screen


		GraphicsDevice tagretScreenDevice = screens.get(targetScreen);

		/*
		GraphicsDevice[] screenDevices = localGraphicsEnvironment.getScreenDevices();
		int nScreen = 0;
		for(int i = 0; i < screenDevices.length; i++)
		{
			if( screenDevices[i].getType() != GraphicsDevice.TYPE_RASTER_SCREEN )
				continue;

			log.severe(screenDevices[i].toString());
			//screenDevices[i].isDisplayChangeSupported();
			// uncomment to use first screen, comment for last
			if(nScreen == targetScreen)
				tagretScreenDevice = screenDevices[i];
			nScreen++;
		}*/



		/*if(nScreens <= 1)
		{
			Dimension size = new Dimension(1000,600);
			window.setSize(size);
			window.pack();
			return size;
		}*/

		if(tagretScreenDevice == null)
		{
			log.severe("No screen devices found, i'll die now.");
			throw new RuntimeException("Need some graphics screen");
		}

		DisplayMode displayMode = tagretScreenDevice.getDisplayMode();
		Dimension size = new Dimension(displayMode.getWidth(),displayMode.getHeight());


		/*if(tagretScreenDevice.isFullScreenSupported())
		{
			window.setVisible(true);
			window.pack();
			tagretScreenDevice.setFullScreenWindow(null); // First exit any prev fullscreen mode
			tagretScreenDevice.setFullScreenWindow(window);
		}
		else /* */
		{
			//GraphicsDevice defaultScreenDevice = localGraphicsEnvironment.getDefaultScreenDevice();

		    
			
			window.setVisible(true);
			window.pack();
			window.toFront();
			
			Insets is = window.getInsets();

			int verticallSizeAddition = 0;
			
			Dimension sz = new Dimension(size.width+is.left+is.right,size.height+is.top+is.bottom+verticallSizeAddition);
			
			window.setLocation(-is.left, -is.top);
			log.log(Level.SEVERE,"set sz = "+sz);
			window.setSize(sz);
			log.log(Level.SEVERE,"got sz = "+window.getSize());
		}

		return size;
	}

	public static Dimension moveToScreen(JFrame window, int targetScreen) {
		Vector<GraphicsDevice> screens = getScreens();

		int nScreens = screens.size();
		if(targetScreen >= nScreens)
			targetScreen = nScreens-1; // Use last screen		

		GraphicsDevice tagretScreenDevice = screens.get(targetScreen);


		Rectangle bounds = tagretScreenDevice.getDefaultConfiguration().getBounds();
		window.setLocation(bounds.getLocation());


		DisplayMode displayMode = tagretScreenDevice.getDisplayMode();
		Dimension size = new Dimension(displayMode.getWidth(),displayMode.getHeight());

		//window.setSize(size);	
		window.pack();
		window.toFront();

		return size;
	}

	
	public static PopupMenu getMainPopupMenu()
	{
		PopupMenu menu = new PopupMenu();
		
		MenuItem stopItem = new MenuItem("Stop");  
		stopItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ConfigurationFactory.stopSystem();
				
			}
		});
		menu.add(stopItem);
		
		return menu;
	}
}
