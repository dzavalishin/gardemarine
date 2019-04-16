package ru.dz.shipMaster.dev.loop;

import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;

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
 * Motor hours counter (soft driver).
 * @author dz
 */
public class TimeCounter extends ThreadedDriver {

	private static final String OLD_SFX = ".old";
	private static final String NEW_SFX = ".new";
	private DriverPort enablePort;
	protected boolean counterEnabled = false;
	private DriverPort counterValuePort;
	private long value = 0;

	public TimeCounter() {
		super(60000, Thread.NORM_PRIORITY, "TimeCounter");
	}

	@Override
	protected void doDriverTask() throws Throwable {
		if(counterEnabled)
			incrementCount();
	}



	@Override
	protected void doStartDriver() throws CommunicationsException {
		loadValue();
	}

	@Override
	protected void doStopDriver() throws CommunicationsException {
		saveValue();
	}

	@Override
	public String getDeviceName() { return "Time counter"; }

	@Override
	public boolean isAutoSeachSupported() { return false; }


	@Override
	protected void updatePorts(Vector<DriverPort> ports) {
		DriverPort.absentize(ports);
		DriverPort.sortPorts(ports);

		enablePort = getPort(ports, 0, Direction.Output, Type.Boolean, "Enable counter");
		enablePort.setDescription("When this output is true, counter will increment each minute.");
		enablePort.setPortDataOutput(new PortDataOutput() {

			@Override
			public void receiveDoubleData(double value) {
				counterEnabled = value > 0.5;
			}});
		
		counterValuePort = getPort(ports, 1, Direction.Input, Type.Numeric, "Counter value, minutes");
	}

	// -----------------------------------------------------------
	// UI 
	// -----------------------------------------------------------

	private JTextField fileNameField = new JTextField();
	private String fileName = "";


	public String getFileName() {		return fileName;	}
	public void setFileName(String fileName) {		this.fileName = fileName;	}

	@Override
	protected void setupPanel(JPanel panel) {
		panel.add(new JLabel("Store file name:"), consL);
		panel.add(fileNameField, consR);
	}

	@Override
	protected void doLoadPanelSettings() {
		fileNameField.setText(fileName);
	}

	@Override
	protected void doSavePanelSettings() {
		fileName = fileNameField.getText();
	}

	
	// -----------------------------------------------------------
	// Implementation
	// -----------------------------------------------------------

	static private final int MAX_DIFF = 10;
	private int diff = 0;
	private void incrementCount() {
		value ++;
		counterValuePort.sendDoubleData(value);
		if(diff++ > MAX_DIFF)
		{
			diff = 0;
			saveValue();
		}
	}

	private void loadValue() {
		try {
			doLoadValue();
		} catch (FileNotFoundException e) {
			log.log(Level.SEVERE,"exception",e);
		}
	}
	
	private void doLoadValue() throws FileNotFoundException {
		FileInputStream fin = new FileInputStream(new File(fileName));
		BufferedInputStream bin = new BufferedInputStream(fin,256*1024);
		XMLDecoder decoder = new XMLDecoder(bin,null);

		try {
			Long data = (Long) decoder.readObject();
			value = data;
		} catch (Throwable e) {
			log.log(Level.SEVERE,"Unable to load "+this,e);
		}

		decoder.close();
		try {
			bin.close();
		} catch (IOException e) {
			log.log(Level.SEVERE,"exception",e);
		}
	}

	private void saveValue() {
		try {
			doSaveValue();
		} catch (FileNotFoundException e) {
			log.log(Level.SEVERE,"exception",e);
		}
	}
	
	private void doSaveValue() throws FileNotFoundException {
		File tmp = new File(fileName+NEW_SFX);
		FileOutputStream fout = new FileOutputStream(tmp);
		BufferedOutputStream bout = new BufferedOutputStream(fout, 128*1024);
		XMLEncoder encoder = new XMLEncoder(bout);

		encoder.setOwner(null);

		encoder.setExceptionListener(new ExceptionListener() {
			public void exceptionThrown(Exception exception) {
				exception.printStackTrace();
			}
		});

		Long data = value;
		encoder.writeObject(data);

		encoder.flush();
		encoder.close();
		try {
			bout.close();
			File killOld = new File(fileName+OLD_SFX);
			killOld.delete();

			File old = new File(fileName);
			old.renameTo(new File(fileName+OLD_SFX));
			tmp.renameTo(new File(fileName));
		} catch (IOException e) {
			log.log(Level.SEVERE,"Unable to save "+this,e);
		}
	}

	@Override
	public String getInstanceName() {
		return fileName;
	}

	@Override
	public String toString() {
		return "timecounter ("+fileName+")";
	}

}
