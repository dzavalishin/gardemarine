package ru.dz.shipMaster.av.audio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import ru.dz.shipMaster.ui.misc.VisualHelpers;

public class SirenSoundPlayer {
	private Clip clip;

	public SirenSoundPlayer() throws FileNotFoundException, UnsupportedAudioFileException, IOException, LineUnavailableException {

		//File tadaSound = new File(System.getenv("windir") + "/" + "media/tada.wav");     
		//AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new FileInputStream(tadaSound));

		InputStream in = VisualHelpers.class.getClassLoader().getResourceAsStream("siren.wav");
		
		if(in == null) return;
		
		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(in);


		AudioFormat audioFormat = audioInputStream.getFormat();
		DataLine.Info dataLineInfo = new DataLine.Info(Clip.class, audioFormat);
		clip = (Clip) AudioSystem.getLine(dataLineInfo);
		clip.open(audioInputStream);
	}

	void start()
	{
		if(clip == null) return;
	
		clip.setLoopPoints(0, -1);
		clip.setMicrosecondPosition(0);
		
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		//clip.start();
	}

	void stop()
	{
		if(clip != null) clip.stop();
	}

	public void startStop(boolean b) {
		if(b) start();
		else stop();		
	}

	
	void sleep(int sec)
	{
		synchronized (this) {
			try {
				wait(1000*sec);
			} catch (InterruptedException e) {
				//log.log( Level.SEVERE, "Interrupted wait", e);
				//e.printStackTrace();
			}
		}
	}
	

	public static void main(String[] args) throws FileNotFoundException, UnsupportedAudioFileException, IOException, LineUnavailableException {
		SirenSoundPlayer player = new SirenSoundPlayer();
		player.start();
		player.sleep(2);
		player.stop();
		player.sleep(3);
		player.start();
		player.sleep(10);

	}


}
