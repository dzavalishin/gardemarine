package ru.dz.shipMaster.ui.config.filter;

import javax.swing.JLabel;
import javax.swing.JTextField;

import ru.dz.shipMaster.data.filter.GeneralFilterDataSource;
import ru.dz.shipMaster.ui.config.ConfigItemEditor;

public abstract class CieGeneralFilter extends ConfigItemEditor {
	private GeneralFilterDataSource gfbean;
	
	private JTextField nameField = new JTextField();

	protected CieGeneralFilter(GeneralFilterDataSource gfbean)
	{
		this.gfbean = gfbean;
		
		panel.add(new JLabel("Name:"),consL);
		panel.add(nameField,consR);
		nameField.setColumns(14);
	}
	
	@Override
	public void applySettings() {
		gfbean.setName(nameField.getText());
	}

	@Override
	public void discardSettings() {
		nameField.setText( gfbean.getName() );

	}

	@Override
	public String getName() { return nameField.getText(); }
	
}
