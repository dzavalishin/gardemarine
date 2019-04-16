package ru.dz.shipMaster.dev.bus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ru.dz.shipMaster.config.Configuration;
import ru.dz.shipMaster.config.items.CliPipe;
import ru.dz.shipMaster.dev.AbstractBus;

public abstract class AbstractPipedBus extends AbstractBus {
	protected CliPipe pipe;
	private CliPipe newPipe;


	private JButton selectPipeButton = new JButton("None");

	@Override
	protected void doLoadPanelSettings() {
		newPipe = pipe;
		setDispName();
	}

	@Override
	protected void doSavePanelSettings() {
		pipe = newPipe;
		setDispName();
	}

	/** set displayed name */
	protected void setDispName() {
		selectPipeButton.setText( (newPipe == null) ? "None" : newPipe.getName());
		//selectPipeButton.repaint(100);
	}

	
	
	
	@Override
	protected void setupPanel(final JPanel panel) {
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
		return getDeviceName() + ((pipe == null || pipe.getPipe() == null) ? "(disconnected)" : "@"+pipe.getPipe().getEndPointName());
	}
	
	
	public CliPipe getPipe() {		return pipe;	}
	public void setPipe(CliPipe pipe) {		this.pipe = pipe;	}

	
	@Override
	protected void internalStartDebugger() {
	}

	@Override
	protected void internalStopDebugger() {
	}
	
	
}
