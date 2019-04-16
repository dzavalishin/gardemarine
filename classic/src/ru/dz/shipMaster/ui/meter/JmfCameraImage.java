package ru.dz.shipMaster.ui.meter;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Vector;
import java.util.logging.Logger;

import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.media.format.RGBFormat;


@SuppressWarnings("serial")
public class JmfCameraImage extends GeneralMeter {
	private final static Logger log = Logger.getLogger(JmfCameraImage.class.getName());

	//private Image cameraImage;
	//private int picX = 0;
	//private int picY = 0;
	private CaptureDeviceInfo device;
	private MediaLocator ml;
	private Player player;
	private Component videoScreen;

	private boolean started = false;

	public JmfCameraImage() {

	}


	private synchronized void startCamera()
	{

		Thread thread = new Thread() {@Override
			public void run() {

			if(started)
				return;

			try
			{ 
				//gets a list of devices how support the given videoformat

				//Vector deviceList = CaptureDeviceManager.getDeviceList(new YUVFormat());
				Vector<CaptureDeviceInfo> deviceList = CaptureDeviceManager.getDeviceList(new RGBFormat());
				//Vector deviceList = CaptureDeviceManager.getDeviceList(null);

				//System.out.println("Devices: ");
				for( CaptureDeviceInfo cdi : deviceList)
				{
					//System.out.println(cdi.toString());
					log.severe("Video in: "+cdi.toString());
				}

				if(deviceList.size() == 0)
				{
					log.severe("No Video in device");
					return;
				}

				device = deviceList.firstElement();
				ml = device.getLocator();
				player = Manager.createRealizedPlayer(ml);

				player.start();

				videoScreen = player.getVisualComponent();

				/*if(false)
				{
					add(videoScreen);
				}
				else
				*/
				{
					Frame frm=new Frame();
					frm.setBounds(10,10,300,300);

					frm.add(videoScreen);
					frm.setVisible(true);
				}			
				started = true;
			}
			catch(Exception e)
			{
				System.out.println(e);
			}		
		}};
		
		thread.start();
	}

	private synchronized void stopCamera()
	{
		started = false;
		player.stop();
		remove(videoScreen);
		player.deallocate();

		device = null;
		ml = null;
		videoScreen = null;
		player = null;
	}

	@Override
	protected void finalize() throws Throwable {
		stopCamera();
		super.finalize();
	}


	@Override
	public void receiveImage(Image val) {
		// Ignores input!
		//cameraImage = val;
		//repaint(20);
	}


	public void paintDashComponent(Graphics2D g2d) {

		/*
		if(cameraImage != null)
			g2d.drawImage(cameraImage, picX, picY, null);

		drawComponentBorder(g2d);
		 */
	}


	@Override
	public void paintDashComponentBackground(Graphics2D g2d) {
		startCamera();
	}

	@Override
	protected void recalcMeterParameters() {
		Dimension myPreferredSize = new Dimension();
		//myPreferredSize.height = 20;
		//myPreferredSize.width  = 20;

		myPreferredSize.height = 200;
		myPreferredSize.width  = 200;

		/*if(cameraImage != null)
		{
			myPreferredSize.height = Math.max( cameraImage.getHeight(null), 40 );
			myPreferredSize.width = Math.max( cameraImage.getWidth(null), 40 );
		}*/

		setPreferredSize(myPreferredSize);
	}




	@Override
	protected void processResize() {
		/*if( cameraImage != null )
		{
			picX = ((getSize().width)/2) - cameraImage.getWidth(null)/2;
			picY = ((getSize().height)/2) - cameraImage.getHeight(null)/2;
		}*/
	}

}
