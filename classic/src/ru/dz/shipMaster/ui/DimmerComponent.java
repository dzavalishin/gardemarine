package ru.dz.shipMaster.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.logging.Logger;

import javax.swing.JComponent;

/**
 * This component is to be installed as glass pane to control brightness of the main frames. Like:<br>
 * <br><code>
 * 		frame.setGlassPane(dimmer);<br>
 * 		dimmer.setVisible(true);
 * </code><br>
 * @author dz
 *
 */
@SuppressWarnings("serial")
public class DimmerComponent extends JComponent {
    private static final Logger log = Logger.getLogger(DimmerComponent.class.getName()); 


    // -----------------------------------------------------------------------
	// Initialize
	// -----------------------------------------------------------------------

	/**
	 * Default constructor.
	 */
	public DimmerComponent()
	{
		width = getSize().width;
		height = getSize().height;

		setOpaque(false);

		
		enableEvents(java.awt.AWTEvent.COMPONENT_EVENT_MASK );
		addComponentListener( new ComponentListener() {
			public void componentHidden(ComponentEvent e) {/*ignore*/}
			public void componentShown(ComponentEvent e) {/*ignore*/}
			public void componentResized(ComponentEvent e) 
			{ 
				width = getSize().width;
				height = getSize().height;
				
				//synchronized (this) { backgroungImage = null; }
				repaint();
			}
			public void componentMoved(ComponentEvent e) {/*ignore*/}
		} );
	}

	// -----------------------------------------------------------------------
	// Sizing
	// -----------------------------------------------------------------------
	/*
	protected Dimension myMinimumSize = new Dimension(1280,1024);
	protected Dimension myPreferredSize = new Dimension(1280,1024);
	/* *
	 * General meter wants to be no smaller than (100,100)
	 * /
	@Override
	public Dimension minimumSize() { return myMinimumSize; }

	/* *
	 * General meters prefer to be (200,200)
	 * /
	public Dimension preferredSize() {		return myPreferredSize;	}
	*/
	protected int width = getSize().width;
	protected int height = getSize().height;


	private static double dimLevel = 0;
	private static Color dimColor = new Color( 0x00000000, true );

	/**
	 * Get dim level.
	 * @return Level from 0 to 1.
	 */
	public static double getDimLevel() {		return dimLevel;	}

	/**
	 * Set dim level.
	 * @param inDimLevel Level: 0 means screen brightness is maximal. 1 is nothing visible.
	 */
	public static void setDimLevel(double inDimLevel) {
		if( inDimLevel > 1 ) { inDimLevel = 1; log.fine("Dim level > 1"); }
		if( inDimLevel < 0 ) { inDimLevel = 0; log.fine("Dim level < 0"); }
		dimLevel = inDimLevel;
		
		byte dim = (byte)(0xFF&((int)(inDimLevel * 255)));
		dimColor = new Color( /*0x00000000 | */ (dim << 24), true );
		
		/*javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() { repaint(50); }
		});*/
	}
	

	// -----------------------------------------------------------------------
	// Main rendering logic
	// -----------------------------------------------------------------------

	
	private void drawShader(Graphics2D g )
	{		
		g.setColor( dimColor ); 
		g.fillRect(0, 0, width, height );		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;

		super.paintComponent(g);
		drawShader( g2d );
	}



	
	
}
