package ru.austeretony.advancedgui.player.inventory.sorters;

import libs.austeretony.advancedgui.guicontainer.framework.GUISorter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;

public class MaterialsSorter extends GUISorter {

	@Override
	public boolean isSlotValid(Slot slot, EntityPlayer player) {
		
		return slot.getStack().getItem() == Items.iron_ingot ||
			slot.getStack().getItem() == Items.gold_ingot ||
			slot.getStack().getItem() == Items.diamond ||
			slot.getStack().getItem() == Items.emerald ||
			slot.getStack().getItem() == Items.coal ||			
			slot.getStack().getItem() == Items.blaze_rod ||
			slot.getStack().getItem() == Items.gunpowder ||
			slot.getStack().getItem() == Items.glowstone_dust ||	
			slot.getStack().getItem() == Items.redstone ||
			slot.getStack().getItem() == Items.paper ||
			slot.getStack().getItem() == Items.clay_ball ||
			slot.getStack().getItem() == Items.magma_cream ||
			slot.getStack().getItem() == Items.ghast_tear ||
			slot.getStack().getItem() == Items.gold_nugget ||
			slot.getStack().getItem() == Items.flint ||
			slot.getStack().getItem() == Items.leather;
	}
	
	@Override
	public boolean shouldAddEmptySlotsAfter() {

		return true;
	}
}
