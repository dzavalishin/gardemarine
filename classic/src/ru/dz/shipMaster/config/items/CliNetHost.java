package ru.dz.shipMaster.config.items;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.dz.shipMaster.config.ConfigListItem;
import ru.dz.shipMaster.net.sign.NetSigner;
import ru.dz.shipMaster.ui.config.item.CieNetHost;

public class CliNetHost extends ConfigListItem {
    private static final Logger log = Logger.getLogger(CliNetHost.class.getName()); 

	private String hostName = "";
	private String hostPassword = "";

	private NetSigner signer = new NetSigner(); 

	
	@Override
	public void destroy() {
		signer = null;
		dialog = null;			
	}

	// Dialog
	
	private CieNetHost dialog = null;
	
	@Override
	public void displayPropertiesDialog() {
		if(!hasEditRight()) return;
		if(dialog == null) dialog = new CieNetHost(this);
		dialog.displayPropertiesDialog(); 
		}


	
	@Override
	public String getName() { return hostName; }



	public String getHostName() {		return hostName;	}
	public void setHostName(String hostName) {		this.hostName = hostName;	}


	public String getHostPassword() {		return hostPassword;	}
	public void setHostPassword(String hostPassword) 
	{		
		this.hostPassword = hostPassword;	
		try {
			signer.setPassword(hostPassword);
		} catch (UnsupportedEncodingException e) {
			log.log(Level.SEVERE,"Digital signature password error", e);
		}
	}



	public NetSigner getSigner() {		return signer;	}

}
