package ru.dz.gardemarine.world;

import javax.swing.JPanel;

public interface IVisual<TItem extends IItem> {

	JPanel getPanel();

	/**
	 * Highlight self. Called on mouse over.
	 * @param onoff On or off highlight.
	 */
	void setHighLight(boolean onoff);

	/**
	 * Called by owner when this visual is not used anymore.
	 */
	void destroy();
	
}
