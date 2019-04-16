package ru.dz.shipMaster.ui.meter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import ru.dz.shipMaster.ui.misc.AlarmRegion;

/**
 * Wrong (right to left) graph.
 * @author dz
 *
 */
@SuppressWarnings("serial")
public abstract class LeftGraph extends AbstractGraph {
	
	protected static final int 	BAR_GRAPH_WIDTH = 50;
	protected static final int	AUTOLABEL_STEP_PIXELS = 30;	
	static final int 			GRID_STEP_SECONDS = 10;
	//static final int 			GRID_STEP_SECONDS = 20;

	public static final int 	LABEL_FROM_SIDE_X_LEFT_DIST = 18;
	public static final int 	LABEL_FROM_SIDE_X_RIGHT_DIST = 18;

	
	
	protected boolean mouseTracking = false;
	protected boolean mouseClick = false;
	protected int mouseXPos = 0;
	protected int mouseYPos = 0;

	protected boolean dragOn = false;
	protected int dragXPos = 0;
	protected int dragYPos = 0;
	
	protected boolean moveWithMouse = false;
	protected boolean killToTheRightInUpdate = true;
	
	
	protected List <Tick> ticks = new LinkedList<Tick>();
	protected final Object ticksSema = new Object();
		
	@Override 
	protected void recalcMeterParameters() { }

	@Override 
	protected void processResize() {
		setGradient();
	}
	

	
	// --------------------------------------------------------------------------
	// tick
	// --------------------------------------------------------------------------
	
	
	public class Tick {
		private long		timeMills;
		protected double	value;
		TickLabel	        label = null;
		public boolean		labelActive = false;
		public boolean 		labelRequest = false;

		public long getTimeMills() {			return timeMills;		}
		public double getValue() {			return value;		}
		
		public Tick(double value) { this.value = value; timeMills = System.currentTimeMillis(); }
		public Tick(double value, long time) { this.value = value; timeMills = time; }
	}
	
	// --------------------------------------------------------------------------
	// auto-labelling feature
	// --------------------------------------------------------------------------

	protected class TickLabel
	{
		String	text = "";
		int		x = -1;
		int		y = -1;
		
		TickLabel(Tick t)
		{
			text = String.format("%.1f",t.getValue() );
		}
		
		public void drawMe(Graphics g, int fromx, int fromy ) {
			if(drawMarkers)
			{
				g.setColor(vis.labelBgColor);
				g.drawLine(x, y, fromx, fromy);
				drawHorLabel(g, text, x, y, true, TextAnchorX.Center, TextAnchorY.Center );
			}
			
		}

		//public boolean hasCoordinates() { return (x > 0) && (y > 0); }
		public void resetPos(int newX, int newY)
		{
			int rndStep = AUTOLABEL_STEP_PIXELS;
			
			rndStep *= (Math.random()-0.45)+1;
			
			if(x < 0) x = newX;
			if(Math.abs(x - newX) > rndStep) x = newX;

			if( x < LABEL_FROM_SIDE_X_LEFT_DIST ) x = LABEL_FROM_SIDE_X_LEFT_DIST; // not too close to left margin
			if( x+LABEL_FROM_SIDE_X_RIGHT_DIST >= width ) x = width - LABEL_FROM_SIDE_X_RIGHT_DIST;
			
			//int height = getSize().height;
			if( y < 0 )
			{
				if( newY > height/2 )
					y = height/4;
				else
					y = height*3/4;
				
				//y += (Math.random()-0.45)*height/10;
			}
		}
		
	}
	
	//int lastAutolabel
	
	protected void processAutoLabelling(List<Tick> ticks)
	{
		for(Tick t : ticks)
			t.labelActive = false;
		
		List<Tick> toLabel = electTicksToLabel(ticks);
		for(Tick t : toLabel)
		{
			t.labelActive = true;
			if( t.label == null )
			{
				t.label = new TickLabel(t);
				//activeLabels++;
			}
		}
	}

