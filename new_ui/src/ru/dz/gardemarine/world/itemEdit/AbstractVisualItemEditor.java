package ru.dz.gardemarine.world.itemEdit;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JPopupMenu;

import ru.dz.gardemarine.world.IConfig;

public class AbstractVisualItemEditor implements IVisualItemEditor {

	protected static final Logger log = Logger.getLogger(AbstractVisualItemEditor.class.getName());
	protected static final Color BG_NORMAL = Color.decode("#004400");
	private static final Color BG_HIGHLIGHT = Color.decode("#115533");
	private static final Color DARK_GREEN = Color.decode("#007700");
	private static final Color LIGHT_GREEN = Color.decode("#00BB00");
	protected static final Color FG_FIELD = Color.WHITE;
	protected static final Color BG_TRANSPARENT = new Color(0,0,0,0);

	protected final GridBagConstraints consL;
	protected final GridBagConstraints consM;
	protected final GridBagConstraints consR;

	protected JLabel nameLabel = new JLabel("");
	protected JLabel specialLabel = new JLabel("");
	
	protected final boolean readonly;
	protected final IConfig<?> owner;

	/**
	 * Used to connect in and out in visual editor.
	 */
	protected Object value;

	private final Method writeMethod;
	private final PropertyDescriptor pd;
	private Method readMethod;


	{
		{
			GridBagConstraints c = new GridBagConstraints();
			c.weightx = 1;
			c.weighty = 1;
			c.gridx = 0;
			c.gridy = GridBagConstraints.RELATIVE;
			c.anchor = GridBagConstraints.NORTHEAST;
			c.ipadx = 4;
			c.ipady = 2;
			c.insets = new Insets(4,4,4,4);
			c.fill = GridBagConstraints.BOTH;
			consL = c;
		}

		{
			GridBagConstraints c = new GridBagConstraints();
			c.weightx = 1;
			c.weighty = 1;
			c.gridx = 1;
			c.gridy = GridBagConstraints.RELATIVE;
			c.anchor = GridBagConstraints.NORTHWEST;
			c.ipadx = 4;
			c.ipady = 2;
			c.insets = new Insets(4,4,4,4);
			consM = c;
		}

		{
			GridBagConstraints c = new GridBagConstraints();
			c.weightx = 1;
			c.weighty = 1;
			c.gridx = 2;
			c.gridy = GridBagConstraints.RELATIVE;
			c.anchor = GridBagConstraints.NORTHWEST;
			c.ipadx = 4;
			c.ipady = 2;
			c.insets = new Insets(4,4,4,4);
			consR = c;
		}

	}


	public AbstractVisualItemEditor(IConfig owner, PropertyDescriptor pd) {
		super();
		this.owner = owner;
		this.pd = pd;
		this.writeMethod = pd.getWriteMethod();
		this.readMethod = pd.getReadMethod();

		this.readonly = this.writeMethod == null;

		nameLabel.setForeground(FG_FIELD);
		specialLabel.setForeground(FG_FIELD);
		
		nameLabel.setText(pd.getName());
	}

	/* (non-Javadoc)
	 * @see ru.dz.gardemarine.world.itemEdit.IVisualItemEditor#setValue(java.lang.Object)
	 */
	public void setValue(Object value) {		
		this.value = value;
	}

	/**
	 * Assign a new value to property we represent. Called as a result of
	 * inline edit of property.
	 * @param args
	 */
	protected void assignValue(Object args) {
		log.log(Level.SEVERE,"assigning value");
		try {
			writeMethod.invoke(owner, args);
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * Read value from property.
	 * @return 
	 */
	protected Object readValue() {
		if(readMethod != null)
		{
			try {
				return readMethod.invoke(owner, null);				
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
		return null;
	}
	
	
	protected MouseListener popupListener = new MouseListener() {

		@Override
		public void mouseClicked(MouseEvent e) { showPopup(e); }

		@Override
		public void mouseEntered(MouseEvent e) { /* ignore */ }

		@Override
		public void mouseExited(MouseEvent e) { /* ignore */ }

		@Override
		public void mousePressed(MouseEvent e) { showPopup(e); }

		@Override
		public void mouseReleased(MouseEvent e) { showPopup(e); }
	};

	protected JPopupMenu popup = new JPopupMenu();

	protected void showPopup(MouseEvent e) {
		if( e.isPopupTrigger() )
			popup.show(e.getComponent(), e.getX(), e.getY());			
	}


}