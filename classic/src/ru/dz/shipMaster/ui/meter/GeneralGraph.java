package ru.dz.shipMaster.ui.meter;

import java.util.logging.Logger;

//public class GeneralGraph extends LeftGraph {
public class GeneralGraph extends RightGraph {
    @SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(GeneralGraph.class.getName()); 

	/**
	 * Eclipse id. 
	 */
	private static final long serialVersionUID = -7739444534475905350L;

	public GeneralGraph() {}
	
	@Override
	public void setCurrent(double newCurr) {
		super.setCurrent(newCurr);
		if(ticks != null)
		{
			synchronized(ticksSema) { ticks.add(new Tick(newCurr)); }
			repaint(100);
		}
	}


	
	// --------------------------------------------------------------------------
	// Abstracts
	// --------------------------------------------------------------------------
	
	private long creationTime = System.currentTimeMillis();
	private long diff = 0;
	private long dragStartTime = creationTime;
	
	
	final static int moveStepMsec = GRID_STEP_SECONDS * 2 * 1000;
	/**
	 * This method must return time which left (latest) margin
	 * of display corresponds to.
	 */
	@Override
	protected long produceLeftMarginTime()
	{
		if(moveWithMouse)
			return dragStartTime+diff;
		
		
		long t = System.currentTimeMillis()/moveStepMsec;
		
		return (t+1)*moveStepMsec;
	}

	@Override
	protected void setMouseDragTime(long timediff)
	{
		diff = timediff;
	}
	
	@Override
	protected void noteMouseDragStart() {
		//dragStartTime += diff;
		//diff = 0;
	}

	@Override
	protected void noteMouseDragEnd() {
		dragStartTime += diff;
		diff = 0;
	}
	
}
