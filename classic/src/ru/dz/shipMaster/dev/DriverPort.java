package ru.dz.shipMaster.dev;

import java.awt.Image;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.dz.shipMaster.config.ConfigListItem;
import ru.dz.shipMaster.config.GeneralConfigBean;
import ru.dz.shipMaster.config.items.CliConversion;
import ru.dz.shipMaster.config.items.CliParameter;
import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.data.AbstractMinimalDataSink;
import ru.dz.shipMaster.data.DataSink;
import ru.dz.shipMaster.data.DataSource;
import ru.dz.shipMaster.ui.config.item.CieDriverPort;

/**
 * General driver's in/out connection object.
 * @author dz
 */

public class DriverPort extends ConfigListItem {
	protected static final Logger log = Logger.getLogger(DriverPort.class.getName()); 

	private double lastValue = 0;
	private boolean haveLastValue = false;

	private final AbstractDriver driver;
	
	public boolean checkIsChanged( double val )
	{
		if(!haveLastValue)
		{
			lastValue = val;
			haveLastValue = true;
			return true;
		}
		
		boolean ret = val != lastValue;
		lastValue = val;
		return ret;
	}
	
	/**
	 * Default.
	 */
	public DriverPort(AbstractDriver driver)
	{
		this.driver = driver;
	}

	/*static {
    	Object o = new DriverPort();
    	BeanInfo info;
    	try {
    		info = Introspector.getBeanInfo(o.getClass());
    		PropertyDescriptor[] propertyDescriptors =
    			info.getPropertyDescriptors();
    		for (int i = 0; i < propertyDescriptors.length; ++i) {
    			PropertyDescriptor pd = propertyDescriptors[i];
    			//if (pd.getName().equals("text")) { pd.setValue("transient", Boolean.TRUE); }


    			System.out.println(
    			String.format("%s <%s >%s", pd.getName(), pd.getReadMethod(), pd.getWriteMethod())		
    			);
    		}
    	} catch (IntrospectionException e) {
    		e.printStackTrace();
    	}

    }*/

	private Type type; // = Type.Numeric;
	private Direction direction; // = Direction.Input;

	/**
	 * Port corresponds to absent hardware. System will not remove such ports
	 * hoping for hardware to return some time.
	 */
	private boolean absent = true;

	/**
	 * This integer value is used to map port to driver's internals. (like minor in Unix)
	 */
	private int internalPortIndex;

	/**
	 * This is the non-modifiable name given by driver itself and determined
	 * by hardware. It must be good to locate actual device's property using this name.
	 */
	private String hardwareName;

	/**
	 * This name is to be edited by user and will, generally, describe parameter
	 * or device, monitored or controlled through this port.
	 */
	private String givenName;

	/**
	 * Converter used to preprocess data.
	 */
	private CliConversion	convertor;

	/**
	 * Target parameter to communicate with.
	 */
	private CliParameter target;

	/**
	 * Verbose description of port, if applicable. Mostly for a human.
	 */
	private String description;

	// Algorithms


	@Override
	public void destroy() {
		convertor = null;
		convertedOut = null;
		target = null;
		dialog = null;			
	}


	/**
	 * Place ports in array by internalPortIndex. Duplicates are silently killed.
	 * @param ports Ports to sort.
	 */
	public static void sortPorts(Vector<DriverPort> ports) {
		Vector<DriverPort> in = new Vector<DriverPort>(ports);

		ports.clear();
		for( DriverPort p : in)
		{
			if(p == null)
			{
				log.log(Level.SEVERE,"NULL port killed!");
				continue;
			}
			if(p.internalPortIndex >= ports.size())
				ports.setSize(p.internalPortIndex+1);
			DriverPort oldPort = ports.set(p.internalPortIndex, p);
			if(oldPort != null)
				log.log(Level.SEVERE,"Duplicate port killed: "+oldPort);
		}
	}

	/**
	 * Extracts port from sorted array by index or creates and puts it inside. 
	 * NB! Port absent state is cleared!
	 * @param ports Ports to find or add to.
	 * @param index Number of port to get or create.
	 * @param direction Direction to set on port.
	 * @param type Data type to set on port.
	 * @param hardwareName Hardware name to assign to port.
	 * @return Found or created port.
	 */
	public static DriverPort getPort(AbstractDriver d, Vector<DriverPort> ports, int index, 
			Direction direction, Type type, String hardwareName )
	{
		DriverPort p = getPort( d, ports, index );
		p.setDirection(direction);
		p.setType(type);
		p.setHardwareName(hardwareName);
		return p;
	}

