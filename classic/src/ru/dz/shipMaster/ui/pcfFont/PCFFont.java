/*
 * PCFFont.java
 *	  Parse and display a PCF font
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
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.MemoryImageSource;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * PCFFont is a class that allows the use of a font in X11 .PCF format.
 *
 * For example:
 * <pre>
 *   g = getGraphics();
 *   InputStream is = <i>stream to the font file</i>
 *   PCFFont bf = new PCFFont(is);
 *   bf.drawString(g, "This is a test string", 10, 10);
 * </pre>
 *
 * @see		PCFFontAA
 * @author      Ken Shirriff, shirriff@eng.sun.com
 */

public class PCFFont {
	private static final Logger log = Logger.getLogger(PCFFont.class.getName()); 


	int fgColor;
	int bgColor;
	int ascent;
	int descent;
	int maxAscent;
	int maxDescent;
	int charSpacing;

	//CompletionCallback callback;

	int firstRow, lastRow;

	/**
	 * Get the height of the font.
	 * The height is the ascent plus descent plus leading.
	 * @return      The height of the font in pixels
	 */

	public int getHeight() {		return ascent+descent+1;	}

	/**
	 * Get the typical ascent of the font.
	 * @return      The ascent of the font in pixels
	 */

	public int getAscent() {		return ascent;	}

	/**
	 * Get the typical descent of the font.
	 * @return      The descent of the font in pixels
	 */
	public int getDescent() {		return descent;	}

	/**
	 * Get the maximum ascent of the font.
	 * @return      The ascent of the font in pixels
	 */
	public int getMaxAscent() {		return maxAscent;	}

	/**
	 * Get the maximum descent of the font.
	 * @return      The descent of the font in pixels
	 */
	public int getMaxDescent() {		return maxDescent;	}

	/**
	 * Set the foreground color. null indicates transparency.
	 * @param       c       The foreground color
	 */
	public void setFgColor(Color c) {
		if (c==null) {
			fgColor = 0;
		} else {
			fgColor = c.getRGB();
		}
	}

	/**
	 * Set the character spacing
	 * @param       s       Spacing in pixels
	 */
	public void setCharSpacing(int s) {
		charSpacing = s;
	}

	/**
	 * Set the background color. null indicates transparency.
	 * @param       c       The background color
	 */
	public void setBgColor(Color c) {
		if (c==null) {
			bgColor = 0;
		} else {
			bgColor = c.getRGB();
		}
	}

	/**
	 * Return the width of the specified string.
	 * @param       s       The string
	 * @return      The width in pixels of the string
	 */
	public int stringWidth(String s) {
		PCFMetrics m = stringMetrics(s);
		int xpos = (m.leftSideBearing<0 ? -m.leftSideBearing : 0);
		return xpos+m.rightSideBearing;
	}

	/**
	 * Return the width of the specified character.
	 * @param       c       The character
	 * @return      The width in pixels of the character
	 */
	public int charWidth(char c) {
		return stringWidth(String.valueOf(c));
	}

	/**
	 * Create an image of the specified string rendered in the font.
	 * @param       s       The string
	 * @return      The image corresponding to the string
	 */
	public Image stringImage(String s) {
		MetricsImage mi = stringImageMI(s);
		return mi.image;
	}

	/**
	 * Draw a string on the screen.
	 * @param       g       The Graphics context
	 * @param       s       The string
	 * @param       x       The x start position in pixels.
	 * @param       y       The y position of the bottom of the line in pixels.
	 */
	public void drawString(Graphics g, String s, int x, int y) {
		MetricsImage mi = stringImageMI(s);
		int xpos = (mi.metrics.leftSideBearing<0 ?
				-mi.metrics.leftSideBearing : 0);
		g.drawImage(mi.image, x-xpos, y-mi.metrics.ascent, null);
	}

