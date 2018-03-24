package ru.austeretony.advancedgui.player.inventory.sorters;

import libs.austeretony.advancedgui.container.framework.GUISorter;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;

public class ArmorSorter extends GUISorter {

	@Override
	public boolean isSlotValid(Slot slot) {
		
		return slot.getStack().getItem() instanceof ItemArmor;
	}
	
	@Override
	public boolean shouldAddEmptySlotsAfter() {

		return true;
	}
}
