package ru.austeretony.advancedgui.network.server;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;
import ru.austeretony.advancedgui.inventory.ContainerStorage;
import ru.austeretony.advancedgui.network.AbstractMessage.AbstractServerMessage;

public class SPContextActionWithdrawStack extends AbstractServerMessage<SPContextActionWithdrawStack> {
	
	private short slotIndex;
	
	public SPContextActionWithdrawStack() {}
	
	public SPContextActionWithdrawStack(int slotIndex) {
		
		this.slotIndex = (short) slotIndex;
	}

	@Override
	protected void writeData(PacketBuffer buffer) throws IOException {
		
		buffer.writeShort(this.slotIndex);
	}

	@Override
	protected void readData(PacketBuffer buffer) throws IOException {
		
		this.slotIndex = buffer.readShort();
	}

	@Override
	public void performProcess(EntityPlayer player, Side side) {
		
		ContainerStorage storage = ((ContainerStorage) player.openContainer);
		
		ItemStack itemStack = storage.slorageInventory.getStackInSlot(this.slotIndex).copy();
		
		storage.slorageInventory.setInventorySlotContents(this.slotIndex, ItemStack.EMPTY);
		
		storage.playerInventory.setInventorySlotContents(storage.playerInventory.getFirstEmptyStack(), itemStack);
	}
}
