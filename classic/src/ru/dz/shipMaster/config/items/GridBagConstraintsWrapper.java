package ru.dz.shipMaster.config.items;

import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * Exists so that GridBagConstraints can be saved with XMLencoder.
 * @author dz
 *
 */
@SuppressWarnings("serial")
public class GridBagConstraintsWrapper extends GridBagConstraints 
{
	public int getAnchor() {		return anchor;	}
	public void setAnchor(int anchor) {	this.anchor = anchor;	}
	
	public int getFill() {		return fill;	}
	public void setFill(int fill) {		this.fill = fill;	}
	
	public int getGridheight() {		return gridheight;	}
	public void setGridheight(int gridheight) {		this.gridheight = gridheight;	}
	
	public int getGridwidth() {		return gridwidth;	}
	public void setGridwidth(int gridwidth) {		this.gridwidth = gridwidth;	}
	
	public int getGridx() {		return gridx;	}
	public void setGridx(int gridx) {		this.gridx = gridx;	}
	
	public int getGridy() {		return gridy;	}
	public void setGridy(int gridy) {		this.gridy = gridy;	}
	
	public Insets getInsets() {		return insets;	}
	public void setInsets(Insets insets) {		this.insets = insets;	}
	
	public int getIpadx() {		return ipadx;	}
	public void setIpadx(int ipadx) {		this.ipadx = ipadx;	}
	
	public int getIpady() {		return ipady;	}
	public void setIpady(int ipady) {		this.ipady = ipady;	}
	
	public double getWeightx() {		return weightx;	}
	public void setWeightx(double weightx) {		this.weightx = weightx;	}
	
	public double getWeighty() {		return weighty;	}
	public void setWeighty(double weighty) {		this.weighty = weighty;	}

}