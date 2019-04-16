package ru.dz.shipMaster.ui.config.item;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import ru.dz.shipMaster.config.IConfigListItem;
import ru.dz.shipMaster.config.items.CliWindowPanelConstraint;
import ru.dz.shipMaster.config.items.CliWindowStructure;
import ru.dz.shipMaster.ui.config.PropertiesEditItemList;

/**
 * Editor for a window breakdown structure. Lets user to create his own window
 * partitioning.
 * @author dz
 *
 */

public class CieWindowStructure extends SideFrameConfigItemEditor {

	private final CliWindowStructure bean;

	private PropertiesEditItemList<CliWindowPanelConstraint> clist;
	private JButton viewButton = new JButton();
	private JTextField nameField = new JTextField();

	private JButton moveUpButton = new JButton();
	private JButton moveDownButton = new JButton();

	private Vector<CliWindowPanelConstraint> constraints;


	@SuppressWarnings("serial")
	public CieWindowStructure(CliWindowStructure bean)
	{
		super(new Dimension(320,200));
		this.bean = bean;		

		discardSettings(); // NB! Before using constraints
		clist = new CieWSList<CliWindowPanelConstraint>(constraints);

		viewButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) { showDemo(); }});
		viewButton.setText("Show");

		moveUpButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) { moveUp(); }});
		moveUpButton.setText("Move up    ");

		moveDownButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) { moveDown(); }});
		moveDownButton.setText("Move down");

		panel.add(new JLabel("name:"),consL);
		panel.add(nameField, consR);
		nameField.setColumns(20);		

		panel.add(new JLabel("Parts:"),consL);
		panel.add(clist,consR);

		panel.add(new JLabel(""),consL);
		panel.add(moveUpButton,consR);

		panel.add(new JLabel(""),consL);
		panel.add(moveDownButton,consR);

		panel.add(new JLabel("Preview:"),consL);
		panel.add(viewButton, consR);

		setupSideFrame("Windows structure preview");
	}

	@Override
	protected void informContainer() {		bean.informContainers(); }

	protected void moveDown() {
		int selectedIndex = clist.getSelectedIndex();
		if(selectedIndex < 0)
			return;
		if(selectedIndex >= constraints.size()-1) // We can not move lower one down
			return;

		CliWindowPanelConstraint temp = constraints.get(selectedIndex);
		constraints.set(selectedIndex, constraints.get(selectedIndex+1));
		constraints.set(selectedIndex+1, temp );

		clist.setItems(constraints);
		clist.setSelectedIndex(selectedIndex+1);

		showDemo();
	}

	protected void moveUp() {
		int selectedIndex = clist.getSelectedIndex();
		if(selectedIndex < 1) // We can not move upper one up
			return;

		CliWindowPanelConstraint temp = constraints.get(selectedIndex);
		constraints.set(selectedIndex, constraints.get(selectedIndex-1));
		constraints.set(selectedIndex-1, temp );


		clist.setItems(constraints);
		clist.setSelectedIndex(selectedIndex-1);

		showDemo();
	}

	/*private Color [] colors = {
			Color.GREEN, Color.RED, Color.YELLOW, Color.ORANGE, Color.BLUE,
			Color.GRAY, Color.PINK, Color.MAGENTA, Color.LIGHT_GRAY, Color.DARK_GRAY
	};*/

	private int highlightIndex = 0;

	protected void showDemo() {
		JPanel demoPanel = new JPanel(new GridBagLayout());
		Vector<JComponent> components = new Vector<JComponent>();

		for( int i = 0; i < 10; i++ )
		{
			JLabel l = new JLabel(" ("+bean.getConstraints().get(i).getId()+") ");
			JPanel p = new JPanel(new BorderLayout());

			if(highlightIndex == i)
			{
				//l.setBackground(colors[i]);
				//p.setBackground(colors[i]);
				p.setBackground(Color.DARK_GRAY);
				l.setForeground(Color.WHITE);
			}
			else
			{
				p.setBackground(Color.WHITE);
				//l.setForeground(colors[i]);
				l.setForeground(Color.DARK_GRAY);
			}
			p.add(l, BorderLayout.CENTER);
			p.setBorder(new LineBorder(Color.BLACK,1));

			components.add(p);
		}

		applySettings(); // BUG!
		bean.applyConstraints(demoPanel, components);
		//bean.applyConstraints(demoPanel, null);
		setSFPanel(demoPanel );		
	}

	@Override
	public void applySettings() {
		//bean.setConstraints((Vector<CliWindowPanelConstraint>) constraints.clone());
		// BUG!
		bean.setConstraints(constraints);
		bean.setName(nameField.getText());
	}

	@Override
	public void discardSettings() {
		nameField.setText( bean.getName() );
		// BUG!
		//constraints = (Vector<CliWindowPanelConstraint>) bean.getConstraints().clone();
		constraints = bean.getConstraints();
	}

	@Override
	public String getName() { return bean.getName(); }

	@Override
	public String getTypeName() { return "Window structure"; }


	@SuppressWarnings("serial")
	class CieWSList<ItemType extends IConfigListItem> extends PropertiesEditItemList<ItemType> 
	{

		public CieWSList(Vector<ItemType> items) {
			super(items);
		}

		@Override
		protected void singleClick() {
			highlightCurrent();
		}
	}


	public void highlightCurrent() {
		highlightIndex  = clist.getSelectedIndex();
		showDemo();
	}	
}


