package ru.dz.shipMaster.dev;

public class DrivertStartObjection {
	private String reason;
	private boolean startForbidden;
	
	public DrivertStartObjection() { reason = "?"; startForbidden = false; }
	public DrivertStartObjection(String reason) {
		this.reason = reason;
		startForbidden = false;
	}
	
	public String getReason() {		return reason;}
	public void setReason(String reason) {		this.reason = reason;	}
	public boolean isStartForbidden() {		return startForbidden;	}
	public void setStartForbidden(boolean startForbidden) {		this.startForbidden = startForbidden;	}
}
