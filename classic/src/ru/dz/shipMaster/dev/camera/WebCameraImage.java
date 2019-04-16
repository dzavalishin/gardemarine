package ru.dz.shipMaster.dev.camera;

import java.awt.Image;
import java.net.URL;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.dev.DriverPort;
import ru.dz.shipMaster.dev.ThreadedDriver;

public class WebCameraImage extends ThreadedDriver {
	private String url = "http://207.251.86.250/images/cams/govt_property/cctv115.jpg";

	private DriverPort cameraPicturePort;
	
	public WebCameraImage() {
		super(1500, Thread.NORM_PRIORITY, "WebCamera driver");
	}

	@Override
	protected void doDriverTask() throws Throwable {
		Image image = ImageIO.read(new URL(url));
		pictureLabel.setIcon(new ImageIcon(image));
		
		if(cameraPicturePort != null)
		{
			cameraPicturePort.sendImageData(image);
		}
		
	}

	@Override
	protected void doLoadPanelSettings() {
		urlField.setText(url);
	}

	@Override
	protected void doSavePanelSettings() {
		setUrl(urlField.getText());
	}

	@Override
	protected void doStartDriver() throws CommunicationsException {
	}

	@Override
	protected void doStopDriver() throws CommunicationsException {
	}

	@Override
	public String getDeviceName() { return "Web Camera"; }

	@Override
	public String getInstanceName() {		return url; }


	@Override
	public boolean isAutoSeachSupported() { return false; }

	private JTextField urlField = new JTextField(url);
	private JLabel pictureLabel = new JLabel();

	@Override
	protected void setupPanel(JPanel panel) {
		panel.add(new JLabel("Camera URL:"), consL);
		panel.add(urlField, consR);

		panel.add(new JLabel("Last shot:"), consL);
		panel.add(pictureLabel, consR);
	}

	@Override
	protected void updatePorts(Vector<DriverPort> ports) {
		DriverPort.absentize(ports);
		DriverPort.sortPorts(ports);
		
		cameraPicturePort = getPort( ports, 0, Direction.Input, Type.Image, "Camera image");
	}

	// Getter/setter
	
	public String getUrl() {		return url;	}
	public void setUrl(String url) {		
		this.url = url;	
		}

}
