package ru.dz.shipMaster.net.server;

import java.awt.Image;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.dz.shipMaster.config.items.CliAlarm.NearStatus;
import ru.dz.shipMaster.data.AbstractMinimalDataSink;
import ru.dz.shipMaster.data.DataSource;
import ru.dz.shipMaster.net.protocol.NetException;
import ru.dz.shipMaster.net.protocol.NetIO;
import ru.dz.shipMaster.net.protocol.NetPacket;
import ru.dz.shipMaster.net.protocol.PacketKind;
import ru.dz.shipMaster.net.protocol.PacketType;

/**
 * List of datasources to send data from.
 * @author dz
 *
 */

public class SendList {
    private static final Logger log = Logger.getLogger(SendList.class.getName()); 

	private final List<SendListItem> list = new LinkedList<SendListItem>();  
	//private DataSource ds;

	/**
	 * Add data stream to send.
	 * @param name Data stream name.
	 * @param ds Get data/parameters from.
	 */
	public void add( String name, DataSource ds )
	{
		synchronized(list)
		{
			list.add( new SendListItem(name,ds) );
		}
	}
	
	public void clear()
	{
		synchronized(list)
		{
			for(SendListItem i : list)
				i.dispose();
			list.clear();
		}
	}
	
	/**
	 * Send all the changed values.
	 */
	void sendChanged(NetIO io) 
	{
		for( SendListItem sli : list )
		{
			try {
				sli.sendChanged(io);
			} catch (NetException e) {
				log.log(Level.SEVERE,"Can't send value",e);
			}
		}
	}
	
	/**
	 * Send description for given name.
	 * @param io Net io object to send with.
	 * @param dsName Name of parameter to send description for.
	 * @throws NetException
	 */
	public void sendDescription(NetIO io, String dsName ) throws NetException
	{
		for( SendListItem sli : list )
		{
			if(dsName.equalsIgnoreCase(sli.getName()))
				sli.sendDescription(io);
		}
	}

	public void sendData(NetIO io, String dsName) throws NetException {
		for( SendListItem sli : list )
		{
			if(dsName.equalsIgnoreCase(sli.getName()))
				sli.sendData(io);
		}
	}

}


class SendListItem
{
	private String 					name;  
	private DataSource 				dataSource;
	protected volatile double		value;
	protected volatile NearStatus 	nearStatus;
	protected volatile boolean 		touch = false;
	private NetSendDataSink			mySink = new NetSendDataSink();
	
	SendListItem( String name, DataSource ds )
	{
		this.name = name;
		this.dataSource = ds;
		this.dataSource.addMeter(mySink);
	}

	/**
	 * Must be called on item deletion.
	 */
	public void dispose()
	{
		this.dataSource.removeMeter(mySink);
	}
	
	
	
	@Override
	protected void finalize() throws Throwable {
		dispose();
		super.finalize();
	}

	public void sendChanged(NetIO io) throws NetException {
		//double toSend;
		double nCrit;
		double nWarn;
		synchronized (this) {
			if(!touch)
				return;
			//toSend = value;
			if(nearStatus != null)
			{
				nCrit = nearStatus.nearCriticalLevel;
				nWarn = nearStatus.nearWarningLevel;
			}
			else
				nCrit = nWarn = 0;
			touch = false;
		}
		/*NetPacket p = new NetPacket(PacketKind.BROADCAST, PacketType.DATA);
		p.setItem("NAME", name);
		p.setItem("DATA", toSend);
		io.sendPacket(p);*/
		doSendData( io,  value, nCrit, nWarn ); 

	}

	public void sendData(NetIO io) throws NetException 
	{
		/*NetPacket p = new NetPacket(PacketKind.BROADCAST, PacketType.DATA);
		p.setItem("NAME", name);
		p.setItem("DATA", value);
		io.sendPacket(p);*/
		doSendData( io,  value, 
				nearStatus == null ? 0 : nearStatus.nearCriticalLevel, 
				nearStatus == null ? 0 : nearStatus.nearWarningLevel ); 
	}
	
	private void doSendData(NetIO io, double value, double nCrit, double nWarn ) throws NetException
	{
		NetPacket p = new NetPacket(PacketKind.BROADCAST, PacketType.DATA);
		p.setItem("NAME", name);
		p.setItem("DATA", value);
		p.setItem("NCRI", nCrit);
		p.setItem("NWAR", nWarn);
		io.sendPacket(p);
	}
	
	public void sendDescription(NetIO io) throws NetException
	{
		// TODO don't send it directly, store and send later to prevent duplicates and flood
		NetPacket p = new NetPacket(PacketKind.BROADCAST, PacketType.INFO);
		p.setItem("NAME", name);
		p.setItem("MINV", dataSource.getMin());
		p.setItem("MAXV", dataSource.getMax());
		p.setItem("LABL", dataSource.getLegend());
		p.setItem("UNIT", dataSource.getUnits());
		io.sendPacket(p);
	}
	
	public DataSource getDataSource() {		return dataSource;	}
	public String getName() {		return name;	}

	
	protected class NetSendDataSink extends AbstractMinimalDataSink//implements DataSink
	{
		@Override
		public void receiveImage(Image val) {
			// TODO Send images over the net?
		}

		@Override
		public void receiveString(String val) {
			// TODO Send strings over net?
			
		}

		
		public void setCurrent(double newCurr) 
		{ 
			synchronized (this) {
				if(value != newCurr) 
					touch = true; 
				value = newCurr;
			}
		}

		@Override
		public void setCurrent(double newCurr, NearStatus nearStatus) {
			synchronized (this) {
				if(value != newCurr) 
					touch = true; 
				value = newCurr;
				SendListItem.this.nearStatus = nearStatus;
			}
		}

		
	}	
	
}