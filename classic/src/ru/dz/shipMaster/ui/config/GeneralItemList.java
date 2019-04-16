package ru.dz.shipMaster.ui.config;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ru.dz.shipMaster.config.ConfigurationFactory;
import ru.dz.shipMaster.config.IConfigListItem;


/**
 * This item list has "refresh", "new", "edit", "delete" and move up/down buttons.
 * @author dz
 *
 * @param <ItemType>
 */
@SuppressWarnings("serial")
public class GeneralItemList<ItemType extends IConfigListItem> extends BasicItemList<ItemType> implements Serializable {

	private static final Logger log = Logger.getLogger(GeneralItemList.class.getName()); 

	protected Class<ItemType> myClass;

	//private boolean selectSubclass = false;


	public ItemType showSelectionDialog(String title)
	{
		return super.showSelectionDialog(title, items);
	}

	{
		buttonConstraints.gridx = 0;
		buttonConstraints.gridy = GridBagConstraints.RELATIVE;
		buttonConstraints.weightx = 1;
		buttonConstraints.weighty = 1;
	}



	public GeneralItemList(LayoutManager layout, Class<ItemType> myc, final Vector<ItemType> items) {
		//super(layout);
		this.myClass = myc;
		this.items = items;
		
		for( ItemType i : items)
			i.addContainer(this);
		
		//list = new JList(items);
		list.setBorder(new EmptyBorder(4,4,4,4));

		reloadItems();
		
		
		
		{
			GridBagConstraints outerConstraints = new GridBagConstraints();
			outerConstraints.gridx = GridBagConstraints.RELATIVE;
			outerConstraints.gridy = 0;
			outerConstraints.weightx = 1;
			outerConstraints.weighty = 1;
			outerConstraints.fill = GridBagConstraints.BOTH;
			outerConstraints.anchor = GridBagConstraints.NORTHWEST;
			outerConstraints.ipadx = 10;
			outerConstraints.ipady = 10;

			list.setMinimumSize(new Dimension(100,200));

			JScrollPane scrollPane = new JScrollPane(list);
			//add(list);
			add(scrollPane,outerConstraints);

			//JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			//buttonPanel.setPreferredSize(new Dimension(buttonsSize.width+10,100));
			outerConstraints.anchor = GridBagConstraints.NORTHEAST;
			outerConstraints.weightx = 0;
			//buttonPanel.setBorder(new LineBorder(Color.GREEN));
			add(buttonPanel,outerConstraints);
			//setBorder(new LineBorder(Color.BLACK));
		}
		
		
		
		
		
		addMyButton("Refresh", "Reload items to show hidden changes, if any (F5)", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {				
				reloadItems();
			}}, KeyStroke.getKeyStroke(KeyEvent.VK_F5,0) );

		addMyButton("New", "Create new item (Ins)", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {				
				ItemType i = makeOne(myClass);
				i.displayPropertiesDialog();
				addItem(i);
			}}, KeyStroke.getKeyStroke("INSERT"), "ADD", ConfigurationFactory.getVisualConfiguration().getAddIcon() );
