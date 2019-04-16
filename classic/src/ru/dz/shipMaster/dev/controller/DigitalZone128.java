package ru.dz.shipMaster.dev.controller;

import gnu.io.CommPortIdentifier;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ru.dz.shipMaster.data.com.FT232PortIO;
import ru.dz.shipMaster.data.com.GenericComPortIO;
import ru.dz.shipMaster.data.misc.CommunicationsException;

/**
 * Driver for our own hardware - Atmel 128 based controller.
 * @author dz
 */
public class DigitalZone128 extends DZ128_General {
	//@SuppressWarnings("hiding")
	protected static final Logger log = Logger.getLogger(DigitalZone128.class.getName());
	
	protected final static String CONTROLLER_NAME = "Digital Zone 128";

	private JLabel serialField  = new JLabel("--");
	private JLabel deviceTypeField  = new JLabel("--");


	@Override
	protected void doLoadPanelSettings() {
		// empty
	}

	@Override
	protected void doSavePanelSettings() {
		// empty
	}

	@Override
	protected void doStartDriver() throws CommunicationsException {
		// empty
	}

	@Override
	protected void doStopDriver() throws CommunicationsException {
		// empty
	}

	@Override
	public String getDeviceName() { return CONTROLLER_NAME; }

	@Override
	public boolean isAutoSeachSupported() {
		return false;
	}

	@Override
	protected void setupPanel(JPanel panel) {
		panel.add(new JLabel("Serial no:"), consL);
		panel.add(serialField, consR);
		
		panel.add(new JLabel("Type:"), consL);
		panel.add(deviceTypeField, consR);
	}

	
	
	
	
	
	
	
	
	
	
	// -----------------------------------------------------------
	// Old code - to move 
	// -----------------------------------------------------------

	


	private static final long PACKET_WAIT_TIME = 6000; // 6 sec. (Controller sends pong once in 5 sec)
	
	
	private String portFileName = null;
	protected GenericComPortIO cpio = null;
	private DigitalZone128ComPortThread cpt = null;

	//private boolean portSearchMode = false;

	//private ComPortDispatcher comPortDispatcher = null;


	/*public ServantProtocol(ComPortThread cpt)
	{
		this.cpt = cpt;		
	}*/

	// public ServantProtocol(int controllerId, DashBoard db)
	// look for controller which will respond with controllerId in pong packet 

	/*@Deprecated
	public DigitalZone128(String portFileName) throws CommunicationsException
	{
		openPort(portFileName);
		
		dataReader.start();
	}*/

	private void openPort(String portName) throws CommunicationsException {
		
		cpt = null;
		cpio = null;
		
		this.portFileName = portName;
		//dataReader.setName("Servant watcher, "+portFileName);
		
		//cpio = new ComPortGnuIO(portName,9600);
		//this.cpio = new ComPortFileIO(portFileName);
		try {
			cpio = new FT232PortIO(0);
		} catch (IOException e) {
			log.log(Level.SEVERE,"io",e);
			throw new CommunicationsException("FT232 error",e);
		}
		
		cpt = new DigitalZone128ComPortThread(cpio, this);

		// First one to push all the possible junk off :)
		sendPing();
		// Next one to request real pong so that we know he's alive 
		sendPing();
		// Ask controller to send us all the values he knows
		// TODO reenable
		//sendValueRequests();
	}
	
	/**
	 * Default constructor.
	 */
	public DigitalZone128() {
		super(10000, Thread.NORM_PRIORITY, CONTROLLER_NAME);
		//this.comPortDispatcher = ConfigurationFactory.getTransientState().getComPortDispatcher();


		//portSearchMode  = true;
	}

	private void reopen() {
		try {
			close();
			openPort("FT");
		} catch (CommunicationsException e) {
			log.log(Level.SEVERE,"exception",e);
		}
	}
	
	
	//public String getName() { return "Servant IO"; }

	protected boolean tryPort(CommPortIdentifier portId) {
		dashMessage.setText(portId.getName()+" - поиск контроллера");
		
		try { openPort(portId.getName()); } 
		catch (Throwable e) {
			return false;
		}
		
		lastInputPacketTimeMillis = -1;

		long waitForPacketStart = System.currentTimeMillis();
		while(true)
		{
			// used as memory boundary to make sure 
			// lastInputPacketTimeMillis is updated
			synchronized (this) {

				if( lastInputPacketTimeMillis > 0 )
				{
					return true;
				}
				
			}
			
			if( System.currentTimeMillis()-waitForPacketStart > PACKET_WAIT_TIME)
				break;
		}
		close();
		
		return false;
	}

	private void close() {
		if(cpt != null) cpt.stopSelf();
		if( cpio  != null) cpio.close();
		cpt = null;
		cpio = null;
	}
	
	
	
	

	
	
	// -------------------------------------------------------------------------------
	// Watcher thread
	// -------------------------------------------------------------------------------

	
	@Override
	protected void doDriverTask() throws Throwable {
		int tryCount = 0;

		if(cpio == null)
			reopen();

		/*for(int j = 0; j <7; j++){
				DigitalZone128Packet req = new DigitalZone128Packet( DigitalZone128PacketType.FROMHOST_DIGITAL_OUT, j, 0 );
				try {
					cpio.write( req.getPacket() );
					cpio.write( req.getPacket() );
					cpio.write( req.getPacket() );
				} catch (CommunicationsException e) {
					// TO DO Auto-generated catch block
					e.printStackTrace();
				}
			}/**/


		// TODO here we must process verifications of write operations, if needed
		// we have to check for verifications requests and send request packets to 
		// read data from controller outputs. On reception of reply packets packet
		// receive code will clear requests.

		long timeDiff = System.currentTimeMillis()-lastInputPacketTimeMillis;
		// No IO for 10 seconds? Reopen.
		if((timeDiff/1000) > 10)
		{
			try {
				tryCount++;
				log.severe("Нет связи с контроллером, попытка "+tryCount);
				dashMessage.renew();
				dashMessage.setText(portFileName+" - контроллер молчит");
				reopen();
 
			}
			catch(Throwable e) {/* ignore */}
		}
		else
		{
			if(tryCount > 0)
				log.severe("Связь с контроллером восстановлена");
			tryCount = 0;
		}
		
	}
	
				

	
	protected void sendPacket(DigitalZone128Packet p) throws CommunicationsException
	{
		if(cpio == null)
		{
			throw new CommunicationsException("COM port is not available");
		}

		cpio.write( p.getPacket() );
	}

	
	protected String getControllerName()
	{
		return CONTROLLER_NAME;
	}
	
	protected void setDeviceTypeField(String string) {
		deviceTypeField.setText(string);		
	}

	
	protected void setDeviceSerialField(String string) {
		serialField.setText(string);		
	}
	
	protected String getPortName() {		return portFileName;	}

	@Override
	public String getInstanceName() {		return portFileName;	}
	
	
}
