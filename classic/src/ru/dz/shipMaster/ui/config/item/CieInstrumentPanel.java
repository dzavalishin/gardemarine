package ru.dz.shipMaster.ui.config.item;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ru.dz.shipMaster.DashBoard;
import ru.dz.shipMaster.config.Configuration;
import ru.dz.shipMaster.config.ConfigurationFactory;
import ru.dz.shipMaster.config.items.CliInstrument;
import ru.dz.shipMaster.config.items.CliInstrumentPanel;
import ru.dz.shipMaster.ui.DashComponent;
import ru.dz.shipMaster.ui.config.panel.ConfigPanelList;


public class CieInstrumentPanel extends SideFrameConfigItemEditor {

	private JTextField nameField = new JTextField();
	private JButton viewButton = new JButton();
	//private JButton addButton = new JButton();
	private JCheckBox verticalField = new JCheckBox();
	private JCheckBox absolutePositioningField = new JCheckBox();
	private JCheckBox snapToGridField = new JCheckBox();

	{
		snapToGridField.setSelected(true);
	}
	
	private final CliInstrumentPanel bean;
	
	private Vector<CliInstrument> instruments = new Vector<CliInstrument>();

	private Vector<CliInstrument> mainList = ConfigurationFactory.getConfiguration().getInstrumentItems();
	
	@SuppressWarnings("serial")
	class MList extends ConfigPanelList<CliInstrument>
	{

		public MList(String name, String tip, Class<CliInstrument> itemClass,
				Vector<CliInstrument> items, Vector<CliInstrument> libItems) {
			super(name, tip, itemClass, items, libItems);			
		}

		@Override
		protected void informAboutListSelection(int selectedIndex, Object itemType) 
		{
			highlight(selectedIndex);				
		};
		
	}

	private MList configPanelInstruments = new MList("Instruments","Edit instruments",CliInstrument.class, instruments, mainList); 

	
	Dimension panelSize = new Dimension();
	
	private JTextField sizeXField = new JTextField();
	private JTextField sizeYField = new JTextField(); 
	
