package ru.dz.shipMaster.config.items;

import java.awt.Image;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import ru.dz.shipMaster.config.ConfigListItem;
import ru.dz.shipMaster.config.items.CliAlarm.AlarmProcessor;
import ru.dz.shipMaster.data.AbstractMinimalDataSink;
import ru.dz.shipMaster.dev.PortDataSource;
import ru.dz.shipMaster.ui.AlarmsFrame;
import ru.dz.shipMaster.ui.config.item.CieAlarmStation;

public class CliAlarmStation extends ConfigListItem {
		private String place = "New";
		private int escalationTime = 180;
		private String password = "";
		private CliAlarmStation nextStation = null;
	
		private static Timer timer = new Timer("Alarm Escalation timer",true); 
		protected Set<AlarmProcessor> alarms = new HashSet<AlarmProcessor>();
		/**
		 * Target parameter to communicate with. This parameter supposed to control
		 * alarm hardware.
		 */
		private CliParameter target;

		/**
		 * Acknowledge input parameter. Must be connected to the button user will press
		 * to acknowledge alarm.
		 */
		private CliParameter ackInput;
		
		//private int activeAlarms = 0;

		@Override
		public void destroy() {
			target = null;
			ackInput = null;
			alarms = null;
			nextStation = null;
			timer = null;
			dialog = null;			
		}

		
		@Override
		public String getName() {
			StringBuilder sb = new StringBuilder();
			
			sb.append(place);
			
			//if( target != null || ackInput != null )
			{
				sb.append( " (Out=" );
				sb.append( target == null ? "--" : target.getName() );
				sb.append(  ", In=" );
				sb.append( ackInput == null ? "--" : ackInput.getName() );
				sb.append(  ")" );
			}
			return sb.toString();
		}
		
		private CieAlarmStation dialog = null;
		
		@Override
		public void displayPropertiesDialog() {
			if(!hasEditRight()) return;
			if(dialog == null) dialog = new CieAlarmStation(this);
			dialog.displayPropertiesDialog(); 
			}
		
		public void registerAlarm(AlarmProcessor alarmProcessor) {
			synchronized (alarms) {
				//activeAlarms++;
				alarms.add(alarmProcessor);
				timer.schedule(new AlarmEscalationTask(alarmProcessor), escalationTime*1000L);
			}
			startStopStation();
		}
		
		public void deRegisterAlarm(AlarmProcessor alarmProcessor) {
			synchronized (alarms) {
				//activeAlarms--;
				alarms.remove(alarmProcessor);
				if(nextStation != null) nextStation.deRegisterAlarm(alarmProcessor);
				/* 
				 * It is better to remove pending request from timer queue, but we
				 * can't. So if some alarm will be fired, then disarmed before escalation is 
				 * to be done and then fired again, it will be escalated too soon. But I believe
				 * that's actually ok.
				 */
				
			}			
			startStopStation();
		}
		
		/**
		 * Start or stop actual hardware alarm.
		 */
		private void startStopStation() {
			synchronized (alarms) {
				int alarmsCount = alarms.size();
				if(alarmsCount > 0)
					startAlarmHardware();
				else
					stopAlarmHardware();
			}
			updateAlarmsWindow();
		}

		
		class AlarmEscalationTask extends TimerTask
		{
			private final AlarmProcessor alarm;

			public AlarmEscalationTask(AlarmProcessor alarmProcessor)
			{
				this.alarm = alarmProcessor;				
			}

			@Override
			public void run() {
				synchronized (alarms) {
					if(nextStation != null && alarms.contains(alarm))						
						nextStation.registerAlarm(alarm);
				}				
			}
			
		}
		
		// TODO Alarm item on dashboard?!
		protected PortDataSource portDataSource = new PortDataSource(0,1,getName(),"");

		/**
		 * Actually start hardware alarm and, if possible,
		 * display alarm information on alarm station.
		 */
		private void startAlarmHardware() {
			if(portDataSource != null)
				portDataSource.setValue(1);
		}

		/**
		 * Stop hardware alarm.
		 */
		private void stopAlarmHardware() {
			if(portDataSource != null)
				portDataSource.setValue(0);
		}

		/**
		 * To be called from hardware alarm station 'Acknowledge' button.
		 */
		public void acknowledgeAlarms() {
			synchronized (alarms) {
				alarms.clear();
				stopAlarmHardware();
			}			
		}
		
		AbstractMinimalDataSink dataSink = new AbstractMinimalDataSink(){
			@Override
			public void receiveImage(Image val) {
				// Ignore images				
			}
			
			@Override
			public void receiveString(String val) {
				// Ignore string				
			}
			
			@Override
			public void setCurrent(double value) {
				if(value > 0.5)
					acknowledgeAlarms();
			}};

		
		// getters/setters
		
		public CliAlarmStation getNextStation() {			return nextStation;		}
		public void setNextStation(CliAlarmStation nextStation) {			this.nextStation = nextStation;		}

		public String getPlace() { return place; }
		public void setPlace(String place) {		this.place = place;	}

		public String getPassword() { return password; }
		public void setPassword(String password) { this.password = password; 	}

		public int getEscalationTime() {			return escalationTime;		}
		public void setEscalationTime(int escalationTime) {			this.escalationTime = escalationTime; }

		public boolean isUseAlarmsWindow() {			return useAlarmsWindow;		}
		public void setUseAlarmsWindow(boolean useAlarmsWindow) {			this.useAlarmsWindow = useAlarmsWindow;		}

		public boolean isSoundEnabled() {			return soundEnabled;		}
		public void setSoundEnabled(boolean soundEnabled) {			this.soundEnabled = soundEnabled;		}

		
		public CliParameter getTarget() {			return target;		}

		/**
		 * Set target to be used as alarm hardware conrol output.
		 * @param target Parameter used to control alarm sound on/off.
		 */
		public void setTarget(CliParameter target) {
			if(this.target == target)
				return;
			
			if(this.target != null)
			{
				this.target.setDataSource(null);
			}
			
			this.target = target;
			this.target.setDataSource(portDataSource);
			}

		public CliParameter getAckInput() {			return ackInput;		}

		/**
		 * Acknowledge input parameter. Must be connected to the button user will press
		 * to acknowledge alarm.
		 */
		public void setAckInput(CliParameter ackInput) 
		{			
			if(this.ackInput == ackInput)
				return;
			
			if(this.ackInput != null)
				this.ackInput.getDataSource().removeMeter(dataSink);
			
			this.ackInput = ackInput;
			if(this.ackInput != null)
				this.ackInput.getDataSource().addMeter(dataSink);
		}

		
		// ----------------------------------------------------------------
		// Alarms window
		// ----------------------------------------------------------------
		
		private boolean useAlarmsWindow = true;
		private boolean soundEnabled = true;
		
		//private boolean displayAlarmsWindow = true;
		private AlarmsFrame alarmWindow;
		
		private void updateAlarmsWindow()
		{
			synchronized (alarms) {
				if(!useAlarmsWindow)
					return;

				
				if(alarmWindow == null) alarmWindow = new AlarmsFrame(this);
				
			}			
			alarmWindow.update();
		}


		public Set<AlarmProcessor> getActiveAlarms() { return alarms; }


}
