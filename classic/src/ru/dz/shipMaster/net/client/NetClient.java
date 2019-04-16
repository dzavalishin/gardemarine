package ru.dz.shipMaster.net.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.dz.shipMaster.config.items.CliNetInput;
import ru.dz.shipMaster.config.items.CliParameter;
import ru.dz.shipMaster.data.DataSource;
import ru.dz.shipMaster.net.protocol.NetException;
import ru.dz.shipMaster.net.protocol.NetIO;
import ru.dz.shipMaster.net.protocol.NetNode;
import ru.dz.shipMaster.net.protocol.NetPacket;
import ru.dz.shipMaster.net.protocol.NodeHash;
import ru.dz.shipMaster.net.protocol.NodeList;
import ru.dz.shipMaster.net.protocol.PacketKind;
import ru.dz.shipMaster.net.protocol.PacketType;

public class NetClient {
    private static final Logger log = Logger.getLogger(NetClient.class.getName()); 

	
	protected NetIO io;
	protected NodeList nodeList = new NodeList();
	protected ReceiveList receiveList = new ReceiveList();

	public NetClient() throws SocketException, IOException
	{
	io = new NetIO() {
		@Override
		protected void receivePacket(NetPacket p) throws IOException, NetException { processPacket(p); }
		};
	}
	
	protected void processPacket(NetPacket p) throws IOException, NetException {
		//System.out.print("Got a packet kind="+p.getKind().getKind());
		//System.out.print(" type="+p.getType().getType());
		
		/*if(!PacketType.NODE.equals( p.getType() ))
			System.out.println(p.toString());*/
		
		if(PacketKind.REQUEST.equals( p.getKind() ))
		{
			//log.log(Level.FINEST,"Request packet ignored");
			log.log(Level.SEVERE,"Request packet ignored");
			return;
		}

		if(!PacketKind.BROADCAST.equals( p.getKind() ))
		{
			log.log(Level.WARNING,"Unknown packet ignored, kind = "+p.getKind());
			return;
		}

		switch(p.getType())
		{
		case NODE:
		{
			// Update node data in node list
			String nodeName = p.getStringItem("NAME");
			InetAddress srcAddress = p.getSrcAddress();
			NodeHash srcNodeHash = p.getSrcNodeHash();
			nodeList.updateNodeInfo(srcNodeHash,srcAddress,nodeName);
		}
			break;
			
			/*
			 * Information about translated parameter received.
			 */
		case INFO:
		{
			double minValue = p.getNumericItem("MINV");
			double maxValue = p.getNumericItem("MAXV");
			String label = p.getStringItem("LABL");			
			String unit = p.getStringItem("UNIT");
			String senderDataSourceName = p.getStringItem("NAME");		
			
			NetNode node = nodeList.getNode(p.getSrcNodeHash());
			
			if(node != null)
			{
				ReceiveListItem item = receiveList.getItem(node.getName(), senderDataSourceName);
				
				boolean needUpdate = !item.isReceivedItemInfo();
				
				item.setInfo(minValue, maxValue, label, unit);				
				
				if(needUpdate)
					newItemEvent(node.getName(),senderDataSourceName);
			}
		}
			break;
		case DATA:
		{
			double value = p.getNumericItem("DATA");
			String senderDataSourceName = p.getStringItem("NAME");			
			NetNode node = nodeList.getNode(p.getSrcNodeHash());
			if(node != null)
			{
				/*
				try { receiveList.setValue(value, node.getName(), senderDataSourceName); }
				catch(NetNoItemException e)
				{
					// Request item description
					NetPacket req = new NetPacket(PacketKind.REQUEST, PacketType.INFO);
					req.setItem("NAME", senderDataSourceName);
					// TO DO send it directly to node!
					req.setItem("NODE", node.getName());
					io.sendPacket(req);					
				}*/
				
				ReceiveListItem item = receiveList.getItem( node.getName(), senderDataSourceName );
				item.setValue(value);
				if(!item.isReceivedItemInfo())
					requestItemDescription(node.getName(), senderDataSourceName);					
			}
		}
			break;
		default:
			log.log(Level.WARNING,"Unknown packet ignored, type = "+p.getType());
			break;
		}	
	}

	/**
	 * Ask hostName for data for itemName.
	 * @param hostName Host to ask from. NB! We broadcast anyway!
	 * @param itemName Item to ask data for.
	 * @throws NetException
	 */
	public void requestDataUpdate(String hostName, String itemName )
			throws NetException {
		NetPacket req = new NetPacket(PacketKind.REQUEST, PacketType.DATA);
		req.setItem("NAME", itemName);
		// TO DO send it directly to node!
		req.setItem("NODE", hostName);
		io.sendPacket(req);
	}

	/**
	 * Request hostName to send description of itemName.
	 * @param hostName Host to ask from. NB! We broadcast anyway!
	 * @param itemName Item to ask info about.
	 * @throws NetException
	 */
	public void requestItemDescription(String hostName, String itemName)
			throws NetException {
		NetPacket req = new NetPacket(PacketKind.REQUEST, PacketType.INFO);
		req.setItem("NAME", itemName);
		// TO DO send it directly to node!
		req.setItem("NODE", hostName);
		io.sendPacket(req);
	}	
	
	public DataSource getDataSource(String name)
	{
		String[] strings = name.split(":");
		return receiveList.getDataSource(strings[0], strings[1]);
	}
	public DataSource getDataSource(String hostName, String itemName)
	{
		return receiveList.getDataSource( hostName, itemName);
	}

	
	public void connectReceiveItem(CliNetInput in) {			
		DataSource dataSource = getDataSource(in.getHostName(), in.getItemName());
		if(dataSource == null)
		{
			log.severe("Unable to setup network reception for "+in);
			return;
		}
		
		final CliParameter target = in.getTarget();
		
		if(target.isTranslateToNet()) // Make sure no loop exists
			log.severe("Item "+in.getItemName()+" from host "+in.getHostName()+" is translated back to network.");
		target.setDataSource(dataSource);

		/*
		if(false)
		{
		try { 
			requestItemDescription(in.getHostName(), in.getItemName());
			requestDataUpdate(in.getHostName(), in.getItemName());
			} 
		catch (NetException e) { 
		// Ignore 
		}
		*/
	}	
	
	/**
	 * Override this to be informed about new datasources arrived. 
	 * @param senderDataSourceName
	 * @param senderDataSourceName2 
	 */
	protected void newItemEvent(String senderHostName, String senderDataSourceName) { }

}
