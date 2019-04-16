package ru.dz.shipMaster.dev.controller.moxa;

import java.util.Vector;
import java.util.logging.Level;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.dev.BusBasedDriver;
import ru.dz.shipMaster.dev.DriverPort;
import ru.dz.shipMaster.dev.IBus;
import ru.dz.shipMaster.dev.PortDataOutput;
import ru.dz.shipMaster.dev.bus.ModBus;
import ru.dz.shipMaster.dev.controller.ModBusException;
import ru.dz.shipMaster.ui.misc.VisualHelpers;

public class E2210 extends BusBasedDriver<ModBus> {

	private int slave = 0;
	private JTextField slaveField = new JTextField(6);

	protected E2210() {		
		super(1000, Thread.NORM_PRIORITY, "MoxaE2210");
	}

	@Override
	public String getDeviceName() { return "Moxa E2210"; }

	@Override
	public boolean isAutoSeachSupported() { return false; }

	@Override
	protected boolean correctBusType(IBus bus) { return (bus instanceof ModBus); }






	@Override
	protected void setupPanel(JPanel panel) {
		super.setupPanel(panel);

		panel.add(new JLabel("Slave"),consL);
		panel.add( slaveField  ,consR);
	}


	@Override
	public void doLoadPanelSettings() {
		super.doLoadPanelSettings();
		slaveField.setText(""+slave);
	}


	@Override
	public void doSavePanelSettings() {
		super.doSavePanelSettings();
		try { 
			slave = Integer.parseInt(slaveField.getText()); 
		} 
		catch( NumberFormatException e) 
		{
			VisualHelpers.showMessageDialog(null, "Invalid slave number");
		}
	}



	
	

	
	
	
	
	
	
	
	
	private int nAnOutputPorts = 0;
	private DriverPort[] anOutputPorts = new DriverPort[ nAnOutputPorts ];  

	private int nAnInputPorts = 0;
	private DriverPort[] anInputPorts = new DriverPort[ nAnInputPorts ];

	private int nDigOutputPorts = 128;
	private DriverPort[] digOutputPorts = new DriverPort[ nDigOutputPorts ];  

	private int nDigInputPorts = 12;
	private DriverPort[] digInputPorts = new DriverPort[ nDigInputPorts ];

	private int nPFCInputPorts = 24;
	private DriverPort[] pfcInputPorts = new DriverPort[ nPFCInputPorts ];
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	private int [] anIn = new int[nPFCInputPorts];
	private boolean [] boolIn = new boolean[nDigInputPorts ]; 
	
	@Override
	protected void doDriverTask() throws Throwable {

		//bus.readInputRegister(regAddr)
		
		
		synchronized(boolIn) {
			int num = nDigInputPorts;// Math.min( digitalInputCount, nDigInputPorts );
			bus.readInputStatus( slave, 0, boolIn.length, boolIn );
			//System.out.println("Wago750.doDriverTask() dig = 0 "+boolIn[0]);
			//System.out.print("bool ");
			for( int i = 0; i < num; i++ )
			{
				DriverPort port = digInputPorts[i];
				if(port != null)
				{
					port.sendBooleanData(boolIn[i]);
					//System.out.print(" "+i+": "+boolIn[i]);
				}
			}
			//System.out.println();
		}

		
		/*synchronized(anIn)
		{
			int aCount = 2 * Math.min(analogueInputCount, nAnInputPorts );
			protocolDriver.read_input_registers(0, aCount, anIn);
			for( int i = 0; i < nAnInputPorts; i++ )
			{
				DriverPort port = anInputPorts[i];
				if(port != null)
					port.sendDoubleData(anIn[i]);			
			}
		}/* */


		synchronized(anIn)
		{
			//int aCount = 2 * Math.min(analogueInputCount, nAnInputPorts );
			bus.readInputRegisters(slave, 0x178, nPFCInputPorts, anIn);
			for( int i = 0; i < nPFCInputPorts; i++ )
			{
				DriverPort port = pfcInputPorts[i];
				if(port != null && port.isUsed())
					port.sendDoubleData(anIn[i]);			
			}
		}
		
	}

