package ru.dz.shipMaster.config.items;

import ru.dz.shipMaster.av.audio.SoundPlayer;
import ru.dz.shipMaster.config.ConfigListItem;
import ru.dz.shipMaster.config.ConfigurationFactory;
import ru.dz.shipMaster.data.history.HistoryAlarmRecord;
import ru.dz.shipMaster.ui.config.item.CieAlarm;
import ru.dz.shipMaster.ui.logger.LogWindow;
import ru.dz.shipMaster.ui.misc.AlarmRegion;
import ru.dz.shipMaster.ui.misc.AlarmRegionSet;

// XXX if one alarm is used twice, shit happens. Must instantiate own copies for users. 

public class CliAlarm extends ConfigListItem {
	public enum Type { 
		/**
		 * Alarm will fire on value getting too low. 
		 */
		LowerForbidden, 
		/**
		 * Alarm will fire on value getting too high.
		 */
		UpperForbidden, 
		/**
		 * Alarm will fire on value getting into the given interval (too low OR too high).
		 */
		IntervalForbidden, 
		/**
		 * Alarm will fire on value getting too low or too high (too low AND too high).
		 */
		SidesForbidden };

		private String 	name = "New alarm";
		private Type	type = Type.UpperForbidden;
		private double	lowWarning = -1;
		private double	lowCritical = -1;
		private double	hiWarning = 1;
		private double	hiCritical = 1;

		private String  soundFile = null;
		
		private CliAlarmStation		station;

		//private LogWindow logWindow;


		public CliAlarm() {
		}

		/**
		 * Used in test only.
		 * @param name
		 */
		public CliAlarm(String name) {
			this.name = name;
		}


		@Override
		public void destroy() {
			station = null;
			dialog = null;			
		}


		@Override
		public String toString() {
			switch(type)
			{
			case UpperForbidden:
				return String.format("%s: %.1f - %.1f - MAX", name, hiWarning,hiCritical);

			case LowerForbidden:
				return String.format("%s: MIN - %.1f - %.1f", name, lowCritical,lowWarning);

			case IntervalForbidden:
				return String.format("%s: %.1f - %.1f - %.1f - %.1f", name, hiWarning,hiCritical,lowCritical,lowWarning);

			case SidesForbidden:
				return String.format("%s: MIN - %.1f - %.1f  +  %.1f - %.1f - MAX", name, lowCritical,lowWarning, hiWarning,hiCritical);
			}

			return name;
		}


		public AlarmRegionSet getRegions(boolean user)
		{
			AlarmRegionSet out = new AlarmRegionSet();

			switch(type)
			{
			case UpperForbidden:
				out.add(new AlarmRegion(hiWarning,hiCritical,false,user));
				out.add(new AlarmRegion(hiCritical,Double.MAX_VALUE,true,user));
				break;

			case LowerForbidden:
				out.add(new AlarmRegion(-Double.MAX_VALUE,lowCritical,true,user));
				out.add(new AlarmRegion(lowCritical,lowWarning,false,user));
				break;

			case IntervalForbidden:
				out.add(new AlarmRegion(hiWarning,hiCritical,false,user));
				out.add(new AlarmRegion(hiCritical,lowCritical,true,user));
				out.add(new AlarmRegion(lowCritical,lowWarning,false,user));
				break;

			case SidesForbidden:
				out.add(new AlarmRegion(hiWarning,hiCritical,false,user));
				out.add(new AlarmRegion(hiCritical,Double.MAX_VALUE,true,user));
				out.add(new AlarmRegion(-Double.MAX_VALUE,lowCritical,true,user));
				out.add(new AlarmRegion(lowCritical,lowWarning,false,user));
				break;		
			}

			return out;
		}

		// -----------------------------------------------------------------------
		// Warning and critical levels triggering logic
		// -----------------------------------------------------------------------


		/**
		 * Object of this class is used to translate to meters (or other listeners)
		 * information about how near current value to warning or critical levels.
		 * @author dz
		 */
		public class NearStatus { public double nearWarningLevel; public double nearCriticalLevel; }

		public AlarmProcessor getProcessor(String name)
		{
			return new AlarmProcessor(name);
		}

		static private SoundPlayer player = new SoundPlayer();
		
		static public void stopSound()
		{
			player.stop();
		}
		
		public class AlarmProcessor
		{
			protected boolean isInWarning = false;
			protected boolean isInCritical = false;
			private final String apName;

			public AlarmProcessor(String name) {
				apName = name;
			}

			public String getName() { return apName; }
			
			/** 
			 * Do alarm processing on the parameter.
			 * @param currVal input value to process
			 * @param aperture Difference between upper and lower meter (parameter value) limits. Used to define what 10% is.
			 * @param out Information about how near this value to the warning or critical levels.  
			 */
			public void processValue(double currVal, double aperture, NearStatus out) {

				double nw = nearWarn(currVal,aperture);
				boolean nowWarning = isInWarning ? nw > 0.95 : nw > 0.999;

				double nc = nearCrit(currVal,aperture);
				boolean nowCritical = isInCritical ? nc > 0.95 : nc > 0.999;
//System.out.println("CliAlarm.AlarmProcessor.processValue() "+currVal);
//System.out.println("CliAlarm.AlarmProcessor.processValue() nearCrit "+nc);
				if(out != null)
				{
					out.nearCriticalLevel = nc;
					out.nearWarningLevel = nw;
				}

				if(nowWarning != isInWarning)
				{
					isInWarning = nowWarning;
					triggerWarning(nowWarning,currVal);
				}

				if(nowCritical != isInCritical )
				{
					isInCritical = nowCritical;
					triggerCritical(nowCritical,currVal);
				}
			}

			public boolean isTriggered() { return isInCritical; }



