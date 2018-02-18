package ru.austeretony.advancedgui.player.inventory.sorters;

import libs.austeretony.advancedgui.guicontainer.framework.GUISorter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemTool;

public class ToolsSorter extends GUISorter {

	@Override
	public boolean isSlotValid(Slot slot, EntityPlayer player) {
		
		return slot.getStack().getItem() instanceof ItemTool || 
			slot.getStack().getItem() == Items.bucket ||
			slot.getStack().getItem() == Items.clock ||
			slot.getStack().getItem() == Items.compass ||
			slot.getStack().getItem() == Items.map ||
			slot.getStack().getItem() == Items.flint_and_steel ||
			slot.getStack().getItem() == Items.shears ||
			slot.getStack().getItem() == Items.fishing_rod;
	}
	
	@Override
	public boolean shouldAddEmptySlotsAfter() {

		return true;
	}
}
