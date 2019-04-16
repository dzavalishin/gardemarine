package ru.dz.shipMaster.ui.config.item;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import ru.dz.shipMaster.config.ConfigurationFactory;
import ru.dz.shipMaster.config.items.CliNetInput;
import ru.dz.shipMaster.config.items.CliParameter;
import ru.dz.shipMaster.ui.SelectFrame;
import ru.dz.shipMaster.ui.config.ConfigItemEditor;

public class CieNetInput extends ConfigItemEditor {

	private JTextField hostNameField = new JTextField(14);
	private JTextField itemNameField = new JTextField(14);

	private JButton changeTarget = new JButton();
	private JLabel targetField = new JLabel("-");
	
	private JCheckBox enabledField = new JCheckBox();
	
	private final CliNetInput bean;
	
	// Temp
	
	private CliParameter target;

	
	/**
	 * Create new editor for this bean. 
	 * @param bean Bean to edit.
	 */
	public CieNetInput(CliNetInput bean) {
		this.bean = bean;
		
		changeTarget.setAction(new AbstractAction() { 
			/**
			 * 
			 */
			private static final long serialVersionUID = 5650449997967547015L;

			public void actionPerformed(ActionEvent e) { selectTarget(); }});
		changeTarget.setText("Select target parameter");

		
		panel.add(new JLabel("Host name:"),consL);
		panel.add(hostNameField,consR);

		panel.add(new JLabel("Item name:"),consL);
		panel.add(itemNameField,consR);

		panel.add(changeTarget,consL);
		panel.add(targetField,consR);
		
		panel.add(new JLabel("Enabled:"),consL);
		panel.add(enabledField,consR);
		
		discardSettings();
	}
	
	protected void selectTarget() {
		Vector<CliParameter> parameterItems = ConfigurationFactory.getConfiguration().getParameterItems();
		
		Frame frame= JOptionPane.getFrameForComponent(panel);
		SelectFrame<CliParameter> sf = new SelectFrame<CliParameter>(parameterItems, frame, "Select target parameter");
		sf.setVisible(true);
		target = sf.getResult();
		//frame.dispose();
		updateLabels();
	}
	
	@Override
	protected void informContainer() {		bean.informContainers(); }

	@Override
	public void applySettings() {
		bean.setHostName( hostNameField.getText() );
		bean.setItemName(itemNameField.getText());
		bean.setTarget(target);
		bean.setEnabled(enabledField.isSelected());
		
	}


	@Override
	public void discardSettings() {
		hostNameField.setText(bean.getHostName());
		itemNameField.setText(bean.getItemName());
		target = bean.getTarget();
		updateLabels();
		enabledField.setSelected(bean.isEnabled());
	}

	private void updateLabels() {
		//CliConversion c = conversion;//bean.getConvertor();
		//convertorField.setText(c != null ? c.getName() : "None" );
		CliParameter t = target;
		targetField.setText(t != null ? t.getName() : "None");
	}
	
	@Override
	public String getName() { return bean.getName(); }

	@Override
	public String getTypeName() { return "Network item"; }
}