	private List<Tick> electTicksToLabel(List<Tick> ticks) {
		List<Tick> out = new LinkedList<Tick>();
		
		// we need it to be in reverse order so that newest are at the beginning
		List<Tick> reverse = new LinkedList<Tick>();
		for(Tick t : ticks)
			reverse.add(0, t);
		
		final int maxLabels = 20;
		doElectTicks( out, maxLabels, reverse );
		
		return out;
	}
	private static void doElectTicks(List<Tick> out, int maxLabels, List<Tick> in) {
		if(maxLabels <= 0 )
			return;
		
		double min = Double.MAX_VALUE;
		double max = 0;
		
		for(Tick t : in)
		{
			double v = t.getValue();
			min = Math.min(min, v);
			max = Math.max(max, v);
		}
		
		for(Tick t : in)
		{
			double v = t.getValue();
			if( v == min )
			{
				out.add(t);
				maxLabels--;
				min = -1; // no more
			}
			if( v == max )
			{
				out.add(t);
				maxLabels--;
				max = -1; // no more
			}
			
			if(maxLabels <= 0 )
				return;
		}
		
		for(Tick t : in)
		{
			if(t.labelRequest && maxLabels > 0)
			{
				maxLabels--;
				out.add(t);
				//t.labelActive = true;
			}
		}
		
		// here we have to process all the ranges between all the labels
		// we set here (excluding themselves)
	}

	
	// --------------------------------------------------------------------------
	// Painting
	// --------------------------------------------------------------------------

	public void paintDashComponent(Graphics2D g2d) {

	    drawGrid(g2d);
	    
	    if(histogramVisible)
	    {
	    	g2d.setColor(vis.histogramColor);
	    	ds.drawVerticallyGraph(g2d, 0, 0, BAR_GRAPH_WIDTH, height);
	    }
		
	    
	    int lastHeight = 0;
	    int lastX = -1;
	    int maxX = 0;
    	Tick mouseXTick = null;
	    synchronized(ticksSema) {

	    	if(killToTheRightInUpdate)
	    		killToTheRight(width+20);
	    	
	    	processAutoLabelling(ticks);
	    	
	    	boolean foundMouseXValue = false;
	    	for(Tick t : ticks)
	    	{
	    		double newXd = calcXPos( t.getTimeMills() );

	    		int newX = (int)newXd;
	    		int newHeight = height-calcHeight(t.getValue()); 

	    		if( lastX >= 0 )
	    		{
		    		g2d.setColor(vis.getClassIndicatorColor("LeftGraph"));
	    			g2d.drawLine(lastX, lastHeight, newX, newHeight);
	    		    
	    		    if(newX > maxX) maxX = newX;
	    		    
	    		    if(mouseTracking && !foundMouseXValue && newX <= mouseXPos)
	    		    {
	    		    	foundMouseXValue = true;
	    		    	mouseXTick = t;
	    		    }
	    		    
	    		}

	    		lastX = newX;
	    		lastHeight = newHeight;
	    	}

	    	for(Tick t : ticks)
	    	{
	    		int newX = (int)calcXPos( t.getTimeMills() );
	    		int newHeight = height-calcHeight(t.getValue()); 

	    		if( newX >= 0 )
	    		{	    		    
	    		    if(t.labelActive)
	    		    {
	    		    	t.label.resetPos(newX, newHeight);
	    		    	t.label.drawMe(g2d,newX, newHeight);
	    		    }
	    		    
	    		}

	    	}
	    
	    }

	    if(mouseTracking && mouseXTick != null)
	    {
	    	g2d.setColor(vis.bgColor);
	    	g2d.drawLine(mouseXPos, 0, mouseXPos, height);
	    	
	    	
	    	int labely = mouseYPos < height/2 ? (height*2/3) : (height/3) ;
	    	int labelx = mouseXPos;
	    	
	    	if( labelx < 12 ) labelx = 12;
	    	if( labelx > width-12 ) labelx = width-12;
	    	
	    	g2d.drawLine(
	    			(int)calcXPos( mouseXTick.getTimeMills() ), 
	    			height-calcHeight(mouseXTick.getValue()), 
	    			labelx, labely);
	  
			drawHorLabel(g2d, String.format("%.1f",mouseXTick.value), 
					labelx, labely, true, 
	    			TextAnchorX.Center, TextAnchorY.Center);
			
			if(mouseClick)
			{
				mouseClick = false;
				mouseXTick.labelRequest = true;
			}
			
			if( dragOn )
			{
				Color over = new Color( 0x28000000 | (0xFFFFFF & Color.GREEN.getRGB()) , true );
				
		    	g2d.setColor(over);
		    	int wid = dragXPos-mouseXPos;
		    	long millis = xDiffToMillis( wid );
		    	if( wid > 0 )
		    	{
		    		g2d.fillRect(mouseXPos, 0, wid, height);
			    	drawHorLabel(g2d, Long.toString(millis)+" msec", 
							labelx+6, 4, true, 
			    			TextAnchorX.Left, TextAnchorY.Top);
		    	}
		    	else
		    	{
		    		g2d.fillRect(dragXPos, 0, -wid, height);
			    	drawHorLabel(g2d, Long.toString(millis)+" msec", 
			    			dragXPos+6, 4, true, 
			    			TextAnchorX.Left, TextAnchorY.Top);
		    	}
			
		    	
			}
			
	    }
	    
	    if(drawMarkers) drawHorLabels(g2d);
	    drawComponentBorder(g2d);
	}

	
	private void drawGrid(Graphics2D g2d)
	{
		/*
		Calendar rightNow = Calendar.getInstance();
		rightNow.set(Calendar.MILLISECOND, 0);
		int sec = rightNow.get(Calendar.SECOND);
		sec = GRID_STEP_SECONDS * (sec / GRID_STEP_SECONDS);
		rightNow.set(Calendar.SECOND, sec);
		long gridBarTime = rightNow.getTimeInMillis();
		*/
		
		long sec = produceLeftMarginTime() / 1000;
		sec = GRID_STEP_SECONDS * (sec / GRID_STEP_SECONDS);
		long gridBarTime = sec * 1000; // 
			
		Color gridColor = new Color((0x00FFFFFF & vis.tickColor.getRGB()) | 0x55000000,true);
		g2d.setColor(gridColor);
		
		int xpos;
		
		int labelNo = 0;
		while( (xpos = (int)calcXPos( gridBarTime )) < width )
		{
			g2d.drawLine(xpos, 0, xpos, height);
			
			String tl = timeLabel(gridBarTime,labelNo == 0);
			if(0 == (labelNo % 2))
				g2d.drawString(tl, xpos+2, 14); // TO DO 14 is label height + some margin
			labelNo++;

			gridBarTime -= GRID_STEP_SECONDS * 1000L; // 10 seconds step
		}
		
		for( int ypos = 0; ypos < height; ypos += (height/4))
			g2d.drawLine(0, ypos, width, ypos);
			
	}

