package ru.dz.shipMaster.dev.ups;

import java.util.Vector;

import javax.swing.JPanel;

import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.dev.DriverPort;
import ru.dz.shipMaster.dev.PortDataOutput;
import ru.dz.shipMaster.dev.ThreadedDriver;

/**
 * Base class for UPS drivers. Defines ports and does system communications.
 * @author dz
 */
public abstract class GeneralUpsDriver extends ThreadedDriver {
	static final long updateIntervalMsec = 1000;

	protected boolean upsFound = false;
	
	private DriverPort upsVoltageRatingPort;
	private DriverPort upsCurrentRatingPort;
	private DriverPort upsBatteryVoltageRatingPort;
	private DriverPort upsFrequencyRatingPort;
	private DriverPort inputVoltagePort;
	private DriverPort inputFaultVoltagePort;
	private DriverPort outputVoltagePort;
	private DriverPort outputLoadPercentPort;
	private DriverPort inputFrequencyPort;
	private DriverPort batteryVoltagePort;
	private DriverPort temperaturePort;
	private DriverPort batteryChargePort;

	private DriverPort beeperOnPort;
	private DriverPort onBatteryPort;
	private DriverPort batteryLowPort;
	private DriverPort loadEnabledPort;
	private DriverPort bypassActivePort;
	private DriverPort hardwareFailurePort;
	private DriverPort testInProgressPort;
	private DriverPort boostTrimPort;

	private DriverPort setBeeperEnabledPort;
	private DriverPort setLoadEnabledPort;

	
	
	private float upsVoltageRating;
	private float upsCurrentRating;
	private float upsBatteryVoltageRating;
	private float upsFrequencyRating;

	private float inputVoltage;
	private float inputFaultVoltage;
	private float outputVoltage;
	private float outputLoadPercent;
	private float inputFrequency;
	private float batteryVoltage;
	private float temperature;
	private float batteryCharge;
	
	private boolean onBattery;
	private boolean batteryLow;
	private boolean loadEnabled;
	private boolean bypassActive;
	private boolean hardwareFailure;
	private boolean testInProgress;
	private boolean beeperOn;
	private boolean boostTrim;

	private PortDataOutput beeperEnabledPortDataOutput = new PortDataOutput() {
		@Override
		public void receiveDoubleData(double value) {
			setBeeperOnOutput(value > 0.01); 
		}};
		
	private PortDataOutput loadEnabledPortDataOutput = new PortDataOutput() {
		@Override
		public void receiveDoubleData(double value) {
			setLoadEnabledOutput(value > 0.01); 			
		}};	
	
	
	
	protected GeneralUpsDriver(String threadName) {
		super(updateIntervalMsec, Thread.NORM_PRIORITY, threadName);
	}


	@Override
	protected void doLoadPanelSettings() {
	// empty
	}

	@Override
	protected void doSavePanelSettings() {
		// empty
	}

	@Override
	protected void doStartDriver() throws CommunicationsException {
		// empty
	}

	@Override
	protected void doStopDriver() throws CommunicationsException {
		// empty
	}

	@Override
	public boolean isAutoSeachSupported() {		return false;	}

	@Override
	protected void setupPanel(JPanel panel) {
		// empty
	}

