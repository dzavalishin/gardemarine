package ru.dz.shipMaster.ui.config;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import ru.dz.shipMaster.config.ConfigurationFactory;
import ru.dz.shipMaster.config.IConfigListItem;
import ru.dz.shipMaster.ui.SelectFrame;

/**
 * A panel which shows list of some items and lets manipulate them.
 * @author dz
 * @param <ItemType> Type of item.
 */
public abstract class BasicItemList<ItemType extends IConfigListItem> extends JPanel implements BasicItemContainer<ItemType> {

	/**
	 * UID
	 */
	private static final long serialVersionUID = 4741365769989146202L;

	protected static final Dimension buttonsSize = new Dimension(110,26);

	/** my list of referenced ones */
	protected Vector<ItemType> items;
	protected JList list = new JList();

	@SuppressWarnings("serial")
	public BasicItemList()
	{
		super(new GridBagLayout());

		{
			final String name = "user-hit-enter";
			list.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), name);
			list.getActionMap().put(name, new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					//System.out.println(".actionPerformed()\n");
					openEditDialog();
				}
			});		
		}

	}

	/* (non-Javadoc)
	 * @see ru.dz.shipMaster.ui.config.BasicItemContainer#addItem(ItemType)
	 */
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

	public void reloadItems() {
		list.setListData(items);
		if(items.size() > 0) list.setSelectedIndex(items.size()-1);
		list.repaint(100);
	}

	protected ItemType getSelectedItem() {
		int selectedIndex = list.getSelectedIndex();
		return (selectedIndex < 0) ? null : items.get(selectedIndex);
	}





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
			//JRootPane rootPane = getRootPane();
			//JComponent rootPane = (JComponent)getParent();
			//BasicItemList<ItemType> rootPane = this;
			JList rootPane = list;

			//InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
			//inputMap.put(key, actionName );
			
			rootPane.getInputMap().put(key, actionName );
			rootPane.getActionMap().put(actionName, action );

			//toolTip += " ("+ KeyStroke.getk(key.getKeyCode()) +")";
		}

		b.setToolTipText(toolTip);

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

	@Override
	protected void finalize() throws Throwable {
		for(ItemType i : items )
			i.removeContainer(this);
		super.finalize();
	}

	protected abstract void openEditDialog();

}
