package ru.austeretony.advancedgui.player.inventory.sorters;

import libs.austeretony.advancedgui.container.framework.GUISorter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemTool;

public class ToolsSorter extends GUISorter {

	@Override
	public boolean isSlotValid(Slot slot, EntityPlayer player) {
		
		return slot.getStack().getItem() instanceof ItemTool || 
			slot.getStack().getItem() == Items.BUCKET ||
			slot.getStack().getItem() == Items.CLOCK ||
			slot.getStack().getItem() == Items.COMPASS ||
			slot.getStack().getItem() == Items.MAP ||
			slot.getStack().getItem() == Items.FLINT_AND_STEEL ||
			slot.getStack().getItem() == Items.SHEARS ||
			slot.getStack().getItem() == Items.FISHING_ROD;
	}

	@Override
	public boolean shouldAddEmptySlotsAfter() {
		
		return true;
	}
}
