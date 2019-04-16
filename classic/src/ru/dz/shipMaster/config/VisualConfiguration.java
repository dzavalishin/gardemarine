package ru.dz.shipMaster.config;

import java.awt.Image;

import ru.dz.shipMaster.ui.VisualSettings;
import ru.dz.shipMaster.ui.misc.VisualHelpers;

public class VisualConfiguration {

	private boolean globalExtendHeight = false;
	private boolean globalExtendWidth = false;

	private VisualSettings visualSettings = new VisualSettings();

	private Image moveToTopIcon;
	private Image deleteIcon;
	private Image moveUpIcon;
	private Image moveDownIcon;
	private Image moveBottomIcon;
	private Image addIcon;
	private Image discardIcon;
	private Image applyIcon;
	private Image moveLeftIcon;
	private Image moveRightIcon;
	private Image moveEmptyIcon;

	
	public boolean isGlobalExtendHeight() {		return globalExtendHeight;	}
	public boolean isGlobalExtendWidth() {		return globalExtendWidth ;	}
	
	public void setGlobalExtendHeight(boolean globalExtendHeight) {		this.globalExtendHeight = globalExtendHeight;	}
	public void setGlobalExtendWidth(boolean globalExtendWidth) {		this.globalExtendWidth = globalExtendWidth;	}
	

	public VisualSettings getVisualSettings() { return visualSettings; }	
	public void setVisualSettings(VisualSettings visualSettings) {
		this.visualSettings = visualSettings;
	}
	

	
	public Image getEmptyIcon() {
		if( moveEmptyIcon == null )
			moveEmptyIcon = VisualHelpers.loadImage("icons_empty.png");
		return moveEmptyIcon;
	}
	
	

	public Image getAddIcon() {
		if( addIcon == null )
			addIcon = VisualHelpers.loadImage("icons_add.png");
		return addIcon;
	}
	
	public Image getDeleteIcon() {
		if( deleteIcon == null )
			deleteIcon = VisualHelpers.loadImage("icons_delete.png");
		return deleteIcon;
	}
	

	
	
	

	
	public Image getApplyIcon() {
		if( applyIcon == null )
			applyIcon = VisualHelpers.loadImage("icons_apply.png");
		return applyIcon;
	}
	
	public Image getDiscardIcon() {
		if( discardIcon == null )
			discardIcon = VisualHelpers.loadImage("icons_discard.png");
		return discardIcon;
	}
	
	
	
	
	
	
	
	
	
	
	public Image getMoveToTopIcon() {
		if( moveToTopIcon == null )
			moveToTopIcon = VisualHelpers.loadImage("icons_green_top.png");
		return moveToTopIcon;
	}
	
	public Image getMoveUpIcon() {
		if( moveUpIcon == null )
			moveUpIcon = VisualHelpers.loadImage("icons_green_up.png");
		return moveUpIcon;
	}
	
	public Image getMoveDownIcon() {
		if( moveDownIcon == null )
			moveDownIcon = VisualHelpers.loadImage("icons_green_down.png");
		return moveDownIcon;
	}
	
	public Image getMoveToBottomIcon() {
		if( moveBottomIcon == null )
			moveBottomIcon = VisualHelpers.loadImage("icons_green_bottom.png");
		return moveBottomIcon;
	}
	
	public Image getMoveLeftIcon() {
		if( moveLeftIcon == null )
			moveLeftIcon = VisualHelpers.loadImage("icons_green_left.png");
		return moveLeftIcon;
	}

	public Image getMoveRightIcon() {
		if( moveRightIcon == null )
			moveRightIcon = VisualHelpers.loadImage("icons_green_right.png");
		return moveRightIcon;
	}
	
	
	
	
}
