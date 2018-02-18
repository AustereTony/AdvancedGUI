package ru.austeretony.advancedgui.player.inventory.sorters;

import libs.austeretony.advancedgui.guicontainer.framework.GUISorter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class CommonSorter extends GUISorter {

	@Override
	public boolean isSlotValid(Slot slot, EntityPlayer player) {
		
		return slot.getHasStack();//Будут добавлены все слоты с предметами подряд.
	}

	@Override
	public boolean shouldAddEmptySlotsAfter() {
		
		return true;//Пустые слоты будут добавлены после слотов с предметами.
	}
}
