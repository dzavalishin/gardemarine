package ru.dz.shipMaster.ui.meter;

public abstract class GeneralLinearMeter extends GeneralMeter {

	//protected int warnLinePos = 0;
	//protected int critLinePos = 0;
	//protected Paint gradient;

	/**
	 * 
	 */
	private static final long serialVersionUID = 9165830323183040607L;

	public GeneralLinearMeter() {}

	/*public GeneralLinearMeter(DashBoard db) {		super(db);	}

	public GeneralLinearMeter(DashBoard db, int min, int max) {
		super(db, min, max);
	}

	public GeneralLinearMeter(DashBoard db, int min, int max, float warn, float crit, String newLegend, String newUnits) {
		super(db, min, max, warn, crit, newLegend, newUnits);
	}*/

	//@Override
	//protected void init()
	{
		vis.setPersonal_4_GeneralLinearMeter();		
	}
	
	/*{
		myIndicatorColor = new Color( (0x00FFFFFF&vis.indicatorColor.getRGB()) | 0x55000000, true);
	}*/
	
}
