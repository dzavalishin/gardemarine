package ru.dz.shipMaster.dev.common;

import java.io.IOException;
import java.util.TooManyListenersException;
import java.util.logging.Level;

import javax.swing.JPanel;
import javax.swing.JTextField;

import jd2xx.JD2XX;
import jd2xx.JD2XXEvent;
import jd2xx.JD2XXEventListener;
import ru.dz.shipMaster.data.misc.CommunicationsException;

public class FT232BiPipe extends AbstractSerialBiPipe {


	private int devNum = 0;
	private JD2XX jd = new JD2XX();
	private JPanel panel;
	private JTextField portNoField  = new JTextField(4);
	private JTextField baudRateField  = new JTextField(4);

	public FT232BiPipe()
	{
	}

	public FT232BiPipe(int devNum, int baudRate) throws CommunicationsException, IOException
	{
		this.devNum = devNum;
		this.baudRate = baudRate;
	}

	@Override
	protected void readStep() {
		reader.stop(); // I don't need me
		reader.sleep(20000); // Do nothing in case stop failed :)
	}

	@Override
	protected void writeStep() {
		byte[] bs;
		try {
			bs = sendQueue.take();
			synchronized (sendQueue) {
				sendQueue.notifyAll();
			}
		} catch (InterruptedException e) {
			// Ignore
			return;
		}

		try {
			halfDuplexStartSend();
			jd.write(bs,0,bs.length);
			halfDuplexEndSend();
		} catch (IOException e) {
			kickErrorEvent(e);
		}
	}

	
	
	@Override
	public void connect() throws CommunicationsException {
		try {
			jd.open(devNum); 

			jd.setChars(0, false, 0xFF, false);
			jd.setBaudRate(baudRate);
			//jd.setLatencyTimer(2); // lowest!

			switch(flowControl)
			{
			case RTSCTS:
				jd.setFlowControl(JD2XX.FLOW_RTS_CTS, 0, 0);
				break;
			case DTRDSR:
				jd.setFlowControl(JD2XX.FLOW_DTR_DSR, 0, 0);
				break;
			case XonXoff:
				jd.setFlowControl(JD2XX.FLOW_XON_XOFF, 0, 0);
				break;
			default:
				jd.setFlowControl(JD2XX.FLOW_NONE, 0, 0);
				break;
			}
			
			jd.setDataCharacteristics(JD2XX.BITS_8, JD2XX.STOP_BITS_1, JD2XX.PARITY_NONE);

			//jd.setUSBParameters(64, 64);

			jd.addEventListener(new JD2XXEventListener() {

				@Override
				public void jd2xxEvent(JD2XXEvent event) {
					if(event.getEventType() == JD2XXEvent.EVENT_RXCHAR)
					{
						try {
							while(jd.getQueueStatus() > 0)
							{
								recvQueue.offer((byte)jd.read());
								halfDuplexNoteReception();
							}
						} catch (IOException e) {
							log.log(Level.SEVERE,"exception",e);
						}
						finally
						{
							kickReceiveEvent();
						}
					}

				}});
			jd.notifyOnRxchar(true);
			connected = true;
		} catch (TooManyListenersException e) {
			throw new CommunicationsException(e);
		} catch (IOException e) {
			throw new CommunicationsException(e);
		}
	}

	@Override
	public void disconnect() throws CommunicationsException {
		try {
			connected = false;
			jd.notifyOnRxchar(false);
			jd.close();
		} catch (IOException e) {
			throw new CommunicationsException(e);
		}
	}

	@Override
	public String getEndPointName() { return Integer.toString(devNum); }

	@Override
	public String getTypeName() { return "FT232 USB device"; }

	@Override
	protected void doSetHalfDuplexMode(boolean newHalfDuplexMode) 
	throws NoHalfDuplexException {
		// Redefined just to remove exception throw.
	}

	@Override
	public JPanel getSetupPanel() {
		if( panel != null )
			return panel;

		panel = new JPanel();

		panel.add(portNoField);
		panel.add(baudRateField);

		return panel;
	}

	@Override
	public void loadPanelSettings() {
		portNoField.setText(Integer.toString(devNum));
		baudRateField.setText(Integer.toString(baudRate));
	}

	@Override
	public void savePanelSettings() {
		try {
			devNum = Integer.parseInt(portNoField.getText());
		} catch(NumberFormatException e)
		{
			log.log(Level.SEVERE,"Invalid port no");
		}
		try {
			baudRate = Integer.parseInt(baudRateField.getText());
		} catch(NumberFormatException e)
		{
			log.log(Level.SEVERE,"Invalid baud rate");
		}
	}


}
