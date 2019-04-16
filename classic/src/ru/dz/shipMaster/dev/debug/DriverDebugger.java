package ru.dz.shipMaster.dev.debug;

import ru.dz.shipMaster.dev.IDriver;
import ru.dz.shipMaster.ui.DebugFrame;

public class DriverDebugger {

	private final IDriver drv;
	private DebugFrame frame = null;

	public DriverDebugger(IDriver drv) 
	{
		this.drv = drv;
	}

	public void start()
	{
		if(frame == null)
			frame = new DebugFrame(this);
		
		frame.setVisible(true);
	}

	public void stop() {
		if(frame != null)
			frame.setVisible(false);
	}

	public IDriver getDriver() 
	{
		return drv;
	}
	
}
