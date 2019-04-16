package ru.dz.shipMaster.ui.bitFont;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import ru.dz.shipMaster.ui.VisualSettings;

public abstract class GeneralBitFontRenderer implements BitFontRenderer {
    //private static final Logger log = Logger.getLogger(GeneralBitFontRenderer.class.getName()); 


    private Map<Character,BufferedImage> glyphCache = new HashMap<Character,BufferedImage>();

	// ---------------------------------------------------------------------------
	// Fields
	// ---------------------------------------------------------------------------

	protected BitFont font = null;

	protected Color 		bgColor = VisualSettings.global.bitFontBgColor; 
	protected Color 		bitOffColor = VisualSettings.global.bitFontBitOffColor;
	protected Color 		bitOnColor = VisualSettings.global.bitFontBitOnColor; 
	// ---------------------------------------------------------------------------
	// Init
	// ---------------------------------------------------------------------------
	
	public GeneralBitFontRenderer( BitFont font )
	{
		this.font = font;
	}
	
	// ---------------------------------------------------------------------------
	// Renderer wrappers
	// ---------------------------------------------------------------------------

	// TO DO renders 8-pixel high fonts only
	public void doRenderChar(Graphics2D g2d, char c, int x, int y) {
		
		byte[] pixels = font.getCharacter(c);
		
		if( pixels == null ) { return; }
		
		for( int charx = 0; charx < font.getSizeX(); charx++ )
		{
			byte column = pixels[charx];
			int pixelx = x + (charx*getBitStepX());
			for( int chary = 0; chary < font.getSizeY(); chary++ )
			{
				boolean isLit = ((column >> chary) & 0x01) != 0;
				renderPixel(g2d, pixelx, y+(chary*getBitStepY()), isLit);
			}
		}
	}
	

	public void render(Graphics2D g2d, char c, int x, int y) {
		synchronized (glyphCache) {
			BufferedImage image = glyphCache.get(c);
			if(image == null)
			{
				int cHeight = stringHeight();
				int cWidth = stringWidth(1);
				image = new BufferedImage(cWidth, cHeight, BufferedImage.TYPE_INT_ARGB);
				Graphics2D graphics = image.createGraphics();
				graphics.setRenderingHints(getHints());
				doRenderChar(graphics, c, 0, 0);
				glyphCache.put(c, image);
			}
			g2d.drawImage(image, x, y, null);
		}		
	}

	public void render(Graphics2D g2d, int minLen, String s, int x, int y) {
		int charStepX = getBitStepY()*font.getSizeX();

		int len = s.length();
		while(len < minLen-- )
		{
			render(g2d, ' ', x, y); // pad left with spaces
			x += charStepX;
		}
		
		for( int i = 0; i < len; i++ )
		{
			render(g2d,s.charAt(i),x,y);
			x += charStepX;
		}
	}

	protected void resetGlyphCache()
	{
		synchronized (glyphCache) { glyphCache.clear(); } 
	}
	
	// ---------------------------------------------------------------------------
	// Renderer
	// ---------------------------------------------------------------------------

	abstract protected void renderPixel( Graphics2D g2d, int x, int y, boolean isLit );
	
	// ---------------------------------------------------------------------------
	// Getters/Setters
	// ---------------------------------------------------------------------------
	
	public Color getBgColor() {		return bgColor;	}
	public void setBgColor(Color bgColor) { this.bgColor = bgColor; resetGlyphCache();	}

	public Color getBitOffColor() {		return bitOffColor;	}
	public void setBitOffColor(Color bitOffColor) { this.bitOffColor = bitOffColor; resetGlyphCache(); }

	public Color getBitOnColor() {		return bitOnColor;	}
	public void setBitOnColor(Color bitOnColor) { this.bitOnColor = bitOnColor; resetGlyphCache(); }

	public BitFont getFont() {		return font;	}
	public void setFont(BitFont font) { this.font = font; resetGlyphCache(); }

	// ---------------------------------------------------------------------------
	// Info
	// ---------------------------------------------------------------------------

	public int stringWidth(int nchars) { return nchars * getBitStepX() * font.getSizeX(); }
	public int stringHeight() { return getBitStepY() * font.getSizeY(); }

	abstract public int getBitStepX();
	abstract public int getBitStepY();

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

}


