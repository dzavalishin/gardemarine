package ru.dz.shipMaster.dev.display.sigma;


import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.dev.DriverPort;
import ru.dz.shipMaster.dev.PipeBasedDriver;

public class ASC105 extends PipeBasedDriver {
	
	private JTextField startupTextField;
	private String startupText = "Gardemarine welcomes you";
	
	public ASC105() {
		// 2 sec between updates
		super(2000, Thread.NORM_PRIORITY, "Sigma ASC105 driver");
	}

	/* *
	 * @param args
	 * @throws CommunicationsException 
	 * /
	public static void main(String[] args) throws CommunicationsException {
		ASC105 driver = new ASC105();
		driver.setPortName("COM1");
		driver.start();
		driver.sendText("Алёна - победитель Перекрёстков. :)");
		driver.stop();
		
	}*/
	

	
	//public void setRedColor(boolean i)	{		isRed = i;	}
	
	
	@Override
	protected void doDriverTask() throws Throwable {		
		doSendText(queue.take());		
	}

	protected void signalStop() throws Throwable 
	{
		sendText("--"); // Just to unlock queue
	};
	
	private final BlockingQueue<String> queue = new ArrayBlockingQueue<String>(100); 
	public void sendText(String text) throws CommunicationsException
	{
		try {
			queue.put(text);
		} catch (InterruptedException e) {
			log.log(Level.SEVERE,"Can't display: "+text, e);
		}
	}
	
	private void doSendText(String text) throws IOException
	{
		// \a - dark red
		// \b - red
		// \d - light yellow
		// \c - dark orange
		// \e - light orange
		// \e - very light orange
		// \g - green
		// \h - light green
		// \i - rainbow
		// \j - layers g y r
		// \k - vert bicolor red/orange
		// \l - vert bicolor green/red
		// \m revers green onred
		// \n reverse red on green
		// \o oragnge on red
		// \p yellow on green
		// \q - change font char ytable
		// \r - double width
		// \s - (orange?) standard font
		// \t - double size
		// \\u - big size
		// \v - triple size
		// \w - half size
		//sendString("~128~f01A\\s\\h");
		sendString("~128~f01B\\s\\h");

		//if(isRed)			sendString("\\b");
		sendString(text);
		
		// End
		write((byte)0x0d);
		write((byte)0x0d);
		write((byte)0x0d);
	
	}
	
	
	private void sendString(String text) throws IOException  {
		//byte[] bytes = text.getBytes();
		
		byte[] bytes = recode(text);
		
		for( int i = 0; i < bytes.length; i++ )
			write(bytes[i]);
	}

