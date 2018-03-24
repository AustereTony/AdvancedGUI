package ru.austeretony.advancedgui.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import ru.austeretony.advancedgui.player.inventory.InventoryExtended;
import ru.austeretony.advancedgui.tiles.TileEntityStorage;

public class ContainerStorage extends Container {
	
	public final InventoryExtended playerInventory;
	
	public final TileEntityStorage slorageInventory;
	
	public final static int 	
	SLOT_STORAGE_FIRST = 0,
	SLOT_STORAGE_LAST = 149,
	
	SLOT_INVENTORY_FIRST = 150,
	SLOT_INVENTORY_LAST = 499;
	
	public ContainerStorage(EntityPlayer player, InventoryExtended inventory, TileEntityStorage tileEntity) {
		
		this.playerInventory = inventory;
		
		this.slorageInventory = tileEntity;
		
		int j;
		
		for (j = 0; j < 350; j++) {
			
	        this.addSlotToContainer(new Slot(tileEntity, j, 100, 100));//Добавление слотов инвентяря в Container#inventorySlots. Позиции будут указаны в ГПИ.
		}
		
		for (j = 0; j < 150; j++) {
			
			this.addSlotToContainer(new Slot(inventory, j, 100, 100));
		}
	}	

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		
		return true;
	}
	
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {  
    	
		ItemStack itemStack = null;
		
		Slot slot = (Slot) this.inventorySlots.get(slotIndex);
		
		int slotId = slot.getSlotIndex();

		if (slot != null && slot.getHasStack()) {
			
			ItemStack itemStackSlot = slot.getStack();
			
			itemStack = itemStackSlot.copy();
	    		
	    	if (slotIndex >= this.SLOT_INVENTORY_FIRST && slotIndex <= this.SLOT_INVENTORY_LAST) {
				
	    		if (!this.mergeItemStack(itemStackSlot, this.SLOT_STORAGE_FIRST, this.SLOT_STORAGE_LAST, false)) {
						
	    			return null;
	    		}
	    	}
	    	
	    	if (slotIndex >= this.SLOT_STORAGE_FIRST && slotIndex <= this.SLOT_STORAGE_LAST) {
				
	    		if (!this.mergeItemStack(itemStackSlot, this.SLOT_INVENTORY_FIRST, this.SLOT_INVENTORY_LAST, false)) {
						
	    			return null;
	    		}
	    	}

			if (itemStackSlot.stackSize == 0) {
				
				slot.putStack((ItemStack) null);
			}
			
			else {
				
				slot.onSlotChanged();
			}

			if (itemStackSlot.stackSize == itemStack.stackSize) {
				
				return null;
			}

			slot.onPickupFromSlot(player, itemStackSlot);
		}

		return itemStack;
	}
    
    public void onContainerClosed(EntityPlayer player) {}
}

