package ru.austeretony.advancedgui.network.server;

import java.io.IOException;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;
import ru.austeretony.advancedgui.network.AbstractMessage.AbstractServerMessage;
import ru.austeretony.advancedgui.player.inventory.capability.IExtendedInventory;
import ru.austeretony.advancedgui.player.inventory.capability.InventoryProvider;

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
				
				IExtendedInventory eInventory = player.getCapability(InventoryProvider.INVENTORY_CAP, null);
				
				itemStack = eInventory.getInventory().getStackInSlot(this.slotIndex).copy();
				
		        EntityEquipmentSlot entityEquipmentSlot = EntityLiving.getSlotForItemStack(itemStack);               

				eInventory.getInventory().setInventorySlotContents(this.slotIndex, ItemStack.EMPTY);					
				
				if (itemStack.getItem() instanceof ItemArmor) {
					
					player.inventory.setInventorySlotContents(36 + entityEquipmentSlot.getIndex(), itemStack);
				}
				
				else {
					
					player.inventory.setInventorySlotContents(player.inventory.getFirstEmptyStack(), itemStack);
				}
		}
	}
}
