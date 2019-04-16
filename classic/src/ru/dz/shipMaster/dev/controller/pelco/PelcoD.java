package ru.dz.shipMaster.dev.controller.pelco;

import java.util.Vector;

import javax.swing.JPanel;

import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.dev.DriverPort;
import ru.dz.shipMaster.dev.ThreadedDriver;

public class PelcoD extends ThreadedDriver {

	protected PelcoD() {
		super(1000, Thread.NORM_PRIORITY, "PelcoD protocol");
	}

	@Override
	protected void doDriverTask() throws Throwable {
	}

	@Override
	protected void doStartDriver() throws CommunicationsException {
	}

	@Override
	protected void doStopDriver() throws CommunicationsException {
	}

	@Override
	protected void updatePorts(Vector<DriverPort> ports) {
	}

	@Override
	protected void doLoadPanelSettings() {
	}

	@Override
	protected void doSavePanelSettings() {
	}

	@Override
	protected void setupPanel(JPanel panel) {
	}

	@Override
	public String getDeviceName() {
		return "PelcoD";
	}

	@Override
	public String getInstanceName() {
		return null;
	}

	@Override
	public boolean isAutoSeachSupported() {
		return false;
	}

}
