package ru.dz.shipMaster.config.items;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager2;
import java.awt.Point;
import java.util.Map;
import java.util.Vector;

import javax.swing.JPanel;

import ru.dz.shipMaster.DashBoard;
import ru.dz.shipMaster.config.ConfigListItem;
import ru.dz.shipMaster.ui.DashComponent;
import ru.dz.shipMaster.ui.VisualSettings;
import ru.dz.shipMaster.ui.config.item.CieInstrumentPanel;
import ru.dz.shipMaster.ui.control.GeneralControl;
import ru.dz.shipMaster.ui.meter.GeneralMeter;
import ru.dz.shipMaster.ui.meter.GeneralPictogram;

/**
 * Set of grouped instruments to place in window slot.
 * @author dz
 *
 */

public class CliInstrumentPanel extends ConfigListItem {
	//private static DashBoard testDashBoard = new DashBoard(); // for test screens only
	private String name = "New";
	//private Vector<DashComponent> components = new Vector<DashComponent>(); 
	private Vector<CliInstrument> instruments = new Vector<CliInstrument>();
	private boolean vertical = false;
	private boolean absolutePositioning = false;
	//private Vector<Point> absolutePositions = new Vector<Point>();
	private Dimension panelSize = new Dimension(200, 200);



	@Override
	public void destroy() {
		if(instruments != null) instruments.removeAllElements();
		//if(absolutePositions != null) absolutePositions.removeAllElements();
		instruments = null;
		//absolutePositions = null;
		dialog = null;			
	}


	//private CliInstrumentPanelLayout absLay = new CliInstrumentPanelLayout(panelSize);

	public JPanel createPanel(DashBoard dashBoard) {
		//reapplyAbsPositions();
		return createPanel(
				dashBoard,
				null,
				instruments,
				vertical,
				absolutePositioning,
				//absolutePositions,
				panelSize,
				true
		);
	}

	/** 
	 * @param instrumentsEnabled Enabled is working, and disabled lets us move buttons in visual editor. 
	 **/
	public static JPanel createPanel(
			DashBoard dashBoard,
			Map<DashComponent,Integer> componentIndices, 
			Vector<CliInstrument> insts, 
			boolean vert, 
			boolean abs,
			//Vector<Point> absolutePositions,
			Dimension panelSize,
			boolean instrumentsEnabled
	) {
		JPanel outPanel = new JPanel();
		outPanel.setBackground(VisualSettings.global.panelBgColor);

		if(abs)
		{			
			outPanel.setLayout(new CliInstrumentPanelLayout(panelSize)); 
			//outPanel.setSize(panelSize);
		}
		else			
			outPanel.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = gbc.weighty = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;

		if(vert) 	gbc.gridy = GridBagConstraints.RELATIVE;
		else		gbc.gridx = GridBagConstraints.RELATIVE;

		int absY = 0;
		int absX = 0;
		int index = 0;
		for( CliInstrument ins : insts )
		{
			DashComponent component = ins.createNewInstance();
			
			if(component == null)
			{
				log.severe("null component in panel: "+ins);
				continue;
			}
			
			component.setEditMode(!instrumentsEnabled);
			
			//component.setMarked(true);
			
			if(componentIndices != null)
				componentIndices.put(component, index);

			component.setEnabled(instrumentsEnabled);

			gbc.weightx = ins.getHorizontalWeight();
			gbc.weighty = ins.getVerticalWeight();

			if (component instanceof GeneralControl) {
				GeneralControl c = (GeneralControl) component;

				c.setDestinationParameter(ins.getParameter());

			}


			if (component instanceof GeneralMeter) {
				GeneralMeter m = (GeneralMeter) component;

				m.setMinimum(ins.getValueMinimum());
				m.setMaximum(ins.getValueMaximum());
				m.setParameter(ins.getParameter());

				//m.setCurrent(55);

				m.setCurrent((ins.getValueMaximum()-ins.getValueMinimum())/2.2); // A bit lower than half
			}

			if (component instanceof GeneralPictogram) {
				GeneralPictogram pic = (GeneralPictogram) component;

				pic.setDashBoard( dashBoard );
			}

			gbc.fill = GridBagConstraints.NONE;
			if(component.canExtendHeight() && component.canExtendWidth())
				gbc.fill = GridBagConstraints.BOTH;
			else if(component.canExtendHeight())
				gbc.fill = GridBagConstraints.VERTICAL;
			else if(component.canExtendWidth())
				gbc.fill = GridBagConstraints.HORIZONTAL;


			if(abs)
			{
				outPanel.add( component );

				try { 
					Point point = ins.getAbsolutePosition(); //absolutePositions.get(index); 
					if(point != null)
					{
						absX = point.x;
						absY = point.y;
					}
				}
				catch(ArrayIndexOutOfBoundsException e) {}

				//component.setBounds(absX, absY, 200, 200);
				//System.out.println("CliInstrumentPanel.createPanel()"+ins.getName()+" x="+absX+" y="+absY);
				component.setLocation(absX, absY);
				component.setSize(component.getPreferredSize());
				component.repaint(100);

				absX += 50;
			}
			else
				outPanel.add( component, gbc );
			index++;
		}
		return outPanel;
	}


