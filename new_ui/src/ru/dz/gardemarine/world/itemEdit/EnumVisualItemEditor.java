package ru.dz.gardemarine.world.itemEdit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ru.dz.gardemarine.world.IConfig;

public class EnumVisualItemEditor extends AbstractVisualItemEditor {

	private Object[] enumConstants;
	JComboBox valueEnumEdit = new JComboBox();

	public EnumVisualItemEditor(JPanel p,
			IConfig owner, PropertyDescriptor pd) {
		super(owner, pd);

		enumConstants = pd.getPropertyType().getEnumConstants();
		valueEnumEdit.setModel(new javax.swing.DefaultComboBoxModel(enumConstants) );

		setValue( readValue() );

		p.add(nameLabel,consL);
		p.add(valueEnumEdit,consM);
		p.add(specialLabel,consR);


		valueEnumEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object args = valueEnumEdit.getSelectedItem();
				assignValue(args);
			}
		});


	}

	public void setValue(Object value) {		
		super.setValue(value);
		valueEnumEdit.setSelectedItem(value);
	}





}