	/**
	 * Create new editor for this bean. 
	 * @param bean Bean to edit.
	 */
	@SuppressWarnings("serial")
	public CieInstrumentPanel(CliInstrumentPanel bean) {
		super(new Dimension(10,10));
		
		this.bean = bean;

		// TO DO Hack!
		//bean.reapplyAbsPositions();
		
		discardSettings(); 
		//clist = new PropertiesEditItemList<CliWindowPanelConstraint>(constraints);

		viewButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) { showDemo(); }});
		viewButton.setText("Show");
		
		/*
		addButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) { addInstrument(); }});
		addButton.setText("Add");
		*/
		
		panel.add(new JLabel("name:"),consL);
		panel.add(nameField, consR);
		nameField.setColumns(20);		

		panel.add(new JLabel("Elements:"),consL);
		panel.add(configPanelInstruments,consR);

		panel.add(new JLabel("Vertical:"),consL);
		panel.add(verticalField,consR);
		
		panel.add(new JLabel("Absolute positions:"),consL);
		panel.add(absolutePositioningField,consR);
		
		panel.add(new JLabel("Snap to grid:"),consL);
		panel.add(snapToGridField,consR);
		
		panel.add(new JLabel("Size X:"),consL);
		panel.add(sizeXField,consR);

		panel.add(new JLabel("Size Y:"),consL);
		panel.add(sizeYField,consR);

		panel.add(new JLabel("Preview:"),consL);
		panel.add(viewButton, consR);

		//panel.add(new JLabel("Add:"),consL);
		//panel.add(addButton, consR);

		
		absolutePositioningField.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {				
				setDragDropEnabled(absolutePositioningField.isSelected());
				showDemo();
			}});
		
		snapToGridField.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				setSnapToGrid(snapToGridField.isSelected());				
			}});
		
		setupSideFrame("Panel preview");
		
		//DashBoard db = new DashBoard();
		//components.add(new CompassMeter(db,0,180,0.6f,0.9f,"test1","cm"));
		//components.add(new HalfGaugeMeter(db,0,180,0.6f,0.9f,"test2","mm"));
		showDemo();
	}


	@Override
	protected void informContainer() {		bean.informContainers(); }

	protected void addInstrument() {
		//DashComponent component = Configuration.SelectAndCreateDashComponent(panel);
		//components.add(component);
		CliInstrument inst = Configuration.SelectInstrument(panel);
		inst.getComponent().setEnabled(false);
		instruments.add( inst );
		showDemo();
	}

	/*private void setEnabledForInstrs(boolean enabled)
	{
		for( CliInstrument i : instruments )
			i.getComponent().setEnabled(enabled);
	}*/
	
	private Map<DashComponent,Integer> componentIndices;
	private static DashBoard testDashBoard = new DashBoard();
	
	
	protected void highlight(int selectedIndex) {
		//System.out.println("CieInstrumentPanel.highlight()"+selectedIndex);
		for( DashComponent key : componentIndices.keySet() )
		{
			int index = componentIndices.get(key);
			key.setMarked( index == selectedIndex );
		}
	}
	
	
	protected void showDemo() {
		JPanel demoPanel;
		//setEnabledForInstrs(false);
		componentIndices = new HashMap<DashComponent, Integer>();
		demoPanel = CliInstrumentPanel.createPanel(
				testDashBoard,
				componentIndices,
				instruments,
				verticalField.isSelected(),
				absolutePositioningField.isSelected(),
				//absolutePositions,
				panelSize, false);
		
		setSFPanel( demoPanel );		
	}

	@Override
	protected boolean checkLocation(Component c, Point p) {
		Integer index = componentIndices.get(c);
		if(index == null)
		{
			//log.severe("Unknown component dragged");
			return false;
		}
		/*if(absolutePositions.size() <= index)
			absolutePositions.setSize(index+1);
		absolutePositions.set(index, p);*/
		
		instruments.get(index).setAbsolutePosition(p);
		return true;
	}
	

	@Override
	public void applySettings() {
		//setEnabledForInstrs(true);
		
		bean.setName(nameField.getText());
		bean.setVertical(verticalField.isSelected());
		bean.setAbsolutePositioning(absolutePositioningField.isSelected());

		/*
		// TODO hack!
		Vector<Point> absolutePositions = new Vector<Point>(instruments.size());
		absolutePositions.setSize(instruments.size());
		for(int i = 0; i < instruments.size(); i++)
		{
			absolutePositions.add(i, instruments.get(i).getAbsolutePosition() );
		}
		bean.setAbsolutePositions(absolutePositions);
		*/
		
		// must be after setAbsolutePositions!
		bean.setInstruments(instruments);
		
		panelSize.width  = Integer.parseInt(sizeXField.getText());
		panelSize.height = Integer.parseInt(sizeYField.getText());
		bean.setPanelSize(panelSize);
	}

	@Override
	public void discardSettings() {
		//setEnabledForInstrs(true);
		
		nameField.setText( bean.getName() );
		instruments = bean.getInstruments();
		verticalField.setSelected( bean.isVertical() );
		
		absolutePositioningField.setSelected( bean.isAbsolutePositioning() );
		setDragDropEnabled(bean.isAbsolutePositioning());
		
		//absolutePositions.addAll( bean.getAbsolutePositions() );
		/*Vector<Point> apos = bean.getAbsolutePositions();
		
		for(int i = 0; i < instruments.size(); i++)
		{
			instruments.get(i).setAbsolutePosition(apos.get(i));
		}*/
		// XXX error
		//configPanelInstruments = new ConfigPanelList<CliInstrument>("Instruments","Edit instruments",CliInstrument.class, instruments, mainList);
		configPanelInstruments = new MList("Instruments","Edit instruments",CliInstrument.class, instruments, mainList);

		panelSize = bean.getPanelSize();
		
		sizeXField.setText(Integer.toString(panelSize.width));
		sizeYField.setText(Integer.toString(panelSize.height));
		
	}

	@Override
	public String getName() { return bean.getName(); }

	@Override
	public String getTypeName() { return "Instrument panel"; }


}
