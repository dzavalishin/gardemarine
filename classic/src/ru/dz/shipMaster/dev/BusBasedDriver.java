package ru.dz.shipMaster.dev;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ru.dz.shipMaster.config.Configuration;
import ru.dz.shipMaster.config.items.CliBus;
import ru.dz.shipMaster.ui.misc.VisualHelpers;

public abstract class BusBasedDriver<BusType extends IBus> extends ThreadedDriver {

	protected BusBasedDriver(long updateIntervalMsec, int priority,
			String threadName) {
		super(updateIntervalMsec, priority, threadName);
	}





	// -----------------------------------------------------------------
	// overrides
	// -----------------------------------------------------------------
	
	@Override
	protected void doLoadPanelSettings() {
		newBus = cliBus;
		setDispName();
	}

	@Override
	protected void doSavePanelSettings() {
		setCliBus( newBus );
		setDispName();
	}
	
	@Override
	protected void setupPanel(final JPanel panel) {
		panel.add(new JLabel("Bus"),consL);
		panel.add(selectBusButton  ,consR);
		selectBusButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				CliBus selBus = Configuration.selectBus(panel);				
				if(selBus == null) return;

				if( !correctBusType(selBus.getBus()) )
				{
					//showMessage("Wrong bus type");
					VisualHelpers.showMessageDialog(panel, "Wrong bus type");
					return;
				}

				newBus = selBus;
				setDispName();
			}} );
	}
	
	@Override
	public String getInstanceName() {
		return getDeviceName()+" @ "+( cliBus == null ? "?" : cliBus.getName());
	}

	// -----------------------------------------------------------------
	// to implement in children
	// -----------------------------------------------------------------

	
	/**
	 * Must return true if cliBus is of good type for this driver.
	 * @param cliBus Bus to check.
	 * @return True if cliBus is ok to use.
	 */
	protected abstract boolean correctBusType(IBus bus);

	// -----------------------------------------------------------------
	// local code
	// -----------------------------------------------------------------
	
	private JButton selectBusButton = new JButton("None");
	private CliBus cliBus;
	private CliBus newBus;
	
	/** Bus instance to use. */
	protected BusType bus;

	private void setDispName() {
		selectBusButton.setText(cliBus == null ? "None" : newBus.getName());
		selectBusButton.repaint(100);
	}

	
	public CliBus getCliBus() {		return cliBus;	}
	@SuppressWarnings("unchecked")
	public void setCliBus(CliBus newBus) {		

		/*if( (newBus != null) && (! (newBus.getBus() instanceof NmeaBus)) ) {
			newBus = null;
			showMessage("Wrong cliBus type, need Nmea0183");
		}*/

		this.cliBus = newBus;	
		if(cliBus == null)
			bus = null;
		else
		{
			try {
				bus = (BusType) cliBus.getBus();
			}
			catch(Throwable e)
			{
				log.log(Level.SEVERE, "Bus type mismatch",e);
				bus = null;
			}
			
		}

	}
	
	
}
