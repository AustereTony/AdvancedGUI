package ru.austeretony.advancedgui.network.server;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;
import ru.austeretony.advancedgui.inventory.ContainerStorage;
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
				
					((ContainerInventory) player.openContainer).inventory.decrStackSize(this.slotIndex, itemStack.getCount() / 2);	
				
					itemStack.setCount(itemStack.getCount() / 2);
				
					((ContainerInventory) player.openContainer).inventory.setInventorySlotContents(((ContainerInventory) player.openContainer).inventory.getFirstEmptyStack(), itemStack);				
				}
				
				else if (player.openContainer instanceof ContainerStorage) {
					
					itemStack = ((ContainerStorage) player.openContainer).playerInventory.getStackInSlot(this.slotIndex).copy();
					
					((ContainerStorage) player.openContainer).playerInventory.decrStackSize(this.slotIndex, itemStack.getCount() / 2);	
					
					itemStack.setCount(itemStack.getCount() / 2);
					
					((ContainerStorage) player.openContainer).playerInventory.setInventorySlotContents(((ContainerStorage) player.openContainer).playerInventory.getFirstEmptyStack(), itemStack);
				}
				
				break;
				
			case 1:
				
				itemStack = ((ContainerStorage) player.openContainer).slorageInventory.getStackInSlot(this.slotIndex).copy();
				
				((ContainerStorage) player.openContainer).slorageInventory.decrStackSize(this.slotIndex, itemStack.getCount() / 2);	
				
				itemStack.setCount(itemStack.getCount() / 2);
				
				((ContainerStorage) player.openContainer).slorageInventory.setInventorySlotContents(((ContainerStorage) player.openContainer).slorageInventory.getFirstEmptyStack(), itemStack);
		}
	}
}
