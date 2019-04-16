package ru.dz.shipMaster.ui.config.item;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ru.dz.shipMaster.DashBoard;
import ru.dz.shipMaster.config.Configuration;
import ru.dz.shipMaster.config.items.CliButtonGroup;
import ru.dz.shipMaster.config.items.CliInstrument;
import ru.dz.shipMaster.config.items.CliParameter;
import ru.dz.shipMaster.data.RandomDataSource;
import ru.dz.shipMaster.data.units.Unit;
import ru.dz.shipMaster.ui.DashComponent;
import ru.dz.shipMaster.ui.component.DashImage;
import ru.dz.shipMaster.ui.control.DashButton;
import ru.dz.shipMaster.ui.control.GeneralControl;
import ru.dz.shipMaster.ui.meter.GeneralMeter;
import ru.dz.shipMaster.ui.meter.GeneralPictogram;
import ru.dz.shipMaster.ui.meter.IInstrumentWithDigits;
import ru.dz.shipMaster.ui.meter.IInstrumentWithFont;
import ru.dz.shipMaster.ui.meter.IInstrumentWithMessages;
import ru.dz.shipMaster.ui.meter.IInstrumentWithRoundScale;
import ru.dz.shipMaster.ui.meter.IInstrumentWithTicks;

public class CieInstrument extends SideFrameConfigItemEditor //implements ActionListener 
{
	private static final JLabel BUTTON_GROUP_LABEL = new JLabel("Button group:");
	//private final JLabel MAJ_TICKS_LABEL = new JLabel("Major ticks:");
	//private final JLabel MIN_TICKS_LABEL = new JLabel("Minor ticks:");

	private JPanel ticsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

	private final JLabel OFF_MSG_LABEL = new JLabel("Off message:");
	private final JLabel ON_MSG_LABEL = new JLabel("On message:");
	private final JLabel SHOW_MSG_LABEL = new JLabel("Show message:");

	private final JLabel BASE_ANGLE_LABEL = new JLabel("Base angle:");
	private final JLabel SCALE_ANGLE_LABEL = new JLabel("Scale angle:");

	private final JLabel N_DIGITS_LABEL = new JLabel("Num of digits");
	private final JLabel FONT_SIZE_LABEL = new JLabel("Font size");

	public static final String INST_CLASS_PREFIX = "ru.dz.shipMaster.ui.";
	private static final String NO_INST = "None";

	final private String[] instrumentClasses = { 
			NO_INST, 
			"meter.PlainDigitalMeter",
			"meter.ClinoMeter", 
			"meter.CompassMeter",
			"meter.CenteredBarRulerMeter",
			"meter.GeneralRulerMeter",
			"meter.HalfGaugeMeter",
			"meter.LinearVerticalMeter",
			"meter.ThermometerVerticalMeter",
			"meter.DirectionMeter",
			"meter.JustHand",
			"meter.JustNumber",
			"meter.LatitudeNumber",
			"meter.LongitudeNumber",
			null,
			"meter.GeneralGraph",
			"meter.BasicGraph",
			"---",
			"meter.GeneralPictogram",
			"meter.WarningPictogram",
			"meter.CriticalPictogram",
			"meter.RawPictogram",
			"meter.RawOnOffPictogram",
			"meter.YellowPictogram",
			"meter.RedPictogram",
			"component.GeneralImage",
			"---",
			"meter.CameraImage",
			"meter.JmfCameraImage",
			"---",
			"logger.LogWindow",
			"logger.MessageWindow",
			"misc.RunningLine",
			"component.Clock",
			"component.Date",
			"component.JustLabel",
			"---",
			"control.DashButton",
			"control.DashTextButton",
			"control.DashImageButton",
			"control.DashToggleImageButton",
			"control.Dash3DClickButton",
			"control.Dash3DToggleButton",
	};

	private final CliInstrument bean;
	//private String name;
	private DashComponent		component;
	private CliParameter		parameter;
	private CliButtonGroup 		buttonGroup;

