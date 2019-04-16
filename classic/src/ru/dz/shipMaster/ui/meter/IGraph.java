package ru.dz.shipMaster.ui.meter;

public interface IGraph {

	public abstract boolean isDrawMarkers();

	public abstract void setDrawMarkers(boolean drawMarkers);

	public abstract double getDisplaySpeed();

	public abstract void setDisplaySpeed(double displaySpeed);

}