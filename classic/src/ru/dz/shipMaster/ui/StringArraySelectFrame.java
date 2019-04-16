package ru.dz.shipMaster.ui;

import java.awt.Frame;


public class StringArraySelectFrame extends GeneralSelectFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4713090304991376174L;
	private final String [] items;

	public StringArraySelectFrame(String[] items, Frame frame, String title) {
		super(frame,title,true);
		this.items = items.clone(); // TODO no problem we make a copy?
		//list = new JList(this.items);
		
		list.setListData(this.items);
		
		this.pack();
	}

	private String result = null;
	protected void select() {
		int selectedIndex = list.getSelectedIndex();
		result = items[selectedIndex];
		setVisible(false);
		dispose();
	}

	protected void selectNothing() {
		result = null;
		setVisible(false);
		dispose();
	}
	
	public String getResult() {		return result;	}
	
	
	
}
