package ru.dz.shipMaster.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import ru.dz.shipMaster.config.ConfigurationFactory;
import ru.dz.shipMaster.config.items.CliAlarm.NearStatus;
import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.data.DataSink;
import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.dev.DriverPort;
import ru.dz.shipMaster.dev.controller.ModBusException;
import ru.dz.shipMaster.dev.controller.Wago750;
import ru.dz.shipMaster.dev.debug.DriverDebugger;
import javax.swing.JCheckBox;

@SuppressWarnings("serial")
public class DebugFrame extends JFrame {
	protected static final Logger log = Logger.getLogger(DebugFrame.class.getName()); 

	private final DriverDebugger dd;
	//private JPanel framePanel = new JPanel(new GridLayout(1, 0));
	private JPanel framePanel = new JPanel(new GridBagLayout());
	
	private GridBagConstraints frameConst = new GridBagConstraints(-1, 1, 1, 1, 1, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 2, 2);
	
	private JPanel aIpanel = new JPanel(new GridLayout(0, 1));
	private JPanel aOpanel = new JPanel(new GridLayout(0, 1));
	private JPanel dIpanel = new JPanel(new GridLayout(0, 1));
	private JPanel dOpanel = new JPanel(new GridLayout(0, 1));

	private JPanel controlPanel = new JPanel(new GridBagLayout());  //  @jve:decl-index=0:visual-constraint="10,154"
	
	public DebugFrame(DriverDebugger dd) throws HeadlessException {
		this.dd = dd;

		setTitle("Debug window for "+dd.getDriver().getName());
		//setSize(400, 400);

		//add(new JScrollPane(panel));
		add(framePanel);

		framePanel.add(new JScrollPane(aIpanel),frameConst);
		framePanel.add(new JScrollPane(aOpanel),frameConst);
		framePanel.add(new JScrollPane(dIpanel),frameConst);
		framePanel.add(new JScrollPane(dOpanel),frameConst);
		
		frameConst.gridy = 0;
		frameConst.weighty = 0.05;
		frameConst.fill = GridBagConstraints.HORIZONTAL;
		framePanel.add(controlPanel,frameConst);

		
		
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		populateControlPanel();
		
		populateList();
	}

	private void populateControlPanel() {

		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 1;
		gridBagConstraints1.gridy = 0;
		showAbsentLabel = new JLabel();
		showAbsentLabel.setText("JLabel");
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		controlPanel.setSize(new Dimension(702, 96));
		controlPanel.add(getShowAbsentCheckBox(), gridBagConstraints);
		controlPanel.add(showAbsentLabel, gridBagConstraints1);
		
	}

	private void populateList()
	{
		
		Vector<DriverPort> ports = dd.getDriver().getPorts();
		
		aIpanel.removeAll();
		aOpanel.removeAll();
		dIpanel.removeAll();
		dOpanel.removeAll();
		
		for( DriverPort dp : ports )
		{
			ru.dz.shipMaster.config.items.CliParameter.Type type = dp.getType();
			
			boolean isBool = type == ru.dz.shipMaster.config.items.CliParameter.Type.Boolean;
			
			//if( dp.isUsed() || !dp.isAbsent() )
			{
				if(dp.getDirection() == Direction.Input)
				{
					if(isBool)		dIpanel.add( new ListItem(dp) );	
					else			aIpanel.add( new ListItem(dp) );
				}
				else
				{
					if(isBool)		dOpanel.add( new ListItem(dp) );
					else			aOpanel.add( new ListItem(dp) );
				}
			}
		}
		
		pack();
	}

	private static final int MINLEN = 20;
	private static String spaces = "                                                                      ";

	private JCheckBox showAbsentCheckBox = null;

	private JLabel showAbsentLabel = null; 
	
	class ListItem extends JPanel implements DataSink
	{
		JButton toggleButton = new JButton();
		JTextField dvalueField = new JTextField(6);
		
		
		
		private final DriverPort dp;
		public ListItem( final DriverPort dp ) {
			this.dp = dp;

			Direction direction = dp.getDirection();		
			boolean isIn = direction == Direction.Input;
			
			toggleButton.setSize(24, 24);
			toggleButton.setPreferredSize(new Dimension(24, 24));
			toggleButton.setMaximumSize(new Dimension(24, 24));
			
			setLayout(new GridLayout(1,0));
			
			String name = " "+dp.getName();
			
			String desc = dp.getGivenName();
			if(desc != null)
				name += " ("+desc+")";
			
			if(name.length() < MINLEN)
				name += spaces.substring(0, MINLEN-name.length());
				
			add(new JLabel( name ) );
			
			if(isIn) 
			add(toggleButton);
			
			toggleButton.addActionListener(new AbstractAction() {

				@Override
				public void actionPerformed(ActionEvent e) {
					
					//System.out.println("DebugFrame.ListItem.ListItem(...).new AbstractAction() {...}.actionPerformed() "+dp.getCurrentValue());
					
					if( dp.getCurrentValue() > 0.1 )
						dp.sendBooleanData(false);
					else
						dp.sendBooleanData(true);
				}
			});
			
			toggleButton.setText("1/0");
			toggleButton.setEnabled(isIn);
			
			
			dvalueField.setEnabled(isIn);
			dvalueField.setText(""+dp.getCurrentValue());
			add(dvalueField);
			dvalueField.addActionListener(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {				
					try {
						double val = Double.parseDouble(dvalueField.getText());
						dp.sendDoubleData(val);
					} catch(NumberFormatException e1)
					{
						dvalueField.setText(dp.getCurrentValue()+"");
					};
				}
			});

			//pack();
			
			dp.connectDebugger(this);
		}


		
		@Override
		protected void finalize() throws Throwable {
			dp.disconnectDebugger(this);
			super.finalize();
		}

		
		private double lastValue = -1;
		private void displayValue(double v)
		{
			// Or else it is impossible to change it manually 'cause it resets every time
			if(v != lastValue)
			{
				lastValue = v;
				
				dvalueField.setText(""+v);
				dvalueField.repaint(100);
			}
		}
		
		/*
		 * DataSink interface
		 */
		
		@Override
		public double getCurrentValue() {
			return 0;
		}

		@Override
		public double getMaximum() {
			return 0;
		}

		@Override
		public double getMinimum() {
			return 0;
		}

		@Override
		public void receiveImage(Image val) {
		}

		@Override
		public void receiveString(String val) {
			dvalueField.setText(val);
			//DebugFrame.this.repaint(100);
			dvalueField.repaint(100);
		}

		@Override
		public void setCurrent(double newCurr, NearStatus nearStatus) {
			displayValue(newCurr);
		}

		@Override
		public void setCurrent(double newCurr) {
			displayValue(newCurr);
		}

		@Override
		public void setLegend(String newLegend) {
		}

		@Override
		public void setMaximum(double max) {
		}

		@Override
		public void setMinimum(double min) {
		}

		@Override
		public void setUnits(String newUnits) {
		}
		
	}

	
	
	// -----------------------------------------------------------
	// test main 
	// -----------------------------------------------------------

	/**
	 * This method initializes showAbsentCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getShowAbsentCheckBox() {
		if (showAbsentCheckBox == null) {
			showAbsentCheckBox = new JCheckBox();
		}
		return showAbsentCheckBox;
	}

	public static void main(String args[]) throws IOException, ModBusException, CommunicationsException 
	{
		Wago750 w = new Wago750();

		w.setIpAddressStr("192.168.1.106");
		w.setDebug(true);
		//ConfigurationFactory.getConfiguration().getGeneral().debug = true;
		w.start();
	
		//w.stop();
	}
	

}
