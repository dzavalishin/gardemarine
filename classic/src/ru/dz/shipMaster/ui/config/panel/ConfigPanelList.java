package ru.dz.shipMaster.ui.config.panel;

import java.util.Vector;

import ru.dz.shipMaster.config.IConfigListItem;
import ru.dz.shipMaster.ui.config.ConfigList;
import ru.dz.shipMaster.ui.config.ConfigPanel;
/**
 * This panel is a list of objects user can add, remove and which properties he can modify.
 * @author dz
 *
 * @param <ItemType> Class of item to be held in.
 */
public class ConfigPanelList<ItemType extends IConfigListItem> extends ConfigPanel {

	/**
	 * Eclipse generated id.
	 */
	private static final long serialVersionUID = -6070158116760077107L;

	private final String listObjectName;
	private final String tip;
	@SuppressWarnings("unused")
	private final Class<ItemType> itemClass;

	@SuppressWarnings("serial")
	public ConfigPanelList(String name, String tip, Class<ItemType> itemClass, final Vector<ItemType> items, final Vector<ItemType> libItems)
	{
		listObjectName = name;
		this.tip = tip;
		this.itemClass = itemClass;
		
		list = new ConfigList<ItemType>(itemClass,items,libItems) {
			@Override
			protected void informAboutSelection(int selectedIndex, ItemType itemType) {
				//System.out.println("ConfigPanelList.ConfigPanelList(...).new ConfigList<ItemType>() {...}.informAboutSelection()"+selectedIndex);
				informAboutListSelection(selectedIndex, itemType);
			};
		};

		add(list, cons);
	}
	
	public String getName() { return listObjectName; }
	public String getTip() { return tip; }
	
	private ConfigList<ItemType> list;

	/**
	 * Supposed to be overriden to catch item selection event.
	 * @param selectedIndex - number of item selected
	 * @param itemType - item itself
	 */
	
	protected void informAboutListSelection(int selectedIndex, Object itemType) 
	{
		//System.out.println("WARNING! ConfigPanelList.informAboutListSelection()"+selectedIndex);
	};

}
