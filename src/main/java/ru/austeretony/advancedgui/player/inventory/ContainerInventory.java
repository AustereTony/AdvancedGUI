package ru.austeretony.advancedgui.player.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ContainerInventory extends Container {
	
	private final EntityPlayer thePlayer;
	
	public final InventoryExtended inventory;
	
	public final static int 
	SLOT_ARMOR_1 = 0,
	SLOT_ARMOR_2 = 1,
	SLOT_ARMOR_3 = 2,
	SLOT_ARMOR_4 = 3,

	SLOT_HOTBAR_1 = 4,
	SLOT_HOTBAR_2 = 5,
	SLOT_HOTBAR_3 = 6,
	SLOT_HOTBAR_4 = 7,
	SLOT_HOTBAR_5 = 8,
	SLOT_HOTBAR_6 = 9,
	SLOT_HOTBAR_7 = 10,
	SLOT_HOTBAR_8 = 11,
	SLOT_HOTBAR_9 = 12,
	
	SLOT_INVENTORY_FIRST = 13,
	SLOT_INVENTORY_LAST = 162;
	
	public ContainerInventory(EntityPlayer player, InventoryExtended inventory) {
		
		this.thePlayer = player;
		
		this.inventory = inventory;
		
		int j, k;
		
		int slotDisatance = 18;	
		
		/*
		 * Для GUIFramework используется последовательность слотов идентичная последовательности
		 * добавления слотов в контейнер.
		 */
		
		for (j = 0; j < 4; j++) {
			
            final int i = j;
            
            //Слоты брони добавляются в массив Container#inventorySlots на позиции 0 - 3.
            this.addSlotToContainer(new Slot(player.inventory, player.inventory.getSizeInventory() - 1 - i, 117, 74 + j * slotDisatance) {
            	
                public int getSlotStackLimit() {
                	
                    return 1;
                }

                public boolean isItemValid(ItemStack itemStack) {
                	
                    if (itemStack == null) return false;
                    
                    return itemStack.getItem().isValidArmor(itemStack, i, thePlayer);
                }

                @SideOnly(Side.CLIENT)
                public IIcon getBackgroundIconIndex() {
                	
                    return ItemArmor.func_94602_b(i);
                }
            });
		}
		
        //Слоты хотбара добавляются в массив Container#inventorySlots на позиции 4 - 13. Да, не 12, а почему то 13...
		for (j = 0; j < 9; j++) {
			
	        this.addSlotToContainer(new Slot(player.inventory, j, 69 + j * slotDisatance, 188));
		}
		
        //Слоты инвентаря добавляются в массив Container#inventorySlots на позиции 13 - 162.
		for (j = 0; j < 150; j++) {
					
			this.addSlotToContainer(new Slot(inventory, j, 0, 0));
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
			
			if (slotIndex >= this.SLOT_ARMOR_1 && slotIndex <= this.SLOT_ARMOR_4) {
				
	    		if (!this.mergeItemStack(itemStackSlot, this.SLOT_INVENTORY_FIRST, this.SLOT_INVENTORY_LAST, false)) {
					
	    			return null;
	    		}	
			}
	    		
			else if (slotIndex >= this.SLOT_HOTBAR_1 && slotIndex <= this.SLOT_HOTBAR_9) {
				
	    		if (!this.mergeItemStack(itemStackSlot, this.SLOT_INVENTORY_FIRST, this.SLOT_INVENTORY_LAST, false)) {
						
	    			return null;
	    		}	    		
	    	}
	    	
			else if (slotIndex >= this.SLOT_INVENTORY_FIRST && slotIndex <= this.SLOT_INVENTORY_LAST) {
	    		
	            if (itemStack.getItem() instanceof ItemArmor && !((Slot) this.inventorySlots.get(((ItemArmor) itemStack.getItem()).armorType)).getHasStack()) {
	            	
	                int j = ((ItemArmor) itemStack.getItem()).armorType;

	                if (!this.mergeItemStack(itemStackSlot, j, j + 1, false)) {
	                	
	                    return null;
	                }
	            }
				
	            else if (!this.mergeItemStack(itemStackSlot, this.SLOT_HOTBAR_1, this.SLOT_HOTBAR_9 + 1, false)) {
						
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
