package soii.warwolves.items;

import net.minecraft.item.Item;
import soii.warwolves.Warwolves;

public class ItemBase extends Item {
	public ItemBase() {
		super((new Item.Properties()).group((Warwolves.TAB)));
	}
}