	/**
	 * Extracts port from sorted array by index or creates and puts it inside. 
	 * NB! Port absent state is cleared!
	 * @param ports Ports to find or add to.
	 * @param index Number of port to get or create.
	 * @return Found or created port.
	 */
	public static DriverPort getPort(AbstractDriver d, Vector<DriverPort> ports, int index )
	{
		DriverPort p = null;
		if(index < ports.size())
			p = ports.get(index);
		if(p == null)
		{
			if(index >= ports.size())
				ports.setSize(index+1);
			p = new DriverPort(d);
			ports.set(index, p);
			p.internalPortIndex = index;
		}

		if(p.internalPortIndex != index)
		{
			log.log(Level.SEVERE,"Port index fixed: "+p);
			p.internalPortIndex = index;
		}
		p.absent = false;
		return p;
	}

	/**
	 * Mark all them as absent. Usually called by driver before re-enabling them one by one
	 * during hardware check.
	 * @param ports Vector of ports to make absent.
	 */
	public static void absentize(Vector<DriverPort> ports) {
		for( DriverPort p : ports )
		{
			if(p != null)
				p.setAbsent(true);
		}
	}
	// toString

	@Override
	public String toString()
	{
		if(givenName == null)
			return String.format("%s, %s %s%s", 
					hardwareName,
					type, direction, absent ? " (absent)" : ""
			);
		else
			return String.format("%s (%s), %s %s%s", 
					givenName, hardwareName,
					type, direction, absent ? " (absent)" : ""
			);
	}

	// Connection
	protected PortDataSource portDataSource = new PortDataSource(0,0,getName(),"");
	protected PortDataOutput portDataOutput = null;

	AbstractMinimalDataSink dataSink = new AbstractMinimalDataSink(){
		//@Override
		public void setCurrent(double value) {
			if(GeneralConfigBean.debug && driver.isDebug())
				System.out
						.println("Port "+internalPortIndex+" "+hardwareName+" = "+value);
			if(portDataOutput != null)
				portDataOutput.receiveDoubleData(value);
			
			// This is for debugging - debugger window subscribes to portDataSource
			portDataSource.setValue(value);
		}

		@Override
		public void receiveImage(Image val) {
			if(portDataOutput != null)
				portDataOutput.receiveImageData(val);

			// This is for debugging - debugger window subscribes to portDataSource
			portDataSource.setValue(val);
		}

		@Override
		public void receiveString(String val) {
			if(portDataOutput != null)
				portDataOutput.receiveStringData(val);

			// This is for debugging - debugger window subscribes to portDataSource
			portDataSource.setValue(val);
		}
	};

	private DataSource convertedOut; // Store out for detach

	/**
	 * Connect port to its parameter.
	 */
	public void connectToParameter() {
		//if(absent)			return; // Absent ports are not connected, right?
		CliParameter p = getTarget();
		if(p == null)
			return;

		switch(direction)
		{
		case Input:
			DataSource convertedIn = portDataSource;
			if(convertor != null)
				convertedIn = convertor.createChain(convertedIn);
			p.setDataSource(convertedIn);
			break;

		case Output:			
			//p.attachMeter(dataSink);
			convertedOut = p.getDataSource();
			if(convertor != null)
				convertedOut = convertor.createChain(convertedOut);
			convertedOut.addMeter(dataSink);
			break;

		}
	}

	/**
	 * Called on driver stop. Disconnects driver from system.
	 */
	public void disconnectFromParameter() {
		CliParameter p = getTarget();
		if(p == null)
			return;

		switch(direction)
		{
		case Input:
			p.setDataSource(null);
			break;
		case Output:
			if(convertedOut != null)
			{
				convertedOut.removeMeter(dataSink);
				convertedOut = null;
			}
			break;
		}

	}

	/**
	 * To be called by driver to translate data.
	 * @param val Numeric data to send.
	 */
	public void sendDoubleData(double val) {portDataSource.setValue(val); }

	/**
	 * To be called by driver to translate data.
	 * @param val Boolean data to send.
	 */
	public void sendBooleanData(boolean val) {		sendDoubleData(val ? 100 : 0);	}

	/**
	 * To be called by driver to translate data.
	 * @param val Image (picture) data to send.
	 */
	public void sendImageData(Image val) { portDataSource.setValue(val); }

