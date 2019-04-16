package ru.dz.shipMaster.ui.config.item;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.logging.Level;

import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import ru.dz.shipMaster.config.items.CliLogger;
import ru.dz.shipMaster.ui.config.ConfigItemEditor;

/**
 * Configuration item editor for CliLogger.
 * @author dz
 */
public class CieLogger extends ConfigItemEditor {

	private JTextField logDestinationNameField = new JTextField("Log file");
	//private JCheckBox XMLModeField = new JCheckBox();
	private final CliLogger bean;
	private JComboBox typeField = new JComboBox(CliLogger.Type.values());
	protected JLabel parameterLabel = new JLabel("Log file name:");
	private JCheckBox logDataField = new JCheckBox();
	private JCheckBox logAlarmsField = new JCheckBox();

	/**
	 * Create new editor for this bean. 
	 * @param bean Bean to edit.
	 */
	@SuppressWarnings("serial")
	public CieLogger(CliLogger bean) {
		this.bean = bean;

		panel.add(new JLabel("Logging method:"),consL);
		typeField.setSelectedIndex(0);
		//drvList.addActionListener(this);
		panel.add(typeField,consR);
		
		typeField.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switch(CliLogger.Type.values()[typeField.getSelectedIndex()])
				{
				default:
					parameterLabel.setText("Name:              ");
					break;

				case LocalCsvFile:
				case LocalXmlFile:
					parameterLabel.setText("Log file name:     ");
					break;
					
				case SyslogdCsvUDP:
					parameterLabel.setText("Syslogd host name: ");
					break;
				}
				
			}});
		
		panel.add(parameterLabel,consL);
		panel.add(logDestinationNameField,consR);
		logDestinationNameField.setColumns(14);
		
		//panel.add(new JLabel("XML forma:"),consL);
		//panel.add(XMLModeField,consR);

		panel.add(new JLabel("Log data:"),consL);
		panel.add(logDataField,consR);
		
		panel.add(new JLabel("Log alarms:"),consL);
		panel.add(logAlarmsField,consR);
		
		
		discardSettings();
	}
	
	@Override
	protected void informContainer() {		bean.informContainers(); }

	
	@Override
	public void applySettings() {
		bean.setType( CliLogger.Type.values()[typeField.getSelectedIndex()] );
		bean.setLogDestinationName( logDestinationNameField.getText() );
		//bean.setXmlMode(XMLModeField.isSelected());
		
		bean.setLogAlarms(logAlarmsField.isSelected());
		bean.setLogData(logDataField.isSelected());
		
		try {			bean.restartLogger();		} 
		catch (IOException e) {
			log.log(Level.SEVERE,"Unable to start parameter logging",e);
			// TODO need retries?
		}
	}


	@Override
	public void discardSettings() {
		typeField.setSelectedIndex(bean.getType().ordinal());
		logDestinationNameField.setText(bean.getLogDestinationName());
		//XMLModeField.setSelected(bean.isXmlMode());
		logDataField.setSelected(bean.isLogData());
		logAlarmsField.setSelected(bean.isLogAlarms());
	}

	@Override
	public String getName() { return bean.getLogDestinationName(); }

	@Override
	public String getTypeName() { return "Logger"; }

}
