package ru.dz.gardemarine.world;

import java.beans.PropertyChangeListener;

public interface IItem {

	void addPropertyChangeListener(PropertyChangeListener listener);
	void removePropertyChangeListener(PropertyChangeListener listener);

	void addPropertyChangeListener(String propertyName,	PropertyChangeListener listener);
	void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);
}
