package ru.dz.shipMaster.ui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import ru.dz.shipMaster.ui.misc.VisualHelpers;

public abstract class GeneralSelectFrame extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2621697555545379956L;
	protected final JPanel panel = new JPanel(new GridBagLayout()); 
	protected JList list = new JList();
	protected JPanel buttonPanel = new JPanel(new GridBagLayout());

	GridBagConstraints outerConstraints = new GridBagConstraints();
	{
		outerConstraints.gridx = 0;
		outerConstraints.gridy = GridBagConstraints.RELATIVE;
		outerConstraints.weightx = 1;
		outerConstraints.weighty = 1;
		outerConstraints.fill = GridBagConstraints.BOTH;
		outerConstraints.anchor = GridBagConstraints.NORTH;
		outerConstraints.ipadx = 10;
		outerConstraints.ipady = 10;
		outerConstraints.insets = new Insets(6,6,0,6);
	}	

	protected GridBagConstraints buttonConstraints = new GridBagConstraints();
	protected JButton selectButton;
	protected JButton refuseButton;
	
	{
		buttonConstraints.gridx = 0;
		buttonConstraints.gridy = GridBagConstraints.RELATIVE;
		buttonConstraints.weightx = 1;
		buttonConstraints.weighty = 1;
		//c.fill = GridBagConstraints.BOTH;		
	}

	protected static final Dimension buttonsSize = new Dimension(100,26);

	protected JButton addMyButton( String label, String toolTip, AbstractAction action )
	{
		JButton b = new JButton(label);

		b.setAction(action);
		b.setMinimumSize(buttonsSize);
		b.setPreferredSize(buttonsSize);
		b.setMaximumSize(buttonsSize);

		b.setToolTipText(toolTip);
		b.setText(label);
		buttonPanel.add(b, buttonConstraints);
		return b;
	}

	@SuppressWarnings("serial")
	public GeneralSelectFrame(Frame frame, String title, boolean b) {
		super(frame,title,true);

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//this.setTitle("Выбор");
		this.setLocationByPlatform(true);
        this.setIconImage(VisualHelpers.getApplicationIconImage());


		//getContentPane().add(tabbedPane, BorderLayout.CENTER);

		getContentPane().add(panel);

		selectButton = addMyButton("Use", "Use selected item", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				select();
			}});
		
		getRootPane().setDefaultButton(selectButton);
		
		refuseButton = addMyButton("None", "Use no item", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				selectNothing();
			}});
		
		{
	        KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");
	        Action actionListener = new AbstractAction() {
	          public void actionPerformed(ActionEvent actionEvent) {
	        	  selectNothing();
	          }
	        };
	        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
	        inputMap.put(stroke, "ESCAPE");
	        getRootPane().getActionMap().put("ESCAPE", actionListener);
		}
		
		{		
			list.setMinimumSize(new Dimension(120,500));

			JScrollPane scrollPane = new JScrollPane(list);
			//add(list);
			panel.add(scrollPane,outerConstraints);
			addImplementationComponents(panel,outerConstraints);
			//JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			panel.add(buttonPanel,outerConstraints);
		}

		list.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
				if( e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1 )
					select();
			}

			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
		});

	}

	/**
	 * To be implemented in subclasses to add UI elements between list and buttons.
	 * @param topPanel
	 * @param constraints 
	 */
	protected void addImplementationComponents(JPanel topPanel, GridBagConstraints constraints) {
	}

	/**
	 * Called when user selects element in the list.
	 */
	abstract protected void selectNothing();
	
	/**
	 * Called when user press 'none' button.
	 */
	abstract protected void select();


}
