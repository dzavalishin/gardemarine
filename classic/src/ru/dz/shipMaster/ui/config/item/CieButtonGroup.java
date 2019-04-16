package ru.dz.shipMaster.ui.config.item;

import javax.swing.JLabel;
import javax.swing.JTextField;

import ru.dz.shipMaster.config.items.CliButtonGroup;
import ru.dz.shipMaster.ui.config.ConfigItemEditor;

public class CieButtonGroup extends ConfigItemEditor {
	private JTextField nameField = new JTextField("Name");

	private final CliButtonGroup bean;

	public CieButtonGroup(CliButtonGroup bean) {
		this.bean = bean;

		panel.add(new JLabel("Name:"),consL);
		panel.add(nameField,consR);

		discardSettings(); // load initial settings
	}

	@Override
	public String getName() { return bean.getName(); }

	@Override
	public String getTypeName() { return "Button group"; }

	@Override
	protected void informContainer() {		bean.informContainers(); }

	
	@Override
	public void applySettings() {
		bean.setName(nameField.getText());
		setTitle();
	}


	@Override
	public void discardSettings() {
		nameField.setText(bean.getName());
	}

}
