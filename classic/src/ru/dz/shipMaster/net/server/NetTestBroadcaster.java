package ru.dz.shipMaster.net.server;

import java.io.IOException;
import java.net.SocketException;

import ru.dz.shipMaster.localhost.CPULoadDataSource;


public class NetTestBroadcaster extends NetServer {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NetTestBroadcaster me;
		try {
			me = new NetTestBroadcaster();
			me.setRunning(true);
			
			me.go();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	private NetTestBroadcaster() throws SocketException, IOException
	{
		super("TestNode");
		
		CPULoadDataSource ds = new CPULoadDataSource();
		sendList.add("DiskFree", ds);
	}

	private void go() {
		try {
			System.out.println("Press Enter to stop");
			while(true)
			{
				synchronized(this) { wait(1000); }
				if(System.in.available() > 0 )
				{
					System.in.read();
					System.out.println("Exiting");
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	protected void newItemEvent(String host, String senderDataSourceName) {
		System.out.print("\nNew item: "+host+" : "+senderDataSourceName+"\n\n");
	}
	
	
}
