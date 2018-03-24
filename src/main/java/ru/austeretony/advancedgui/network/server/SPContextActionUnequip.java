package ru.austeretony.advancedgui.network.server;

import java.io.IOException;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import ru.austeretony.advancedgui.network.AbstractMessage.AbstractServerMessage;
import ru.austeretony.advancedgui.player.ExtendedPlayer;

public class SPContextActionUnequip extends AbstractServerMessage<SPContextActionUnequip> {
	
	private byte inventoryId;
	
	private short slotIndex;
	
	public SPContextActionUnequip() {}
	
	public SPContextActionUnequip(int inventoryId, int slotIndex) {
		
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
		
			case 2:
				
				ExtendedPlayer ePlayer = ExtendedPlayer.get(player);
				
				itemStack = player.inventory.getStackInSlot(this.slotIndex).copy();

				player.inventory.setInventorySlotContents(this.slotIndex, null);
				
				ePlayer.inventory.setInventorySlotContents(ePlayer.inventory.getFirstEmptyStack(), itemStack);
		}
	}
}
