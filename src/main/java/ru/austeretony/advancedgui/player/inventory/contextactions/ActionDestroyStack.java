package ru.austeretony.advancedgui.player.inventory.contextactions;

import libs.austeretony.advancedgui.container.contextmenu.GUIPresetAction;
import libs.austeretony.advancedgui.screen.button.GUISound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import ru.austeretony.advancedgui.main.SoundHandler;
import ru.austeretony.advancedgui.network.NetworkHandler;
import ru.austeretony.advancedgui.network.server.SPContextActionDestroyStack;
import ru.austeretony.advancedgui.player.inventory.InventoryExtended;
import ru.austeretony.advancedgui.tiles.TileEntityStorage;

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
		
		inventory.setInventorySlotContents(slot.getSlotIndex(), ItemStack.EMPTY);
		
		int inventoryId = - 1;
		
		if (inventory instanceof InventoryExtended) {
			
			inventoryId = 0;
		}
		
		else if (inventory instanceof TileEntityStorage) {
			
			inventoryId = 1;
		}
		
		else if (inventory instanceof InventoryPlayer) {
			
			inventoryId = 2;
		}
		
		NetworkHandler.sendToServer(new SPContextActionDestroyStack(inventoryId, slot.getSlotIndex()));
	}

	@Override
	public GUISound getSound() {
		
		return new GUISound(SoundHandler.CLICK, 1.0F, 1.0F);
	}
}
