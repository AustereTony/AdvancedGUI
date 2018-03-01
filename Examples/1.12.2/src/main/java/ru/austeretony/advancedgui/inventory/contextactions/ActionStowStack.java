package ru.austeretony.advancedgui.inventory.contextactions;

import libs.austeretony.advancedgui.container.contextmenu.GUIPresetAction;
import libs.austeretony.advancedgui.screen.button.GUIButtonSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import ru.austeretony.advancedgui.inventory.ContainerStorage;
import ru.austeretony.advancedgui.network.NetworkHandler;
import ru.austeretony.advancedgui.network.server.SPContextActionStowStack;

public class ActionStowStack extends GUIPresetAction {

	public ActionStowStack(String name) {
		
		super(name);
	}

	@Override
	public boolean isValidAction(IInventory inventory, Slot slot, EntityPlayer player) {
		
		return ((ContainerStorage) player.openContainer).slorageInventory.getFirstEmptyStack() != - 1;
	}

	@Override
	public void performAction(IInventory inventory, Slot slot, EntityPlayer player) {
		
		slot.inventory.setInventorySlotContents(slot.getSlotIndex(), ItemStack.EMPTY);
		
		NetworkHandler.sendToServer(new SPContextActionStowStack(slot.getSlotIndex()));
	}

	@Override
	public GUIButtonSound getSound() {
		
		return null	;
	}
}
