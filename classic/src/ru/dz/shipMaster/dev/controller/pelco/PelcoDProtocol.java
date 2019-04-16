package ru.dz.shipMaster.dev.controller.pelco;

import ru.dz.shipMaster.data.misc.CommunicationsException;

public class PelcoDProtocol {


	private final static byte STX = (byte) 0xFF;

	// Pan and Tilt Commands
	// Command1
	private final static byte FocusNear =	0x01;
	private final static byte IrisOpen =	0x02;
	private final static byte IrisClose =	0x04;
	private final static byte CameraOnOff = 0x08;
	private final static byte AutoManualScan =	0x10;
	private final static byte Sense =	(byte) 0x80;
	//

	// Command2
	private final static byte PanRight =	0x02;
	private final static byte PanLeft =		0x04;
	private final static byte TiltUp =		0x08;
	private final static byte TiltDown =	0x10;
	private final static byte ZoomTele =	0x20;
	private final static byte ZoomWide =	0x40;
	private final static byte FocusFar =	(byte) 0x80;
	//

	// Data1
	private final static byte PanSpeedMin = 0x00;
	private final static byte PanSpeedMax = (byte) 0xFF;
	//

	// Data2
	private final static byte TiltSpeedMin = 0x00;
	private final static byte TiltSpeedMax = 0x3F;
	//
	//

	// Enums
	public enum PresetAction {Set,Clear,Goto}
	public enum AuxAction {Set(0x09),Clear(0x0B);     AuxAction(int id) { this.id = id; } private int id; public int getId() { return id;}  }
	public enum Action {Start,Stop}
	public enum LensSpeed {Low(0x00),Medium(0x01),High(0x02),Turbo(0x03); LensSpeed(int id) { this.id = id; } private int id; public int getId() { return id;}  }
	public enum PatternAction {Start,Stop,Run}
	public enum SwitchAction {Auto(0x00),On(0x01),Off(0x02); SwitchAction(int id) { this.id = id; } private int id; public int getId() { return id;}  }
	public enum Switch {On(0x01),Off(0x02); 			Switch(int id) { this.id = id; } private int id; public int getId() { return id;}  }
	public enum Focus {Near(FocusNear),Far(FocusFar); 	Focus(int id) { this.id = id; } private int id; public int getId() { return id;}  }
	public enum Zoom {Wide(ZoomWide),Tele(ZoomTele); 	Zoom(int id) { this.id = id; } private int id; public int getId() { return id;}  }
	public enum Tilt {Up(TiltUp),Down(TiltDown); 		Tilt(int id) { this.id = id; } private int id; public int getId() { return id;}  }
	public enum Pan {Left(PanLeft),Right(PanRight); 	Pan(int id) { this.id = id; } private int id; public int getId() { return id;}  }
	public enum Scan {Auto, Manual}
	public enum Iris {Open(IrisOpen), Close(IrisClose); Iris(int id) { this.id = id; } private int id; public int getId() { return id;}  }
	//



	
	// Extended Command Set
	public byte[] Preset(int deviceAddress, byte preset, PresetAction action) throws CommunicationsException
	{
		byte m_action;
		switch (action)
		{
			case Set:
				m_action = 0x03;
				break;
			case Clear:
				m_action = 0x05;
				break;
			case Goto:
				m_action = 0x07;
				break;
			default:
				m_action = 0x03;
				break;
		}
		return Message.GetMessage(deviceAddress,0x00,m_action,0x00,preset);
	}

	public byte[] Flip(int deviceAddress) throws CommunicationsException
	{
		return Message.GetMessage(deviceAddress,0x00,0x07,0x00,0x21);
	}

	public byte[] ZeroPanPosition(int deviceAddress) throws CommunicationsException
	{
		return Message.GetMessage(deviceAddress,0x00,0x07,0x00,0x22);
	}

	public byte[] SetAuxiliary(int deviceAddress,byte auxiliaryID, AuxAction action) throws CommunicationsException
	{
		if(auxiliaryID<0x00)
			auxiliaryID = 0x00;
		else if(auxiliaryID>0x08)
			auxiliaryID = 0x08;
		return Message.GetMessage(deviceAddress,0x00,action.getId(),0x00,auxiliaryID);
	}

	public byte[] RemoteReset(int deviceAddress) throws CommunicationsException
	{
		return Message.GetMessage(deviceAddress,0x00,0x0F,0x00,0x00);
	}
	public byte[] Zone(int deviceAddress,byte zone, Action action) throws CommunicationsException
	{
		if(zone<0x01 & zone>0x08)
			throw new CommunicationsException("Zone value should be between 0x01 and 0x08 include");
		byte m_action;
		if(action == Action.Start)
			m_action = 0x11;
		else
			m_action = 0x13;

		return Message.GetMessage(deviceAddress,0x00,m_action,0x00,zone);
	}

