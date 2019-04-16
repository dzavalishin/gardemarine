package ru.dz.gardemarine.world;

public class Input implements IInput<IInterfaceType> {

	private IOutput<IInterfaceType> peer;

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

	@Override
	public String toString() {
		return peer == null ? "(disconnected)" : "<< "+peer.getOwner();
	}

	@Override
	public IOutput<IInterfaceType> getPeer() {
		return peer;
	}

	@Override
	public void setBackPeer(IOutput<IInterfaceType> peer) {
		this.peer = peer;
		if(owner != null) owner.firePropertyChange(null, null, null);
	}

	@Override
	public void setPeer(IOutput<IInterfaceType> peer) {
		// Disconnect first
		if(this.peer != null && this.peer != peer)
			this.peer.setBackPeer(null);

		setBackPeer( peer );

		if((peer != null) && (peer.getPeer() != this)) // Prevent call loop
			peer.setBackPeer(this);

	}

	@Override
	public void disconnect() {
		setPeer(null);
	}
}