	/**
	 * To be called by driver to translate data.
	 * @param val String data to send.
	 */
	public void sendStringData(String val) {portDataSource.setValue(val);	}


	// Dialog

	private CieDriverPort dialog = null;

	@Override
	public void displayPropertiesDialog() {
		if(dialog == null) dialog = new CieDriverPort(this);
		dialog.displayPropertiesDialog(); 
	}

	@Override
	public String getName() { return hardwareName; }

	// Getters/Setters

	//public PortDataOutput getPortDataOutput() {		return portDataOutput;	}

	/**
	 * Used internally by driver to attach actual output implementation to the port.
	 * @param portDataOutput Object of overridden PortDataOutput class. 
	 */
	public void setPortDataOutput(PortDataOutput portDataOutput) {		this.portDataOutput = portDataOutput;	}


	public CliConversion getConvertor() {		return convertor;	}
	public void setConvertor(CliConversion convertor) {		this.convertor = convertor;	}

	/**
	 * @return True if port is not mapped to any existing hardware. It is assumed that
	 * if hardware will come back, driver will do its best to map it back to the same port. 
	 */
	public boolean isAbsent() {		return absent;	}

	/**
	 * @param absent Absent state for port. Supposed to be set by driver only. Will be
	 * reset at any moment as driver wishes.
	 */
	public void setAbsent(boolean absent) {		this.absent = absent;	}

	/**
	 * @return Port direction.
	 */
	public Direction getDirection() {		return direction;	}
	/**
	 * Set port direction. Supposed to be called by driver only.
	 * @param direction Direction to set.
	 */
	public void setDirection(Direction direction) {		this.direction = direction;	}

	/**
	 * @return User-defined name for this port.
	 */
	public String getGivenName() {		return givenName;		}
	/**
	 * @param givenName User-defined name for this port. Some drivers will use it for own
	 * purposes. For example, system tray driver takes parameter name from this field. 
	 */
	public void setGivenName(String givenName) {		this.givenName = givenName;	}

	/**
	 * Returns name of hardware this port is assigned to. Must be descriptive
	 * enough for human to be able to find this port on the device. For example,
	 * "Pin A12" is OK. 
	 * @return Hardware name.
	 */
	public String getHardwareName() {		return hardwareName;	}
	/**
	 * Set hardware name for port. Supposed to be called by driver only.
	 * @param hardwareName Name of underlying hardware, if any. 
	 */
	public void setHardwareName(String hardwareName) {		
		this.hardwareName = hardwareName;
		/*if(givenName == null || givenName.length() == 0)
			givenName = hardwareName;*/
	}

	/**
	 * Internal number of port in the driver instance. Used to map port to device hardware.
	 * @return Port index.
	 */
	public int getInternalPortIndex() {		return internalPortIndex;	}
	/**
	 * Called by static methods of this class, do not touch ever. Even from driver. 
	 * Driver must use static <code>getPort</code> to produce port with given index.
	 * @param internalPortIndex Index to assign to port.
	 */
	public void setInternalPortIndex(int internalPortIndex) {		this.internalPortIndex = internalPortIndex;	}

	/**
	 * @return Type of data this port is transferring.
	 */
	public Type getType() {		return type;	}
	/**
	 * Supposed to be called by driver only.
	 * @param type Port type (data format).
	 */
	public void setType(Type type) {		this.type = type;	}

	/**
	 * Target parameter for the port. Assigned by UI code. Note that it will not
	 * have effect until connectToParameter is called.
	 * @param target Parameter to connect to.
	 */
	public void setTarget(CliParameter target) { this.target = target; }
	/**
	 * @return Current target parameter.
	 */
	public CliParameter getTarget() {		return target;	}

	/**
	 * Returns description of the port for human to read. Similar to hardware name, but longer.
	 * @return Description text.
	 */
	public String getDescription() {		return description;	}
	/**
	 * Supposed to be called by driver only.
	 * @param description Description of what this port is for.
	 */
	public void setDescription(String description) {		this.description = description;	}


	public boolean isUsed() {
		return target != null;
	}

	public double getCurrentValue() {
		return portDataSource.getCurrentValue();
	}

	
	public void connectDebugger(DataSink m)
	{
		portDataSource.addMeter(m);
	}

	public void disconnectDebugger(DataSink m)
	{
		portDataSource.removeMeter(m);
	}
	

}
