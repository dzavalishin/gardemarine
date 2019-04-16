package ru.dz.shipMaster.ui.meter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.logging.Logger;

public class ThermometerVerticalMeter extends GeneralMeter {
    @SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(ThermometerVerticalMeter.class.getName()); 

	
	/**
	 * Eclipse id.
	 */
	private static final long serialVersionUID = 1676034086757290778L;

	//private static final Color LIQUID_COLOR = VisualSettings.PALE_RED_COLOR; //new Color(0xBB0000);

	public ThermometerVerticalMeter() { }

	{
		setPreferredSize( new Dimension( 20, getHeight() ) );
		//tickColor = VisualSettings.THERMOMETER_VERTICAL_METER_TICK_COLOR;
	}

	@Override
	protected void recalcMeterParameters() {
		// ignore
	}
	
	@Override 
	protected void processResize() {
//		 ignore
	}
	
	@Override
	public void paintDashComponent(Graphics2D g2d) {
	    //int barHeight = (int) (((float)currVal/(float)(maxVal-minVal)) * getSize().height);

	    int percent = (int) (((float)currVal/(float)(maximum-minimum)) * 100.0);
	    
    	//final int widthSixths = width/6;
    	final int widthThirds = width/3;
    	final int heightTenths = height/10;
    	final int heightFifths = height/5;
	    
	       // compute rects
        Rectangle gaugeRect = new Rectangle( widthThirds, 0,
            width-(2*widthThirds), height-heightTenths );

        Rectangle bulbRect = new Rectangle(1, height-heightFifths,
            width-2, heightFifths-2);

        Rectangle emptyRect = new Rectangle( gaugeRect.x, gaugeRect.y,
            gaugeRect.width, (gaugeRect.height*(100-percent)/100));

        Rectangle filledRect = new Rectangle( gaugeRect.x,
            emptyRect.y+emptyRect.height, gaugeRect.width+1,
            gaugeRect.height-emptyRect.height );

        //Graphics2D g2d = (Graphics2D)g;
        //g2d.setRenderingHints(getHints());
        
        Color indicatorColor = vis.getIndicatorColor();
        
        // draw gauge
        g2d.setColor(indicatorColor);
        g2d.fillOval(bulbRect.x, bulbRect.y, bulbRect.width, bulbRect.height);

        g2d.setColor( vis.getClassScaleColor( this.getClass().getSimpleName() ) );
        g2d.fillRect(emptyRect.x, emptyRect.y, emptyRect.width, emptyRect.height);
        
        g2d.setColor(indicatorColor);
        g2d.drawRect(emptyRect.x, emptyRect.y, emptyRect.width, filledRect.y-emptyRect.y);
        
        g2d.setColor(indicatorColor);
        g2d.fillRect(filledRect.x, filledRect.y, filledRect.width, filledRect.height );
        
        // draw ticks
        g2d.setColor(vis.tickColor);
        for( int i = 1; i < 10; i ++ )
        {
            g2d.fillOval(gaugeRect.x, ((gaugeRect.height*(10*i))/100), 
            		gaugeRect.width/3, 3);
        }

		drawComponentBorder(g2d); 
 	    
	}
	
	@Override
	public void paintDashComponentBackground(Graphics2D g2d) {
		g2d.setColor( vis.getClassScaleColor( this.getClass().getSimpleName() ));
		g2d.fillRect(0, 0, width, height);
	}

}
