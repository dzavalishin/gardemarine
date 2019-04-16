package ru.dz.gardemarine.world.itemEdit;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ru.dz.gardemarine.world.IConfig;
import ru.dz.gardemarine.world.IInOut;
import ru.dz.gardemarine.world.IInput;
import ru.dz.gardemarine.world.IOutput;

public class StringVisualItemEditor extends AbstractVisualItemEditor {

	JMenuItem connect = new JMenuItem();
	JMenuItem disconnect = new JMenuItem();

	JLabel valueLabel = new JLabel();
	JTextField valueTextEdit = new JTextField(16);
	JCheckBox valueBoolEdit = new JCheckBox();
	JComboBox valueEnumEdit = new JComboBox();

	public StringVisualItemEditor(JPanel p,
			IConfig owner, PropertyDescriptor pd) {
		super(owner, pd);
		setValue( readValue() );

		valueTextEdit.setForeground(FG_FIELD);
		valueTextEdit.setCaretColor(FG_FIELD);
		valueTextEdit.setBackground(BG_NORMAL);


		p.add(nameLabel,consL);
		p.add(valueTextEdit,consM);
		p.add(specialLabel,consR);


		valueTextEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object args = valueTextEdit.getText();
				assignValue(args);
			}
		});

	}

	public void setValue(Object value) {		
		super.setValue(value);
		valueTextEdit.setText((value == null) ? "(none)" : value.toString());
	}




}
