package ru.dz.shipMaster.ui.config.item;

import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import ru.dz.shipMaster.config.ConfigurationFactory;
import ru.dz.shipMaster.config.items.CliGroup;
import ru.dz.shipMaster.config.items.CliUser;
import ru.dz.shipMaster.ui.config.ConfigItemEditor;
import ru.dz.shipMaster.ui.config.RefItemList;

public class CieUser extends ConfigItemEditor {

	private JTextField loginField = new JTextField("Login");
	private JPasswordField passwordField = new JPasswordField("Password");
	private Vector<CliGroup> groups = new Vector<CliGroup>(); 
	private RefItemList<CliGroup> groupsField;

	private final CliUser bean;
	
	/**
	 * Create new editor for this bean. 
	 * @param bean Bean to edit.
	 */
	public CieUser(CliUser bean)
	{
		this.bean = bean;
		//panel.add(new JLabel("Edit user properties:"));
		panel.add(new JLabel("Login:"),consL);
		panel.add(loginField,consR);
		//loginField.setText(login);
		loginField.setColumns(14);
		
		panel.add(new JLabel("Password:"),consL);
		panel.add(passwordField,consR);
		passwordField.setColumns(14);
		
		groupsField = new RefItemList<CliGroup>(
				groups,
				ConfigurationFactory.getConfiguration().getGroupItems(),
				"group");

		panel.add(new JLabel("Groups:"),consL);
		panel.add(groupsField,consR);
		
		discardSettings();
	}

	/**
	 * Returns name of item instance.
	 * @return Instance name string.
	 */
	@Override
	public String getName() { return bean.getLogin(); }

	/**
	 * Returns name of subclass's item type, like user, alarm station, etc
	 * @return constant name for each subclass
	 */
	@Override
	public String getTypeName() { return "User"; }

	@Override
	protected void informContainer() {		bean.informContainers(); }

	
	@Override
	public void applySettings() {
		
		//if(!ConfigurationFactory.getTransientState().assertRight(CliRight.usersControl))			return;
		
		bean.setLogin( loginField.getText() );
		bean.setPassword( passwordField.getText() );
		bean.setGroups(groups);
		setTitle();
	}

	@Override
	public void discardSettings() {
		passwordField.setText( bean.getPassword() );
		loginField.setText(bean.getLogin());
		groups = bean.getGroups();
		groupsField.setItems(groups);
	}
	
}
