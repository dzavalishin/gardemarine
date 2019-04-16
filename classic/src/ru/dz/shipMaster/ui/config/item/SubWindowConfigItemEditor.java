package ru.dz.shipMaster.ui.config.item;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ru.dz.shipMaster.ui.config.ConfigItemEditor;

/**
 * Configuration item editor that keeps editing panel inside itself.
 * @author dz
 *
 */
public abstract class SubWindowConfigItemEditor extends ConfigItemEditor {

	@SuppressWarnings("unused")
	private final Dimension minSize;

	protected SubWindowConfigItemEditor(Dimension minSize, String title)
	{
		this.minSize = minSize;
		frame.setTitle(title);
	}
	
	private JPanel noDrvPanel = new JPanel();
	


	protected void setSideFrameMinSize(Dimension dimension) {
		frame.setMinimumSize(dimension);		
	}
	
	


	// TODO it must be something like AbstractDriverPanel
	private Component prevComp = null;
	protected void setSFPanel( JPanel p )
	{
		if(p == null)
			p = noDrvPanel; // to replace with empty one
		
		p.setBorder(new EmptyBorder(8,8,8,8));
		if(prevComp != null) frame.getContentPane().remove(prevComp);
		prevComp = p;
		frame.getContentPane().add(p, BorderLayout.SOUTH);
		frame.pack();
	}




}
