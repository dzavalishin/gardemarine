package ru.dz.shipMaster.dev.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import ru.dz.shipMaster.data.misc.CommunicationsException;

/**
 * Implementation of BiPipe for TCP transport.
 * @author dz
 *
 */
public class TcpBiPipe extends AbstractTcpBiPipe {
	private Socket socket = null;
	private OutputStream outputStream;

	public TcpBiPipe() 
	{
	}

	public TcpBiPipe(InetAddress targetAddress, int targetPort)
	{
		inetAddress = targetAddress;
		port = targetPort;
	}

	@Override
	protected void readStep() {
		try {
			InputStream is = socket.getInputStream();
			//while( is.available() > 0 )
			int max = 128;
			while( (max-- > 0) && (is.available() > 0) )
			{
				int read = is.read();
				if(read < 0)
				{
					//reader.stop();
					//return;
					break;
				}

				recvQueue.put( (byte)read );
				//System.out.print((char)read);
			}

		} catch (IOException e) {
			kickErrorEvent(e);
		} catch (InterruptedException e) {
			return;
		} 
		finally {
			kickReceiveEvent();
		}

	}

	@Override
	protected void writeStep() { writeStep(outputStream); }

	@Override
	public void connect() throws CommunicationsException {
		socket = new Socket();	
		try {
			socket.connect(new InetSocketAddress(inetAddress,port));
			outputStream = socket.getOutputStream();
		} catch (IOException e) {
			throw new CommunicationsException("Can't connect to host", e);
		}
		startThreads();
		connected = true;
	}

	@Override
	public void disconnect() throws CommunicationsException {
		connected = false;

		stopThreads();
		try {
			if(socket != null) socket.close();
		} catch (IOException e) {
			throw new CommunicationsException("Can't disconnect from host", e);
		}
	}


	@Override
	public String getTypeName() { return "TCP"; }

	
	


	
	
}
