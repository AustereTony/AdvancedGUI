package ru.austeretony.advancedgui.player.inventory.contextactions;

import libs.austeretony.advancedgui.container.contextmenu.GUIPresetAction;
import libs.austeretony.advancedgui.screen.button.GUISound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import ru.austeretony.advancedgui.inventory.ContainerStorage;
import ru.austeretony.advancedgui.network.NetworkHandler;
import ru.austeretony.advancedgui.network.server.SPContextActionSplitStack;
import ru.austeretony.advancedgui.player.inventory.ContainerInventory;
import ru.austeretony.advancedgui.player.inventory.InventoryExtended;
import ru.austeretony.advancedgui.tiles.TileEntityStorage;

public class ActionSplitStack extends GUIPresetAction {

	public ActionSplitStack(String name) {
		
		super(name);
	}

	@Override
	public boolean isValidAction(IInventory inventory, Slot slot, EntityPlayer player) {
		
		boolean hasSpace = false;
		
		if (inventory instanceof InventoryExtended) {
			
			if (player.openContainer instanceof ContainerInventory) {
				
				hasSpace = ((ContainerInventory) player.openContainer).inventory.getFirstEmptyStack() != - 1;
			}
			
			else if (player.openContainer instanceof ContainerStorage) {
				
				hasSpace = ((ContainerStorage) player.openContainer).playerInventory.getFirstEmptyStack() != - 1;
			}
		}
		
		else if (inventory instanceof TileEntityStorage) {
			
			hasSpace = ((ContainerStorage) player.openContainer).slorageInventory.getFirstEmptyStack() != - 1;
		}
		
		return hasSpace && slot.getStack().stackSize > 1;
	}

	@Override
	public void performAction(IInventory inventory, Slot slot, EntityPlayer player) {
		
		int inventoryId = - 1;
		
		if (inventory instanceof InventoryExtended) {
			
			inventoryId = 0;
		}
		
		else if (inventory instanceof TileEntityStorage) {
			
			inventoryId = 1;
		}
		
		NetworkHandler.sendToServer(new SPContextActionSplitStack(inventoryId, slot.getSlotIndex()));
	}

	@Override
	public GUISound getSound() {
		
		return null;
	}
}
