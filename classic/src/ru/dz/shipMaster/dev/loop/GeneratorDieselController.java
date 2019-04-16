package ru.dz.shipMaster.dev.loop;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import ru.dz.shipMaster.DashBoard;
import ru.dz.shipMaster.config.ConfigurationFactory;
import ru.dz.shipMaster.config.items.CliAlarm;
import ru.dz.shipMaster.config.items.CliAlarm.AlarmProcessor;
import ru.dz.shipMaster.config.items.CliAlarmStation;
import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.dev.DriverPort;
import ru.dz.shipMaster.dev.EventSteadyTimer;
import ru.dz.shipMaster.dev.PortDataOutput;
import ru.dz.shipMaster.dev.ThreadedDriver;
import ru.dz.shipMaster.ui.logger.GeneralLogWindow.Item;
import ru.dz.shipMaster.ui.logger.LogWindow;
import ru.dz.shipMaster.ui.logger.MessageWindow;

/**
 * Control loop (no real device) driver. Generator engine control.
 * @author dz
 */
public class GeneratorDieselController extends ThreadedDriver {

	private static final Logger log = Logger.getLogger(GeneratorDieselController.class.getName());

	private static final String NAME = "Generator Diesel Controller";
	private static final String MESAAGE_PREFIX = "Гeн: ";

	// Inputs
	
	private DriverPort batteryVoltagePort;
	private PortDataOutput batteryVoltageOutput = new PortDataOutput() {
		@Override
		public void receiveDoubleData(double value)	{ upsBatteryVoltageValue = value;	}
	}; 

	private DriverPort inputVoltagePort;
	private PortDataOutput inputVoltageOutput = new PortDataOutput() {
		@Override
		public void receiveDoubleData(double value)	{ upsInputVoltageValue = value;	}
	}; 

	private DriverPort outputLoadPercentPort;
	private PortDataOutput outputLoadPercentOutput = new PortDataOutput() {
		@Override
		public void receiveDoubleData(double value)	{ upsLoadPercentageValue = value;	}
	}; 

	private DriverPort onBatteryPort;
	private PortDataOutput onBatteryOutput = new PortDataOutput() {
		@Override
		public void receiveDoubleData(double value)	{ upsOnBatteryValue = value > 0.01;	}
	}; 

	private DriverPort batteryLowPort;
	private PortDataOutput batteryLowOutput = new PortDataOutput() {
		@Override
		public void receiveDoubleData(double value)	{ upsBatteryLowValue = value > 0.01;	}
	}; 

	private DriverPort batteryChargePercentPort;
	private PortDataOutput batteryChargePercentOutput = new PortDataOutput() {
		@Override
		public void receiveDoubleData(double value)	{ batteryChargePercentValue = value;	}
	}; 

	private DriverPort generatorRpmPort;
	private PortDataOutput generatorRpmOutput = new PortDataOutput() {
		@Override
		public void receiveDoubleData(double value)	{ generatorRpmValue = value;	}
	}; 

	
	// Outputs
	
	private DriverPort generatorEnablePort;
	private DriverPort generatorStartPort;
	private DriverPort generatorStopPort;
	private DriverPort generatorRunMonitorPort;

	// Internal Parameters

	private boolean skipOnBatteryCheck = false;
	
	private int startPercents = 65;
	private int stopPercents = 95;
	private int nightStartPercents = 75;
	private int overloadPercents = 85;

	private int secondsToCrank = 8;
	private int secondsToWaitStart = 10;
	
	private int startedFrequency = 50; // If freq is more - engine is running
	

	private JCheckBox skipOnBatteryCheckField = new JCheckBox();
	
	private JSpinner startPercentsField = new JSpinner(new SpinnerNumberModel(startPercents,0,100,5));
	private JSpinner stopPercentsField = new JSpinner(new SpinnerNumberModel(stopPercents,0,100,5));
	private JSpinner nightStartPercentsField =  new JSpinner(new SpinnerNumberModel(nightStartPercents,0,100,5));
	private JTextField nightCheckStartTimeField = new JTextField(4);
	private JTextField nightCheckEndTimeField = new JTextField(4);
	private JSpinner overloadPercentageField = new JSpinner(new SpinnerNumberModel(overloadPercents,0,100,5));
	
