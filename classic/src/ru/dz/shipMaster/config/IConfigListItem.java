package ru.dz.shipMaster.config;

import ru.dz.shipMaster.ui.config.BasicItemContainer;

public interface IConfigListItem {

	/**
	 * Returns name of item instance.
	 * @return Instance name string.
	 */
	public abstract String getName(); // { return Messages.getString("ConfigListItem.UndefItem"); } //$NON-NLS-1$

	public abstract String toString();

	public abstract void displayPropertiesDialog();

	/**
	 * Destroy item: delete all connections to other objects.
	 */
	public abstract void destroy();

	/**
	 * Inform container about this item's value change.
	 */
    public void informContainers();
    public void addContainer(BasicItemContainer<? extends IConfigListItem> c);
    public void removeContainer(BasicItemContainer<? extends IConfigListItem> c);
	
}