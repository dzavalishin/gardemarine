package ru.dz.shipMaster.dev;

import java.util.logging.Logger;

import javax.swing.JLabel;

import ru.dz.shipMaster.data.misc.CommunicationsException;


/**
 * Abstract bus. Base class for all the buses.
 * @author dz
 */
public abstract class AbstractBus extends AbstractModule implements IBus {
	protected static final Logger log = Logger.getLogger(AbstractBus.class.getName()); 







	@Override
	protected void mainSetupPanel()
	{
		super.mainSetupPanel();
		
		moduleSetupPanel.add(new JLabel("Bus:"), consL);
		moduleSetupPanel.add(new JLabel(getDeviceName()), consR);

	}

	



	/**
	 * Deactivate bus, close and detach all resources.
	 * To be implemented in subclass.
	 * @throws CommunicationsException if can not stop for some reason
	 */
	protected abstract void doStopBus() throws CommunicationsException;

	/**
	 * Activate bus, allocate and open all resources.
	 * To be implemented in subclass.
	 * @throws CommunicationsException if can not start due to device communications error
	 */
	protected abstract void doStartBus() throws CommunicationsException;

	protected final void doStartModule() throws CommunicationsException { doStartBus(); }
	protected final void doStopModule() throws CommunicationsException {doStopBus(); }

	

	
	/* (non-Javadoc)
	 * @see ru.dz.shipMaster.dev.IModule#destroy()
	 */
	public void destroy() {
		//stop();
		super.destroy();
	}

}