	/**
	 * Draw a string into a buffer
	 * @param       s       The string
	 * @param       b       The buffer
	 * @param       x       The x offset into the buffer
	 * @param       y       The y position of the bottom of the line in pixels.
	 * @param       w       The width of a buffer line
	 */
	public void drawStringBuffer(String s, int b[], int x, int y, int w) {

		for (int i=0; i<s.length(); i++) {
			int bm = getBitmap((int)s.charAt(i));
			blitGlyph(bm, b, x+
					charInfo[bm].metrics.leftSideBearing +
					(y-charInfo[bm].metrics.ascent)*w, w);
			x += charInfo[bm].metrics.characterWidth+charSpacing;
		}
	}

	/**
	 * Draw a string into a buffer
	 * @param       c       The character
	 * @param       b       The buffer
	 * @param       x       The x offset into the buffer
	 * @param       y       The y position of the bottom of the line in pixels.
	 * @param       w       The width of a buffer line
	 */
	public void drawCharBuffer(char c, int b[], int x, int y, int w) {
		int bm = getBitmap(c);
		blitGlyph(bm, b, x+
				charInfo[bm].metrics.leftSideBearing +
				(y-charInfo[bm].metrics.ascent)*w, w);			
	}


	/**
	 * Get the PCF metrics associated with the font.
	 * @return      The metrics
	 */
	public PCFMetrics fontMetrics() {		return maxbounds;	}

	/**
	 * Get the PCF metrics associated with a string.
	 * @param       s       The string
	 * @return      The metrics
	 */
	public PCFMetrics stringMetrics(String s) {
		//
		// Compute the maximum ascent, descent, and bearings for the string.
		//
		PCFMetrics m = new PCFMetrics();
		m.ascent = 0;
		m.descent = 0;
		m.leftSideBearing = 0;
		m.rightSideBearing = 0;
		m.characterWidth = 0;

		for (int i=0; i<s.length(); i++) {
			int bm = getBitmap((int)s.charAt(i));
			if (charInfo[bm].metrics.ascent > m.ascent) {
				m.ascent = charInfo[bm].metrics.ascent;
			}
			if (charInfo[bm].metrics.descent > m.descent) {
				m.descent = charInfo[bm].metrics.descent;
			}
			if (m.characterWidth+charInfo[bm].metrics.
					leftSideBearing < m.leftSideBearing) {
				m.leftSideBearing = m.characterWidth+
				charInfo[bm].metrics.leftSideBearing;
			}
			if (m.characterWidth+charInfo[bm].metrics.
					rightSideBearing > m.rightSideBearing) {
				m.rightSideBearing = m.characterWidth+
				charInfo[bm].metrics.rightSideBearing;
			}
			m.characterWidth += charInfo[bm].metrics.characterWidth;
			m.characterWidth += charSpacing;
		}
		m.characterWidth -= charSpacing; // Remove last space
		return m;
	}

	/**
	 * Get the PCF metrics associated with a string.
	 * @param c Character to mesaure
	 * @return      The metrics
	 */
	public PCFMetrics charMetrics(char c) {
		//
		// Compute the maximum ascent, descent, and bearings for the string.
		//
		PCFMetrics m = new PCFMetrics();
		m.ascent = 0;
		m.descent = 0;
		m.leftSideBearing = 0;
		m.rightSideBearing = 0;
		m.characterWidth = 0;

		int bm = getBitmap(c);
		if (charInfo[bm].metrics.ascent > m.ascent) {
			m.ascent = charInfo[bm].metrics.ascent;
		}
		if (charInfo[bm].metrics.descent > m.descent) {
			m.descent = charInfo[bm].metrics.descent;
		}
		if (m.characterWidth+charInfo[bm].metrics.
				leftSideBearing < m.leftSideBearing) {
			m.leftSideBearing = m.characterWidth+
			charInfo[bm].metrics.leftSideBearing;
		}
		if (m.characterWidth+charInfo[bm].metrics.
				rightSideBearing > m.rightSideBearing) {
			m.rightSideBearing = m.characterWidth+
			charInfo[bm].metrics.rightSideBearing;
		}
		m.characterWidth += charInfo[bm].metrics.characterWidth;

		return m;
	}


