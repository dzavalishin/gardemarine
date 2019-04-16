package ru.dz.shipMaster.ui.bitFont;

import java.awt.Color;
import java.awt.Graphics2D;

public interface BitFontRenderer {

	void setFont( BitFont f );
	
	void setBgColor( Color c );
	void setBitOnColor( Color c );
	void setBitOffColor( Color c );
	
	void render( Graphics2D g2d, char c, int x, int y  );
	
	/**
	 * Render a string of text.
	 * @param g2d where to draw to.
	 * @param minLen pad left spaces to make sure we have at least this number
	 * of chars rendered.
	 * @param s String to put.
	 * @param x Left upper corner x.
	 * @param y Left upper corner y.
	 */
	void render( Graphics2D g2d, int minLen, String s, int x, int y );

	/** @return this renderer's horizontal distance between font 'pixels'. */
	int getBitStepX();

	/** @return this renderer's vertical distance between font 'pixels' */
	int getBitStepY();

	/** @return width of this many character length string */
	int stringWidth(int nchars);

	public int stringHeight(); 
}
