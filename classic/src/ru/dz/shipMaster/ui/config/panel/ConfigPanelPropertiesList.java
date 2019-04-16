package ru.dz.shipMaster.ui.config.panel;

import java.util.Vector;

import ru.dz.shipMaster.config.IConfigListItem;
import ru.dz.shipMaster.ui.config.ConfigPanel;
import ru.dz.shipMaster.ui.config.PropertiesEditItemList;

/**
 * This panel is used to edit properties of items which you can not delete or add.
 * @author dz
 *
 */

public class ConfigPanelPropertiesList<ItemType extends IConfigListItem> extends ConfigPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6235435772148547182L;
	private final String listObjectName;
	private final String tip;

	public ConfigPanelPropertiesList(String name, String tip, final Vector<ItemType> items)
	{
		listObjectName = name;
		this.tip = tip;
		
		list = new PropertiesEditItemList<ItemType>(items);
		//list.setMinimumSize(new Dimension(300,50));
		//list.setPreferredSize(new Dimension(400,50));
		
		add(list, cons);

	}
	
	public String getName() { return listObjectName; }
	public String getTip() { return tip; }
	
	private PropertiesEditItemList<ItemType> list;

}
