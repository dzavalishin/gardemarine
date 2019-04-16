package ru.dz.shipMaster.ui.misc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.dz.shipMaster.ui.DashComponent;
import ru.dz.shipMaster.ui.bitFont.AsciiBitFont;
import ru.dz.shipMaster.ui.bitFont.BitFont;
import ru.dz.shipMaster.ui.bitFont.PCFBasedBitFont;
import ru.dz.shipMaster.ui.bitFont.SimpleBitFontRenderer;
import ru.dz.shipMaster.ui.pcfFont.PCFFont;

public class RunningLine extends DashComponent {
	private static final Logger log = Logger.getLogger(RunningLine.class.getName()); 

	/**
	 * Eclipse id. 
	 */
	private static final long serialVersionUID = -6539249092328458665L;

	private static Object syncObject = new Object();

	private BitFont font;
	private SimpleBitFontRenderer fr;

	private int bitStepX = 1;
	private int charStepX = 1;
	private int charWidthPixels = 1;

	public RunningLine(PCFFont src)
	{
		font = new PCFBasedBitFont(src);
		fr = new SimpleBitFontRenderer(font);
		setup();
	}

	public RunningLine(String PCFFontName) throws IOException
	{	
		font = loadPcf(PCFFontName);
		fr = new SimpleBitFontRenderer(font);
		setup();
	}

	public RunningLine() throws IOException
	{
		setupDefault();
	}





	private void setupDefault() throws IOException
	{
		font = new AsciiBitFont();
		fr = new SimpleBitFontRenderer(font);
		loadAscii(font, "asc6x8.font");

		setup();
	}

	/* (non-Javadoc)
	 * @see ru.dz.shipMaster.ui.DashComponent#canExtendWidth()
	 */
	@Override
	public boolean canExtendWidth() {
		return true;
	}


	private static void loadAscii(BitFont font, String fn)
	{
		{
			try { 
				font.loadFromAsciiPicture(fn);
			}
			catch( Exception e) {
				log.log(Level.SEVERE,"font error",e);
			}
		}
	}

	private static BitFont loadPcf(String PCFFontName) throws IOException
	{
		FileInputStream fis = new FileInputStream(PCFFontName);
		PCFFont pcfFont = new PCFFont(fis);
		return new PCFBasedBitFont(pcfFont);		
	}

	{
		vis.setPersonal_4_RunningLine();
	}

	private void setup()
	{
		fr.setPixelSize(5);

		bitStepX = fr.getBitStepX(); 
		charStepX = font.getSizeX();		
		charWidthPixels = bitStepX*charStepX;

		//fr.setBgColor(new Color(0x44000000, true));
		//fr.setBitOffColor(new Color(0x44000000, true));
		//fr.setBitOnColor(new Color(0xFFFFFFFF, true));

		//savedBg = fr.getBgColor();
		savedBg = Color.BLACK;
		savedBitOff = fr.getBitOffColor();

		processResize();
		repaint();		
	}



	private int widthInCharacters = 1; 
	@Override
	protected void processResize() {
		//int chWidDiff = 0;
		int oldWidthInCharacters = widthInCharacters;

		widthInCharacters = 1+(width/charWidthPixels);
		if(width == 0) widthInCharacters = 200; //	 so that nothing will be visible on start before first resize

		charShift += (widthInCharacters-oldWidthInCharacters);

		Dimension myPreferredSize = new Dimension();
		myPreferredSize.width = 600;
		myPreferredSize.height = fr.getBitStepY()*font.getSizeY();
		setPreferredSize(myPreferredSize);
	}


	Color savedBg = new Color(0,true);
	Color savedBitOff = savedBg;
	protected void processColorChange() 
	{
		//savedBg = fr.getBgColor();
		//savedBitOff = fr.getBitOffColor();
		//fr.setBgColor(transparentColor);
		//fr.setBitOffColor(transparentColor);
		//resetDashComponentBackgroundImage();
		//repaint();
	}


	//private static final int SHIFT_MSEC = 200;

