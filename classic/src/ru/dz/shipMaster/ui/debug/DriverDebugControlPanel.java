package ru.dz.shipMaster.ui.debug;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import javax.swing.JCheckBox;
import java.awt.GridBagConstraints;

public class DriverDebugControlPanel extends JPanel {

	private JCheckBox showAbsentCheckBox = null;
	private JCheckBox showDisconnectedCheckBox = null;

	/**
	 * This method initializes 
	 * 
	 */
	public DriverDebugControlPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.anchor = GridBagConstraints.WEST;
        gridBagConstraints1.gridy = 1;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridy = 0;
        this.setLayout(new GridBagLayout());
        this.setSize(new Dimension(598, 104));
        this.add(getShowAbsentCheckBox(), gridBagConstraints);
        this.add(getShowDisconnectedCheckBox(), gridBagConstraints1);
			
	}

	/**
	 * This method initializes showAbsentCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getShowAbsentCheckBox() {
		if (showAbsentCheckBox == null) {
			showAbsentCheckBox = new JCheckBox();
			showAbsentCheckBox.setText("Show absent");
			showAbsentCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					updateSettings();
				}
			});
		}
		return showAbsentCheckBox;
	}

	protected void updateSettings() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * This method initializes showDisconnectedCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getShowDisconnectedCheckBox() {
		if (showDisconnectedCheckBox == null) {
			showDisconnectedCheckBox = new JCheckBox();
			showDisconnectedCheckBox.setText("Show disconnected");
			showDisconnectedCheckBox
					.addChangeListener(new javax.swing.event.ChangeListener() {
						public void stateChanged(javax.swing.event.ChangeEvent e) {
							updateSettings();
						}
					});
		}
		return showDisconnectedCheckBox;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
