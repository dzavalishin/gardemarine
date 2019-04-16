package ru.dz.gardemarine.ui.frames;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.ScrollPane;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import ru.dz.gardemarine.world.IConfig;
import ru.dz.gardemarine.world.IItem;
import ru.dz.gardemarine.world.IVisual;
import ru.dz.gardemarine.world.MinimalConfig;
import ru.dz.gardemarine.world.MinimalItem;



/**
 * Configuration item editor that keeps a sibling frame aside which can be used to
 * display item's state.
 * @author dz
 *
 */

public class WorldEditorFrame implements IWorldEditor  
{
	protected static final Logger log = Logger.getLogger(WorldEditorFrame.class.getName()); 


	@Override
	public String toString() { return "World editor"; }





	private final Dimension minSize;

	protected WorldEditorFrame(Dimension minSize)
	{
		this.minSize = minSize;
	}

	protected JFrame sideFrame = new JFrame("?"); //$NON-NLS-1$
	//private JPanel noDrvPanel = new JPanel();
	private Point contentPaneLocation;

	void setupSideFrame(String title)
	{
		sideFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		//sideFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		//sideFrame.setMinimumSize(new Dimension(240,200));
		sideFrame.setMinimumSize(minSize);
		sideFrame.setTitle(title);
		sideFrame.pack();
		sideFrame.setVisible(true);

		// Now for the mouse tracking

		sideFrame.addMouseMotionListener(mmListener);
		sideFrame.addMouseListener(mListener);

		// hack - doesn't work too
		contentPaneLocation = sideFrame.getContentPane().getLocation();

		// doesn't work :(
		//sideFrame.getContentPane().addMouseMotionListener(mmListener);
		//sideFrame.getContentPane().addMouseListener(mListener);

	}

	private MouseListener mListener;
	{
		mListener = new MouseListener() {

			public void mouseClicked(MouseEvent e) {/*ignore*/}
			public void mouseEntered(MouseEvent e) {/*ignore*/}
			public void mouseExited(MouseEvent e)  {/*ignore*/}

			public void mousePressed(MouseEvent e) {
				Component c = sideFrame.getContentPane().getComponentAt(e.getPoint());
				setCurrenSFChild(c, e.getPoint());
			}

			public void mouseReleased(MouseEvent e) {/*ignore*/}
		};
	}

	//private boolean dragDropEnabled;
	private MouseMotionListener mmListener;
	private Component currentSFChild;
	private Point currentSFChildStartDragCursorPos;
	private Point currentSFChildStartDragChildPos;
	{
		mmListener = new MouseMotionListener() {

			public void mouseDragged(MouseEvent e) {
				//log.log(Level.SEVERE, "Drag: "+e);
				moveCurrentSFChildTo(e.getPoint());
				//e.consume();
			}

			public void mouseMoved(MouseEvent e) {
				//log.log(Level.SEVERE, "Move: "+e);
				Component c = sideFrame.getContentPane().getComponentAt(e.getPoint());
				setCurrenSFChild(c, e.getPoint());
				//e.consume();

			}};
	}

	protected void moveCurrentSFChildTo(Point point) {
		//if(!dragDropEnabled) return;

		Point p = new Point(
				point.x-currentSFChildStartDragCursorPos.x+currentSFChildStartDragChildPos.x-contentPaneLocation.x,
				point.y-currentSFChildStartDragCursorPos.y+currentSFChildStartDragChildPos.y-contentPaneLocation.y
		);
		if(!checkLocation(currentSFChild,p))
			return;
		currentSFChild.setLocation(p);
	}

	@SuppressWarnings("unchecked")
	private void setChildHilite(Component c, boolean onoff) {
		if ( (c != null) && (c instanceof IVisual) ) {
			IVisual<IItem> vis = (IVisual<IItem>) c;
			vis.setHighLight(onoff);
		}		
	}

