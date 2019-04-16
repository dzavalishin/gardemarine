package ru.dz.shipMaster.ui.config.item;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import ru.dz.shipMaster.DashBoard;
import ru.dz.shipMaster.ScreenManager;
import ru.dz.shipMaster.config.Configuration;
import ru.dz.shipMaster.config.ConfigurationFactory;
import ru.dz.shipMaster.config.items.CliInstrumentPanel;
import ru.dz.shipMaster.config.items.CliWindow;
import ru.dz.shipMaster.config.items.CliWindowPanelConstraint;
import ru.dz.shipMaster.config.items.CliWindowStructure;
import ru.dz.shipMaster.ui.MainFrame;
import ru.dz.shipMaster.ui.config.ConfigItemEditor;
import ru.dz.shipMaster.ui.config.panel.ConfigPanelList;

public class CieWindow extends ConfigItemEditor {
	private final CliWindow bean;

	private JButton hideWindowButton = new JButton(); 
	private JButton showWindowButton = new JButton(); 
	private JButton showFullScreenWindowButton = new JButton(); 
	
	private JButton selectStructureButton = new JButton();
	
	private JTextField nameField = new JTextField("Name");
	private JTextField windowNumberFiled = new JTextField("");
	private JCheckBox fullscreenField = new JCheckBox("");
	private JComboBox screenNumberFiled = setupScreenList();
	private JCheckBox autostartField = new JCheckBox("");

	private Vector<CliInstrumentPanel> panels = new Vector<CliInstrumentPanel>();
	private Vector<CliInstrumentPanel> mainList = ConfigurationFactory.getConfiguration().getInstrumentPanelItems();
	private ConfigPanelList<CliInstrumentPanel> configPanelInstrumentPanels = new ConfigPanelList<CliInstrumentPanel>("Panels","Edit panels",CliInstrumentPanel.class, panels, mainList); 
	
	
	private MainFrame frame = new MainFrame();

	private JLabel structureLabel;
	

