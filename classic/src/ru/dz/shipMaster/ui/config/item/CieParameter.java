package ru.dz.shipMaster.ui.config.item;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import ru.dz.shipMaster.config.Configuration;
import ru.dz.shipMaster.config.ConstantState;
import ru.dz.shipMaster.config.items.CliAlarm;
import ru.dz.shipMaster.config.items.CliParameter;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.data.units.Unit;
import ru.dz.shipMaster.ui.config.ConfigItemEditor;

public class CieParameter extends ConfigItemEditor {

	private static final int TEXT_FIELD_CHARS = 24;
	private JTextField nameField = new JTextField("Name");
	private JTextField legendField = new JTextField("");
	private JTextField minValueField = new JTextField("");
	private JTextField maxValueField = new JTextField("");
	private JCheckBox translateToNetField = new JCheckBox("");
	private JCheckBox checkTimeoutField = new JCheckBox("");
	private JCheckBox debugField = new JCheckBox("");
	//private JComboBox typeField = new JComboBox(CliParameter.Type.values());
	private ButtonGroup typeGroup = new ButtonGroup();
	private JButton alarmButton = new JButton();
	private JButton unitButton = new JButton();
	private CliAlarm alarm;
	private Unit unit;

	//private Vector<CliConversion> conversions = new Vector<CliConversion>(); 


	private final CliParameter bean;
	private Type selectedType;
	private JRadioButton[] typeButtons = new JRadioButton[CliParameter.Type.values().length];
	private boolean haveLegend;;

	/**
	 * Create new editor for this bean. 
	 * @param bean Bean to edit.
	 */
	@SuppressWarnings("serial")
	public CieParameter(CliParameter bean) {
		this.bean = bean;
		panel.add(new JLabel("Name:"),consL);
		panel.add(nameField,consR);
		nameField.setColumns(TEXT_FIELD_CHARS);

		panel.add(new JLabel("Legend:"),consL);
		panel.add(legendField,consR);
		legendField.setColumns(TEXT_FIELD_CHARS);
		
		legendField.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
			}

			@Override
			public void focusLost(FocusEvent e) {
				haveLegend = legendField.getText().length() > 0;				
			}});

		{
			JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			consL.gridwidth = 2;
			panel.add(typePanel,consL);
			consL.gridwidth = 1;


			for( CliParameter.Type _v : CliParameter.Type.values() )	
			{
				final CliParameter.Type v = _v;
				JRadioButton typeButton = new JRadioButton(v.toString());
				typeGroup.add(typeButton);			
				typePanel.add(typeButton);
				typePanel.add(new JLabel(v.toString()));
				//typePanel.add(new JComponent
				
				typeButton.setAction(new AbstractAction() {

					@Override
					public void actionPerformed(ActionEvent e) {
						selectType(v);						
					}});
				
				typeButtons[v.ordinal()] = typeButton; 
			}


		}

		panel.add(new JLabel("Debug:"),consL);
		panel.add(debugField,consR);

		panel.add(new JLabel("Min:"),consL);
		panel.add(minValueField,consR);
		minValueField.setColumns(TEXT_FIELD_CHARS);

		panel.add(new JLabel("Max:"),consL);
		panel.add(maxValueField,consR);
		maxValueField.setColumns(TEXT_FIELD_CHARS);

		panel.add(new JLabel("Translate to net:"),consL);
		panel.add(translateToNetField,consR);

		panel.add(new JLabel("Reset on timeout:"),consL);
		panel.add(checkTimeoutField,consR);

		//panel.add(new JLabel("Type:"),consL);
		//typeField.setSelectedIndex(0);
		//panel.add(typeField,consR);

		panel.add(new JLabel("Alarm:"),consL);
		panel.add(alarmButton,consR);
		alarmButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) { selectAlarm(); }});

		panel.add(new JLabel("Unit:"),consL);
		panel.add(unitButton,consR);
		unitButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) { selectUnit(); }});

		discardSettings(); // load initial settings
	}

	protected void selectType(Type v) {
		selectedType = v;
		showHide();
	}

	private void showHide() {
		boolean isNum = (selectedType == Type.Numeric);
		boolean isBool = (selectedType == Type.Boolean);
		
		maxValueField.setEnabled(isNum);
		minValueField.setEnabled(isNum);
		
		unitButton.setEnabled(isNum);

		if(isBool)
		{
			maxValueField.setText("100");
			minValueField.setText("0");
			unit = ConstantState.getBooleanUnit();
		}
		else
		{
			if(unit != null && unit.equals(ConstantState.getBooleanUnit()))
				unit = null;
		}
		updateNames();
	}

	protected void selectAlarm() {
		/*Frame frame= JOptionPane.getFrameForComponent(panel);
		SelectFrame<CliAlarm> sf = new SelectFrame<CliAlarm>(
				ConfigurationFactory.getConfiguration().getAlarmItems()
				, frame, "Select alarm");
		sf.setVisible(true);
		alarm = sf.getResult();*/
		alarm = Configuration.selectAlarm(panel);
		updateNames();
	}

	protected void selectUnit()
	{
		unit = Configuration.selectUnit(panel);
		updateNames();
	}

	@Override
	protected void informContainer() {		bean.informContainers(); }

	@Override
	public void applySettings() {
		bean.setName( nameField.getText() );
		bean.setTranslateToNet( translateToNetField.isSelected() );
		bean.setCheckTimeout(checkTimeoutField.isSelected());
		
		bean.setDebug(debugField.isSelected());
		
		//bean.setType( CliParameter.Type.values()[typeField.getSelectedIndex()] );
		bean.setType(selectedType);
		
		
		bean.setAlarm(alarm);
		bean.setUnit(unit);
		bean.setMinValue(Double.parseDouble(minValueField.getText()));
		bean.setMaxValue(Double.parseDouble(maxValueField.getText()));
		
		if(haveLegend)
			bean.setLegend(legendField.getText());

		setTitle();
		showHide();
	}

	@Override
	public void discardSettings() {
		nameField.setText(bean.getName());
		translateToNetField.setSelected(bean.isTranslateToNet());
		checkTimeoutField.setSelected(bean.isCheckTimeout());
		//typeField.setSelectedIndex(bean.getType().ordinal());
		
		debugField.setSelected(bean.isDebug());
		
		alarm = bean.getAlarm();
		unit = bean.getUnit();
		maxValueField.setText(String.format(Locale.PRC,"%.1f", bean.getMaxValue()));
		minValueField.setText(String.format(Locale.PRC,"%.1f", bean.getMinValue()));

		haveLegend = bean.haveLegend();
		
		if(haveLegend)
			legendField.setText(bean.getLegend());
		
		typeButtons[bean.getType().ordinal()].setSelected(true);
		selectedType = bean.getType();
		
		updateNames();
		showHide();
	}

	private void updateNames() {
		alarmButton.setText(alarm == null ? "None" : alarm.getName() );
		unitButton.setText(unit == null ? "None" : unit.getName() );
	}

	@Override
	public String getTypeName() { return "Parameter"; }

	@Override
	public String getName() { return bean.getName(); }

}
