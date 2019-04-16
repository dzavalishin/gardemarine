package ru.dz.gardemarine.world;

import java.beans.PropertyChangeEvent;

import javax.swing.JFrame;

public class MinimalEditor<TItem extends IItem> extends AbstractEditor<TItem> {

	public MinimalEditor(IConfig<TItem> config) {
		super(config);
	}

	private JFrame	frame = new JFrame();
	
	@Override
	public void showFrame() {
		frame.setVisible(true);		
	}

	@Override
	public void hideFrame() {
		frame.setVisible(false);		
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		
	}
	
}
