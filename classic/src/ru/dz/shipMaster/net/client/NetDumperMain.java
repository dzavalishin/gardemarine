package ru.dz.shipMaster.net.client;

import java.io.IOException;
import java.net.SocketException;

public class NetDumperMain extends NetClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NetDumperMain dumper;
		try {
			dumper = new NetDumperMain();
			dumper.go();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	private NetDumperMain() throws SocketException, IOException
	{
		
	}

	private void go() {
		try {
			System.out.println("Press Enter to stop");
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
