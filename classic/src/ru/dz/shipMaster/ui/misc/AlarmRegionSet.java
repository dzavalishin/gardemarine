package ru.dz.shipMaster.ui.misc;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

public class AlarmRegionSet {

	TreeSet<AlarmRegion> regions = new TreeSet<AlarmRegion>();

	/**
	 * @param e
	 * @return
	 * @see java.util.TreeSet#add(java.lang.Object)
	 */
	public boolean add(AlarmRegion e) {
		return regions.add(e);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.TreeSet#addAll(java.util.Collection)
	 */
	public boolean addAll(Collection<? extends AlarmRegion> c) {
		return regions.addAll(c);
	}

	/**
	 * 
	 * @see java.util.TreeSet#clear()
	 */
	public void clear() {
		regions.clear();
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.TreeSet#contains(java.lang.Object)
	 */
	public boolean contains(Object o) {
		return regions.contains(o);
	}

	/**
	 * @return
	 * @see java.util.TreeSet#isEmpty()
	 */
	public boolean isEmpty() {
		return regions.isEmpty();
	}

	/**
	 * @return
	 * @see java.util.TreeSet#iterator()
	 */
	public Iterator<AlarmRegion> iterator() {
		return regions.iterator();
	}

	/**
	 * @return
	 * @see java.util.TreeSet#size()
	 */
	public int size() {
		return regions.size();
	}

	/**
	 * @return
	 * @see java.util.AbstractCollection#toString()
	 */
	public String toString() {
		return regions.toString();
	} 
	
}
