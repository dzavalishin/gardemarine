package ru.dz.shipMaster.dev.common;

public interface SerialBiPipe {

	public enum FlowControl { None, XonXoff, RTSCTS, DTRDSR };
	public enum DataBits { Data5, Data6, Data7, Data8 };
	public enum StopBits { Stop1, Stop15, Stop2 };
	public enum Parity { None, Odd, Even, Mark, Space};
	
	void setBaudRate(int rate);
	int getBaudRate();

	FlowControl getFlowControl();
	void setFlowcontrol( FlowControl fc );
	
	//void setStopBits( StopBits stopBits );
	//void setParity( Parity parity );
	//void setDataBits( DataBits dataBits );

	
}
