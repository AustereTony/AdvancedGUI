package ru.austeretony.advancedgui.player.inventory;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ReportedException;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class InventoryExtended implements IInventory {

	private static final String 
	NAME = "extendedInventory",
	NAME_TAG = "extendedInventoryTag";
	
	public static final int 
	INVENTORY_MAX_SIZE = 150;

    public final NonNullList<ItemStack> 
    inventoryArray = NonNullList.<ItemStack>withSize(this.INVENTORY_MAX_SIZE, ItemStack.EMPTY);
                
    public InventoryExtended() {}
    
    public void copy(InventoryExtended inventory) {
    	
    	int i;
        
        for (i = 0; i < inventory.inventoryArray.size(); i++) {
        	
            ItemStack itemStack = inventory.getStackInSlot(i);
            
            this.inventoryArray.set(i, (itemStack.isEmpty() ? ItemStack.EMPTY : itemStack.copy()));
        }
    }
	
    @Override
    public void clear() {
    	
    	this.inventoryArray.clear();
    }
    
    @Override
    public String getName() {
    	
        return this.NAME;
    }

    @Override
    public boolean hasCustomName() {
    	
        return true;
    }

    @Override
    public ITextComponent getDisplayName() {
    	
        return (ITextComponent) (this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]));
    }

    @Override
    public int getSizeInventory() {
    	
        return this.inventoryArray.size();
    }
    
    public int getFirstEmptyStack() {
    	
        for (int i = 0; i < this.inventoryArray.size(); i++) {
        	
            if (((ItemStack) this.inventoryArray.get(i)).isEmpty()) {
            	
                return i;
            }
        }

        return - 1;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
    		
		return this.inventoryArray.get(index) != ItemStack.EMPTY ? this.inventoryArray.get(index) : ItemStack.EMPTY;
	}

    @Override
    public ItemStack decrStackSize(int index, int count) {   	
    	
    	ItemStack itemStack = ItemStackHelper.getAndSplit(this.inventoryArray, index, count);
        
        if (!itemStack.isEmpty()) {
        	
            this.markDirty();
        }
        
        return itemStack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
			
	    if (!this.inventoryArray.get(index).isEmpty()) {
	        	 
	    	ItemStack itemStack = this.inventoryArray.get(index);
	                
	        this.inventoryArray.set(index, ItemStack.EMPTY);
	                
	        return itemStack;              
	    }
	         
	    else {
	        	 
	       return ItemStack.EMPTY;
	    }
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack itemStack) {
			
		if (index < this.inventoryArray.size()) {
				
		    this.inventoryArray.set(index, itemStack);
		}
    	        
        if (!itemStack.isEmpty() && itemStack.getCount() > this.getInventoryStackLimit()) {
        	
            itemStack.setCount(this.getInventoryStackLimit());
        }
        
        this.markDirty();
    }

    @Override
    public boolean isEmpty() {
    	
        for (ItemStack itemStack : this.inventoryArray) {
        	
            if (!itemStack.isEmpty()) {
            	
                 return false;
            }
        }
        
        return true;
    }

    @Override
    public int getInventoryStackLimit() {
    	
        return 64;
    }

    @Override
    public void markDirty() {
    	
		for (int i = 0; i < this.getSizeInventory(); i++) {
			
			if (!this.getStackInSlot(i).isEmpty() && this.getStackInSlot(i).getCount() == 0) {
							
				this.inventoryArray.set(i, ItemStack.EMPTY);
			}
		}
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
    	
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {}

    @Override
    public void closeInventory(EntityPlayer player) {}

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
    	
        return true;
    }

    @Override
    public int getField(int id) {
    	
        return 0;
    }

    @Override
    public void setField(int id, int value) {}

    @Override
    public int getFieldCount() {
    	
        return 0;
    }

    public void writeToNBT(NBTTagCompound tagCompound) {
    	    	 
        this.saveAllItems(tagCompound, this.inventoryArray, "inventory", true);    
    }
    
    public static NBTTagCompound saveAllItems(NBTTagCompound tagCompound, NonNullList<ItemStack> list, String tagKey, boolean saveEmpty) {
    	
        NBTTagList tagList = new NBTTagList();

        for (int i = 0; i < list.size(); ++i) {
        	
            ItemStack itemStack = list.get(i);

            if (!itemStack.isEmpty()) {
            	
                NBTTagCompound slotCompound = new NBTTagCompound();
                
                slotCompound.setShort("slot", (short) i);
                
                itemStack.writeToNBT(slotCompound);
                
                tagList.appendTag(slotCompound);
            }
        }

        if (!tagList.hasNoTags() || saveEmpty) {
        	
            tagCompound.setTag("items" + tagKey, tagList);
        }

        return tagCompound;
    }

    public void readFromNBT(NBTTagCompound tagCompound) {
    	    	        
        this.loadAllItems(tagCompound, this.inventoryArray, "inventory");
    }
    
    public static void loadAllItems(NBTTagCompound tagCompound, NonNullList<ItemStack> list, String tagKey) {
    	
        NBTTagList tagList = tagCompound.getTagList("items" + tagKey, 10);

        for (int i = 0; i < tagList.tagCount(); ++i) {
        	
            NBTTagCompound slotCompound = tagList.getCompoundTagAt(i);
            
            int j = slotCompound.getShort("slot");

            if (j >= 0 && j < list.size()) {
            	
                list.set(j, new ItemStack(slotCompound));
            }
        }
    }
    
    public boolean addItemStackToInventory(ItemStack itemStack) {
    	    	
        return this.add(- 1, itemStack);
    }

    public boolean add(int value, final ItemStack itemStack) {
    	    	
        if (itemStack.isEmpty()) {
        	
            return false;
        }         
        
        else {
        	    
            try {
            	
                if (itemStack.isItemDamaged()) {
                	
                    if (value == - 1) {
                    	
                        value = this.getFirstEmptyStack();
                    }

                    if (value >= 0) {
                    	
                        this.inventoryArray.set(value, itemStack.copy());
                        
                        ((ItemStack) this.inventoryArray.get(value)).setAnimationsToGo(5);
                        
                        itemStack.setCount(0);                    
                        
                        return true;
                    }
                    
                    else {
                    	
                        return false;
                    }
                }
                
                else {
                	
                    int i;

                    while (true) {                   
                    	
                        i = itemStack.getCount();

                        if (value == - 1) {
                        	
                            itemStack.setCount(this.storePartialItemStack(itemStack));
                        }
                        
                        else {
                        	
                            itemStack.setCount(this.addResource(value, itemStack));
                        }

                        if (itemStack.isEmpty() || itemStack.getCount() >= i) {
                        	
                            break;
                        }
                    }    
                                        	
                    return itemStack.getCount() < i;
                }
            }
            
            catch (Throwable throwable) {
            	
                CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Adding item to inventory");
                CrashReportCategory crashreportcategory = crashReport.makeCategory("Item being added");
                crashreportcategory.addCrashSection("Item ID", Integer.valueOf(Item.getIdFromItem(itemStack.getItem())));
                crashreportcategory.addCrashSection("Item data", Integer.valueOf(itemStack.getMetadata()));
                crashreportcategory.addDetail("Item name", new ICrashReportDetail<String>() {
                	
                    public String call() throws Exception {
                    	
                        return itemStack.getDisplayName();
                    }
                });
                
                throw new ReportedException(crashReport);
            }
        }
    }
    
    private int storePartialItemStack(ItemStack itemStack) {
    	
        int i = this.storeItemStack(itemStack);

        if (i == - 1) {
        	
            i = this.getFirstEmptyStack();
        }

        return i == - 1 ? itemStack.getCount() : this.addResource(i, itemStack);
    }
    
    private int addResource(int index, ItemStack itemStack) {
    	
        Item item = itemStack.getItem();
        
        int i = itemStack.getCount();
        
        ItemStack slotStack = this.getStackInSlot(index);

        if (slotStack.isEmpty()) {
        	
            slotStack = itemStack.copy();
            
            slotStack.setCount(0);

            if (itemStack.hasTagCompound()) {
            	
                slotStack.setTagCompound(itemStack.getTagCompound().copy());
            }

            this.setInventorySlotContents(index, slotStack);
        }

        int j = i;

        if (i > slotStack.getMaxStackSize() - slotStack.getCount()) {
        	
            j = slotStack.getMaxStackSize() - slotStack.getCount();
        }

        if (j > this.getInventoryStackLimit() - slotStack.getCount()) {
        	
            j = this.getInventoryStackLimit() - slotStack.getCount();
        }

        if (j == 0) {
        	
            return i;
        }
        
        else {
        	
            i = i - j;
            
            slotStack.grow(j);
            
            slotStack.setAnimationsToGo(5);
            
            return i;
        }
    }
    
    public int storeItemStack(ItemStack itemStack) {
        	
        for (int i = 0; i < this.inventoryArray.size(); ++i) {
            	
            if (this.canMergeStacks(this.inventoryArray.get(i), itemStack)) {
                	
                return i;
            }
        }

        return - 1;
    }
    
    private boolean canMergeStacks(ItemStack itemStack1, ItemStack itemStack2) {
    	
        return !itemStack1.isEmpty() && this.stackEqualExact(itemStack1, itemStack2) && itemStack1.isStackable() && itemStack1.getCount() < itemStack1.getMaxStackSize() && itemStack1.getCount() < this.getInventoryStackLimit();
    }
    
    private boolean stackEqualExact(ItemStack itemStack1, ItemStack itemStack2) {
    	
        return itemStack1.getItem() == itemStack2.getItem() && (!itemStack1.getHasSubtypes() || itemStack1.getMetadata() == itemStack2.getMetadata()) && ItemStack.areItemStackTagsEqual(itemStack1, itemStack2);
    }
}