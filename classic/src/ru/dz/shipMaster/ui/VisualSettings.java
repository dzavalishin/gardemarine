package ru.dz.shipMaster.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Used to configure color scheme. Select base class representing a color scheme needed.
 * @author dz
 */

public class VisualSettings
	//extends ColorSetup_RedBlackColors {
	extends ColorSetup_GreenBlackColors {
	//extends ColorSetup_GreenBlackWhiteColors {

    @SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(VisualSettings.class.getName());

    
    //private VisualSettings() {}

/* 
 * NB! No content permitted!
 * Select base class.
 */
	
	// TO DO: this method can select settings by callee class name using reflections
	
	/** This object is needed to access colors from static context */
	static final public VisualSettings global = new VisualSettings();
	
	
	private static float [] hsbvalsC = new float[3];
	private static float [] hsbvalsW = new float[3];


	static {
		Color.RGBtoHSB(global.warnColor.getRed(), global.warnColor.getGreen(), global.warnColor.getBlue(), hsbvalsW);
		Color.RGBtoHSB(global.critColor.getRed(), global.critColor.getGreen(), global.critColor.getBlue(), hsbvalsC);
	}

	public static final float warnHue = hsbvalsW[0];
	public static final float critHue = hsbvalsC[0];
	
	
/*	
	private static boolean globalExtendHeight = false;
	private static boolean globalExtendWidth = false;
	
	public static boolean isGlobalExtendHeight() {		return globalExtendHeight;	}
	public static boolean isGlobalExtendWidth() {		return globalExtendWidth ;	}
	
	public static void setGlobalExtendHeight(boolean globalExtendHeight) {		VisualSettings.globalExtendHeight = globalExtendHeight;	}
	public static void setGlobalExtendWidth(boolean globalExtendWidth) {		VisualSettings.globalExtendWidth = globalExtendWidth;	}
*/





	
}





class ColorSetup_GreenBlackWhiteColors extends ColorSetup_BasicColors
{
	// This color scheme's specific colors
	
	public static final Color STYLE_RED = new Color(0xEE1111);
	public static final Color STYLE_DARK_RED = new Color(0xA70C0C);
	public static final Color STYLE_BLACK = new Color(0x050505); 
	public static final Color STYLE_GLIMPSE = new Color(0x3A3A3A);
	public static final Color STYLE_LIGHT_GREEN = new Color(0x55FF59);
	public static final Color STYLE_GREEN = new Color(0x11EE11);
	public static final Color STYLE_DARK_GREEN = new Color(0x0ca729);
	public static final Color STYLE_HALFBLUE = new Color(0xDDAACCEE,true); 

	// Colors by function - general
	
	public static final Color BUTTON_SHADOW_COLOR = STYLE_GREEN;
	public static final Color BUTTON_DARK_SHADOW_COLOR = STYLE_DARK_GREEN;
	public static final Color BUTTON_HIGHLIGHT_COLOR = STYLE_LIGHT_GREEN;
	public static final Color BUTTON_LIGHT_HIGHLIGHT_COLOR = STYLE_HALFBLUE;

	
	/** 'lowest' background - 'panel' */
	public static final Color METER_BACKGROUND_COLOR = STYLE_GLIMPSE;
	
	/** meter's workspace (scale, or what it has) background */
	public static final Color METER_SCALE_COLOR = STYLE_BLACK;
	
	/** meter's workspace (scale, or what it has) glimpse, where appropriate */
	public static final Color METER_SCALE_GLIMPSE_COLOR = STYLE_GLIMPSE;
	
	/** value ticks color */
	//public static final Color METER_TICK_COLOR = STYLE_GREEN;
	public static final Color METER_TICK_COLOR = Color.WHITE;

	/** Histogram colors */
	public static final Color METER_HISTOGRAM_COLOR = STYLE_HALFBLUE; //STYLE_GREEN; // LIGHT_YELLOW_COLOR;
	
	/** indicator - hand, active part of bar, etc */
	//public static final Color METER_INDICATOR_COLOR = STYLE_DARK_GREEN;
	public static final Color METER_INDICATOR_COLOR = Color.WHITE;

	
	/** lit pictogram - trivial */
	public static final Color METER_TRIVIAL_PICTOGRAM_COLOR = STYLE_HALFBLUE;

