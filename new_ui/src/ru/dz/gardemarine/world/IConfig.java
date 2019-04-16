package ru.dz.gardemarine.world;

import java.beans.PropertyChangeListener;

import ru.dz.gardemarine.ui.frames.IWorldEditor;

public interface IConfig<TItem extends IItem> extends Comparable<IConfig>{

	TItem produceItem();
	IVisual<TItem> newVisual(IWorldEditor editor);
	IEditor<TItem> newEditor();
	
	void addPropertyChangeListener(PropertyChangeListener listener);
	void removePropertyChangeListener(PropertyChangeListener listener);

	void addPropertyChangeListener(String propertyName,	PropertyChangeListener listener);
	void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);

	void firePropertyChange(String propertyName, Object oldValue,
			Object newValue);

	/**
	 * Return type of this thing, as to be shown to user.
	 * Supposed to be redefined in children.
	 * @return Type of this object, in user-displayable terms. 
	 */
	String getType();
	
	String getName();
	void setName(String name);
	
	void destroy();
}
