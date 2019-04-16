package ru.dz.shipMaster.dev;

import java.awt.Image;

/**
 * Driver creates and attaches to the output port item of this class
 * to receive data on port.
 * @author dz
 *
 */
public abstract class PortDataOutput {
	public abstract void receiveDoubleData(double value);

	/**
	 * Override to be able to receive images.
	 * @param val
	 */
	public void receiveImageData(Image val) {}

	/**
	 * Override to be able to receive strings.
	 * @param val
	 */
	public void receiveStringData(String val) {}
}