	protected void setCurrenSFChild(Component c, Point cursorPos) {
		setChildHilite(currentSFChild, false);

		if( c != null & ! (c instanceof IVisual) )
		{
			currentSFChild = null;
			return;
		}

		currentSFChild = c;
		currentSFChildStartDragCursorPos = cursorPos;

		if( c == null )
			return;

		currentSFChildStartDragChildPos = c.getLocation();

		setChildHilite(currentSFChild, true);
		//currentSFChild

		demoPanel.setComponentZOrder(c, 0);
		c.requestFocusInWindow();

	}


	protected void setSFPanel( JPanel p )
	{
		if(false)
		{
			ScrollPane scrollPane = new ScrollPane(ScrollPane.SCROLLBARS_ALWAYS);
			scrollPane.add(p);
			sideFrame.setContentPane( scrollPane );
		}
		else
			sideFrame.setContentPane(p);

		p.setBorder(new EmptyBorder(4,4,4,4));
		p.setBackground(Color.BLACK);
		//p.setBorder(new MatteBorder(4,4,4,4,Color.DARK_GRAY));
		sideFrame.pack();
	}






	//private Map<JPanel,Integer> componentIndices = new HashMap<JPanel, Integer>();
	//private Vector<Point> absolutePositions = new Vector<Point>();

	/** 
	 * 
	 * @return true if it is OK to move this component to this location. 
	 * False return forbids movement.
	 *  
	 **/
	protected boolean checkLocation(Component c, Point p) {
		/*
		Integer index = componentIndices.get(c);
		if(index == null)
		{
			//log.severe("Unknown component dragged");
			return false;
		}
		if(absolutePositions.size() <= index)
			absolutePositions.setSize(index+1);
		 */

		//if( p.x < 0 || p.y < 0 )			return false;

		//if( p.x + c.getWidth() > demoPanel.getWidth() ) return false;
		//if( p.y + c.getHeight() > demoPanel.getHeight() ) return false;

		if( c == null ) return false;

		if( p.x < 0 )			p.x = 0;
		if( p.y < 0 )			p.y = 0;

		if( p.x + c.getWidth() > demoPanel.getWidth() ) p.x = demoPanel.getWidth() - c.getWidth(); 
		if( p.y + c.getHeight() > demoPanel.getHeight() ) p.y = demoPanel.getHeight() - c.getHeight();

		//absolutePositions.set(index, p);
		return true;
	}


	private JPanel demoPanel = new JPanel(null);
	{
		//demoPanel.setLayout(null);
	}


	Map<IConfig,IVisual> items = new TreeMap<IConfig,IVisual>();

	int startYpos = 0; 
	public void addItem(IConfig<? extends IItem> c)
	{
		IVisual<? extends IItem> newVisual = c.newVisual(this);
		JPanel visPanel = newVisual.getPanel();
		demoPanel.add( visPanel );

		items.put(c,newVisual);

		//componentIndices.put(panel2, 0);
		visPanel.setLocation(10, startYpos);
		startYpos += 40;

		visPanel.setVisible(true);
		visPanel.validate();
		visPanel.setSize(visPanel.getPreferredSize());
	}

	@Override
	public void deleteItem(IConfig<? extends IItem> toDelete) {
		if( !items.containsKey(toDelete))
			return;

		IVisual visual = items.get(toDelete);

		demoPanel.remove(visual.getPanel());
		items.remove(toDelete);

		toDelete.destroy();
		visual.destroy();

		demoPanel.repaint(100);
	}

	void makesample()
	{
		setupSideFrame("Panel preview");
		setSFPanel( demoPanel  );		

		{
			addItem( new MinimalConfig<MinimalItem>() );
			addItem( new MinimalConfig<MinimalItem>() );
			addItem( new MinimalConfig<MinimalItem>() );
			addItem( new MinimalConfig<MinimalItem>() );
		}

		sideFrame.pack();

	}


	public static void main(String[] args) {
		WorldEditorFrame frame = new WorldEditorFrame(new Dimension(600,400));

		frame.makesample();
		frame.sideFrame.validate();

	}


}

