package ru.dz.gardemarine.ui.frames;

import ru.dz.gardemarine.world.IConfig;
import ru.dz.gardemarine.world.IItem;

public interface IWorldEditor {
	void deleteItem(IConfig<? extends IItem> toDelete);
	void addItem(IConfig<? extends IItem> toAdd);
}