//			}}, KeyStroke.getKeyStroke(KeyEvent.VK_INSERT,InputEvent.ALT_DOWN_MASK) );

		addMyButton("Edit", "Edit selected item (Enter)", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				openEditDialog();
			}});

		addMyButton("Remove", "Remove selected item (Del)", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				/*
				int selectedIndex = list.getSelectedIndex();
				if(selectedIndex < 0)
					return;
				ItemType i = items.get(selectedIndex);
				*/
				ItemType i = getSelectedItem();
				if(i != null)
				{	

					final String msg = "Delete "+i.getName() + "?";
					if(!confirmDelete(msg))
						return;
					i.removeContainer(GeneralItemList.this);
					i.destroy();
				}
				items.remove(i);
				list.clearSelection();
				list.repaint(100);				
			}}, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,0), "DELETE", ConfigurationFactory.getVisualConfiguration().getDeleteIcon() );

		addMyButton("Move to top", "Move selected item to the beginning of list (Ctrl-Home)", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				int selectedIndex = list.getSelectedIndex();

				if(selectedIndex == 0)
					return;

				ItemType i = items.get(selectedIndex);
				ItemType ifirst = items.get(0);

				items.set(selectedIndex, ifirst);
				items.set(0, i);
				list.repaint(100);
				list.setSelectedIndex(0);
			}}, KeyStroke.getKeyStroke(KeyEvent.VK_HOME,InputEvent.CTRL_DOWN_MASK), "TOP", ConfigurationFactory.getVisualConfiguration().getMoveToTopIcon() );
		
		addMyButton("Move up", "Move selected item up one step (Ctrl-Up)", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				int selectedIndex = list.getSelectedIndex();
				if(selectedIndex < 1)
					return;
				ItemType i = items.get(selectedIndex);
				ItemType iup = items.get(selectedIndex-1);

				items.set(selectedIndex, iup);
				items.set(selectedIndex-1, i);

				list.setSelectedIndex(selectedIndex-1);
				list.repaint(100);				
			}}, KeyStroke.getKeyStroke(KeyEvent.VK_UP,InputEvent.CTRL_DOWN_MASK), "UP", ConfigurationFactory.getVisualConfiguration().getMoveUpIcon());

		addMyButton("Move down", "Move selected item down one step (Ctrl-Down)", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				int selectedIndex = list.getSelectedIndex();
				if(selectedIndex >= items.size()-1)
					return;
				ItemType i = items.get(selectedIndex);
				ItemType idn = items.get(selectedIndex+1);

				items.set(selectedIndex, idn);
				items.set(selectedIndex+1, i);

				list.setSelectedIndex(selectedIndex+1);
				list.repaint(100);				
			}}, KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,InputEvent.CTRL_DOWN_MASK), "DOWN", ConfigurationFactory.getVisualConfiguration().getMoveDownIcon());

		addMyButton("Move to end", "Move selected item to the end of list (Ctrl-End)", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				int selectedIndex = list.getSelectedIndex();
				int last = items.size()-1;
				if(selectedIndex >= items.size()-1 || selectedIndex == last)
					return;

				ItemType i = items.get(selectedIndex);
				ItemType ilast = items.get(last);

				items.set(selectedIndex, ilast);
				items.set(last, i);
				list.setSelectedIndex(last);
				list.repaint(100);				
			}}, KeyStroke.getKeyStroke(KeyEvent.VK_END,InputEvent.CTRL_DOWN_MASK), "END", ConfigurationFactory.getVisualConfiguration().getMoveToBottomIcon());




		list.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
				
				if( e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1 )
					openEditDialog();
			}

			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {
				/*
				int selectedIndex = list.getSelectedIndex();
				//System.out.println("GeneralItemList.GeneralItemList(...).new MouseListener() {...}.mouseClicked()"+selectedIndex);
				informAboutSelection(selectedIndex, items.get(selectedIndex));
				*/				
			}
			public void mouseReleased(MouseEvent e) {}
		});

		ListSelectionModel listSelectionModel = list.getSelectionModel();
		listSelectionModel.addListSelectionListener(
				new ListSelectionListener() {

					@Override
					public void valueChanged(ListSelectionEvent arg0) {
						int selectedIndex = list.getSelectedIndex();
						//System.out.println("GeneralItemList.GeneralItemList(...).new MouseListener() {...}.mouseClicked()"+selectedIndex);
						if(selectedIndex >= 0)
							informAboutSelection(selectedIndex, items.get(selectedIndex));				
					}
					
				}
		);

		
		//this.revalidate();
	}


	@Override
	protected void openEditDialog() {
		int selectedIndex = list.getSelectedIndex();
		if(selectedIndex < 0)
			return;
		ItemType i = items.get(selectedIndex);
		if(i == null)
			return;
		i.displayPropertiesDialog();
	}

	protected ItemType makeOne(Class<ItemType> iclass)
	{
		try {
			return iclass.newInstance();
		} catch (InstantiationException e) {
			log.log(Level.SEVERE,"Can't create object",e);
		} catch (IllegalAccessException e) {
			log.log(Level.SEVERE,"Can't create object",e);
		}
		return null;
	}


	/**
	 * Supposed to be overriden to catch item selection event.
	 * @param selectedIndex - number of item selected
	 * @param itemType - item itself
	 */
	protected void informAboutSelection(int selectedIndex, ItemType itemType) {
		//System.out.println("GeneralItemList.informAboutSelection()"+selectedIndex);
	}


}
