package ru.dz.shipMaster.ui.config;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.util.Collection;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import ru.dz.shipMaster.config.ConfigurationFactory;
import ru.dz.shipMaster.config.IConfigListItem;
import ru.dz.shipMaster.ui.SelectFrame;

/**
 * A panel which shows tree of some items and lets manipulate them.
 * @author dz
 * @param <ItemType> Type of item.
 */
public abstract class BasicItemTree<ItemType extends IConfigListItem> extends JPanel implements BasicItemContainer<ItemType> {

	/**
	 * UID
	 */
	private static final long serialVersionUID = 8567635612703611885L;

	protected static final Dimension buttonsSize = new Dimension(100,26);

	/** my list of referenced ones */
	protected Vector<ItemType> items;
	protected JTree tree;
	protected DefaultTreeModel model;


	public BasicItemTree()
	{
		super(new GridBagLayout());

	}

	public void addItem(ItemType i) { 
		items.add(i);
		i.addContainer(this);
		reloadItems(); 
	}

	@Override
	public void updateItem(IConfigListItem configListItem) {
		// TODO it can be done better
		reloadItems();		
	}

	protected void reloadItems() {
		/*TreeModel m = tree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)m.getRoot();

		root.removeAllChildren();
		 */

		ItemTreeNode<ItemType> root = new ItemTreeNode<ItemType>("/");
		TreeModel m = new DefaultTreeModel(root);
		tree.setModel(m);

		//System.out.println("-----");

		for( ItemType item : items )
		{
			String fullName = item.getName();
			String[] split = fullName.split("\\.");

			addRecursive(root, split, 0, false, item);
			//System.out.println(" .");
		}

		//tree.setRootVisible(false);

		tree.expandPath(new TreePath(root));
		

		//list.setSelectedIndex(items.size()-1);

		//tree.setModel(m);
		tree.repaint(100);
	}

	private static int dupNum = 0;
	private void addRecursive(ItemTreeNode<ItemType> container, String[] names,
			int namepos, boolean added, ItemType item) {

		if(namepos >= names.length)
		{
			if(!added)
				//VisualHelpers.showMessageDialog(this, "Dup tree path ignored: "+names);
			{
				while(true)
				{
					String addName = "pleaseRenameMe.duplicate."+dupNum++;
					
					ItemTreeNode<ItemType> newChild = new ItemTreeNode<ItemType>(item,addName);
					container.add(newChild);
					break;
				}
			}
			return;
		}

		String stepName = names[namepos];

		int cc = container.getChildCount();

		for( int i = 0; i < cc; i++ )
		{
			ItemTreeNode<ItemType> child = (ItemTreeNode<ItemType>) container.getChildAt(i);

			//tree.expandPath(new TreePath(child.getPath()));

			if(child.getName().equals(stepName))
			{
				//System.out.print(" found "+stepName);
				addRecursive(child, names, namepos+1, added, item);
				return;
			}
		}

		ItemType ni = null;

		// We are adding a leaf
		if(namepos == names.length-1)
			ni = item;

		//System.out.print(" adding "+stepName);
		ItemTreeNode<ItemType> newChild = new ItemTreeNode<ItemType>(ni,stepName);
		container.add(newChild);

		//if( ni != null )	tree.expandPath(new TreePath(newChild.getPath()));


		addRecursive(newChild, names, namepos+1, true, item);

	}

	/*private void addToNode(String[] split, int pos, DefaultMutableTreeNode node) {
		DefaultMutableTreeNode child = new DefaultMutableTreeNode(split[pos]);
		node.add(child);
		if( pos+1 < split.length )
			addToNode(split, pos+1, child);
	}*/












	protected GridBagConstraints buttonConstraints = new GridBagConstraints();
	{
		buttonConstraints.weightx = buttonConstraints.weighty = 1;
		buttonConstraints.anchor = GridBagConstraints.NORTHEAST;
		buttonConstraints.gridx = 0;
		buttonConstraints.gridy = GridBagConstraints.RELATIVE;
		buttonConstraints.fill = GridBagConstraints.NONE;
		buttonConstraints.ipadx = 8;

	}
	protected JPanel buttonPanel = new JPanel(new GridBagLayout());

