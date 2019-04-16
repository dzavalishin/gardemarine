/**
 * 
 */
package ru.dz.shipMaster.ui.config.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ru.dz.shipMaster.Version;
import ru.dz.shipMaster.config.ConfigurationFactory;
import ru.dz.shipMaster.ui.config.ConfigPanel;
import ru.dz.shipMaster.ui.config.Messages;

/**
 * @author dz
 *
 */
@SuppressWarnings("serial")
public class ConfigPanelColors extends ConfigPanel {

	/**
	 * 
	 */
	public ConfigPanelColors() {
		discardChanges();

		cons.gridx = 0;
		cons.gridy = 0;
		JPanel leftPanel = new JPanel(new GridBagLayout() );
		add(leftPanel, cons);

		createLeftPanel(leftPanel);

		cons.gridx = 1;
		cons.gridy = 0;
		JPanel rightPanel = new JPanel(new GridBagLayout() );
		add(rightPanel, cons);

		createRightPanel(rightPanel);

		cons.gridx = 0;
		cons.gridy = 1;
		cons.gridwidth = 2;
		cons.weighty = 0;
		JPanel botPanel = new JPanel(new GridBagLayout() );
		add(botPanel, cons);

		createBottomPanel(botPanel);
	}


	/** @return panel name for tab */
	@Override
	public String getName() { return Messages.getString("ConfigPanelColors.PanelName"); } //$NON-NLS-1$

	/** @return panel tip for tab */
	@Override
	public String getTip() { return Messages.getString("ConfigPanelColors.PanelDescription"); } //$NON-NLS-1$

	
	
	
	
	
	
	
	
	
	private void createLeftPanel(JPanel panel) {
		GridBagConstraints consL;
		GridBagConstraints consR;


		{
			{
				GridBagConstraints c = new GridBagConstraints();
				c.weightx = 1;
				c.weighty = 1;
				c.gridx = 0;
				c.gridy = GridBagConstraints.RELATIVE;
				c.anchor = GridBagConstraints.NORTHEAST;
				c.ipadx = 4;
				c.ipady = 2;
				//c.insets = new Insets(4,4,4,4);
				c.insets = new Insets(2,2,2,2);
				consL = c;
			}

			{
				GridBagConstraints c = new GridBagConstraints();
				c.weightx = 1;
				c.weighty = 1;
				c.gridx = 1;
				c.gridy = GridBagConstraints.RELATIVE;
				c.anchor = GridBagConstraints.NORTHWEST;
				c.ipadx = 4;
				c.ipady = 2;
				//c.insets = new Insets(4,4,4,4);
				c.insets = new Insets(2,2,2,2);
				consR = c;
			}

		}

		{
			panel.add(new JLabel(Messages.getString("ConfigPanelGeneral.Version")), consL); //$NON-NLS-1$

			JTextField vf = new JTextField(Version.VERSION+" (bld "+Version.BUILD+")");
			vf.setEditable(false);
			panel.add(vf, consR);
		}

/*
		panel.add(new JLabel("Extend width on resize"), consL); 
		panel.add( globalExtendWidthField, consR );

		panel.add(new JLabel("Extend height on resize"), consL); 
		panel.add( globalExtendHeightField, consR );
*/
		
		/*		

		panel.add(new JLabel(Messages.getString("GeneralPanel.DemoMode")+": "), consL);  //$NON-NLS-1$//$NON-NLS-2$
		panel.add(demoModeField, consR);

		String simTip = Messages.getString("ConfigPanelGeneral.SimulateDescription"); //$NON-NLS-1$
		panel.add(new JLabel(Messages.getString("ConfigPanelGeneral.Simulate")), consL); //$NON-NLS-1$
		panel.add(simulationModeField, consR);
		simulationModeField.setToolTipText(simTip);

		panel.add(new JLabel(Messages.getString("ConfigPanelGeneral.ShowSplash")), consL); //$NON-NLS-1$
		panel.add(splashScreenEnabledField, consR);				

*/		
		
	}

	
	private void createRightPanel(JPanel panel) {
		GridBagConstraints consL;
		GridBagConstraints consR;

		{
			{
				GridBagConstraints c = new GridBagConstraints();
				c.weightx = 1;				c.weighty = 1;
				c.gridx = 0;				c.gridy = GridBagConstraints.RELATIVE;
				c.anchor = GridBagConstraints.NORTHEAST;
				c.ipadx = 4;				c.ipady = 2;
				//c.insets = new Insets(4,4,4,4);
				c.insets = new Insets(2,2,2,2);
				consL = c;
			}

			{
				GridBagConstraints c = new GridBagConstraints();
				c.weightx = 1;				c.weighty = 1;
				c.gridx = 1;				c.gridy = GridBagConstraints.RELATIVE;
				c.anchor = GridBagConstraints.NORTHWEST;
				c.ipadx = 4;				c.ipady = 2;
				//c.insets = new Insets(4,4,4,4);
				c.insets = new Insets(2,2,2,2);
				consR = c;
			}

		}


		panel.add(new JLabel(Messages.getString("Color.GlobalBG")), consL); //$NON-NLS-1$
		panel.add( new SelectorItem("", Color.YELLOW), consR );
		panel.add(new JLabel(Messages.getString("Color.GlobalFG")), consL); //$NON-NLS-1$
		panel.add( new SelectorItem("", Color.RED), consR );
		
		panel.add(new JLabel("Meter scale color"), consL);
		panel.add( new SelectorItem("", Color.RED), consR );
		panel.add(new JLabel("Meter tick color"), consL);
		panel.add( new SelectorItem("", Color.RED), consR );
		panel.add(new JLabel("Meter indicator color"), consL);
		panel.add( new SelectorItem("", Color.RED), consR );

		panel.add(new JLabel("Warning line color"), consL);
		panel.add( new SelectorItem("", Color.RED), consR );
		panel.add(new JLabel("Critical line color"), consL);
		panel.add( new SelectorItem("", Color.RED), consR );
	
	}
	
	
	