	private JSpinner secondsToCrankField = new JSpinner(new SpinnerNumberModel(secondsToCrank,0,30,2));
	private JSpinner secondsToWaitStartField = new JSpinner(new SpinnerNumberModel(secondsToWaitStart,0,180,10));

	private JSpinner startedFrequencyField = new JSpinner(new SpinnerNumberModel(startedFrequency,10,1000,10));


	// --------------------------------------------------------------------
	// Init
	// --------------------------------------------------------------------
	

	public GeneratorDieselController() {
		super(1000, Thread.NORM_PRIORITY, NAME+" loop driver");
		log.log(Level.CONFIG,NAME+" start");
	}






	@Override
	protected void doStartDriver() throws CommunicationsException {
		// none
	}

	@Override
	protected void doStopDriver() throws CommunicationsException {
		// none
	}

	@Override
	public String getDeviceName() { return NAME+" loop (preliminary)"; }

	@Override
	public String getInstanceName() {		return "internal";	}


	@Override
	public boolean isAutoSeachSupported() { return false; }


	@Override
	protected void updatePorts(Vector<DriverPort> ports) {
		
		batteryVoltagePort = getPort(ports, 0, Direction.Output, Type.Numeric, "Battery voltage");
		batteryVoltagePort.setPortDataOutput(batteryVoltageOutput);
		batteryVoltagePort.setDescription("Unused.");
		
		inputVoltagePort = getPort(ports, 1, Direction.Output, Type.Numeric, "Input voltage");
		inputVoltagePort.setPortDataOutput(inputVoltageOutput);
		
		outputLoadPercentPort = getPort(ports, 2, Direction.Output, Type.Numeric, "Output load percentage");
		outputLoadPercentPort.setPortDataOutput(outputLoadPercentOutput);
		
		
		onBatteryPort = getPort(ports, 3, Direction.Output, Type.Boolean, "On battery");
		onBatteryPort.setPortDataOutput(onBatteryOutput);
		
		batteryLowPort = getPort(ports, 4, Direction.Output, Type.Boolean, "Battery low");
		batteryLowPort.setPortDataOutput(batteryLowOutput);
		batteryLowPort.setDescription("Boolean 'battery low' signal from UPS. Will trigger charge sequence instantly.");
		
		batteryChargePercentPort = getPort(ports, 5, Direction.Output, Type.Numeric, "Battery charge percentage");
		batteryChargePercentPort.setPortDataOutput(batteryChargePercentOutput);
		batteryChargePercentPort.setDescription("0-100% of charge in battery. Used to determine when to start charging battaries.");

		
		generatorEnablePort = getPort(ports, 6, Direction.Input, Type.Boolean, "Generator enable");
		generatorEnablePort.setDescription("Will be on as long as generator engine is supposed to be started or running.");
		
		generatorStartPort = getPort(ports, 7, Direction.Input, Type.Boolean, "Generator start");
		generatorStartPort.setDescription("Will be on as long as engine starter must be running.");
		
		generatorStopPort = getPort(ports, 8, Direction.Input, Type.Boolean, "Generator stop");
		generatorStopPort.setDescription("Will be on for engine to be halted.");

		generatorRunMonitorPort = getPort(ports, 9, Direction.Input, Type.Boolean, "Generator run monitor");
		generatorRunMonitorPort.setDescription("Will be on as long as engine is detected to be running.");
		
		generatorRpmPort = getPort(ports, 10, Direction.Output, Type.Numeric, "Generator RPM Frequency");
		generatorRpmPort.setPortDataOutput( generatorRpmOutput );
		generatorRpmPort.setDescription("(will be) used to determine that generator engine is running");
	}

	
	
	
	// --------------------------------------------------------------------
	// UI
	// --------------------------------------------------------------------
	
	
	@Override
	protected void setupPanel(JPanel panel) {
		panel.add(new JLabel("Start at % of battery charge:"), consL);
		panel.add(startPercentsField, consR);
		startPercentsField.setToolTipText(
				"When battery discharges to this percentage, \n" +
				"generator will be started to charge it.");
		
		panel.add(new JLabel("Stop at % of battery charge:"), consL);
		panel.add(stopPercentsField, consR);
		stopPercentsField.setToolTipText("When battery charges to this percentage, generator will be stopped.");
		
		panel.add(new JLabel("Night start prevention at % of battery charge:"), consL);
		panel.add(nightStartPercentsField, consR);
		nightStartPercentsField.setToolTipText(
				"Percentage of battery charge which will \n" +
				"be enough usually to feed sheep for all \n" +
				"the night without generator start.");

		panel.add(new JLabel("Night check start time, hr:"), consL);
		panel.add(nightCheckStartTimeField, consR);
		nightCheckStartTimeField.setEnabled(false);
		nightCheckStartTimeField.setToolTipText(
				"Starting at this time system will check \n" +
				"that battery charge is big enough to make \n" +
				"sure generator won't be started at night.");

		panel.add(new JLabel("Night check end time, hr:"), consL);
		panel.add(nightCheckEndTimeField, consR);
		nightCheckEndTimeField.setEnabled(false);
		nightCheckEndTimeField.setToolTipText(
				"This is the time after which nigt precharge \n" +
				"percentage is not more checked.");

		panel.add(new JLabel("Overload start at % of load:"), consL);
		panel.add(overloadPercentageField, consR);
		overloadPercentageField.setToolTipText(
				"When UPS load will reach this value, generator \n" +
				"will be started to let UPS to switch to bypass.");
		
		panel.add(new JLabel("Seconds to run starter (for one attempt):"), consL);
		panel.add(secondsToCrankField, consR);

		panel.add(new JLabel("Seconds to wait after running starter (for one attempt):"), consL);
		panel.add(secondsToWaitStartField, consR);
		secondsToWaitStartField.setToolTipText(
				"This is NOT a time between start attempts. \n" +
				"System will ANYWAY wait for one minute \n" +
				"between attempts to start diesel. This \n" +
				"is the time system will wait for diesel to \n" +
				"start AFTER start pulse is finished. This \n" +
				"is to be used with external automatic \n" +
				"start systems, which need a lot of time to \n" +
				"start generator after the start button press.");
		
		panel.add(new JLabel("Minimum RPM frequency treated as started condition:"), consL);
		panel.add( startedFrequencyField, consR);

		
		panel.add(new JLabel("Skip 'on battery' check:"), consL);
		panel.add(skipOnBatteryCheckField, consR);
		skipOnBatteryCheckField.setToolTipText(
				"If checked, controller will ignore\n" +
				"UPS 'on battery' state and suppose\n" +
				"that UPS is allways on batteries."
				);
		
		doLoadPanelSettings();
	}
	
