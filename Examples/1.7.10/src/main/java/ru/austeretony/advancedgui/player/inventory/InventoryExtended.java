package ru.austeretony.advancedgui.player.inventory;

import java.util.concurrent.Callable;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ReportedException;

public class InventoryExtended implements IInventory {
	
	private static final String 
	NAME = "extendedInventory",
	NAME_TAG = "extendedInventoryTag";
	 
	public static final int INVENTORY_MAX_SIZE = 150;
		
	public ItemStack[] inventoryArray = new ItemStack[this.INVENTORY_MAX_SIZE];
	
	public EntityPlayer player;
	
	public InventoryExtended() {}
	
	public void clearInventory() {
		
		for (int i = 0; i < this.inventoryArray.length; i++) {
			
			this.inventoryArray[i] = null;
		}
	}

	@Override
	public int getSizeInventory() {
		
		return this.inventoryArray.length;
	}
	
	@Override
	public ItemStack getStackInSlot(int slotId) {
				
		return inventoryArray[slotId];
	}

	@Override
	public ItemStack decrStackSize(int slotId, int amount) {
		
		ItemStack itemStack = this.getStackInSlot(slotId);
		
		if (itemStack != null) {
			
			if (itemStack.stackSize > amount) {
				
				itemStack = itemStack.splitStack(amount);
				
				this.markDirty();
			}
			
			else {
				
				this.setInventorySlotContents(slotId, null);
			}
		}
		
		return itemStack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slotId) {
		
		ItemStack itemStack = this.getStackInSlot(slotId);
		
		this.setInventorySlotContents(slotId, null);
		
		return itemStack;
	}

	@Override
	public void setInventorySlotContents(int slotId, ItemStack itemStack) {
		
			
		if (slotId < this.inventoryArray.length) {
				
			this.inventoryArray[slotId] = itemStack;
		}

		if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
			
			itemStack.stackSize = this.getInventoryStackLimit();
		}

