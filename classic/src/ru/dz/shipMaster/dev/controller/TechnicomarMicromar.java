package ru.dz.shipMaster.dev.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.dev.DriverPort;
import ru.dz.shipMaster.dev.PortDataOutput;
import ru.dz.shipMaster.dev.ThreadedDriver;

public class TechnicomarMicromar extends ThreadedDriver {

	protected static final int HOLDING_0 = 0;
	protected static final int CMD_START = 0x01;
	protected static final int CMD_STOP = 0x02;
	protected static final int CMD_F1 = 0x04;
	protected static final int CMD_F2 = 0x08;
	protected static final int CMD_RESET = 0x10;
	private static final int INPUT_HOLDING_REGISTERS = 16;

	private JTextField ipAddressField = new JTextField(20);
	private String ipAddressString = "192.168.1.103";

	private InetAddress ipAddress;
	private JTextField tcpPortField = new JTextField(6);
	private int tcpPort = 13033;

	private JTextField unitBaseField = new JTextField();
	private JCheckBox unitActiveCheckbox = new JCheckBox();
	private int unitBase = 1;

	private ModBusSerial protocolDriver = new ModBusSerial();
	private DriverPort in_HP_Pump;
	private DriverPort in_LP_Pump;
	private DriverPort in_3way_status;
	private DriverPort in_3way_discharge;
	private DriverPort in_auto_flush;
	private DriverPort in_water_produced;
	private DriverPort in_pumps_active;
	private DriverPort in_alarm_hi_pressure;
	private DriverPort in_alarm_lo_pressure;
	private DriverPort in_alarm_overload;
	private DriverPort in_salinity;
	private DriverPort in_low_pressure;
	private DriverPort in_high_pressure;
	private DriverPort in_water_temperature;
	private DriverPort in_current;
	private DriverPort in_freq;
	private DriverPort in_flush_time;

	public TechnicomarMicromar() {
		super(1000, Thread.NORM_PRIORITY, "Technicomar Micromar updater");
		protocolDriver.setAsciiMode(true);
	}




	@Override
	protected void updatePorts(Vector<DriverPort> ports) {

		DriverPort.absentize(ports);
		DriverPort.sortPorts(ports);

		{
			DriverPort startPort = getPort(ports, 0, Direction.Output, Type.Boolean, "Start Cmd" );
			startPort.setPortDataOutput(
					new PortDataOutput() {
						@Override
						public void receiveDoubleData(double value) {
							setRegister(HOLDING_0,CMD_START);
						}});
		}

		{
			DriverPort port = getPort(ports, 1, Direction.Output, Type.Boolean, "Stop Cmd" );
			port.setPortDataOutput(
					new PortDataOutput() {
						@Override
						public void receiveDoubleData(double value) {
							setRegister(HOLDING_0,CMD_STOP);
						}});
		}

		{
			DriverPort port = getPort(ports, 2, Direction.Output, Type.Boolean, "F1 Cmd" );
			port.setPortDataOutput(
					new PortDataOutput() {
						@Override
						public void receiveDoubleData(double value) {
							setRegister(HOLDING_0,CMD_F1);
						}});
		}

		{
			DriverPort port = getPort(ports, 3, Direction.Output, Type.Boolean, "F2 Cmd" );
			port.setPortDataOutput(
					new PortDataOutput() {
						@Override
						public void receiveDoubleData(double value) {
							setRegister(HOLDING_0,CMD_F2);
						}});
		}

		{
			DriverPort port = getPort(ports, 4, Direction.Output, Type.Boolean, "Reset Cmd" );
			port.setPortDataOutput(
					new PortDataOutput() {
						@Override
						public void receiveDoubleData(double value) {
							setRegister(HOLDING_0,CMD_RESET);
						}});
		}


		in_HP_Pump = getPort(ports, 16, Direction.Input, Type.Boolean, "High pressure pump active" );
		in_LP_Pump = getPort(ports, 17, Direction.Input, Type.Boolean, "Low pressure pump active" );
		in_3way_status = getPort(ports, 18, Direction.Input, Type.Boolean, "3-way valve status" );
		in_3way_discharge = getPort(ports, 19, Direction.Input, Type.Boolean, "3-way discharge overboard" );
		in_auto_flush = getPort(ports, 20, Direction.Input, Type.Boolean, "Automatic flushing" );

		in_water_produced = getPort(ports, 21, Direction.Input, Type.Boolean, "Water produced" );
		in_pumps_active = getPort(ports, 22, Direction.Input, Type.Boolean, "Pumps active" );

		in_alarm_hi_pressure = getPort(ports, 24, Direction.Input, Type.Boolean, "High pressure alarm" );
		in_alarm_lo_pressure = getPort(ports, 25, Direction.Input, Type.Boolean, "Low pressure alarm" );
		in_alarm_overload = getPort(ports, 26, Direction.Input, Type.Boolean, "Overload" );

		in_salinity = getPort(ports, 32, Direction.Input, Type.Numeric, "Salinity value" );
		in_low_pressure = getPort(ports, 33, Direction.Input, Type.Numeric, "Low pressure value" );
		in_high_pressure = getPort(ports, 34, Direction.Input, Type.Numeric, "High pressure value" );
		in_water_temperature = getPort(ports, 35, Direction.Input, Type.Numeric, "Water temperature value" );
		in_current = getPort(ports, 36, Direction.Input, Type.Numeric, "Power current value" );
		in_freq = getPort(ports, 37, Direction.Input, Type.Numeric, "Power frequency value" );
		in_flush_time = getPort(ports, 38, Direction.Input, Type.Numeric, "Flushing time value, sec" );


	}

