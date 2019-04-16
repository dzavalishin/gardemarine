package ru.dz.gardemarine.world;

public abstract class AbstractOutput<IType extends IInterfaceType> implements
		IOutput<IType> {
	
	private IInput<IType> peer;

	@Override
	public IInput<IType> getPeer() {
		return peer;
	}

	@Override
	public void setPeer(IInput<IType> peer) {
		this.peer = peer;
	}

}
