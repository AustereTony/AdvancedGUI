package ru.austeretony.advancedgui.player.inventory.sorters;

import libs.austeretony.advancedgui.guicontainer.framework.GUISorter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemSword;

public class WeaponSorter extends GUISorter {

	@Override
	public boolean isSlotValid(Slot slot, EntityPlayer player) {
		
		return slot.getStack().getItem() instanceof ItemSword || 
			slot.getStack().getItem() instanceof ItemBow ||
			slot.getStack().getItem() == Items.arrow;
	}

	@Override
	public boolean shouldAddEmptySlotsAfter() {

		return true;
	}
}
