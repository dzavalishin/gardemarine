package ru.dz.gardemarine.world;

public interface IInput<IType extends IInterfaceType> extends IInOut {
	void setPeer( IOutput<IType> peer );
	IOutput<IType> getPeer(  );
	void setBackPeer(IOutput<IInterfaceType> peer);
}
