package ru.dz.shipMaster.config;

import java.awt.Frame;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ru.dz.shipMaster.config.items.CliAlarm;
import ru.dz.shipMaster.config.items.CliAlarmStation;
import ru.dz.shipMaster.config.items.CliBus;
import ru.dz.shipMaster.config.items.CliButtonGroup;
import ru.dz.shipMaster.config.items.CliConversion;
import ru.dz.shipMaster.config.items.CliDriver;
import ru.dz.shipMaster.config.items.CliGroup;
import ru.dz.shipMaster.config.items.CliInstrument;
import ru.dz.shipMaster.config.items.CliInstrumentPanel;
import ru.dz.shipMaster.config.items.CliLogger;
import ru.dz.shipMaster.config.items.CliNetHost;
import ru.dz.shipMaster.config.items.CliNetInput;
import ru.dz.shipMaster.config.items.CliParameter;
import ru.dz.shipMaster.config.items.CliPipe;
import ru.dz.shipMaster.config.items.CliRight;
import ru.dz.shipMaster.config.items.CliSystemDriver;
import ru.dz.shipMaster.config.items.CliUser;
import ru.dz.shipMaster.config.items.CliWindow;
import ru.dz.shipMaster.config.items.CliWindowStructure;
import ru.dz.shipMaster.data.units.Unit;
import ru.dz.shipMaster.data.units.UnitGroup;
import ru.dz.shipMaster.ui.LoginFrame;
import ru.dz.shipMaster.ui.SelectFrame;

public class Configuration {

	// Selectors
	/*@Deprecated
	public static DashComponent SelectAndCreateDashComponent(JPanel referencePanel)
	{
		CliInstrument result = SelectInstrument(referencePanel);	
		if(result == null)			return null;		
		return result.createNewInstance();
	}*/

	public static CliInstrument SelectInstrument(JPanel referencePanel)
	{
		Frame frame= JOptionPane.getFrameForComponent(referencePanel);
		SelectFrame<CliInstrument> sf = new SelectFrame<CliInstrument>(
				ConfigurationFactory.getConfiguration().getInstrumentItems(), frame, "Select instrument");
		sf.setVisible(true);
		return sf.getResult();		
	}

	public static CliInstrumentPanel SelectInstrumentPanel(JPanel referencePanel)
	{
		Frame frame= JOptionPane.getFrameForComponent(referencePanel);
		SelectFrame<CliInstrumentPanel> sf = new SelectFrame<CliInstrumentPanel>(
				ConfigurationFactory.getConfiguration().getInstrumentPanelItems(), frame, "Select instrument panel");
		sf.setVisible(true);
		return sf.getResult();		
	}
	
	public static CliWindowStructure selectWindowStructure(JPanel referencePanel)
	{
		Frame frame= JOptionPane.getFrameForComponent(referencePanel);
		SelectFrame<CliWindowStructure> sf = new SelectFrame<CliWindowStructure>(ConfigurationFactory.getConfiguration().getWindowStructureItems(), frame, "Select window structure");
		sf.setVisible(true);
		return sf.getResult();
		//structureLabel.setText(result.getName());
		//structure = result;		
	}
	
	public static CliAlarmStation selectAlarmStation(JPanel referencePanel)
	{
		Frame frame= JOptionPane.getFrameForComponent(referencePanel);
		SelectFrame<CliAlarmStation> sf = new SelectFrame<CliAlarmStation>(
				ConfigurationFactory.getConfiguration().getAlarmStationItems(), frame, "Select alarm station");
		sf.setVisible(true);
		return sf.getResult();		
	}
	
	public static CliAlarm selectAlarm(JPanel referencePanel) {
		Frame frame= JOptionPane.getFrameForComponent(referencePanel);
		SelectFrame<CliAlarm> sf = new SelectFrame<CliAlarm>(
				ConfigurationFactory.getConfiguration().getAlarmItems()
				, frame, "Select alarm");
		sf.setVisible(true);
		return sf.getResult();
	}
	
	
	public static UnitGroup selectUnitGroup(JPanel referencePanel)
	{
		Frame frame= JOptionPane.getFrameForComponent(referencePanel);
		SelectFrame<UnitGroup> sf = new SelectFrame<UnitGroup>(
				ConfigurationFactory.getConfiguration().getUnitGroupItems(), frame, "Select unit group");
		sf.setVisible(true);
		return sf.getResult();		
	}

	public static Unit selectUnit(JPanel referencePanel)
	{
		Frame frame= JOptionPane.getFrameForComponent(referencePanel);
		SelectFrame<Unit> sf = new SelectFrame<Unit>(
				ConfigurationFactory.getConfiguration().getUnitItems(), frame, "Select unit");
		sf.setVisible(true);
		return sf.getResult();		
	}

