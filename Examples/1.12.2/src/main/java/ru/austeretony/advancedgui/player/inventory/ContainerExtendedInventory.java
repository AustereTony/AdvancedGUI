package ru.austeretony.advancedgui.player.inventory;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerExtendedInventory extends Container {
	
	private final EntityPlayer thePlayer;
	
	public final InventoryExtended inventory;
	
    private static final EntityEquipmentSlot[] VALID_EQUIPMENT_SLOTS = new EntityEquipmentSlot[] {EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};
	
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
	
	public ContainerExtendedInventory(EntityPlayer player, InventoryExtended inventory) {
		
		this.thePlayer = player;
		
		this.inventory = inventory;
		
		int j, k;
		
		int slotDisatance = 18;	
		
		/*
		 * Для GUIFramework используется последовательность слотов идентичная последовательности
		 * добавления слотов в контейнер.
		 */
		
		for (j = 0; j < 4; j++) {
			
            final EntityEquipmentSlot entityEquipmentSlot = VALID_EQUIPMENT_SLOTS[j];
            
            //Слоты брони добавляются в массив Container#inventorySlots на позиции 0 - 3.
            this.addSlotToContainer(new Slot(player.inventory, 36 + (3 - j), 117, 74 + j * slotDisatance) {
            	
                public int getSlotStackLimit() {
                	
                    return 1;
                }

                public boolean isItemValid(ItemStack itemStack) {
                	
                    return itemStack.getItem().isValidArmor(itemStack, entityEquipmentSlot, player);
                }

                @Nullable
                @SideOnly(Side.CLIENT)
                public String getSlotTexture() {
                	
                    return ItemArmor.EMPTY_SLOT_NAMES[entityEquipmentSlot.getIndex()];
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
    	
		ItemStack itemStack = ItemStack.EMPTY;
		
		Slot slot = (Slot) this.inventorySlots.get(slotIndex);
		
		int slotId = slot.getSlotIndex();

		if (slot != null && slot.getHasStack()) {
			
			ItemStack itemStackSlot = slot.getStack();
			
			itemStack = itemStackSlot.copy();
			
            EntityEquipmentSlot entityEquipmentSlot = EntityLiving.getSlotForItemStack(itemStack);
			
			if (slotIndex >= this.SLOT_ARMOR_1 && slotIndex <= this.SLOT_ARMOR_4) {
				
	    		if (!this.mergeItemStack(itemStackSlot, this.SLOT_INVENTORY_FIRST, this.SLOT_INVENTORY_LAST, false)) {
					
	    			return ItemStack.EMPTY;
	    		}	
			}
	    		
			else if (slotIndex >= this.SLOT_HOTBAR_1 && slotIndex <= this.SLOT_HOTBAR_9) {
				
	    		if (!this.mergeItemStack(itemStackSlot, this.SLOT_INVENTORY_FIRST, this.SLOT_INVENTORY_LAST, false)) {
						
	    			return ItemStack.EMPTY;
	    		}	    		
	    	}
	    	
			else if (slotIndex >= this.SLOT_INVENTORY_FIRST && slotIndex <= this.SLOT_INVENTORY_LAST) {
	    		
	            if (entityEquipmentSlot.getSlotType() == EntityEquipmentSlot.Type.ARMOR && !((Slot) this.inventorySlots.get(3 - entityEquipmentSlot.getIndex())).getHasStack()) {
	            	
	                int i = 3 - entityEquipmentSlot.getIndex();

	                if (!this.mergeItemStack(itemStackSlot, i, i + 1, false)) {
	                    	
	                	return ItemStack.EMPTY;
	                }
	            }
				
	            else if (!this.mergeItemStack(itemStackSlot, this.SLOT_HOTBAR_1, this.SLOT_HOTBAR_9 + 1, false)) {
						
	    			return ItemStack.EMPTY;
	    		}
	    	}

			if (itemStackSlot.getCount() == 0) {
				
				slot.putStack((ItemStack) ItemStack.EMPTY);
			}
			
			else {
				
				slot.onSlotChanged();
			}

			if (itemStackSlot.getCount() == itemStack.getCount()) {
				
				return ItemStack.EMPTY;
			}

			slot.onTake(player, itemStackSlot);
		}

		return itemStack;
	}
    
    public void onContainerClosed(EntityPlayer player) {}
}

