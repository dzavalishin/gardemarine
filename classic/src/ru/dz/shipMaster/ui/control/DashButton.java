/**
 * 
 */
package ru.dz.shipMaster.ui.control;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.plaf.basic.BasicGraphicsUtils;

import ru.dz.shipMaster.DashBoard;
import ru.dz.shipMaster.config.items.CliButtonGroup;
import ru.dz.shipMaster.ui.VisualSettings;
import ru.dz.shipMaster.ui.component.DashImage;
import ru.dz.shipMaster.ui.misc.VisualHelpers;

/**
 * @author dz
 *
 */
public class DashButton extends GeneralControl implements DashImage {

	/**
	 * Generated uid.
	 */
	private static final long serialVersionUID = -5668254574208960615L;
	
	protected boolean pressed = false;
	protected boolean defaultButton = false;
	protected boolean isToggle = false;
	//private boolean isToggle = true;
	private String pictogramFileName;
	protected BufferedImage iPictogram;
	private MouseListener myListener;
	protected String buttonText = "Button";
	protected CliButtonGroup buttonGroup = null;

	public DashButton(DashBoard db) { setup(); }
	public DashButton() { setup(); }

	private void setup() {
		setMinimumSize(new Dimension(40,24));		
		
		addMouseListener(myListener = new MouseListener() {

			public void mouseClicked(MouseEvent e) {
				//if(isToggle)					setPressed(!pressed);
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
				if(isToggle)					
					setPressedInternal(!pressed);
				else
					setPressedInternal(true);				
			}

			public void mouseReleased(MouseEvent e) {
				if(!isToggle)
					setPressedInternal(false);
			}});
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		removeMouseListener(myListener);
		if(enabled) 
			addMouseListener(myListener);
		
		super.setEnabled(enabled);
	}
	
	/**
	 * From outside. Does not translate to button group.
	 * @param pressed Draw button as pressed.
	 */
	public void setPressedInternal(boolean pressed)
	{
		doSetPressed(pressed, true);
	}

	/**
	 * From inside. Translates to button group too.
	 * @param pressed Draw button as pressed.
	 */
	public void setPressedExternal(boolean pressed)
	{
		doSetPressed(pressed, false);
	}
	
	/**
	 * Changes the visual representation of the button. Translates state out.
	 * @param pressed Draw button as pressed.
	 */
	public void doSetPressed(boolean pressed, boolean toButtonGroup)
	{
		if( toButtonGroup && buttonGroup != null)
			buttonGroup.propagate(this); // Turn off other buttons in group
		
		sendValue(pressed ? 100 : 0);
		this.pressed = pressed;
		
		/* 
		 * TO DO later we will redo it so that button will get its
		 * visual status by separate channel so that several buttons
		 * driving the same output will reflect output's state all
		 * together.
		 */
		resetDashComponentBackgroundImage();
		repaint(100);
	}
	
	
	/* (non-Javadoc)
	 * @see ru.dz.shipMaster.ui.DashComponent#paintDashComponent(java.awt.Graphics2D)
	 */
	@Override
	protected void paintDashComponent(Graphics2D g2d) {
	}

	/* (non-Javadoc)
	 * @see ru.dz.shipMaster.ui.DashComponent#paintDashComponentBackground(java.awt.Graphics2D)
	 */
	@SuppressWarnings("static-access")
	@Override
	protected void paintDashComponentBackground(Graphics2D g2d) {
		g2d.setColor(VisualSettings.global.bgColor);
		g2d.fillRect(0, 0, width, height);

		//Border
		BasicGraphicsUtils.drawBezel(g2d, 0, 0, width, height, 
				   pressed, defaultButton , 
				   VisualSettings.global.BUTTON_SHADOW_COLOR,//shadow,
				   VisualSettings.global.BUTTON_DARK_SHADOW_COLOR, //darkShadow, 
				   VisualSettings.global.BUTTON_HIGHLIGHT_COLOR, //highlight, 
				   VisualSettings.global.BUTTON_LIGHT_HIGHLIGHT_COLOR //lightHighlight
                                );

		g2d.setClip(4, 4, width-8, height-8);
		
		if(iPictogram != null)
		{
			g2d.drawImage(iPictogram, width/2-iPictogram.getWidth()/2, 4, null);
			//g2d.drawImage(iPictogram, 0, 0, null);
		}
		
		
		String str = buttonText;
		Rectangle2D stringBounds = getFont().getStringBounds(str, g2d.getFontRenderContext());

		int sx = width/2-(int)(stringBounds.getWidth()/2);
		int sy = height*3/4;
		
		g2d.setColor(VisualSettings.global.bgColor);
		g2d.fillRect(
				sx-3, sy-(int)stringBounds.getHeight()-2, 
				(int)stringBounds.getWidth()+8, (int)stringBounds.getHeight()+6);

		g2d.setColor(VisualSettings.global.labelColor);
		g2d.drawString(str, sx, sy);
		
	}

	/* (non-Javadoc)
	 * @see ru.dz.shipMaster.ui.DashComponent#processResize()
	 */
	@Override
	protected void processResize() {
		

	}

	/* (non-Javadoc)
	 * @see ru.dz.shipMaster.ui.component.DashImage#setPictogramFileName(java.lang.String)
	 */
	public void setPictogramFileName(String pictogramIconName)
	{
		if(pictogramIconName != null )
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
				resetDashComponentBackgroundImage();
			}
		}
	}

	/**
	 * @return the pictogramFileName
	 */
	public String getPictogramFileName() {		return pictogramFileName;	}

	public String getButtonText() {		return buttonText;	}
	public void setButtonText(String buttonText) {		this.buttonText = buttonText;	}
	
	public void setButtonGroup(CliButtonGroup g) {
		if(buttonGroup != null)
			buttonGroup.removeButton(this);
		buttonGroup = g;
		if( g != null )
			buttonGroup.addButton(this);
	}


}
