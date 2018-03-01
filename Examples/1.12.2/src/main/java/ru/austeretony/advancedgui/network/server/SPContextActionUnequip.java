package ru.austeretony.advancedgui.network.server;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;
import ru.austeretony.advancedgui.network.AbstractMessage.AbstractServerMessage;
import ru.austeretony.advancedgui.player.inventory.capability.IExtendedInventory;
import ru.austeretony.advancedgui.player.inventory.capability.InventoryProvider;

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
				
				IExtendedInventory eInventory = player.getCapability(InventoryProvider.INVENTORY_CAP, null);
				
				itemStack = player.inventory.getStackInSlot(this.slotIndex).copy();

				player.inventory.setInventorySlotContents(this.slotIndex, ItemStack.EMPTY);
				
				eInventory.getInventory().setInventorySlotContents(eInventory.getInventory().getFirstEmptyStack(), itemStack);
		}
	}
}
