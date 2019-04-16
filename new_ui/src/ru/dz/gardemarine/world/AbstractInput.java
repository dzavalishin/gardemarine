package ru.dz.gardemarine.world;

public abstract class AbstractInput<IType extends IInterfaceType> implements
		IInput<IType> {

	private IOutput<IType> peer;

	@Override
	public IOutput<IType> getPeer() {
		return peer;
	}

	@Override
	public void setPeer(IOutput<IType> peer) {
		this.peer = peer;
	}

}
