package ru.dz.shipMaster.net.protocol;

public class ProtocolDefs {
	public static final int		VERSION = 0;
	
	public static final int		UDP_PORT = 16013; // Seems to be unused in Linux /etc/services
	public static final int		MAX_PACKET_SIZE = 8192; // Reception
	
	//public static final InetAddress multicastGroup = InetAddress.getByName("238.16.0.13");
	public static final String multicastGroupName = "238.16.0.13";
}
