package ru.dz.shipMaster.dev.controller;


public abstract class CBusCommand {

	public static final int CMD_OFF = 0x01;
	public static final int CMD_ON = 0x79;
	
	protected int application;
	protected int sourceAddress;


	public int getApplication() {		return application;	}
	public void setApplication(int application) {		this.application = application;	}

	public int getSourceAddress() {		return sourceAddress;	}
	public void setSourceAddress(int sourceAddress) {		this.sourceAddress = sourceAddress;	}


	
	public abstract byte [] getPacket();

	
	
	public static CBusCommand onCommand(int group) { return new CBusSimpleCommand(0x05, 0x38, CMD_ON, group ); }
	public static CBusCommand offCommand(int group) { return new CBusSimpleCommand(0x05, 0x38, CMD_OFF, group ); }
	public static CBusCommand onOffCommand(int group, boolean isOn ) 
	{ 
		return new CBusSimpleCommand(0x05, 0x38, isOn ? CMD_ON : CMD_OFF, group ); 
	}

	
	
}