	private CieInstrumentPanel dialog = null;

	@Override
	public void displayPropertiesDialog() {
		if(!hasEditRight()) return;
		if(dialog == null) dialog = new CieInstrumentPanel(this);
		dialog.displayPropertiesDialog(); 
	}



	// getters/setters

	@Override
	public String getName() {		return name;	}
	public void setName(String name) {		this.name = name;	}

	//public Vector<DashComponent> getComponents() {		return components;	}
	//public void setComponents(Vector<DashComponent> components) {		this.components = components;	}

	public Vector<CliInstrument> getInstruments() {
		//reapplyAbsPositions();
		return instruments;	
	}
	public void setInstruments(Vector<CliInstrument> instruments) {		
		this.instruments = instruments;
		//reapplyAbsPositions();
	}



	public boolean isVertical() {		return vertical;	}
	public void setVertical(boolean vertical) {		this.vertical = vertical;	}

	public boolean isAbsolutePositioning() {		return absolutePositioning;	}
	public void setAbsolutePositioning(boolean absolutePositioning) {		this.absolutePositioning = absolutePositioning;	}

	public Dimension getPanelSize() {		return panelSize;	}
	public void setPanelSize(Dimension panelSize) {		
		this.panelSize = panelSize; 
		//absLay.setSize(panelSize);	
	}

	/*
	public Vector<Point> getAbsolutePositions() {
		//return (Vector<Point>) absolutePositions.clone();
		//return absolutePositions;

		Vector<Point> absolutePositions = absolutePositionsSave; //new Vector<Point>(instruments.size());
		//absolutePositionsSave = absolutePositions;

		if(instruments != null)
		{
			int size = instruments.size();
			absolutePositions.setSize(size);
			for(int i = 0; i < size; i++)
			{
				absolutePositions.set(i, instruments.get(i).getAbsolutePosition() );
			}
		}

		return absolutePositions;


		//return null;
	}*/

	/*
	// All this is to support old XML saves
	private Vector<Point> absolutePositionsSave = new Vector<Point>();
	public void setAbsolutePositions(Vector<Point> absolutePositions) {
		//this.absolutePositions.clear();
		//this.absolutePositions.addAll( absolutePositions );	
		absolutePositionsSave = absolutePositions;
		//reapplyAbsPositions();
	}
	*/

	/** hack! * /
	public void reapplyAbsPositions()
	{
		//System.out.println("CliInstrumentPanel.reapplyAbsPositions() "+absolutePositionsSave);

		if(absolutePositionsSave == null || absolutePositionsSave.isEmpty())
			return;

		for(int i = 0; i < instruments.size(); i++)
		{
			// Only if instrument has no own position info
			Point p = instruments.get(i).getAbsolutePosition();
			if( p == null || (p.x == 0 && p.y == 0) )
				instruments.get(i).setAbsolutePosition(absolutePositionsSave.get(i));
		}

	}*/




}


class CliInstrumentPanelLayout implements LayoutManager2
{

	private Dimension size;

	public CliInstrumentPanelLayout(Dimension size) {
		this.size = size;
	}

	public void setSize(Dimension panelSize) {
		size = panelSize;
	}

	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		// ignore
	}

	@Override
	public float getLayoutAlignmentX(Container target) {
		return 0.5f;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		return 0.5f;
	}

	@Override
	public void invalidateLayout(Container target) {
		// ignore
	}


	@Override
	public void addLayoutComponent(String name, Component comp) {
		// ignore
	}

	@Override
	public void layoutContainer(Container parent) {
		// ignore
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return size;
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return size;
	}

	@Override
	public Dimension maximumLayoutSize(Container target) {
		return size;
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		// ignore
	}

}

