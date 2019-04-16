package ru.dz.gardemarine.world;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public abstract class AbstractEditor<TItem extends IItem> implements IEditor<TItem>, PropertyChangeListener {

	private final IConfig<TItem> config;

	public AbstractEditor(IConfig<TItem> config) {
		this.config = config;
		config.addPropertyChangeListener(this);
	}

	@Override
	protected void finalize() throws Throwable {
		config.removePropertyChangeListener(this);
		super.finalize();
	}
	
	
}
