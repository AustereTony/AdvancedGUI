package ru.austeretony.advancedgui.network.server;

import java.io.IOException;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import ru.austeretony.advancedgui.inventory.ContainerAdvancedStorage;
import ru.austeretony.advancedgui.network.AbstractMessage.AbstractServerMessage;
import ru.austeretony.advancedgui.player.inventory.ContainerInventory;

public class SPContextActionSplitStack extends AbstractServerMessage<SPContextActionSplitStack> {
	
	private byte inventoryId;
	
	private short slotIndex;
	
	public SPContextActionSplitStack() {}
	
	public SPContextActionSplitStack(int inventoryId, int slotIndex) {
		
		this.inventoryId = (byte) inventoryId;
		
		this.slotIndex = (short) slotIndex;
	}

	@Override
	protected void writeData(PacketBuffer buffer) throws IOException {
		
		buffer.writeByte(this.inventoryId);
		
		buffer.writeShort(this.slotIndex);
	}

	@Override
	protected void readData(PacketBuffer buffer) throws IOException {
		
		this.inventoryId = buffer.readByte();
		
		this.slotIndex = buffer.readShort();
	}

	@Override
	public void performProcess(EntityPlayer player, Side side) {
				
		ItemStack itemStack;
				
		switch (this.inventoryId) {
		
			case 0:
				
				if (player.openContainer instanceof ContainerInventory) {
					
					itemStack = ((ContainerInventory) player.openContainer).inventory.getStackInSlot(this.slotIndex).copy();
				
					((ContainerInventory) player.openContainer).inventory.decrStackSize(this.slotIndex, itemStack.stackSize / 2);	
				
					itemStack.stackSize = itemStack.stackSize / 2;
				
					((ContainerInventory) player.openContainer).inventory.setInventorySlotContents(((ContainerInventory) player.openContainer).inventory.getFirstEmptyStack(), itemStack);				
				}
				
				else if (player.openContainer instanceof ContainerAdvancedStorage) {
					
					itemStack = ((ContainerAdvancedStorage) player.openContainer).playerInventory.getStackInSlot(this.slotIndex).copy();
					
					((ContainerAdvancedStorage) player.openContainer).playerInventory.decrStackSize(this.slotIndex, itemStack.stackSize / 2);	
					
					itemStack.stackSize = itemStack.stackSize / 2;
					
					((ContainerAdvancedStorage) player.openContainer).playerInventory.setInventorySlotContents(((ContainerAdvancedStorage) player.openContainer).playerInventory.getFirstEmptyStack(), itemStack);
				}
				
				break;
				
			case 1:
				
				itemStack = ((ContainerAdvancedStorage) player.openContainer).slorageInventory.getStackInSlot(this.slotIndex).copy();
				
				((ContainerAdvancedStorage) player.openContainer).slorageInventory.decrStackSize(this.slotIndex, itemStack.stackSize / 2);	
				
				itemStack.stackSize = itemStack.stackSize / 2;
				
				((ContainerAdvancedStorage) player.openContainer).slorageInventory.setInventorySlotContents(((ContainerAdvancedStorage) player.openContainer).slorageInventory.getFirstEmptyStack(), itemStack);
		}
	}
}