	public static CliParameter selectParameter(JPanel referencePanel) {
		Frame frame= JOptionPane.getFrameForComponent(referencePanel);
		SelectFrame<CliParameter> sf = new SelectFrame<CliParameter>(
				ConfigurationFactory.getConfiguration().getParameterItems(), frame, "Select parameter");
		sf.setVisible(true);
		return sf.getResult();		
	}

	public CliUser selectUser(JPanel referencePanel) {
		Frame frame = JOptionPane.getFrameForComponent(referencePanel);
		SelectFrame<CliUser> sf = new SelectFrame<CliUser>(
				ConfigurationFactory.getConfiguration().getUserItems(), frame, "Select user");
		sf.setVisible(true);
		return sf.getResult();		
	}

	public static CliPipe selectPipe(JPanel referencePanel) {
		Frame frame = JOptionPane.getFrameForComponent(referencePanel);
		SelectFrame<CliPipe> sf = new SelectFrame<CliPipe>(
				ConfigurationFactory.getConfiguration().getPipeItems(), frame, "Select channel");
		sf.setVisible(true);
		return sf.getResult();		
	}

	
	public static CliBus selectBus(JPanel referencePanel) {
		Frame frame = JOptionPane.getFrameForComponent(referencePanel);
		SelectFrame<CliBus> sf = new SelectFrame<CliBus>(
				ConfigurationFactory.getConfiguration().getBusItems(), frame, "Select bus");
		sf.setVisible(true);
		return sf.getResult();		
	}

	public static CliButtonGroup selectButtomGroup(JPanel referencePanel) {
		Frame frame = JOptionPane.getFrameForComponent(referencePanel);
		SelectFrame<CliButtonGroup> sf = new SelectFrame<CliButtonGroup>(
				ConfigurationFactory.getConfiguration().getButtonGroupsItems(), frame, "Select button group");
		sf.setVisible(true);
		return sf.getResult();		
	}

	
	public CliUser login(JPanel referencePanel) {
		Frame frame = JOptionPane.getFrameForComponent(referencePanel);
		LoginFrame lf = new LoginFrame(frame);
		lf.setVisible(true);
		return lf.getResult();		
	}

	
	
	// Beans
	
	private GeneralConfigBean general = new GeneralConfigBean();
	
	private Vector<CliUser> userItems = new Vector<CliUser>();
	private Vector<CliUser> userLibItems = new Vector<CliUser>();

	private Vector<CliGroup> groupItems = new Vector<CliGroup>();
	private Vector<CliGroup> groupLibItems = new Vector<CliGroup>();

	private Vector<CliAlarmStation> alarmStationItems = new Vector<CliAlarmStation>();
	private Vector<CliAlarmStation> alarmStationLibItems = new Vector<CliAlarmStation>();
	
	private Vector<CliAlarm> alarmItems = new Vector<CliAlarm>();
	private Vector<CliAlarm> alarmLibItems = new Vector<CliAlarm>();
	
	private Vector<CliDriver> driverItems = new Vector<CliDriver>();
	private Vector<CliDriver> driverLibItems = new Vector<CliDriver>();
	
	private Vector<CliParameter> parameterItems = new Vector<CliParameter>();
	private Vector<CliParameter> parameterLibItems = new Vector<CliParameter>();
	
	private Vector<CliConversion> conversionItems = new Vector<CliConversion>();
	private Vector<CliConversion> conversionLibItems = new Vector<CliConversion>();
	
	private Vector<Unit> unitItems = new Vector<Unit>();
	private Vector<Unit> unitLibItems = new Vector<Unit>();

	private Vector<UnitGroup> unitGroupItems = new Vector<UnitGroup>();
	private Vector<UnitGroup> unitGroupLibItems = new Vector<UnitGroup>();

	private Vector<CliWindow> windowItems = new Vector<CliWindow>();
	private Vector<CliWindow> windowLibItems = new Vector<CliWindow>();
	
	private Vector<CliLogger> loggerItems = new Vector<CliLogger>();
	private Vector<CliLogger> loggerLibItems = new Vector<CliLogger>();
	
	private Vector<CliWindowStructure> windowStructureItems = new Vector<CliWindowStructure>();
	private Vector<CliWindowStructure> windowStructureLibItems = new Vector<CliWindowStructure>();

	private Vector<CliInstrumentPanel> instrumentPanelItems = new Vector<CliInstrumentPanel>();
	private Vector<CliInstrumentPanel> instrumentPanelLibItems = new Vector<CliInstrumentPanel>();

	private Vector<CliInstrument> instrumentItems = new Vector<CliInstrument>();
	private Vector<CliInstrument> instrumentLibItems = new Vector<CliInstrument>();

	private Vector<CliNetHost> netHostItems = new Vector<CliNetHost>();
	private Vector<CliNetHost> netHostLibItems = new Vector<CliNetHost>();

