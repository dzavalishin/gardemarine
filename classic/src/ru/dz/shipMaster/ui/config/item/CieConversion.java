package ru.dz.shipMaster.ui.config.item;

import java.awt.Frame;
import java.awt.GridBagLayout;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import ru.dz.shipMaster.config.items.CliConversion;
import ru.dz.shipMaster.data.filter.GeneralFilterDataSource;
import ru.dz.shipMaster.ui.StringArraySelectFrame;
import ru.dz.shipMaster.ui.config.ConfigItemEditor;
import ru.dz.shipMaster.ui.config.GeneralItemList;

/**
 * Editor for conversion class.
 * @author dz
 *
 */
public class CieConversion extends ConfigItemEditor {
	private JTextField nameField = new JTextField(20);
	private JTextField inMinField = new JTextField(8);
	private JTextField inMaxField = new JTextField(8);
	private JTextField outMinField = new JTextField(8);
	private JTextField outMaxField = new JTextField(8);
	private JCheckBox limitInputField = new JCheckBox("");
	private JTextField outputLPFilterWindowSizeField  = new JTextField(8);

	private Vector<GeneralFilterDataSource> filters = new Vector<GeneralFilterDataSource>();
	private FilterList filterList = new FilterList(filters); 
		
	private final CliConversion bean;

	/**
	 * Create new editor for this bean. 
	 * @param bean Bean to edit.
	 */
	public CieConversion(CliConversion bean) {
		this.bean = bean;
		
		panel.add(new JLabel("Name:"),consL);
		panel.add(nameField,consR);
		//nameField.setColumns(14);
		
		panel.add(new JLabel("Limit Input values:"),consL);
		panel.add(limitInputField,consR);

		
		panel.add(new JLabel("Input minimum value:"),consL);
		panel.add(inMinField,consR);

		panel.add(new JLabel("Input maximum value:"),consL);
		panel.add(inMaxField,consR);

		panel.add(new JLabel("Output minimum value:"),consL);
		panel.add(outMinField,consR);

		panel.add(new JLabel("Output maximum value:"),consL);
		panel.add(outMaxField,consR);

		panel.add(new JLabel("Output LP filter window:"),consL);
		panel.add(outputLPFilterWindowSizeField = new JTextField(8),consR);

		panel.add(new JLabel("Filters:"),consL);
		panel.add(filterList,consR);


		discardSettings(); // load initial settings
		pack();
	}

	@Override
	protected void informContainer() {		bean.informContainers(); }

	@Override
	public void applySettings() {
		bean.setName( nameField.getText() );
		bean.setLimitInput( limitInputField.isSelected() );
		
		bean.setInMin( Double.parseDouble( inMinField.getText() ) );
		bean.setInMax( Double.parseDouble( inMaxField.getText() ) );
		bean.setOutMin( Double.parseDouble( outMinField.getText() ) );
		bean.setOutMax( Double.parseDouble( outMaxField.getText() ) );
		
		bean.setOutputLPFilterWindowSize( Integer.parseInt( outputLPFilterWindowSizeField.getText() ) );
		
		bean.setFilters(filters);
		
		setTitle();
	}

	@Override
	public void discardSettings() {
		nameField.setText(bean.getName());
		limitInputField.setSelected(bean.isLimitInput());
		
		inMinField.setText( Double.toString(bean.getInMin()));
		inMaxField.setText( Double.toString(bean.getInMax()));
		outMinField.setText( Double.toString(bean.getOutMin()));
		outMaxField.setText( Double.toString(bean.getOutMax()));
		
		outputLPFilterWindowSizeField.setText( Integer.toString( bean.getOutputLPFilterWindowSize() ) );
		
		filters.removeAllElements();
		filters.addAll( bean.getFilters() );
		filterList.reloadItems();
	}

	@Override
	public String getTypeName() { return "Conversion"; }

	@Override
	public String getName() { return bean.getName(); }
	
}


class FilterList extends GeneralItemList<GeneralFilterDataSource>
{
    /**
	 * UID
	 */
	private static final long serialVersionUID = 4238967122310669360L;

	private static final Logger log = Logger.getLogger(FilterList.class.getName()); 

	static final String [] filterClassNames = {
		"HighPassFilter",
		"LowPassFilter",
		"AngleFromDutyCycleFilter",
		"TriggerFilter",
		"CountPerSecondFilter",
		"NormalizeFilter",
		"ApproximatorFilter"
	};
	
	public FilterList(Vector<GeneralFilterDataSource> items) {
		super(new GridBagLayout(), GeneralFilterDataSource.class, items);
	}

	@Override
	protected GeneralFilterDataSource makeOne(Class<GeneralFilterDataSource> iclass) {
		//GeneralFilterDataSource oclass = super.makeOne(iclass);
		
		Frame frame = JOptionPane.getFrameForComponent(this);
		StringArraySelectFrame classesSelect = new StringArraySelectFrame(
				filterClassNames,frame ,"Select filter type");
		
		classesSelect.setVisible(true);
		String className = classesSelect.getResult();
		
		if(className == null)
			return null;
		
		className = "ru.dz.shipMaster.data.filter."+className;
		
		try {
			Class<?> subclass = Class.forName(className);

			Class<? extends GeneralFilterDataSource> oclass = subclass.asSubclass(GeneralFilterDataSource.class);
			
			return (GeneralFilterDataSource)oclass.newInstance();
			
		} catch (ClassNotFoundException e) {
			log.log(Level.SEVERE,"Can't create filter",e);
			return null;
		} catch (InstantiationException e) {
			log.log(Level.SEVERE,"Can't create filter",e);
			return null;
		} catch (IllegalAccessException e) {
			log.log(Level.SEVERE,"Can't create filter",e);
			return null;
		}
		
	}
	
	
	
	
}
