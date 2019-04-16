package ru.dz.shipMaster.net.protocol;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * List of active (detected from network) Gardemarine nodes.
 * @author dz
 */
public class NodeList {
	private static final Logger log = Logger.getLogger(NodeList.class.getName()); 

	protected final Map <NodeHash,NetNode> nodes = new HashMap<NodeHash,NetNode>();

	protected void cleanupByTime()
	{
		synchronized(nodes)
		{
			restart: while(true)
			{
				for(NetNode n : nodes.values())
				{
					if(!n.HadPacketWithinLast(60))
					{
						nodes.remove(n.getHash());
						continue restart;
					}
				}
				break;
			}
		}
	}

	Timer cleanupTimer = new Timer();
	{
		TimerTask tt = new TimerTask() {
			@Override
			public void run() { cleanupByTime(); }
			};
			
		cleanupTimer.schedule(tt, 10000, 10000);
	}
	/*
	NetNode addNode(NodeHash hash, InetAddress address, String name)
	{
		NetNode node = new NetNode(hash, address, name);
		nodes.put(hash, node);
		return node;
	}*/

	public void removeNode(NodeHash hash)
	{
		synchronized(nodes)
		{
			nodes.remove(hash);
		}
	}

	/*
	void removeNode(NetNode node)
	{

	}*/

	public NetNode getNode(NodeHash hash)
	{
		synchronized(nodes)
		{
			return nodes.get(hash);
		}
	}

	public boolean haveNode(NetNode node)
	{
		synchronized(nodes)
		{
			return nodes.containsValue(node);
		}
	}


	public NetNode updateNodeInfo(NodeHash srcNodeHash, InetAddress address, String nodeName) 
	{
		synchronized(nodes)
		{
			NetNode node = nodes.get(srcNodeHash); 
			if( node == null )
			{
				node = new NetNode(srcNodeHash, address, nodeName);
				nodes.put(srcNodeHash, node);
				log.log(Level.SEVERE,toString());
			}
			else
			{
				node.markPacketReceptionTime();
				if(!node.getName().equalsIgnoreCase(nodeName) )
				{
					log.log(Level.SEVERE,"Network node changed name!");
					node.setName(nodeName);
				}
			}		

			return node;
		}
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Nodes: ");
		synchronized(nodes)
		{
			for(NetNode n : nodes.values())
			{
				sb.append(n.getName());
				sb.append(": ");
				sb.append(n.getAddress());
				sb.append("\n");
			}
		}
		return sb.toString();
	}
}
