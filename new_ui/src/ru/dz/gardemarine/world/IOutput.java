package ru.dz.gardemarine.world;

public interface IOutput<IType extends IInterfaceType> extends IInOut {
	void setPeer( IInput<IType> peer );
	IInput<IType> getPeer(  );
	
	/**
	 * To be called from IInput only!
	 * @param peer
	 */
	void setBackPeer(IInput<IInterfaceType> peer);
}