	@Override
	protected void updatePorts(Vector<DriverPort> ports) {

		DriverPort.absentize(ports);
		DriverPort.sortPorts(ports);
		
		upsVoltageRatingPort = getPort(ports, 0, Direction.Input, Type.Numeric, "Voltage rating");
		upsVoltageRatingPort.setDescription("");

		upsCurrentRatingPort = getPort(ports, 1, Direction.Input, Type.Numeric, "Current rating"); 
		
		upsBatteryVoltageRatingPort = getPort(ports, 2, Direction.Input, Type.Numeric, "Battery voltage rating");		
		upsFrequencyRatingPort = getPort(ports, 3, Direction.Input, Type.Numeric, "Frequency rating");			

		inputVoltagePort = getPort(ports, 4, Direction.Input, Type.Numeric, "Input voltage");				
		inputFaultVoltagePort = getPort(ports, 5, Direction.Input, Type.Numeric, "Input fault voltage");			
		outputVoltagePort = getPort(ports, 6, Direction.Input, Type.Numeric, "Output voltage");				
		outputLoadPercentPort = getPort(ports, 7, Direction.Input, Type.Numeric, "Output load percent");			
		inputFrequencyPort = getPort(ports, 8, Direction.Input, Type.Numeric, "Input frequency");				
		batteryVoltagePort = getPort(ports, 9, Direction.Input, Type.Numeric, "Battery voltage");				
		temperaturePort = getPort(ports, 10, Direction.Input, Type.Numeric, "Temperature");					
		batteryChargePort = getPort(ports, 11, Direction.Input, Type.Numeric, "Battery charge");
		
		beeperOnPort = getPort(ports, 12, Direction.Input, Type.Boolean, "Beeper on");			
		onBatteryPort = getPort(ports, 13, Direction.Input, Type.Boolean, "On battery");			
		batteryLowPort = getPort(ports, 14, Direction.Input, Type.Boolean, "Battery low");			
		loadEnabledPort = getPort(ports, 15, Direction.Input, Type.Boolean, "Load enabled");		
		bypassActivePort = getPort(ports, 16, Direction.Input, Type.Boolean, "Bypass active");		
		hardwareFailurePort = getPort(ports, 17, Direction.Input, Type.Boolean, "Hardware failure");	
		testInProgressPort = getPort(ports, 18, Direction.Input, Type.Boolean, "Test in progress");
		boostTrimPort = getPort(ports, 19, Direction.Input, Type.Boolean, "Boost/Trim");

		setBeeperEnabledPort = getPort(ports, 20, Direction.Output, Type.Boolean, "Enable beeper");
		setBeeperEnabledPort.setPortDataOutput(beeperEnabledPortDataOutput);
		
		setLoadEnabledPort = getPort(ports, 21, Direction.Output, Type.Boolean, "Enable load");
		setLoadEnabledPort.setPortDataOutput(loadEnabledPortDataOutput);
		
	}

	protected final float getUpsVoltageRating() {		return upsVoltageRating;	}

	protected final void setUpsVoltageRating(float upsVoltageRating) {
		this.upsVoltageRating = upsVoltageRating;
		upsVoltageRatingPort.sendDoubleData(upsVoltageRating);
	}

	protected final float getUpsCurrentRating() {		return upsCurrentRating;	}

	protected final void setUpsCurrentRating(float upsCurrentRating) {
		this.upsCurrentRating = upsCurrentRating;
		upsCurrentRatingPort.sendDoubleData(upsCurrentRating);
	}

	protected final float getUpsBatteryVoltageRating() {		return upsBatteryVoltageRating;	}

	protected final void setUpsBatteryVoltageRating(float upsBatteryVoltageRating) {
		this.upsBatteryVoltageRating = upsBatteryVoltageRating;
		upsBatteryVoltageRatingPort.sendDoubleData(upsBatteryVoltageRating);
	}

	protected final float getUpsFrequencyRating() {		return upsFrequencyRating;	}

	protected final void setUpsFrequencyRating(float upsFrequencyRating) {
		this.upsFrequencyRating = upsFrequencyRating;
		upsFrequencyRatingPort.sendDoubleData(upsFrequencyRating);
	}

	protected final float getInputVoltage() {		return inputVoltage;	}

	protected final void setInputVoltage(float inputVoltage) {
		this.inputVoltage = inputVoltage;
		inputVoltagePort.sendDoubleData(inputVoltage);
	}

	protected final float getInputFaultVoltage() {		return inputFaultVoltage;	}

	protected final void setInputFaultVoltage(float inputFaultVoltage) {
		this.inputFaultVoltage = inputFaultVoltage;
		inputFaultVoltagePort.sendDoubleData(inputFaultVoltage);
	}

