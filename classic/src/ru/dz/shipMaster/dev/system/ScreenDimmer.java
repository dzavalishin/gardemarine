package ru.dz.shipMaster.dev.system;

import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ru.dz.shipMaster.config.ConfigurationFactory;
import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.dev.AbstractDriver;
import ru.dz.shipMaster.dev.DriverPort;
import ru.dz.shipMaster.dev.PortDataOutput;
import ru.dz.shipMaster.ui.DimmerComponent;

public class ScreenDimmer extends AbstractDriver {

	private DriverPort dimPort;

	private double maximalDimLevel = 0.8;
	private JTextField maxDimField = new JTextField(6);

	@Override
	protected void doLoadPanelSettings() {
		maxDimField.setText(""+Double.toString(maximalDimLevel));
	}

	@Override
	protected void doSavePanelSettings() {
		maximalDimLevel = Double.parseDouble( maxDimField.getText() );		
	}

	@Override
	protected void doStartDriver() throws CommunicationsException {
	}

	@Override
	protected void doStopDriver() throws CommunicationsException {
	}

	@Override
	public String getDeviceName() {		return "UI Dimmer";	}

	@Override
	public String getInstanceName() {		return "internal";	}

	@Override
	public boolean isAutoSeachSupported() {		return false;	}



	@Override
	protected void setupPanel(JPanel panel) {	
		panel.add(new JLabel("Max dim level"),consL);
		panel.add(maxDimField,consR);
	}

	private final PortDataOutput dimPortDataOutput = new PortDataOutput() {
		@Override
		public void receiveDoubleData(double value) { 
			setDimLevel(value); 
		}
	};

	@Override
	protected void updatePorts(Vector<DriverPort> ports) {
		DriverPort.absentize(ports);
		DriverPort.sortPorts(ports);

		{
			dimPort = getPort(ports,0,Direction.Output,Type.Numeric,"Dimmer");
			dimPort.setPortDataOutput(dimPortDataOutput);
		}

		for(int i = 0; i < 10; i++)
		{
			DriverPort port = getPort(ports, i+1, Direction.Output, Type.Boolean, "Window "+i);
			port.setDescription("Bring window "+i+" to front if value is 1");

			final int screenNo = i;

			port.setPortDataOutput(new PortDataOutput() {
				@Override
				public void receiveDoubleData(double value) {
					if(value > 0.01) selectScreen(screenNo);					
				}});
		}

		{
			dimPort = getPort(ports,11,Direction.Output,Type.Numeric,"Hide windows");
			dimPort.setPortDataOutput(new PortDataOutput() {
				@Override
				public void receiveDoubleData(double value) {
					if(value > 0.01) hideScreens();					
				}});
		}

		{
			dimPort = getPort(ports,12,Direction.Output,Type.Numeric,"Bring alerts window (NOT IMPL)");
			dimPort.setPortDataOutput(new PortDataOutput() {
				@Override
				public void receiveDoubleData(double value) {
					if(value > 0.01) showAlertsWindow();					
				}});
		}

		{
			dimPort = getPort(ports,13,Direction.Output,Type.Numeric,"Bring alerts history window");
			dimPort.setPortDataOutput(new PortDataOutput() {
				@Override
				public void receiveDoubleData(double value) {
					if(value > 0.01) showAlertsHistoryWindow();					
				}});
		}
		
	}



	protected void showAlertsHistoryWindow() {
		ConfigurationFactory.openEventLogWindow();
	}

	protected void showAlertsWindow() {
	}

	private static Vector<JFrame> frames = new Vector<JFrame>(4);

	/**
	 * Bring to front.
	 * @param screenNo
	 */
	protected static void selectScreen(int screenNo) {
		if(screenNo >= frames.size())
			return;
		JFrame frame = frames.get(screenNo);
		log.fine("sel win "+screenNo+" = "+frame);
		if(frame != null)
		{
			frame.setVisible(true);
			frame.toFront();
		}
	}

	protected void hideScreens() {
		for( JFrame f : frames )
			f.setVisible(false);		
	}

	
	/**
	 * Register frame so that it can be brought to front with this driver.
	 * @param f Frame
	 * @param no index
	 */
	public static void registerFrame(JFrame f, int no)
	{
		frames.setSize(Math.max(frames.size(),no+1));
		frames.set(no, f);
	}




	protected void setDimLevel(double level)
	{
		DimmerComponent.setDimLevel(Math.min( level, maximalDimLevel ));
	}


	/**
	 * @return Maximum dim level for this driver.
	 */
	public double getMaximalDimLevel() {		return maximalDimLevel;	}

	/**
	 * Driver will limit dim level with this to prevent complete blackout. Note that max level is
	 * itself limited to 0.9 just for any case.
	 * @param maximalDimLevel Maximum dim level for this driver.
	 */
	public void setMaximalDimLevel(double maximalDimLevel) {		this.maximalDimLevel = Math.min( maximalDimLevel, 0.9 );	}


}
