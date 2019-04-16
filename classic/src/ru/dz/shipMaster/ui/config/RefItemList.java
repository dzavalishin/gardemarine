package ru.dz.shipMaster.ui.config;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JList;
import javax.swing.JScrollPane;

import ru.dz.shipMaster.config.IConfigListItem;

/**
 * Panel of referenced (taken from some other list) items. Example is groups for user.
 * This interface does not let user to create or modify objects - just add and remove
 * to his list.
 * @author dz
 * @param <ItemType> Type of items.
 *
 */

@SuppressWarnings("serial")
public class RefItemList<ItemType extends IConfigListItem> extends BasicItemList<ItemType> {
    @SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(GeneralItemList.class.getName()); 

	/** to select added items from */
	private final Collection<ItemType> lib;
	private final String itemTypeName;
	
	private ItemType showSelectionDialog(String title)
	{
		return super.showSelectionDialog(title, lib);
	}
	
	{
		buttonConstraints.gridx = GridBagConstraints.RELATIVE;
		buttonConstraints.gridy = 0;
		buttonConstraints.weightx = 1;
		buttonConstraints.weighty = 1;
		//c.fill = GridBagConstraints.BOTH;		
	}


	
	
	public void setItems(Vector<ItemType> items) {
		this.items = items;
		reloadItems(); 
	}



	public RefItemList(final Vector<ItemType> items, final Collection<ItemType> lib, final String itemTypeName)
	{
		this.items = items;
		this.lib = lib;
		this.itemTypeName = itemTypeName;

		list = new JList(items);
		
		addMyButton("+", "Add new "+itemTypeName, new AbstractAction() {
			public void actionPerformed(ActionEvent e) {				
				ItemType i = showSelectionDialog("Select "+itemTypeName+" to add");
				if(i != null)
					addItem(i);
			}} );
		

		addMyButton("-", "Remove selected "+itemTypeName, new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				int selectedIndex = list.getSelectedIndex();
				if(selectedIndex < 0)
					return;
				ItemType i = items.get(selectedIndex);
				if(i == null)
					return;
				
				if(!confirmDelete("Delete "+i.getName() + "?"))
					return;				
				
				items.remove(selectedIndex);
				list.clearSelection();
				list.repaint(100);
				
			}});
		
		{
			GridBagConstraints outerConstraints = new GridBagConstraints();
			outerConstraints.gridx = 0;
			outerConstraints.gridy = GridBagConstraints.RELATIVE;
			outerConstraints.weightx = 1;
			outerConstraints.weighty = 1;
			outerConstraints.fill = GridBagConstraints.BOTH;
			outerConstraints.anchor = GridBagConstraints.NORTH;
			outerConstraints.ipadx = 10;
			outerConstraints.ipady = 10;
		
			list.setMinimumSize(new Dimension(60,40));
			
			JScrollPane scrollPane = new JScrollPane(list);

			add(scrollPane,outerConstraints);		
			add(buttonPanel,outerConstraints);
		}
	}



	/**
	 * @return the itemTypeName
	 */
	public String getItemTypeName() {
		return itemTypeName;
	}



	@Override
	protected void openEditDialog() {
		// Ignore.		
	}


}