	private int bitShift = 0;
	private int charShift = widthInCharacters;
	//private long last = System.currentTimeMillis();
	String displayString = "";
	protected void shift() {
		//long now = System.currentTimeMillis(); 
		//if(now < last+SHIFT_MSEC)			return;
		//last += SHIFT_MSEC;
		//last = now;

		if( displayString.length() <= 0 )
		{
			stopMe();
			return;
		}

		bitShift += 1;
		if(bitShift > font.getSizeX())
		{
			bitShift = 0;

			synchronized (syncObject) {
				if(charShift > 0)
					charShift--;
				else
				{						
					if(displayString.length() > 0)
						displayString = displayString.substring(1);
					else
					{
						charShift = widthInCharacters;
						//running = false;
						stopMe();
					}
				}
				repaint();
			}
		}
		repaint();
	}

	public void addMessage(String message)
	{
		synchronized (syncObject) {

			if(	!running ) // && charShift <= 0 )		
				charShift = widthInCharacters;

			if(displayString.length() <= 0)
				displayString = message;
			else
				displayString = displayString+" // "+message;

			//running = true;
			startMe();
		}
	}


	private String backgroundMessage = "(C) 2006, 2009 Сделано в Digital Zone";
	//private String backgroundMessage = "абвгдеёжзийклмнопрстфхцшщъыьэюя";
	//private String backgroundMessage = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
	//private String backgroundMessage = "abcdefghijklmnopqrstuvwxyz";
	/**
	 * Sets message to show when no running text is there.
	 * @param message Background message text.
	 */
	public void setBackgroundMessage(String message)
	{
		backgroundMessage = message;
		resetDashComponentBackgroundImage();
		repaint();
	}


	@Override
	protected void paintDashComponent(Graphics2D g2d)
	{
		if(running)
			fr.render(g2d, 1, displayString, 
					0-bitShift*fr.getBitStepX()+(charShift*charWidthPixels), 
					0 );
	}

	//private static final String longEmpty = "                                                                       ";
	private static final Color transparentColor = new Color(0x0,true);
	@Override
	public void paintDashComponentBackground(Graphics2D g2d) 
	{
		//g2d.setColor(scaleColor);
		//g2d.setColor(Color.BLACK); // TO DO which color?
		g2d.setColor(vis.bgColor);
		//g.setColor(Color.DARK_GRAY);
		g2d.fillRect(0, 0, width, height);
		// fill bg with "spaces"
		//fr.render(g2d, 120, "", 0-bitShift*fr.getBitStepX(), 0 );
		fr.setBgColor(savedBg);
		//fr.setBgColor(Color.BLACK);
		fr.setBitOffColor(savedBitOff);
		//fr.setBitOffColor(Color.BLACK);
		fr.render(g2d, 120, "", 0, 0 );
		//fr.setBgColor(transparentColor);
		fr.setBitOffColor(transparentColor);
		if(!running) 
		{
			int shift = widthInCharacters-backgroundMessage.length();
			shift /= 2;
			shift *= charWidthPixels;
			if( shift < 0 ) shift = 0;
			//fr.render(g2d, 0, backgroundMessage+longEmpty, shift, 0 );
			fr.render(g2d, 0, backgroundMessage, shift, 0 );
		}
	}

	// -----------------------------------------------------------------------
	// Timer
	// -----------------------------------------------------------------------


	private boolean running = false;

	/*
	{
		setTimer();
	}
	private Timer timer;
	private ActionListener timerAction;
	private void setTimer()
	{

		timerAction = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(running)
					shift();
			}	
		};	

		timer = new Timer(150, timerAction);
		timer.setInitialDelay(150);
		//timer.start();

	}*/

	private static final Object runningWait = new Object();

	private void startMe() 
	{ 
		running = true; 
		resetDashComponentBackgroundImage();
		//animatorThread.resume();
		synchronized (runningWait) {
			runningWait.notifyAll();
		}

		//timer.start();
		repaint();
	} 

	private void stopMe() 
	{ 
		running = false; 
		resetDashComponentBackgroundImage(); 
		//timer.stop();
		repaint();
	}

	protected Thread animatorThread;
	{
		animatorThread = new Thread( new Runnable() {

			public void run() {
				while(true)
				{
					try 
					{ 
						Thread.sleep(100);
						if(running)
							shift();

						// is NOT equal to else
						if(!running)
						{
							//animatorThread.suspend();
							synchronized (runningWait) {
								runningWait.wait();
							}
						}


					}
					catch(Throwable e) {/* ignore */}
				}

			}});
		animatorThread.setName("RunningLine animator");
		animatorThread.setDaemon(true);
		animatorThread.setPriority(Thread.MAX_PRIORITY);
		animatorThread.start();
	}

}