			private String mkValueMessage(String text, double currVal)
			{
				// TODO units for alarm
				return String.format("%s - %s: %.1f", name, text, currVal);
			}

			/**
			 * Called when value changes from normal to warning level or back.
			 * @param nowWarning is true if now value is in warning zone;
			 * @param value current value to display
			 */
			protected void triggerWarning(boolean nowWarning, double value)
			{
				propagateAlarm(nowWarning, true, mkValueMessage(nowWarning ? "опасность" : "норма", value));
			}

			/**
			 * Called when value changes from normal to critical level or back.
			 * @param nowCritical is true if now value is in critical zone;
			 * @param value current value to display
			 */
			protected void triggerCritical(boolean nowCritical, double value)
			{
				propagateAlarm(nowCritical, true, mkValueMessage(nowCritical ? "авария" : "конец аварии", value));
			}



			private void propagateAlarm(boolean start, boolean critical, String text )
			{
				LogWindow logWindow = ConfigurationFactory.getTransientState().getDashBoard().getLogWindow();
				if(logWarnAndCritical && logWindow != null)
				{
					if(critical && start)	logWindow.addCriticalMessage(text);
					else					logWindow.addMessage(text);
				}

				ConfigurationFactory.getTransientState().addHistoryRecord(new HistoryAlarmRecord(critical, text, start));	
				if(station != null && critical)
				{
					if(start) station.registerAlarm(this);
					else station.deRegisterAlarm(this);
				}

				if(critical && start && soundFile != null && soundFile.trim().length() > 0 
						&& !ConfigurationFactory.getConfiguration().getGeneral().isSuppressAlarms())
					player.start(soundFile);				
			}

		}


		protected boolean logWarnAndCritical = true;
		public boolean isLogWarnAndCritical() {		return logWarnAndCritical;	}
		public void setLogWarnAndCritical(boolean logWarnAndCritical) {		this.logWarnAndCritical = logWarnAndCritical;	}




		/** 
		 * Defines what 'warning value' is. 
		 * @param aperture Difference between upper and lower meter (parameter value) limits. Used to define what 10% is.
		 * @return 0 if value is 'far' from warning threshold (more then 10% from) 
		 * and 1 if equals or over. Changes from 0 to 1 in between.  
		 */
		public double nearWarn(double value, double aperture)
		{
			double ret;
			switch(type)
			{
			case LowerForbidden: ret = lowNearWarn(value, aperture, lowWarning); break;
			case UpperForbidden: ret = hiNearWarn(value, aperture, hiWarning); break;
			case SidesForbidden: ret = Math.max(lowNearWarn(value, aperture, lowWarning), hiNearWarn(value, aperture, hiWarning)); break;
			default: ret = lowNearWarn(value, aperture, lowWarning) * hiNearWarn(value, aperture, hiWarning); break;
			}


			if( ret > 1) ret = 1;
			if( ret < 0) ret = 0;

			return ret;
		}



		/**
		 * Defines what 'critical value' is. 
		 * @param aperture Difference between upper and lower meter (parameter value) limits. Used to define what 10% is.
		 * @return 0 if value is 'far' from critical threshold (more then 10% from) 
		 * and 1 if equals or over. Changes from 0 to 1 in between.  
		 */
		public double nearCrit(double value, double aperture)
		{
			double ret;
			switch(type)
			{
			case UpperForbidden: ret = hiNearWarn(value, aperture, hiCritical); break;
			case LowerForbidden: ret = lowNearWarn(value, aperture, lowCritical); break;
			case SidesForbidden: ret = Math.max(lowNearWarn(value, aperture, lowCritical), hiNearWarn(value, aperture, hiCritical)); break;
			default: ret = lowNearWarn(value, aperture, lowCritical) * hiNearWarn(value, aperture, hiCritical); break;
			}

			if( ret > 1) ret = 1;
			if( ret < 0) ret = 0;

			return ret;
		}

		private double hiNearWarn(double value, double aperture, double hiLimit)
		{
			double warnDown10percent = hiLimit - (aperture*0.1f);
			return (value-warnDown10percent)/(hiLimit-warnDown10percent);
		}

		private double lowNearWarn(double value, double aperture, double lowLimit)
		{
			double warnUp10percent = lowLimit + (aperture*0.1f);
			return (warnUp10percent - value)/(warnUp10percent - lowLimit);
		}

		// Dialog

		private CieAlarm dialog = null;

		@Override
		public void displayPropertiesDialog() {
			if(!hasEditRight()) return;
			if(dialog == null) dialog = new CieAlarm(this);
			dialog.displayPropertiesDialog(); 
		}

		// getters/setters

		@Override
		public String getName() { return name;	}
		public void setName(String name) {		this.name = name;	}

		public double getHiCritical() {		return hiCritical;	}
		public void setHiCritical(double hiCritical) {		this.hiCritical = hiCritical;	}

		public double getHiWarning() {		return hiWarning;	}
		public void setHiWarning(double hiWarning) {		this.hiWarning = hiWarning;	}

		public double getLowCritical() {		return lowCritical;	}
		public void setLowCritical(double lowCritical) {		this.lowCritical = lowCritical;	}

		public double getLowWarning() {		return lowWarning;	}
		public void setLowWarning(double lowWarning) {		this.lowWarning = lowWarning;	}

		public Type getType() {		return type;	}
		public void setType(Type type) {		this.type = type;	}

		//public LogWindow getLogWindow() {		return logWindow;	}
		//public void setLogWindow(LogWindow logWindow) {		this.logWindow = logWindow;	}

		public CliAlarmStation getStation() {		return station;	}
		public void setStation(CliAlarmStation station) {		this.station = station;	}

		public String getSoundFile() {			return soundFile;		}
		public void setSoundFile(String soundFile) {			this.soundFile = soundFile;		}



}
