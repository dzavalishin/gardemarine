package ru.dz.shipMaster.ui.config.item;

import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import ru.dz.shipMaster.config.ConfigurationFactory;
import ru.dz.shipMaster.config.items.CliGroup;
import ru.dz.shipMaster.config.items.CliRight;
import ru.dz.shipMaster.ui.config.ConfigItemEditor;
import ru.dz.shipMaster.ui.config.RefItemList;

public class CieGroup extends ConfigItemEditor {

	private JTextField loginField = new JTextField("Login");
	private JPasswordField passwordField = new JPasswordField("Password");
	private Vector<CliRight> rights = new Vector<CliRight>(); 
	private RefItemList<CliRight> rightsField;

	private final CliGroup bean;

	/**
	 * Create new editor for this bean. 
	 * @param bean Bean to edit.
	 */
	public CieGroup(CliGroup bean)
	{
		this.bean = bean;
		panel.add(new JLabel("Group:"),consL);
		panel.add(loginField,consR);
		loginField.setColumns(14);

		panel.add(new JLabel("Password:"),consL);
		panel.add(passwordField,consR);
		passwordField.setColumns(14);
		
		rightsField = new RefItemList<CliRight>(
				rights,
				ConfigurationFactory.getConfiguration().getRightsItems(),
				//ConfigurationFactory.getConstantState().getRightsItems(),
				"access right");

		panel.add(new JLabel("Rights:"),consL);
		panel.add(rightsField,consR);
		
		
		discardSettings();
	}

	@Override
	protected void informContainer() {		bean.informContainers(); }

	/**
	 * Returns name of item instance.
	 * @return Instance name string.
	 */
	@Override
	public String getName() { return bean.getName(); }

	/**
	 * Returns name of subclass's item type, like user, alarm station, etc
	 * @return constant name for each subclass
	 */
	@Override
	public String getTypeName() { return "Group"; }

	@Override
	public void applySettings() {
		
		//if(!ConfigurationFactory.getTransientState().assertRight(CliRight.groupsControl))			return;
		
		bean.setName( loginField.getText() );
		bean.setPassword( passwordField.getText() );
		bean.setRights(rights);
		setTitle();
	}

	@Override
	public void discardSettings() {
		passwordField.setText( bean.getPassword() );
		loginField.setText(bean.getName());
		rights = bean.getRights();
		rightsField.setItems(rights);
	}
	
}