	/** lit pictogram - noncritical one */
	public static final Color METER_NORMAL_PICTOGRAM_COLOR = STYLE_LIGHT_GREEN;
	
	/** lit pictogram - warning */
	public static final Color METER_WARNING_PICTOGRAM_COLOR = LIGHT_YELLOW_COLOR;

	/** lit pictogram - critical one */
	public static final Color METER_CRITICAL_PICTOGRAM_COLOR = STYLE_RED;

	
	/** line or mark, representing warning level */
	public static final Color WARNING_LINE_COLOR = LIGHT_YELLOW_COLOR;
	
	/** line or mark, representing critical level */
	public static final Color CRITICAL_LINE_COLOR = STYLE_RED;

	/** tick or other label */
	//public static final Color METER_LABEL_COLOR = LIGHT_YELLOW_COLOR;
	public static final Color METER_LABEL_COLOR = Color.WHITE;

	/** label background - not that every label has it */
	public static final Color METER_BG_LABEL_COLOR = 
		new Color( 
				(0x0FFFFFF&Color.DARK_GRAY.getRGB()) | 0xCC000000  
				,true); 
	
	
	
	@Override
	public void setPersonal_4_HalfGaugeMeter() 
	{
	}	
	
}









class ColorSetup_GreenBlackColors extends ColorSetup_BasicColors
{
	// This color scheme's specific colors
	
	public static final Color STYLE_RED = new Color(0xEE1111);
	public static final Color STYLE_DARK_RED = new Color(0xA70C0C);
	public static final Color STYLE_BLACK = new Color(0x050505); 
	public static final Color STYLE_GLIMPSE = new Color(0x3A3A3A);
	public static final Color STYLE_LIGHT_GREEN = new Color(0x55FF59);
	public static final Color STYLE_GREEN = new Color(0x11EE11);
	public static final Color STYLE_DARK_GREEN = new Color(0x0ca729);
	public static final Color STYLE_HALFBLUE = new Color(0xDDAACCEE,true); 

	// Colors by function - general
	
	public static final Color BUTTON_SHADOW_COLOR = STYLE_GREEN;
	public static final Color BUTTON_DARK_SHADOW_COLOR = STYLE_DARK_GREEN;
	public static final Color BUTTON_HIGHLIGHT_COLOR = STYLE_LIGHT_GREEN;
	public static final Color BUTTON_LIGHT_HIGHLIGHT_COLOR = STYLE_HALFBLUE;

	
	/** 'lowest' background - 'panel' */
	public static final Color METER_BACKGROUND_COLOR = STYLE_GLIMPSE;
	
	/** meter's workspace (scale, or what it has) background */
	public static final Color METER_SCALE_COLOR = STYLE_BLACK;
	
	/** meter's workspace (scale, or what it has) glimpse, where appropriate */
	public static final Color METER_SCALE_GLIMPSE_COLOR = STYLE_GLIMPSE;
	
	/** value ticks color */
	public static final Color METER_TICK_COLOR = STYLE_GREEN;

	/** Histogram colors */
	public static final Color METER_HISTOGRAM_COLOR = STYLE_HALFBLUE; //STYLE_GREEN; // LIGHT_YELLOW_COLOR;
	
	/** indicator - hand, active part of bar, etc */
	public static final Color METER_INDICATOR_COLOR = STYLE_DARK_GREEN;

	
	/** lit pictogram - trivial */
	public static final Color METER_TRIVIAL_PICTOGRAM_COLOR = STYLE_HALFBLUE;

	/** lit pictogram - noncritical one */
	public static final Color METER_NORMAL_PICTOGRAM_COLOR = STYLE_LIGHT_GREEN;
	
	/** lit pictogram - warning */
	public static final Color METER_WARNING_PICTOGRAM_COLOR = LIGHT_YELLOW_COLOR;

	/** lit pictogram - critical one */
	public static final Color METER_CRITICAL_PICTOGRAM_COLOR = STYLE_RED;

	
	/** line or mark, representing warning level */
	public static final Color WARNING_LINE_COLOR = LIGHT_YELLOW_COLOR;
	
