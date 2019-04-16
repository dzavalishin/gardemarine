/*
 * PCFFontAA.java
 *	  Parse and display a PCF font using Anti-Aliasing
 *
 * Ken Shirriff	 ken.shirriff@eng.sun.com
 *
 * Copyright (c) 1997 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Some of this code is based on the X Consortium's pcfread.c.
 *
 * SUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. SUN SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 *
 * Please refer to the file "license.txt"
 * for further important copyright and licensing information.
 */

package ru.dz.shipMaster.ui.pcfFont;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * PCFFontAA provides antialiased use of a font in X11 .PCF format.
 *
 * For example:
 * <pre>
 *   g = getGraphics();
 *   InputStream is = <i>stream to the font file</i>
 *   PCFFont bf = new PCFFontAA(is);
 *   bf.drawString(g, "This is a test string", 10, 10);
 * </pre>
 *
 * @see      PCFFont
 * @author      Ken Shirriff, shirriff@eng.sun.com
 */

public class PCFFontAA extends PCFFont {
    private static final Logger log = Logger.getLogger(PCFFontAA.class.getName()); 

	PCFMetrics[] origMetrics;
	int[] fgFracs;

	public void setFgColor(Color c) {
		super.setFgColor(c);
		recomputeFracs();
	}

	public void setBgColor(Color c) {
		super.setBgColor(c);
		recomputeFracs();
	}

	void recomputeFracs() {
		if (fgFracs == null) {
			fgFracs = new int[5];
		}
		int af = (fgColor>>>24)&0xff;
		int rf = (fgColor>>>16)&0xff;
		int gf = (fgColor>>>8)&0xff;
		int bf = (fgColor)&0xff;
		int ab = (bgColor>>>24)&0xff;
		int rb = (bgColor>>>16)&0xff;
		int gb = (bgColor>>>8)&0xff;
		int bb = (bgColor)&0xff;
		for (int i=0;i<=4;i++) {
			int a,r,g,b;
			if (ab==0) {
				a = (af*i+ab*(4-i))/4;
				r = rf;
				g = gf;
				b = bf;
			} else {
				a = (af*i+ab*(4-i))/4;
				r = (rf*i+rb*(4-i))/4;
				g = (gf*i+gb*(4-i))/4;
				b = (bf*i+bb*(4-i))/4;
			}
			fgFracs[i] = (a<<24)|(r<<16)|(g<<8)|(b<<0);
		}
	}

/**
 * Creates a new PCFFontAA.
 * A stream to the PCF font file is passed in.
 * @param	data	A input stream supplying the font data
 * @exception	IOException	If a problem occurs.
 */
	public PCFFontAA(InputStream data)
	    throws IOException {
		super(data);
		setBgColor(Color.white);

		// Divide dimensions by two

		ascent = divide2(ascent);
		descent = divide2(descent);
		maxAscent = divide2(maxAscent);
		maxDescent = divide2(maxDescent);

		maxbounds = divide2(maxbounds);

		origMetrics = new PCFMetrics[nbitmaps];
		for (int i=0; i<nbitmaps; i++) {
			origMetrics[i] = charInfo[i].metrics;
			charInfo[i].metrics = divide2(charInfo[i].metrics);
		}
		
	}

	// ------- end of public methods ---------

	// ------- methods to help with the public methods

	// Divide by two, rounding up
	final int divide2(int val) {
		return (val+1)/2;
	}

	PCFMetrics divide2(PCFMetrics in) {
		PCFMetrics out = new PCFMetrics();
		out.leftSideBearing = divide2(in.leftSideBearing);
		out.rightSideBearing = divide2(in.rightSideBearing);
		out.characterWidth = divide2(in.characterWidth);
		out.ascent = divide2(in.ascent);
		out.descent = divide2(in.descent);
		return out;
	}

	// -------- Methods to process the bitmap

	// Copy a glyph into buf, starting at the specified byte offset.
	// The width of the buffer is given in span.
	// The glyph better fit into the buffer, or there'll be trouble.
	void blitGlyph(int num, int buf[], int bufoffset, int span) {
		if (num == defaultBitmap) {
			return;
		}
		@SuppressWarnings("unused")
		boolean msb_bitorder = msb_bit_order(bitmap_format);
		@SuppressWarnings("unused")
		boolean msb_byteorder = msb_byte_order(bitmap_format);
		int glyphpad = glyph_pad(bitmap_format);
		@SuppressWarnings("unused")
		int scanunit = scan_unit(bitmap_format);
		int bitflip = msb_bit_order(bitmap_format) ? 7 : 0;
		int byteflip = (msb_bit_order(bitmap_format) ==
		    msb_byte_order(bitmap_format)) ? 0 :
		    (scan_unit(bitmap_format)-1);
		int height = origMetrics[num].ascent +
		    origMetrics[num].descent;
		int width = -origMetrics[num].leftSideBearing+
			origMetrics[num].rightSideBearing;
		if (height<0 || width<0) {
			log.severe("Negative height/width for "+num);
			return;
		}
		int offset = charInfo[num].offset;
		int stride = ((width+7)>>3);
		int w2 = divide2(width);
		int h2 = divide2(height);
		int[] tmpbuf = new int[w2*h2];
		stride = (stride+glyphpad-1)&~(glyphpad-1);
		for (int i=0;i<height;i++) {
			for (int j=0; j<width; j++) {
				int bit_pos = (j&7)^bitflip;
				int byte_pos = j>>3;
				if ((bits[offset+
				    ((byte_pos+i*stride)^byteflip)]&
				    (1<<bit_pos)) != 0) {
					tmpbuf[(i/2)*w2+j/2]++;
				}
			}
		}
		for (int i=0; i<h2; i++) {
			for (int j=0; j<w2; j++) {
				int pixel = tmpbuf[i*w2+j];
				if (pixel>0) {
					buf[bufoffset+i*span+j] =
					    fgFracs[pixel];
				}
			}
		}
	}
}
