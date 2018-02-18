package ru.austeretony.advancedgui.player.inventory.contextactions;

import libs.austeretony.advancedgui.guicontainer.button.GUIButtonSound;
import libs.austeretony.advancedgui.guicontainer.contextmenu.GUIPresetAction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import ru.austeretony.advancedgui.network.NetworkHandler;
import ru.austeretony.advancedgui.network.server.SPContextActionDestroyStack;
import ru.austeretony.advancedgui.player.inventory.PlayerInventory;
import ru.austeretony.advancedgui.tiles.TileEntityAdvancedStorage;

public class ActionDestroyStack extends GUIPresetAction {

	public ActionDestroyStack(String name) {
		
		super(name);
	}

	@Override
	public boolean isValidAction(IInventory inventory, Slot slot, EntityPlayer player) {
		
		return true;
	}

	@Override
	public void performAction(IInventory inventory, Slot slot, EntityPlayer player) {
		
		inventory.setInventorySlotContents(slot.getSlotIndex(), null);
		
		int inventoryId = - 1;
		
		if (inventory instanceof PlayerInventory) {
			
			inventoryId = 0;
		}
		
		else if (inventory instanceof TileEntityAdvancedStorage) {
			
			inventoryId = 1;
		}
		
		else if (inventory instanceof InventoryPlayer) {
			
			inventoryId = 2;
		}
		
		NetworkHandler.sendToServer(new SPContextActionDestroyStack(inventoryId, slot.getSlotIndex()));
	}

	@Override
	public GUIButtonSound getSound() {
		
		return null;
	}
}
