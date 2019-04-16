package ru.dz.shipMaster.ui.render;

import java.awt.Color;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.util.logging.Logger;

public class RoundGradientPaint implements Paint {
    @SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(RoundGradientPaint.class.getName()); 


    protected Point2D point;

    protected Point2D mRadius;

    protected Color mPointColor;
    protected Color mBackgroundColor;

    public RoundGradientPaint(double x, double y, Color pointColor,
        Point2D radius, Color backgroundColor) {
      if (radius.distance(0, 0) <= 0)
        throw new IllegalArgumentException("Radius must be greater than 0.");
      point = new Point2D.Double(x, y);
      mPointColor = pointColor;
      mRadius = radius;
      mBackgroundColor = backgroundColor;
    }

    public PaintContext createContext(ColorModel cm, Rectangle deviceBounds,
        Rectangle2D userBounds, AffineTransform xform, RenderingHints hints) {
      Point2D transformedPoint = xform.transform(point, null);
      Point2D transformedRadius = xform.deltaTransform(mRadius, null);
      return new RoundGradientContext(transformedPoint, mPointColor,
          transformedRadius, mBackgroundColor);
    }

    public int getTransparency() {
      int a1 = mPointColor.getAlpha();
      int a2 = mBackgroundColor.getAlpha();
      return (((a1 & a2) == 0xff) ? OPAQUE : TRANSLUCENT);
    }
  }
