package ru.dz.shipMaster.config.items;

import javax.swing.JPanel;

import ru.dz.shipMaster.config.ConfigListItem;
import ru.dz.shipMaster.dev.IBus;
import ru.dz.shipMaster.ui.config.item.CieBus;
import ru.dz.shipMaster.ui.config.item.CieDriver;

public class CliBus extends ConfigListItem {

	private IBus myBus = null;
	@SuppressWarnings("unused")
	private JPanel setupPanel;


	@Override
	public void destroy() {
		if(myBus != null)
			myBus.destroy();
		myBus = null;
		dialog = null;			
	}

	
	public IBus getBus() { return myBus; }

	public void setBus(IBus bus) {
		if(myBus != null)
			if(myBus.isRunning())
				return;

		if(myBus != null)
		{
			myBus.stop(); // For any case
			myBus = null;
			setDialogDriverField();
		}

		if(bus != null)
		{
			//drvLabel.setText(driver.getDriverDeviceName());

			myBus = bus;
			//panel.add(myDriver.getSetupPanel());
			//setSFPanel(myDriver.getSetupPanel());
			setDialogDriverField();
			//searchButton.setEnabled(myDriver.isAutoSeachSupported());
		}

	}


	@Override
	public String getName() 
	{ 
		//AbstractDriver myDriver = bean.getDriver();
		if(myBus != null)
		{
			String cn = myBus.getClass().getName();
			if(cn != null)
			{
				if( cn.startsWith(CieDriver.DRV_CLASS_PREFIX))
					cn = cn.substring(CieDriver.DRV_CLASS_PREFIX.length());
				return cn + " (" + myBus.getInstanceName() + ")";
			}
		}
		return "? No driver"; 
	}


	private void setDialogDriverField() {
		if(dialog == null) dialog = new CieBus(this,myBus);
		else dialog.setBus(myBus);		
	}




	private CieBus dialog = null;
	
	@Override
	public void displayPropertiesDialog() {
		if(!hasEditRight()) return;
		if(dialog == null) dialog = new CieBus(this,myBus);
		dialog.displayPropertiesDialog(); 
		}
	
	
}
