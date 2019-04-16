package ru.dz.shipMaster.dev.controller;

public class CBusRampCommand extends CBusCommand {

	private int time;
	private int group;
	private double level;

	@Override
	public String toString() {
		return String.format("CBus ramp src=%02X app=%02X time=%d grp=%02X lvl=%.3f",
				sourceAddress, application, time, group, level
				);
	}
	
	
	public CBusRampCommand(int sourceAddress, int application, int timeSec, int group, double level) {
		this.sourceAddress = sourceAddress;
		super.application = (byte)application;
		this.time = timeSec;
		this.group = group;
		this.level = level;
	}

	@Override
	public byte[] getPacket() {
		
		int enc = 0;
		
		if(time > 0) enc++;
		if(time > 4) enc++;
		if(time > 8) enc++;
		if(time > 12) enc++;
		if(time > 20) enc++;
		if(time > 30) enc++;
		if(time > 40) enc++;
		if(time > 60) enc++;
		if(time > 90) enc++;
		if(time > 120) enc++;
		if(time > 180) enc++;
		if(time > 300) enc++;
		if(time > 420) enc++;
		if(time > 600) enc++;
		if(time > 900) enc++;
		
		byte [] cmd = new byte[6];
		cmd[0] = 0x05;
		cmd[1] = (byte)this.application;
		cmd[2] = (byte)0;
		cmd[3] = (byte)(0x02 | ((enc<<3) & 0x78));
		cmd[4] = (byte)this.group;
		cmd[5] = (byte) (this.level*255);
		
		return CBusProtocol.createPacket(cmd); 
	}

	
	/** seconds */
	public int getTime() {		return time;	}
	public void setTime(int sec) {		this.time = sec;	}

	public int getGroup() {		return group;	}
	public void setGroup(int group) {		this.group = group;	}

	public double getLevel() {		return level;	}
	public void setLevel(double level) {		this.level = level;	}

}
