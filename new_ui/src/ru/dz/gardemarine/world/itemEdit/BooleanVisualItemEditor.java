package ru.dz.gardemarine.world.itemEdit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ru.dz.gardemarine.world.IConfig;

public class BooleanVisualItemEditor extends AbstractVisualItemEditor {


	JMenuItem connect = new JMenuItem();
	JMenuItem disconnect = new JMenuItem();

	JLabel valueLabel = new JLabel();
	JTextField valueTextEdit = new JTextField(16);
	JCheckBox valueBoolEdit = new JCheckBox();
	JComboBox valueEnumEdit = new JComboBox();

	public BooleanVisualItemEditor(JPanel p,
			IConfig owner, PropertyDescriptor pd) {
		super(owner, pd);
		setValue( readValue() );

		valueBoolEdit.setForeground(FG_FIELD);
		valueBoolEdit.setBackground(BG_TRANSPARENT);

		p.add(nameLabel,consL);
		p.add(valueBoolEdit,consM);
		p.add(specialLabel,consR);

		valueBoolEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object args = valueBoolEdit.isSelected();
				assignValue(args);
			}
		});

	}

	public void setValue(Object value) {		
		super.setValue(value);
		valueBoolEdit.setSelected(((Boolean)value));	
	}



}
