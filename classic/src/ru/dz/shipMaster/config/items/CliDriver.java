package ru.dz.shipMaster.config.items;

import javax.swing.JPanel;

import ru.dz.shipMaster.config.ConfigListItem;
import ru.dz.shipMaster.dev.AbstractDriver;
import ru.dz.shipMaster.ui.config.item.CieDriver;

public class CliDriver extends ConfigListItem {

	private AbstractDriver myDriver = null;
	@SuppressWarnings("unused")
	private JPanel setupPanel;


	@Override
	public void destroy() {
		if(myDriver != null)
			myDriver.destroy();
		myDriver = null;
		dialog = null;			
	}

	
	public AbstractDriver getDriver() { return myDriver; }

	public void setDriver(AbstractDriver driver) {
		if(myDriver != null)
			if(myDriver.isRunning())
				return;

		if(myDriver != null)
		{
			myDriver.stop(); // For any case
			myDriver = null;
			setDialogDriverField();
		}

		if(driver != null)
		{
			myDriver = driver;
			setDialogDriverField();
		}

	}


	@Override
	public String getName() 
	{ 
		//AbstractDriver myDriver = bean.getDriver();
		if(myDriver != null)
		{
			String cn = myDriver.getClass().getName();
			if(cn != null)
			{
				if( cn.startsWith(CieDriver.DRV_CLASS_PREFIX))
					cn = cn.substring(CieDriver.DRV_CLASS_PREFIX.length());
				return cn + " (" + myDriver.getInstanceName() + ")";
			}
		}
		return "? No driver"; 
	}


	private void setDialogDriverField() {
		if(dialog == null) dialog = new CieDriver(this,myDriver);
		else dialog.setDriver(myDriver);		
	}




	private CieDriver dialog = null;
	
	@Override
	public void displayPropertiesDialog() {
		if(!hasEditRight()) return;
		if(dialog == null) dialog = new CieDriver(this,myDriver);
		dialog.displayPropertiesDialog(); 
		}
	
	
}