	@Override
	protected void doStartDriver() throws CommunicationsException {
		
	}

	@Override
	protected void doStopDriver() throws CommunicationsException {
	}

	@Override
	protected void updatePorts(Vector<DriverPort> ports) {
		DriverPort.absentize(ports);
		DriverPort.sortPorts(ports);

		/* None yet
		{
		DriverPort port = DriverPort.getPort(ports,0);
		port.setDirection(Direction.Output);
		port.setHardwareName("TextToShow");
		port.setType(Type.String);
		}*/

		int i;

		boolean prevIsUsed = true;
		for( i = 0; i < nAnInputPorts; i++ )
		{
			String portName = "An. I "+i;
			anInputPorts[i] = getPort(ports, i, Direction.Input, Type.Numeric, portName );
			//if( i >= analogueInputCount )				anInputPorts[i].setAbsent(true);
		}

		for( i = 0; i < nAnOutputPorts; i++ )
		{
			//final int portNo = i;
			String portName = "An. O "+i;
			anOutputPorts[i] = getPort(ports, i+nAnInputPorts, Direction.Output, Type.Numeric, portName );
			//if( i >= analogueOutputCount )				anOutputPorts[i].setAbsent(true);

			final int controllerPortNo = i;

			anOutputPorts[i].setPortDataOutput(
					new PortDataOutput() {
						@Override
						public void receiveDoubleData(double value) {
							try {
								if( !anOutputPorts[controllerPortNo].checkIsChanged(value) )
									return;

								log.fine("Set analogue out: "+value);
								bus.presetSingleRegister( slave, /*OUTPUT_REGISTERS_START_NUMBER+*/controllerPortNo, 256*(int)value );

								//protocolDriver.force_single_coil( /*OUTPUT_REGISTERS_START_NUMBER+*/controllerPortNo, value > 50);

							} catch (ModBusException e) {
								log.log(Level.SEVERE,"can't send data", e);
							}
						}});

		}


		for( i = 0; i < nDigInputPorts; i++ )
		{
			String portName = "Dig. I "+i;
			final int shift = +nAnInputPorts+nAnOutputPorts;
			digInputPorts[i] = getPort(ports, i+shift, Direction.Input, Type.Boolean, portName );
			//if( i >= digitalInputCount )				digInputPorts[i].setAbsent(true);

		}

		for( i = 0; i < nDigOutputPorts; i++ )
		{
			String portName = "Dig. O "+i;
			final int shift = +nAnInputPorts+nAnOutputPorts+nDigInputPorts;
			digOutputPorts[i] = getPort(ports, i+shift, Direction.Output, Type.Boolean, portName );
			//if( i >= digitalOutputCount )				digOutputPorts[i].setAbsent(true);

			final int controllerPortNo = i;

			digOutputPorts[i].setPortDataOutput(
					new PortDataOutput() {
						@Override
						public void receiveDoubleData(double value) {
							try {
								if( !anOutputPorts[controllerPortNo].checkIsChanged(value) )
									return;

								log.fine("Set digital out: "+value);
								bus.forceSingleCoil( slave, controllerPortNo, value > 0.1 );

							} catch (ModBusException e) {
								log.log(Level.SEVERE,"can't send data", e);
							}
						}});

		}

		prevIsUsed = true;
		for( i = 0; i < nPFCInputPorts; i++ )
		{
			String portName = "IR I "+i;
			final int shift = +nAnInputPorts+nAnOutputPorts+nDigInputPorts+nDigOutputPorts;
			pfcInputPorts[i] = getPort(ports, i+shift, Direction.Input, Type.Numeric, portName );
			pfcInputPorts[i].setAbsent( (!prevIsUsed) && (!pfcInputPorts[i].isUsed()) );
			prevIsUsed = pfcInputPorts[i].isUsed(); 
		}


	}



}