	/** line or mark, representing critical level */
	public static final Color CRITICAL_LINE_COLOR = STYLE_RED;

	/** tick or other label */
	public static final Color METER_LABEL_COLOR = LIGHT_YELLOW_COLOR;

	/** label background - not that every label has it */
	public static final Color METER_BG_LABEL_COLOR = 
		new Color( 
				(0x0FFFFFF&Color.DARK_GRAY.getRGB()) | 0xCC000000  
				,true); 
	
	
	
	@Override
	public void setPersonal_4_HalfGaugeMeter() 
	{
	}	
	
}




class ColorSetup_RedBlackColors extends ColorSetup_BasicColors
{
	// This color scheme's specific colors
	
	public static final Color STYLE_RED = new Color(0xEE1111);
	public static final Color STYLE_DARK_RED = new Color(0xA70C0C);
	public static final Color STYLE_BLACK = new Color(0x050505); 
	public static final Color STYLE_GLIMPSE = new Color(0x3A3A3A);
	public static final Color STYLE_GREEN = new Color(0x55FF59);
	public static final Color STYLE_HALFBLUE = new Color(0xDDAACCEE,true); 

	// Colors by function - general
	
	/** 'lowest' background - 'panel' */
	public static final Color METER_BACKGROUND_COLOR = STYLE_GLIMPSE;
	
	/** meter's workspace (scale, or what it has) background */
	public static final Color METER_SCALE_COLOR = STYLE_BLACK;
	
	/** meter's workspace (scale, or what it has) glimpse, where appropriate */
	public static final Color METER_SCALE_GLIMPSE_COLOR = STYLE_GLIMPSE;
	
	/** value ticks color */
	public static final Color METER_TICK_COLOR = STYLE_DARK_RED;

	/** Histogram colors */
	public static final Color METER_HISTOGRAM_COLOR = STYLE_HALFBLUE; //STYLE_GREEN; // LIGHT_YELLOW_COLOR;
	
	/** indicator - hand, active part of bar, etc */
	public static final Color METER_INDICATOR_COLOR = STYLE_RED;

	
	/** lit pictogram - trivial */
	public static final Color METER_TRIVIAL_PICTOGRAM_COLOR = STYLE_HALFBLUE;

	/** lit pictogram - noncritical one */
	public static final Color METER_NORMAL_PICTOGRAM_COLOR = STYLE_GREEN;
	
	/** lit pictogram - warning */
	public static final Color METER_WARNING_PICTOGRAM_COLOR = LIGHT_YELLOW_COLOR;

	/** lit pictogram - critical one */
	public static final Color METER_CRITICAL_PICTOGRAM_COLOR = STYLE_RED;

	
	/** line or mark, representing warning level */
	public static final Color WARNING_LINE_COLOR = LIGHT_YELLOW_COLOR;
	
	/** line or mark, representing critical level */
	public static final Color CRITICAL_LINE_COLOR = MID_RED_COLOR;

	/** tick or other label */
	public static final Color METER_LABEL_COLOR = LIGHT_YELLOW_COLOR;

	/** label background - not that every label has it */
	public static final Color METER_BG_LABEL_COLOR = 
		new Color( 
				(0x0FFFFFF&Color.DARK_GRAY.getRGB()) | 0xCC000000  
				,true); 
	
	
//	personal for specific kinds of meters, 
	
	@Override
	public void setPersonal_4_HalfGaugeMeter() 
	{
	}	
	
}






class ColorSetup_ChryslerColors extends ColorSetup_BasicColors
{
	// This color scheme's specific colors
	
	//public static final Color CHRYSLER_DARK_GREEN = new Color(0x3c9285);
	public static final Color CHRYSLER_DARK_GREEN = new Color(0x508d74); //0x31766b);
	public static final Color CHRYSLER_LIGHT_GREEN = new Color(0x7fdfb7); //0x5cdfcb);

	// Colors by function - general
	
	/** 'lowest' background - 'panel' */
	public static final Color METER_BACKGROUND_COLOR = BLACK_COLOR;
	
	/** meter's workspace (scale, or what it has) background */
	public static final Color METER_SCALE_COLOR = CHRYSLER_DARK_GREEN;
	
