package ru.dz.shipMaster.ui.config.item;

import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import ru.dz.shipMaster.config.Configuration;
import ru.dz.shipMaster.config.ConfigurationFactory;
import ru.dz.shipMaster.config.items.CliConversion;
import ru.dz.shipMaster.config.items.CliParameter;
import ru.dz.shipMaster.dev.DriverPort;
import ru.dz.shipMaster.ui.SelectFrame;
import ru.dz.shipMaster.ui.config.ConfigItemEditor;

public class CieDriverPort extends ConfigItemEditor {

	private final DriverPort bean;
	private JLabel indexField;
	private JLabel hardwareNameField;
	private JTextField givenNameField = new JTextField("Name");
	private JLabel absentField;
	private JButton changeConvertor = new JButton();
	private JLabel convertorField;
	private JButton changeTarget = new JButton();
	private JLabel targetField;
	/** 
	 * Temp storage.
	 */
	private CliConversion conversion;
	private CliParameter target;
	private JTextArea descriptionField = new JTextArea(5,20);

	/**
	 * Create new editor for this bean. 
	 * @param bean Bean to edit.
	 */
	@SuppressWarnings("serial")
	public CieDriverPort(DriverPort bean) {
		this.bean = bean;

		changeConvertor.setAction(new AbstractAction() { 
			public void actionPerformed(ActionEvent e) { selectConvertor(); }});
		changeConvertor.setText("Select convertor:");
		
		changeTarget.setAction(new AbstractAction() { 
			public void actionPerformed(ActionEvent e) { selectTarget(); }});
		changeTarget.setText("Select target parameter");
		
		panel.add(new JLabel("Index (minor):"),consL);
		panel.add(indexField = new JLabel("-"),consR);
		
		panel.add(new JLabel("HW Name:"),consL);
		panel.add(hardwareNameField = new JLabel("-"),consR);

		panel.add(new JLabel("Given Name:"),consL);
		panel.add(givenNameField,consR);
		givenNameField.setColumns(14);
		
		panel.add(new JLabel("Hardware is:"),consL);
		panel.add(absentField = new JLabel("-"),consR);

		panel.add(changeConvertor,consL);
		panel.add(convertorField = new JLabel("-"),consR);

		panel.add(changeTarget,consL);
		panel.add(targetField = new JLabel("-"),consR);

		/*
		panel.add(new JLabel("Info:"),consL);
		panel.add(descriptionField ,consR);
		descriptionField.setEnabled(false);
		*/
		
		consL.gridwidth = 2;
		panel.add(descriptionField, consL);
		consL.gridwidth = 1;
		
		descriptionField.setFont(new Font(
				descriptionField.getFont().getFontName(), Font.PLAIN, 12
				));
		descriptionField.setEditable(false);
		
		discardSettings(); // load initial settings
	}

	@Override
	protected void informContainer() {		bean.informContainers(); }

	protected void selectTarget() {
		ConfigurationFactory.getConfiguration();
		/*
		Vector<CliParameter> parameterItems = ConfigurationFactory.getConfiguration().getParameterItems();
		
		Frame frame= JOptionPane.getFrameForComponent(panel);
		SelectFrame<CliParameter> sf = new SelectFrame<CliParameter>(parameterItems, frame, "Select target parameter");
		sf.setVisible(true);
		target = sf.getResult();
		//frame.dispose();
		*/
		target = Configuration.selectParameter(panel);
		
		if(target != null && hardwareNameField.getText().length() == 0)
			hardwareNameField.setText(target.getName());
		
		updateLabels();
	}

	protected void selectConvertor() {
		//ConfigurationFactory.getConfiguration().getConversionItems()
		Vector<CliConversion> conversionItems = ConfigurationFactory.getConfiguration().getConversionItems();
		
		Frame frame= JOptionPane.getFrameForComponent(panel);
		SelectFrame<CliConversion> sf = new SelectFrame<CliConversion>(conversionItems, frame, "Select convertor");
		sf.setVisible(true);
		conversion = sf.getResult();
		//frame.dispose();
		updateLabels();
	}

	@Override
	public void applySettings() {
		bean.setConvertor(conversion);
		bean.setTarget(target);
		bean.setGivenName(givenNameField.getText());
		updateLabels();
		setTitle();
	}

	@Override
	public void discardSettings() {
		conversion = bean.getConvertor();
		target = bean.getTarget();
		indexField.setText( Integer.toString( bean.getInternalPortIndex() ) );
		hardwareNameField.setText( bean.getHardwareName() );
		givenNameField.setText( bean.getGivenName() );
		absentField.setText( bean.isAbsent() ? "Absent" : "Present" );	
		descriptionField.setText( bean.getDescription() );
		updateLabels();
	}

	private void updateLabels() {
		CliConversion c = conversion;//bean.getConvertor();
		convertorField.setText(c != null ? c.getName() : "None" );
		CliParameter t = target;
		targetField.setText(t != null ? t.getName() : "None");
	}

	@Override
	public String getName() { return bean.getHardwareName() + "("+bean.getGivenName()+")"; }

	@Override
	public String getTypeName() { return "Driver port"; }

}
