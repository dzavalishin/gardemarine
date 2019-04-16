package ru.dz.shipMaster.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

import ru.dz.shipMaster.config.ConfigurationFactory;
import ru.dz.shipMaster.ui.bitFont.BitFont;
import ru.dz.shipMaster.ui.bitFont.BitFontRenderer;
import ru.dz.shipMaster.ui.bitFont.ConstantBitFont;
import ru.dz.shipMaster.ui.bitFont.SimpleBitFontRenderer;
import ru.dz.shipMaster.ui.misc.VisualHelpers;
import ru.dz.shipMaster.ui.plaf.DashComponentUI;

@SuppressWarnings("serial")
public abstract class DashComponent extends JComponent {
    @SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(DashComponent.class.getName()); 

    private static final boolean SELF_DRAWN_BORDER = false;

    // -----------------------------------------------------------------------
	// Used in editing to mark current instrument position  
	// -----------------------------------------------------------------------
    
	private boolean marked = false;
	/** If true - we're instantiated in editor and must display as much as possible. */
	protected boolean editMode = false;

    public boolean isMarked() {		return marked;	}

	public void setMarked(boolean marked) {
		this.marked = marked;
		resetDashComponentBackgroundImage();
		repaint(100);
	}
	

    // -----------------------------------------------------------------------
	// Information on component's readiness to be extended horizontally or vertically
	// -----------------------------------------------------------------------
    
	/** 
     * @return true if component is ready to be extended horizontally.
     */
    public boolean canExtendWidth() { return false; }

    /** 
     * @return true if component is ready to be extended vertically.
     */
    public boolean canExtendHeight() { return false; }

    // -----------------------------------------------------------------------
	// Visual constants - have to go to VisualSettings, in fact
	// -----------------------------------------------------------------------

	/** distance from label (such as meter name) to the side of component */
	protected static final int LABEL_FROM_SIDE = 4;
	
	// TODO remove it and replace with PixelMeter
	protected static final BitFont defaultBitFont = new ConstantBitFont();
	protected static final BitFontRenderer defaultBitfontRenderer = new SimpleBitFontRenderer(defaultBitFont);

	protected BasicStroke defaultStroke = new BasicStroke( 1 );
	
	// -----------------------------------------------------------------------
	// Init
	// -----------------------------------------------------------------------

	protected DashComponent()
	{
		setMinimumSize(new Dimension(100,100));
		setPreferredSize(new Dimension(200,200));
		setMaximumSize(new Dimension(400,400));
		
		if(SELF_DRAWN_BORDER)
		{
			width = getSize().width;
			height = getSize().height;
		}
		else
		{
			setBorder( BorderFactory.createLineBorder(vis.borderColor, 1));
			
			Insets insets = getInsets();
			
			height = getSize().height - (insets.top+insets.bottom);
			width = getSize().width - (insets.left+insets.right);
		}

		enableEvents(java.awt.AWTEvent.COMPONENT_EVENT_MASK );
		addComponentListener( new ComponentListener() {
			public void componentHidden(ComponentEvent e) {}
			public void componentShown(ComponentEvent e) {}
			public void componentResized(ComponentEvent e) 
			{ 
				if(SELF_DRAWN_BORDER)
				{
					width = getSize().width;
					height = getSize().height;
				}
				else
				{
					Insets insets = getInsets();
					
					height = getSize().height - (insets.top+insets.bottom);
					width = getSize().width - (insets.left+insets.right);
				}
				
				//synchronized (this) { backgroungImage = null; }
				resetDashComponentBackgroundImage();
				processResize();
				processColorChange();
				
				if(ui != null)
				{
					((DashComponentUI)ui).processResize(DashComponent.this);
				}
				
				repaint();
			}
			public void componentMoved(ComponentEvent e) {}
		} );
	}

	// -----------------------------------------------------------------------
	// Sizing
	// -----------------------------------------------------------------------
	
	protected int width = getSize().width <= 10 ? 200 : getSize().width;
	protected int height = getSize().height <= 10 ? 200 : getSize().height;

	// -----------------------------------------------------------------------
	// Visual settings
	// -----------------------------------------------------------------------

	protected VisualSettings	vis = ConfigurationFactory.getVisualConfiguration().getVisualSettings();
	
	public VisualSettings getVis() { return vis; }
	//public void setVis(VisualSettings vis) { this.vis = vis; processColorChange(); }

	// -----------------------------------------------------------------------
	// Helpers
	// -----------------------------------------------------------------------

	private static RenderingHints myHints = null;
	/**
	 * Returns hints for quality rendering.
	 * @return
	 */
	