	//private JLabel instLabel;
	private JComboBox instList;
	
	private JTextField parameterNameField = new JTextField(12);
	private JButton parameterSelectButton = new JButton();
	private JButton parameterEditButton = new JButton();
	
	private JPanel bgPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	
	private JTextField buttonGroupNameField = new JTextField(12);
	private JButton buttonGroupSelectButton = new JButton();
	private JButton buttonGroupEditButton = new JButton();
	
	private JTextField nameField;
	private JTextField minField = new JTextField(6);
	private JTextField maxField = new JTextField(6);
	private JTextField unitsField = new JTextField(6);
	private JTextField labelField = new JTextField(6);

	private JTextField nMinorTicksField = new JTextField(6);
	private JTextField nMajorTicksField = new JTextField(6);

	private JTextField verticalWeightField = new JTextField(6);
	private JTextField horizontalWeightField = new JTextField(6);

	private JTextField verticalSizeField = new JTextField(6);
	private JTextField horizontalSizeField = new JTextField(6);

	private JTextField verticalPosField = new JTextField(6);
	private JTextField horizontalPosField = new JTextField(6);
	
	private JTextField nDigitsField = new JTextField(6);
	private JTextField fontSizeField = new JTextField(6);
	
	//private JButton iconButton;
	//private String imageShortName;
	private JTextField imageShortNameField = new JTextField(20);
	
	private JTextField onMessageField;
	private JTextField offMessageField;
	private JCheckBox pictogramMessageField = new JCheckBox();
	private JCheckBox drawHistogramCheckbox = new JCheckBox();
	private JTextField baseAngleField = new JTextField(6);
	private JTextField scaleAngleField = new JTextField(6);
	private JLabel ticsLabel = new JLabel("Ticks Major/Minor:");

