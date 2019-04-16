package ru.dz.shipMaster.ui.config.panel;

import java.util.Vector;

import ru.dz.shipMaster.config.IConfigListItem;
import ru.dz.shipMaster.ui.config.ConfigPanel;
import ru.dz.shipMaster.ui.config.ConfigTree;
/**
 * This panel is a list of objects user can add, remove and which properties he can modify.
 * @author dz
 *
 * @param <ItemType> Class of item to be held in.
 */
public class ConfigPanelTree<ItemType extends IConfigListItem> extends ConfigPanel {

	/**
	 * Eclipse generated id.
	 */
	private static final long serialVersionUID = -6070158116760077107L;

	private final String listObjectName;
	private final String tip;
	@SuppressWarnings("unused")
	private final Class<ItemType> itemClass;

	public ConfigPanelTree(String name, String tip, Class<ItemType> itemClass, final Vector<ItemType> items, final Vector<ItemType> libItems)
	{
		listObjectName = name;
		this.tip = tip;
		this.itemClass = itemClass;
		
		list = new ConfigTree<ItemType>(itemClass,items,libItems);

		add(list, cons);

	}
	
	public String getName() { return listObjectName; }
	public String getTip() { return tip; }
	
	private ConfigTree<ItemType> list;
	
	//@Override	public void load(XMLDecoder decoder) { list.load(decoder); }

	//@Override	public void save(XMLEncoder encoder) { list.save(encoder); }

}
