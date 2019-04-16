package ru.dz.shipMaster.ui;

import java.awt.Color;
import java.util.logging.Logger;

import javax.swing.JFrame;

import ru.dz.shipMaster.ui.misc.VisualHelpers;

/**
 * Main Gardemarine window class.
 * @author dz
 */
public class MainFrame extends JFrame {
    @SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(MainFrame.class.getName()); 


    /**
	 * Eclipse id.
	 */
	private static final long serialVersionUID = -3101088016715630711L;
	
	private DimmerComponent dimmer = new DimmerComponent();
	
	/**
	 * This method initializes 
	 * 
	 */
	public MainFrame() {
		super();
		
		setUndecorated(true);
	    //frame.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);

		//setBackground(VisualSettings.panelBgColor);
		setBackground(Color.black);
		initialize();
		
		setGlassPane(dimmer);
		dimmer.setVisible(true);		
	}

	/**
	 * Set if dimmer will be active for this window.
	 * @param enabled Dimmer on/off.
	 */
	public void setDimmerEnabled(boolean enabled) { dimmer.setVisible(enabled); }
	/**
	 * @return Dimmer on/off.
	 */
	public boolean getDimmerEnabled() { return dimmer.isVisible(); }
	
	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setTitle("Gardemarine");
        this.setIconImage(VisualHelpers.getApplicationIconImage());
		//this.setBackground(VisualSettings.global.bgColor);
	}




}  
