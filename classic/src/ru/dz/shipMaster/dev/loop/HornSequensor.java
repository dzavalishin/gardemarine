package ru.dz.shipMaster.dev.loop;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ru.dz.shipMaster.av.audio.SirenSoundPlayer;
import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.dev.DriverPort;
import ru.dz.shipMaster.dev.PortDataOutput;
import ru.dz.shipMaster.dev.ThreadedDriver;

public class HornSequensor extends ThreadedDriver {
	private static final Logger log = Logger.getLogger(HornSequensor.class.getName());

	private static final String DRIVER_NAME = "Horn Sequencor";

	private boolean 		playing = false;
	private HornSequence	sequence;

	private SirenSoundPlayer siren;
	private boolean localSoundEnabled = false;

	private void sirenStartStop(boolean b) {
		if(!localSoundEnabled) return;

		if( siren == null) 
		{
			try {
				siren = new SirenSoundPlayer();
			} catch (Throwable e) {
				localSoundEnabled = false; // to make sure we don't try to resetup siren on any call
				log.log(Level.SEVERE,"can't create siren",e);
			}
		}

		if( siren != null) siren.startStop(b);
	}



	public HornSequensor() {
		super(1000, Thread.MAX_PRIORITY, DRIVER_NAME);
	}

	/*
	@Override
	protected void doDriverTask() throws Throwable {
		if( (!playing) || (sequence == null) )
		{
			horn(false);
			return;
		}

		SequenceStep step = sequence.getCurrentStep();
		// New sequence?
		if(step == null)
		{
			startNextStep();
			return;
		}

		step.incrementTime();

		if(!step.isOver())
			return;

		startNextStep();
	}
	 */

	//private int pauseTime = 30;

	private SequenceStep step = null; 

	private boolean offOnce = false;
	
	@Override
	protected void doDriverTask() throws Throwable {
		if( (!playing) || (sequence == null) )
		{
			if( offOnce )
			{
				horn(false);
				step = null;
			}
			offOnce = false;
			return;
		}
		offOnce = true;

		if(step != null)
		{
			step.incrementTime();
			if(!step.isOver())
				return;			
		}

		step = sequence.getNextStep();
		// Sequence restarted?
		if(step == null)
		{
			int pt = interval - sequence.getTotalTime();
			if( pt <= 30 ) pt = 30;
			
			if(isDebug())
				System.out.println("HornSequensor.doDriverTask() interval "+interval+" pt "+pt);
			step = new SequenceStep(false, pt);
		}

		step.start();
		horn(step.isOn());
	}


	


	private int lastSoundNumber = -1;
	protected void setSound(int soundNumber, double ena) {
		boolean enable = ena > 0.001;
		if(isDebug())
			System.out.println("HornSequensor.setSound() enable "+enable+" sound "+soundNumber);

		// in any case turn off sound first and inform thread
		// that we're not in kansa... not playing anymore

		if(!enable)
		{
			if(soundNumber == lastSoundNumber)
			{
				playing = false;
				step = null;
				horn(false);
			}
			return;
		}

		lastSoundNumber = soundNumber;
		sequence = sequences[soundNumber];
		playing = true;
	}

	
	

	@Override
	protected void doStartDriver() throws CommunicationsException {
	}

	@Override
	protected void doStopDriver() throws CommunicationsException {
		horn(false);

	}

	private void mkOut(final int nSound, final DriverPort port)
	{
		port.setPortDataOutput( new PortDataOutput() {
			@Override
			public void receiveDoubleData(double value)	{
				if( port.checkIsChanged(value) )
					setSound( nSound, value );	
			}
		} ); 
	}

	private DriverPort [] sounEnablePort = new DriverPort[18];

	private DriverPort soundManualPort;

	private int interval = 30;

