package ru.dz.shipMaster.dev.controller;


public class CBusSimpleCommand extends CBusCommand {
	private byte messageType;
	private byte command;
	private byte group;
	
	@Override
	public String toString() {
		if(command == CMD_OFF)
			return String.format("CBusCmd off src=%02X app=%02X grp=%02X",
					sourceAddress, application, group
					);
		else if(command == CMD_ON)
			return String.format("CBusCmd on src=%02X app=%02X grp=%02X",
					sourceAddress, application, group
					);
		else
			return String.format("CBusCmd type=%02X src=%02X app=%02X cmd=%02X grp=%02X",
				messageType, sourceAddress, application, command, group
				);
	}
	
	/*
	public CBusSimpleCommand(byte [] idata) throws CommunicationsException {
		int pos = 0;
		
		byte [] data = CBusProtocol.decodePacket(idata);
		
		messageType = data[pos++];
		sourceAddress = data[pos++];
		application = data[pos++];
		
		if(data[pos] == 0)
			pos++;
		else
		{
			if(data[pos] != 1 && data[pos+1] != 0)
				throw new CommunicationsException("Unknown packet format");
			pos += 2;
		}
		
		command = data[pos++];
		group = data[pos++];
		
	}*/
	
	public CBusSimpleCommand(int messageType, int application, int command, int group ) {
		this.messageType = (byte)messageType;
		this.application = (byte)application;
		this.command = (byte)command;
		this.group = (byte)group;	
	}

	public byte [] getPacket()
	{
		byte [] cmd = new byte[5];
		cmd[0] = this.messageType;
		cmd[1] = (byte)this.application;
		cmd[2] = (byte)0;
		cmd[3] = this.command;
		cmd[4] = this.group;
		
		return CBusProtocol.createPacket(cmd); 
	}
	
	
	
	public byte getMessageType() {		return messageType;	}
	public void setMessageType(byte messageType) {		this.messageType = messageType;	}

	public byte getCommand() {		return command;	}
	public void setCommand(byte command) {		this.command = command;	}

	public byte getGroup() {		return group;	}
	public void setGroup(byte group) {		this.group = group;	}
	
}