	/**
	 * Convert a string to Unicode.
	 * That is, "\ u x x x x" substrings are made into Unicode characters.
	 * @param	s	The string
	 * @return      The converted string.
	 */
	static public String toUnicode(String s) {
		StringBuffer outstr = new StringBuffer(s.length());
		for (int i=0; i<s.length(); i++) {
			char c = s.charAt(i);
			if (c == '\\' && s.length()-i >= 6 &&
					s.charAt(i+1) == 'u') {
				c = (char)Integer.parseInt(
						s.substring(i+2,i+6),16);
				i += 5;
			}
			outstr.append(c);
		}
		return outstr.toString();
	}

	/**
	 * Get first page number
	 * @return      First page
	 */
	public int getFirstPage() {		return firstRow;	}

	/**
	 * Get last page number
	 * @return      Last page
	 */
	public int getLastPage() {		return lastRow;	}

	/**
	 * Test if the font is a 2-byte font.
	 * @return      True if the font is 2-byte (e.g. JISX0208).
	 */
	public boolean is2Byte() {		return firstRow>0;	}

	/**
	 * Test if the character is defined in the font.
	 * @param	c	The character
	 * @return      True or false
	 */
	public boolean defined(char c) {
		return getBitmap(c)!=defaultBitmap;
	}

	/**
	 * Convert ASCII encodings in the string to JISX0208.  Unknown characters
	 * are left unmodified.
	 * @param	s	The string
	 * @return      The converted string.
	 */
	static public String toJISX(String s) {
		StringBuffer sb = new StringBuffer(s.length());
		int mode = 0;
		for (int i=0; i<s.length(); i++) {
			int c = (int)s.charAt(i);
			if (c == 0x1b && i<s.length()-2) {
				if (s.charAt(i+1) == '(' &&
						(s.charAt(i+2)=='B' ||
								s.charAt(i+2)=='J')) {
					mode = 0;
					i += 2;
					continue;
				} else if (s.charAt(i+1) == '$' &&
						(s.charAt(i+2)=='@' ||
								s.charAt(i+2)=='B')) {
					mode = 1;
					i += 2;
					continue;
				}
			}
			if (mode == 1 && i<s.length()-1) {
				int c2 = (int)(s.charAt(i+1));
				c = (char)((c<<8)|c2);
				i++;
			} else if (c > 0x80 && i<s.length()-1) {
				c -= 0x80;
				int c2 = (int)(s.charAt(i+1))-0x80;
				c = (char)((c<<8)|c2);
				i++;
			}
			int cj = c-jisxOffset;
			if (cj >= 0 && cj < jisx.length && jisx[cj]>0) {
				c = jisx[cj];
			}
			sb.append((char)c);
		}
		return sb.toString();
	}

	/**
	 * Creates a new PCFFont.
	 * A stream to the PCF font file is passed in.
	 * @param	data	A input stream supplying the font data
	 * @exception	IOException	If a problem occurs.
	 */

	public PCFFont(InputStream data)
	throws IOException {
		//this.callback = callback;
		setFgColor(Color.black);
		setBgColor(null);
		this.data = new DataInputStream(data);
		// Read in the table of contents
		getTable();
		// Set the callback length
		int offset = findTableOffset(PCF_SWIDTHS);
		if (offset == -1) {
			offset = findTableOffset(PCF_BDF_ENCODINGS);
		}
		setCallbackEnd(offset);
		// Read in the accelerators
		boolean gotAccel = getAccelerators(PCF_ACCELERATORS);
		// Read in the metrics
		getMetrics();
		// Read in the bitmaps
		getBitmaps();
		// Read in the character encodings
		getEncodings();
		// Read in the bdf accelerators
		if (!gotAccel) {
			gotAccel = getAccelerators(PCF_BDF_ACCELERATORS);
		}
		if (!gotAccel) {
			throw new IOException("No accelerators");
		}
		// cleanup
		/*if (callback != null) {
			callback.completed(100);
		}*/
		data.close();
		table = null;
		data = null;
	}

