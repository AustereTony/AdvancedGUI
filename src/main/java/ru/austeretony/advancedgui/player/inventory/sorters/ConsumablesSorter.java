package ru.austeretony.advancedgui.player.inventory.sorters;

import libs.austeretony.advancedgui.container.framework.GUISorter;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;

public class ConsumablesSorter extends GUISorter {

	@Override
	public boolean isSlotValid(Slot slot) {
		
		return slot.getStack().getItem() instanceof ItemPotion || 
			slot.getStack().getItem() instanceof ItemFood;
	}
	
	@Override
	public boolean shouldAddEmptySlotsAfter() {

		return true;
	}
}