	private byte[] recode(String text) {
		byte[] bytes = new byte[text.length()];
		
		for( int i = 0; i < text.length(); i++ )
		{
			int o = '.';
			char c = text.charAt(i);
			if( c < 0x7F && c > 0 )
				o = (byte)c;
			
			switch(c)
			{
			case 'А': o = 'A'; break;
			case 'Б': o = 0x81; break;
			case 'В': o = 'B'; break;
			case 'Г': o = 0x83; break;
			case 'Д': o = 0x84; break;
			case 'Е': o = 'E'; break;
			case 'Ё': o = 0x86; break;
			case 'Ж': o = 0x87; break;
			case 'З': o = 0x88; break;
			case 'И': o = 0x89; break;
			case 'Й': o = 0x8A; break;
			case 'К': o = 'K'; break;
			case 'Л': o = 0x8C; break;
			case 'М': o = 'M'; break;
			case 'Н': o = 'H'; break;
			case 'О': o = 'O'; break;
			case 'П': o = 0x90; break;
			case 'Р': o = 'P'; break;
			case 'С': o = 'C'; break;
			case 'Т': o = 'T'; break;
			case 'У': o = 0x94; break;
			case 'Ф': o = 0x95; break;
			case 'Х': o = 'X'; break;
			case 'Ц': o = 0x97; break;
			case 'Ч': o = 0x98; break;
			case 'Ш': o = 0x99; break;
			case 'Щ': o = 0x9A; break;
			case 'Ъ': o = 0x9B; break;
			case 'Ы': o = 0x9C; break;
			case 'Ь': o = 'b'; break;
			case 'Э': o = 0x9E; break;
			case 'Ю': o = 0x9F; break;
			case 'Я': o = 0xA0; break;

			
			case 'а': o = 'a'; break;
			case 'б': o = 0xA2; break;
			case 'в': o = 0xA3; break;
			case 'г': o = 0xA4; break;
			case 'д': o = 0xA5; break;
			case 'е': o = 'e'; break;
			case 'ё': o = 0xA7; break;
			case 'ж': o = 0xA8; break;
			case 'з': o = 0xA9; break;
			case 'и': o = 0xAA; break;
			case 'й': o = 0xAB; break;
			case 'к': o = 'k'; break;
			//case 'к': o = 0xAC; break;
			case 'л': o = 0xAD; break;
			case 'м': o = 0xAE; break;
			case 'н': o = 0xAF; break;
			case 'о': o = 'o'; break;
			case 'п': o = 0xB1; break;
			case 'р': o = 'p'; break;
			case 'с': o = 'c'; break;
			case 'т': o = 0xE0; break;
			case 'у': o = 0xE1; break;
			case 'ф': o = 0xE2; break;
			case 'х': o = 'x'; break;
			case 'ц': o = 0xE4; break;
			case 'ч': o = 0xE5; break;
			case 'ш': o = 0xE6; break;
			case 'щ': o = 0xE7; break;
			case 'ъ': o = 0xE8; break;
			case 'ы': o = 0xE9; break;
			case 'ь': o = 0xEA; break;
			case 'э': o = 0xEB; break;
			case 'ю': o = 0xEC; break;
			case 'я': o = 0xED; break;
			
			default: /*o = o*/; break;
			}
			bytes[i] = (byte)o;
		}
		
		return bytes;
	}

	/*private void sendByte( int b )
	{
		byte [] buf = new byte[1]; 
		
		buf[0] = (byte) b;
		write(buf);	
	}*/


	@Override
	protected void setupPanel(JPanel panel) {
		super.setupPanel(panel);

		panel.add(new JLabel("Startup text:"), consL);
		panel.add(startupTextField = new JTextField(startupText), consR);
		startupTextField.setColumns(20);
	}
	
	@Override
	public String getDeviceName()
	{
		return "Бегущая строка Sigma ASC105";
	}


	@Override
	protected void doLoadPanelSettings() {
		super.doLoadPanelSettings();
		startupTextField.setText(startupText);
	}

	@Override
	protected void doSavePanelSettings() {
		super.doSavePanelSettings();
		startupText = startupTextField.getText();
	}


	@Override
	protected void doStartDriver() throws CommunicationsException {
		super.doStartDriver();
		sendText(startupText);
	}

	@Override
	protected void doStopDriver() throws CommunicationsException {
		int cnt = 20;
		while( (cnt-- > 0) && !queue.isEmpty())
			sleep(100);
		
		super.doStopDriver();				
	}

	@Override
	public boolean isAutoSeachSupported() { return false; }
	
	@Override
	protected void updatePorts(Vector<DriverPort> ports) {

		DriverPort.absentize(ports);
		DriverPort.sortPorts(ports);
		
		{
		DriverPort port = getPort(ports,0);
		port.setDirection(Direction.Output);
		port.setHardwareName("TextToShow.Green");
		port.setType(Type.String);
		}
		
		{
		DriverPort port = getPort(ports,1);
		port.setDirection(Direction.Output);
		port.setHardwareName("TextToShow.Red");
		port.setType(Type.String);
		}
	}

	public String getStartupText() {		return startupText;	}
	public void setStartupText(String startupText) {		this.startupText = startupText;	}

	
}

