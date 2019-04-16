package ru.dz.shipMaster.ui.config.item;

import java.util.Date;

import javax.swing.JLabel;

import ru.dz.shipMaster.data.history.HistoryAlarmRecord;
import ru.dz.shipMaster.data.history.HistoryDataRecord;
import ru.dz.shipMaster.data.history.HistoryRecord;
import ru.dz.shipMaster.ui.config.ConfigItemEditor;

public class CieHistoryRecord extends ConfigItemEditor {

	private final HistoryRecord bean;
	private JLabel parameterNameField;
	private JLabel sourceNodeNameField;
	private JLabel timeField;
	private JLabel alarmNameField;

	/**
	 * Create new editor for this bean. 
	 * @param bean Bean to edit.
	 */
	public CieHistoryRecord(HistoryRecord bean) {
		this.bean = bean;

		panel.add(new JLabel("Parameter:"),consL);
		panel.add(parameterNameField = new JLabel(""),consR);

		panel.add(new JLabel("Alarm:"),consL);
		panel.add(alarmNameField = new JLabel(""),consR);

		panel.add(new JLabel("Source node:"),consL);
		panel.add(sourceNodeNameField = new JLabel(""),consR);

		panel.add(new JLabel("Time:"),consL);
		panel.add(timeField = new JLabel(""),consR);
		
		discardSettings();
	}

	@Override
	protected void informContainer() {		bean.informContainers(); }

	@Override
	public void applySettings() {
		// No edit possible
	}

	@Override
	public void discardSettings() {
		if (bean instanceof HistoryDataRecord) {
			HistoryDataRecord dr = (HistoryDataRecord) bean;
			parameterNameField.setText(dr.getParameterName());			
		}

		if (bean instanceof HistoryAlarmRecord) {
			HistoryAlarmRecord ar = (HistoryAlarmRecord) bean;
			alarmNameField.setText(ar.getAlarmName());			
		}
		
		sourceNodeNameField.setText(bean.getSourceNodeName());
		Date d = new Date(bean.getTime());
		timeField.setText( d.toString() );
	}

	@Override
	public String getName() { return bean.getName(); }

	@Override
	public String getTypeName() { return "History record"; }

}
