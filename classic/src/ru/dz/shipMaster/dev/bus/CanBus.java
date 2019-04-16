package ru.dz.shipMaster.dev.bus;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.dev.bus.ModBusProtocolAdapter.Mode;
import ru.dz.shipMaster.dev.common.TcpBiPipe;

/**
 * CAN bus interface.
 * Not done.
 * @author dz
 *
 */

public class CanBus extends AbstractPipedBus {

	@Override
	public String getDeviceName() {		return "CanBus";	}

	@Override
	public boolean isAutoSeachSupported() {		return false;	}

	private static final ModBusProtocolAdapter.Mode[] modes = {
		ModBusProtocolAdapter.Mode.Ascii,
		ModBusProtocolAdapter.Mode.RTU,
		ModBusProtocolAdapter.Mode.TCP,
	};
	
	private JComboBox modeField = new JComboBox(modes);
	private ModBusProtocolAdapter.Mode mode = ModBusProtocolAdapter.Mode.Ascii;


	public ModBusProtocolAdapter.Mode getMode() {		return mode;	}
	public void setMode(ModBusProtocolAdapter.Mode mode) {		this.mode = mode;	}

	
	
	
	@Override
	protected void doStartBus() throws CommunicationsException {
		//pipe.getPipe().addReceiveListener(recv);
		//pipe.getPipe().addErrorListener(err);
		pipe.getPipe().connect();
		//protocol.setPipe(pipe.getPipe());
		//protocol.setMode(mode);
	}


	@Override
	protected void doStopBus() throws CommunicationsException {
		pipe.getPipe().disconnect();
		//pipe.getPipe().removeReceiveListener(recv);
		//pipe.getPipe().removeErrorListener(err);
		//protocol.setPipe(null);
	}







	@Override
	protected void setupPanel(JPanel panel) {
		super.setupPanel(panel);
		
		panel.add(new JLabel("Mode"),consL);
		panel.add( modeField ,consR);
		
	}


	@Override
	public void doSavePanelSettings() {
		super.doSavePanelSettings();
		mode = (Mode) modeField.getSelectedItem();
		if( (pipe != null) && (pipe.getPipe() instanceof TcpBiPipe) )
			mode = Mode.TCP;
	}	


	@Override
	public void doLoadPanelSettings() {
		super.doLoadPanelSettings();
		modeField.setSelectedItem(mode);
	}





	public CanMessage sendAndReceive(CanMessage send)
	{
		CanMessage recv = null;
		
		return recv;
	}


	public void send(CanMessage send)
	{
		
	}









}