	protected final float getOutputVoltage() {		return outputVoltage;	}

	protected final void setOutputVoltage(float outputVoltage) {
		this.outputVoltage = outputVoltage;
		outputVoltagePort.sendDoubleData(outputVoltage);
	}

	protected final float getOutputLoadPercent() {		return outputLoadPercent;	}

	protected final void setOutputLoadPercent(float outputLoadPercent) {
		this.outputLoadPercent = outputLoadPercent;
		outputLoadPercentPort.sendDoubleData(outputLoadPercent);
	}

	protected final float getInputFrequency() {		return inputFrequency;	}

	protected final void setInputFrequency(float inputFrequency) {
		this.inputFrequency = inputFrequency;
		inputFrequencyPort.sendDoubleData(inputFrequency);
	}

	protected final float getBatteryVoltage() {		return batteryVoltage;	}

	protected final void setBatteryVoltage(float batteryVoltage) {
		this.batteryVoltage = batteryVoltage;
		batteryVoltagePort.sendDoubleData(batteryVoltage);
	}

	protected final float getTemperature() {		return temperature;	}

	protected final void setTemperature(float temperature) {
		this.temperature = temperature;
		temperaturePort.sendDoubleData(temperature);
	}

	protected final float getBatteryCharge() {		return batteryCharge;	}

	protected final void setBatteryCharge(float batteryCharge) {
		this.batteryCharge = batteryCharge;
		batteryChargePort.sendDoubleData(batteryCharge);
	}

	protected final boolean isOnBattery() {		return onBattery;	}

	protected final void setOnBattery(boolean onBattery) {
		this.onBattery = onBattery;
		onBatteryPort.sendBooleanData(onBattery);
	}

	protected final boolean isBatteryLow() {		return batteryLow;	}

	protected final void setBatteryLow(boolean batteryLow) {
		this.batteryLow = batteryLow;
		batteryLowPort.sendBooleanData(batteryLow);
	}

	protected final boolean isLoadEnabled() {		return loadEnabled;	}

	protected final void setLoadEnabled(boolean loadEnabled) {
		this.loadEnabled = loadEnabled;
		loadEnabledPort.sendBooleanData(loadEnabled);
	}

	protected final boolean isBypassActive() {		return bypassActive;	}

	protected final void setBypassActive(boolean bypassActive) {
		this.bypassActive = bypassActive;
		bypassActivePort.sendBooleanData(bypassActive);
	}

	protected final boolean isHardwareFailure() {		return hardwareFailure;	}

	protected final void setHardwareFailure(boolean hardwareFailure) {
		this.hardwareFailure = hardwareFailure;
		hardwareFailurePort.sendBooleanData(hardwareFailure);
	}

	protected final boolean isTestInProgress() {		return testInProgress;	}

	protected final void setTestInProgress(boolean testInProgress) {
		this.testInProgress = testInProgress;
		testInProgressPort.sendBooleanData(testInProgress);
	}

	protected final boolean isBeeperOn() {		return beeperOn;	}

	protected final void setBeeperOn(boolean beeperOn) {
		this.beeperOn = beeperOn;
		beeperOnPort.sendBooleanData(beeperOn);
	}

	protected final boolean isBoostTrim() {		return boostTrim;	}

	protected final void setBoostTrim(boolean boostTrim) {
		this.boostTrim = boostTrim;
		boostTrimPort.sendBooleanData(boostTrim);
	}


	/**
	 * Must be implemented in child to control UPS beeper (on/off).
	 * @param setBeeperOn True to enable UPS beeper.
	 */
	protected abstract void setBeeperOnOutput(boolean setBeeperOn);

	/**
	 * Must be implemented in child to control UPS load (on/off).
	 * @param setBeeperOn True to turn on UPS power output.
	 */
	protected abstract void setLoadEnabledOutput(boolean setLoadEnabled);

}
