package ru.dz.shipMaster.net.protocol;


public enum PacketType {
	PING,
	NODE,
	INFO,
	DATA
}

/*
public class PacketType {
    private static final Logger log = Logger.getLogger(PacketType.class.getName()); 

	private final String type;

	private PacketType(String type) {		this.type = type;	}
	public String getType() {		return type;	} 

	/**
	 * Information about sending node as a whole. 
	 * /
	public static final PacketType NODE_ADVERT = new PacketType("NODE"); 

	/**
	 * Information about data sources at this node. 
	 * /
	public static final PacketType DATA_INFO = new PacketType("INFO"); 
	
	/**
	 * Data itself. 
	 * /
	public static final PacketType DATA_UPDATE = new PacketType("DATA");

	public static PacketType typeByName(String typeName) {
		if(typeName.equalsIgnoreCase("NODE")) return NODE_ADVERT;
		if(typeName.equalsIgnoreCase("INFO")) return DATA_INFO;
		if(typeName.equalsIgnoreCase("DATA")) return DATA_UPDATE;
		
		return null;
	}

	
}
*/