	@Override
	protected void updatePorts(Vector<DriverPort> ports) {
		int nport = 0;

		hornPort = getPort(ports, nport++, Direction.Input, Type.Boolean, "Horn control");
		hornPort.setDescription("Must be connected to device controlling horn itself.");

		{
			sounEnablePort[0] = getPort(ports, nport++, Direction.Output, Type.Numeric, "Enable 'Moving' horn sequence");
			mkOut(0,sounEnablePort[0]);
		}

		{
			sounEnablePort[1] = getPort(ports, nport++, Direction.Output, Type.Numeric, "Enable 'Stopped' horn sequence");
			mkOut(1,sounEnablePort[1]);
		}

		{
			sounEnablePort[2] = getPort(ports, nport++, Direction.Output, Type.Numeric, "Enable 'Steering failed' horn sequence");
			mkOut(2,sounEnablePort[2]);
		}

		{
			sounEnablePort[3] = getPort(ports, nport++, Direction.Output, Type.Numeric, "Enable 'Tugging' horn sequence");
			mkOut(3,sounEnablePort[3]);
		}

		{
			sounEnablePort[4] = getPort(ports, nport++, Direction.Output, Type.Numeric, "Enable 'Anchored' horn sequence");
			mkOut(4,sounEnablePort[4]);
		}

		{
			sounEnablePort[5] = getPort(ports, nport++, Direction.Output, Type.Numeric, "Enable 'Right' horn sequence");
			mkOut(5,sounEnablePort[5]);
		}

		{
			sounEnablePort[6] = getPort(ports, nport++, Direction.Output, Type.Numeric, "Enable 'Left' horn sequence");
			mkOut(6,sounEnablePort[6]);
		}

		{
			sounEnablePort[7] = getPort(ports, nport++, Direction.Output, Type.Numeric, "Enable 'Reverse' horn sequence");
			mkOut(7,sounEnablePort[7]);
		}

		{
			sounEnablePort[8] = getPort(ports, nport++, Direction.Output, Type.Numeric, "Enable 'Repeat' horn sequence");
			mkOut(8,sounEnablePort[8]);
		}

		{
			sounEnablePort[9] = getPort(ports, nport++, Direction.Output, Type.Numeric, "Enable 'Pass on Right' horn sequence");
			mkOut(9,sounEnablePort[9]);
		}

		{
			sounEnablePort[10] = getPort(ports, nport++, Direction.Output, Type.Numeric, "Enable 'Pass on Left' horn sequence");
			mkOut(10,sounEnablePort[10]);
		}

		{
			sounEnablePort[11] = getPort(ports, nport++, Direction.Output, Type.Numeric, "Enable 'Acknowledge' horn sequence");
			mkOut(11,sounEnablePort[11]);
		}

		{
			sounEnablePort[12] = getPort(ports, nport++, Direction.Output, Type.Numeric, "Enable 'Turn' horn sequence");
			mkOut(12,sounEnablePort[12]);
		}

		{
			sounEnablePort[13] = getPort(ports, nport++, Direction.Output, Type.Numeric, "Enable 'Alarm' horn sequence");
			mkOut(13,sounEnablePort[13]);
		}

		{
			sounEnablePort[14] = getPort(ports, nport++, Direction.Output, Type.Numeric, "Enable 'Aground' horn sequence");
			mkOut(14,sounEnablePort[14]);
		}

		//

		{
			sounEnablePort[15] = getPort(ports, nport++, Direction.Output, Type.Numeric, "Enable 'Left' light sequence");
			mkOut(15,sounEnablePort[15]);
		}

		{
			sounEnablePort[16] = getPort(ports, nport++, Direction.Output, Type.Numeric, "Enable 'Right' light sequence");
			mkOut(16,sounEnablePort[16]);
		}

		{
			sounEnablePort[17] = getPort(ports, nport++, Direction.Output, Type.Numeric, "Enable 'Back' light sequence");
			mkOut(17,sounEnablePort[17]);
		}

		nport = 64;
		{
			soundManualPort = getPort(ports, nport++, Direction.Output, Type.Numeric, "Manual horn control (stops sequence)");
			soundManualPort.setPortDataOutput( new PortDataOutput() {
				@Override
				public void receiveDoubleData(double value)	{
					//if(isDebug()) System.out.println("manual val "+value);
					if( soundManualPort.checkIsChanged(value) )
					{
						//setSound( 0, 0 );
						playing = false;
						horn(value > 0.1);
					}
				}
			} ); 
		}

		{
			mkIntervalOut(ports, nport++, 30);
			mkIntervalOut(ports, nport++, 60);
			mkIntervalOut(ports, nport++, 90);
			mkIntervalOut(ports, nport++, 120);
		}



	}

	private void mkIntervalOut(Vector<DriverPort> ports, int nport, final int interval) {
		final DriverPort port = getPort(ports, nport, Direction.Output, Type.Numeric, "Set interval "+interval+" (not implemented)");

		port.setPortDataOutput( new PortDataOutput() {
			@Override
			public void receiveDoubleData(double value)	{
				if( port.checkIsChanged(value) )
				{
					if( value > 0.1 )
						setHornInterval( interval );
				}
			}
		} ); 

	}


	protected void setHornInterval(int interval) {
		this.interval = interval;
		if(isDebug())
			System.out.println("HornSequensor.setHornInterval("+interval+")");
		//pauseTime = interval;
	}


	private JCheckBox localSoundField = new JCheckBox(); 

	@Override
	protected void doLoadPanelSettings() {
		localSoundField.setSelected(localSoundEnabled);
	}