	// ------- end of public methods ---------

	// ------- methods to help with the public methods


	final static int jisxOffset = 32; // Offset of first character
	// Mapping from ASCII to JISX-0208
	final static char[] jisx = {
		'\u2121', '\u212a', '\u216d', '\u2174', '\u2170', '\u2173', '\u2175',
		'\u216c', '\u214a', '\u214b', '\u2176', '\u215c', '\u2124', '\u215d',
		'\u2125', '\u213f', '\u2330', '\u2331', '\u2332', '\u2333', '\u2334',
		'\u2335', '\u2336', '\u2337', '\u2338', '\u2339', '\u2127', '\u2128',
		'\u2163', '\u2161', '\u2164', '\u2129', '\u2177', '\u2341', '\u2342',
		'\u2343', '\u2344', '\u2345', '\u2346', '\u2347', '\u2348', '\u2349',
		'\u234a', '\u234b', '\u234c', '\u234d', '\u234e', '\u234f', '\u2350',
		'\u2351', '\u2352', '\u2353', '\u2354', '\u2355', '\u2356', '\u2357',
		'\u2358', '\u2359', '\u235a', '\u214e', '\u2140', '\u214f', '\u2130',
		'\u2132', '\u2129', '\u2361', '\u2362', '\u2363', '\u2364', '\u2365',
		'\u2366', '\u2367', '\u2368', '\u2369', '\u236a', '\u236b', '\u236c',
		'\u236d', '\u236e', '\u236f', '\u2370', '\u2371', '\u2372', '\u2373',
		'\u2374', '\u2375', '\u2376', '\u2377', '\u2378', '\u2379', '\u237a',
		'\u2150', '\u2143', '\u2151', '\u2141', '\u2121'};

	// Create an image corresponding to the string
	// Return both the image and the metrics.
	MetricsImage stringImageMI(String s) {
		//@SuppressWarnings("unused")
		//String str = new String(s);
		PCFMetrics bbox = new PCFMetrics();

		// Compute the size of the buffer

		bbox = stringMetrics(s);

		// Create the image buffer

		int xpos = (bbox.leftSideBearing<0 ? -bbox.leftSideBearing : 0);
		int w = xpos + bbox.rightSideBearing;
		int h = bbox.ascent + bbox.descent;

		// Internet Explorer bug: doesn't like 0x0 images.
		if (w == 0) {
			w = 1;
		}
		if (h == 0) {
			h =1;
		}

		int[] b = new int[w*h];
		if (bgColor != 0) {
			for (int i=0; i<w*h; i++) {
				b[i] = bgColor;
			}
		}

		int ypos = bbox.ascent;

		// Render the string into the buffer
		drawStringBuffer(s, b, xpos, ypos, w);

		// Create an image from the buffer

		Image i = 
			Toolkit.getDefaultToolkit().createImage(new
					MemoryImageSource(w,h,b,0,w));
		//MetricsImage mi = new MetricsImage(bbox, i);
		//return mi;
		return new MetricsImage(bbox, i);
	}

	//
	// Map a character to a bitmap
	//
	int getBitmap(int c) {
		if (c < 0 || c >= max_char) {
			return defaultBitmap;
		}
		return char_map[c];
	}


	// ------- methods for reading data from file

	//
	// Read a 32 bit int in lsb form
	//
	int getlsb32() throws IOException {
		pos += 4;
		int val = data.readInt();
		return ((val&0xff000000)>>>24)|((val&0x00ff0000)>>8)|
		((val&0x0000ff00)<<8)|((val&0x000000ff)<<24);
	}