	/** value ticks color */
	public static final Color METER_TICK_COLOR = BLACK_COLOR;

	/** Histogram colors */
	public static final Color METER_HISTOGRAM_COLOR = CHRYSLER_LIGHT_GREEN; // LIGHT_YELLOW_COLOR;
	//public static final Color METER_HISTOGRAM_COLOR = new Color(0x11BB11);
	
	/** indicator - hand, active part of bar, etc */
	public static final Color METER_INDICATOR_COLOR = BLACK_COLOR;

	/** lit pictogram - noncritical one */
	public static final Color METER_NORMAL_PICTOGRAM_COLOR = CHRYSLER_LIGHT_GREEN;
	
	/** lit pictogram - critical one */
	public static final Color METER_CRITICAL_PICTOGRAM_COLOR = MID_RED_COLOR;
	
	/** line or mark, representing warning level */
	public static final Color WARNING_LINE_COLOR = LIGHT_YELLOW_COLOR;
	
	/** line or mark, representing critical level */
	public static final Color CRITICAL_LINE_COLOR = MID_RED_COLOR;

	/** tick or other label */
	public static final Color METER_LABEL_COLOR = METER_TICK_COLOR;

	/** label background - not that every label has it */
	public static final Color METER_BG_LABEL_COLOR = Color.DARK_GRAY;
	
	
//	personal for specific kinds of meters, 
	
	@Override
	public void setPersonal_4_HalfGaugeMeter() 
	{
		bgColor = METER_SCALE_COLOR;
	}	
	
}

class CororSetup_ColdColors extends ColorSetup_BasicColors {

	// Colors by function - general
	
	/** 'lowest' background - 'panel' */
	public static final Color METER_BACKGROUND_COLOR = BLACK_COLOR;
	
	/** meter's workspace (scale, or what it has) background */
	public static final Color METER_SCALE_COLOR = DARK_BLUEBERRY_COLOR;
	
	/** value ticks color */
	public static final Color METER_TICK_COLOR = BLACK_COLOR;

	/** Histogram colors */
	public static final Color METER_HISTOGRAM_COLOR = LIGHT_YELLOW_COLOR;
	//public static final Color METER_HISTOGRAM_COLOR = new Color(0x11BB11);
	
	/** indicator - hand, active part of bar, etc */
	public static final Color METER_INDICATOR_COLOR = LIGHT_BLUEBERRY_COLOR;

	/** line or mark, representing warning level */
	public static final Color WARNING_LINE_COLOR = LIGHT_YELLOW_COLOR;
	
	/** line or mark, representing critical level */
	public static final Color CRITICAL_LINE_COLOR = MID_RED_COLOR;

	/** tick or other label */
	public static final Color METER_LABEL_COLOR = Color.LIGHT_GRAY;

	/** label background - not that every label has it */
	public static final Color METER_BG_LABEL_COLOR = Color.DARK_GRAY;

	
	
//	personal for specific kinds of meters, 

	
	@Override
	public void setPersonal_4_LinearVerticalMeter()
	{
		//scaleColor = METER_BACKGROUND_COLOR;
		classScaleColor.put("LinearVerticalMeter", METER_BACKGROUND_COLOR);
	}

	@Override
	public void setPersonal_4_CompassMeter()	
	{
		tickColor = (LIGHT_YELLOW_COLOR);
		//indicatorColor = (Color.DARK_GRAY);
		classIndicatorColor.put("CompassMeter", Color.DARK_GRAY);
	}
	
	@Override
	public void setPersonal_4_RulerMeter() 
	{
		//m.setBgColor(LIGHT_YELLOW_COLOR);

		//scaleColor = (LIGHT_YELLOW_COLOR);
		classScaleColor.put("RulerMeter", LIGHT_YELLOW_COLOR);

		
		//indicatorColor = (DARK_BLUEBERRY_COLOR);
		classIndicatorColor.put("RulerMeter", DARK_BLUEBERRY_COLOR);
		
		labelColor = (METER_TICK_COLOR);
	}	

