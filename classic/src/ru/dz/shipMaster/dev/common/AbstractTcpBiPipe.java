package ru.dz.shipMaster.dev.common;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public abstract class AbstractTcpBiPipe extends GenericBiPipe {

	protected InetAddress inetAddress;
	protected int port = 0;

	private JPanel panel;
	private JTextField tcpAddrField = new JTextField(14);
	private JTextField tcpPortField = new JTextField(6);

	@Override
	public String getEndPointName() {
		if(inetAddress == null)
			return "(none)";
		return inetAddress.getHostName()+":"+port; 
		}
	

	
	
	
	@Override
	public JPanel getSetupPanel() {
		if( panel != null )
			return panel;

		panel = new JPanel();

		panel.add( new JLabel("IP/port: ") );
		panel.add( tcpAddrField );
		panel.add( tcpPortField );

		return panel;
	}

	@Override
	public void loadPanelSettings() {
		try {
		tcpAddrField.setText( inetAddress.getHostAddress() );
		tcpPortField.setText( Integer.toString( port ) );
		} catch(Throwable e) {}		
	}

	@Override
	public void savePanelSettings() {
		try {
			port = Integer.parseInt(tcpPortField.getText());		
			inetAddress = InetAddress.getByName(tcpAddrField.getText());

		} catch(NumberFormatException e)
		{
			log.log(Level.SEVERE,"Invalid port no");
		} catch (UnknownHostException e) {
			log.log(Level.SEVERE,"Invalid host name");
		}
		
	}

	
	
	
	
	
	public String getTcpAddress() {		return (inetAddress == null) ? null : inetAddress.getHostAddress();	}
	public void setTcpAddress(String addr) throws UnknownHostException {		
		inetAddress = InetAddress.getByName(addr); 
		}
	
	public int getPort() {		return port;	}
	public void setPort(int port) {		this.port = port;	}
	
	

}
