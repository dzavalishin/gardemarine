package ru.dz.shipMaster.config.items;

import java.awt.Dimension;
import java.awt.Point;
import java.util.logging.Level;

import ru.dz.shipMaster.config.ConfigListItem;
import ru.dz.shipMaster.config.ConfigurationFactory;
import ru.dz.shipMaster.ui.DashComponent;
import ru.dz.shipMaster.ui.component.DashImage;
import ru.dz.shipMaster.ui.config.item.CieInstrument;
import ru.dz.shipMaster.ui.control.DashButton;
import ru.dz.shipMaster.ui.logger.LogWindow;
import ru.dz.shipMaster.ui.logger.MessageWindow;
import ru.dz.shipMaster.ui.meter.GeneralMeter;
import ru.dz.shipMaster.ui.meter.IInstrumentWithDigits;
import ru.dz.shipMaster.ui.meter.IInstrumentWithFont;
import ru.dz.shipMaster.ui.meter.IInstrumentWithMessages;
import ru.dz.shipMaster.ui.meter.IInstrumentWithRoundScale;
import ru.dz.shipMaster.ui.meter.IInstrumentWithTicks;
import ru.dz.shipMaster.ui.misc.ClassCache;

public class CliInstrument extends ConfigListItem {
	private String name = "New";

	private DashComponent	unusedComponent;
	private String instrumentClassName = null;

	private double valueMinimum = 0;
	private double valueMaximum = 100;
	private String legend = "?";
	private String units = "?";

	private String imageShortName;
	private String onMessage;
	private String offMessage;

	private Dimension preferredSize = null; 
	
	private int nMinorTicks = 0;
	private int nMajorTicks = -1;

	private double baseRotationAngle = -135;
	private double scaleAngle = 270;

	private double verticalWeight = 1;
	private double horizontalWeight = 1;

	private boolean histogramVisible = true;
	private boolean pictogramMessage = false;
	
	private int nDigits = 2;
	private int fontSize = -1;
	
	private CliButtonGroup buttonGroup = null;
	
	private CliParameter	parameter;

	
	@Override
	public void destroy() {
		parameter = null;
		unusedComponent = null;
		dialog = null;			
	}

	
	
	//public boolean isMeter() { return unusedComponent instanceof GeneralMeter; }

	public DashComponent createNewInstance()
	{
		// XXX HACK!

		DashComponent nc = instantiateInstrument(instrumentClassName);
		if(nc == null)
		{
			return null;
		}

		boolean isMeter = (nc instanceof GeneralMeter);
		//boolean isPictogram = (nc instanceof GeneralPictogram);
		boolean isImage = nc instanceof DashImage;
		boolean isInstrumentWithMessages = nc instanceof IInstrumentWithMessages;

		if(preferredSize != null)
			nc.setPreferredSize(preferredSize);
		
		if (isMeter) {
			GeneralMeter gm = (GeneralMeter) nc;
			gm.setMinimum(valueMinimum);
			gm.setMaximum(valueMaximum);
			gm.setLegend(legend);
			gm.setUnits(units);
			gm.setHistogramVisible(histogramVisible);
		}

		if (isInstrumentWithMessages) {
			IInstrumentWithMessages gp = (IInstrumentWithMessages) nc;
			gp.setOffMessage(offMessage);
			gp.setOnMessage(onMessage);
			gp.setDrawMessage(pictogramMessage);
		}

		if(isImage)
		{
			DashImage di = (DashImage) nc;
			di.setPictogramFileName(imageShortName);
		}

		/*if(component instanceof ClinoMeter)
		{
			ClinoMeter cl = (ClinoMeter)nc;
			cl.setPictogramFileName(imageShortName);
		}*/

		if (nc instanceof DashImage) 
		{
			DashImage di = (DashImage) nc;
			di.setPictogramFileName(imageShortName);
		}

		// dashimage does it
		/*if (component instanceof ClinoMeter) {
			ClinoMeter cl = (ClinoMeter) component;
			cl.setPictogramFileName(imageShortName);
		}*/

		if( nc instanceof IInstrumentWithFont )
		{
			IInstrumentWithFont m = (IInstrumentWithFont)nc;
			if( fontSize >= 0 ) m.setFontSize(fontSize);
		}

		if( nc instanceof IInstrumentWithTicks )
		{
			IInstrumentWithTicks m = (IInstrumentWithTicks)nc;
			if( nMinorTicks >= 0 ) m.setNumMinorTicks(nMinorTicks);
			if( nMajorTicks >= 0 ) m.setNumNumbers(nMajorTicks);
		}

		if (nc instanceof IInstrumentWithRoundScale) {
			IInstrumentWithRoundScale rs = (IInstrumentWithRoundScale) nc;
			rs.setBaseRotationAngle(baseRotationAngle);
			rs.setScaleAngle(scaleAngle);
		}
		
		if (nc instanceof DashButton) {
			DashButton b = (DashButton) nc;
			b.setButtonText(legend);
			b.setButtonGroup(buttonGroup);
		}
		
		if(nc instanceof IInstrumentWithDigits)
		{
			IInstrumentWithDigits i = (IInstrumentWithDigits)nc;
			i.setNDigits(nDigits);
		}
		
		return nc;
	}

