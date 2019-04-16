package ru.dz.shipMaster.dev.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JPanel;
import javax.swing.JTextField;

import ru.dz.shipMaster.data.misc.CommunicationsException;

public class PipeBiPipe extends GenericBiPipe {

	private static final int MIN_CAP = 100;

	private Process child = null;

	private String commandToExec = null;

	private InputStream inputStream = null;
	private OutputStream outputStream = null;
	@SuppressWarnings("unused")
	private InputStream errorStream = null;

	private JPanel panel;

	private JTextField cmdField = new JTextField(16);

	public PipeBiPipe()
	{		
	}

	public PipeBiPipe(String commandToExec)
	{
		this.commandToExec = commandToExec;
	}

	//byte[] readBs = new byte[256];

	@Override
	protected void readStep() {
		boolean got = false;
		try {
			//inputStream.read(readBs);

			while(inputStream.available() > 0 && recvQueue.remainingCapacity() > MIN_CAP)
			{

				int read = inputStream.read();
				got = true;
				//System.out.append((char)read);
				
				if(read < 0)
				{
					reader.stop();
					//kickErrorEvent(new End);
					return;
				}
				recvQueue.put((byte)read);
				
				// Force events on \r\n\0
				if(read == 0x0D || read == 0x0A || read == 0)
					break;
			}
		} catch (InterruptedException e) {
			// Ignore
			return;
		} catch (IOException e) {
			kickErrorEvent(e);
		} 
		finally 
		{
			if(got) kickReceiveEvent();
		}
	}

	@Override
	protected void writeStep() {
		byte[] bs;
		try {
			bs = sendQueue.take();
			outputStream.write(bs);
			synchronized (sendQueue) {
				sendQueue.notifyAll();
			}
		} catch (InterruptedException e) {
			// Ignore
			return;
		} catch (IOException e) {
			kickErrorEvent(e);
		}
	}

	@Override
	public void connect() throws CommunicationsException {
		if(commandToExec == null)
			throw new CommunicationsException("No cmd to exec");

		try {

			child = Runtime.getRuntime().exec(commandToExec);

			inputStream = child.getInputStream();
			outputStream = child.getOutputStream();
			errorStream = child.getErrorStream();			

			startThreads();

			connected = true;
		} catch (IOException e) {
			try { disconnect(); } catch(Throwable e1) {}

			throw new CommunicationsException(e);
		}
	}

	@Override
	public void disconnect() throws CommunicationsException {
		connected = false;

		stopThreads();

		inputStream = null;
		outputStream = null;
		errorStream = null;
		if( child != null ) child.destroy();
	}

	@Override
	protected void finalize() throws Throwable {
		try { disconnect(); }
		finally { super.finalize(); }
	}
	
	
	@Override
	public String getEndPointName() { return commandToExec; }

	@Override
	public String getTypeName() { return "Pipe"; }

	// We just ignore halfduplex, as it is not relevant for pipe io
	@Override
	protected void doSetHalfDuplexMode(boolean newHalfDuplexMode) 
	throws NoHalfDuplexException {
		// Redefined just to remove exception throw.
	}





	public String getCommandToExec() {
		return commandToExec;
	}

	public void setCommandToExec(String commandToExec) {
		this.commandToExec = commandToExec;
	}





	@Override
	public JPanel getSetupPanel() {
		if( panel != null )
			return panel;

		panel = new JPanel();

		panel.add(cmdField );

		return panel;
	}


	@Override
	public void loadPanelSettings() {
		cmdField.setText(commandToExec);
	}

	@Override
	public void savePanelSettings() {
		commandToExec = cmdField.getText();
	}


}