	DateFormat tickLabelFormat = new SimpleDateFormat("mm:ss", Locale.PRC);
	DateFormat tickLabelLongFormat = new SimpleDateFormat("hh:mm:ss", Locale.PRC);
	private String timeLabel(long gridBarTime, boolean firstLabel) {
		if(firstLabel)
			return tickLabelLongFormat.format(new Date(gridBarTime));
			
		return tickLabelFormat.format(new Date(gridBarTime));
	}
	
	public void paintDashComponentBackground(Graphics2D g2d) {
		g2d.setPaint(gradient);
		g2d.fillRect(0, 0, width, height);

		paintAlarmRegions(g2d);
	}
	
	
	
	@Override
	protected void paintAlarmRegion(Graphics2D g2d, AlarmRegion r) {
		if( r.isCritical() )
			g2d.setColor(new Color((0x00FFFFFF & vis.critColor.getRGB()) | ALARM_FILL_ALPHA,true));
		else
			g2d.setColor(new Color((0x00FFFFFF & vis.warnColor.getRGB()) | ALARM_FILL_ALPHA,true));
		
		int from = height - calcHeight(r.getFrom());
		int to = height - calcHeight(r.getTo());
		
		final int MIN = -1;
		
		if( from < MIN ) from = MIN;
		if( to < MIN ) to = MIN;
		
		if( from > height ) from = height;
		if( to > height ) to = height;
		
		//g2d.fillRect(0, Math.min(from, to), width, Math.abs(to-from));

		g2d.drawLine(0, from, width, from );
	    g2d.drawLine(0, to, width, to );

	}
	
	// --------------------------------------------------------------------------
	// List cleanup
	// --------------------------------------------------------------------------

