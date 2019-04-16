package ru.dz.shipMaster.ui.component;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import ru.dz.shipMaster.ui.DashComponent;
import ru.dz.shipMaster.ui.misc.VisualHelpers;

public class GeneralImage extends DashComponent implements DashImage {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7013778515324197675L;
	private BufferedImage iPictogram;
	private String pictogramFileName;

	/** This one is for XML serializer. */
	public GeneralImage() {}


	@Override
	protected void paintDashComponent(Graphics2D g2d) {
	}

	@Override
	protected void paintDashComponentBackground(Graphics2D g2d) {
		if(iPictogram != null)
			g2d.drawImage(iPictogram, 0, 0, null);
	}

	@Override
	protected void processResize() {
	}

	
	
	/* (non-Javadoc)
	 * @see ru.dz.shipMaster.ui.component.DashImage#setPictogramFileName(java.lang.String)
	 */
	public void setPictogramFileName(String pictogramIconName)
	{
		if( pictogramIconName != null )
		{
			this.pictogramFileName = pictogramIconName;
			iPictogram = VisualHelpers.loadImage(pictogramIconName); 
			if(iPictogram != null)
			{
				Dimension myPreferredSize = new Dimension();
				myPreferredSize.height = iPictogram.getHeight();
				myPreferredSize.width = iPictogram.getWidth();
				setPreferredSize(myPreferredSize);
				setMinimumSize(myPreferredSize);
				
				Container parent = getParent();
				if(parent != null)
				{
					parent.validate();
					parent.repaint(100);
				}
			}
		}
	}

	/**
	 * @return the pictogramFileName
	 */
	public String getPictogramFileName() {
		return pictogramFileName;
	}
	
}