	protected void setRegister(int holding_reg_no, int value) {
		try {
			log.severe("Set out: "+value);
			protocolDriver.preset_single_register( holding_reg_no, value );
		} catch (ModBusException e) {
			log.log(Level.SEVERE,"can't send data", e);
		}
	}




	@Override
	protected void setupPanel(JPanel panel) {
		panel.add(new JLabel("IP Address:"), consL);
		panel.add(ipAddressField, consR);

		panel.add(new JLabel("TCP port:"), consL);
		panel.add(tcpPortField, consR);

		panel.add(new JLabel("Modbus address:"), consL);
		panel.add(unitBaseField, consR);

		panel.add(new JLabel("Active:"), consL);
		panel.add(unitActiveCheckbox, consR);
		unitActiveCheckbox.setEnabled(false);

	}


	@Override
	protected void doLoadPanelSettings() {
		if(ipAddress != null)
		{
			ipAddressString = ipAddress.getHostName();
		}
		if(ipAddressString != null)
			ipAddressField.setText(ipAddressString);

		tcpPortField.setText(Integer.toString(tcpPort));

		unitBaseField.setText(unitBase < 0 ? "" : Integer.toString(unitBase));

	}

	@Override
	protected void doSavePanelSettings() {
		String tmpIpAddressString = ipAddressField.getText();
		try { tcpPort = Integer.parseInt(tcpPortField.getText()); }
		catch( NumberFormatException e ) { /* ignore */ }

		activateIp(tmpIpAddressString);

		try { unitBase = Integer.parseInt( unitBaseField.getText() ); }
		catch( NumberFormatException e ) { /* ignore */ }

		unitActiveCheckbox.setSelected(false);		

	}

	private void activateIp(String addressString) {
		try {
			InetAddress ia = InetAddress.getByName(addressString);
			ipAddressString = addressString;
			ipAddress = ia;
		} catch (UnknownHostException e) {
			log.warning("Can't resolve "+addressString);
			showMessage("Can't resolve address "+addressString);
			return;
		}
	}

	@Override
	protected void doStartDriver() throws CommunicationsException {
		try {
			protocolDriver.startTcp(ipAddress,tcpPort);

		} catch (IOException e) {
			throw new CommunicationsException(e.getMessage(),e);
		}

	}

