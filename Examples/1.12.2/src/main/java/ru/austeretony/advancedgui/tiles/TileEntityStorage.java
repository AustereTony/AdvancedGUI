package ru.austeretony.advancedgui.tiles;

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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ReportedException;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class TileEntityStorage extends TileEntity implements IInventory {

	private static final String 
	NAME = "storageInventory",
	NAME_TAG = "storageInventoryTag";
	
	public static final int 
	STORAGE_MAX_SIZE = 350;

    public final NonNullList<ItemStack> 
    storageArray = NonNullList.<ItemStack>withSize(this.STORAGE_MAX_SIZE, ItemStack.EMPTY);
                
    public TileEntityStorage() {}
	
    @Override
    public void clear() {
    	
    	this.storageArray.clear();
    	
    	this.markDirty();
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
    	
        return this.storageArray.size();
    }
    
    public int getFirstEmptyStack() {
    	
        for (int i = 0; i < this.storageArray.size(); i++) {
        	
            if (((ItemStack) this.storageArray.get(i)).isEmpty()) {
            	
                return i;
            }
        }

        return - 1;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
    		
		return this.storageArray.get(index) != ItemStack.EMPTY ? this.storageArray.get(index) : ItemStack.EMPTY;
	}

    @Override
    public ItemStack decrStackSize(int index, int count) {   	
    	
    	ItemStack itemStack = ItemStackHelper.getAndSplit(this.storageArray, index, count);
        
        if (!itemStack.isEmpty()) {
        	
            this.markDirty();
        }
        
        return itemStack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
			
	    if (!this.storageArray.get(index).isEmpty()) {
	        	 
	    	ItemStack itemStack = this.storageArray.get(index);
	                
	        this.storageArray.set(index, ItemStack.EMPTY);
	        
	    	this.markDirty();
	                
	        return itemStack;              
	    }
	         
	    else {
	        	 
	       return ItemStack.EMPTY;
	    }
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack itemStack) {
			
		if (index < this.storageArray.size()) {
				
		    this.storageArray.set(index, itemStack);
		    
	    	this.markDirty();
		}
    	        
        if (!itemStack.isEmpty() && itemStack.getCount() > this.getInventoryStackLimit()) {
        	
            itemStack.setCount(this.getInventoryStackLimit());
        }
        
        this.markDirty();
    }

    @Override
    public boolean isEmpty() {
    	
        for (ItemStack itemStack : this.storageArray) {
        	
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
    	
    	super.markDirty();
    	
		for (int i = 0; i < this.getSizeInventory(); i++) {
			
			if (!this.getStackInSlot(i).isEmpty() && this.getStackInSlot(i).getCount() == 0) {
							
				this.storageArray.set(i, ItemStack.EMPTY);
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
    
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		
        this.saveAllItems(tagCompound, this.storageArray, "storage", true); 
        		
		return super.writeToNBT(tagCompound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		
        this.loadAllItems(tagCompound, this.storageArray, "storage");
		
		super.readFromNBT(tagCompound);
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
    	    	    
    	this.markDirty();
    	
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
                    	
                        this.storageArray.set(value, itemStack.copy());
                        
                        ((ItemStack) this.storageArray.get(value)).setAnimationsToGo(5);
                        
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
        	
        for (int i = 0; i < this.storageArray.size(); ++i) {
            	
            if (this.canMergeStacks(this.storageArray.get(i), itemStack)) {
                	
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
