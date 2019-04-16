package ru.dz.shipMaster.dev.ups;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ru.dz.shipMaster.config.Configuration;
import ru.dz.shipMaster.config.items.CliPipe;
import ru.dz.shipMaster.data.misc.CommunicationsException;

public abstract class PipedUpsDriver extends GeneralUpsDriver {

	protected CliPipe pipe;
	private CliPipe newPipe;

	private JButton selectPipeButton = new JButton("None");

	protected PipedUpsDriver(String threadName) {
		super(threadName);
	}


	@Override
	protected void setupPanel(final JPanel panel) {
		super.setupPanel(panel);
		panel.add(new JLabel("Channel"),consL);
		panel.add(selectPipeButton ,consR);
		selectPipeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				newPipe = Configuration.selectPipe(panel);
				setDispName();
			}} );
	}


	
	@Override
	public String getInstanceName() {
		return getDeviceName() + ((pipe == null) ? "(disconnected)" : "@"+pipe.getPipe().getEndPointName());
	}
	

	@Override
	protected void doLoadPanelSettings() {
		super.doLoadPanelSettings();
		newPipe = pipe;
		setDispName();
	}

	@Override
	protected void doSavePanelSettings() {
		super.doSavePanelSettings();
		pipe = newPipe;
		setDispName();
	}

	
	@Override
	protected void doStartDriver() throws CommunicationsException {
		super.doStartDriver();
		if(pipe != null) pipe.getPipe().connect();
	}

	@Override
	protected void doStopDriver() throws CommunicationsException {
		super.doStopDriver();
		if(pipe != null) pipe.getPipe().disconnect();
	}
	
	
	
	
	
	// try twice in 100 msec
	protected int read(byte data[]) throws IOException {
		if(pipe == null)
			throw new IOException("Not connected");
		int got = 0;
		got = pipe.getPipe().readUnblocked(data);
		if( got == 0 )
		{
			sleep(200);
			return pipe.getPipe().readUnblocked(data);
		}
		return got;
	}
	
	
	protected void drainInput() throws IOException {
		if(pipe == null)
			throw new IOException("Not connected");
		byte[] data = new byte[1];
		while( pipe.getPipe().readUnblocked(data) > 0 )
		{
			/* repeat */;
		}
	}
		
	
	protected void write(byte [] data) throws IOException
	{
		if(pipe == null)
			throw new IOException("Not connected");
		
		try {
			pipe.getPipe().write(data);
		} catch (CommunicationsException e) {
			throw new IOException("Pipe IO Error", e);
		}
	}
	
	
	protected void write(byte data) throws IOException
	{
		if(pipe == null)
			throw new IOException("Not connected");
		
		try {
			pipe.getPipe().write(data);
		} catch (CommunicationsException e) {
			throw new IOException("Pipe IO Error", e);
		}
	}
	
	
	protected void flush()
	{
		// It is barely possible and anyway useless to call flush on nonexisting pipe
		if(pipe == null) return;
		pipe.getPipe().flush();
	}
	
	
	
	/** set displayed name */
	protected void setDispName() {
		selectPipeButton.setText( (newPipe == null) ? "None" : newPipe.getName());
	}

	public CliPipe getPipe() {		return pipe;	}
	public void setPipe(CliPipe pipe) {		this.pipe = pipe;	}

	
}
