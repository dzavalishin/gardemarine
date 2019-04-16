package ru.dz.gardemarine.world;

public interface IInOut {
	void setOwner(IConfig<IItem> config);
	IConfig<IItem> getOwner();

	boolean isConnected();
	void disconnect();
}
