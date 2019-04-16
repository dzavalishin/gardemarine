package ru.dz.shipMaster.ui.config;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import ru.dz.shipMaster.config.ConfigurationFactory;

public abstract class ConfigItemEditor {
    protected static final Logger log = Logger.getLogger(ConfigItemEditor.class.getName()); 

	
	protected JFrame frame = new JFrame();
	//protected String name = "Undefined item";
	//protected JPanel panel = new JPanel(new GridLayout(0,2) );
	protected JPanel panel = new JPanel( );
	
	private JPanel savePanel = new JPanel(new FlowLayout() );
	private JButton applyButton = new JButton();
	private JButton discardButton = new JButton();


	protected final GridBagConstraints consL;
	protected final GridBagConstraints consR;


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
			c.insets = new Insets(4,4,4,4);
			c.fill = GridBagConstraints.BOTH;
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
			c.insets = new Insets(4,4,4,4);
			consR = c;
		}
		
	}

	@SuppressWarnings("serial")
	protected ConfigItemEditor()
	{
		//panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		panel.setLayout(new GridBagLayout());
		//panel.setBorder(new EmptyBorder(4,4,4,4));
		//panel.setBorder(new EmptyBorder(8,8,8,8));

		
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setMinimumSize(new Dimension(240,100));
		
        //frame.getContentPane().add(panel, BorderLayout.CENTER);		
        //frame.getContentPane().add(savePanel, BorderLayout.SOUTH);

		final JPanel tempPanel = new JPanel( new BorderLayout() );
		frame.getContentPane().add(tempPanel, BorderLayout.CENTER);
		tempPanel.add(panel,BorderLayout.CENTER);
		tempPanel.add(savePanel,BorderLayout.SOUTH);
		
		//tempPanel.setBorder(new EmptyBorder(4,4,4,4));
		tempPanel.setBorder(new EmptyBorder(8,8,8,8));
		
        Dimension minButtonSize = new Dimension(110,22);
        applyButton.setMinimumSize(minButtonSize);
        discardButton.setMinimumSize(minButtonSize);
        applyButton.setPreferredSize(minButtonSize);
        discardButton.setPreferredSize(minButtonSize);
        
        applyButton.setAction(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				applySettings();
				informContainer();
				frame.setVisible(false);
			}});

        frame.getRootPane().setDefaultButton(applyButton);
        
        discardButton.setAction(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				discardSettings();
				frame.setVisible(false);
			}});

        KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");
        Action actionListener = new AbstractAction() {
          public void actionPerformed(ActionEvent actionEvent) {
            frame.setVisible(false);
          }
        };
        InputMap inputMap = frame.getRootPane()
            .getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(stroke, "ESCAPE");
        frame.getRootPane().getActionMap().put("ESCAPE", actionListener);
        
        applyButton.setText(Messages.getString("ConfigListItem.Apply")); savePanel.add(applyButton); //$NON-NLS-1$
        discardButton.setText(Messages.getString("ConfigListItem.Discard")); savePanel.add(discardButton);      //$NON-NLS-1$
        
        applyButton.setIcon(new ImageIcon(ConfigurationFactory.getVisualConfiguration().getApplyIcon()));
        discardButton.setIcon(new ImageIcon(ConfigurationFactory.getVisualConfiguration().getDiscardIcon()));
        
	}
	
	// Helpers for children
	
	protected void pack() { frame.pack(); }
	
	protected InputVerifier doubleInputVerifier = new InputVerifier(){		
		@Override
		public boolean verify(JComponent input) {
			JTextField tf = (JTextField) input;
			String text = tf.getText();
			try
			{
				Double.parseDouble(text);
				return true;
			}
			catch(Throwable e){
				return false;
			}
		}};
	
	
	// Dialog
	
	private boolean dialogNotLoaded = true;
	public void displayPropertiesDialog()
	{
		if(dialogNotLoaded)
		{
			dialogNotLoaded  = false;
			discardSettings();
		}
		
		discardSettings();
		
		setTitle();
		frame.setLocationByPlatform(true);
        frame.pack();        
        frame.setVisible(true);
	}

	protected void setTitle()
	{
		frame.setTitle(getTypeName()+Messages.getString("ConfigListItem.Colon")+getName()); //$NON-NLS-1$		
	}

	@Override
	public String toString() { return getName(); }
	
	/**
	 * Returns name of item instance.
	 * @return Instance name string.
	 */
	public abstract String getName(); // { return Messages.getString("ConfigListItem.UndefItem"); } //$NON-NLS-1$

	/**
	 * Returns name of subclass's item type, like user, alarm station, etc
	 * @return constant name for each subclass
	 */
	public abstract String getTypeName(); // { return Messages.getString("ConfigListItem.UndefItemType"); } //$NON-NLS-1$
	
	/**
	 * Apply (actualize) current settings.
	 *
	 */
	public abstract void applySettings();

	
	protected abstract void informContainer();
	
	/**
	 * Load settings to UI.
	 *
	 */
	public abstract void discardSettings();

}
