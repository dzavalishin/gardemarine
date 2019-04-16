package ru.dz.shipMaster.dev.controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.dev.DrivertStartObjection;
import ru.dz.shipMaster.misc.CrcExceprion;

public class MMNet101 extends DZ128_General {
	private static final int SERVANT_UDP_PORT = 16113;
	private static final int SERVANT_UDP_LONG_PORT = 16114;

	protected static final Logger log = Logger.getLogger(DigitalZone128.class.getName());
	
	protected final static String CONTROLLER_NAME = "Digital Zone MMNet101";

	private JLabel serialField  = new JLabel("--");
	private JLabel deviceTypeField  = new JLabel("--");
	private JTextField servantAddressField  = new JTextField(12);

	private DrivertStartObjection badIp = new DrivertStartObjection("Bad Servant address");

	@Override
	protected void doLoadPanelSettings() {
		//servantAddressField.setText(servantInetAddress.getHostAddress());
		servantAddressField.setText(servantAddress);
	}

	@Override
	protected void doSavePanelSettings() {
		servantAddress = servantAddressField.getText();
		try {
			servantInetAddress = InetAddress.getByName(servantAddress);
			badIp.setStartForbidden(false);
		} catch (UnknownHostException e) {
			badIp.setStartForbidden(true);
			log.log(Level.SEVERE,"Bad address",e);
		}
	}

	@Override
	protected void doStartDriver() throws CommunicationsException {
		
		synchronized (stopRequestSema) {
			stopRequest = false;
		}
		
		try {
			//startUdp("255.255.255.255");
			startUdp();
		} catch (IOException e) {
			throw new CommunicationsException(e.getMessage(), e);
		}
	}

	@Override
	protected void doStopDriver() throws CommunicationsException {
		synchronized (stopRequestSema) {
			stopRequest = true;
		}

		receptionThreadForShortPackets.stop(new InterruptedException("Driver stop"));
		socketForShortPackets.close();

		receptionThreadForLongPackets.stop(new InterruptedException("Driver stop"));
		socketForLongPackets.close();
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
		
		panel.add(new JLabel("IP address:"), consL);
		panel.add(servantAddressField, consR);
	}

	
	
	
	
	// -----------------------------------------------------------
	// Old code - to move 
	// -----------------------------------------------------------

	
	//private static final int MAX_OUT_DIGITAL_PORTS = 10;


	//private static final long PACKET_WAIT_TIME = 6000; // 6 sec. (Controller sends pong once in 5 sec)
	
	//private volatile long lastInputPacketTimeMillis = -1;

	private String servantAddress;
	
	private Thread receptionThreadForShortPackets, receptionThreadForLongPackets;
	private MulticastSocket socketForShortPackets;
	private MulticastSocket socketForLongPackets;
	private InetAddress servantInetAddress;

	protected final Object stopRequestSema = new Object();
	protected volatile boolean stopRequest = false;


	private void startUdp() throws CommunicationsException, IOException {
		
		servantInetAddress = InetAddress.getByName(servantAddress);
		
		socketForShortPackets = new MulticastSocket(SERVANT_UDP_PORT);
		socketForShortPackets.joinGroup(InetAddress.getByName("238.16.0.13"));
		//socketForShortPackets.connect(servantInetAddress, SERVANT_UDP_PORT);

		socketForLongPackets = new MulticastSocket(SERVANT_UDP_LONG_PORT);
		socketForLongPackets.joinGroup(InetAddress.getByName("238.16.0.13"));
		//socketForLongPackets.connect(servantInetAddress, SERVANT_UDP_PORT);
		
		receptionThreadForShortPackets = new Thread() {
			@Override
			public void run() {
				while(true)
				{
					try { receiveShortData(); }
					catch(Throwable e)
					{
						log.log(Level.SEVERE,"Net recv error",e);
					}
					synchronized (stopRequestSema) {
						if(stopRequest )
							break;						
					}
				}
			}};
			
		receptionThreadForShortPackets.setName("UDP short listener");
		receptionThreadForShortPackets.setDaemon(true);
		receptionThreadForShortPackets.start();
		

		receptionThreadForLongPackets = new Thread() {
			@Override
			public void run() {
				while(true)
				{
					try { receiveLongData(); }
					catch(Throwable e)
					{
						log.log(Level.SEVERE,"Net recv error",e);
					}
					synchronized (stopRequestSema) {
						if(stopRequest )
							break;						
					}
				}
			}};
			
		receptionThreadForLongPackets.setName("UDP long listener");
		receptionThreadForLongPackets.setDaemon(true);
		receptionThreadForLongPackets.start();
		
		// First one to push all the possible junk off :)
		sendPing();
		// Next one to request real pong so that we know he's alive 
		sendPing();
		// Ask controller to send us all the values he knows
		// TODO reenable
		//sendValueRequests();
	}
	
	
	protected void receiveShortData() throws IOException, CrcExceprion {
		byte[] buf = new byte[1024];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		socketForShortPackets.receive(packet);
		
		if( !packet.getAddress().equals(servantInetAddress) )
		{
			//log.log(Level.FINEST,"Rejected UDP from"+packet.getAddress());
			return;
		}
		log.log(Level.FINEST,"Got short UDP");
		
		DigitalZone128Packet digitalZone128Packet = new DigitalZone128Packet(buf);
		
		receivePacket(digitalZone128Packet);
	}

	
	protected void receiveLongData() throws IOException, CrcExceprion {
		byte[] buf = new byte[1024];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		socketForLongPackets.receive(packet);
		
		if( !packet.getAddress().equals(servantInetAddress) )
		{
			//log.log(Level.FINEST,"Rejected UDP from"+packet.getAddress());
			return;
		}
		log.log(Level.FINEST,"Got long UDP");
		
		//GenericComPortIO.dump("Long UDP",buf);
		
		DigitalZone128Packet digitalZone128Packet = DigitalZone128Packet.decodeLongUDP(buf, packet.getLength());
		
		receivePacket(digitalZone128Packet);
	}
	
