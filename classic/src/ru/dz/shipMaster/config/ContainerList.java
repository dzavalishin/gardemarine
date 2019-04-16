package ru.dz.shipMaster.config;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

import ru.dz.shipMaster.ui.config.BasicItemContainer;

public class ContainerList {
	private Set< WeakReference< BasicItemContainer<? extends IConfigListItem>>> containers = new HashSet< WeakReference< BasicItemContainer<? extends IConfigListItem>>>();

	public void addContainer(BasicItemContainer<? extends IConfigListItem> c)
	{
		containers.add(new WeakReference<BasicItemContainer<? extends IConfigListItem>>(c) );
	}

	public void removeContainer(BasicItemContainer<? extends IConfigListItem> c)
	{
		//containers.remove(c);
		for( WeakReference< BasicItemContainer<? extends IConfigListItem>> w : containers )
		{
			if( (w.get() == null) || (w.get() == c) )
				containers.remove(w);
		}
	}

	public void informContainers(IConfigListItem i)
	{
		for( WeakReference< BasicItemContainer<? extends IConfigListItem>> w : containers )
		{
			BasicItemContainer<? extends IConfigListItem> c = w.get();
			if( c != null ) c.updateItem(i);
			else containers.remove(w);
		}
	}

}
