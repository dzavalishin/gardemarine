package ru.dz.shipMaster.dev.misc;

import java.util.Vector;

import javax.swing.JPanel;

import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.dev.DriverPort;
import ru.dz.shipMaster.dev.ThreadedDriver;

public class TestSignalDriver extends ThreadedDriver {

	public TestSignalDriver() {
		super(1600, Thread.MAX_PRIORITY, "Test signal driver");
	}

	@Override
	protected void setupPanel(JPanel panel) {
	}

	@Override
	protected void doLoadPanelSettings() {
	}

	@Override
	protected void doSavePanelSettings() {
	}

	@Override
	protected void doStartDriver() throws CommunicationsException {
	}

	@Override
	protected void doStopDriver() throws CommunicationsException {
	}

	@Override
	public String getDeviceName() {
		return "Test signal generator";
	}

	@Override
	public boolean isAutoSeachSupported() {
		return false;
	}

	private DriverPort meanderPort;
	private DriverPort sinusPort;
	private DriverPort randomPort;
	@Override
	protected void updatePorts(Vector<DriverPort> ports) {

		DriverPort.absentize(ports);
		DriverPort.sortPorts(ports);
		
		{
			meanderPort = getPort(ports,0);
			meanderPort.setDirection(Direction.Input);
			meanderPort.setHardwareName("Meander");
			meanderPort.setType(Type.Numeric);
		}

		{
			sinusPort = getPort(ports,1);
			sinusPort.setDirection(Direction.Input);
			sinusPort.setHardwareName("Sinus");
			sinusPort.setType(Type.Numeric);
		}
		
		{
			randomPort = getPort(ports,2);
			randomPort.setDirection(Direction.Input);
			randomPort.setHardwareName("Random");
			randomPort.setType(Type.Numeric);
		}
		
		/*{
		DriverPort port = getPort(ports,0);
		port.setDirection(Direction.Output);
		port.setHardwareName("TextToShow.Red");
		port.setType(Type.String);
		}*/
	}

	private boolean lastValue = false;
	private double randomPortValue = 0;
	
	@Override
	protected void doDriverTask() throws Throwable {
		lastValue = !lastValue;

		if(meanderPort != null)
			meanderPort.sendDoubleData(lastValue ? 1.0 : 0.0);
		
		if(sinusPort != null)
			sinusPort.sendDoubleData(Math.sin(System.currentTimeMillis()/100));
		
		if( randomPort != null)
		{
			randomPortValue += (Math.random() - 0.5);
			if(randomPortValue > 1) randomPortValue = 1;
			if(randomPortValue < -1) randomPortValue = -1;
			randomPort.sendDoubleData(randomPortValue);
		}
	}

	@Override
	public String getInstanceName() {
		return "internal";
	}

}
