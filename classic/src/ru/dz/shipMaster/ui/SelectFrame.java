package ru.dz.shipMaster.ui;

import java.awt.Frame;
import java.util.Collection;
import java.util.Vector;

import ru.dz.shipMaster.config.IConfigListItem;

public class SelectFrame<ItemType extends IConfigListItem> extends GeneralSelectFrame {

	/**
	 * UID 
	 */
	private static final long serialVersionUID = -3453161247105007479L;
	
	protected final Vector<ItemType> items;

	public SelectFrame(Collection<ItemType> items, Frame frame, String title) {
		super(frame,title,true);
		this.items = new Vector<ItemType>(items); // TODO no problem we make a copy?
		//list = new JList(this.items);
		list.setListData(this.items);
		//new JList()
        

 		
		this.pack();
	}

	protected ItemType result = null;
	protected void select() {
		int selectedIndex = list.getSelectedIndex();
		result = items.elementAt(selectedIndex);
		setVisible(false);
		dispose();
	}

	protected void selectNothing() {
		result = null;
		setVisible(false);
		dispose();
	}
	
	public ItemType getResult() {		return result;	}
	
	
	
}
