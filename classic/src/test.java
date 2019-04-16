import java.io.IOException;
import java.net.MalformedURLException;

import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoPlayerException;
import javax.media.Player;


class test
{

    static boolean recv()
    {
        //String url= "rtp://224.144.251.104:49150/audio/1";
        String url= "rtp://224.123.111.101:22224/audio/1";

        MediaLocator mrl= new MediaLocator(url);

        /*
        if (mrl == null) {
            System.err.println("Can't build MRL for RTP");
            return false;
        }
		*/
        
        Player player;
        // Create a player for this rtp session
        try {
            player = Manager.createPlayer(mrl);
        } catch (NoPlayerException e) {
            System.err.println("Error:" + e);
            return false;
        } catch (MalformedURLException e) {
            System.err.println("Error:" + e);
            return false;
        } catch (IOException e) {
            System.err.println("Error:" + e);
            return false;
        }

        if (player != null) {
            //if (this.player == null) {

            //    this.player = player;
            //    player.addControllerListener(this);
                player.realize();
            //}
        }

    return true;
    }

    public static void main ( String args[] ) throws InterruptedException {

        try {
            recv();
            //receptor.doit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Object sleep = new Object();

        synchronized (sleep) {
            sleep.wait();
        }

    }


}




