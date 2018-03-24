package ru.austeretony.advancedgui.network.server;

import java.io.IOException;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import ru.austeretony.advancedgui.inventory.ContainerStorage;
import ru.austeretony.advancedgui.network.AbstractMessage.AbstractServerMessage;
import ru.austeretony.advancedgui.player.inventory.ContainerInventory;

public class SPContextActionDestroyStack extends AbstractServerMessage<SPContextActionDestroyStack> {
	
	private byte inventoryId;
	
	private short slotIndex;
	
	public SPContextActionDestroyStack() {}
	
	public SPContextActionDestroyStack(int inventoryId, int slotIndex) {
		
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
					
					((ContainerInventory) player.openContainer).inventory.setInventorySlotContents(this.slotIndex, null);			
				}
				
				else if (player.openContainer instanceof ContainerStorage) {
					
					((ContainerStorage) player.openContainer).playerInventory.setInventorySlotContents(this.slotIndex, null);			
				}
								
				break;
				
			case 1:
				
				((ContainerStorage) player.openContainer).slorageInventory.setInventorySlotContents(this.slotIndex, null);
				
				break;
				
			case 2:
				
				player.inventory.setInventorySlotContents(this.slotIndex, null);
		}
	}
}