	protected RenderingHints getHints() {
        synchronized(this)
        {
            if(myHints == null)
            {
                myHints =
                        new RenderingHints( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                myHints.add(
                        new RenderingHints( RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
            }
            return myHints;
        }
    }

	// -----------------------------------------------------------------------
	// Text render - NB! It caches context!
	// -----------------------------------------------------------------------
	
	//protected Font textFont = getFont();
	private FontMetrics metrics = null;
	int getTextHeight(Graphics g) 
	{
		if(metrics == null)		{ metrics = g.getFontMetrics(getFont()); }
		return metrics.getHeight();
	}
	
	int getStringWidth(Graphics g, String str) 
	{
		if(metrics == null)		{ metrics = g.getFontMetrics(getFont()); }
		return metrics.stringWidth(str);
	}
	
	public enum TextAnchorX { Left, Right, Center }	
	public enum TextAnchorY { Top, Bottom, Center }	
	
	private final boolean silverTest = false;
	
	public void drawHorLabel(Graphics g, String text, int x, int y, boolean drawBg, 
			TextAnchorX alignHor, TextAnchorY alignVert )
	{
		if(text == null)
			return;
		
		g.setFont(getFont());
		/*if(metrics == null)*/		metrics = g.getFontMetrics(getFont());
		//int sHeight = metrics.getHeight();
		//int sWidth = metrics.stringWidth(text);
	
		Rectangle2D rect = metrics.getStringBounds(text,g);
		int sHeight = 1+(int)rect.getHeight();
		int sWidth = 1+(int)rect.getWidth();
		
		switch(alignHor){
		case Right: 	x -= sWidth;			break;
		case Center:	x -= sWidth/2;			break;
		case Left:								break;
		default:								break;
		}

		switch(alignVert){
		case Top: 		y += sHeight;			break;
		case Center:	y += sHeight/2;			break;
		case Bottom:							break;
		default:								break;
		}
		
		if(drawBg && !silverTest)
		{
			g.setColor(vis.labelBgColor);
			//g.fillRect(x-2, y-2-sHeight, sWidth+4, sHeight+4);
			g.fillRect(x-2, y+2-sHeight, sWidth+2, sHeight);

			g.setColor( vis.transparentLabelColor ); 
			//g.drawRect(x-2, y+2-sHeight, sWidth+2, sHeight);
			g.drawRect(x-2, y+2-sHeight, sWidth+1, sHeight-1);
		}
		

		if(silverTest)
			g.setColor(Color.BLACK);
		else
			g.setColor(vis.labelColor);
		g.drawString(text, x, y);
		
	}

	/**
	 * Darw vertical label
	 * @param g where to draw to.
	 * @param text text of label.
	 * @param ox x position.
	 * @param oy y position.
	 * @param drawBg fill background and put border
	 * @param alignHor position of reference point.
	 * @param alignVert position of reference point.
	 */
	public void drawVerLabel(Graphics2D g, String text, int ox, int oy, boolean drawBg, 
			TextAnchorX alignHor, TextAnchorY alignVert )
	{
		if(text == null)
			return;
		
		g.setFont(getFont());
		/*if(metrics == null)*/		metrics = g.getFontMetrics(getFont());
		//int sHeight = metrics.getHeight();
		//int sWidth = metrics.stringWidth(text);
	
		Rectangle2D rect = metrics.getStringBounds(text,g);
		int sHeight = 1+(int)rect.getHeight();
		int sWidth = 1+(int)rect.getWidth();

		g.translate(ox, oy);
		g.rotate(Math.PI/2);

		int x = 0;
		int y = 0;
		
		switch(alignHor){
		case Right: 	y += sHeight;			break;
		case Center:	y += sHeight/2;			break;
		case Left:								break;
		default:								break;
		}

		switch(alignVert){
		case Top: 								break;
		case Center:	x -= sWidth/2;			break;
		case Bottom:	x -= sWidth;			break;
		default:								break;
		}
		
		
		if(drawBg && !silverTest)
		{
			g.setColor(vis.labelBgColor);
			//g.fillRect(x-2, y-2-sHeight, sWidth+4, sHeight+4);
			g.fillRect(x-2, y+2-sHeight, sWidth+2, sHeight);

			g.setColor( vis.transparentLabelColor ); 
			//g.drawRect(x-2, y+2-sHeight, sWidth+2, sHeight);
			g.drawRect(x-2, y+2-sHeight, sWidth+1, sHeight-1);
		}
		

		if(silverTest)
			g.setColor(Color.BLACK);
		else
			g.setColor(vis.labelColor);
		g.drawString(text, x, y);

		g.rotate(-Math.PI/2);
		g.translate(-ox, -oy);
		
	}
	
	
	// -----------------------------------------------------------------------
	// Render of default elements - to be called by children
	// -----------------------------------------------------------------------

	protected int digitalMeterNChars = 3;

	protected void drawDigitalMeter(Graphics2D g2d, double value) {
		// TODO must be able to resize. need own renderer per component?
		String valS = Integer.toString((int)value);
		defaultBitfontRenderer.render(g2d, digitalMeterNChars, valS, width-LABEL_FROM_SIDE-defaultBitfontRenderer.stringWidth(digitalMeterNChars), height-LABEL_FROM_SIDE-defaultBitfontRenderer.stringHeight());
	}

	// XXX temp made public for PLAF impl
	public void drawComponentBorder(Graphics2D g2d) {
		if(SELF_DRAWN_BORDER)
		{
			g2d.setColor(vis.borderColor);
			g2d.drawRect(0, 0, width-1, height-1);
		}
	}

	// -----------------------------------------------------------------------
	// Main rendering logic
	//
	// 1. We are forbidding override of paint, update & paintComponent
	//
	// 2. Children must implement paintDashComponent to paint changing elements
	//    and paintDashComponentBackground to paint nonchanging part of image.
	//
	// -----------------------------------------------------------------------
	
	@Override
	protected final void paintComponent(Graphics g) {
        //Graphics2D g2d = (Graphics2D)g.create();
        Graphics2D g2d = (Graphics2D)g;

        g2d.setRenderingHints(getHints());
		super.paintComponent(g);

		Insets insets = getInsets();
		
		g2d.translate(insets.left, insets.top);
		
		height = getSize().height - (insets.top+insets.bottom);
		width = getSize().width - (insets.left+insets.right);
		
		//paintDashComponentBackground(g2d);
		paintBackgroundImage(g2d);
		paintDashComponent(g2d);

		if(marked)
		{
			g2d.setColor(Color.red);
			g2d.drawRect(0, 0, width-1, height-1);
			g2d.drawLine(width/2, 0, width/2, height-1);
			g2d.drawLine(0, height/2, width-1, height/2);
		}
		g2d.translate(-insets.left, -insets.top);
	}

	
	/** Called with synchronized (this) only! */
    private BufferedImage backgroungImage = null;
	private void generateBackgroundImage()
	{
		if(width == 0 || height == 0)
		{
			return;
		}
		
		backgroungImage = VisualHelpers.createCompatibleBitmap(width, height, true);
        if (backgroungImage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            //if (hasAlpha) 
            {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            backgroungImage = new BufferedImage(width, height, type);
        }
    
		//Insets insets = getInsets();	
        
        // Copy image to buffered image
        Graphics2D g2d = backgroungImage.createGraphics();
        g2d.setRenderingHints(getHints());
		//g2d.translate(insets.left, insets.top);
        paintDashComponentBackground(g2d);
		//g2d.translate(-insets.left, -insets.top);
        g2d.dispose();
        g2d = null;
	} 
	
	private void paintBackgroundImage(Graphics2D g2d)
	{
		synchronized (this) 
		{			
			if(backgroungImage == null)
			{
				generateBackgroundImage();
			}
			if(backgroungImage != null)
			{
				g2d.drawImage(backgroungImage, 0, 0, null );
			}
			else
			{
				paintDashComponentBackground(g2d);
				// TODO call paint alarm regions from here?
			}
		}
	} /* */
	
	/** Called by children to reset background image cache in DashboardComponent */
	protected void resetDashComponentBackgroundImage() 
	{
		synchronized (this) { backgroungImage = null; }					
	}
	
	// Just disable overrides
	@Override
	public final void paint(Graphics g) { super.paint(g); }

	// Just disable overrides
	@Override
	public final void update(Graphics g) { super.update(g); }
	

	//protected abstract void paintDashComponent(Graphics2D g2d);
	//protected abstract void paintDashComponentBackground(Graphics2D g2d);

	// -----------------------------------------------------------------------
	// PLAF support - strange and incomplete yet
	// -----------------------------------------------------------------------

	protected void paintDashComponent(Graphics2D g2d)
	{
		if(ui != null)
			((DashComponentUI)ui).paintDashComponent(g2d, this);
	}
	
	protected void paintDashComponentBackground(Graphics2D g2d)
	{
		if(ui != null)
			((DashComponentUI)ui).paintDashComponentBackground(g2d, this);
	}

	//public void setUI(DashComponentUI newUI) { super.setUI(newUI); }
	//public DashComponentUI getUI() {      return (DashComponentUI)ui; }
	
	// -----------------------------------------------------------------------
	// Downstream messages. Must be implemented in children to react
	// on some events.
	// -----------------------------------------------------------------------

	/**
	 * Must be implemented in children to recalculate things on resize.
	 */
	protected abstract void processResize();

	/**
	 * Must be implemented in children to recalculate stored colors.
	 */
	protected void processColorChange() {}

	public boolean isEditMode() {		return editMode;	}
	public void setEditMode(boolean editMode) {		this.editMode = editMode;	}

}
