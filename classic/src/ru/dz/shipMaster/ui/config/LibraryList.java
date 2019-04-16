package ru.dz.shipMaster.ui.config;

import java.awt.GridBagLayout;
import java.util.Vector;

import ru.dz.shipMaster.config.IConfigListItem;

public class LibraryList<ItemType extends IConfigListItem> extends GeneralItemList<ItemType> {

	private static final long serialVersionUID = 1821587840552490896L;

	/**
	 * @param myc - class of stored object
	 * @param storageName - prefix of storage (file name, XML node, etc) for this lib
	 */
	
	public LibraryList(Class<ItemType> myc, String storageName, final Vector<ItemType> items) {
		super(new GridBagLayout(), myc,items);
	}

}
