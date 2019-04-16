package ru.dz.shipMaster.ui.control;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;

import ru.dz.shipMaster.ui.VisualSettings;
import ru.dz.shipMaster.ui.misc.VisualHelpers;
import ru.dz.shipMaster.ui.render.ColorifyFilter;

@SuppressWarnings("serial")
public class DashImageButton extends DashButton implements DashOffImage {

	public DashImageButton() {
		setBorder( null );
		
		Insets insets = getInsets();
		
		height = getSize().height - (insets.top+insets.bottom);
		width = getSize().width - (insets.left+insets.right);
	}
	
	//static ImageFilter filter = new ColorifyFilter(VisualSettings.global.digitalMeterColor);
	//static ImageFilter filter = new ColorifyFilter(new Color(8*3, 42*3, 41*3));
	static ImageFilter filter = new ColorifyFilter(new Color(8*3, 42*3, 41*3));
	private Image iPalePictogram;
	private String pictogramOffFileName;

	
	@Override
	public void setPictogramFileName(String pictogramIconName) {
		super.setPictogramFileName(pictogramIconName);
		
		if(iPictogram != null && iPalePictogram == null )
		{
			FilteredImageSource filteredSrc = new FilteredImageSource(iPictogram.getSource(), filter);
			iPalePictogram = Toolkit.getDefaultToolkit().createImage(filteredSrc);
		}
	}

	@Override
	public void setOffPictogramFileName(String pictogramIconName) {
		if(pictogramIconName != null )
		{
			this.pictogramOffFileName = pictogramIconName;
			iPalePictogram = VisualHelpers.loadImage(pictogramIconName); 
		}
	}
	
	
	public String getOffPictogramFileName() { return pictogramOffFileName; }
	
	
	/* (non-Javadoc)
	 * @see ru.dz.shipMaster.ui.DashComponent#paintDashComponentBackground(java.awt.Graphics2D)
	 */
	@Override
	protected void paintDashComponentBackground(Graphics2D g2d) {
		g2d.setColor(VisualSettings.global.bgColor);
		g2d.fillRect(0, 0, width, height);

		/*
		//Border
		BasicGraphicsUtils.drawBezel(g2d, 0, 0, width, height, 
				   pressed, defaultButton , 
				   VisualSettings.global.BUTTON_SHADOW_COLOR,//shadow,
				   VisualSettings.global.BUTTON_DARK_SHADOW_COLOR, //darkShadow, 
				   VisualSettings.global.BUTTON_HIGHLIGHT_COLOR, //highlight, 
				   VisualSettings.global.BUTTON_LIGHT_HIGHLIGHT_COLOR //lightHighlight
                                );
		*/
		//g2d.setClip(4, 4, width-8, height-8);
		
		if(iPictogram != null)
		{
			g2d.drawImage( pressed ? iPictogram : iPalePictogram, width/2-iPictogram.getWidth()/2, 0, null);
			//g2d.drawImage(iPictogram, 0, 0, null);
		}
		else
		{
			g2d.setColor( pressed ? Color.RED : Color.CYAN );
			g2d.drawRect( 0, 0, width, height );
		}
		
		/*
		String str = "Button";
		Rectangle2D stringBounds = getFont().getStringBounds(str, g2d.getFontRenderContext());

		int sx = width/2-(int)(stringBounds.getWidth()/2);
		int sy = height*3/4;
		
		g2d.setColor(VisualSettings.global.bgColor);
		g2d.fillRect(
				sx-3, sy-(int)stringBounds.getHeight()-2, 
				(int)stringBounds.getWidth()+8, (int)stringBounds.getHeight()+6);

		g2d.setColor(VisualSettings.global.labelColor);
		g2d.drawString(str, sx, sy);
		*/
	}
	
	
}