	@Override
	protected void doSavePanelSettings() {
		localSoundEnabled = localSoundField.isSelected();
	}

	@Override
	protected void setupPanel(JPanel panel) {
		panel.add(new JLabel("Local sound:"), consL);
		panel.add(localSoundField, consR);
		localSoundField.setToolTipText( "Make computer sound too. Mostly for debug." );
	}

	@Override
	public String getDeviceName() { return DRIVER_NAME; }

	@Override
	public String getInstanceName() { return ""; }

	@Override
	public boolean isAutoSeachSupported() { return false; }

	// ------------------------------------------------------------------------------
	//
	// ------------------------------------------------------------------------------
	private DriverPort hornPort;

	protected void horn(boolean b) {
		if(isStopRequested())
			b = false;
		if(hornPort != null)
			hornPort.sendBooleanData(b);
		log.log(Level.FINE,"horn is "+ (b ? "on" : "off") );
		sirenStartStop(b);
	}



	// ------------------------------------------------------------------------------
	// poor man's midi
	// ------------------------------------------------------------------------------
	private HornSequence	seq_running = new HornSequence(
			new SequenceStep[] {
					new SequenceStep(true,2),
					//new SequenceStep(false,120-2),
			}
	);

	private HornSequence	seq_stopped = new HornSequence(
			new SequenceStep[] {
					new SequenceStep(true,2),
					new SequenceStep(false,2),
					new SequenceStep(true,2),
					//new SequenceStep(false,120-6),
			}
	);

	private HornSequence	seq_failed = new HornSequence(
			new SequenceStep[] {
					new SequenceStep(true,2),
					new SequenceStep(false,2),
					new SequenceStep(true,1),
					new SequenceStep(false,2),
					new SequenceStep(true,1),
					//new SequenceStep(false,120-8),
			}
	);

	private HornSequence	seq_trailed = new HornSequence(
			new SequenceStep[] {
					new SequenceStep(true,2),
					new SequenceStep(false,2),
					new SequenceStep(true,1),
					new SequenceStep(false,2),
					new SequenceStep(true,1),
					new SequenceStep(false,2),
					new SequenceStep(true,1),
					//new SequenceStep(false,120-11),
			}
	);

	private HornSequence	seq_anchor = new HornSequence(
			new SequenceStep[] {
					new SequenceStep(true,1),
					new SequenceStep(false,2),
					new SequenceStep(true,2),
					new SequenceStep(false,2),
					new SequenceStep(true,1),
					//new SequenceStep(false,120-8),
			}
	);

	private HornSequence	seq_right = new HornSequence(
			new SequenceStep[] {
					new SequenceStep(true,1),
					//new SequenceStep(false,120-1),
			}
	);


	private HornSequence	seq_left = new HornSequence(
			new SequenceStep[] {
					new SequenceStep(true,1),
					new SequenceStep(false,1),
					new SequenceStep(true,1),
					//new SequenceStep(false,120-3),
			}
	);

	private HornSequence	seq_reverse = new HornSequence(
			new SequenceStep[] {
					new SequenceStep(true,1),
					new SequenceStep(false,1),
					new SequenceStep(true,1),
					new SequenceStep(false,1),
					new SequenceStep(true,1),
					//new SequenceStep(false,120-5),
			}
	);

	private HornSequence	seq_repeat = new HornSequence(
			new SequenceStep[] {
					new SequenceStep(true,1),
					new SequenceStep(false,1),
					new SequenceStep(true,1),
					new SequenceStep(false,1),
					new SequenceStep(true,1),
					new SequenceStep(false,1),
					new SequenceStep(true,1),
					new SequenceStep(false,1),
					new SequenceStep(true,1),
					new SequenceStep(false,1),
					//new SequenceStep(false,120-10),
			}
	);

	private HornSequence	seq_pass_right = new HornSequence(
			new SequenceStep[] {
					new SequenceStep(true,2),
					new SequenceStep(false,1),
					new SequenceStep(true,2),
					new SequenceStep(false,1),
					new SequenceStep(true,1),
					//new SequenceStep(false,120-7),
			}
	);

	private HornSequence	seq_pass_left = new HornSequence(
			new SequenceStep[] {
					new SequenceStep(true,2),
					new SequenceStep(false,1),
					new SequenceStep(true,2),
					new SequenceStep(false,1),
					new SequenceStep(true,1),
					new SequenceStep(false,1),
					new SequenceStep(true,1),
					//new SequenceStep(false,120-9),
			}
	);


	private HornSequence	seq_ack = new HornSequence(
			new SequenceStep[] {
					new SequenceStep(true,2),
					new SequenceStep(false,1),
					new SequenceStep(true,1),
					new SequenceStep(false,1),
					new SequenceStep(true,2),
					new SequenceStep(false,1),
					new SequenceStep(true,1),
					//new SequenceStep(false,120-9),
			}
	);

