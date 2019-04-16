package ru.dz.shipMaster.ui.config;

import ru.dz.shipMaster.config.IConfigListItem;

public interface BasicItemContainer<ItemType extends IConfigListItem> {

	public abstract void addItem(ItemType i);

	/**
	 * Called from item itself after item was modofied in a way related
	 * to item's view or position in container.
	 * 
	 * @param configListItem item changed.
	 */
	public abstract void updateItem(IConfigListItem configListItem);

}