	/**
	 * Default constructor.
	 */
	public MMNet101() {
		super(500, Thread.NORM_PRIORITY, CONTROLLER_NAME);
		//this.comPortDispatcher = ConfigurationFactory.getTransientState().getComPortDispatcher();

		addStartObjection(badIp);

	}

	
	
	//public String getName() { return "Servant IO"; }

	
	
	

	
	
	// -------------------------------------------------------------------------------
	// Watcher thread
	// -------------------------------------------------------------------------------

	private int tryCount = 0;
	private int callCount = 0;
	@Override
	protected void doDriverTask() throws Throwable {
		
		// once in 0.5 sec request all analogue data to be resent
		for(int port = 7; port >= 0; port--)
		{
			sendPacket( new DigitalZone128Packet( DigitalZone128PacketType.FROMHOST_ANALOG_REQUEST, port, 0 ) );
			sleep(20);
		}

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

		
		// Do check once in 20 calls
		if(callCount++ < 20 )
			return;
		callCount = 0;

		// TODO here we must process verifications of write operations, if needed
		// we have to check for verifications requests and send request packets to 
		// read data from controller outputs. On reception of reply packets packet
		// receive code will clear requests.

		final long now = System.currentTimeMillis();
		final long timeDiff = now-lastInputPacketTimeMillis;
		// No IO for 10 seconds? Problem.
		if((timeDiff/1000) > 10)
		{
			try {
				tryCount++;
				// TO DO on 20th error search port again?
				log.severe(getPortName() + " Нет связи с контроллером, попытка "+tryCount);
				dashMessage.renew();
				dashMessage.setText(getPortName() + " - контроллер молчит");
				sendPing();
				// TODO put it back - turned off for debug
				//reopen();
 
			}
			catch(Throwable e) {/* ignore */}
		}
		else
		{
			if(tryCount > 0)
			{
				log.severe(getPortName() + " Связь с контроллером восстановлена");
				dashMessage.renew();
				dashMessage.setText(getPortName() + " Связь с контроллером восстановлена");
			}
			tryCount = 0;
		}
		
	}

	@Override
	protected String getControllerName() {
		return CONTROLLER_NAME;
	}

	@Override
	protected String getPortName() { return servantAddress; }

	@Override
	public String getInstanceName() {		return servantAddress;	}

	
	@Override
	protected void sendPacket(DigitalZone128Packet p)
			throws CommunicationsException {
		byte[] buf = p.getPacket();

		//dump("Send", buf, 9);
		
		DatagramPacket udpPacket = new DatagramPacket(buf, buf.length, servantInetAddress, SERVANT_UDP_PORT );
		try {
			socketForShortPackets.send(udpPacket);
			log.log(Level.FINEST,"Sent UDP");
		} catch (IOException e) {
			log.log(Level.SEVERE,"UDP send error", e);
		}

	}

	public static void dump(String prefix, byte[] pkt, int maxLen) {
		System.out.print(prefix+": 0x.. ");
		for(int i = 0; i < pkt.length; i++ )
		{
			if( i >= maxLen ) break;

			System.out.print(String.format("%02X", 0xFF&(int)pkt[i]));
			System.out.print(" ");
		}
		System.out.println();
		
	}
	
	protected void setDeviceTypeField(String string) {
		deviceTypeField.setText(string);		
	}

	
	protected void setDeviceSerialField(String string) {
		serialField.setText(string);		
	}

	
	// -------------------------------------------------------------------------------
	// Getters/setters
	// -------------------------------------------------------------------------------

	
	public String getServantAddress() {
		return servantAddress;
	}

	public void setServantAddress(String servantAddress) {
		this.servantAddress = servantAddress;
	}

				
	


	
	
	
	
	
	
	
	
	
	

}