	private CieInstrument dialog = null;

	@Override
	public void displayPropertiesDialog() {
		if(!hasEditRight()) return;
		if(dialog == null) dialog = new CieInstrument(this);
		dialog.displayPropertiesDialog(); 
	}

	// reflections

	//private static DashBoard testDashBoard = new DashBoard();

	static ClassCache classCache = new ClassCache();
	
	public static DashComponent instantiateInstrument(String iClassName) {
		if(iClassName == null) return null;
		
		try {
			//Class<? extends DashComponent> iClass = (Class<? extends DashComponent>) Class.forName(iClassName);
			
			Class<? extends DashComponent> iClass = ClassCache.get(iClassName);

			// XXX HACK!
			if(iClass.equals(LogWindow.class))
				return ConfigurationFactory.getTransientState().getDashBoard().getLogWindow(); 

			if(iClass.equals(MessageWindow.class))
				return ConfigurationFactory.getTransientState().getDashBoard().getMessageWindow(); 

			/*if(false)
			{
				//Class<? extends DashComponent>[] parameterTypes = new Class<GeneralMeter>[1];
				Class[] parameterTypes = new Class[1];
				parameterTypes[0] = DashBoard.class; 
				//DashBoard

				Constructor<? extends DashComponent> constructor = iClass.getConstructor(parameterTypes);

				Object[] initargs = new Object[1];
				initargs[0] = testDashBoard;
				DashComponent instrument = constructor.newInstance(initargs);
				//bean.setComponent(instrument);
				return instrument;
			}
			else*/
			{
				DashComponent instrument = iClass.newInstance();
				//bean.setComponent(instrument);
				return instrument;
			}
		}
		catch(ClassNotFoundException e1)
		{
			//log.severe("Driver class not found: "+e.toString());
			log.log(Level.SEVERE,"Instrument class not found: ",e1);
			return null;
		} catch (InstantiationException e1) {
			log.log(Level.SEVERE,"Instrument object can't be created: ",e1);
			return null;
		} catch (IllegalAccessException e2) {
			log.log(Level.SEVERE,"Instrument object can't be created: ",e2);
			return null;
		} catch (IllegalArgumentException e3) {
			log.log(Level.SEVERE,"Instrument object can't be created: ",e3);
			return null;
		/*} catch (InvocationTargetException e4) {
			log.log(Level.SEVERE,"Instrument object can't be created: ",e4);
			return null; */
		} catch (SecurityException e5) {
			log.log(Level.SEVERE,"Instrument object can't be created: ",e5);
			return null;
		/*} catch (NoSuchMethodException e6) {
			log.log(Level.SEVERE,"Instrument object can't be created: ",e6);
			return null;*/
		}
	}

	
    // -----------------------------------------------------------------------
	// Used in absolute position panels, supposed to not to be used from inside
	// -----------------------------------------------------------------------
    
    private Point absolutePosition = new Point(0,0);
    
	public Point getAbsolutePosition() {		return absolutePosition;	}
	public void setAbsolutePosition(Point absolutePosition) {		this.absolutePosition = absolutePosition;	}

	
	
    // -----------------------------------------------------------------------
	// Getters/setters
    // -----------------------------------------------------------------------

	@Override
	public String getName() { return name; }
	public void setName(String name) {		this.name = name;	}

