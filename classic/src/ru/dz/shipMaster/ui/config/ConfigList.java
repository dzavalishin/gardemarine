package ru.dz.shipMaster.ui.config;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JFrame;

import ru.dz.shipMaster.config.ConfigurationFactory;
import ru.dz.shipMaster.config.IConfigListItem;


public class ConfigList<ItemType extends IConfigListItem> extends GeneralItemList<ItemType> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6403339788954161827L;
	private LibraryList<ItemType> library;
	private JFrame libFrame = new JFrame();
	
	public ConfigList(Class<ItemType> myc, final Vector<ItemType> items, final Vector<ItemType> libItems )
	{
		super(new GridBagLayout(), myc, items);

		library = new LibraryList<ItemType>(myc,myc.getName()+"_MainLib",libItems);
		
		
		libFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        
		
		libFrame.setPreferredSize(new Dimension(400, 300));
        libFrame.setMinimumSize(new Dimension(300, 250));
        libFrame.setMaximumSize(new Dimension(800, 600));        
		
        libFrame.setLocationRelativeTo(this);
		libFrame.setTitle("Библиотека");
        
        
        //getContentPane().add(tabbedPane, BorderLayout.CENTER);
		libFrame.getContentPane().add(library, BorderLayout.CENTER);
		
		
		addMyButton("From lib", "Add item from library", new AbstractAction() {
			private static final long serialVersionUID = 5347851468551551944L;

			public void actionPerformed(ActionEvent e) {
				ItemType item = library.showSelectionDialog("Добавление из библиотеки");
				if(item != null)
				{
					addItem(item);
				}
			}}, null, null, ConfigurationFactory.getVisualConfiguration().getMoveLeftIcon() );
		
		addMyButton("To lib", "Store item to library", new AbstractAction() {
			private static final long serialVersionUID = 8044535014458845408L;

			public void actionPerformed(ActionEvent e) {
				ItemType i = getSelectedItem();
				if(i == null)
					return;
				//i.displayPropertiesDialog();
				library.addItem(i);
				libFrame.setVisible(true);
			}}, null, null, ConfigurationFactory.getVisualConfiguration().getMoveRightIcon());
		
		addMyButton("Manage lib", "Open library", new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 872630492932985736L;

			public void actionPerformed(ActionEvent e) {
				libFrame.setVisible(true);
			}});

		libFrame.pack();
	}


	
}
