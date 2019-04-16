package ru.dz.shipMaster.ui.config.item;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import ru.dz.shipMaster.config.Configuration;
import ru.dz.shipMaster.data.units.Unit;
import ru.dz.shipMaster.data.units.UnitGroup;
import ru.dz.shipMaster.ui.config.ConfigItemEditor;
import ru.dz.shipMaster.ui.misc.VisualHelpers;

public class CieUnit extends ConfigItemEditor {

	private final Unit bean;
	private JTextField nameField = new JTextField();
	private JTextField longNameField = new JTextField();
	private JLabel groupNameField = new JLabel();
	private JCheckBox oneDividedByField = new JCheckBox();
	private JTextField firstShiftField = new JTextField();
	private JTextField firstMultiplicatorField = new JTextField();
	private JTextField secondMultiplicatorField = new JTextField();
	private JTextField secondShiftField = new JTextField();
	private UnitGroup group;
	private JButton changeButton;

	/**
	 * Create new editor for this bean. 
	 * @param bean Bean to edit.
	 */
	public CieUnit(Unit bean) {
		this.bean = bean;
		
		panel.add(new JLabel("Name:"),consL);
		panel.add(nameField,consR);
		nameField.setColumns(14);
		
		panel.add(new JLabel("Long name:"),consL);
		panel.add(longNameField,consR);
		longNameField.setColumns(14);
		
		panel.add(new JLabel("Group:"),consL);
		panel.add(groupNameField ,consR);

		panel.add(new JLabel("X+:"),consL);
		panel.add(firstShiftField ,consR);
		firstShiftField.setColumns(8);
		
		panel.add(new JLabel("X*:"),consL);
		panel.add(firstMultiplicatorField ,consR);
		firstMultiplicatorField.setColumns(8);
		
		panel.add(new JLabel("1/X:"),consL);
		panel.add(oneDividedByField ,consR);

		panel.add(new JLabel("X*:"),consL);
		panel.add(secondMultiplicatorField ,consR);
		secondMultiplicatorField.setColumns(8);
		
		panel.add(new JLabel("X+:"),consL);
		panel.add(secondShiftField ,consR);
		secondShiftField.setColumns(8);
		
		panel.add(new JLabel("Unit Group:"),consL);
		panel.add(changeButton = new JButton(),consR);
		
		changeButton.addActionListener(new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 4152566730620860969L;

			public void actionPerformed(ActionEvent e) { selectGroup(); }});
		changeButton.setText("Change");
		
		/*
		panel.add(new JLabel("Type:"),consL);
		typeField.setSelectedIndex(0);
		//drvList.addActionListener(this);
		panel.add(typeField,consR);

		panel.add(new JLabel("Alarm:"),consL);
		panel.add(alarmButton,consR);
		alarmButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) { selectAlarm(); }});
		*/
		
		discardSettings(); // load initial settings
	}

	protected void selectGroup() {

		if(group != null)
		{
			if(group.getReference() == bean)
			{
				VisualHelpers.showMessageDialog(panel, "This unit is reference for its current group");
				return;
			}
			
			if(group.getImperialDefault() == bean)
			{
				VisualHelpers.showMessageDialog(panel, "This unit is imperial default for its current group");
				return;
			}
			
			if(group.getMetricDefault() == bean)
			{
				VisualHelpers.showMessageDialog(panel, "This unit is metric default for its current group");
				return;
			}
		}
		
		group = Configuration.selectUnitGroup(panel);
		updateGroupName();
	}
	
	
	private void updateGroupName() {
		groupNameField.setText(group == null ? "Unknown" : group.getName());
	}

	@Override
	protected void informContainer() {		bean.informContainers(); }

	@Override
	public void applySettings() {
		bean.setGroup(group);
		if(group != null && group.getReference() == null)
			group.setReference(bean);
		
		bean.setName( nameField.getText() );
		bean.setLongName( longNameField.getText() );
		updateGroupName(); // for it may be modified by setName above
		
		bean.setOneDividedBy( oneDividedByField.isSelected() );
		
		bean.setFirstShift( Double.parseDouble(firstShiftField.getText()));
		bean.setSecondShift( Double.parseDouble(secondShiftField.getText()));
		bean.setFirstMultiplicator( Double.parseDouble(firstMultiplicatorField.getText()));
		bean.setSecondMultiplicator( Double.parseDouble(secondMultiplicatorField.getText()));
		
		//bean.setType( CliParameter.Type.values()[typeField.getSelectedIndex()] );
		//bean.setAlarm(alarm);
		setTitle();
	}

	@Override
	public void discardSettings() {
		nameField.setText(bean.getName());
		longNameField.setText(bean.getLongName());
		group = bean.getGroup();
		//groupNameField.setText(bean.getGroup().getName());
		updateGroupName();
		
		oneDividedByField.setSelected(bean.isOneDividedBy());
		
		firstShiftField.setText(Double.toString(bean.getFirstShift()));
		secondShiftField.setText(Double.toString(bean.getSecondShift()));
		firstMultiplicatorField.setText(Double.toString(bean.getFirstMultiplicator()));
		secondMultiplicatorField.setText(Double.toString(bean.getSecondMultiplicator()));
		
		//typeField.setSelectedIndex(bean.getType().ordinal());
		//alarm = bean.getAlarm();
		setTitle();
	}

	@Override
	public String getName() { return bean.getName(); }

	@Override
	public String getTypeName() { return "Unit"; }

}
