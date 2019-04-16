package ru.dz.shipMaster.ui.plaf;

import java.awt.Graphics2D;

import javax.swing.JComponent;

import ru.dz.shipMaster.ui.VisualSettings;
import ru.dz.shipMaster.ui.meter.GeneralMeter;
import ru.dz.shipMaster.ui.misc.AlarmRegion;

public abstract class MeterUI extends DashComponentUI {

	GeneralMeter m;
	VisualSettings vis;


	public void paintAlarmRegion(Graphics2D g2d, AlarmRegion r)
	{
	}
	
	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
		
		m = (GeneralMeter)c;	
		vis = m.getVis();
		recalcMeterParameters();
		
		//warnDarkColor = new Color((0x00FFFFFF & vis.warnColor.getRGB()) | 0x77000000,true);
		//critDarkColor = new Color((0x00FFFFFF & vis.critColor.getRGB()) | 0x77000000,true);
		
		

		//processResize((DashComponent)c);
	}
	
	protected double minimum = 0, maximum = 1;
	public void recalcMeterParameters()
	{
		minimum = m.getMinimum();
		maximum = m.getMaximum(); 
	}
	

}