		this.markDirty();
	}

	@Override
	public String getInventoryName() {
		
		return this.NAME;
	}

	@Override
	public boolean hasCustomInventoryName() {
		
		return this.NAME.length() > 0;
	}

	@Override
	public int getInventoryStackLimit() {
		
		return 64;
	}

	@Override
	public void markDirty() {
		
		for (int i = 0; i < this.getSizeInventory(); ++i) {
			
			if (this.getStackInSlot(i) != null && this.getStackInSlot(i).stackSize == 0) {
								
				this.inventoryArray[i] = null;
			}
		}
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		
		return true;
	}
	
	public void writeToNBT(NBTTagCompound tagCompound) {
		
		int i;
		
		NBTTagList itemsList = new NBTTagList();

		for (i = 0; i < this.getSizeInventory(); ++i) {
			
			if (this.getStackInSlot(i) != null) {
				
				NBTTagCompound itemCompound = new NBTTagCompound();
				
				itemCompound.setShort("slot", (short) i);
				
				this.getStackInSlot(i).writeToNBT(itemCompound);
				
				itemsList.appendTag(itemCompound);
			}
		}
	
		tagCompound.setTag(this.NAME_TAG, itemsList);	
	}

	public void readFromNBT(NBTTagCompound tagCompound) {		
		
		short slotId;
		
		int i, j;
		
		NBTTagList itemsList = tagCompound.getTagList(NAME_TAG, tagCompound.getId());

		for (i = 0; i < itemsList.tagCount(); ++i) {
			
			NBTTagCompound itemCompound = (NBTTagCompound) itemsList.getCompoundTagAt(i);
			
			slotId = itemCompound.getShort("slot");

			j = itemCompound.getShort("slot");
			
			if (slotId >= 0 && slotId < this.getSizeInventory()) {
					
				this.inventoryArray[slotId] = ItemStack.loadItemStackFromNBT(itemCompound);			
			}
			
			if (itemsList != null) {	
	            	
	            this.inventoryArray[j] = this.inventoryArray[slotId];
	        }
		}
	}
	
	public boolean addItemStackToInventory(final ItemStack itemStack) {
		
        if (itemStack != null && itemStack.stackSize != 0 && itemStack.getItem() != null) {
        	
        	int i;
        	
            try {

                if (itemStack.isItemDamaged()) {
                	
                    i = this.getFirstEmptyStack();

                    if (i >= 0) {
                    	
                        this.inventoryArray[i] = ItemStack.copyItemStack(itemStack);
                        this.inventoryArray[i].animationsToGo = 5;
                        itemStack.stackSize = 0;
                        return true;
                    }
                    
                    else if (this.player.capabilities.isCreativeMode) {
                    	
                    	itemStack.stackSize = 0;
                        return true;
                    }
                    
                    else {
                    	
                        return false;
                    }
                }
                
                else {
                	
                    do {
                    	
                        i = itemStack.stackSize;
                        itemStack.stackSize = this.storePartialItemStack(itemStack);
                    }
                    
                    while (itemStack.stackSize > 0 && itemStack.stackSize < i);

                    if (itemStack.stackSize == i && this.player.capabilities.isCreativeMode) {
                    	
                    	itemStack.stackSize = 0;
                        return true;
                    }
                    
                    else {
                    	
                        return itemStack.stackSize < i;
                    }
                }
            }
            
            catch (Throwable throwable) {
            	
                CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Adding item to inventory");
                CrashReportCategory reportCategory = crashReport.makeCategory("Item being added");
                reportCategory.addCrashSection("Item ID", Integer.valueOf(Item.getIdFromItem(itemStack.getItem())));
                reportCategory.addCrashSection("Item data", Integer.valueOf(itemStack.getItemDamage()));
                reportCategory.addCrashSectionCallable("Item name", new Callable() { public String call() { return itemStack.getDisplayName(); }});
                
                throw new ReportedException(crashReport);
            }
        }
        
        else {
        	
            return false;
        }
    }
	
    public int getFirstEmptyStack() {
    	
        for (int i = 0; i < this.inventoryArray.length; ++i) {
        	
            if (this.inventoryArray[i] == null) {
            	
                return i;
            }
        }

        return - 1;
    }
    
    private int storePartialItemStack(ItemStack itemStack) {
    	
        Item item = itemStack.getItem();
        int i = itemStack.stackSize;
        int j;

        if (itemStack.getMaxStackSize() == 1) {
        	
            j = this.getFirstEmptyStack();

            if (j < 0) {
            	
                return i;
            }
            
            else {
            	
                if (this.inventoryArray[j] == null) {
                	
                    this.inventoryArray[j] = ItemStack.copyItemStack(itemStack);
                }

                return 0;
            }
        }
        
        else {
        	
            j = this.storeItemStack(itemStack);

            if (j < 0) {
            	
                j = this.getFirstEmptyStack();
            }

            if (j < 0) {
            	
                return i;
            }
            
            else {
            	
                if (this.inventoryArray[j] == null) {
                	
                    this.inventoryArray[j] = new ItemStack(item, 0, itemStack.getItemDamage());

                    if (itemStack.hasTagCompound()) {
                    	
                        this.inventoryArray[j].setTagCompound((NBTTagCompound)itemStack.getTagCompound().copy());
                    }
                }

                int k = i;

                if (i > this.inventoryArray[j].getMaxStackSize() - this.inventoryArray[j].stackSize) {
                	
                    k = this.inventoryArray[j].getMaxStackSize() - this.inventoryArray[j].stackSize;
                }

                if (k > this.getInventoryStackLimit() - this.inventoryArray[j].stackSize) {
                	
                    k = this.getInventoryStackLimit() - this.inventoryArray[j].stackSize;
                }

                if (k == 0) {
                	
                    return i;
                }
                
                else {
                	
                    i -= k;
                    
                    this.inventoryArray[j].stackSize += k;
                    this.inventoryArray[j].animationsToGo = 5;
                    
                    return i;
                }
            }
        }
    }

    private int storeItemStack(ItemStack itemStack) {
    	
        for (int i = 0; i < this.inventoryArray.length; ++i) {
        	
            if (this.inventoryArray[i] != null && this.inventoryArray[i].getItem() == itemStack.getItem() && this.inventoryArray[i].isStackable() && this.inventoryArray[i].stackSize < this.inventoryArray[i].getMaxStackSize() && this.inventoryArray[i].stackSize < this.getInventoryStackLimit() && (!this.inventoryArray[i].getHasSubtypes() || this.inventoryArray[i].getItemDamage() == itemStack.getItemDamage()) && ItemStack.areItemStackTagsEqual(this.inventoryArray[i], itemStack)) {
            	
                return i;
            }
        }

        return - 1;
    }
    
    public boolean hasItem(ItemStack itemStack) {
    	
    	int i, itemDamage = itemStack.getItemDamage();
    	
        for (i = 0; i < this.inventoryArray.length; i++) {
        	
            if (this.inventoryArray[i] != null && this.inventoryArray[i].getItem() == itemStack.getItem() && this.inventoryArray[i].getItemDamage() == itemDamage) {
            
            	return true;
            }
        }

        return false;
    }

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}
}