	protected void addMyButton( String label, String toolTip, AbstractAction action )
	{
		addMyButton( label, toolTip, action, null, null, null );
	}

	protected void addMyButton( String label, String toolTip, AbstractAction action, KeyStroke key )
	{
		addMyButton( label, toolTip, action, key, label, null );
	}

	protected void addMyButton( String label, String toolTip, AbstractAction action, KeyStroke key, String actionName, Image ii )
	{
		JButton b = new JButton(label);

		b.setAction(action);
		b.setMinimumSize(buttonsSize);
		b.setPreferredSize(buttonsSize);
		b.setMaximumSize(buttonsSize);

		b.setHorizontalAlignment(SwingConstants.LEFT);

		b.setToolTipText(toolTip);
		b.setText(label);

		if(ii == null)
			ii = ConfigurationFactory.getVisualConfiguration().getEmptyIcon();
		
		if(ii != null)
		{
			Icon icon = new ImageIcon(ii);
			b.setIcon(icon);
		}
		
		buttonPanel.add(b, buttonConstraints);
		
		if(key != null && actionName != null )
		{
			JTree rootPane = tree;
			rootPane.getInputMap().put(key, actionName );
			rootPane.getActionMap().put(actionName, action );

			//toolTip += " ("+ KeyStroke.getk(key.getKeyCode()) +")";
		}

	}

	protected ItemType getSelectedItem() {
		TreePath path = tree.getSelectionPath();
		if(path == null)
			return null;
		
		ItemTreeNode<ItemType> last = (ItemTreeNode<ItemType>) path.getLastPathComponent();
		
		return (ItemType) last.getUserObject();
	}

	protected ItemType showSelectionDialog(String title, Collection<ItemType> lib)
	{
		Frame frame= JOptionPane.getFrameForComponent(this);
		SelectFrame<ItemType> sf = new SelectFrame<ItemType>(lib, frame, title);
		sf.setVisible(true);
		return sf.getResult();
	}

	protected boolean confirmDelete(String msg) {
		int i = JOptionPane.showConfirmDialog(this, msg, "Confirm delete", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		return i == 0;
	}

	/*
	protected ItemTreeNode<ItemType> getPathNode(LinkedList<String> path, boolean create)
	{
		return getPathNode((ItemTreeNode<ItemType>)model.getRoot(), path, create);
	}

	protected ItemTreeNode<ItemType> getPathNode(ItemTreeNode<ItemType> from, LinkedList<String> path, boolean create)
	{

		for( ItemTreeNode<ItemType> n = (ItemTreeNode<ItemType>)from.getFirstChild(); n != null; n = (ItemTreeNode<ItemType>)n.getNextSibling() )
		{
			if( !n.toString().equalsIgnoreCase(path.getFirst()) )
				continue;

			return getPathNode(n, (LinkedList<String>) path.subList(1, path.size()-1), create);					
		}


		ItemTreeNode<ItemType> newChild = new ItemTreeNode<ItemType>(path.getFirst());
		from.add(newChild);

		return getPathNode(newChild, (LinkedList<String>) path.subList(1, path.size()-1), create);
	}


	protected void addTreeNode(ItemType node, String ipath)
	{
		String[] split = ipath.split("\\.");



		LinkedList<String> list = new LinkedList<String>();

		for( int i = 0; i < split.length-1; i++)
			list.add(split[i]);

		ItemTreeNode<ItemType> dirNode = getPathNode((LinkedList<String>) list.subList(0, list.size()-2), true);		


		ItemTreeNode<ItemType> ni = new ItemTreeNode<ItemType>( node, list.getLast() );

		dirNode.add(ni);

	}
	 */
}


class ItemTreeNode<ItemType extends IConfigListItem> extends DefaultMutableTreeNode
{
	/**
	 * UID 
	 */
	private static final long serialVersionUID = 1965721740970075643L;

	private final String name;


	public String getName() {
		return name;
	}


	ItemTreeNode(String name)
	{
		this.name = name;

	}


	public ItemTreeNode(ItemType item, String name) {
		this.name = name;
		setUserObject(item);
	}


	@Override
	public String toString() { return name;	}



}
