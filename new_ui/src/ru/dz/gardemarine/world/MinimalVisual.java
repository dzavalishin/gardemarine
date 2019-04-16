package ru.dz.gardemarine.world;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ru.dz.gardemarine.ui.VisualHelpers;
import ru.dz.gardemarine.ui.frames.IWorldEditor;
import ru.dz.gardemarine.world.itemEdit.AbstractVisualItemEditor;
import ru.dz.gardemarine.world.itemEdit.BooleanVisualItemEditor;
import ru.dz.gardemarine.world.itemEdit.EnumVisualItemEditor;
import ru.dz.gardemarine.world.itemEdit.GeneralVisualItemViewer;
import ru.dz.gardemarine.world.itemEdit.IVisualItemEditor;
import ru.dz.gardemarine.world.itemEdit.InOutVisualItemEditor;
import ru.dz.gardemarine.world.itemEdit.StringVisualItemEditor;

public class MinimalVisual<TItem extends IItem> extends AbstractVisual<TItem> {
	protected static final Logger log = Logger.getLogger(MinimalVisual.class.getName()); 

	static final Color BG_NORMAL = Color.decode("#004400");
	private static final Color BG_HIGHLIGHT = Color.decode("#115533");
	private static final Color DARK_GREEN = Color.decode("#007700");
	private static final Color LIGHT_GREEN = Color.decode("#00BB00");

	static final Color FG_FIELD = Color.WHITE;
	static final Color BG_TRANSPARENT = new Color(0,0,0,0);
	
	private JLabel headerNameLabel = new JLabel("?");

	private JButton headerDeleteButton = new JButton();

	private final IWorldEditor worldEditor;

	/*static float[] hsbvals;
	static {
		Color.RGBtoHSB(0, 100, 0, hsbvals);

	}*/


