package ru.dz.shipMaster.dev.loop;

import java.util.Vector;
import java.util.logging.Level;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.dev.DriverPort;
import ru.dz.shipMaster.dev.PortDataOutput;
import ru.dz.shipMaster.dev.ThreadedDriver;

/**
 * Timer element (soft driver).
 * @author dz
 */
public class Timer extends ThreadedDriver {

	private DriverPort directResultPort;
	private DriverPort invertedResultPort;

	private DriverPort in1, in2, in3, in4;
	protected boolean inTriggerValue, inResetValue, inSetValue, inEnableValue = true;

	private boolean triggerInvert = false;

	//private boolean have1 = false, have2 = false, have3 = false, have4 = false;

	private int beforeOnCounter = -1, beforeOnCount = 30; // in 0.1 of sec
	private int beforeOffCounter = -1, beforeOffCount = 30; // in 0.1 of sec

	private Object sema = new Object();

	enum TimerState {
		/** No trigger signal encountered. */
		Untriggered, 
		/** Triggered, counting before turning on. */
		CountDouwnOff, 
		/** Turned on, counting before turning off. */
		CountDownOn, 
		/** Turned off, waiting for trigger to release. */
		Finished 
	};

	TimerState timerState = TimerState.Untriggered;

	enum TimerMode {
		/** Turns on after given time until trigger is gone. */
		OneShotOn,
		/** Gives one pulse. */
		OneShotPulse,
		/** Oscillates as long as triger is true. */
		Oscillator,
	};

	TimerMode timerMode = TimerMode.OneShotOn;
	private boolean timerOut = false;

	public Timer() {
		super(100, Thread.NORM_PRIORITY, "Timer");
	}


	@Override
	protected void doDriverTask() throws Throwable {

		synchronized (sema) {
			switch(timerState)
			{
			case Untriggered:
				return;

			case CountDouwnOff:
				beforeOnCounter--;
				if(beforeOnCounter <= 0)
				{
					timerOut = true;
					beforeOnCounter = beforeOnCount;
					timerState =  (timerMode == TimerMode.OneShotOn) ? 
							TimerState.Finished : TimerState.CountDownOn;
				}
				break;

			case CountDownOn:

				beforeOffCounter--;
				if(beforeOffCounter <= 0)
				{
					timerOut = false;
					beforeOffCounter = beforeOffCount;
					timerState = (timerMode == TimerMode.Oscillator) ? TimerState.CountDouwnOff : TimerState.Finished;				
				}
				break;

			case Finished:
				return;
			}

			trySend();
		}

	}



	@Override
	protected void doStartDriver() throws CommunicationsException {
	}

	@Override
	protected void doStopDriver() throws CommunicationsException {
	}

	@Override
	public String getDeviceName() { return "Timer"; }

	@Override
	public boolean isAutoSeachSupported() { return false; }


	@Override
	protected void updatePorts(Vector<DriverPort> ports) {
		DriverPort.absentize(ports);
		DriverPort.sortPorts(ports);

		in1 = getPort(ports, 0, Direction.Output, Type.Numeric, "Trigger In");
		//in1.setDescription("Value to subtract from.");
		in1.setPortDataOutput(new PortDataOutput() {

			@Override
			public void receiveDoubleData(double value) {
				synchronized (sema) {
					inTriggerValue = doubleToBoolean(value) ? (!triggerInvert) : triggerInvert;
					log.log(Level.FINEST,"trigger = "+value);

					trySend();
				}
			}});

		in2 = getPort(ports, 1, Direction.Output, Type.Numeric, "Reset In");
		in2.setDescription("Forces output to be false.");
		in2.setPortDataOutput(new PortDataOutput() {

			@Override
			public void receiveDoubleData(double value) {
				synchronized (sema) {
					inResetValue = doubleToBoolean(value);
					log.log(Level.FINEST,"reset = "+value);
					trySend();
				}
			}});

		in3 = getPort(ports, 2, Direction.Output, Type.Numeric, "Set In");
		in3.setDescription("Forces output to be true (but reset overrides).");
		in3.setPortDataOutput(new PortDataOutput() {

			@Override
			public void receiveDoubleData(double value) {
				synchronized (sema) {
					inSetValue = doubleToBoolean(value);
					log.log(Level.FINEST,"set = "+value);
					trySend();
				}
			}});

		in4 = getPort(ports, 3, Direction.Output, Type.Numeric, "Enable In");
		in4.setDescription("Enables trigger.");
		in4.setPortDataOutput(new PortDataOutput() {

			@Override
			public void receiveDoubleData(double value) {
				synchronized (sema) {
					inEnableValue = doubleToBoolean(value);
					log.log(Level.FINEST,"enable = "+value);
					trySend();
				}
			}});


		directResultPort = getPort(ports, 4, Direction.Input, Type.Numeric, "Out (direct)");
		invertedResultPort = getPort(ports, 5, Direction.Input, Type.Numeric, "Out (inverted)");
	}

