package ru.dz.shipMaster.dev.nmeaSentence;

import java.util.Vector;
import java.util.logging.Level;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.dev.DriverPort;

// TODO correct PC clock option
//            ----------------- NOT DONE -----------------------------

/*
 ZDA - Time & Date - UTC, day, month, year and local time zone

	1         2  3  4    5  6  7
        |         |  |  |    |  |  |
 $--ZDA,hhmmss.ss,xx,xx,xxxx,xx,xx*hh<CR><LF>

 Field Number:
  1) UTC time (hours, minutes, seconds, may have fractional subsecond)
  2) Day, 01 to 31
  3) Month, 01 to 12
  4) Year (4 digits)
  5) Local zone description, 00 to +- 13 hours
  6) Local zone minutes description, apply same sign as local hours
  7) Checksum

Example: $GPZDA,160012.71,11,03,2004,-1,00*7D

 */


public class NmeaZDA extends GeneralNmeaSentenceDriver {
	private DriverPort datePort, tzPort, timePort;


	@Override
	public String getSentenceId() { return "ZDA"; }

	

	
	private JCheckBox updateClockCheckbox = new JCheckBox("");
	private boolean updateClock = false;
	
	@Override
	protected void setupPanel(final JPanel panel) {
		super.setupPanel(panel);
		panel.add(new JLabel("Update local clock"),consL);
		panel.add( updateClockCheckbox, consR);
		
		updateClockCheckbox.setToolTipText("not implemented yet");
		updateClockCheckbox.setEnabled(false);
	}

	@Override
	public void doLoadPanelSettings() {
		super.doLoadPanelSettings();
		updateClockCheckbox.setSelected(updateClock);
	}	
	
	@Override
	public void doSavePanelSettings() {
		super.doSavePanelSettings();
		updateClock = updateClockCheckbox.isSelected();
	}
	
	@Override
	protected void updatePorts(Vector<DriverPort> ports) {
		timePort = getPort(ports, 0, Direction.Input, Type.String, "Time string HHMMSS.FF");
		datePort = getPort(ports, 1, Direction.Input, Type.String, "Date string, D/M/Y");
		tzPort = getPort(ports, 2, Direction.Input, Type.String, "TZ string, HH:MM");
	}

	@Override
	public void parse(NMEA0183Sentence s) throws InvalidNMEASentenceException {
		checkValidity(s);

		try { 			
			timePort.sendStringData( s.getField(0) );
			datePort.sendStringData( s.getField(1)+"/"+s.getField(2)+"/"+s.getField(3) );
			tzPort.sendStringData(s.getField(4)+":"+s.getField(5));
			
			showMessage( s.getField(0) );
		}		
		catch(NumberFormatException e) { 
			log.log(Level.SEVERE, "NAN in "+s, e);
		}

	}

	public boolean isUpdateClock() {		return updateClock;	}
	public void setUpdateClock(boolean updateClock) {		this.updateClock = updateClock; updateClockCheckbox.setSelected(updateClock);	}


}
