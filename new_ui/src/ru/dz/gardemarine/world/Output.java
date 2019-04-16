package ru.dz.gardemarine.world;

public class Output implements IOutput<IInterfaceType> {

	private IConfig<IItem> owner;

	@Override
	public void setOwner(IConfig<IItem> owner )
	{
		this.owner = owner;		
	}
	public IConfig<IItem> getOwner() {		return owner;	}

	@Override
	public boolean isConnected() {
		return peer != null;
	}
	
	
	private IInput<IInterfaceType> peer;

	@Override
	public String toString() {
		return peer == null ? "(disconnected)" : ">> "+peer.getOwner();
	}

	@Override
	public IInput<IInterfaceType> getPeer() {
		return peer;
	}

	@Override
	public void setBackPeer(IInput<IInterfaceType> peer) {
		this.peer = peer;
		if(owner != null) owner.firePropertyChange(null, null, null);
	}
	
	@Override
	public void setPeer(IInput<IInterfaceType> peer) {
		IInput<IInterfaceType> old = this.peer;

		setBackPeer( peer );
		
		// Disconnect
		if(old != null && old != peer)
			old.setBackPeer(null);

		if((peer != null) && (peer.getPeer() != this)) // Prevent call loop
			peer.setBackPeer(this);

	}

	@Override
	public void disconnect() {
		setPeer(null);
	}

}
