package ru.dz.shipMaster.dev.common;



public abstract class AbstractSerialBiPipe extends GenericBiPipe implements
		SerialBiPipe {

	protected int baudRate = 9600;
	protected FlowControl flowControl = FlowControl.None;

	
	public void setBaudRate(int baudRate) {
		this.baudRate = baudRate;
	}
	
	@Override
	public void setFlowcontrol(FlowControl fc) {
		this.flowControl  = fc;
	}

	public int getBaudRate() {
		return baudRate;
	}
	
	public FlowControl getFlowControl() {
		return flowControl;
	}
}
