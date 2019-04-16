package ru.dz.shipMaster.ui.element;

import java.awt.Dimension;
import java.awt.Graphics2D;

import ru.dz.shipMaster.ui.DashComponent;
import ru.dz.shipMaster.ui.bitFont.BitFont;
import ru.dz.shipMaster.ui.bitFont.BitFontRenderer;
import ru.dz.shipMaster.ui.bitFont.ConstantBitFont;
import ru.dz.shipMaster.ui.bitFont.SimpleBitFontRenderer;

public class PixelMeter extends DashComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -851115397526480587L;
	protected static final BitFont defaultBitFont = new ConstantBitFont();
	protected static final BitFontRenderer defaultBitfontRenderer = new SimpleBitFontRenderer(defaultBitFont);
	private double value;
	private int nChars = 3;
	
	public PixelMeter()
	{
		calcSetSize();
		setBorder(null);
	}

	private void calcSetSize() {
		Dimension myPreferredSize = new Dimension();
		myPreferredSize.height = defaultBitfontRenderer.stringHeight();
		myPreferredSize.width = defaultBitfontRenderer.stringWidth(nChars);
		//myMinimumSize = myPreferredSize;
		setMinimumSize(myPreferredSize);
		setPreferredSize(myPreferredSize);
		setSize(myPreferredSize);
	}
	
	@Override
	protected void paintDashComponent(Graphics2D g2d) { }

	@Override
	protected void paintDashComponentBackground(Graphics2D g2d) {
		String valS = Integer.toString((int)value);
		//defaultBitfontRenderer.render(g2d, digitalMeterNChars , valS, width-LABEL_FROM_SIDE-defaultBitfontRenderer.stringWidth(digitalMeterNChars), height-LABEL_FROM_SIDE-defaultBitfontRenderer.stringHeight());
		defaultBitfontRenderer.render(g2d, nChars , valS, 0, 0);
	}

	@Override
	protected void processResize() { }

	public double getValue() {		return value;	}

	public void setValue(double value) {
		this.value = value;
		resetDashComponentBackgroundImage();
	}

	/** Number of characters to draw */
	public void setNChars(int chars) 
	{ 
		nChars = chars;
		if(nChars <= 0) { nChars = 1; }
		calcSetSize(); 
	}
	
	public int getNChars() {		return nChars;	}


}
