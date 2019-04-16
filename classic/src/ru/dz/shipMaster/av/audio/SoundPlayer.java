package ru.dz.shipMaster.av.audio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundPlayer {
    protected static final Logger log = Logger.getLogger(SoundPlayer.class.getName()); 

	private Clip clip = null;

	@SuppressWarnings("unused")
	private static Clip getClip(String fileName) throws FileNotFoundException, UnsupportedAudioFileException, IOException, LineUnavailableException {

		//File tadaSound = new File(System.getenv("windir") + "/" + "media/tada.wav");
		InputStream in = new FileInputStream(fileName);
		//AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new FileInputStream(tadaSound));

		//InputStream in = VisualHelpers.class.getClassLoader().getResourceAsStream("siren.wav");

		if(in == null) return null;

		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(in);


		AudioFormat audioFormat = audioInputStream.getFormat();
		DataLine.Info dataLineInfo = new DataLine.Info(Clip.class, audioFormat);
		Clip ret = (Clip) AudioSystem.getLine(dataLineInfo);
		ret.open(audioInputStream);

		return ret;
	}

	public void start(String fileName)
	{
		try {
			Clip nc = getClip(fileName);
			if(nc == null) return;

			synchronized (this) {

				if(clip != null)
				{
					clip.stop();
					clip.close();
				}

				clip = nc;
			}

		} catch (FileNotFoundException e) {
			log.log(Level.SEVERE, "Can't open file "+fileName, e);
		} catch (Throwable e) {
			log.log(Level.SEVERE, "Can't play clip "+fileName, e);
			return;
		}

		clip.setMicrosecondPosition(0);
		clip.start();
	}

	public void stop()
	{
		if(clip != null) clip.stop();
	}


	void sleep(int sec)
	{
		synchronized (this) {
			try {
				wait(1000*sec);
			} catch (InterruptedException e) {
				//e.printStackTrace();
			}
		}
	}


	public static void main(String[] args) throws FileNotFoundException, UnsupportedAudioFileException, IOException, LineUnavailableException {
		SoundPlayer player = new SoundPlayer();
		player.start("M:/Mixes/Glushenko/old/Angel_123_CD.wav");
		player.sleep(100);

	}


}
