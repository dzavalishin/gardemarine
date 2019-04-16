package ru.dz.shipMaster.ui.config.item;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import ru.dz.shipMaster.config.ConfigurationFactory;
import ru.dz.shipMaster.data.units.Unit;
import ru.dz.shipMaster.data.units.UnitGroup;
import ru.dz.shipMaster.ui.config.ConfigItemEditor;
import ru.dz.shipMaster.ui.misc.VisualHelpers;

public class CieUnitGroup extends ConfigItemEditor {

	private final UnitGroup bean;
	private JLabel nameField = new JLabel();
	private JButton setImperialDefaultButton;
	private JButton setMetricDefaultButton;
	private JButton setNauticalDefaultButton;
	private Unit metricDefault;
	private Unit imperialDefault;
	private Unit reference;
	private JButton setReferenceButton;
	private JTextArea unitsField;
	private Unit nauticalDefault;

	/**
	 * Create new editor for this bean. 
	 * @param bean Bean to edit.
	 */
	@SuppressWarnings("serial")
	public CieUnitGroup(UnitGroup bean) {
		this.bean = bean;
		
		panel.add(new JLabel("Name:"),consL);
		panel.add(nameField ,consR);
		//nameField.setColumns(14);

		panel.add(new JLabel("Units:"),consL);
		panel.add(unitsField = new JTextArea(),consR);
		unitsField.setColumns(14);
		unitsField.setRows(5);
		
		
		panel.add(new JLabel("Reference (1x1) unit:"),consL);
		panel.add(setReferenceButton = new JButton(),consR);
		setReferenceButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) { selectReference(); }});
		setReferenceButton.setText("-");
		
		panel.add(new JLabel("Imperial default:"),consL);
		panel.add(setImperialDefaultButton = new JButton(),consR);
		setImperialDefaultButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) { selectImperial(); }});
		setImperialDefaultButton.setText("-");

		panel.add(new JLabel("Metric default:"),consL);
		panel.add(setMetricDefaultButton = new JButton(),consR);
		setMetricDefaultButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) { selectMetric(); }});
		setMetricDefaultButton.setText("-");

		panel.add(new JLabel("Nautical default:"),consL);
		panel.add(setNauticalDefaultButton = new JButton(),consR);
		setNauticalDefaultButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) { selectNautical(); }});
		setNauticalDefaultButton.setText("-");

		
		
		/*
		panel.add(new JLabel("Translate to net:"),consL);
		panel.add(translateToNetField,consR);

		panel.add(new JLabel("Type:"),consL);
		typeField.setSelectedIndex(0);
		//drvList.addActionListener(this);
		panel.add(typeField,consR);

		
		*/

		discardSettings(); // load initial settings

	}


	@SuppressWarnings("static-access")
	protected void selectReference() {
		Unit unit = ConfigurationFactory.getConfiguration().selectUnit(panel);
		if(unit == null)
			return;
		
		if(unit.getGroup() != null && unit.getGroup() != bean)
		{
			VisualHelpers.showMessageDialog(panel, "This unit belongs to other group");		
			return;
		}
		
		reference = unit;
		setButtonTexts();
	}

	@SuppressWarnings("static-access")
	protected void selectMetric() {
		Unit unit = ConfigurationFactory.getConfiguration().selectUnit(panel);
		if(unit == null || !unit.getGroup().equals( bean ) )
		{
			VisualHelpers.showMessageDialog(panel, "This unit belongs to other group");		
			return;
		}

		metricDefault = unit;
		setButtonTexts();
	}

	protected void selectImperial() {
		Unit unit = ConfigurationFactory.getConfiguration().selectUnit(panel);
		if(unit == null || !unit.getGroup().equals( bean ) )
		{
			VisualHelpers.showMessageDialog(panel, "This unit belongs to other group");		
			return;
		}
		imperialDefault = unit;
		setButtonTexts();
	}

	@SuppressWarnings("static-access")
	protected void selectNautical() {
		Unit unit = ConfigurationFactory.getConfiguration().selectUnit(panel);
		if(unit == null || !unit.getGroup().equals( bean ) )
		{
			VisualHelpers.showMessageDialog(panel, "This unit belongs to other group");		
			return;
		}
		nauticalDefault = unit;
		setButtonTexts();
	}
	
	@Override
	protected void informContainer() {		bean.informContainers(); }

	@Override
	public void applySettings() {
		bean.setImperialDefault(imperialDefault);
		bean.setMetricDefault(metricDefault);
		bean.setNauticalDefault(nauticalDefault);
		bean.setReference(reference);
		setTitle();
	}

	@Override
	public void discardSettings() {
		nameField.setText(bean.getName());
		
		imperialDefault = bean.getImperialDefault();
		metricDefault = bean.getMetricDefault();
		nauticalDefault = bean.getNauticalDefault();
		reference = bean.getReference();

		setButtonTexts();
	}

	
	private void setButtonTexts()
	{
		setImperialDefaultButton.setText(imperialDefault == null ? "none" : imperialDefault.getLongName());
		setMetricDefaultButton.setText(metricDefault == null ? "none" : metricDefault.getLongName());
		setNauticalDefaultButton.setText(nauticalDefault == null ? "none" : nauticalDefault.getLongName());
		
		setReferenceButton.setText(reference == null ? "none" : reference.getLongName());
		
		StringBuilder units = new StringBuilder();
		for(Unit u : ConfigurationFactory.getConfiguration().getUnitItems())
		{
			if(u.getGroup() == bean)
			{
				units.append(u.getLongName());
				units.append('\n');
			}
			
		}
		unitsField.setText(units.toString());
	}
	
	@Override
	public String getName() { return bean.getName(); }

	@Override
	public String getTypeName() { return "Unit group"; }

}
