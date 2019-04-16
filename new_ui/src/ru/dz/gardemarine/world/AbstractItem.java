package ru.dz.gardemarine.world;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class AbstractItem implements IItem {

    /**
     * Used to handle the listener list for property change events.
     *
     * @see #addPropertyChangeListener
     * @see #removePropertyChangeListener
     * @see #firePropertyChangeListener
     */
    private PropertyChangeSupport accessibleChangeSupport = null;
	
	
    /**
     * Adds a PropertyChangeListener to the listener list.
     * The listener is registered for all properties and will
     * be called when those properties change.
     *
     * @param listener  The PropertyChangeListener to be added
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        if (accessibleChangeSupport == null) {
            accessibleChangeSupport = new PropertyChangeSupport(this);
        }
        accessibleChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Removes a PropertyChangeListener from the listener list.
     * This removes a PropertyChangeListener that was registered
     * for all properties.
     *
     * @param listener  The PropertyChangeListener to be removed
     */
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        if (accessibleChangeSupport != null) {
            accessibleChangeSupport.removePropertyChangeListener(listener);
        }
    }
	
	/**
	 * Adds a PropertyChangeListener to the listener list.
	 * The listener is registered for all properties and will
	 * be called when those properties change.
	 *
	 * @param listener  The PropertyChangeListener to be added
	 */
	@Override
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		if (accessibleChangeSupport == null) {
			accessibleChangeSupport = new PropertyChangeSupport(this);
		}
		accessibleChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	/**
	 * Removes a PropertyChangeListener from the listener list.
	 * This removes a PropertyChangeListener that was registered
	 * for all properties.
	 *
	 * @param listener  The PropertyChangeListener to be removed
	 */
	@Override
	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		if (accessibleChangeSupport != null) {
			accessibleChangeSupport.removePropertyChangeListener(propertyName,listener);
		}
	}
    
    
    
    /**
     * Support for reporting bound property changes.  If oldValue and 
     * newValue are not equal and the PropertyChangeEvent listener list 
     * is not empty, then fire a PropertyChange event to each listener.
     * In general, this is for use by the Accessible objects themselves
     * and should not be called by an application program.
     * @param propertyName  The programmatic name of the property that
     * was changed.
     * @param oldValue  The old value of the property.
     * @param newValue  The new value of the property.
     * @see java.beans.PropertyChangeSupport
     * @see #addPropertyChangeListener
     * @see #removePropertyChangeListener
     */
    protected void firePropertyChange(String propertyName, 
				   Object oldValue, 
				   Object newValue) {
        if (accessibleChangeSupport != null) {
	    if (newValue instanceof PropertyChangeEvent) {
		PropertyChangeEvent pce = (PropertyChangeEvent)newValue;
		accessibleChangeSupport.firePropertyChange(pce);
	    } else {
		accessibleChangeSupport.firePropertyChange(propertyName, 
							   oldValue, 
							   newValue);
	    }
	}
    }
	
}