	@Override
	protected void doLoadPanelSettings() {
		startPercentsField.setValue(new Integer(startPercents));
		stopPercentsField.setValue(new Integer(stopPercents));
		nightStartPercentsField.setValue(new Integer(nightStartPercents));
		overloadPercentageField.setValue(new Integer(overloadPercents));
		
		secondsToWaitStartField.setValue(new Integer(secondsToWaitStart));
		secondsToCrankField.setValue(new Integer(secondsToCrank));
		
		startedFrequencyField.setValue(new Integer(startedFrequency));
		
		skipOnBatteryCheckField.setSelected(skipOnBatteryCheck);
	}

	@Override
	protected void doSavePanelSettings() {
		startPercents = (Integer)startPercentsField.getValue();
		stopPercents  = (Integer)stopPercentsField.getValue();
		overloadPercents = (Integer)overloadPercentageField.getValue(); 
		
		secondsToWaitStart = (Integer)secondsToWaitStartField.getValue();
		secondsToCrank = (Integer)secondsToCrankField.getValue();
		
		startedFrequency = (Integer)startedFrequencyField.getValue();
		
		skipOnBatteryCheck = skipOnBatteryCheckField.isSelected();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	



	private LogWindow logWindow;
	private Item messageWindowItem;

	{
		DashBoard db = ConfigurationFactory.getTransientState().getDashBoard();
		MessageWindow messageWindow;
		logWindow = db.getLogWindow();
		messageWindow = db.getMessageWindow();		
		messageWindowItem = messageWindow.getItem(MESAAGE_PREFIX+"готов");		
	}




	// --------------------------------------------------------------------
	// Inputs
	// --------------------------------------------------------------------

	protected double upsBatteryVoltageValue = 0;
	protected double upsInputVoltageValue = 0;
	/** Must be connected to UPS load percentage meter */
	protected double upsLoadPercentageValue = 100; // Worse case
	protected boolean upsOnBatteryValue = false;
	protected boolean upsBatteryLowValue = false;
	protected boolean dieselStartedValue = false;
	protected double batteryChargePercentValue = 100;
	protected double generatorRpmValue = 0;

	// --------------------------------------------------------------------
	// Outputs
	// --------------------------------------------------------------------
	
	/** Must be on when we suppose that generator engine must be stopping. */
	private void setGeneratorStopPin(boolean b) {	generatorStopPort.sendBooleanData(b);	}

	/** Must be on when we suppose that generator engine must be starting. */
	private void setGeneratorStartPin(boolean b) {	generatorStartPort.sendBooleanData(b);	}

	/** Must be on when we suppose that generator engine must be running. */
	private void setGeneratorEnablePin(boolean b) {	generatorEnablePort.sendBooleanData(b);	}
	
	/** Must be on when we detect that generator engine is running (mostly for test purposes). */
	private void setGeneratorRunMonitorPin(boolean b) {		generatorRunMonitorPort.sendBooleanData(b);	}
	
	// --------------------------------------------------------------------
	// Helpers
	// --------------------------------------------------------------------

	private CliAlarmStation alarmStation = null;
	private CliAlarm cantStopAlarm = new CliAlarm();
	private AlarmProcessor cantStopAlarmProcessor;
	{
		cantStopAlarm.setName("Can't stop generator engine");
		cantStopAlarm.setType(CliAlarm.Type.UpperForbidden);
		cantStopAlarm.setHiCritical(1.0);
		cantStopAlarmProcessor = cantStopAlarm.new AlarmProcessor("Останов генератора"); // TODO internationalize
	}

	private CliAlarm cantStartAlarm = new CliAlarm();
	private AlarmProcessor cantStartAlarmProcessor;
	{
		cantStartAlarm.setName("Can't start generator engine");
		cantStartAlarm.setType(CliAlarm.Type.UpperForbidden);
		cantStartAlarm.setHiCritical(1.0);
		cantStartAlarmProcessor = cantStartAlarm.new AlarmProcessor("Старт генератора"); // TODO internationalize
	}
	
	private CliAlarm cantControlAlarm = new CliAlarm();
	private AlarmProcessor cantControlAlarmProcessor;
	{
		cantControlAlarm.setName("Can't control generator engine");
		cantControlAlarm.setType(CliAlarm.Type.UpperForbidden);
		cantControlAlarm.setHiCritical(1.0);
		cantControlAlarmProcessor = cantControlAlarm.new AlarmProcessor("Управление генератором"); // TODO internationalize
	}
	
	/*
	private void sendAlert()
	{
		ConfigurationFactory.getTransientState().addHistoryRecord(
				new HistoryAlarmRecord(critical, text, start));	

		alarmStation.
	}*/
	
	private boolean isGeneratorRunnning() {
		// TO DO Use RPM too?
		return dieselStartedValue || (generatorRpmValue > startedFrequency ) ;
		
		// X XX TEST MODE!
		//return isUpsPowered();
	}

	private boolean isBatteryLow() {
		if( batteryChargePercentValue < startPercents )
		{
			logWindow.addMessage(MESAAGE_PREFIX+"заряд батарей UPS слишком мал");
			return true;
		}

		if( upsBatteryLowValue )
		{
			logWindow.addMessage(MESAAGE_PREFIX+"UPS сообщает, что заряд батарей мал");
			return true;
		}
		
		return false;
	}

	private boolean isBatteryCharged() {
		// Don't stop charging if UPS tells its still hungry
		if( upsBatteryLowValue )
		{
			//logWindow.addMessage(MESAAGE_PREFIX+"UPS сообщает, что заряд батарей мал");
			return false;
		}
		
		if( batteryChargePercentValue > stopPercents )
		{
			logWindow.addMessage(MESAAGE_PREFIX+"заряд батарей UPS достаточен");
			return true;
		}

		return false;
	}
	
	
	/** UPS has input voltage. */
	private boolean isUpsPowered()
	{
		//log.severe("UPS in is "+upsInputVoltageValue);
		return upsInputVoltageValue > 190; 
	}
	
	private boolean isUpsOnBattery()
	{
		return skipOnBatteryCheck ||upsOnBatteryValue;
	}
	
	// TO DO take average load in account
	/** Use to decide on engine start. */
	private boolean isUpsOverloaded()
	{
		return upsLoadPercentageValue > overloadPercents;
	}

	/** Use to decide on engine stop. */
	private boolean isUpsNotOverloaded()
	{
		return upsLoadPercentageValue < Math.max(5,overloadPercents);
	}
	
	/*private boolean upsRatingsOk() {
		return (ups.getUpsVoltageRating() > 210) && (ups.getUpsVoltageRating() < 230);
	}*/

	private void logGeneratorState() {
		logWindow.addMessage(MESAAGE_PREFIX+"двигатель в данный момент " + (isGeneratorRunnning() ? "работает" : "не работает"));
		messageWindowItem.setText(MESAAGE_PREFIX+"двигатель " + (isGeneratorRunnning() ? "запущен" : "остановлен"));
	}

	
	private void sleepSec(int sec)
	{
		long sleepStart = System.currentTimeMillis();
		long waitUntil = sleepStart + (1000L*sec); 
		
		while(System.currentTimeMillis() < waitUntil )
		{
			super.sleep(waitUntil - System.currentTimeMillis());
		}
	}
	
	// --------------------------------------------------------------------
	// Timing
	// --------------------------------------------------------------------

	private static final long STARTSTOP_TIMEOUT_SEC = 60; 
	private long lastGeneratorStartStopTime = System.currentTimeMillis() - (1000L*STARTSTOP_TIMEOUT_SEC);
	private void waitStartStopTimeout()
	{
		// TODO if load percentage is too high start generator immediately. 
		while((System.currentTimeMillis() - lastGeneratorStartStopTime) < (1000L*STARTSTOP_TIMEOUT_SEC))
		{
			 sleep(60000);
		}		
		
		lastGeneratorStartStopTime = System.currentTimeMillis();
	}

	// --------------------------------------------------------------------
	// Actual start and stop code. To be called from our thread only.
	// Can take long time.
	// --------------------------------------------------------------------

	/** 
	 * Actual start code. To be called from our thread only. 
	 * Can take long time.
	 * @throws CommunicationsException If unable to send command
	 */
	private void doStartProcedure() 
	{
		//messageWindowItem.setText(MESAAGE_PREFIX+"запускаем двигатель - таймаут");
		//waitStartStopTimeout();
		messageWindowItem.setText(MESAAGE_PREFIX+"запускаем двигатель");
		logWindow.addMessage(MESAAGE_PREFIX+"запускаем двигатель");
		
		if(!doStartTry())
		{
			sleepSec(30);
			messageWindowItem.setText(MESAAGE_PREFIX+"запускаем двигатель, попытка 2");
			logWindow.addMessage(MESAAGE_PREFIX+"запускаем двигатель, попытка 2");
			if(!checkStartConditions())
			{
				messageWindowItem.setText(MESAAGE_PREFIX+"запуск отменён");
				logWindow.addMessage(MESAAGE_PREFIX+"запуск отменён");
				return;
			}
			
			if(!doStartTry())
			{
				sleepSec(60);
				messageWindowItem.setText(MESAAGE_PREFIX+"запускаем двигатель, попытка 3");
				logWindow.addMessage(MESAAGE_PREFIX+"запускаем двигатель, попытка 3");
				if(!checkStartConditions())
				{
					messageWindowItem.setText(MESAAGE_PREFIX+"запуск отменён");
					logWindow.addMessage(MESAAGE_PREFIX+"запуск отменён");
					return;
				}

				if(!doStartTry())
				{
					messageWindowItem.setText(MESAAGE_PREFIX+"запуск не удался");
					messageWindowItem.setCritical(true);
					logWindow.addCriticalMessage(MESAAGE_PREFIX+"запуск не удался");
					return;
				}
				
			}
			
		}

		messageWindowItem.setCritical(false);
		messageWindowItem.setText(MESAAGE_PREFIX+"запущен ");
	}
	
	/**
	 * Try once to start engine.
	 * @return true on success.
	 */
	private boolean doStartTry( ) {
		if(isGeneratorRunnning())
		{
			logWindow.addCriticalMessage(MESAAGE_PREFIX+"ошибка - попытка запустить работающий двигатель");
			return true;
		}
		
		messageWindowItem.setText(MESAAGE_PREFIX+"запускаем");
		logWindow.addMessage(MESAAGE_PREFIX+"выдача команды на запуск");
		
		setGeneratorEnablePin(true);
		setGeneratorStopPin(false);
		setGeneratorStartPin(true);
		
		for(int i = 0; i < secondsToCrank; i++ )
		{
			if(isGeneratorRunnning())
				break;
			sleepSec(1);
		}

		setGeneratorStartPin(false);
		logWindow.addMessage(MESAAGE_PREFIX+"ожидание запуска");

		for(int i = 0; i < secondsToWaitStart; i++ )
		{
			if(isGeneratorRunnning())
				break;
			sleepSec(1);
		}
		
		if(isGeneratorRunnning())
		{
			messageWindowItem.setCritical(false);
			messageWindowItem.setText(MESAAGE_PREFIX+"запущен");
			return true;
		}
		else
		{
			setGeneratorEnablePin(false);

			logWindow.addCriticalMessage(MESAAGE_PREFIX+"не удалось запустить двигатель");			
			messageWindowItem.setCritical(true);
			messageWindowItem.setText(MESAAGE_PREFIX+"не удаётся запустить двигатель");
			cantStartAlarmProcessor.processValue(1, 1, null);
			return false;
		}
	}

	/** 
	 * Actual stop code. To be called from our thread only. 
	 * Can take long time.
	 */
	private void doStopProcedure() 
	{
		//messageWindowItem.setText(MESAAGE_PREFIX+"останавливаем двигатель - таймаут");
		//waitStartStopTimeout();
		messageWindowItem.setText(MESAAGE_PREFIX+"останавливаем двигатель");
		logWindow.addMessage(MESAAGE_PREFIX+"останавливаем двигатель");
		
		while(isGeneratorRunnning())
		{
			doStopTry();
			if(!checkStopConditions())
			{
				messageWindowItem.setText(MESAAGE_PREFIX+"останов отменён");
				logWindow.addMessage(MESAAGE_PREFIX+"останов отменён");
				return;
			}
			sleepSec(10);
		}
		cantStopAlarmProcessor.processValue(0, 1, null); // set off alarm
	}

	/**
	 * Try once to stop engine.
	 */

	private void doStopTry() {
		if(!isGeneratorRunnning())
		{
			logWindow.addCriticalMessage(MESAAGE_PREFIX+"ошибка - попытка остановить неработающий двигатель");
			return;
		}

		messageWindowItem.setText(MESAAGE_PREFIX+"останавливаем");
		logWindow.addMessage(MESAAGE_PREFIX+"выдача команды на останов");

		setGeneratorEnablePin(false);
		setGeneratorStopPin(true);
		setGeneratorStartPin(false);
		
		for(int i = 0; i < 10; i++ )
		{
			if(!isGeneratorRunnning())
				break;
			sleepSec(1);
		}

		setGeneratorStopPin(false);
		
		if(isGeneratorRunnning())
		{
			logWindow.addCriticalMessage(MESAAGE_PREFIX+"двигатель игнорирует команду на останов!");			
			messageWindowItem.setCritical(true);
			messageWindowItem.setText(MESAAGE_PREFIX+"игнорирует команду на останов!");
			cantStopAlarmProcessor.processValue(1, 1, null); // set off alarm
		}
		else
		{
			messageWindowItem.setCritical(false);
			messageWindowItem.setText(MESAAGE_PREFIX+"остановлен");
		}
	}
	
	// --------------------------------------------------------------------
	// Own thread
	// --------------------------------------------------------------------

	@Override
	protected void doDriverTask() throws Throwable {
		checkCommunications();
		findOutSituation();
		mainLoop();		
	}


	private void checkCommunications()  {

		/* TODO fix UPS/Diesel absence check */
		
		// TODO: note that we will not find out here if communications are jammed
		// on the physical layer so we have to check ourselves if our commands
		// are executed
		
		setGeneratorStartPin(false);
		setGeneratorStopPin(false);

		// TO DO check if our commands had some effect
		
		// test
		//setGeneratorEnablePin(false);
	}



	private void findOutSituation() {
		messageWindowItem.setText(MESAAGE_PREFIX+"проверка состояния");
		logWindow.addMessage(MESAAGE_PREFIX+"проверка состояния");
		
		// Make sure we do no harm
		setGeneratorRunMonitorPin(isGeneratorRunnning());
		setGeneratorEnablePin(isGeneratorRunnning());
		
		// Report
		logGeneratorState();
	}
	
	/**
	 * Main tasks:
	 * 1. If on battery and batteries are low and no input voltage - start diesel.
	 * 2. If on battery and load is high and no input voltage - start diesel.
	 * 3. If load is low and batteries are charged - stop diesel
	 * 4. If (NOT IMPLEMENTED) we have outer electricity supplies - stop diesel
	 * @throws InterruptedException If wait is interrupted...
	 */

	private void mainLoop() throws InterruptedException {
		logWindow.addMessage(MESAAGE_PREFIX+"стандартный режим");
		//messageWindowItem.setText(MESAAGE_PREFIX+"стандартный режим");
		while(true)
		{
			//messageWindowItem.setText(MESAAGE_PREFIX+"таймаут");
			messageWindowItem.setCritical(false);
			logWindow.addMessage(MESAAGE_PREFIX+"таймаут");
			waitStartStopTimeout();
			logWindow.addMessage(MESAAGE_PREFIX+"конец таймаута");
			
			cantControlAlarmProcessor.processValue(0, 1, null); // is it a good place to stop alert?
			
			if(isUpsOnBattery() && (!isUpsPowered()))
				logWindow.addCriticalMessage(MESAAGE_PREFIX+"питание UPS присутствует, но UPS на батареях");
			
			if( isUpsPowered() && (!isGeneratorRunnning()) )
			{
				messageWindowItem.setText(MESAAGE_PREFIX+"дизель остановлен, но питание UPS присутствует");
				logWindow.addMessage(MESAAGE_PREFIX+"питание UPS присутствует, дизель остановлен");
				logWindow.addMessage(MESAAGE_PREFIX+"автоматика управления скучает");
				while(isUpsPowered())
					sleepSec(1);
				logWindow.addMessage(MESAAGE_PREFIX+"питание UPS отсутствует");
				logWindow.addMessage(MESAAGE_PREFIX+"автоматика управления оживилась");
				logGeneratorState();
				cantStartAlarmProcessor.processValue(0, 1, null);
			}
			
			waitForCondition: while(true)
			{
				synchronized (this) {
					wait(100);
				}
				if(isGeneratorRunnning())
				{
					cantStartAlarmProcessor.processValue(0, 1, null);
					if(checkStopConditions())
					{
						logWindow.addMessage(MESAAGE_PREFIX+"принято решение остановить двигатель");
						doStopProcedure();
						break waitForCondition;
					}
				}
				else
				{
					cantStopAlarmProcessor.processValue(0, 1, null);
					if(checkStartConditions())
					{
						logWindow.addMessage(MESAAGE_PREFIX+"принято решение запустить двигатель");
						doStartProcedure();
						break waitForCondition;
					}
				}
			}
			
		}		
	}

	
	private static final long START_CONDITION_CHECK_TIME = 2*1000; // 2 sec
	private EventSteadyTimer startSteadyTimer = new EventSteadyTimer(START_CONDITION_CHECK_TIME);
	
	/**
	 * 1. If on battery and batteries are low and no input voltage - start diesel.
	 * 2. If on battery and load is high and no input voltage - start diesel.
	 */
	private boolean checkStartConditions() {
		
		if(isUpsOnBattery() && isBatteryLow() && (!isUpsPowered()))
		{
			logWindow.addMessage(MESAAGE_PREFIX+"батареи UPS разряжены");
			return startSteadyTimer.checkEvent( true );
		}

		if(isUpsOnBattery() && isUpsOverloaded() && (!isUpsPowered()))
		{
			logWindow.addMessage(MESAAGE_PREFIX+"UPS перегружен");
			return startSteadyTimer.checkEvent( true );
		}
		
		return startSteadyTimer.checkEvent( false );
	}

	private static final long STOP_CONDITION_CHECK_TIME = 10*1000; // 10 sec
	private EventSteadyTimer stopSteadyTimer = new EventSteadyTimer(STOP_CONDITION_CHECK_TIME);
	
	/**
	 * 3. If load is low and batteries are charged - stop diesel
	 * 4. If (NOT IMPLEMENTED) we have outer electricity supplies - stop diesel
	 */
	
	private boolean checkStopConditions() {
		return stopSteadyTimer.checkEvent( (isBatteryCharged()) && isUpsNotOverloaded() );
	}

	public CliAlarmStation getAlarmStation() {
		return alarmStation;
	}

	public void setAlarmStation(CliAlarmStation alarmStation) {
		this.alarmStation = alarmStation;
		cantStartAlarm.setStation(alarmStation);
		cantStopAlarm.setStation(alarmStation);
		cantControlAlarm.setStation(alarmStation);
	}






	public int getStartPercents() {		return startPercents;	}
	public void setStartPercents(int startPercents) {		this.startPercents = startPercents;	}
	public int getStopPercents() {		return stopPercents;	}
	public void setStopPercents(int stopPercents) {		this.stopPercents = stopPercents;	}
	public int getNightStartPercents() {		return nightStartPercents;	}
	public void setNightStartPercents(int nightStartPercents) {		this.nightStartPercents = nightStartPercents;	}
	public int getOverloadPercents() {		return overloadPercents;	}
	public void setOverloadPercents(int overloadPercents) {		this.overloadPercents = overloadPercents;	}
	public int getSecondsToCrank() {		return secondsToCrank;	}
	public void setSecondsToCrank(int secondsToCrank) {		this.secondsToCrank = secondsToCrank;	}
	public int getSecondsToWaitStart() {		return secondsToWaitStart;	}
	public void setSecondsToWaitStart(int secondsToWaitStart) {		this.secondsToWaitStart = secondsToWaitStart;	}


	public boolean isSkipOnBatteryCheck() {		return skipOnBatteryCheck;	}
	public void setSkipOnBatteryCheck(boolean skipOnBatteryCheck) {		this.skipOnBatteryCheck = skipOnBatteryCheck;	}






	
}
