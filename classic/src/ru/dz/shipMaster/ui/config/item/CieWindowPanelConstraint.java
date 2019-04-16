package ru.dz.shipMaster.ui.config.item;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import ru.dz.shipMaster.config.items.CliWindowPanelConstraint;
import ru.dz.shipMaster.config.items.GridBagConstraintsWrapper;
import ru.dz.shipMaster.ui.config.ConfigItemEditor;

public class CieWindowPanelConstraint extends ConfigItemEditor {

	protected final GridBagConstraints consL2;
	protected final GridBagConstraints consR2;


	{
		{
			GridBagConstraints c = new GridBagConstraints();
			c.weightx = 1;
			c.weighty = 1;
			c.gridx = 2;
			c.gridy = GridBagConstraints.RELATIVE;
			c.anchor = GridBagConstraints.NORTHEAST;
			c.ipadx = 4;
			c.ipady = 2;
			c.insets = new Insets(4,4,4,4);
			c.fill = GridBagConstraints.BOTH;
			consL2 = c;
		}
		
		{
			GridBagConstraints c = new GridBagConstraints();
			c.weightx = 1;
			c.weighty = 1;
			c.gridx = 3;
			c.gridy = GridBagConstraints.RELATIVE;
			c.anchor = GridBagConstraints.NORTHWEST;
			c.ipadx = 4;
			c.ipady = 2;
			c.insets = new Insets(4,4,4,4);
			consR2 = c;
		}
		
	}
	
	
	private final CliWindowPanelConstraint bean;

	private JCheckBox visibleField = new JCheckBox();

	private JCheckBox fillXField = new JCheckBox();
	private JCheckBox fillYField = new JCheckBox();
	
	//private JTextField gridXField = new JTextField();
	private JSpinner gridXField = new JSpinner(new SpinnerNumberModel());
	private JSpinner gridYField = new JSpinner(new SpinnerNumberModel());
	
	
	
	private JSpinner gridHeightField = new JSpinner(new SpinnerNumberModel());
	private JSpinner gridWidthField = new JSpinner(new SpinnerNumberModel());
	
	private JTextField xWeightField = new JTextField();
	private JTextField yWeightField = new JTextField();

	private JSpinner xPadField = new JSpinner(new SpinnerNumberModel());
	private JSpinner yPadField = new JSpinner(new SpinnerNumberModel());
	
	private GridBagConstraintsWrapper constraints;

	private JTextField topInsetField	 	= new JTextField();
	private JTextField leftInsetField		= new JTextField();
	private JTextField bottomInsetField		= new JTextField();
	private JTextField rightInsetField		= new JTextField();
	
	/**
	 * Create new editor for this bean. 
	 * @param bean Bean to edit.
	 */
	public CieWindowPanelConstraint(CliWindowPanelConstraint bean) {
		this.bean = bean;

		panel.add(new JLabel("Visible:"),consL);		panel.add(visibleField,consR);
		panel.add(new JLabel(""),consL2);		panel.add(new JLabel(""),consR2);
		
		panel.add(new JLabel("Fill X:"),consL);			panel.add(fillXField,consR);
		panel.add(new JLabel("Fill Y:"),consL2);			panel.add(fillYField,consR2);
		panel.add(new JLabel("Grid X:"),consL);			panel.add(gridXField,consR);
		panel.add(new JLabel("Grid Y:"),consL2);			panel.add(gridYField,consR2);
		panel.add(new JLabel("Grid X size:"),consL);	panel.add(gridWidthField,consR);
		panel.add(new JLabel("Grid Y size:"),consL2);	panel.add(gridHeightField,consR2);
		panel.add(new JLabel("X weight:"),consL);		panel.add(xWeightField,consR);
		panel.add(new JLabel("Y weight:"),consL2);		panel.add(yWeightField,consR2);
		panel.add(new JLabel("X pad:"),consL);			panel.add(xPadField,consR);
		panel.add(new JLabel("Y pad:"),consL2);			panel.add(yPadField,consR2);

		panel.add(new JLabel("Top inset:"),consL);		panel.add(topInsetField,consR);
		panel.add(new JLabel("Left inset:"),consL);		panel.add(leftInsetField,consR);
		panel.add(new JLabel("Bot inset:"),consL2);		panel.add(bottomInsetField,consR2);
		panel.add(new JLabel("Right inset:"),consL2);	panel.add(rightInsetField,consR2);
		
		discardSettings();
	}