	//
	// Read a 32 bit int in specified format
	//
	int getint32(int format) throws
	IOException {
		pos += 4;
		int val = data.readInt();
		if (msb_byte_order(format)) {
			// MSBFirst
			return val;
		} else {
			return ((val&0xff000000)>>>24)|((val&0x00ff0000)>>8)|
			((val&0x0000ff00)<<8)|((val&0x000000ff)<<24);
		}
	}

	//
	// Read a 16 bit int in specified format
	//
	int getint16(int format) throws IOException {
		pos += 2;
		int b0 = data.readByte()&0xff;
		int b1 = data.readByte()&0xff;
		if (msb_byte_order(format)) {
			// MSBFirst
			return (b0<<8)|b1;
		} else {
			return (b1<<8)|b0;
		}
	}

	//
	// Read a 8 bit int in specified format
	//
	int getint8(int format) throws IOException {
		pos += 1;
		return data.readByte()&0xff;
	}

	// Skip over bytes
	void skipTo(int skip) throws IOException {
		if (skip-pos<0) {
			throw new IOException("Backwards skipTo");
		}
		data.skipBytes(skip-pos);
		pos = skip;
		checkCallback();
	}

	// Read raw bytes
	byte[] readbytes(int len, byte bytes[]) throws IOException {
		int offset = 0;
		while (len > 0) {
			int got = data.read(bytes, offset, len);
			if (got <= 0) {
				throw new IOException("Read hit EOF");
			}
			pos += got;
			checkCallback();
			offset += got;
			len -= got;
		}
		return bytes;
	}

	int callbackNext = 0;
	int callbackEnd = 0;
	int callbackGranularity = 10;

	void setCallbackEnd(int len) {
		callbackEnd = len;
		callbackNext = 0;
	}

	// Check if we should call the completion callback.
	void checkCallback() {
		/*if (callback != null && callbackEnd > 0 &&
		    pos > callbackNext) {
			// p = current percent rounded to 10%
			int p = (100*pos/callbackEnd/callbackGranularity);
			p = p*callbackGranularity;
			callback.completed(p);
			callbackNext = callbackEnd*(p+callbackGranularity)/100;
		}*/
	}

	// Seek to the specified table; returning false if it doesn't exist.
	boolean seek(int type) throws IOException {
		int off = findTableOffset(type);
		if (off == -1) {
			return false;
		} else {
			skipTo(off);
			return true;
		}
	}

	// Find the offset of the specified table, or -1 if it doesn't exist
	final int findTableOffset(int type) {
		for (int i=0; i<num_tables; i++) {
			if (table[i].type == type) {
				return table[i].offset;
			}
		}
		return -1;
	}

	CharInfo[] charInfo;
	int nbitmaps;
	int max_char;
	int[] char_map;
	byte[] bits;
	int bitmap_format;
	int defaultBitmap;

	int pos = 0;
	DataInputStream data;

	int num_tables = 0;
	PCFTableRec[] table = null;

	final static int default_format = 0x00000000, inkbounds=0x00000200,
	accel_w_inkbounds=0x00000100, compressed_metrics=0x00000100;
	final static int format_mask = 0xffffff00;
	final static int PCF_PROPERTIES=1, PCF_ACCELERATORS=2, PCF_METRICS=4,
	PCF_BITMAPS=8, PCF_INK_METRICS=16, PCF_BDF_ENCODINGS=32,
	PCF_SWIDTHS=64, PCF_GLYPH_NAMES=128, PCF_BDF_ACCELERATORS=256;
	final static int max_tables = 9;

	final static int glyph_pad_mask = 3<<0;
	final static int byte_mask = 1<<2;
	final static int MSBFirst = 1;
	final static int LSBFirst = 0;
	final static int bit_mask = 1<<3;
	final static int scan_unit_mask = 3<<4;

	final static int GLYPHPADOPTIONS = 4;


	final static int magic = 1885562369; //pcf1

	static boolean msb_bit_order(int format) {
		return (format&bit_mask) != 0;
	}

	static boolean msb_byte_order(int format) {
		return (format&byte_mask) != 0;
	}