	private void createBottomPanel(JPanel panel) {
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = GridBagConstraints.RELATIVE;
		c.gridy = 0;
		c.anchor = GridBagConstraints.NORTH;
		c.ipadx = 4;
		c.ipady = 2;
		c.insets = new Insets(4,4,4,4);


		JButton applyButton = new JButton();
		applyButton.addActionListener(new AbstractAction() { public void actionPerformed(ActionEvent e) { applyChanges(); }});
		applyButton.setText(Messages.getString("ConfigPanelGeneral.ApplyButton"));		 //$NON-NLS-1$
		panel.add(applyButton, c);

		JButton discardButton = new JButton();
		discardButton.addActionListener(new AbstractAction() { public void actionPerformed(ActionEvent e) { discardChanges(); }});
		discardButton.setText(Messages.getString("ConfigPanelGeneral.DiscardButton"));		 //$NON-NLS-1$
		panel.add(discardButton, c);
		
        applyButton.setIcon(new ImageIcon(ConfigurationFactory.getVisualConfiguration().getApplyIcon()));
        discardButton.setIcon(new ImageIcon(ConfigurationFactory.getVisualConfiguration().getDiscardIcon()));
		
	}

	
	
	
	
	
	
	protected void applyChanges() {
	}

	private void discardChanges() {
	}
	

	
	
	class SelectorItem extends JPanel 
	{
		
		private final String name;
		private Color oldColor;
		private JPanel testPanel;
		private JButton defButton;
		private JButton setButton;

		public SelectorItem( String name, Color oldColor ) 
		{
			this.name = name;
			this.oldColor = oldColor;

			setLayout(new FlowLayout());
			
			add(new JLabel(name));
			
			setButton = new JButton("Set");
			add( setButton );
			
			defButton = new JButton("Default");
			add( defButton );
			
			testPanel = new JPanel();
			add( testPanel );
			testPanel.setMaximumSize(new Dimension(40,20));
			
					
			//pack();
			
			testPanel.setBackground(oldColor);
			
			setButton.addActionListener(new ActionListener() {				
				@Override
				public void actionPerformed(ActionEvent e) { setButtonPressed(); }
			});
			
			defButton.addActionListener(new ActionListener() {				
				@Override
				public void actionPerformed(ActionEvent e) { defButtonPressed(); }
			});
			
			
			
		}
		
		protected void defButtonPressed() {
			
		}

		protected void setButtonPressed() {
			Color color = JColorChooser.showDialog(this, name, oldColor);
			setColor(color);
		}

		void setColor( Color c )
		{
			oldColor = c;
			testPanel.setBackground(oldColor);
		}
		
		
	}
	
	
	
}
