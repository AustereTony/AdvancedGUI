package ru.austeretony.advancedgui.network.server;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;
import ru.austeretony.advancedgui.inventory.ContainerStorage;
import ru.austeretony.advancedgui.network.AbstractMessage.AbstractServerMessage;
import ru.austeretony.advancedgui.player.inventory.ContainerExtendedInventory;

public class SPContextActionDropStack extends AbstractServerMessage<SPContextActionDropStack> {
	
	private byte inventoryId;
	
	private short slotIndex;
	
	public SPContextActionDropStack() {}
	
	public SPContextActionDropStack(int inventoryId, int slotIndex) {
		
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
		
		switch (this.inventoryId) {
		
			case 0:
				
				if (player.openContainer instanceof ContainerExtendedInventory) {
					
					player.entityDropItem(((ContainerExtendedInventory) player.openContainer).inventory.getStackInSlot(this.slotIndex), 1.0F).setDefaultPickupDelay();
					((ContainerExtendedInventory) player.openContainer).inventory.setInventorySlotContents(this.slotIndex, ItemStack.EMPTY.EMPTY);
				}
				
				else if (player.openContainer instanceof ContainerStorage) {
					
					player.entityDropItem(((ContainerStorage) player.openContainer).playerInventory.getStackInSlot(this.slotIndex), 1.0F).setDefaultPickupDelay();
					((ContainerStorage) player.openContainer).playerInventory.setInventorySlotContents(this.slotIndex, ItemStack.EMPTY.EMPTY);
				}
								
				break;
				
			case 1:
				
				player.entityDropItem(((ContainerStorage) player.openContainer).slorageInventory.getStackInSlot(this.slotIndex), 1.0F).setDefaultPickupDelay();
				((ContainerStorage) player.openContainer).slorageInventory.setInventorySlotContents(this.slotIndex, ItemStack.EMPTY.EMPTY);
				
				break;
				
			case 2:
				
				player.entityDropItem(player.inventory.getStackInSlot(this.slotIndex), 1.0F).setDefaultPickupDelay();
				player.inventory.setInventorySlotContents(this.slotIndex, ItemStack.EMPTY.EMPTY);
		}
	}
}
