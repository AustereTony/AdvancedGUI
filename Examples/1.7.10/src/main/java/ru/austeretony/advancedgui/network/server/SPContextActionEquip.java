package ru.austeretony.advancedgui.network.server;

import java.io.IOException;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import ru.austeretony.advancedgui.network.AbstractMessage.AbstractServerMessage;
import ru.austeretony.advancedgui.player.ExtendedPlayer;

public class SPContextActionEquip extends AbstractServerMessage<SPContextActionEquip> {
	
	private byte inventoryId;
	
	private short slotIndex;
	
	public SPContextActionEquip() {}
	
	public SPContextActionEquip(int inventoryId, int slotIndex) {
		
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
				
				ExtendedPlayer ePlayer = ExtendedPlayer.get(player);
				
				itemStack = ePlayer.inventory.getStackInSlot(this.slotIndex).copy();

				ePlayer.inventory.setInventorySlotContents(this.slotIndex, null);
				
				if (itemStack.getItem() instanceof ItemArmor) {
					
					player.inventory.setInventorySlotContents(player.inventory.getSizeInventory() - 1 - ((ItemArmor) itemStack.getItem()).armorType, itemStack);
				}
				
				else {
					
					player.inventory.setInventorySlotContents(player.inventory.getFirstEmptyStack(), itemStack);
				}
		}
	}
}