	/** Serialization and edit only! Don't try to use directly! */
	public DashComponent getComponent() {		return unusedComponent;	}
	public void setComponent(DashComponent component) {		
		this.unusedComponent = component;
		//if(dialog != null)			dialog.redrawSideFrame();
	}

	public double getValueMaximum() {		return valueMaximum;	}
	public void setValueMaximum(double valueMaximum) {		this.valueMaximum = valueMaximum;	}
	public double getValueMinimum() {		return valueMinimum;	}
	public void setValueMinimum(double valueMinimum) {		this.valueMinimum = valueMinimum;	}

	// to pickip old saves (XML deserializes)
	public void setValueMaximum(int max) {		this.valueMaximum = max;	}
	public void setValueMinimum(int min) {		this.valueMinimum = min;	}

	public String getLegend() {		return legend;	}
	public void setLegend(String legend) {		this.legend = legend;	}

	public String getUnits() {		return units;	}
	public void setUnits(String units) {		this.units = units;	}

	public String getInstrumentClassName() {		return instrumentClassName;	}
	public void setInstrumentClassName(String instrumentClassName) {		this.instrumentClassName = instrumentClassName;	}

	public void setImageShortName(String imageShortName) {		this.imageShortName = imageShortName;	}
	public String getImageShortName() {		return imageShortName;	}

	public void setOnMessage(String onMessage) {		this.onMessage = onMessage;	}
	public String getOnMessage() {		return onMessage;	}

	public void setOffMessage(String offMessage) {		this.offMessage = offMessage;	}
	public String getOffMessage() {		return offMessage;	}

	/**
	 * @see setParameter
	 * @return CliParameter for this instrument.
	 */
	public CliParameter getParameter() {		return parameter;	}

	/**
	 * @param parameter CliParameter for this instrument 
	 * - where to get data from (for meters) or translate to
	 * (for controls).
	 */
	public void setParameter(CliParameter parameter) {		this.parameter = parameter;	}

	/**
	 * @return the horizontalWeight
	 */
	public double getHorizontalWeight() {
		return horizontalWeight;
	}

	/**
	 * @param horizontalWeight the horizontalWeight to set
	 */
	public void setHorizontalWeight(double horizontalWeight) {
		this.horizontalWeight = horizontalWeight;
	}

	/**
	 * @return the verticalWeight
	 */
	public double getVerticalWeight() {
		return verticalWeight;
	}

	/**
	 * @param verticalWeight the verticalWeight to set
	 */
	public void setVerticalWeight(double verticalWeight) {
		this.verticalWeight = verticalWeight;
	}



	public void setNMinorTicks(int nMinorTicks) { this.nMinorTicks = nMinorTicks;	}
	public void setNMajorTicks(int nMajorTicks) { this.nMajorTicks = nMajorTicks;	}

	public int getNMinorTicks() { return nMinorTicks;	}
	public int getNMajorTicks() { return nMajorTicks;	}

	public boolean isHistogramVisible() {		return histogramVisible;	}
	public void setHistogramVisible(boolean histogramVisible) {		this.histogramVisible = histogramVisible;	}


	public double getBaseRotationAngle() {		return baseRotationAngle;	}
	/** Start angle of round scale, degrees. */
	public void setBaseRotationAngle(double baseRotationAngle) {		this.baseRotationAngle = baseRotationAngle;	}

	public double getScaleAngle() {		return scaleAngle;	}
	/** Total angle of round scale, degrees. */
	public void setScaleAngle(double scaleAngle) {		this.scaleAngle = scaleAngle;	}



	public Dimension getPreferredSize() {	return preferredSize;	}
	public void setPreferredSize(Dimension preferredSize) {		this.preferredSize = preferredSize;	}

	public boolean isPictogramMessage() {		return pictogramMessage;	}
	public void setPictogramMessage(boolean pictogramMessage) {		this.pictogramMessage = pictogramMessage;	}


	public CliButtonGroup getButtonGroup() {		return buttonGroup;	}
	public void setButtonGroup(CliButtonGroup buttonGroup) {		this.buttonGroup = buttonGroup;	}


	public int getNDigits() {		return nDigits;	}
	public void setNDigits(int nDigits) {		this.nDigits = nDigits;	}


	public int getFontSize() {		return fontSize;	}
	public void setFontSize(int fontSize) {		this.fontSize = fontSize;	}



}
