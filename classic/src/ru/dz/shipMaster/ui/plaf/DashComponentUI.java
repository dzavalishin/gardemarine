package ru.dz.shipMaster.ui.plaf;

import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

import ru.dz.shipMaster.ui.DashComponent;

public abstract class DashComponentUI extends ComponentUI {

	public DashComponentUI() {
	}

	public abstract void paintDashComponent(Graphics2D g2d, DashComponent dc );
	public abstract void paintDashComponentBackground(Graphics2D g2d, DashComponent dc );
	
	
	public void installUI(JComponent c) {
		super.installUI(c);
	}
	
	protected int width;
	protected int height;
	
	/** 
	 * Called by DashComponent on resize.
	 * @param dc Resized component.
	 */
	public void processResize(DashComponent dc) 
	{
		//super.processResize(dc);
		width = dc.getWidth();
		height = dc.getHeight();
	}	
}
