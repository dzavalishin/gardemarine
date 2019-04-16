package ru.dz.shipMaster.config;

import java.util.logging.Logger;

import ru.dz.shipMaster.misc.RightsControl;

//public abstract class ConfigListItem implements IConfigListItem, RightsControl 

public abstract class ConfigListItem extends ContainerInformer implements RightsControl
{
    protected static final Logger log = Logger.getLogger(ConfigListItem.class.getName()); 

    
    
    private static boolean rightsDisabled = false;
    /** For debug puproses. */
    public static void setRightsDisabed() { rightsDisabled = true; }
    
	/* (non-Javadoc)
	 * @see ru.dz.shipMaster.config.IConfigListItem#getName()
	 */
	public abstract String getName(); // { return Messages.getString("ConfigListItem.UndefItem"); } //$NON-NLS-1$

    /* (non-Javadoc)
	 * @see ru.dz.shipMaster.config.IConfigListItem#toString()
	 */
    @Override
	public String toString() {
    	if( getName() == null )
    		return "";
    	return getName(); 
    	}
    
    /* (non-Javadoc)
	 * @see ru.dz.shipMaster.config.IConfigListItem#displayPropertiesDialog()
	 */
    public abstract void displayPropertiesDialog();

    protected boolean hasEditRight()
    {
    	if(rightsDisabled) return true;
    	/*if(ConfigurationFactory.getTransientState().hasEditRight(this.getClass()))
    		return true;
    	VisualHelpers.showMessageDialog(null, "You have no right to edit it");
    	return false;*/
    	return ConfigurationFactory.getTransientState().assertEditRight(this.getClass());
    }

}
