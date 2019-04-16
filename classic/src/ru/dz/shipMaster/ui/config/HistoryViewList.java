package ru.dz.shipMaster.ui.config;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import ru.dz.shipMaster.data.history.HistoryRecord;

@SuppressWarnings("serial")
public class HistoryViewList extends BasicItemList<HistoryRecord> {
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(PropertiesEditItemList.class.getName()); 
	private List<HistoryRecord> history;

	private Set<ListDataListener> listeners = new HashSet<ListDataListener>();

	ListModel lm = new ListModel() {

		public void addListDataListener(ListDataListener l) {
			listeners.add(l);
		}

		public Object getElementAt(int index) {
			HistoryRecord record = history.get(index);
//System.out.println("HistoryViewList.getElementAt() "+index+" "+record);			
			return record;
		}

		public int getSize() {
			return history.size();
		}

		public void removeListDataListener(ListDataListener l) {
			listeners.remove(l);
		}

	};


	HistoryRecord lastLast = null;
	protected void fireListUpdate() {
		if(history.size() == 0 )
			return;
		HistoryRecord newLast = history.get(history.size()-1);
		if(newLast == lastLast)
			return;
		
		lastLast = newLast;
		
		list.setSelectedIndex(history.size()-1);
		//list.firePropertyChange(null,0,0);
				
		for( ListDataListener l : listeners )
			l.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, history.size()));
			

		//list.repaint(100);

	}



	Timer timer = new Timer("history view update daemon", true);


	public HistoryViewList(final List<HistoryRecord> items) {
		history = items;
		//this.items = items;
		list = new JList(lm);

		if(true)
		{
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					fireListUpdate();
				}}, 2000, 2000);
		}
		{
			GridBagConstraints outerConstraints = new GridBagConstraints();
			outerConstraints.gridx = GridBagConstraints.RELATIVE;
			outerConstraints.gridy = 0;
			outerConstraints.weightx = 1;
			outerConstraints.weighty = 1;
			outerConstraints.fill = GridBagConstraints.BOTH;
			outerConstraints.anchor = GridBagConstraints.NORTH;
			outerConstraints.ipadx = 10;
			outerConstraints.ipady = 10;

			list.setMinimumSize(new Dimension(100,200));

			JScrollPane scrollPane = new JScrollPane(list);
			add(scrollPane,outerConstraints);		

			list.addMouseListener(new MouseListener() {

				public void mouseClicked(MouseEvent e) {
					if( e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1 )
						openEditDialog();
				}

				public void mouseEntered(MouseEvent e) {/* not used */}
				public void mouseExited(MouseEvent e) {/* not used */}
				public void mousePressed(MouseEvent e) {/* not used */}
				public void mouseReleased(MouseEvent e) {/* not used */}
			});

		}

	}

	protected void openEditDialog() {
		int selectedIndex = list.getSelectedIndex();
		if(selectedIndex < 0)
			return;
		//ItemType i = items.get(selectedIndex);
		//if(i == null)			return;
		//i.displayPropertiesDialog();
	}

}
