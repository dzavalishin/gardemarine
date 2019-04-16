/*
 * MetricsImage
 *	  Object containing metrics and an image.
 *
 * Ken Shirriff	 ken.shirriff@eng.sun.com
 *
 * Copyright (c) 1997 Sun Microsystems, Inc. All Rights Reserved.
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

import java.awt.Image;
import java.util.logging.Logger;


class MetricsImage {
    @SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(MetricsImage.class.getName()); 

    PCFMetrics metrics;
	Image image;
	MetricsImage(PCFMetrics m, Image i) {
		metrics = m;
		image = i;
	}
}
