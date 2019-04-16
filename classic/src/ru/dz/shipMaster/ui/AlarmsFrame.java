package ru.dz.shipMaster.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ru.dz.shipMaster.av.audio.SirenSoundPlayer;
import ru.dz.shipMaster.config.ConfigurationFactory;
import ru.dz.shipMaster.config.items.CliAlarm.AlarmProcessor;
import ru.dz.shipMaster.config.items.CliAlarmStation;

@SuppressWarnings("serial")
public class AlarmsFrame extends JFrame {
	protected static final Logger log = Logger.getLogger(AlarmsFrame.class.getName()); 

	private final CliAlarmStation owner;

	private Set<AlarmProcessor> sounding = new HashSet<AlarmProcessor>();
	private Set<AlarmProcessor> muted = new HashSet<AlarmProcessor>();

	//private JList list = new JList();
	private JPanel panel = new JPanel(new GridLayout(0, 1));

	public AlarmsFrame(CliAlarmStation owner) {
		this.owner = owner;
		
		setTitle("Тревога"); // TODO internationalize

		add(panel);

		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		//panel.add(list);
	}


	private void populateList()
	{
		Set<AlarmProcessor> activeAlarms = owner.getActiveAlarms();
		/*list.removeAll();
		for( AlarmProcessor ap : activeAlarms )
			add( new ListItem(ap) );
		*/
		/*
		DefaultListModel model = (DefaultListModel) list.getModel();
		
		for( AlarmProcessor ap : activeAlarms )
			model.addElement( new ListItem(ap) );
		*/
		
		panel.removeAll();
		for( AlarmProcessor ap : activeAlarms )
			panel.add( new ListItem(ap, muted.contains(ap)) );
		
		
		pack();
	}


	private void updateActive()
	{
		Set<AlarmProcessor> activeAlarms = owner.getActiveAlarms();

		synchronized (activeAlarms) {
			muted.retainAll(activeAlarms);

			sounding.clear();
			sounding.addAll(activeAlarms);
			sounding.removeAll(muted);

			boolean showMe = !activeAlarms.isEmpty(); 

			if(!ConfigurationFactory.getConfiguration().getGeneral().isSuppressAlarms())
				setVisible(showMe);
//System.out.println("AlarmsFrame.updateActive() muted "+muted.size()+" sounding "+sounding.size());			
		}
	}


	public void mute(AlarmProcessor ap) {
		Set<AlarmProcessor> activeAlarms = owner.getActiveAlarms();

		synchronized (activeAlarms) {
			muted.add(ap);
			sounding.remove(ap);
		}
		updateSound();
	}


	private static SirenSoundPlayer siren;
	
	static {
		try {
			siren = new SirenSoundPlayer();
		} catch (Throwable e) {
			log.log(Level.SEVERE,"Can't create siren", e);
		}
	}

	static public void stopSound()
	{
		if(siren != null)
			siren.startStop(false);
	}

	public void update()
	{
		updateActive();
		updateSound();
		populateList();

	}


	private void updateSound() {

		if(!owner.isSoundEnabled())
			return;
/*
		if(siren == null)
		{
			try {
				siren = new SirenSoundPlayer();
			} catch (Throwable e) {
				log.log(Level.SEVERE,"Can't create siren", e);
			}
		}
*/		
		if(siren == null)
			return;
		
		siren.startStop(
				(!sounding.isEmpty())
				&&
				!ConfigurationFactory.getConfiguration().getGeneral().isSuppressAlarms()
				);


	}


	private static final int MINLEN = 60;
	private static String spaces = "                                                                      "; 
	
	class ListItem extends JPanel
	{
		JButton silence = new JButton();
		
		
		private final AlarmProcessor ap;
		public ListItem( AlarmProcessor ap, boolean isSilent ) {
			this.ap = ap;
	
			silence.setSize(24, 24);
			
			setLayout(new GridLayout(1,0));
			
			String name = " "+ap.getName();
			if(name.length() < MINLEN)
				name += spaces.substring(0, MINLEN-name.length());
//System.out.println("AlarmsFrame.ListItem.ListItem() '"+name+"'\n");				
			add(new JLabel( name ) );
			add(silence);
			silence.addActionListener(new AbstractAction() {

				@Override
				public void actionPerformed(ActionEvent e) {
					doSilence();					
				}
			});
			silence.setText("X");
			silence.setEnabled(!isSilent);
		}

		protected void doSilence() {
			mute(ap);
			silence.setEnabled(false);
		}

		AlarmProcessor getAlarmProcessor() { return ap; }
	}


	/**
	 * @param args
	 * /
	public static void main(String[] args) {

	}*/


	
	

}
