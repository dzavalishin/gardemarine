package ru.dz.shipMaster.ui.config.item;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import ru.dz.shipMaster.ui.config.ConfigItemEditor;

/**
 * Configuration item editor that keeps a sibling frame aside which can be used to
 * display item's state.
 * @author dz
 *
 */
public abstract class SideFrameConfigItemEditor 
	extends ConfigItemEditor  implements AncestorListener{

	// -------------------------------------------------------------
	//        SIDE FRAME
	// -------------------------------------------------------------

	private final Dimension minSize;

	protected SideFrameConfigItemEditor(Dimension minSize)
	{
		this.minSize = minSize;
		JRootPane rootPane = panel.getRootPane();
		rootPane.addAncestorListener(this);		
	}
	
	protected JFrame sideFrame = new JFrame("?");
	private JPanel noDrvPanel = new JPanel();
	private Point contentPaneLocation;
	//private SelectMarker selectMarker;
	
	void setupSideFrame(String title)
	{
		//sideFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		sideFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		//sideFrame.setMinimumSize(new Dimension(240,200));
		sideFrame.setMinimumSize(minSize);
		sideFrame.setTitle(title);
		sideFrame.pack();
		sideFrame.setVisible(true);
		setSFPosition();
	
		noDrvPanel.add(new JLabel("Empty"));
		
		setSFPanel(null); // so that default panel is set up
		
		// Now for the mouse tracking
		
		sideFrame.addMouseMotionListener(mmListener);
		sideFrame.addMouseListener(mListener);
				
		// hack - doesn't work too
		contentPaneLocation = sideFrame.getContentPane().getLocation();
		
		
		// doesn't work :(
		//sideFrame.getContentPane().addMouseMotionListener(mmListener);
		//sideFrame.getContentPane().addMouseListener(mListener);
		
		/*
		//JPanel glassPane = new JPanel();
		//sideFrame.setGlassPane(glassPane);
		JPanel glassPane = (JPanel)sideFrame.getGlassPane();
		
		selectMarker = new SelectMarker();
		
		selectMarker.x = 10;
		selectMarker.y = 10;
		selectMarker.width = 10;
		selectMarker.height = 10;
		
		glassPane.add(selectMarker);
		
		glassPane.setVisible(true);
		*/
	}

	private MouseListener mListener;
	{
		mListener = new MouseListener() {

			public void mouseClicked(MouseEvent e) {/*ignore*/}
			public void mouseEntered(MouseEvent e) {/*ignore*/}
			public void mouseExited(MouseEvent e)  {/*ignore*/}

			public void mousePressed(MouseEvent e) {
				// TODO HACK!!
				Point location = sideFrame.getContentPane().getParent().getParent().getLocation();
				//System.out.println("SideFrameConfigItemEditor.{...}.new MouseListener() {...}.mousePressed() loc="+location);
				
				Point p = new Point((int)(e.getX()-location.getX()), (int)(e.getY()-location.getY()) );
				
				Component c = sideFrame.getContentPane().getComponentAt(p);
				//Component c = sideFrame.getComponentAt(e.getPoint()); // doesnt work				
				setCurrenSFChild(c, e.getPoint());
			}

			public void mouseReleased(MouseEvent e) {/*ignore*/}
			};
	}
	
	private boolean dragDropEnabled;
	private MouseMotionListener mmListener;
	private Component currentSFChild;
	private Point currentSFChildStartDragCursorPos;
	private Point currentSFChildStartDragChildPos;
	private boolean snapToGrid = true;
	
	{
		mmListener = new MouseMotionListener() {

			public void mouseDragged(MouseEvent e) {
				//log.log(Level.SEVERE, "Drag: "+e);
				moveCurrentSFChildTo(e.getPoint());
			}

			public void mouseMoved(MouseEvent e) {
				//log.log(Level.SEVERE, "Move: "+e);
				
			}};
	}
	
	protected void setSideFrameMinSize(Dimension dimension) {
		sideFrame.setMinimumSize(dimension);		
	}
	
	protected void moveCurrentSFChildTo(Point point) {
		if(!dragDropEnabled) return;
		
		Point p = new Point(
				point.x-currentSFChildStartDragCursorPos.x+currentSFChildStartDragChildPos.x-contentPaneLocation.x,
				point.y-currentSFChildStartDragCursorPos.y+currentSFChildStartDragChildPos.y-contentPaneLocation.y
				);
		
		int gridStep = 4;
		
		if(snapToGrid )
		{
			p.x = ((int)( (p.x + gridStep/2) / gridStep )) * gridStep; 
			p.y = ((int)( (p.y + gridStep/2) / gridStep )) * gridStep; 
		}
		if(!checkLocation(currentSFChild,p))
			return;

		currentSFChild.setLocation(p);
	}

	/** 
	 * 
	 * Can be overridden in subclass to find out where the child was dragged.
	 *
	 * @return true if it is OK to move this component to this location. 
	 * False return forbids movement.
	 *  
	 *  Default implementation always returns true;
	 **/
	protected boolean checkLocation( Component child, Point p) {
		return true;
	}

	protected void setCurrenSFChild(Component c, Point cursorPos) {
		currentSFChild = c;
		currentSFChildStartDragCursorPos = cursorPos;
		currentSFChildStartDragChildPos = c.getLocation();
	}

	// TODO it must be something like AbstractDriverPanel
	protected void setSFPanel( JPanel p )
	{
		//log.severe("panel = "+p);
		if(p == null)
			p = noDrvPanel; // to replace with empty one
		
		sideFrame.setContentPane(p);
		p.setBorder(new EmptyBorder(4,4,4,4));
		//p.setBorder(new MatteBorder(4,4,4,4,Color.DARK_GRAY.));
		sideFrame.pack();
	}

	void setSFPosition()
	{
		Container topLevelAncestor = panel.getTopLevelAncestor();
		
		//JRootPane rootPane = panel.getRootPane();
		
		//Container contentPane = rootPane.getContentPane();
		
		if(true)
		{
			Point p = topLevelAncestor.getLocation();
			//log.severe(p.toString());
			p.x += topLevelAncestor.getWidth()+20;
			sideFrame.setLocation(p);
			sideFrame.setVisible(topLevelAncestor.isVisible());
		}
	}

	public void ancestorAdded(AncestorEvent event) { setSFPosition(); }
	public void ancestorMoved(AncestorEvent event) { setSFPosition(); }
	public void ancestorRemoved(AncestorEvent event) { setSFPosition(); }

	/**
	 * @return True if this frame currently lets user to drag children.
	 */
	public boolean isDragDropEnabled() {		return dragDropEnabled;	}
	/**
	 * Enable or disable move of this frame's children by mouse.
	 * @param dragDropEnabled True to enable mouse editing of children positions.
	 */
	public void setDragDropEnabled(boolean dragDropEnabled) {		this.dragDropEnabled = dragDropEnabled;	}

	public boolean isSnapToGrid() {
		return snapToGrid;
	}

	public void setSnapToGrid(boolean snapToGrid) {
		this.snapToGrid = snapToGrid;
	}


	
	/*
	class SelectMarker extends Component
	{
			
		int x;
		int y;
		int width;
		int height;

		@Override
		public void paint(Graphics g) {
			//super.paint(arg0);
			g.setColor(Color.RED);
			g.drawRect(x,y,width,height);
		}
	}
	*/
	
	
	
}
