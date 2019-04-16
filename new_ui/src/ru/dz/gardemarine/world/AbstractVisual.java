package ru.dz.gardemarine.world;

import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

public abstract class AbstractVisual<TItem extends IItem>
extends JPanel
implements IVisual<TItem>, PropertyChangeListener 
{

	protected final IConfig<TItem> config;

	public AbstractVisual(IConfig<TItem> config) {
		this.config = config;
		config.addPropertyChangeListener(this);
	}

	@Override
	protected void finalize() throws Throwable {
		config.removePropertyChangeListener(this);
		super.finalize();
	}
	
}
