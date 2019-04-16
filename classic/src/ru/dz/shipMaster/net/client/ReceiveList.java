package ru.dz.shipMaster.net.client;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.dz.shipMaster.net.protocol.NetException;

public class ReceiveList {
    public static final String NETWORK_ERROR = "NETWORK ERROR";

	private static final Logger log = Logger.getLogger(ReceiveList.class.getName()); 

	Map<String,ReceiveListItem> map = new HashMap<String,ReceiveListItem>();

	
	/**
	 * Request senders to resend info adn data for items we need.
	 */
	public void requestEmpty(NetClient client) 
	{
		for( ReceiveListItem rli : map.values() )
		{
			try {
				if(!rli.isReceivedItemInfo())
					client.requestItemDescription(rli.getSenderNodeName(), rli.getSenderDataSourceName());
				if(!rli.isReceivedItemData())
					client.requestDataUpdate(rli.getSenderNodeName(), rli.getSenderDataSourceName());
			} catch (NetException e) {
				log.log(Level.SEVERE,"Can't send value",e);
			}
		}
	}
	
	
	private static String makeName(String senderNodeName, String senderDataSourceName)
	{
		return senderNodeName.toLowerCase() + ":" + senderDataSourceName.toLowerCase();
	}
	
	private void add(NetDataSource ds, String senderNodeName, String senderDataSourceName )
	{
		map.put(makeName(senderNodeName, senderDataSourceName),
			new ReceiveListItem(senderNodeName, senderDataSourceName, ds));
	}

	
	public ReceiveListItem getItem( String senderNodeName, String senderDataSourceName )
	{
		ReceiveListItem item = map.get(makeName(senderNodeName, senderDataSourceName));
		
		if(item == null)
		{
			setInfo(senderNodeName, senderDataSourceName, 0, 100, NETWORK_ERROR, NETWORK_ERROR);
			item = map.get(makeName(senderNodeName, senderDataSourceName));
		}
		
		return item;
	}
	
	public NetDataSource getDataSource( String senderNodeName, String senderDataSourceName )
	{
		ReceiveListItem item = getItem( senderNodeName, senderDataSourceName );
		if(item == null)
			return null;
		return item.getDataSource();
	}
	
	/*void setValue(double value, String senderNodeName, String senderDataSourceName) throws NetNoItemException
	{
		String name = makeName(senderNodeName, senderDataSourceName);
		ReceiveListItem item = map.get(name);
		if(item==null)
		{
			log.log(Level.SEVERE,"Data for unknown ReceiveListItem received: "+name);
			throw new NetNoItemException("Data for unknown ReceiveListItem received: "+name);
		}
		item.setValue(value);
	}*/

	/**
	 * 
	 * @param senderNodeName
	 * @param senderDataSourceName
	 * @param minValue
	 * @param maxValue
	 * @param label
	 * @param unit
	 * @return True if no data was received for this item yet. Used to request data update after creation.
	 */
	public boolean setInfo(String senderNodeName, String senderDataSourceName, double minValue, double maxValue, String label, String unit) {
		ReceiveListItem item = map.get(makeName(senderNodeName, senderDataSourceName));
		if(item == null)
		{
			NetDataSource ds = new NetDataSource(minValue,maxValue,label,unit);
			add(ds,senderNodeName, senderDataSourceName);
			return true;
		}
		else
		{
			item.setInfo(minValue,maxValue,label,unit);
			return !item.isReceivedItemData();
		}
	}
	
}


class ReceiveListItem
{
	private NetDataSource dataSource;
	private final String senderNodeName;
	private final String senderDataSourceName;
	
	/**
	 * This one must be true if sender sent us information about this item.
	 */
	private boolean receivedItemInfo = false;
	
	/**
	 * This one gets true when at least one data packet is received for this item.
	 */
	private boolean receivedItemData = false;
	
	ReceiveListItem( String senderNodeName, String senderDataSourceName, NetDataSource ds )
	{
		this.senderNodeName = senderNodeName;
		this.senderDataSourceName = senderDataSourceName;
		this.dataSource = ds;		
	}


	public NetDataSource getDataSource() 	{ return dataSource;			}
	public String getSenderDataSourceName() { return senderDataSourceName;	}
	public String getSenderNodeName() 		{ return senderNodeName;		}

	void setValue(double value) { dataSource.setCurrentValue(value); receivedItemData = true; }

	void setInfo(double minValue, double maxValue, String label, String unit) {
		if( 
				(dataSource.getMin() != minValue) ||
				(dataSource.getMax() != maxValue) ||
				(!dataSource.getLegend().equalsIgnoreCase(label)) ||
				(!dataSource.getUnits().equalsIgnoreCase(unit)) )
		{
			dataSource.setInfo(minValue, maxValue, label, unit);
			receivedItemInfo = true;
		}
	}


	/**
	 * This one must be true if sender sent us information about this item.
	 */
	public boolean isReceivedItemInfo() {		return receivedItemInfo;	}
	/**
	 * This one must be true if sender sent us information about this item.
	 */
	public void setReceivedItemInfo(boolean receivedItemInfo) {		this.receivedItemInfo = receivedItemInfo;	}


	/**
	 * This one gets true when at least one data packet is received for this item.
	 */
	public boolean isReceivedItemData() {		return receivedItemData;	}
	/**
	 * This one gets true when at least one data packet is received for this item.
	 */
	public void setReceivedItemData(boolean receivedItemData) {		this.receivedItemData = receivedItemData;	}
}

