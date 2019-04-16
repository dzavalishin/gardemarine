package ru.dz.shipMaster.net.server;

import java.io.IOException;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.dz.shipMaster.data.DataSource;
import ru.dz.shipMaster.net.client.NetClient;
import ru.dz.shipMaster.net.protocol.NetException;
import ru.dz.shipMaster.net.protocol.NetPacket;
import ru.dz.shipMaster.net.protocol.PacketKind;
import ru.dz.shipMaster.net.protocol.PacketType;

/**
 * Gardemarine network server.
 * @author dz
 */
public class NetServer extends NetClient {
    private static final Logger log = Logger.getLogger(NetServer.class.getName()); 

    private static final int SEND_INTERVAL_MSEC = 1000;
    
	private String nodeName;
	private Thread sendThread;
	protected SendList sendList = new SendList();

	private boolean running = false;


	public void addSendListItem(String name, DataSource ds) {
		sendList.add(name, ds);
	}

	public void clearSendList() {
		sendList.clear();
	}

	public NetServer(String nodeName) throws SocketException, IOException
	{
		this.nodeName = nodeName;
		
		sendThread = new Thread() {
			@Override
			public void run() {
				while(true)
				{
					try 
					{ 
						synchronized(this) { wait(SEND_INTERVAL_MSEC); }
						sendData(); 
					}
					catch(Throwable e)
					{
						log.log(Level.SEVERE,"Net send error",e);
					}
				}
			}};
			
		sendThread.setName("UDP sender");
		sendThread.setDaemon(true);
		sendThread.start();
	}
	
	private int hackDataInfoRequestSkipCounter = 0;
	protected void sendData() throws NetException {
		if(!running)
			return;
		
		NetPacket p = new NetPacket(PacketKind.BROADCAST, PacketType.NODE);
		p.setItem("Name", nodeName);
		io.sendPacket(p);
		
		sendList.sendChanged(io);
		
		if(hackDataInfoRequestSkipCounter++ > 10)
		{
			hackDataInfoRequestSkipCounter = 0;
			receiveList.requestEmpty(this);
		}
	}

	public String getNodeName() { return nodeName; }
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	/**
	 * Start or stop network server. Client is working anyway.
	 * @param running
	 */
	public void setRunning(boolean running)
	{
		this.running = running;		
	}
	
	/**
	 * @return True if network server was started and is running.
	 */
	public boolean isRunning() { return running; }

	@Override
	protected void processPacket(NetPacket p) throws IOException, NetException {
		//System.out.print("Got a packet kind="+p.getKind().getKind());
		//System.out.print(" type="+p.getType().getType());
		
		// Broadcasts are processed in receiver
		if(PacketKind.BROADCAST.equals( p.getKind() ))
		{
			super.processPacket(p);
			return;
		}

		//System.out.println(p.toString());

		if(!p.getKind().equals( PacketKind.REQUEST ))
		{
			log.log(Level.WARNING,"Unknown packet ignored, kind = "+p.getKind());
			return;
		}

		switch(p.getType())
		{
		case NODE:
		/*{
			// Update node data in node list
			String nodeName = p.getStringItem("NAME");
			InetAddress srcAddress = p.getSrcAddress();
			NodeHash srcNodeHash = p.getSrcNodeHash();
			nodeList.updateNodeInfo(srcNodeHash,srcAddress,nodeName);
		}*/
			break;
		case INFO:
		{
			/*
			double minValue = p.getNumericItem("MINV");
			double maxValue = p.getNumericItem("MAXV");
			String label = p.getStringItem("LABL");			
			String unit = p.getStringItem("UNIT");
			String senderDataSourceName = p.getStringItem("NAME");			
			NetNode node = nodeList.getNode(p.getSrcNodeHash());
			if(node != null)
				receiveList.setInfo(node.getName(), senderDataSourceName,
						minValue, maxValue, label, unit
						);
						*/
			// Got request to send item info
			String senderDataSourceName = p.getStringItem("NAME");
			String senderNode = p.getStringItem("NODE");
			log.log(Level.SEVERE,"Req for item info = "+senderDataSourceName);
			if((senderNode == null) || (senderNode.equalsIgnoreCase(nodeName)))
				sendList.sendDescription(io, senderDataSourceName );

		}
			break;
			
		case DATA:
		/*{
			double value = p.getNumericItem("DATA");
			String senderDataSourceName = p.getStringItem("NAME");			
			NetNode node = nodeList.getNode(p.getSrcNodeHash());
			if(node != null)
				receiveList.setValue(value, node.getName(), senderDataSourceName);
		}*/
		{
			String reqDataSourceName = p.getStringItem("NAME");
			String senderNode = p.getStringItem("NODE");
			log.log(Level.SEVERE,"Req for item data = "+reqDataSourceName);
			if((senderNode == null) || (senderNode.equalsIgnoreCase(nodeName)))
			{
				log.log(Level.SEVERE,"Send back item data = "+reqDataSourceName);
				sendList.sendData(io, reqDataSourceName );
			}
		}
			break;
			
		default:
			log.log(Level.WARNING,"Unknown packet ignored, type = "+p.getType());
			break;
		}
		
	}


	
}
