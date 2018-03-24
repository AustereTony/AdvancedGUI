	package ru.austeretony.advancedgui.player.inventory.contextactions;

import libs.austeretony.advancedgui.container.contextmenu.GUIPresetAction;
import libs.austeretony.advancedgui.screen.button.GUISound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import ru.austeretony.advancedgui.network.NetworkHandler;
import ru.austeretony.advancedgui.network.server.SPContextActionUnequip;
import ru.austeretony.advancedgui.player.inventory.ContainerInventory;

public class ActionUnequip extends GUIPresetAction {

	public ActionUnequip(String name) {
		
		super(name);
	}

	@Override
	public boolean isValidAction(IInventory inventory, Slot slot, EntityPlayer player) {
		
		return ((ContainerInventory) player.openContainer).inventory.getFirstEmptyStack() != - 1;
	}

	@Override
	public void performAction(IInventory inventory, Slot slot, EntityPlayer player) {
		
		int inventoryId = - 1;
		
		if (inventory instanceof InventoryPlayer) {
			
			inventoryId = 2;
		}
		
		NetworkHandler.sendToServer(new SPContextActionUnequip(inventoryId, slot.getSlotIndex()));
	}

	@Override
	public GUISound getSound() {
		
		//return new GUIButtonSound(new ResourceLocation(AdvancedGUIMain.MODID + ":" + "click"), 1.0F);
		return null;
	}
}