	@Override
	protected void informContainer() {		bean.informContainers(); }

	@Override
	public void applySettings() {
		bean.setVisible(visibleField.isSelected());
		
		//constraints.gridx = Integer.parseInt(gridXField.getText());
		constraints.gridx = (Integer)gridXField.getValue();
		constraints.gridy = (Integer)gridYField.getValue();
		
		constraints.gridheight = (Integer)gridHeightField.getValue();
		constraints.gridwidth  = (Integer)gridWidthField.getValue();
		
		constraints.weightx = Double.parseDouble(xWeightField.getText());
		constraints.weighty = Double.parseDouble(yWeightField.getText());
		
		constraints.fill = GridBagConstraintsWrapper.NONE;

		if( fillXField.isSelected() )		constraints.fill = GridBagConstraintsWrapper.HORIZONTAL;
		if( fillYField.isSelected())		constraints.fill = GridBagConstraintsWrapper.VERTICAL;
		if( fillXField.isSelected() && fillYField.isSelected() )			constraints.fill = GridBagConstraintsWrapper.BOTH;
		
		constraints.ipadx = (Integer) xPadField.getValue();
		constraints.ipady = (Integer) yPadField.getValue();
		
		constraints.insets = new Insets(
				Integer.parseInt( topInsetField.getText() ),
				Integer.parseInt( leftInsetField.getText() ),
				Integer.parseInt( bottomInsetField.getText() ),
				Integer.parseInt( rightInsetField.getText() )
				);
		
		//bean.setConstraints((GridBagConstraints) constraints.clone());
		bean.setConstraints(constraints);
	}

	@Override
	public void discardSettings() {
		visibleField.setSelected(bean.isVisible());
		
		//constraints = (GridBagConstraints) bean.getConstraints().clone();
		constraints = bean.getConstraints();
		
		//gridXField.setText( Integer.toString( constraints.gridx ) );
		//gridXField.setValue( Integer.toString( constraints.gridx ) );
		gridXField.setValue( new Integer( constraints.gridx ) );
		gridYField.setValue( new Integer( constraints.gridy ) );
		
		gridHeightField.setValue( new Integer( constraints.gridheight ) );
		gridWidthField.setValue( new Integer( constraints.gridwidth ) );
		
		xWeightField.setText( Double.toString(constraints.weightx) );
		yWeightField.setText( Double.toString(constraints.weighty) );
		
		fillXField.setSelected(constraints.fill == GridBagConstraintsWrapper.HORIZONTAL || constraints.fill == GridBagConstraintsWrapper.BOTH);
		fillYField.setSelected(constraints.fill == GridBagConstraintsWrapper.VERTICAL || constraints.fill == GridBagConstraintsWrapper.BOTH);
		
		xPadField.setValue(new Integer( constraints.ipadx ));
		yPadField.setValue(new Integer( constraints.ipady ));
		
		if(constraints.insets != null)
		{
			topInsetField.setText(Integer.toString( constraints.insets.top));
			leftInsetField.setText(Integer.toString( constraints.insets.left));
			bottomInsetField.setText(Integer.toString( constraints.insets.bottom));
			rightInsetField.setText(Integer.toString( constraints.insets.right));
		}
	}

	@Override
	public String getName() { return bean.getName(); }

	@Override
	public String getTypeName() { return "Window panel"; }

}
