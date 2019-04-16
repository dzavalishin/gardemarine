package ru.dz.shipMaster.ui.meter;

public interface IInstrumentWithMessages {

	/**
	 * @return the offMessage
	 */
	public String getOffMessage();

	/**
	 * @param offMessage the offMessage to set
	 */
	public void setOffMessage(String offMessage);

	/**
	 * @return the onMessage
	 */
	public String getOnMessage();

	/**
	 * @param onMessage the onMessage to set
	 */
	public void setOnMessage(String onMessage);

	public void setDrawMessage(boolean drawmessage);
	public boolean isDrawMessage();
	
}