	/**
	 * Create new editor for this bean. 
	 * @param bean Bean to edit.
	 */
	@SuppressWarnings("serial")
	public CieInstrument(CliInstrument bean) {
		super(new Dimension(20,20));

		this.bean = bean;
		this.component = bean.getComponent(); // XXX wrong! (why wrong?)

		panel.add(new JLabel("Name:"),consL);
		panel.add(nameField = new JTextField(),consR);
		nameField.setColumns(20);

		//panel.add(new JLabel("Instrument type:"),consL);
		//panel.add(instLabel = new JLabel("None"),consR);

		panel.add(new JLabel("Instrument class:"),consL);
		instList = new JComboBox(instrumentClasses);
		instList.setSelectedIndex(0);
		instList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { selectInstrumentClass(e); }});

		panel.add(instList,consR);

		//panel.add(new JLabel("Parameter:"),consL);
		//panel.add(parameterSelectButton,consR);

		{
			panel.add(new JLabel("Parameter:"),consL);
			JPanel wp = new JPanel(new FlowLayout(FlowLayout.LEFT));
			panel.add( wp, consR );

			wp.add( parameterNameField );
			parameterNameField.setEditable(false);
			wp.add( parameterSelectButton );
			wp.add( parameterEditButton );

			parameterSelectButton.addActionListener(new AbstractAction() { public void actionPerformed(ActionEvent e) { selectParameter(); }});
			parameterSelectButton.setText("...");

			parameterEditButton.addActionListener(new AbstractAction() { public void actionPerformed(ActionEvent e) { editParameter(); }});
			parameterEditButton.setText(">");

			//wp.setBorder(new BevelBorder(BevelBorder.LOWERED));
		}

		{
			panel.add(BUTTON_GROUP_LABEL,consL);
			panel.add( bgPanel, consR );

			bgPanel.add( buttonGroupNameField );
			buttonGroupNameField.setEditable(false);
			bgPanel.add( buttonGroupSelectButton );
			bgPanel.add( buttonGroupEditButton );
			//buttonGroupEditButton.setEnabled(false);

			buttonGroupSelectButton.addActionListener(new AbstractAction() { public void actionPerformed(ActionEvent e) { selectButtonGroup(); }});
			buttonGroupSelectButton.setText("...");

			buttonGroupEditButton.addActionListener(new AbstractAction() { public void actionPerformed(ActionEvent e) { editButtonGroup(); }});
			buttonGroupEditButton.setText(">");

			//wp.setBorder(new BevelBorder(BevelBorder.LOWERED));
		}
		
		
		/*if(false)
		{
			panel.add(new JLabel("Vert weight:"),consL);
			panel.add(verticalWeightField, consR );
			verticalWeightField.setToolTipText("Grid cell weight. Set to zero to not extend.");

			panel.add(new JLabel("Horiz weight:"),consL);
			panel.add(horizontalWeightField, consR );
			horizontalWeightField.setToolTipText("Grid cell weight. Set to zero to not extend.");
		}
		else if(false)
		{
			JPanel wp = new JPanel(new FlowLayout(FlowLayout.LEFT));

			wp.add(new JLabel("Vert weight:"));
			wp.add(verticalWeightField );
			verticalWeightField.setToolTipText("Grid cell weight. Set to zero to not extend.");

			wp.add(new JLabel("Horiz weight:"));
			wp.add(horizontalWeightField );
			horizontalWeightField.setToolTipText("Grid cell weight. Set to zero to not extend.");

			wp.setBorder(new BevelBorder(BevelBorder.LOWERED));

			consL.gridwidth = 2;
			panel.add(wp,consL);
			consL.gridwidth = 1;
		}
		else*/
		{
			panel.add(new JLabel("Weight vert/hor:"),consL);
			JPanel wp = new JPanel(new FlowLayout(FlowLayout.LEFT));
			panel.add( wp, consR );

			wp.add( verticalWeightField );
			verticalWeightField.setToolTipText("Grid cell weight. Set to zero to not extend.");


			wp.add(new JLabel("/"));

			wp.add( horizontalWeightField );
			horizontalWeightField.setToolTipText("Grid cell weight. Set to zero to not extend.");

			//wp.setBorder(new BevelBorder(BevelBorder.LOWERED));
		}

		{
			panel.add(new JLabel("Width/height:"),consL);
			JPanel wp = new JPanel(new FlowLayout(FlowLayout.LEFT));
			panel.add( wp, consR );

			wp.add( horizontalSizeField );
			horizontalSizeField.setToolTipText("Prefered width. -1 - use instrument default");

			wp.add(new JLabel("/"));

			wp.add( verticalSizeField );
			verticalSizeField.setToolTipText("Preferred height. -1 - use instrument default");
		

			//wp.setBorder(new BevelBorder(BevelBorder.LOWERED));
		}

		{
			panel.add(new JLabel("x/y:"),consL);
			JPanel wp = new JPanel(new FlowLayout(FlowLayout.LEFT));
			panel.add( wp, consR );

			wp.add( horizontalPosField );
			horizontalPosField.setToolTipText("x position");

			wp.add(new JLabel("/"));

			wp.add( verticalPosField );
			verticalPosField.setToolTipText("y position");
		

			//wp.setBorder(new BevelBorder(BevelBorder.LOWERED));
		}
		

		/*if(false)
		{
			panel.add(new JLabel("Value Min:"),consL);
			panel.add(minField,consR);
			minField.setColumns(6);
			minField.setInputVerifier(doubleInputVerifier);

			panel.add(new JLabel("Value Max:"),consL);
			panel.add(maxField,consR);
			maxField.setColumns(6);
			maxField.setInputVerifier(doubleInputVerifier);
		}
		else*/
		{
			panel.add(new JLabel("Value Min/Max:"),consL);
			JPanel wp = new JPanel(new FlowLayout(FlowLayout.LEFT));
			panel.add( wp, consR );

			wp.add(minField);
			//minField.setColumns(6);
			minField.setInputVerifier(doubleInputVerifier);

			wp.add(new JLabel("/"));

			wp.add(maxField);
			//maxField.setColumns(6);
			maxField.setInputVerifier(doubleInputVerifier);			
		}

		/*
		panel.add(new JLabel("Units:"),consL);
		panel.add(unitsField = new JTextField(20),consR);

		panel.add(new JLabel("Label:"),consL);
		panel.add(labelField = new JTextField(20),consR);
		*/
		
		
		/*if(false)
		{
			panel.add(MIN_TICKS_LABEL,consL);
			panel.add(nMinorTicksField,consR);
			nMinorTicksField.setToolTipText("-1 - use instrument default");

			panel.add(MAJ_TICKS_LABEL,consL);
			panel.add(nMajorTicksField,consR);
			nMajorTicksField.setToolTipText("-1 - use instrument default");
		}
		else*/
		{
			panel.add(ticsLabel,consL);
			panel.add( ticsPanel, consR );

			ticsPanel.add(nMajorTicksField);
			nMajorTicksField.setToolTipText("-1 - use instrument default");

			ticsPanel.add(new JLabel("/"));

			ticsPanel.add(nMinorTicksField);
			nMinorTicksField.setToolTipText("-1 - use instrument default");
		}

		panel.add(BASE_ANGLE_LABEL,consL);
		panel.add(baseAngleField,consR);

		panel.add(SCALE_ANGLE_LABEL,consL);
		panel.add(scaleAngleField,consR);

		panel.add(N_DIGITS_LABEL,consL);
		panel.add(nDigitsField,consR);
		
		panel.add(FONT_SIZE_LABEL,consL);
		panel.add(fontSizeField,consR);
		
		panel.add(new JLabel("Icon:"),consL);
		//panel.add(iconButton = new JButton(),consR);
		panel.add(imageShortNameField,consR);

		panel.add(ON_MSG_LABEL,consL);
		panel.add(onMessageField = new JTextField(),consR);
		onMessageField.setColumns(20);

		panel.add(OFF_MSG_LABEL,consL);
		panel.add(offMessageField = new JTextField(),consR);
		offMessageField.setColumns(20);

		panel.add(SHOW_MSG_LABEL, consL);
		panel.add(pictogramMessageField,consR);

		panel.add(new JLabel("Draw histogram:"),consL);
		panel.add(drawHistogramCheckbox ,consR);

		panel.add(new JLabel("Redraw example:"),consL);
		JButton redrawButton;
		panel.add(redrawButton = new JButton(),consR);

		redrawButton.addActionListener(new AbstractAction() { public void actionPerformed(ActionEvent e) { updatePreview(); }});
		redrawButton.setText("Redraw");


		//iconButton.addActionListener(new AbstractAction() { public void actionPerformed(ActionEvent e) { selectIcon(); }});
		//iconButton.setText("Select");


		setupSideFrame("Instrument preview");
		setSideFrameMinSize(new Dimension(20,10));

		discardSettings();
		updatePreview();

		setFieldsEnable();
	}


	@Override
	protected void informContainer() {		bean.informContainers(); }


	protected void selectParameter() {
		parameter = Configuration.selectParameter(panel);
		displayParameter();
	}

	private void editParameter()
	{
		if(parameter != null)
			parameter.displayPropertiesDialog();
	}
	
	private void displayParameter() {
		if(parameter != null)
		{
			parameterNameField.setText(parameter.getName());
			//if( nameField.getText().equalsIgnoreCase("New") )
			nameField.setText(parameter.getName());
		}
		else
			parameterNameField.setText("None");
		
		parameterEditButton.setEnabled(parameter != null);
	}


	
	
	
	protected void selectButtonGroup()
	{
		buttonGroup = Configuration.selectButtomGroup(panel);
		displayButtonGroup();
	}
	
	private void editButtonGroup()
	{
		if(buttonGroup != null)
			buttonGroup.displayPropertiesDialog();
	}
	
	private void displayButtonGroup() {
		if(buttonGroup != null)
		{
			buttonGroupNameField.setText(buttonGroup.getName());
			//nameField.setText(parameter.getName());
		}
		else
			buttonGroupNameField.setText("None");
		
		//buttonGroupEditButton.setEnabled(buttonGroup != null);
	}


	
	
	/*
	protected void selectIcon() {
		JFileChooser chooser = new JFileChooser("resources");
		//chooser.setApproveButtonText("Select");
		chooser.setMultiSelectionEnabled(false);

		int ret = chooser.showOpenDialog(panel);
		if(ret == JFileChooser.APPROVE_OPTION )
		{
			File selectedFile = chooser.getSelectedFile();
			imageShortName = selectedFile.getName();
			//imageShortName = selectedFile.getPath();
			BufferedImage image = VisualHelpers.loadImage(imageShortName);
			//if(image != null)				iconButton.setIcon(new ImageIcon(image));
			updatePreview();
		}
	}
	*/



	private int ignoreEvent = 0;
	private double valueMinimum = 0;
	private double valueMaximum = 100;
	private String meterLegend = "Delay";
	private String meterUnits = "sec";
	private String iClassName = null;
	private int nMinorTicks = -1;
	private int nMajorTicks = -1;
	private double baseRotationAngle;
	private double scaleAngle;

	protected void selectInstrumentClass(ActionEvent e) {
		// Hack!
		// We ignore events caused by setting combo box position from code 
		if(ignoreEvent  > 0)
			return;

		JComboBox cb = (JComboBox)e.getSource();
		String rawName = (String)cb.getSelectedItem();
		String newClassName = INST_CLASS_PREFIX+rawName;

		if( rawName.equalsIgnoreCase(NO_INST))
		{
			bean.setComponent(null); // XXX wrong!
			return;
		}

		iClassName  = newClassName;
		DashComponent newComponent = CliInstrument.instantiateInstrument(newClassName);
		if(newComponent != null)
			component = newComponent;
		setFieldsEnable();
		updatePreview();


	}


	private void setFieldsEnable() {
		if(component != null)
		{
			boolean isMeter = (component instanceof GeneralMeter);
			boolean isControl = component instanceof GeneralControl;
			boolean isInstrumentWithTicks = component instanceof IInstrumentWithTicks;
			boolean isInstrumentWithRoundScale = component instanceof IInstrumentWithRoundScale;
			boolean isMeterNoParameter = isMeter && (parameter == null);
			boolean isInstrumentWithFont = component instanceof IInstrumentWithFont;

			minField.setEnabled(isMeterNoParameter);
			maxField.setEnabled(isMeterNoParameter);
			unitsField.setEnabled(isMeterNoParameter);
			labelField.setEnabled(isMeterNoParameter);

			fontSizeField.setEnabled(isInstrumentWithFont);
			
			/*
			if(false)
			{
				nMinorTicksField.setEnabled(isInstrumentWithTicks);
				nMajorTicksField.setEnabled(isInstrumentWithTicks);
				nMinorTicksField.setVisible(isInstrumentWithTicks);
				nMajorTicksField.setVisible(isInstrumentWithTicks);
				MIN_TICKS_LABEL.setVisible(isInstrumentWithTicks);
				MAJ_TICKS_LABEL.setVisible(isInstrumentWithTicks);
			}
			else
			*/
			{
				ticsPanel.setVisible(isInstrumentWithTicks);
				ticsLabel.setVisible(isInstrumentWithTicks);
			}
			baseAngleField.setVisible(isInstrumentWithRoundScale);
			BASE_ANGLE_LABEL.setVisible(isInstrumentWithRoundScale);
			scaleAngleField.setVisible(isInstrumentWithRoundScale);
			SCALE_ANGLE_LABEL.setVisible(isInstrumentWithRoundScale);

			drawHistogramCheckbox.setEnabled(isMeter);

			boolean isPictogram = (component instanceof GeneralPictogram);
			boolean isImage = component instanceof DashImage;
			boolean isWithDigits = component instanceof IInstrumentWithDigits;
			boolean isWithMessages = component instanceof IInstrumentWithMessages;
			boolean isButton = component instanceof DashButton;

			//iconButton.setEnabled(isImage || (component instanceof ClinoMeter));
			//iconButton.setEnabled(isImage);
			imageShortNameField.setEnabled(isImage);

			onMessageField.setEnabled(isWithMessages);
			offMessageField.setEnabled(isWithMessages);
			onMessageField.setVisible(isWithMessages);
			offMessageField.setVisible(isWithMessages);
			ON_MSG_LABEL.setVisible(isWithMessages);
			OFF_MSG_LABEL.setVisible(isWithMessages);
			SHOW_MSG_LABEL.setVisible(isWithMessages);
			
			pictogramMessageField.setVisible(isWithMessages);

			nDigitsField.setEnabled(isWithDigits);
			
			/*
			buttonGroupEditButton.setVisible(isButton);
			buttonGroupNameField.setVisible(isButton);
			buttonGroupSelectButton.setVisible(isButton);

			BUTTON_GROUP_LABEL.setVisible(isButton);
			bgPanel.setVisible(isButton);
			*/
			bgPanel.setEnabled(isButton);
			buttonGroupSelectButton.setEnabled(isButton);
			buttonGroupEditButton.setEnabled(isButton);
			
			String iType = "Other";

			if(isImage)	iType = "Image";
			if(isMeter) iType = "Meter";
			if(isPictogram) iType = "Pictogram";
			if(isControl) iType = "Control";

			//instLabel.setText(iType );
			frame.setTitle(iType+": "+nameField.getText());

			//panel.validate();
			//panel.setSize(panel.getPreferredSize());
		}

	}



	private void loadFromFields()
	{
		if(parameter != null)
		{
			valueMaximum = parameter.getMaxValue();
			valueMinimum = parameter.getMinValue();
			meterLegend = parameter.getLegend();

			Unit unit = parameter.getUnit();
			//if( unit != null ) meterUnits = unit.toString();
			if( unit != null ) meterUnits = unit.getName();

			minField.setText(Double.toString(valueMinimum));
			maxField.setText(Double.toString(valueMaximum));
			labelField.setText(meterLegend);
			unitsField.setText(meterUnits);
		}
		else
		{
			try { valueMinimum = Integer.parseInt(minField.getText()); } catch( Throwable e) {/* */}
			try { valueMaximum = Integer.parseInt(maxField.getText()); } catch( Throwable e) {/* */}
			meterLegend = labelField.getText();
			meterUnits = unitsField.getText();
		}

		try{ nMinorTicks = Integer.parseInt( nMinorTicksField.getText() ); } catch( Throwable e) {/* */}
		try{ nMajorTicks = Integer.parseInt( nMajorTicksField.getText() ); } catch( Throwable e) {/* */}

		try{ baseRotationAngle = Integer.parseInt( baseAngleField.getText() ); } catch( Throwable e) {/* */}
		try{ scaleAngle = Integer.parseInt( scaleAngleField.getText() ); } catch( Throwable e) {/* */}
	}

	private static DashBoard fakeDb = new DashBoard();
	private void updatePreview() {
		loadFromFields();
		if(component != null)
		{
			if (component instanceof GeneralMeter) {

				GeneralMeter m = (GeneralMeter) component;

				m.setUnits(meterUnits);
				m.setLegend(meterLegend);
				m.setMaximum(valueMaximum);
				m.setMinimum(valueMinimum); /* */

				m.setCurrent(52);

				m.setHistogramVisible(drawHistogramCheckbox.isSelected());

				RandomDataSource source = new RandomDataSource(valueMinimum,valueMaximum,meterLegend,meterUnits);
				source.addMeter(m);
			}

			if (component instanceof GeneralPictogram) {
				GeneralPictogram pic = (GeneralPictogram) component;
				pic.setDashBoard(fakeDb);				
			}

			if(component instanceof IInstrumentWithMessages)
			{
				IInstrumentWithMessages p = (IInstrumentWithMessages) component;
				p.setOnMessage(onMessageField.getText());
				p.setOffMessage(offMessageField.getText());
				p.setDrawMessage(pictogramMessageField.isSelected());
			}

			if (component instanceof DashImage) {
				DashImage di = (DashImage) component;
				di.setPictogramFileName(imageShortNameField.getText());
			}

			// dashimage does it
			/*if (component instanceof ClinoMeter) {
				ClinoMeter cl = (ClinoMeter) component;
				cl.setPictogramFileName(imageShortName);
			}*/

			if( component instanceof IInstrumentWithTicks )
			{
				IInstrumentWithTicks m = (IInstrumentWithTicks)component;
				if( nMinorTicks >= 0 ) m.setNumMinorTicks(nMinorTicks);
				if( nMajorTicks >= 0 ) m.setNumNumbers(nMajorTicks);
			}

			if (component instanceof IInstrumentWithRoundScale) {
				IInstrumentWithRoundScale rs = (IInstrumentWithRoundScale) component;
				rs.setBaseRotationAngle(baseRotationAngle);
				rs.setScaleAngle(scaleAngle);
			}

			if(component instanceof IInstrumentWithDigits)
			{
				IInstrumentWithDigits i = (IInstrumentWithDigits)component;
				try
				{ 
					int nd = Integer.parseInt( nDigitsField.getText() );
					i.setNDigits(nd);
				} 
				catch( Throwable e) {/* */}				
			}

			if( component instanceof IInstrumentWithFont )
			{
				IInstrumentWithFont m = (IInstrumentWithFont)component;
				try
				{ 
					int fs = Integer.parseInt( fontSizeField.getText() );
					m.setFontSize(fs);
				} 
				catch( Throwable e) {/* */}
			}
			
			JPanel p = new JPanel(new BorderLayout());
			p.add(component, BorderLayout.CENTER);
			setSFPanel(p);

			// setCurrent again. some of them will recompute size
			if (component instanceof GeneralMeter) {
				GeneralMeter m = (GeneralMeter) component;
				m.setCurrent(52);
			}			
		}
	}

	@Override
	public void applySettings() {
		loadFromFields();
		bean.setName(nameField.getText());
		bean.setComponent(component);
		bean.setValueMinimum(valueMinimum);
		bean.setValueMaximum(valueMaximum);
		bean.setNMinorTicks(nMinorTicks);
		bean.setNMajorTicks(nMajorTicks);
		bean.setScaleAngle(scaleAngle);
		bean.setBaseRotationAngle(baseRotationAngle);
		bean.setLegend(meterLegend);
		bean.setUnits(meterUnits);
		bean.setInstrumentClassName(iClassName);
		bean.setImageShortName(imageShortNameField.getText());
		bean.setOnMessage(onMessageField.getText());
		bean.setOffMessage(offMessageField.getText());
		bean.setPictogramMessage(pictogramMessageField.isSelected());
		bean.setParameter(parameter);
		bean.setButtonGroup(buttonGroup);
		bean.setVerticalWeight(Double.parseDouble(verticalWeightField.getText()));
		bean.setHorizontalWeight(Double.parseDouble(horizontalWeightField.getText()));
		bean.setHistogramVisible(drawHistogramCheckbox.isSelected());		

		try { 
			int nd = Integer.parseInt( nDigitsField.getText() );
			bean.setNDigits(nd);
		} 
		catch( Throwable e) {/* */}

		try { 
			int fs = Integer.parseInt( fontSizeField.getText() );
			bean.setFontSize(fs);
		} 
		catch( Throwable e) {/* */}
			
		{
			int prefHeight = -1;
			int prefWidth = -1;

			try{ prefHeight = Integer.parseInt( verticalSizeField.getText() ); } catch( Throwable e) {/* */}
			try{ prefWidth = Integer.parseInt( horizontalSizeField.getText() ); } catch( Throwable e) {/* */}

			if( prefHeight > 0 && prefWidth > 0)
				bean.setPreferredSize(new Dimension(prefWidth,prefHeight));
		}

		{
			int prefX = -1;
			int prefY = -1;

			try{ prefY= Integer.parseInt( verticalPosField.getText() ); } catch( Throwable e) {/* */}
			try{ prefX= Integer.parseInt( horizontalPosField.getText() ); } catch( Throwable e) {/* */}

			if( prefX> 0 && prefY > 0)
				bean.setAbsolutePosition(new Point(prefX, prefY));
		}
	}

	@Override
	public void discardSettings() {
		nameField.setText(bean.getName());

		nDigitsField.setText(""+bean.getNDigits());
		fontSizeField.setText(""+bean.getFontSize());
		
		valueMaximum = bean.getValueMaximum();
		valueMinimum = bean.getValueMinimum();

		maxField.setText(""+valueMaximum);
		minField.setText(""+valueMinimum);

		labelField.setText(meterLegend = bean.getLegend());
		unitsField.setText(meterUnits = bean.getUnits());

		imageShortNameField.setText( bean.getImageShortName() );
		offMessageField.setText(bean.getOffMessage());
		onMessageField.setText(bean.getOnMessage());
		pictogramMessageField.setSelected(bean.isPictogramMessage());

		verticalWeightField.setText(""+bean.getVerticalWeight());
		horizontalWeightField.setText(""+bean.getHorizontalWeight());		

		nMajorTicksField.setText(""+(nMajorTicks = bean.getNMajorTicks()));
		nMinorTicksField.setText(""+(nMinorTicks = bean.getNMinorTicks()));

		baseAngleField.setText(""+(baseRotationAngle = bean.getBaseRotationAngle()));
		scaleAngleField.setText(""+(scaleAngle = bean.getScaleAngle()));

		drawHistogramCheckbox.setSelected(bean.isHistogramVisible());

		iClassName = bean.getInstrumentClassName();

		{
			Dimension size = bean.getPreferredSize();
			if(size != null)
			{
				horizontalSizeField.setText(""+size.width);
				verticalSizeField.setText(""+size.height);
			}
			else
			{
				horizontalSizeField.setText("-1");
				verticalSizeField.setText("-1");
			}
		}
		
		{
			Point position = bean.getAbsolutePosition();
			if(position != null)
			{
				horizontalPosField.setText(""+position.x);
				verticalPosField.setText(""+position.y);				
			}
			else
			{
				horizontalPosField.setText("-1");
				verticalPosField.setText("-1");				
			}
		}
		
		parameter = bean.getParameter();
		buttonGroup = bean.getButtonGroup();
		
		displayParameter();
		displayButtonGroup();

		DashComponent myComp = bean.getComponent();
		if(myComp != null)
		{
			String cn = myComp.getClass().getName();
			if( cn.startsWith(INST_CLASS_PREFIX))
				cn = cn.substring(INST_CLASS_PREFIX.length());

			int select = 0;
			for(int i = 0; i < instrumentClasses.length; i++)
			{
				if(instrumentClasses[i] == null) continue;

				if(instrumentClasses[i].equals(cn))
				{
					select = i;
					break;
				}
			}
			ignoreEvent++;
			instList.setSelectedIndex(select);
			ignoreEvent--;

			updatePreview();
			//myDriver.loadPanelSettings();
		}
	}

	@Override
	public String getName() { return bean.getName(); }

	@Override
	public String getTypeName() { return "Instrument"; }



}
