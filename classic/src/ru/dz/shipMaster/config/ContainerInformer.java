package ru.dz.shipMaster.config;

import javax.swing.SwingUtilities;

import ru.dz.shipMaster.ui.config.BasicItemContainer;

public abstract class ContainerInformer implements IConfigListItem {

	/*
    private Set<BasicItemContainer<? extends IConfigListItem>> containers = new HashSet<BasicItemContainer<? extends IConfigListItem>>();

    public void addContainer(BasicItemContainer<? extends IConfigListItem> c)
    {
    	containers.add(c);
    }

    public void removeContainer(BasicItemContainer<? extends IConfigListItem> c)
    {
    	containers.remove(c);
    }

    public void informContainers()
    {
    	for( BasicItemContainer<? extends IConfigListItem> c : containers )
    		c.updateItem(this);
    }*/


	private ContainerList clist = new ContainerList();

	@Override
	public void addContainer(BasicItemContainer<? extends IConfigListItem> c) {		clist.addContainer(c);	}

	@Override
	public void informContainers() {		

		Runnable doWorkRunnable = new Runnable() {
			public void run() { clist.informContainers(ContainerInformer.this); }
		};
		SwingUtilities.invokeLater(doWorkRunnable);

	}

	@Override
	public void removeContainer(BasicItemContainer<? extends IConfigListItem> c) {		clist.removeContainer(c);	} 


}
