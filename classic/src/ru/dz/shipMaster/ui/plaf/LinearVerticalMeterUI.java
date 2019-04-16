package ru.dz.shipMaster.ui.plaf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;

import javax.swing.JComponent;

import ru.dz.shipMaster.ui.DashComponent;

public abstract class LinearVerticalMeterUI extends MeterUI {

	//private static LinearVerticalMeterUI me = new LinearVerticalMeterUI();
	//public static LinearVerticalMeterUI createUI(JComponent c) { return new LinearVerticalMeterUI(); }

	protected abstract int calcBarHeight(double val);



	//private int warnLinePos = -1;
	//private int critLinePos = -1;
	

	
	
	
	

	
	private Dimension myMinimumSize = new Dimension( 30, 100 );
	@Override
	public Dimension getMinimumSize(JComponent c) { return myMinimumSize; }

	private Dimension myPreferredSize = new Dimension( 50, 200 );
	protected GradientPaint gradient;
	@Override
	public Dimension getPreferredSize(JComponent c) { return myPreferredSize; }

	
	protected Color warnDarkColor;// = new Color((0x00FFFFFF & vis.warnColor.getRGB()) | 0x77000000,true);
	protected Color critDarkColor;// = new Color((0x00FFFFFF & vis.critColor.getRGB()) | 0x77000000,true);
	

	
	@Override
	public void installUI(JComponent c) {
		super.installUI(c);	
		//m = (LinearVerticalMeter)c;	
		//vis = m.getVis();
		
		warnDarkColor = new Color((0x00FFFFFF & vis.warnColor.getRGB()) | 0x77000000,true);
		critDarkColor = new Color((0x00FFFFFF & vis.critColor.getRGB()) | 0x77000000,true);

		processResize((DashComponent)c);
	}

	@Override
	public void uninstallUI(JComponent c) {
		super.uninstallUI(c);
	}

	
	
}
