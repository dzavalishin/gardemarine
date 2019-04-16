package ru.dz.gardemarine.world.itemEdit;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ru.dz.gardemarine.world.IConfig;
import ru.dz.gardemarine.world.MinimalVisual;

public class GeneralVisualItemViewer extends AbstractVisualItemEditor {

	JLabel valueLabel = new JLabel();

	public GeneralVisualItemViewer(JPanel p,
			IConfig owner, PropertyDescriptor pd) {
		super(owner, pd);
		setValue( readValue() );

		//specialLabel.setText("?");
		
		valueLabel.setForeground(FG_FIELD);
		
		p.add(nameLabel,consL);
		p.add(valueLabel,consM);
		p.add(specialLabel,consR);
	}

	public void setValue(Object value) {		
		super.setValue(value);
		valueLabel.setText((value == null) ? "(none)" : value.toString());
	}


}
