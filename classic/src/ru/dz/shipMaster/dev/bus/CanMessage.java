package ru.dz.shipMaster.dev.bus;

public class CanMessage {

	private int			address;
	private boolean		extendedAddress = true;
	public boolean isExtendedAddress() {
		return extendedAddress;
	}

	private byte []		data;
	private boolean		remote; // is request
	
	public CanMessage(int address, byte[] data, boolean remote) {
		super();
		
		setData(data);
		
		this.address = address;
		//this.data = data;
		this.remote = remote;
	}

	public int getAddress() {
		return address;
	}

	public void setAddress(int address) {
		this.address = address;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		if(data.length > 8)
			throw new IllegalArgumentException("data length > 8");
		this.data = data;
	}

	public boolean isRemote() {
		return remote;
	}

	public void setRemote(boolean remote) {
		this.remote = remote;
	}

	
	
	
}
