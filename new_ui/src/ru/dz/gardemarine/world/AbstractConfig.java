package ru.dz.gardemarine.world;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import ru.dz.gardemarine.ui.frames.IWorldEditor;

public abstract class AbstractConfig<TItem extends IItem> implements IConfig<TItem> {

	
	@Override
	public IVisual<TItem> newVisual(IWorldEditor editor) {
		return new MinimalVisual<TItem>(this,editor);
	}
	
	@Override
	public IEditor<TItem> newEditor() {
		return new MinimalEditor<TItem>(this);
	}	
	
	
	@Override
	public String toString() {
		return getType()+":"+getName();
	}
	
	
	
	@Override
	public int compareTo(IConfig o) {
		if( hashCode() > o.hashCode() ) return 1;
		if( hashCode() < o.hashCode() ) return -1;
		return 0;
	}
	
	
	@Override
	public void destroy() {
		try {
			BeanInfo info = Introspector.getBeanInfo( this.getClass() );
			for ( PropertyDescriptor pd : info.getPropertyDescriptors() )
			{
				//System.out.println( pd.getName() );
				String fname = pd.getName();

				Object v = readProperty(pd);
				
				if( (v != null) && (v instanceof IInOut) )
					((IInOut)v).disconnect();
			}
		} catch (IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * @param pd
	 */
	private Object readProperty(PropertyDescriptor pd) {
		Method readMethod = pd.getReadMethod();
		if(readMethod != null)
		{
			try {
				return readMethod.invoke(this, null);				
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "";
	}
	
	
	
	
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
	 * @param propertyName Name of the property to monitor
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
	 * @param propertyName Name of the property to monitor
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
	 * In general, this is for use by the config objects themselves
	 * and should not be called by an application program.
	 * @param propertyName  The programmatic name of the property that
	 * was changed.
	 * @param oldValue  The old value of the property.
	 * @param newValue  The new value of the property.
	 * @see java.beans.PropertyChangeSupport
	 * @see #addPropertyChangeListener
	 * @see #removePropertyChangeListener
	 */
	@Override
	public void firePropertyChange(String propertyName, 
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


	//------------------------------------------------------------------------
	// Properties
	//------------------------------------------------------------------------

	/**
	 * Return type of this thing, as to be shown to user.
	 * Supposed to be redefined in children.
	 * @return Type of this object, in user-displayable terms. 
	 */
	public String getType() { return "Undefned"; }
	
	
	
	
	
	private boolean disabled = false;
	
	public boolean isDisabled() {		return disabled;	}

	public void setDisabled(boolean disabled) {
		boolean old = this.disabled; 
		this.disabled = disabled;
		firePropertyChange("disabled", old, disabled);
	}

	
	
	
	/**
	 * Personal name of this object. Has no functional usage - only used to show in UI.
	 */
	private String 		name = "unnamed";

	/**
	 * Get personal name of this object. Has no functional usage - only used to show in UI.
	 */
	@Override
	public String getName() {		return name;	}

	/**
	 * Set personal name of this object. Has no functional usage - only used to show in UI.
	 */
	@Override
	public void setName(String name) {
		String oldName = this.name;
		this.name = name;
		firePropertyChange("name", oldName, this.name);
	}

	

}