	@Override
	public void setPersonal_4_HalfGaugeMeter() 
	{
		//indicatorColor = (LIGHT_BLUEBERRY_COLOR);
		classIndicatorColor.put("HalfGaugeMeter", LIGHT_BLUEBERRY_COLOR);
	}	
	
}

abstract class ColorSetup_BasicColors 
//extends VisualHelpers 
{

	// Colors by color
	
	public static final Color BLACK_COLOR = Color.BLACK;

	public static final Color LIGHT_BLUEBERRY_COLOR = new Color(0x7a96df);
	public static final Color DARK_BLUEBERRY_COLOR = new Color(0x596ea3);
	
	public static final Color LIGHT_YELLOW_COLOR = new Color(0xf0e78c);
	public static final Color PALE_RED_COLOR = new Color(0x773333);
	public static final Color MID_RED_COLOR = new Color(0xAA3333);
	
	public static final Color COMPONENT_BORDER_COLOR = new Color(0x525145);
	
	public void setPersonal_4_RunningLine() {
		bgColor = Color.BLACK;		
	} 

	public void setPersonal_4_GeneralLinearMeter() {
		indicatorColor = transparentIndicatorColor;
	}

	public void setPersonal_4_LinearVerticalMeter()	{}
	public void setPersonal_4_CompassMeter()	{}
	public void setPersonal_4_RulerMeter() {}	
	public void setPersonal_4_HalfGaugeMeter() {}


	// variables are specific for each instance. Each component has an instance.

	public static final BasicStroke PLAIN_STROKE = new BasicStroke( 1 );
	
	
	public Color			borderColor = VisualSettings.COMPONENT_BORDER_COLOR;
	
	public Color			bgColor = VisualSettings.METER_BACKGROUND_COLOR;

	public Color			labelColor = VisualSettings.METER_LABEL_COLOR;
	public Color			labelBgColor = VisualSettings.METER_BG_LABEL_COLOR;
	
	public Color 			transparentLabelColor = new Color(
			0x22000000 | (labelColor.getRGB() & 0x00FFFFFF) ,true);

	public Color			pictogramNormalColor = VisualSettings.METER_NORMAL_PICTOGRAM_COLOR;
	public Color			pictogramCriticalColor = VisualSettings.METER_CRITICAL_PICTOGRAM_COLOR;
	public Color			pictogramWarningColor = VisualSettings.METER_WARNING_PICTOGRAM_COLOR;
	public Color			pictogramTrivialColor = VisualSettings.METER_TRIVIAL_PICTOGRAM_COLOR;
	
	public Color			warnColor = VisualSettings.WARNING_LINE_COLOR; 
	public Color			critColor = VisualSettings.CRITICAL_LINE_COLOR;

	public Color			scaleGlimpseColor = VisualSettings.METER_SCALE_GLIMPSE_COLOR;
	public Color			tickColor = VisualSettings.METER_TICK_COLOR;
	public Color			histogramColor = VisualSettings.METER_HISTOGRAM_COLOR;

	public Color 			digitalMeterColor = VisualSettings.STYLE_HALFBLUE;

	public Color 			cupColor = new Color(0xFF111111);

	public Color 			bitFontBgColor = new Color(0x66000000,true); 
	public Color 			bitFontBitOffColor = new Color(0x44444444,true);
	public Color 			bitFontBitOnColor = new Color(0xDDAACCEE,true); 
	
	public Color 			panelBgColor = Color.BLACK;

	
	
	
	// -------------------------------------------------------------------
	// These getters are for use by actual components
	// getClass___Color( this.getClass().getSimpleName() )
	// -------------------------------------------------------------------

	private Color			indicatorColor = VisualSettings.METER_INDICATOR_COLOR;
	protected Map<String,Color> classIndicatorColor = new HashMap<String, Color>(); 
	
	public Color getClassIndicatorColor( String className )
	{
		Color specific = classIndicatorColor.get(className);
		if(specific != null) return specific;
		
		return indicatorColor;
	}
	
	
	
	
	
	private Color			scaleColor = VisualSettings.METER_SCALE_COLOR;
	protected Map<String,Color> classScaleColor = new HashMap<String, Color>(); 

	public Color getClassScaleColor( String className )
	{
		Color specific = classScaleColor.get(className);
		if(specific != null) return specific;
		
		return scaleColor;
	}
	
	

	public Color 			transparentIndicatorColor = new Color( (0x00FFFFFF&VisualSettings.METER_INDICATOR_COLOR.getRGB()) | 0x55000000, true);;
	public Color 			indicatorHighlightColor = scaleColor;

	
	// -------------------------------------------------------------------
	// These getters/setters are for serializer
	// -------------------------------------------------------------------
	
	
	
	
	/**
	 * @return the borderColor
	 */
	public final Color getBorderColor() {
		return borderColor;
	}

	/**
	 * @param borderColor the borderColor to set
	 */
	public final void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}

	/**
	 * @return the bgColor
	 */
	public final Color getBgColor() {
		return bgColor;
	}

	/**
	 * @param bgColor the bgColor to set
	 */
	public final void setBgColor(Color bgColor) {
		this.bgColor = bgColor;
	}

	/**
	 * @return the labelColor
	 */
	public final Color getLabelColor() {
		return labelColor;
	}

	/**
	 * @param labelColor the labelColor to set
	 */
	public final void setLabelColor(Color labelColor) {
		this.labelColor = labelColor;
	}

	/**
	 * @return the labelBgColor
	 */
	public final Color getLabelBgColor() {
		return labelBgColor;
	}

	/**
	 * @param labelBgColor the labelBgColor to set
	 */
	public final void setLabelBgColor(Color labelBgColor) {
		this.labelBgColor = labelBgColor;
	}

	/**
	 * @return the transparentLabelColor
	 */
	public final Color getTransparentLabelColor() {
		return transparentLabelColor;
	}

	/**
	 * @param transparentLabelColor the transparentLabelColor to set
	 */
	public final void setTransparentLabelColor(Color transparentLabelColor) {
		this.transparentLabelColor = transparentLabelColor;
	}

	/**
	 * @return the pictogramNormalColor
	 */
	public final Color getPictogramNormalColor() {
		return pictogramNormalColor;
	}

	/**
	 * @param pictogramNormalColor the pictogramNormalColor to set
	 */
	public final void setPictogramNormalColor(Color pictogramNormalColor) {
		this.pictogramNormalColor = pictogramNormalColor;
	}

	/**
	 * @return the pictogramCriticalColor
	 */
	public final Color getPictogramCriticalColor() {
		return pictogramCriticalColor;
	}

	/**
	 * @param pictogramCriticalColor the pictogramCriticalColor to set
	 */
	public final void setPictogramCriticalColor(Color pictogramCriticalColor) {
		this.pictogramCriticalColor = pictogramCriticalColor;
	}

	/**
	 * @return the pictogramWarningColor
	 */
	public final Color getPictogramWarningColor() {
		return pictogramWarningColor;
	}

	/**
	 * @param pictogramWarningColor the pictogramWarningColor to set
	 */
	public final void setPictogramWarningColor(Color pictogramWarningColor) {
		this.pictogramWarningColor = pictogramWarningColor;
	}

	/**
	 * @return the pictogramTrivialColor
	 */
	public final Color getPictogramTrivialColor() {
		return pictogramTrivialColor;
	}

	/**
	 * @param pictogramTrivialColor the pictogramTrivialColor to set
	 */
	public final void setPictogramTrivialColor(Color pictogramTrivialColor) {
		this.pictogramTrivialColor = pictogramTrivialColor;
	}

	/**
	 * @return the warnColor
	 */
	public final Color getWarnColor() {
		return warnColor;
	}

	/**
	 * @param warnColor the warnColor to set
	 */
	public final void setWarnColor(Color warnColor) {
		this.warnColor = warnColor;
	}

	/**
	 * @return the critColor
	 */
	public final Color getCritColor() {
		return critColor;
	}

	/**
	 * @param critColor the critColor to set
	 */
	public final void setCritColor(Color critColor) {
		this.critColor = critColor;
	}

	/**
	 * @return the scaleColor
	 */
	public final Color getScaleColor() {
		return scaleColor;
	}

	/**
	 * @param scaleColor the scaleColor to set
	 */
	public final void setScaleColor(Color scaleColor) {
		this.scaleColor = scaleColor;
	}

	/**
	 * @return the scaleGlimpseColor
	 */
	public final Color getScaleGlimpseColor() {
		return scaleGlimpseColor;
	}

	/**
	 * @param scaleGlimpseColor the scaleGlimpseColor to set
	 */
	public final void setScaleGlimpseColor(Color scaleGlimpseColor) {
		this.scaleGlimpseColor = scaleGlimpseColor;
	}

	/**
	 * @return the tickColor
	 */
	public final Color getTickColor() {
		return tickColor;
	}

	/**
	 * @param tickColor the tickColor to set
	 */
	public final void setTickColor(Color tickColor) {
		this.tickColor = tickColor;
	}

	/**
	 * @return the histogramColor
	 */
	public final Color getHistogramColor() {
		return histogramColor;
	}

	/**
	 * @param histogramColor the histogramColor to set
	 */
	public final void setHistogramColor(Color histogramColor) {
		this.histogramColor = histogramColor;
	}

	/**
	 * @return the indicatorColor
	 */
	public final Color getIndicatorColor() {
		return indicatorColor;
	}

	/**
	 * @param indicatorColor the indicatorColor to set
	 */
	public final void setIndicatorColor(Color indicatorColor) {
		this.indicatorColor = indicatorColor;
	}

	/**
	 * @return the transparentIndicatorColor
	 */
	public final Color getTransparentIndicatorColor() {
		return transparentIndicatorColor;
	}

	/**
	 * @param transparentIndicatorColor the transparentIndicatorColor to set
	 */
	public final void setTransparentIndicatorColor(Color transparentIndicatorColor) {
		this.transparentIndicatorColor = transparentIndicatorColor;
	}

	/**
	 * @return the indicatorHighlightColor
	 */
	public final Color getIndicatorHighlightColor() {
		return indicatorHighlightColor;
	}

	/**
	 * @param indicatorHighlightColor the indicatorHighlightColor to set
	 */
	public final void setIndicatorHighlightColor(Color indicatorHighlightColor) {
		this.indicatorHighlightColor = indicatorHighlightColor;
	}

	/**
	 * @return the digitalMeterColor
	 */
	public final Color getDigitalMeterColor() {
		return digitalMeterColor;
	}

	/**
	 * @param digitalMeterColor the digitalMeterColor to set
	 */
	public final void setDigitalMeterColor(Color digitalMeterColor) {
		this.digitalMeterColor = digitalMeterColor;
	}

	/**
	 * @return the cupColor
	 */
	public final Color getCupColor() {
		return cupColor;
	}

	/**
	 * @param cupColor the cupColor to set
	 */
	public final void setCupColor(Color cupColor) {
		this.cupColor = cupColor;
	}

	/**
	 * @return the bitFontBgColor
	 */
	public final Color getBitFontBgColor() {
		return bitFontBgColor;
	}

	/**
	 * @param bitFontBgColor the bitFontBgColor to set
	 */
	public final void setBitFontBgColor(Color bitFontBgColor) {
		this.bitFontBgColor = bitFontBgColor;
	}

	/**
	 * @return the bitFontBitOffColor
	 */
	public final Color getBitFontBitOffColor() {
		return bitFontBitOffColor;
	}

	/**
	 * @param bitFontBitOffColor the bitFontBitOffColor to set
	 */
	public final void setBitFontBitOffColor(Color bitFontBitOffColor) {
		this.bitFontBitOffColor = bitFontBitOffColor;
	}

	/**
	 * @return the bitFontBitOnColor
	 */
	public final Color getBitFontBitOnColor() {
		return bitFontBitOnColor;
	}

	/**
	 * @param bitFontBitOnColor the bitFontBitOnColor to set
	 */
	public final void setBitFontBitOnColor(Color bitFontBitOnColor) {
		this.bitFontBitOnColor = bitFontBitOnColor;
	}

	public final Color getPanelBgColor() {
		return panelBgColor;
	}

	/**
	 * 
	 * Panel background color - WHAT IS IT?
	 * 
	 * @param panelBgColor the panelBgColor to set
	 */
	public final void setPanelBgColor(Color panelBgColor) {
		this.panelBgColor = panelBgColor;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}