	static int glyph_pad(int format) {
		return 1<<glyph_pad_index(format);
	}

	static int glyph_pad_index(int format) {
		return format&glyph_pad_mask;
	}

	static int scan_unit(int format) {
		return 1<<((format&scan_unit_mask)>>4);
	}

	static boolean format_match(int format, int wanted) {
		return (format&format_mask)==(wanted&format_mask);
	}

	// Read the table of contents
	final void getTable() throws IOException {
		int version = getlsb32();
		if (version != magic) {
			throw new IOException("Bad version "+version);
		}
		int count = getlsb32();
		if (count > max_tables) {
			throw new IOException("table count "+count+" too big");
		}
		table = new PCFTableRec[count];
		for (num_tables=0; num_tables<count; num_tables++) {
			table[num_tables] = new PCFTableRec();
			table[num_tables].type = getlsb32();
			table[num_tables].format = getlsb32();
			table[num_tables].size = getlsb32();
			table[num_tables].offset = getlsb32();
		}
	}

	PCFMetrics maxbounds;

	// Read the accelerators part of the font file
	// Return true if successful
	final boolean getAccelerators(int table) throws IOException {
		if (!seek(table)) {
			return false;
		}
		int format = getlsb32();
		if (!format_match(format, default_format) &&
				!format_match(format, accel_w_inkbounds)) {
			throw new IOException("Bad format for accelerator");
		}
		@SuppressWarnings("unused")
		int noOverlap = getint8(format);
		@SuppressWarnings("unused")
		int constantMetrics = getint8(format);
		@SuppressWarnings("unused")
		int terminalFont = getint8(format);
		@SuppressWarnings("unused")
		int constantWidth = getint8(format);
		@SuppressWarnings("unused")
		int inkInside = getint8(format);
		@SuppressWarnings("unused")
		int inkMetrics = getint8(format);
		@SuppressWarnings("unused")
		int drawDirection = getint8(format);
		@SuppressWarnings("unused")
		int naturalAlignment = getint8(format);
		ascent = getint32(format);
		descent = getint32(format);
		@SuppressWarnings("unused")
		int maxOverlap = getint32(format);
		PCFMetrics minbounds = getMetric(format);
		maxbounds = getMetric(format);
		@SuppressWarnings("unused")
		PCFMetrics ink_minbounds = minbounds;
		@SuppressWarnings("unused")
		PCFMetrics ink_maxbounds = maxbounds;
		if (format_match(format, accel_w_inkbounds)) {
			ink_minbounds = getMetric(format);
			ink_maxbounds = getMetric(format);
		}
		maxAscent = maxbounds.ascent;
		maxDescent = maxbounds.descent;
		checkCallback();
		return true;
	}

	// Read the PCFMetrics for a single character
	final PCFMetrics getMetric(int format) throws IOException {
		PCFMetrics metrics = new PCFMetrics();
		metrics.leftSideBearing = getint16(format);
		metrics.rightSideBearing = getint16(format);
		metrics.characterWidth = getint16(format);
		metrics.ascent = getint16(format);
		metrics.descent = getint16(format);
		getint16(format); // attributes
		return metrics;
	}

	final PCFMetrics getCompressedMetric(int format) throws
	IOException {
		PCFMetrics metrics = new PCFMetrics();
		metrics.leftSideBearing = getint8(format)-0x80;
		metrics.rightSideBearing = getint8(format)-0x80;
		metrics.characterWidth = getint8(format)-0x80;
		metrics.ascent = getint8(format)-0x80;
		metrics.descent = getint8(format)-0x80;
		return metrics;
	}