	@SuppressWarnings("serial")
	public CieWindow(CliWindow bean) {
		this.bean = bean;
		
		frame.setDimmerEnabled(false); // do not mess with dimmer
		
		discardSettings(); // load initial settings
		
		nameField.setColumns(20);
		panel.add(new JLabel("Name:"),consL);
		panel.add(nameField,consR);
		
		panel.add(new JLabel("Autostart:"),consL);
		panel.add(autostartField,consR);

		panel.add(new JLabel("Fullscreen:"),consL);
		panel.add(fullscreenField,consR);

		panel.add(new JLabel("Display:"),consL);
		panel.add(screenNumberFiled,consR);

		panel.add(new JLabel("Window No.:"),consL);
		panel.add(windowNumberFiled,consR);
		
		panel.add(new JLabel("Panels:"),consL);
		panel.add(configPanelInstrumentPanels,consR);
		
		panel.add(new JLabel("Preview:"),consL);
		panel.add(hideWindowButton,consR);
		panel.add(showWindowButton,consL);
		panel.add(showFullScreenWindowButton,consR);

		panel.add(selectStructureButton,consL);
		panel.add(structureLabel = new JLabel(""),consR);
		
		hideWindowButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				hidePreview();
			}});
		
		showWindowButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				showPreview();
			}});
		
		showFullScreenWindowButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				showPreviewFS();
			}});
		
		selectStructureButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				selectStructure();
			}});
		
		hideWindowButton.setText("Hide");
		showWindowButton.setText("Show");
		showFullScreenWindowButton.setText("FullScreen");
		selectStructureButton.setText("Structure");
		
		pack();
	
	
		// Now for the mouse tracking
		
		frame.addMouseMotionListener(mmListener);
		frame.addMouseListener(mListener);
	}

	private MouseListener mListener;
	{
		mListener = new MouseListener() {

			public void mouseClicked(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e)  {}

			public void mousePressed(MouseEvent e) {
				//Component c = frame.getContentPane().getComponentAt(e.getPoint());
				//c.setVisible(!c.isVisible());
			}

			public void mouseReleased(MouseEvent e) {
			}};
	}
	private MouseMotionListener mmListener;
	{
		mmListener = new MouseMotionListener() {

			public void mouseDragged(MouseEvent e) {
				//log.log(Level.SEVERE, "Drag: "+e);
			}

			public void mouseMoved(MouseEvent e) {
				//log.log(Level.SEVERE, "Move: "+e);				
			}};
	}
	
	private CliWindowStructure structure;
	private void selectStructure()
	{
		//Frame frame= JOptionPane.getFrameForComponent(panel);
		//SelectFrame<CliWindowStructure> sf = new SelectFrame<CliWindowStructure>(ConfigurationFactory.getConfiguration().getWindowStructureItems(), frame, "Select window structure");
		//sf.setVisible(true);
		CliWindowStructure result = Configuration.selectWindowStructure(panel);//sf.getResult();
		structureLabel.setText(result.getName());
		structure = result;		
	}
	
	private int selectedScreenNumber()
	{
		return (Integer)screenNumberFiled.getSelectedItem();
	}
	
	// TO DO must be private or be in CliWindow!
	public JPanel composeWindow(DashBoard db, boolean debugViev)
	{
		JPanel demoPanel = new JPanel(new GridBagLayout());
		Vector<JComponent> components = new Vector<JComponent>();
		
		CliWindowStructure myStruct = structure;
		
		if(myStruct == null)
		{
			log.log( Level.SEVERE, "Window structure is not set, using default");
			myStruct = CliWindowStructure.createDefaultStructure(); 
		}
		
		int i = 0;
		for( CliInstrumentPanel panel : panels )
		{
			JPanel p = new JPanel(new BorderLayout());
			
			CliWindowPanelConstraint constraint = myStruct.getConstraints().get(i);
			if(constraint.isVisible())
				p.add(panel.createPanel(db), BorderLayout.CENTER);
			else
				p.add(new JLabel(""), BorderLayout.CENTER);
			
			if(debugViev) p.setBorder(new LineBorder(Color.RED,1));
			components.add(p);
			//log.severe("Adding panel ");
			//components.add(panel.createPanel());
			i++;
		}

		for( ; i < 10; i++ )
		{
			JLabel l = new JLabel(" ("+myStruct.getConstraints().get(i).getId()+") ");
			JPanel p = new JPanel(new BorderLayout());
			
			//l.setBackground(colors[i]);
			//l.setForeground(colors[i]);
			
			p.add(l, BorderLayout.CENTER);
			p.setBorder(new LineBorder(Color.BLACK,1));
			
			components.add(p);
		}
		
		//applySettings(); // XX X B UG!
		myStruct.applyConstraints(demoPanel, components);
		return demoPanel;
	}
	
	private DashBoard testDashBoard = new DashBoard();
	protected void showPreviewFS() {
		
		frame.setContentPane(composeWindow(testDashBoard, true));
		frame.pack();

		frame.setVisible(false); 
		ScreenManager.goFullScreen(frame, selectedScreenNumber()); 
		}

	protected void showPreview() {
		frame.setContentPane(composeWindow(testDashBoard, true));
		frame.pack();

		frame.setVisible(false); 
		frame.setVisible(true); 
		ScreenManager.moveToScreen(frame, selectedScreenNumber() );
		}

	private void hidePreview() 
	{ 
		frame.setVisible(false); 
	}
	
	protected void setTitle()
	{
		super.setTitle();		
		frame.setTitle("Dashboard: "+getName()); //$NON-NLS-1$		
	}

	@Override
	protected void informContainer() {		bean.informContainers(); }

	@Override
	public void applySettings() {
		bean.setName( nameField.getText() );
		bean.setAutostart(autostartField.isSelected());
		bean.setFullscreen( fullscreenField.isSelected() );
		
		try { bean.setScreenNumber( (Integer)screenNumberFiled.getSelectedItem() ); }
		catch(Throwable e) { bean.setScreenNumber(0); }

		try { bean.setWindowNumber( Integer.parseInt(windowNumberFiled.getText()) ); }
		catch(Throwable e) { bean.setWindowNumber(0); }
		
		bean.setStructure(structure);
		bean.setPanels(panels);
		setTitle();
	}

	@Override
	public void discardSettings() {
		nameField.setText(bean.getName());
		autostartField.setSelected(bean.isAutostart());
		fullscreenField.setSelected(bean.isFullscreen());
		int screenNumber = bean.getScreenNumber();
		int select = -1;
		for(int i = 0; i < screens.size(); i++)
			if( screenNumber == (Integer)(screens.get(i)) )
				select = i;
		screenNumberFiled.setSelectedIndex(select);
		windowNumberFiled.setText(""+bean.getWindowNumber());
		structure = bean.getStructure();
		
		//panels = bean.getPanels();
		// X XX error
		//configPanelInstrumentPanels = new ConfigPanelList<CliInstrumentPanel>("Panels","Edit panels",CliInstrumentPanel.class, panels, mainList);
		
		panels.clear();
		panels.addAll(bean.getPanels());
		
		setTitle();
	}

	private Vector<Integer> screens;
	private JComboBox setupScreenList()
	{
		int sn = ScreenManager.countScreens();
		screens = new Vector<Integer>();
		for( int i = 0; i < sn; i++ )
			screens.add(i);
		return new JComboBox(screens);
	}
	
	@Override
	public String getTypeName() { return "Window"; }

	@Override
	public String getName() { return bean.getName(); }
}
