package ru.austeretony.advancedgui.player.inventory.sorters;

import libs.austeretony.advancedgui.container.framework.GUISorter;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;

public class MaterialsSorter extends GUISorter {

	@Override
	public boolean isSlotValid(Slot slot) {
		
		return slot.getStack().getItem() == Items.IRON_INGOT ||
			slot.getStack().getItem() == Items.GOLD_INGOT ||
			slot.getStack().getItem() == Items.DIAMOND ||
			slot.getStack().getItem() == Items.EMERALD ||
			slot.getStack().getItem() == Items.COAL ||			
			slot.getStack().getItem() == Items.BLAZE_ROD ||
			slot.getStack().getItem() == Items.GUNPOWDER ||
			slot.getStack().getItem() == Items.GLOWSTONE_DUST ||	
			slot.getStack().getItem() == Items.REDSTONE ||
			slot.getStack().getItem() == Items.PAPER ||
			slot.getStack().getItem() == Items.CLAY_BALL ||
			slot.getStack().getItem() == Items.MAGMA_CREAM ||
			slot.getStack().getItem() == Items.GHAST_TEAR ||
			slot.getStack().getItem() == Items.GOLD_NUGGET ||
			slot.getStack().getItem() == Items.FLINT ||
			slot.getStack().getItem() == Items.LEATHER;
	}
	
	@Override
	public boolean shouldAddEmptySlotsAfter() {
		
		return true;
	}
}
