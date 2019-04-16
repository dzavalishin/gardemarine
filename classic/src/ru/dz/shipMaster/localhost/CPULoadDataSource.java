package ru.dz.shipMaster.localhost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.dz.shipMaster.data.DataGeneration;
import ru.dz.shipMaster.data.GeneralDataSource;

public class CPULoadDataSource extends GeneralDataSource {
    private static final Logger log = Logger.getLogger(CPULoadDataSource.class.getName()); 


	protected static final long MEASURE_INTERVAL_MSEC = 2000;
	private double value;
	private Thread measureThread;

	public CPULoadDataSource() {
		super(0, 100, "Disk Free", "%");
		measureThread = new Thread() {
			@Override
			public void run() {
				while(true)
				{
					try 
					{ 
						synchronized(this) { wait(MEASURE_INTERVAL_MSEC); }
						measure(); 
					}
					catch(Throwable e)
					{
						log.log(Level.SEVERE,"measure error",e);
					}
				}
			}};
			
		measureThread.setName("Disk free Measure");
		measureThread.setDaemon(true);
		measureThread.start();
	}

	protected void measure() throws IOException {

		Process proc = Runtime.getRuntime().exec("df -k -P /cygdrive/c");
		//Process proc = Runtime.getRuntime().exec("df -k -P");
        BufferedReader br = new BufferedReader (new InputStreamReader(proc.getInputStream ()));
        
        //String h = 
        br.readLine(); // skip header
        
        //System.out.println(h);
        
        String s = br.readLine();
        //System.out.println(s);

        
        //String[] word = s.split("[\\p{Space}]"); //java.util.regex.Pattern d
        //String[] word = s.split("[ \t\n\f\r]"); //java.util.regex.Pattern d
        String[] word = s.split("\\s+"); //java.util.regex.Pattern d
 

		
        //System.out.print("["+word[0]+"]");
        //System.out.print("["+word[1]+"]");
        //System.out.print("["+word[2]+"]");
        //System.out.print("["+word[3]+"]");
        String nonpercent = word[4].replaceAll("%", "");
        value = 100 - Double.parseDouble(nonpercent);
        //System.out.print("["+value+"]");
        translateToMeters( (int)value, null );        
	}

	/** called from timer */
	@Override
	public void performMeasure() { 
		//translateToMeters( (int)value ); 
		}


	/**
	 * Nobody above...
	 */
	protected void callSourceDataPumpEntry(DataGeneration currentGeneration)
	{
	}

	@Override
	public String getName() {
		return "CPULoad";
	}


	

}