	// Read the metrics part of the font file
	final void getMetrics() throws IOException {
		if (!seek(PCF_METRICS)) {
			throw new IOException("No metrics");
		}
		int format = getlsb32();
		if (format_match(format, default_format)) {
			nbitmaps = getint32(format);
		} else if (format_match(format, compressed_metrics)) {
			nbitmaps = getint16(format);
		} else {
			throw new IOException("bad metrics format");
		}
		charInfo = new CharInfo[nbitmaps];
		for (int i=0; i<nbitmaps; i++) {
			charInfo[i] = new CharInfo();
			if (format_match(format, default_format)) {
				charInfo[i].metrics = getMetric(format);
			} else {
				charInfo[i].metrics =
					getCompressedMetric(format);
			}
			checkCallback();
		}
	}

	// Read the bitmaps
	final void getBitmaps() throws IOException {
		if (!seek(PCF_BITMAPS)) {
			throw new IOException("No metrics");
		}
		int format = getlsb32();
		if (!format_match(format, default_format)) {
			throw new IOException("bitmaps: bad bitmap format");
		}
		bitmap_format = format;
		int nbitmaps2 = getint32(format);
		if (nbitmaps != nbitmaps2) {
			throw new IOException("bitmaps and metrics mismatch");
		}
		for (int i=0; i<nbitmaps; i++) {
			charInfo[i].offset = getint32(format);
		}
		int[] bitmapSizes = new int[GLYPHPADOPTIONS];
		for (int i=0; i<GLYPHPADOPTIONS; i++) {
			bitmapSizes[i] = getint32(format);
		}
		int sizebitmaps = bitmapSizes[glyph_pad_index(format)];
		bits = new byte[sizebitmaps+scan_unit(bitmap_format)];
		bits = readbytes(sizebitmaps, bits);

	}

	// Read the encodings
	final void getEncodings()
	throws IOException {
		int firstCol, lastCol, defaultCh;
		if (!seek(PCF_BDF_ENCODINGS)) {
			throw new IOException("No metrics");
		}
		int format = getlsb32();
		if (!format_match(format, default_format)) {
			throw new IOException("encodings: bad bitmap format");
		}
		firstCol = getint16(format);
		lastCol = getint16(format);
		firstRow = getint16(format);
		lastRow = getint16(format);
		defaultCh = getint16(format);
		@SuppressWarnings("unused")
		int nencoding = (lastCol-firstCol+1)*(lastRow-firstRow+1);
		if (lastRow>0) {
			max_char = 65536;
		} else {
			max_char = 256;
		}
		char_map = new int[max_char];
		for (int i=0;i<max_char;i++) {
			char_map[i] = 0xffff;
		}
		for (int r=firstRow; r<=lastRow; r++) {
			for (int c=firstCol; c<=lastCol; c++) {
				int map = getint16(format);
				if (map != 0xffff) {
					char_map[(r<<8)|c] = map;
				}
			}
			checkCallback();
		}
		defaultBitmap = char_map[defaultCh];
		if (defaultBitmap == 0xffff) {
			defaultBitmap = 0;
		}
		for (int i=0;i<max_char;i++) {
			if (char_map[i] == 0xffff) {
				char_map[i] = defaultBitmap;
			}
		}
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
		int height = charInfo[num].metrics.ascent +
		charInfo[num].metrics.descent;
		int width = -charInfo[num].metrics.leftSideBearing+
		charInfo[num].metrics.rightSideBearing;
		if (height<0 || width<0) {
			log.severe("Negative height/width for "+num);
			return;
		}
		int offset = charInfo[num].offset;
		int stride = ((width+7)>>3);
		stride = (stride+glyphpad-1)&~(glyphpad-1);
		for (int i=0;i<height;i++) {
			for (int j=0; j<width; j++) {
				int bit_pos = (j&7)^bitflip;
				int byte_pos = j>>3;
			if ((bits[offset+
			          ((byte_pos+i*stride)^byteflip)]&
			          (1<<bit_pos)) != 0) {
				buf[bufoffset+i*span+j] = fgColor;
			}
			}
		}
	}

}

// A PCF record specifying a table
class PCFTableRec {
	int type;
	int format;
	int size;
	int offset;
}

class CharInfo {
	PCFMetrics metrics;
	int offset; // into the bit table
}
