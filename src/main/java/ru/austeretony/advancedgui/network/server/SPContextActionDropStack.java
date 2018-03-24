package ru.austeretony.advancedgui.network.server;

import java.io.IOException;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import ru.austeretony.advancedgui.inventory.ContainerStorage;
import ru.austeretony.advancedgui.network.AbstractMessage.AbstractServerMessage;
import ru.austeretony.advancedgui.player.inventory.ContainerInventory;

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
				
				if (player.openContainer instanceof ContainerInventory) {
					
					player.entityDropItem(((ContainerInventory) player.openContainer).inventory.getStackInSlot(this.slotIndex), 1.0F).delayBeforeCanPickup = 40;
				}
				
				else if (player.openContainer instanceof ContainerStorage) {
					
					player.entityDropItem(((ContainerStorage) player.openContainer).playerInventory.getStackInSlot(this.slotIndex), 1.0F).delayBeforeCanPickup = 40;
				}
								
				break;
				
			case 1:
				
				player.entityDropItem(((ContainerStorage) player.openContainer).slorageInventory.getStackInSlot(this.slotIndex), 1.0F).delayBeforeCanPickup = 40;
				
				break;
				
			case 2:
				
				player.entityDropItem(player.inventory.getStackInSlot(this.slotIndex), 1.0F).delayBeforeCanPickup = 40;
		}
	}
}