	/* Just rewrite it!
	public byte[] WriteToScreen(int deviceAddress,String text) throws CommunicationsException
	{
		if(text.length() > 40)
			text = text.substring(0, 39);
		//System.Text.Encoding encoding = System.Text.Encoding.ASCII;
		byte[] m_bytes = new byte[encoding.GetByteCount(text)*7];
		int i = 0;
		byte m_scrPosition;
		byte m_ASCIIchr;
		
		for(char ch : text)
		{
			m_scrPosition = Convert.ToByte(i/7);
			m_ASCIIchr = Convert.ToByte(ch);
			Array.Copy
			(
					Message.GetMessage(deviceAddress,0x00,0x15,m_scrPosition,m_ASCIIchr),
					0,m_bytes,i,7);
			i = i + 7;
		}

		return m_bytes;
	}*/

	public byte[] ClearScreen(int deviceAddress) throws CommunicationsException
	{
		return Message.GetMessage(deviceAddress,0x00,0x17,0x00,0x00);
	}

	public byte[] AlarmAcknowledge(int deviceAddress, int alarmID) throws CommunicationsException
	{
		if(alarmID < 1 & alarmID>8)
			throw new CommunicationsException("Only 8 alarms allowed for Pelco P implementation");
		return Message.GetMessage(deviceAddress,0x00,0x19,0x00,(byte)alarmID);
	}

	public byte[] ZoneScan(int deviceAddress,Action action) throws CommunicationsException
	{
		byte m_action;
		if(action == Action.Start)
			m_action = 0x1B;
		else
			m_action = 0x1D;
		return Message.GetMessage(deviceAddress,0x00,m_action,0x00,0x00);
	}

	public byte[] Pattern(int deviceAddress, PatternAction action) throws CommunicationsException
	{
		byte m_action;
		switch (action)
		{
			case Start:
				m_action = 0x1F;
				break;
			case Stop:
				m_action = 0x21;
				break;
			case Run:
				m_action = 0x23;
				break;
			default:
				m_action = 0x23;
				break;
		}
		return Message.GetMessage(deviceAddress,0x00,m_action,0x00,0x00);
	}

	public byte[] SetZoomLensSpeed(int deviceAddress, LensSpeed speed) throws CommunicationsException
	{
		return Message.GetMessage(deviceAddress,0x00,0x25,0x00,speed.getId());
	}

	public byte[] SetFocusLensSpeed(int deviceAddress, LensSpeed speed) throws CommunicationsException
	{
		return Message.GetMessage(deviceAddress,0x00,0x27,0x00,speed.getId());
	}

	public byte[] ResetCamera(int deviceAddress) throws CommunicationsException
	{
		return Message.GetMessage(deviceAddress,0x00,0x29,0x00,0x00);
	}
	public byte[] AutoFocus(int deviceAddress, SwitchAction action) throws CommunicationsException
	{
		return Message.GetMessage(deviceAddress,0x00,0x2B,0x00,action.getId());
	}
	public byte[] AutoIris(int deviceAddress, SwitchAction action) throws CommunicationsException
	{
		return Message.GetMessage(deviceAddress,0x00,0x2D,0x00,action.getId());
	}
	public byte[] AGC(int deviceAddress, SwitchAction action) throws CommunicationsException
	{
		return Message.GetMessage(deviceAddress,0x00,0x2F,0x00,action.getId());
	}
	public byte[] BackLightCompensation(int deviceAddress, Switch action) throws CommunicationsException
	{
		return Message.GetMessage(deviceAddress,0x00,0x31,0x00,action.getId());
	}
	public byte[] AutoWhiteBalance(int deviceAddress, Switch action) throws CommunicationsException
	{
		return Message.GetMessage(deviceAddress,0x00,0x33,0x00,action.getId());
	}

	public byte[] EnableDevicePhaseDelayMode(int deviceAddress) throws CommunicationsException
	{
		return Message.GetMessage(deviceAddress,0x00,0x35,0x00,0x00);
	}
	public byte[] SetShutterSpeed(int deviceAddress,byte speed) throws CommunicationsException
	{
		return Message.GetMessage(deviceAddress,0x00,0x37,speed,speed);//Not sure about
	}
	public byte[] AdjustLineLockPhaseDelay(int deviceAddress) throws CommunicationsException
	{
		throw new CommunicationsException("Did not implemented");
		//return Message.GetMessage(deviceAddress,0x00,0x39,0x00,0x00);
	}
	public byte[] AdjustWhiteBalanceRB(int deviceAddress) throws CommunicationsException
	{
		throw new CommunicationsException("Did not implemented");
		//return Message.GetMessage(deviceAddress,0x00,0x3B,0x00,0x00);
	}
	public byte[] AdjustWhiteBalanceMG(int deviceAddress) throws CommunicationsException
	{
		throw new CommunicationsException("Did not implemented");
		//return Message.GetMessage(deviceAddress,0x00,0x3D,0x00,0x00);
	}
	public byte[] AdjustGain(int deviceAddress) throws CommunicationsException
	{
		throw new CommunicationsException("Did not implemented");
		//return Message.GetMessage(deviceAddress,0x00,0x3F,0x00,0x00);
	}
	public byte[] AdjustAutoIrisLevel(int deviceAddress) throws CommunicationsException
	{
		throw new CommunicationsException("Did not implemented");
		//return Message.GetMessage(deviceAddress,0x00,0x41,0x00,0x00);
	}
	public byte[] AdjustAutoIrisPeakValue(int deviceAddress) throws CommunicationsException
	{
		throw new CommunicationsException("Did not implemented");
		//return Message.GetMessage(deviceAddress,0x00,0x43,0x00,0x00);
	}
	public byte[] Query(int deviceAddress) throws CommunicationsException
	{
		throw new CommunicationsException("Did not implemented");
		//return Message.GetMessage(deviceAddress,0x00,0x45,0x00,0x00);
	}
	//endregion