	@Override
	protected void doStopDriver() throws CommunicationsException {
		try {
			protocolDriver.stopTcp();
		} catch (IOException e) {
			throw new CommunicationsException(e.getMessage(),e);
		}
	}

	@Override
	public String getDeviceName() {
		return "Technicomar Micromar";
	}

	@Override
	public String getInstanceName() {
		return ipAddressString;
	}


	@Override
	public boolean isAutoSeachSupported() { return false; }


	private int[] readBuffer = new int[INPUT_HOLDING_REGISTERS];

	@Override
	protected void doDriverTask() throws Throwable {

		protocolDriver.setSlave(unitBase);
		try {
			protocolDriver.read_input_registers(0, INPUT_HOLDING_REGISTERS, readBuffer );
			failCounter.noteSuccess();

			
			in_salinity.sendDoubleData(readBuffer[2]);
			in_low_pressure.sendDoubleData(readBuffer[3]/100.0);
			in_high_pressure.sendDoubleData(readBuffer[6]);
			in_water_temperature.sendDoubleData(readBuffer[7]/10.0);
			in_current.sendDoubleData(readBuffer[9]/10.0);
			in_freq.sendDoubleData(readBuffer[11]/10.0);
			in_flush_time.sendDoubleData(readBuffer[13]);

			{
			int alarms = readBuffer[14];
			
			in_alarm_hi_pressure.sendBooleanData( (alarms & 0x01) != 0 );
			in_alarm_lo_pressure.sendBooleanData( (alarms & 0x02) != 0 );
			in_alarm_overload.sendBooleanData( (alarms & 0x04) != 0 );
			}
			
			{
			int status = readBuffer[1];

			in_HP_Pump.sendBooleanData( (status & 0x01) != 0 );
			in_LP_Pump.sendBooleanData( (status & 0x02) != 0 );
			in_3way_status.sendBooleanData( (status & 0x04) != 0 );
			in_3way_discharge.sendBooleanData( (status & 0x08) != 0 );
			in_auto_flush.sendBooleanData( (status & 0x10) != 0 );
			in_water_produced.sendBooleanData( (status & 0x40) != 0 );
			in_pumps_active.sendBooleanData( (status & 0x80) != 0 );
			}

		} catch(ModBusIOException e)
		{
			log.log(Level.SEVERE, "IO error", e);
			failCounter.noteFailure();
		}

	}




	//private void markUnitPassive(int unit) { unitActiveCheckbox.setSelected(false); }
	//private void markUnitActive(int unit) {  unitActiveCheckbox.setSelected(true);  }


	// -----------------------------------------------------------
	// test main 
	// -----------------------------------------------------------

	public static void main(String args[]) throws IOException, ModBusException, CommunicationsException 
	{
		TechnicomarMicromar w = new TechnicomarMicromar();

		w.setIpAddressString("192.168.1.240");
		//w.setIpAddressString("192.168.1.103");
		w.start();

		w.protocolDriver.setSlave(16);
		//w.protocolDriver.preset_single_register( 0x32, 0 );

		int[] data = new int[1];
		data[0] = 0x000;
		w.protocolDriver.preset_multiple_registers(0x32, 1, data);

		//w.readWagoConfiguration();
		for(int i = 0; i < 10; i++ )
		{
			try {
				//w.doDriverTask();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		BinaryTools.sleepMsec(20000);
		w.stop();
	}


	// -----------------------------------------------------------
	// getters/setters 
	// -----------------------------------------------------------


	public int getTcpPort() {		return tcpPort;	}
	public void setTcpPort(int tcpPort) {		this.tcpPort = tcpPort;	}

	public int getUnitBase() {		return unitBase;	}
	public void setUnitBase(int unitBase) {		this.unitBase = unitBase;	}

	public String getIpAddressString() {	return ipAddressString;	}
	public void setIpAddressString(String ipAddressString) {		activateIp(  ipAddressString );	}






}
