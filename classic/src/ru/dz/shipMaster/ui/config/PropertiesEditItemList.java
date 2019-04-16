package ru.dz.shipMaster.ui.config;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.JList;
import javax.swing.JScrollPane;

import ru.dz.shipMaster.config.IConfigListItem;

/**
 * This panel is used to edit properties of items which you can not delete or add.
 * @author dz
 *
 */

@SuppressWarnings("serial")
public class PropertiesEditItemList<ItemType extends IConfigListItem> extends BasicItemList<ItemType> {
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(PropertiesEditItemList.class.getName()); 

	public PropertiesEditItemList(final Vector<ItemType> items) {
		this.items = items;
		list = new JList(items);

		{
			GridBagConstraints outerConstraints = new GridBagConstraints();
			outerConstraints.gridx = GridBagConstraints.RELATIVE;
			outerConstraints.gridy = 0;
			outerConstraints.weightx = 1;
			outerConstraints.weighty = 1;
			outerConstraints.fill = GridBagConstraints.BOTH;
			outerConstraints.anchor = GridBagConstraints.NORTH;
			outerConstraints.ipadx = 10;
			outerConstraints.ipady = 10;

			list.setMinimumSize(new Dimension(100,200));

			JScrollPane scrollPane = new JScrollPane(list);
			add(scrollPane,outerConstraints);		

			list.addMouseListener(new MouseListener() {

				public void mouseClicked(MouseEvent e) {
					if( e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1 )
						openEditDialog();
					else if( e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON1 )
						singleClick();
				}

				public void mouseEntered(MouseEvent e) {}
				public void mouseExited(MouseEvent e) {}
				public void mousePressed(MouseEvent e) {}
				public void mouseReleased(MouseEvent e) {}
			});

		}

	}

	/**
	 * Override to catch a click event
	 */
	protected void singleClick() {
		// Empty		
	}

	public void setItems(final Vector<ItemType> items) {
		list.setListData(items);
	}	

	protected void openEditDialog() {
		if(true)
		{
			Object selectedValue = list.getSelectedValue();
			ItemType i = (ItemType)selectedValue;
			if(i == null)
				return;
			i.displayPropertiesDialog();
		}
		/*else
		{
			int selectedIndex = list.getSelectedIndex();
			if(selectedIndex < 0)
				return;
			ItemType i = items.get(selectedIndex);
			if(i == null)
				return;
			i.displayPropertiesDialog();
		}*/
	}

	/**
	 * @return Current selected index or -1 if nothing selected.
	 */
	public int getSelectedIndex() { return list.getSelectedIndex(); }

	public void setSelectedIndex(int index) { list.setSelectedIndex(index); }

}