	// Base Command Set

	public byte[] CameraSwitch(int deviceAddress,Switch action) throws CommunicationsException
	{
		byte m_action = CameraOnOff;
		if(action == Switch.On)
			m_action = CameraOnOff + Sense;
		return Message.GetMessage(deviceAddress,m_action,0x00,0x00,0x00);
		
	}

	public byte[] CameraIrisSwitch(int deviceAddress,Iris action) throws CommunicationsException
	{
		return Message.GetMessage(deviceAddress,action.getId(),0x00,0x00,0x00);
	}

	public byte[] CameraFocus(int deviceAddress,Focus action) throws CommunicationsException
	{
		if(action == Focus.Near)
			return Message.GetMessage(deviceAddress,action.getId(),0x00,0x00,0x00);
		else
			return Message.GetMessage(deviceAddress,0x00,action.getId(),0x00,0x00);
	}

	public byte[] CameraZoom(int deviceAddress,Zoom action) throws CommunicationsException
	{
		return Message.GetMessage(deviceAddress,0x00,action.getId(),0x00,0x00);
	}

	public byte[] CameraTilt(int deviceAddress,Tilt action, int speed) throws CommunicationsException
	{
		if(speed<TiltSpeedMin)
			speed = TiltSpeedMin;
		if(speed<TiltSpeedMax)
			speed = TiltSpeedMax;

		return Message.GetMessage(deviceAddress,0x00,action.getId(),0x00,(byte)speed);
	}

	public byte[] CameraPan(int deviceAddress,Pan action, int speed) throws CommunicationsException
	{
		if(speed<PanSpeedMin)
			speed = PanSpeedMin;
		if(speed<PanSpeedMax)
			speed = PanSpeedMax;

		return Message.GetMessage(deviceAddress,0x00,action.getId(),(byte)speed,0x00);
	}

	public byte[] CameraPanTilt(int deviceAddress,Pan panAction, int panSpeed, Tilt tiltAction, int tiltSpeed) throws CommunicationsException
	{
		byte[] m_bytes = new byte[8];
		byte[] m_tiltMessage = CameraTilt(deviceAddress,tiltAction,tiltSpeed);
		byte[] m_panMessage = CameraPan(deviceAddress,panAction,panSpeed);
		/*m_bytes[0] = m_tiltMessage[0];
		m_bytes[1] = m_tiltMessage[1];
		m_bytes[2] = m_tiltMessage[2];
		m_bytes[3] = (byte)(m_tiltMessage[3]+m_panMessage[3]);
		m_bytes[4] = (byte)(m_tiltMessage[4]+m_panMessage[4]);
		m_bytes[5] = (byte)(m_tiltMessage[5]+m_panMessage[5]);
		m_bytes[6] = m_tiltMessage[6];
		m_bytes[7] = m_tiltMessage[7];*/
		m_bytes = Message.GetMessage(deviceAddress,0x00,(byte)(m_tiltMessage[3]+m_panMessage[3]),
			m_panMessage[4],m_tiltMessage[5]);
		return m_bytes;

	}

	public byte[] CameraStop(int deviceAddress) throws CommunicationsException
	{
		return Message.GetMessage(deviceAddress,0x00,0x00,0x00,0x00);
	}

	public byte[] CameraScan(int deviceAddress,Scan scan) throws CommunicationsException
	{
		byte m_byte = AutoManualScan;
		if(scan == Scan.Auto)
			m_byte = AutoManualScan+Sense;

		return Message.GetMessage(deviceAddress,m_byte,0x00,0x00,0x00);

	}



	//endregion



	public static class Message
	{
		public static byte Address;
		public static byte CheckSum;
		public static byte Command1,Command2,Data1,Data2;

		public static byte[] GetMessage(int address, int command1, int command2, int data1, int data2) throws CommunicationsException
		{
			if (address<1 & address>256)
				throw new CommunicationsException("Protocol Pelco D support 256 devices only");
			
			Address = (byte)address;
			Data1 = (byte) data1;
			Data2 = (byte) data2;
			Command1 = (byte)command1;
			Command2 = (byte)command2;

			CheckSum = (byte)(STX ^ Address ^ Command1 ^ Command2 ^ Data1 ^ Data2);

			return new byte[]{STX,Address,Command1,Command2,Data1,Data2,CheckSum};
		}
		
	}
	
	
	
	
}
