package ru.dz.shipMaster.misc;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ru.dz.shipMaster.ui.misc.VisualHelpers;

/**
 * Splash screen.
 * @author dz
 *
 */
@SuppressWarnings("serial")
public class SplashFrame extends JFrame {
	
	public SplashFrame() {
		super();
		initialize();
	}

	private BufferedImage iPictogram;
	private void initialize() {
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        this.setTitle("Gardemarine salutes you");
        this.setIconImage(VisualHelpers.getApplicationIconImage());
        this.setLocationByPlatform(true);

        this.setResizable(false);
        
        JPanel contentPane = new JPanel();
        this.setContentPane(contentPane);

        iPictogram = VisualHelpers.loadImage("splash.png");
        
        JLabel pic = new JLabel(new ImageIcon(iPictogram));

		contentPane.add(pic);
		this.pack();
        
	}
	
	public void showSplash()
	{
		if(iPictogram == null)
			return;
		setVisible(true);
	}
	
	public void hideSplash()
	{
		setVisible(false);
	}

}
