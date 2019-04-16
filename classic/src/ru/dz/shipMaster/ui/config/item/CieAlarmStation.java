package ru.dz.shipMaster.ui.config.item;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import ru.dz.shipMaster.config.Configuration;
import ru.dz.shipMaster.config.items.CliAlarmStation;
import ru.dz.shipMaster.config.items.CliParameter;
import ru.dz.shipMaster.ui.config.ConfigItemEditor;

/**
 * Alarm station configuration item editor.
 * @author dz
 *
 */
public class CieAlarmStation extends ConfigItemEditor {
	private final CliAlarmStation bean;

	private JTextField placeField = new JTextField();
	private JPasswordField password = new JPasswordField();
	private JTextField escalationTimeField = new JTextField();
	private JButton changeTarget = new JButton();
	private JButton changeAckInput = new JButton();
	private JButton stationChangeButton;
	private JLabel nextStationLabel;
	private JLabel targetField;
	private JLabel ackInputField;
	private JCheckBox windowEnabled = new JCheckBox();
	private JCheckBox soundEnabled = new JCheckBox();

	private CliAlarmStation nextStation;
	private CliParameter target;
	private CliParameter ackInput;


	
	/**
	 * Create new editor for this bean. 
	 * @param bean Bean to edit.
	 */
	@SuppressWarnings("serial")
	public CieAlarmStation(CliAlarmStation bean)
	{
		this.bean = bean;
		//panel.add(new JLabel("Edit user properties:"));
		panel.add(new JLabel("Place:"),consL);
		panel.add(placeField,consR);
		placeField.setColumns(14);
		
		panel.add(new JLabel("Password:"),consL);
		panel.add(password,consR);
		password.setColumns(14);

		panel.add(new JLabel("Escalation time, sec:"),consL);
		panel.add(escalationTimeField,consR);
		escalationTimeField.setColumns(4);

		panel.add(stationChangeButton = new JButton(),consL);
		panel.add(nextStationLabel = new JLabel(""),consR);
		stationChangeButton.addActionListener(new AbstractAction() { public void actionPerformed(ActionEvent e) { selectStation(); }});
		stationChangeButton.setText("Propagate alarm to:");

		changeTarget.setAction(new AbstractAction() { 
			public void actionPerformed(ActionEvent e) { selectTarget(); }});
		changeTarget.setText("Alarm control output parameter");

		panel.add(changeTarget,consL);
		panel.add(targetField = new JLabel("-"),consR);

		changeAckInput .setAction(new AbstractAction() { 
			public void actionPerformed(ActionEvent e) { selectAckInput(); }});
		changeAckInput.setText("Alarm acknowledge input parameter");

		panel.add(changeAckInput,consL);
		panel.add(ackInputField = new JLabel("-"),consR);
		
		panel.add(new JLabel("Display alarms window:"),consL);
		panel.add(windowEnabled,consR);

		panel.add(new JLabel("Siren sound:"),consL);
		panel.add(soundEnabled,consR);
	}

	protected void selectStation() {		
		CliAlarmStation result = Configuration.selectAlarmStation(panel);
		// Forbid self reference
		nextStation = (result == bean) ? null : result;
		updateLabels();
	}

	protected void selectTarget() {
		target = Configuration.selectParameter(panel);
		updateLabels();
	}

	protected void selectAckInput() {
		ackInput= Configuration.selectParameter(panel);
		updateLabels();
	}


	private void updateLabels()
	{
		nextStationLabel.setText(nextStation == null ? "None" : nextStation.getName());
		targetField.setText(target == null ? "none" : target.getName());
		ackInputField.setText(ackInput == null ? "none" : ackInput.getName());
	}
	
	@Override
	public void applySettings() {
		bean.setPlace( placeField.getText() );
		bean.setEscalationTime( Integer.parseInt(escalationTimeField.getText()) );
		bean.setNextStation(nextStation);
		bean.setTarget(target);
		bean.setAckInput(ackInput);
		bean.setUseAlarmsWindow(windowEnabled.isSelected());
		bean.setSoundEnabled(soundEnabled.isSelected());
		setTitle();
	}

	@Override
	public void discardSettings() {
		placeField.setText(bean.getPlace());
		escalationTimeField.setText(Integer.toString(bean.getEscalationTime()));
		nextStation = bean.getNextStation();
		target = bean.getTarget();
		ackInput = bean.getAckInput();
		windowEnabled.setSelected(bean.isUseAlarmsWindow());
		soundEnabled.setSelected(bean.isSoundEnabled());
		updateLabels();
	}

	/**
	 * Returns name of item instance.
	 * @return Instance name string.
	 */
	@Override
	public String getName() { return bean.getPlace(); }

	/**
	 * Returns name of subclass's item type, like user, alarm station, etc
	 * @return constant name for each subclass
	 */
	@Override
	public String getTypeName() { return "Alarm station"; }
	
	@Override
	protected void informContainer() {		bean.informContainers(); }

}