	public MinimalVisual(IConfig<TItem> config, IWorldEditor editor) {
		super(config);
		this.worldEditor = editor;

		setLayout( new GridBagLayout() );
		setBorder(BorderFactory.createEtchedBorder( 2, LIGHT_GREEN, DARK_GREEN ) );
		/*panel.setBorder(BorderFactory.createEtchedBorder(
				Color.HSBtoRGB(hsbvals[0], 0.5f, 0.6f),
				Color.HSBtoRGB(hsbvals[0], 0.5f, 0.4f) );*/

		setBackground(BG_NORMAL);
		setForeground(FG_FIELD);

		{
			//JPanel hdr = new JPanel(new HorizBagLayout());
			final JPanel hdr = new JPanel(new BorderLayout());
			
			hdr.setBackground(BG_NORMAL);
			hdr.setForeground(FG_FIELD);

			
			hdr.add(headerNameLabel,BorderLayout.CENTER);
			headerNameLabel.setForeground(FG_FIELD);

			hdr.add(headerDeleteButton,BorderLayout.EAST);
			//headerDeleteButton.setForeground(FG_FIELD);
			//headerDeleteButton.setMaximumSize(new Dimension(20, 8));
			headerDeleteButton.setBorderPainted(false);
			headerDeleteButton.setContentAreaFilled(false);
			headerDeleteButton.setMargin(new Insets(0,0,0,0));
			
			headerDeleteButton.setIcon( VisualHelpers.loadIcon("bitcons/trash.gif") );
			headerDeleteButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if( VisualHelpers.showConfirmDialog(hdr, "Kill "+MinimalVisual.this.getName()+"?") )
						killMe();
				}});

			hdr.add(new JLabel("        "),BorderLayout.WEST);
			
			GridBagConstraints c = new GridBagConstraints();
			c.weightx = 1;
			c.weighty = 1;
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 3;
			c.anchor = GridBagConstraints.NORTHEAST;
			c.ipadx = 4;
			c.ipady = 0;
			//c.insets = new Insets(4,4,4,4);
			//c.insets = new Insets(0,4,0,4);
			c.fill = GridBagConstraints.HORIZONTAL;
			//c.fill = GridBagConstraints.BOTH;
			
			add(hdr,c);

			hdr.doLayout();
			//hdr.setSize(100,20);
			//hdr.setSize(hdr.getPreferredSize());

		}

		{
			JPanel fillPanel = new JPanel(null);
			
			fillPanel.setBackground(LIGHT_GREEN);
			//hdr.setForeground(FG_FIELD);
			
			GridBagConstraints c = new GridBagConstraints();
			c.weightx = 1;
			c.weighty = 0.01;
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 3;
			c.anchor = GridBagConstraints.NORTHEAST;
			//c.ipadx = 4;
			c.ipady = 1;
			//c.insets = new Insets(4,4,4,4);
			c.fill = GridBagConstraints.HORIZONTAL;
			//c.fill = GridBagConstraints.BOTH;
			
			add(fillPanel,c);

			//hdr.doLayout();
			fillPanel.setSize(1,1);
			//hdr.setSize(hdr.getPreferredSize());

		}
		
		try {
			BeanInfo info = Introspector.getBeanInfo( config.getClass() );
			for ( PropertyDescriptor pd : info.getPropertyDescriptors() )
			{
				//System.out.println( pd.getName() );
				String fname = pd.getName();

				// Don't show class name to user
				if(fname.equals("class"))
					continue;

				//boolean readonly = pd.getWriteMethod() == null;

				Object v = readProperty(pd);
				AbstractVisualItemEditor pair = null;
				
				{
					Class<?> pt = pd.getPropertyType();
					Method writeMethod = pd.getWriteMethod();

					if( pt.isEnum() )
					{
						pair = new EnumVisualItemEditor(this,config,pd);					
					}
					else if( pt.isAssignableFrom(boolean.class) || pt.isAssignableFrom(Boolean.class) )
					{
						pair = new BooleanVisualItemEditor(this,config,pd);					
					}
					else if( pt.isAssignableFrom(String.class) && (writeMethod != null) )
					{
						pair = new StringVisualItemEditor(this,config,pd);					
					} 
					else if( pt.isAssignableFrom(IOutput.class) || pt.isAssignableFrom(IInput.class) )
					{
						pair = new InOutVisualItemEditor(this,config,pd);
						//ioList.add(pair);
					}
					else
					{
						pair = new GeneralVisualItemViewer(this,config,pd);
					}
				}
				

				if(fname.equals("name"))
				{
					headerNameLabel.setText(v.toString());
					setName(v.toString());
				}

				if(pair == null) continue;
				items.put(fname, pair);
			}
		} catch (IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}


	protected void killMe() {
		log.log(Level.FINE, "Asked to kill self: "+this);
		worldEditor.deleteItem(config);
	}


	private Map<String,AbstractVisualItemEditor> items = new TreeMap<String, AbstractVisualItemEditor>();


	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		if(evt.getNewValue() != null)
			setValue(evt.getPropertyName(), evt.getNewValue());
		else
		{
			try {
				BeanInfo info = Introspector.getBeanInfo( config.getClass() );
				for ( PropertyDescriptor pd : info.getPropertyDescriptors() )
				{
					//System.out.println( pd.getName() );
					String fname = pd.getName();

					if(fname.equals("class"))
						continue;

					Object v = readProperty(pd);
					setValue(fname, v);
				}
			} catch (IntrospectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		doLayout();
		setSize(getPreferredSize());
	}

	/**
	 * @param pd
	 */
	private Object readProperty(PropertyDescriptor pd) {
		Method readMethod = pd.getReadMethod();
		if(readMethod != null)
		{
			try {
				return readMethod.invoke(config, null);				
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

	private void setValue(String propertyName, Object newValue) {
		IVisualItemEditor p = items.get(propertyName);
		if(p == null)
		{
			return; // if you want to add items at runtime fix this - use introspection to get property type 
			//p = new GeneralVisualItemEditor(propertyName,this, newValue);
			//items.put(propertyName, p);
		}
		else
			p.setValue(newValue);

		if(propertyName.equals("name"))
		{
			headerNameLabel.setText(newValue.toString());
			setName(newValue.toString());
		}
	}

	@Override	
	public JPanel getPanel() {		return this;	}

	@Override
	public void setHighLight(boolean onoff) {
		setBackground( onoff ? BG_HIGHLIGHT : BG_NORMAL );
		repaint(100);
	}

	@Override
	public void destroy() {
		// Unused
		
	}
}
