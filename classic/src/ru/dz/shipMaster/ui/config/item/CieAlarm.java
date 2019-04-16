package ru.dz.shipMaster.ui.config.item;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import ru.dz.shipMaster.config.Configuration;
import ru.dz.shipMaster.config.items.CliAlarm;
import ru.dz.shipMaster.config.items.CliAlarm.Type;
import ru.dz.shipMaster.config.items.CliAlarmStation;
import ru.dz.shipMaster.ui.config.ConfigItemEditor;

public class CieAlarm extends ConfigItemEditor {
	private final CliAlarm bean;
	
	private JTextField nameField = new JTextField();
	private JComboBox typeField = new JComboBox(CliAlarm.Type.values());

	private JTextField lwField = new JTextField("0");
	private JTextField lcField = new JTextField("0");
	private JTextField hwField = new JTextField("0");
	private JTextField hcField = new JTextField("0");

	private JTextField soundFileField = new JTextField(20);
	
	private JButton stationChangeButton;

	private JLabel stationLabel;

	private CliAlarmStation station;
	
	
	/**
	 * Create new editor for this bean. 
	 * @param bean Bean to edit.
	 */
	@SuppressWarnings("serial")
	public CieAlarm(CliAlarm bean) {
		this.bean = bean;
		
		panel.add(new JLabel("Name:"),consL);
		panel.add(nameField,consR);
		nameField.setColumns(20);

		panel.add(new JLabel("Type:"),consL);
		panel.add(typeField,consR);

		panel.add(new JLabel("Hi critical:"),consL);
		panel.add(hcField,consR);
		hcField.setColumns(8);
		hcField.setToolTipText("Value above this supposed to be critical -- EVEN IN INTERVAL MODE");

		panel.add(new JLabel("Hi warning:"),consL);
		panel.add(hwField,consR);
		hwField.setColumns(8);

		panel.add(new JLabel("Low warning:"),consL);
		panel.add(lwField,consR);
		lwField.setColumns(8);

		panel.add(new JLabel("Low critical:"),consL);
		panel.add(lcField,consR);
		lcField.setColumns(8);
		lcField.setToolTipText("Value below this supposed to be critical -- EVEN IN INTERVAL MODE");

		typeField.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				showHide();
				
			}});

		panel.add(stationChangeButton = new JButton(),consL);
		panel.add(stationLabel = new JLabel(""),consR);
		stationChangeButton.addActionListener(new AbstractAction() { public void actionPerformed(ActionEvent e) { selectStation(); }});
		stationChangeButton.setText("Alarm station:");
		
		panel.add(new JLabel("Alarm sound file:"),consL);
		panel.add(soundFileField, consR);
		
	}

	protected void selectStation() {
		/*
		 * Frame frame= JOptionPane.getFrameForComponent(panel);
		SelectFrame<CliAlarmStation> sf = new SelectFrame<CliAlarmStation>(
				ConfigurationFactory.getConfiguration().getAlarmStationItems(), frame, "Select alarm station");
		sf.setVisible(true);
		station = sf.getResult();
		*/
		station = Configuration.selectAlarmStation(panel);
		updateStationName();
	}

	protected void showHide() {
		Type selectedItem = (Type) typeField.getSelectedItem();
		
		boolean editLow = !Type.UpperForbidden.equals( selectedItem );
		boolean editHi = !Type.LowerForbidden.equals( selectedItem );
		
		lwField.setEnabled(editLow);
		lcField.setEnabled(editLow);
		
		hwField.setEnabled(editHi);
		hcField.setEnabled(editHi);
	}

	@Override
	public void applySettings() {
		bean.setName(nameField.getText());

		bean.setHiCritical(Double.parseDouble(hcField.getText()));
		bean.setHiWarning(Double.parseDouble(hwField.getText()));
		bean.setLowWarning(Double.parseDouble(lwField.getText()));
		bean.setLowCritical(Double.parseDouble(lcField.getText()));
		
		bean.setType((Type) typeField.getSelectedItem());
		bean.setStation(station);
		
		bean.setSoundFile(soundFileField.getText());
		
		showHide();
	}

	@Override
	public void discardSettings() {
		nameField.setText(bean.getName());

		lwField.setText(""+bean.getLowWarning());
		hwField.setText(""+bean.getHiWarning());
		lcField.setText(""+bean.getLowCritical());
		hcField.setText(""+bean.getHiCritical());
		
		typeField.setSelectedItem(bean.getType());
		
		soundFileField.setText(bean.getSoundFile());
		
		station = bean.getStation();
		updateStationName();
		showHide();
	}

	private void updateStationName()
	{
		stationLabel.setText(station == null ? "None" : station.getName());
	}
	
	@Override
	public String getName() { return bean.getName(); }

	@Override
	public String getTypeName() { return "Alarm"; }

	@Override
	protected void informContainer() {		bean.informContainers(); }

}
