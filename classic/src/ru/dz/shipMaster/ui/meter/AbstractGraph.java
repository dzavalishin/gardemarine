package ru.dz.shipMaster.ui.meter;

import java.awt.Paint;
import java.awt.geom.Point2D;

import ru.dz.shipMaster.ui.render.RoundGradientPaint;

@SuppressWarnings("serial")
public abstract class AbstractGraph extends GeneralMeter implements IGraph {

	
	protected boolean drawMarkers = true;
	protected double displaySpeed = 4.0; // pixels/sec 


	
	// --------------------------------------------------------------------------
	// Constr
	// --------------------------------------------------------------------------
	
	
	public AbstractGraph() {}


	/* (non-Javadoc)
	 * @see ru.dz.shipMaster.ui.DashComponent#canExtendWidth()
	 */
	@Override
	public boolean canExtendWidth() {
		return true;
	}
	
	
	protected Paint gradient;
	protected void setGradient() {
		int rad = Math.max(200, width/2);
		gradient = //new GradientPaint( 				0, height*1/4, scaleGlimpseColor, 				0, height*2/4, scaleColor, 				false); // true means to repeat pattern
			//new RoundGradientPaint(height/3, height/3, scaleGlimpseColor, new Point2D.Double(0, 125), scaleColor);
			new RoundGradientPaint(height/3, height/3, vis.scaleGlimpseColor, new Point2D.Double(0, rad), vis.getClassScaleColor( this.getClass().getSimpleName() ) );
	}
	

	
	// --------------------------------------------------------------------------
	// Getters/setters
	// --------------------------------------------------------------------------
	
	/* (non-Javadoc)
	 * @see ru.dz.shipMaster.ui.meter.IGraph#isDrawMarkers()
	 */
	public boolean isDrawMarkers() {		return drawMarkers;	}
	/* (non-Javadoc)
	 * @see ru.dz.shipMaster.ui.meter.IGraph#setDrawMarkers(boolean)
	 */
	public void setDrawMarkers(boolean drawMarkers) {		this.drawMarkers = drawMarkers;	}
	
	/* (non-Javadoc)
	 * @see ru.dz.shipMaster.ui.meter.IGraph#getDisplaySpeed()
	 */
	public double getDisplaySpeed() {		return displaySpeed;	}
	/* (non-Javadoc)
	 * @see ru.dz.shipMaster.ui.meter.IGraph#setDisplaySpeed(double)
	 */
	public void setDisplaySpeed(double displaySpeed) {		this.displaySpeed = displaySpeed;	}
	
	
}
