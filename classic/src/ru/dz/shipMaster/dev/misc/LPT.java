package ru.dz.shipMaster.dev.misc;

import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.dev.DriverPort;
import ru.dz.shipMaster.dev.PortDataOutput;
import ru.dz.shipMaster.dev.ThreadedDriver;
import ru.dz.shipMaster.dev.misc.direct.ParallelPort;

public class LPT extends ThreadedDriver {

	public LPT() {
		super(200, Thread.NORM_PRIORITY, "LPT driver");
	}


	@Override
	protected void doLoadPanelSettings() {
		lptPortField.setText(Integer.toHexString(pp.getPortBase()));

	}

	@Override
	protected void doSavePanelSettings() {
		try { 
			int port = Integer.parseInt(lptPortField.getText(),16); 
			pp.setPortBase(port);
		}
		catch( NumberFormatException e ) { /* ignore */ }

	}

	@Override
	protected void doStartDriver() throws CommunicationsException {
		haveOldData = false;
	}

	@Override
	protected void doStopDriver() throws CommunicationsException {
		// nothing
	}

	@Override
	public String getDeviceName() {
		//return "LPT port @0x"+ Integer.toHexString( pp.getPortBase() );
		return "LPT port";
	}

	@Override
	public String getInstanceName() {
		return Integer.toHexString(pp.getPortBase());
	}


	@Override
	public boolean isAutoSeachSupported() { return false; }

	private JTextField lptPortField = new JTextField(6);

	@Override
	protected void setupPanel(JPanel panel) {
		panel.add(new JLabel("LPT port addr:"), consL);
		panel.add(lptPortField, consR);

	}


	private int nOutputPorts = 8;
	private DriverPort[] outputPorts = new DriverPort[ nOutputPorts ];
	private double [] lastOutValue = new double [nOutputPorts]; 
	private int outByte = 0;

	private int nInputPorts = 8;
	private DriverPort[] inputPorts = new DriverPort[ nInputPorts ];

	@Override
	protected void updatePorts(Vector<DriverPort> ports) {
		DriverPort.absentize(ports);
		DriverPort.sortPorts(ports);

		int i;

		for( i = 0; i < nInputPorts; i++ )
		{
			String portName = "In bit "+i;
			inputPorts[i] = getPort(ports, i, Direction.Input, Type.Numeric, portName );
		}

		for( i = 0; i < nOutputPorts; i++ )
		{
			String portName = "Out bit "+i;
			outputPorts[i] = getPort(ports, i+nInputPorts, Direction.Output, Type.Numeric, portName );

			final int bitNo = i;

			outputPorts[i].setPortDataOutput
			(
					new PortDataOutput() {
						@Override
						public void receiveDoubleData(double value) {
							if(value != lastOutValue[bitNo])
							{
								lastOutValue[bitNo] = value;
								log.info("LPT Set out: "+value);

								int bitMask = 1 << bitNo;

								outByte &= ~bitMask;
								if(value > 0)
									outByte |= bitMask;
								setOutput( outByte );
							}
						}});

		}



	}


	private ParallelPort pp = new ParallelPort();
	protected void setOutput(int b) {
		pp.write(b);
	}

	private int oldData = 0;
	private boolean haveOldData = false;
	@Override
	protected void doDriverTask() throws Throwable {
		int data = pp.readData();

		int shift = data;
		int oshift = oldData;

		for(int i = 0; i < 8; i++)
		{
			int bit = shift & 1;
			int obit = oshift & 1;

			shift >>= 1;
		oshift >>= 1;

		if( !haveOldData || (bit != obit))
			recvData( i, bit );
		}

		oldData = data;
		haveOldData = true;
	}


	private void recvData(int i, int bit) {
		inputPorts[i].sendBooleanData(bit != 0 );
	}


	public int getPortBase() { 		return pp.getPortBase();   	}
	public void setPortBase(int portBase) {		pp.setPortBase(portBase);	}


	
}
