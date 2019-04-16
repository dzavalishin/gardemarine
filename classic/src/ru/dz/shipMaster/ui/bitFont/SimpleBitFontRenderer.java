package ru.dz.shipMaster.ui.bitFont;

import java.awt.Graphics2D;
import java.util.logging.Logger;

public class SimpleBitFontRenderer extends GeneralBitFontRenderer {
    @SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(SimpleBitFontRenderer.class.getName()); 


    private int pixelSize = 4;
	
	// ---------------------------------------------------------------------------
	// Init
	// ---------------------------------------------------------------------------
	
	public SimpleBitFontRenderer(BitFont font) {
		super(font);
	}

	// ---------------------------------------------------------------------------
	// Getters/Setters
	// ---------------------------------------------------------------------------
	
	public int getPixelSize() {		return pixelSize;	}
	public void setPixelSize(int pixelSize) 
	{
		/*synchronized (glyphCache) {
			this.pixelSize = pixelSize;	
			glyphCache.clear();
		}*/
		this.pixelSize = pixelSize;	
		resetGlyphCache();
	}

	// ---------------------------------------------------------------------------
	// Info
	// ---------------------------------------------------------------------------

	@Override
	public int getBitStepX() { return pixelSize; }

	@Override
	public int getBitStepY() { return pixelSize; }

	// ---------------------------------------------------------------------------
	// Render
	// ---------------------------------------------------------------------------
	
	@Override
	protected void renderPixel(Graphics2D g2d, int x, int y, boolean isLit) 
	{
		g2d.setColor(isLit ? bitOnColor : bitOffColor );
		g2d.fillOval(x, y, pixelSize, pixelSize);
		g2d.setColor(bgColor);
		g2d.drawOval(x, y, pixelSize, pixelSize);
	}


}
