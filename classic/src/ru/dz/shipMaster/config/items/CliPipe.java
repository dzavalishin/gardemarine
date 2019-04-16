package ru.dz.shipMaster.config.items;

import java.util.logging.Level;

import javax.swing.JPanel;

import ru.dz.shipMaster.config.ConfigListItem;
import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.dev.common.BiPipe;
import ru.dz.shipMaster.ui.config.item.CieDriver;
import ru.dz.shipMaster.ui.config.item.CiePipe;

public class CliPipe extends ConfigListItem {

	private BiPipe pipe = null;
	@SuppressWarnings("unused")
	private JPanel setupPanel;


	@Override
	public void destroy() {
		if(pipe != null)
			try {
				pipe.disconnect();
			} catch (CommunicationsException e) {
				log.log(Level.SEVERE, "Unable to disconnect", e);
			}
		pipe = null;
		dialog = null;			
	}

	
	public BiPipe getPipe() { return pipe; }

	public void setPipe(BiPipe bus) {
		if(pipe != null)
			if(pipe.isConected())
				return;

		if(pipe != null)
		{
			try {
				pipe.disconnect(); // For any case				
			} catch (CommunicationsException e) {
				log.log(Level.SEVERE, "Unable to disconnect", e);
			}
			pipe = null;
			setDialogDriverField();
		}

		if(bus != null)
		{
			//drvLabel.setText(driver.getDriverDeviceName());

			pipe = bus;
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
		if(pipe != null)
		{
			String cn = pipe.getClass().getName();
			if(cn != null)
			{
				if( cn.startsWith(CieDriver.DRV_CLASS_PREFIX))
					cn = cn.substring(CieDriver.DRV_CLASS_PREFIX.length());
				return cn + " (" + pipe.getEndPointName() + ")";
			}
		}
		return "? No driver"; 
	}


	private void setDialogDriverField() {
		if(dialog == null) dialog = new CiePipe(this,pipe);
		else dialog.setPipe(pipe);		
	}




	private CiePipe dialog = null;
	
	@Override
	public void displayPropertiesDialog() {
		if(!hasEditRight()) return;
		if(dialog == null) dialog = new CiePipe(this,pipe);
		dialog.displayPropertiesDialog(); 
		}
	
	
}