	private HornSequence	seq_turn = new HornSequence(
			new SequenceStep[] {
					new SequenceStep(true,2),
					//new SequenceStep(false,120-2),
			}
	);

	private HornSequence	seq_aground = new HornSequence(
			new SequenceStep[] {
					new SequenceStep(true,1),
					new SequenceStep(false,2),
					new SequenceStep(true,1),
					new SequenceStep(false,2),
					new SequenceStep(true,4),
					//new SequenceStep(false,120-2),
			}
	);



	private HornSequence	seq_alarm = new HornSequence(
			new SequenceStep[] {
					new SequenceStep(true,1),
					new SequenceStep(false,1),
					new SequenceStep(true,1),
					new SequenceStep(false,1),
					new SequenceStep(true,1),
					new SequenceStep(false,1),
					new SequenceStep(true,1),
					new SequenceStep(false,1),
					new SequenceStep(true,1),
					new SequenceStep(false,1),
					new SequenceStep(true,1),
					new SequenceStep(false,1),
					new SequenceStep(true,1),
					new SequenceStep(false,1),
					new SequenceStep(true,1),
					new SequenceStep(false,1),
					new SequenceStep(true,2),
					//new SequenceStep(false,120-16),
			}
	);


	private HornSequence	seq_light_right = new HornSequence(
			new SequenceStep[] {
					new SequenceStep(true,1),
					//new SequenceStep(false,120-1),
			}
	);

	private HornSequence	seq_light_left = new HornSequence(
			new SequenceStep[] {
					new SequenceStep(true,1),
					new SequenceStep(false,1),
					new SequenceStep(true,1),
					//new SequenceStep(false,120-3),
			}
	);

	private HornSequence	seq_light_back = new HornSequence(
			new SequenceStep[] {
					new SequenceStep(true,1),
					new SequenceStep(false,1),
					new SequenceStep(true,1),
					new SequenceStep(false,1),
					new SequenceStep(true,1),
					//new SequenceStep(false,120-5),
			}
	);




	private HornSequence[] sequences = 
	{
			seq_running, seq_stopped, seq_failed, seq_trailed, seq_anchor,
			seq_left, seq_right, seq_reverse, seq_repeat, seq_pass_right,
			seq_pass_left, seq_ack, seq_turn, seq_alarm, seq_aground,
			seq_light_right, seq_light_left, seq_light_back
	};



	public static void main(String[] args) {
		final HornSequensor sequensor = new HornSequensor() {
			@Override
			protected void horn(boolean b) {
				System.out.println(".horn("+b+")");
				super.horn(b);
			}

			@Override
			protected void doDriverTask() throws Throwable {
				super.doDriverTask();
				//SequenceStep step = sequensor.sequence.getCurrentStep();
			}
		};

		System.out.println("HornSequensor.main() start");
		sequensor.start();
		sequensor.setSound(4, 1);

		sequensor.sleep(140*1000);
		sequensor.stop();
		System.out.println("HornSequensor.main() stop");
	}

	public boolean isLocalSoundEnabled() {		return localSoundEnabled;	}
	public void setLocalSoundEnabled(boolean localSound) {		this.localSoundEnabled = localSound;	}

}


class HornSequence
{
	SequenceStep[] steps;
	private SequenceStep currentStep;
	private int step = 0;

	private int totalTime = 0;

	public HornSequence(SequenceStep[] iSteps) {
		steps = iSteps;
	}

	public int getTotalTime() {
		if(totalTime <= 0)
		{
			totalTime = 0;
			for( SequenceStep s : steps )
				totalTime += s.getTime();
			System.out.println("HornSequence.getTotalTime() = "+totalTime);
		}
		return totalTime;
	}

	public void start()
	{
		step = -1; // getNextStep will step to 0
		currentStep = null;		
	}

	public SequenceStep getCurrentStep() { return currentStep; }

	public SequenceStep getNextStep() {
		step++;

		if(step >= steps.length)
		{
			step = -1;
			currentStep = null;
		}
		else
			currentStep = steps[step];		

		return currentStep;
	}

	public void restart()
	{
		step = 0;
		currentStep = steps[step];
	}

}


class SequenceStep
{
	private boolean			on;
	private int				time;

	private int				spentTime;

	public SequenceStep(boolean on, int time) {
		super();
		this.on = on;
		this.time = time;
	}

	public void start() { spentTime = 0; }
	public void incrementTime() { spentTime++; }
	public boolean isOver() { return spentTime >= time; }


	public boolean isOn() {		return on;	}
	public int getTime() {		return time;	}

}