	/**
	 * Removes from the list ticks that too far to the left (too new)
	 * from current time origin.
	 * NB! Must be called in synchronized(ticksSema).
	 */
	protected void killToTheLeft(int xpos) {
		while(ticks.size() > 1)
    	{
    		Tick tick = ticks.get(ticks.size()-1);
    		double tickx = calcXPos( tick.getTimeMills() );
    		if(tickx < xpos)
    		{
    			//logTime("remove left at",ticks.get(ticks.size()-1).getTimeMills());
    			ticks.remove(ticks.size()-1);
    		}
    		else
    			break;
    	}
	}

	/**
	 * Removes from the list ticks that too far to the right (too old) 
	 * from current time origin.
	 * NB! Must be called in synchronized(ticksSema).
	 */
	protected void killToTheRight(int xpos) {
		while(ticks.size() > 1)
    	{
    		Tick t = ticks.get(0);	    		
    		double tickx = calcXPos( t.getTimeMills() );
    		
    		if(tickx > xpos)
    		{
    			//logTime("remove right at",ticks.get(0).getTimeMills());
    			ticks.remove(0);    			
    		}
    		else 
    			break;
    	}
	}
	
	
	// --------------------------------------------------------------------------
	// Debug
	// --------------------------------------------------------------------------

	private static long startT = System.currentTimeMillis() - 60000;
	protected void logTime(String msg, long time)
	{
		//log.log(Level.SEVERE,msg+" "+time);
		System.out.println(msg+" "+(time-startT)+" @ "+calcXPos(time));
	}
	
	// --------------------------------------------------------------------------
	// Abstracts
	// --------------------------------------------------------------------------
	
	/**
	 * This method must return time which left (latest) margin
	 * of display corresponds to.
	 */
	protected abstract long produceLeftMarginTime();
	
	/**
	 * Return x position on graph that corresponds to given time.
	 * @param timeMillis Time to calc pos for.
	 * @return x position in pixels.
	 */
	protected double calcXPos( long timeMillis )
	{
		return (produceLeftMarginTime()-timeMillis)*displaySpeed/1000;
	}
	/**
	 * Return height (not y position!) on the graph corresponding to a given value.
	 * @param val value
	 * @return height, pixels
	 */
	protected int calcHeight(double val)
	{
		return (int) (((val-minimum)/(maximum-minimum)) * height); 
	}
	

	protected long xDiffToMillis( int xdiff )
	{
		return (long)(xdiff*1000L/displaySpeed);
	}

	/*protected long xPosToTime( int xpos )
	{
		return produceLeftMarginTime()-(long)(xpos*1000L/displaySpeed);
	}*/
	
	protected abstract void setMouseDragTime(long timediff);

	protected abstract void noteMouseDragStart();
	protected abstract void noteMouseDragEnd();
	
	// --------------------------------------------------------------------------
	// Tracking
	// --------------------------------------------------------------------------
	
	
	{
		Dimension myPreferredSize = new Dimension();
		myPreferredSize .width = 600;
		myPreferredSize.height = 100;
		setPreferredSize(myPreferredSize);
		
		setGradient();
		
		addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {
				if(mouseTracking)
				{
					dragXPos  = e.getPoint().x;
					dragYPos  = e.getPoint().y;
					if(moveWithMouse)
					{
				    	int wid = dragXPos-mouseXPos;
				    	long millis = xDiffToMillis( wid );
				    	setMouseDragTime(millis);
					}
					else
					{
						dragOn = true;
					}
					repaint();
				}
			}

			public void mouseMoved(MouseEvent e) {
				if(mouseTracking)
				{
					mouseXPos  = e.getPoint().x;
					mouseYPos  = e.getPoint().y;
					repaint();
				}
			}			
		});
		
		addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) { mouseClick = true; noteMouseDragStart(); }
			public void mouseEntered(MouseEvent e) { mouseTracking = true;  }
			public void mouseExited(MouseEvent e) { mouseTracking = false; repaint(); }
			public void mousePressed(MouseEvent e) { }
			public void mouseReleased(MouseEvent e) 
			{ 
				noteMouseDragEnd();
				dragOn = false; 
				mouseXPos  = e.getPoint().x;
				mouseYPos  = e.getPoint().y;
				repaint();
			}		
		});
		
		if(drawMarkers)
			enableEvents(java.awt.AWTEvent.MOUSE_EVENT_MASK | java.awt.AWTEvent.MOUSE_MOTION_EVENT_MASK );
	}
	
}