	private Vector<CliNetInput> netInputItems = new Vector<CliNetInput>();
	private Vector<CliNetInput> netInputLibItems = new Vector<CliNetInput>();
	
	private Vector<CliBus> busItems = new Vector<CliBus>();
	private Vector<CliBus> busLibItems = new Vector<CliBus>();

	private Vector<CliPipe> pipeItems = new Vector<CliPipe>();
	private Vector<CliPipe> pipeLibItems = new Vector<CliPipe>();

	private Vector<CliRight> rightsItems = new Vector<CliRight>(); //CliRight.getRights();
	
	private Vector<CliSystemDriver> systemDriverItems = new Vector<CliSystemDriver>(); // NB - will be set up in factory.

	private Vector<CliButtonGroup> buttonGroupsItems = new Vector<CliButtonGroup>();


	// Now getters/setters
	
	public GeneralConfigBean getGeneral() {		return general;	}
	public void setGeneral(GeneralConfigBean general) {		this.general = general;	}
	
	public Vector<CliUser> getUserItems() {		return userItems;	}
	public void setUserItems(Vector<CliUser> userItems) {		this.userItems = userItems;	}
	public Vector<CliUser> getUserLibItems() {		return userLibItems;	}
	public void setUserLibItems(Vector<CliUser> userLibItems) {		this.userLibItems = userLibItems;	}

	public Vector<CliAlarmStation> getAlarmStationItems() {		return alarmStationItems;		}
	public void setAlarmStationItems(Vector<CliAlarmStation> alarmStationItems) {				this.alarmStationItems = alarmStationItems;		}
	
	public Vector<CliAlarmStation> getAlarmStationLibItems() {		return alarmStationLibItems;	}
	public void setAlarmStationLibItems(Vector<CliAlarmStation> alarmStationLibItems) {		this.alarmStationLibItems = alarmStationLibItems;	}
	
	public Vector<CliConversion> getConversionItems() {		return conversionItems;	}
	public void setConversionItems(Vector<CliConversion> conversionItems) {		this.conversionItems = conversionItems;	}
	public Vector<CliConversion> getConversionLibItems() {		return conversionLibItems;	}
	public void setConversionLibItems(Vector<CliConversion> conversionLibItems) {		this.conversionLibItems = conversionLibItems;	}
	
	public Vector<CliDriver> getDriverItems() {		return driverItems;	}
	public void setDriverItems(Vector<CliDriver> driverItems) {		this.driverItems = driverItems;	}
	public Vector<CliDriver> getDriverLibItems() {		return driverLibItems;	}
	public void setDriverLibItems(Vector<CliDriver> driverLibItems) {		this.driverLibItems = driverLibItems;	}
	
	public Vector<CliGroup> getGroupItems() {		return groupItems;	}
	public void setGroupItems(Vector<CliGroup> groupItems) {		this.groupItems = groupItems;	}
	public Vector<CliGroup> getGroupLibItems() {		return groupLibItems;	}
	public void setGroupLibItems(Vector<CliGroup> groupLibItems) {		this.groupLibItems = groupLibItems;	}
	
	public Vector<CliLogger> getLoggerItems() {		return loggerItems;	}
	public void setLoggerItems(Vector<CliLogger> loggerItems) {		this.loggerItems = loggerItems;	}
	public Vector<CliLogger> getLoggerLibItems() {		return loggerLibItems;	}
	public void setLoggerLibItems(Vector<CliLogger> loggerLibItems) {		this.loggerLibItems = loggerLibItems;	}
	
	public Vector<CliParameter> getParameterItems() {		return parameterItems;	}
	public void setParameterItems(Vector<CliParameter> parameterItems) {		this.parameterItems = parameterItems;	}
	public Vector<CliParameter> getParameterLibItems() {		return parameterLibItems;	}
	public void setParameterLibItems(Vector<CliParameter> parameterLibItems) {		this.parameterLibItems = parameterLibItems;	}
	
	public Vector<CliWindow> getWindowItems() {		return windowItems;	}
	public void setWindowItems(Vector<CliWindow> windowItems) {		this.windowItems = windowItems;	}
	public Vector<CliWindow> getWindowLibItems() {		return windowLibItems;	}
	public void setWindowLibItems(Vector<CliWindow> windowLibItems) {		this.windowLibItems = windowLibItems;	}
	
	public Vector<CliRight> getRightsItems() {		return rightsItems;	}
	public void setRightsItems(Vector<CliRight> rightsItems) {		this.rightsItems = rightsItems;	}

	public Vector<CliSystemDriver> getSystemDriverItems() {		return systemDriverItems;	}
	public void setSystemDriverItems(Vector<CliSystemDriver> systemDriverItems) {		this.systemDriverItems = systemDriverItems;	}
	
