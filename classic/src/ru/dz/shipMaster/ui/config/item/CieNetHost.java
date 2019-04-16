package ru.dz.shipMaster.ui.config.item;


import javax.swing.JLabel;
import javax.swing.JTextField;

import ru.dz.shipMaster.config.items.CliNetHost;
import ru.dz.shipMaster.ui.config.ConfigItemEditor;

public class CieNetHost extends ConfigItemEditor {

	private JTextField hostNameField = new JTextField(14);
	private JTextField hostPasswordField = new JTextField(14);
	
	//private JCheckBox XMLModeField = new JCheckBox();
	
	private final CliNetHost bean;
	
	/**
	 * Create new editor for this bean. 
	 * @param bean Bean to edit.
	 */
	public CieNetHost(CliNetHost bean) {
		this.bean = bean;
		
		panel.add(new JLabel("Host name:"),consL);
		panel.add(hostNameField,consR);

		panel.add(new JLabel("Host password:"),consL);
		panel.add(hostPasswordField,consR);
		
		//panel.add(new JLabel("XML forma:"),consL);
		//panel.add(XMLModeField,consR);
		
		discardSettings();
	}
	
	@Override
	protected void informContainer() {		bean.informContainers(); }

	@Override
	public void applySettings() {
		bean.setHostName( hostNameField.getText() );
		bean.setHostPassword(hostPasswordField.getText());
		//bean.setXmlMode(XMLModeField.isSelected());
		
	}


	@Override
	public void discardSettings() {
		hostNameField.setText(bean.getHostName());
		hostPasswordField.setText(bean.getHostPassword());
		//XMLModeField.setSelected(bean.isXmlMode());
	}

	@Override
	public String getName() { return bean.getName(); }

	@Override
	public String getTypeName() { return "Network host"; }

}