	// -----------------------------------------------------------
	// Implementation
	// -----------------------------------------------------------

	/** called in sema */
	protected void trySend() {

		if(inTriggerValue && inEnableValue && timerState == TimerState.Untriggered)
		{
			// Start
			timerState = TimerState.CountDouwnOff;
			beforeOffCounter = beforeOffCount;
			beforeOnCounter = beforeOnCount;
		}
		/*
		if(inTriggerValue == false && timerState == TimerState.Finished)
			timerState = TimerState.Untriggered;

		if(inTriggerValue == false && timerMode == TimerMode.Oscillator)
			timerState = TimerState.Untriggered;
		 */

		if(inTriggerValue == false)
		{
			// Stop
			timerState = TimerState.Untriggered;
			timerOut = false;
		}

		boolean result = (timerOut || inSetValue ) && (!inResetValue);
		directResultPort.sendBooleanData(result);
		invertedResultPort.sendBooleanData(!result);
		log.log(Level.FINEST,"out = "+result);
	}

	// -----------------------------------------------------------
	// UI 
	// -----------------------------------------------------------

	private JTextField offTimeField = new JTextField(6);
	private JTextField onTimeField = new JTextField(6);

	private JComboBox modeField = new JComboBox(TimerMode.values());
	private JCheckBox triggerInvertField = new JCheckBox();
	
	@Override
	protected void setupPanel(JPanel panel) {
		
		panel.add(new JLabel("Off time, x0.1 sec:"), consL);
		panel.add(offTimeField, consR);
		offTimeField.setToolTipText("Time after trigger before output will went true.");
		
		panel.add(new JLabel("On time, x0.1 sec:"), consL);
		panel.add(onTimeField, consR);
		onTimeField.setToolTipText("Time for output to be true (in one shot pulse mode).");
		
		panel.add(new JLabel("Mode:"), consL);
		panel.add(modeField, consR);
		modeField.setToolTipText(
				"OneShotOn - Turns on after given time until trigger is gone."+
				"OneShotPulse - Gives one pulse"+
				"Oscillator - pulses on/off continuously"
				);
		
		panel.add(new JLabel("Invert trigger:"), consL);
		panel.add(triggerInvertField, consR);

	}

	@Override
	protected void doLoadPanelSettings() {

		offTimeField.setText(""+beforeOnCount);
		onTimeField.setText(""+beforeOffCount);

		modeField.setSelectedItem(timerMode);
		triggerInvertField.setSelected(triggerInvert);
	}

	@Override
	protected void doSavePanelSettings() {

		try { beforeOnCount = Integer.parseInt( offTimeField.getText() ); }
		catch(NumberFormatException e) {};
		
		try { beforeOffCount = Integer.parseInt( onTimeField.getText() ); }
		catch(NumberFormatException e) {};

		timerMode = (TimerMode) modeField.getSelectedItem();

		triggerInvert = triggerInvertField.isSelected();
	}

	@Override
	public String getInstanceName() {
		return "N/A";
	}


	public int getBeforeOnCount() {		return beforeOnCount;	}
	public void setBeforeOnCount(int beforeOnCount) {		this.beforeOnCount = beforeOnCount;	}

	public int getBeforeOffCount() {		return beforeOffCount;	}
	public void setBeforeOffCount(int beforeOffCount) {		this.beforeOffCount = beforeOffCount;	}




}
