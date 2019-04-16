/**
 * The X11-based metrics for a PCF font.
 *
 * @see PCFFont
 * @author      Ken Shirriff, shirriff@eng.sun.com
 */

package ru.dz.shipMaster.ui.pcfFont;

import java.util.logging.Logger;

public class PCFMetrics {
    @SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(PCFMetrics.class.getName()); 

    /**
	 * The distance between the character origin and the start of the
	 * bitmap.  The leftSideBearing is zero if the character starts at
	 * the origin, positive if there is padding before the bitmap
	 * starts, or negative in the unusual case that the bitmap starts
	 * before the specified origin.
	 */
	public int leftSideBearing;

	/**
	 * The distance between the character origin and the end of the
	 * bitmap.  The rightSideBearing is normally positive, specifying the
	 * number of pixels to the right of the origin.  The width of the
	 * bitmap is is given by rightSideBearing-leftSideBearing.
	 * The width of the bitmap is, in general, different from the width
	 * of the character.
	 */
	public int rightSideBearing;

	/**
	 * The distance in pixels between the character origin and the
	 * origin of the next character.
	 */
	public int characterWidth;

	/**
	 * The ascent in pixels of the character.  This is the number of
	 * pixels in the bitmap above the origin.  The height of the bitmap
	 * is given by ascent+descent.
	 */
	public int ascent;

	/**
	 * The distance in pixels of the character.  This is the number of
	 * pixels in the bitmap below the origin.
	 */
	public int descent;

	/**
	 * Debugging function to print the metrics information.
	 */
	public String toString() {
		return "leftSideBearing="+leftSideBearing+
		", rightSideBearing="+rightSideBearing+
		", characterWidth="+characterWidth+
		", ascent="+ascent+
		", descent="+descent;
	}
}