	public Vector<CliWindowStructure> getWindowStructureItems() {		return windowStructureItems;	}
	public void setWindowStructureItems(			Vector<CliWindowStructure> windowStructureItems) {		this.windowStructureItems = windowStructureItems; }
	public Vector<CliWindowStructure> getWindowStructureLibItems() {		return windowStructureLibItems;	}
	public void setWindowStructureLibItems(	Vector<CliWindowStructure> windowStructureLibItems) {	this.windowStructureLibItems = windowStructureLibItems;	}
	
	public Vector<CliInstrumentPanel> getInstrumentPanelItems() {		return instrumentPanelItems;	}
	public void setInstrumentPanelItems(	Vector<CliInstrumentPanel> instrumentPanelItems) {		this.instrumentPanelItems = instrumentPanelItems;	}
	public Vector<CliInstrumentPanel> getInstrumentPanelLibItems() {		return instrumentPanelLibItems;	}
	public void setInstrumentPanelLibItems(		Vector<CliInstrumentPanel> instrumentPanelLibItems) {		this.instrumentPanelLibItems = instrumentPanelLibItems;	}
	
	public Vector<CliInstrument> getInstrumentItems() {		return instrumentItems;	}
	public void setInstrumentItems(Vector<CliInstrument> instrumentItems) {		this.instrumentItems = instrumentItems;	}
	public Vector<CliInstrument> getInstrumentLibItems() {		return instrumentLibItems;	}
	public void setInstrumentLibItems(Vector<CliInstrument> instrumentLibItems) {		this.instrumentLibItems = instrumentLibItems;	}
	
	public Vector<CliAlarm> getAlarmItems() {		return alarmItems;	}
	public void setAlarmItems(Vector<CliAlarm> alarmItems) {		this.alarmItems = alarmItems;	}
	public Vector<CliAlarm> getAlarmLibItems() {		return alarmLibItems;	}
	public void setAlarmLibItems(Vector<CliAlarm> alarmLibItems) {		this.alarmLibItems = alarmLibItems;	}
	
	public Vector<Unit> getUnitItems() {		return unitItems;	}
	public void setUnitItems(Vector<Unit> unitItems) {		this.unitItems = unitItems;	}
	public Vector<Unit> getUnitLibItems() {		return unitLibItems;	}
	public void setUnitLibItems(Vector<Unit> unitLibItems) {		this.unitLibItems = unitLibItems;	}
	
	public Vector<UnitGroup> getUnitGroupItems() {		return unitGroupItems;	}
	public void setUnitGroupItems(Vector<UnitGroup> unitGroupItems) {		this.unitGroupItems = unitGroupItems;	}
	public Vector<UnitGroup> getUnitGroupLibItems() {		return unitGroupLibItems;	}
	public void setUnitGroupLibItems(Vector<UnitGroup> unitGroupLibItems) {		this.unitGroupLibItems = unitGroupLibItems;	}

	public Vector<CliNetHost> getNetHostItems() {		return netHostItems;	}
	public void setNetHostItems(Vector<CliNetHost> netHostItems) {		this.netHostItems = netHostItems;	}
	public Vector<CliNetHost> getNetHostLibItems() {		return netHostLibItems;	}
	public void setNetHostLibItems(Vector<CliNetHost> netHostLibItems) {		this.netHostLibItems = netHostLibItems;	}

	public Vector<CliNetInput> getNetInputItems() {		return netInputItems;	}
	public void setNetInputItems(Vector<CliNetInput> netItemItems) {		this.netInputItems = netItemItems;	}
	public Vector<CliNetInput> getNetInputLibItems() {		return netInputLibItems;	}
	public void setNetInputLibItems(Vector<CliNetInput> netItemLibItems) {		this.netInputLibItems = netItemLibItems;	}

	public Vector<CliBus> getBusItems() { return busItems;	}
	public Vector<CliBus> getBusLibItems() { return busLibItems;	}
	public void setBusItems(Vector<CliBus> busItems) {		this.busItems = busItems;	}
	public void setBusLibItems(Vector<CliBus> busLibItems) {		this.busLibItems = busLibItems;	}

	public Vector<CliPipe> getPipeItems() { return pipeItems; }
	public Vector<CliPipe> getPipeLibItems() { return pipeLibItems; }

	public void setPipeItems(Vector<CliPipe> pipeItems) { this.pipeItems = pipeItems; }
	public void setPipeLibItems(Vector<CliPipe> pipeLibItems) { this.pipeLibItems = pipeLibItems; }

	public Vector<CliButtonGroup> getButtonGroupsItems() {		return buttonGroupsItems;	}
	public void setButtonGroupsItems(Vector<CliButtonGroup> items) {		buttonGroupsItems = items;	}



	
}
