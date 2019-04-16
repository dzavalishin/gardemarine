package ru.dz.shipMaster.ui.meter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.font.FontRenderContext;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.dz.shipMaster.DashBoard;
import ru.dz.shipMaster.ui.component.DashImage;
import ru.dz.shipMaster.ui.logger.GeneralLogWindow.Item;
import ru.dz.shipMaster.ui.misc.VisualHelpers;
import ru.dz.shipMaster.ui.render.RoundGradientPaint;

public class GeneralPictogram extends GeneralMeter 
	implements DashImage, IInstrumentWithFont, IInstrumentWithMessages {
	private static final Logger log = Logger.getLogger(GeneralPictogram.class.getName()); 


	/**
	 * Eclipse id. 
	 */
	private static final long serialVersionUID = 3479741817528597008L;

	protected Image iPictogram;
	private String offMessage;
	private String onMessage;
	private boolean drawMessage = false;
	protected Severity severity;

	private Item message;
	protected boolean isOn = false;

	protected boolean isRaw = false;


	private String pictogramFileName;


	private DashBoard dashBoard;


	public GeneralPictogram( String pictogramIconName,
			String offMessage, String onMessage, Severity severity )
	{ 
		//super(db);
		this.offMessage = offMessage;
		this.onMessage = onMessage;
		this.severity = severity; 
		//iPictogram = VisualSettings.loadImage(pictogramIconName);
		setPictogramFileName(pictogramIconName);
		//new ImageIcon("resources/"+pictogramIconName); // TO DO jar

		//message = db.getMessageWindow().getItem(offMessage);
	}

	/*public GeneralPictogram(DashBoard db)
	{
		super(db);
		severity = Severity.Info;
	}*/

	public GeneralPictogram()
	{
		severity = Severity.Info;
	}

	public void setPictogramFileName(String pictogramIconName)
	{
		if(pictogramIconName != null )
		{
			this.pictogramFileName = pictogramIconName;
			iPictogram = VisualHelpers.loadImage(pictogramIconName); 
			processResize();
		}
	}

	/* (non-Javadoc)
	 * @see ru.dz.shipMaster.ui.meter.GeneralMeter#setDashBoard(ru.dz.shipMaster.DashBoard)
	 */
	//@Override
	public void setDashBoard(DashBoard dashBoard) {
		this.dashBoard = dashBoard;
		String msg = isOn ? onMessage : offMessage;
		message = dashBoard.getMessageWindow().getItem(msg == null ? "-" : msg);
	}

	@Override
	protected void finalize() throws Throwable {
		dashBoard.getMessageWindow().removeItem(message);
		super.finalize();
	}

	{
		//setLogWarnAndCritical(false);
	}



	@Override
	protected void recalcMeterParameters() {
		if(isPreferredSizeSet())
			return;
		
		Dimension myPreferredSize = new Dimension();
		//myPreferredSize.height = 20;
		//myPreferredSize.width  = 20;

		myPreferredSize.height = 40;
		myPreferredSize.width  = 40;
		
		if(iPictogram != null)
		{
			myPreferredSize.height = Math.max( iPictogram.getHeight(null), 40 );
			myPreferredSize.width = Math.max( iPictogram.getWidth(null), 40 );
		}
		
		setPreferredSize(myPreferredSize);
	}

	private boolean prevIsOn = true;

	protected int picX = 0;
	protected int picY = 0;
	
	@Override
	public void setCurrent(double newCurr) {
		determineIsOn(newCurr);

		super.setCurrent(newCurr);


		if(isOn != prevIsOn)
		{
			prevIsOn = isOn;
			if( message != null )
			{
				message.setText(isOn ? onMessage : offMessage);
				message.setCritical( isOn && severity.equals( Severity.Critical ) );
			}
			resetDashComponentBackgroundImage();
		}
	}

	protected void determineIsOn(double newCurr) {
		isOn = newCurr > ((maximum-minimum)/2);
		//System.out.println("GeneralPictogram.determineIsOn() val "+newCurr+", "+isOn);
	}

	@Override 
	protected void processResize() { 
		if( iPictogram != null )
		{
			picX = ((getSize().width)/2) - iPictogram.getWidth(null)/2;
			picY = ((getSize().height)/2) - iPictogram.getHeight(null)/2;
		}
	}

	public void paintDashComponent(Graphics2D g2d) {		
	}

	private Font myFont; // = getFont().deriveFont(24); 


	private int fontSize = 18;

	private void resetFont()
	{
		if(myFont == null )
		{
			try {			
				myFont = new Font("Verdana", Font.PLAIN, fontSize);				
			} catch(Throwable e)
			{
				log.log(Level.SEVERE, "Can't create font",e);
				myFont = new Font(Font.SANS_SERIF, Font.BOLD, fontSize);
			}
		}
	}
	
	{
		resetFont();
	}
	
	@Override
	public void paintDashComponentBackground(Graphics2D g2d) {

		double gw = width*3/4;
		if( gw < 0.1 ) gw = 1; // or else it will die on zero radius

		if(!isRaw)
		{
			Color litColor = 
				severity.equals( Severity.Critical ) ? vis.pictogramCriticalColor :
					severity.equals( Severity.Warning )  ? vis.pictogramWarningColor :
						severity.equals( Severity.Info )     ? vis.pictogramNormalColor :
							vis.pictogramTrivialColor;

			Paint gradient = 
				new RoundGradientPaint(width/4, height/4, 
						isOn ? litColor : vis.scaleGlimpseColor, 
								new Point2D.Double(gw, height*4/4), vis.getClassScaleColor( this.getClass().getSimpleName() ) );

			g2d.setPaint(gradient);
			g2d.fillRect(0, 0, width, height);
		}
		if(iPictogram != null)
			g2d.drawImage(iPictogram, picX, picY, null);

		if(!isRaw)
			drawComponentBorder(g2d);
		
		if(drawMessage)
		{
			//g2d.setColor(vis.digitalMeterColor);
			g2d.setColor(Color.BLACK);
			g2d.setFont(myFont);
			
			String txt = (isOn||offMessage == null||offMessage.length() == 0) ? onMessage : offMessage;
			FontRenderContext frc = g2d.getFontRenderContext();
			Rectangle2D stringBounds = myFont.getStringBounds(txt, frc);
			
			g2d.drawString(txt, 
					(int)(width-stringBounds.getWidth())/2, 
					(int)(height/2-(stringBounds.getCenterY())));
		}
	}

	public enum Severity { Trivial, Info, Warning, Critical }

	/**
	 * @return the offMessage
	 */
	public String getOffMessage() {		return offMessage;	}

	/**
	 * @param offMessage the offMessage to set
	 */
	public void setOffMessage(String offMessage) {		this.offMessage = offMessage;	}

	/**
	 * @return the onMessage
	 */
	public String getOnMessage() {		return onMessage;	}

	/**
	 * @param onMessage the onMessage to set
	 */
	public void setOnMessage(String onMessage) {		this.onMessage = onMessage;	}

	/**
	 * @return the severity
	 */
	public Severity getSeverity() {		return severity;	}

	/**
	 * @param severity the severity to set
	 */
	public void setSeverity(Severity severity) {		this.severity = severity;	}

	public String getPictogramFileName() {
		return pictogramFileName;
	}

	public boolean isDrawMessage() {		return drawMessage;	}
	public void setDrawMessage(boolean drawMessage) {		this.drawMessage = drawMessage;	}

	@Override
	public void setFontSize(int size) {
		if( size > 0 )
		{
			this.fontSize = size;
			myFont = null;
			resetFont();
		}
	}

	@Override
	public int getFontSize() {
		return fontSize;
	};

}
