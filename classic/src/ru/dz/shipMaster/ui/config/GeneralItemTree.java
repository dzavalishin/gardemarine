package ru.dz.shipMaster.ui.config;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultTreeModel;

import ru.dz.shipMaster.config.ConfigListItem;
import ru.dz.shipMaster.config.ConfigurationFactory;
import ru.dz.shipMaster.config.IConfigListItem;
import ru.dz.shipMaster.config.items.CliAlarm;


/**
 * This item list has "refresh", "new", "edit", "delete" and move up/down buttons.
 * @author dz
 *
 * @param <ItemType>
 */
@SuppressWarnings("serial")
public class GeneralItemTree<ItemType extends IConfigListItem> extends BasicItemTree<ItemType> implements Serializable {

    private static final Logger log = Logger.getLogger(GeneralItemTree.class.getName()); 

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

	
	
	public GeneralItemTree(LayoutManager layout, Class<ItemType> myc, final Vector<ItemType> items) {
		//super(layout);
		this.myClass = myc;
		this.items = items;
		
		ItemTreeNode<ItemType> top =
	        new ItemTreeNode<ItemType>(".");

		
		model = new DefaultTreeModel(top);

		/*for( ItemType item : items )
		{
			//top.add(new ItemTreeNode(item.getName()));
			addTreeNode(item, item.getName());
		}*/
		
		//list = new JList(items);
		tree = new JTree(model);
		tree.setBorder(new EmptyBorder(4,4,4,4));

		reloadItems();
		
		
		addMyButton("Refresh", "Reload items to show hidden changes, if any (F5)", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {				
				reloadItems();
			}}, KeyStroke.getKeyStroke(KeyEvent.VK_F5,0)  );

		addMyButton("New", "Create new item (Ins)", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {				
				ItemType i = makeOne(myClass);
				i.displayPropertiesDialog();
				addItem(i);
			}}, KeyStroke.getKeyStroke("INSERT"), "ADD", ConfigurationFactory.getVisualConfiguration().getAddIcon() );
		
		addMyButton("Edit", "Edit selected item", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				openEditDialog();
			}});

		addMyButton("Remove", "Remove selected item (Del)", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {

				ItemType i = getSelectedItem();
				if(i == null)
					return;		
			
				final String msg = "Delete "+i.getName() + "?";
				if(!confirmDelete(msg))
					return;
				
				i.removeContainer(GeneralItemTree.this);
				items.remove(i);
				reloadItems();
				tree.clearSelection();
				tree.repaint(100);				
			}}, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,0), "DELETE", ConfigurationFactory.getVisualConfiguration().getDeleteIcon() );

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
		
			tree.setMinimumSize(new Dimension(100,200));
			
			JScrollPane scrollPane = new JScrollPane(tree);
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


		
		tree.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
				if( e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1 )
					openEditDialog();
			}

			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			});
		
		//this.revalidate();
	}


	protected void openEditDialog() {
		ItemType uo = getSelectedItem();
		if(uo != null)
			uo.displayPropertiesDialog();
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


	public static void main(String[] args) {
		try { 
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			//UIManager.setLookAndFeel("org.jvnet.substance.SubstanceLookAndFeel");
			} 
		catch (Exception e) {/* ignore inability to set l&f */}
		
		ConfigListItem.setRightsDisabed();
		
		JFrame f = new JFrame("Tree test");
		Vector<CliAlarm> alarms = new Vector<CliAlarm>();
		GeneralItemTree<CliAlarm> tree = new GeneralItemTree<CliAlarm>(
				new GridLayout(), CliAlarm.class, alarms
				);
		
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		tree.addItem(new CliAlarm("a.b.c"));
		tree.addItem(new CliAlarm("a.b.d"));
		tree.addItem(new CliAlarm("a.d.e"));
		tree.addItem(new CliAlarm("a.g.f"));
		tree.addItem(new CliAlarm("b.b.c"));
		tree.addItem(new CliAlarm("b.b.a"));
		
		f.add(tree);
		f.setMinimumSize(new Dimension(300,400));
		f.pack();
		f.setVisible(true);
	}

	
}
