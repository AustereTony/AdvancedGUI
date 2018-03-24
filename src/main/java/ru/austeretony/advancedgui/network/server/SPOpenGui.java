package ru.austeretony.advancedgui.network.server;

import java.io.IOException;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import ru.austeretony.advancedgui.main.AdvancedGUIMain;
import ru.austeretony.advancedgui.network.AbstractMessage.AbstractServerMessage;

public class SPOpenGui extends AbstractServerMessage<SPOpenGui> {

	private byte guiId;
	
	public SPOpenGui() {}
	
	public SPOpenGui(int guiId) {
		
		this.guiId = (byte) guiId;
	}
	
	@Override
	protected void writeData(PacketBuffer buffer) throws IOException {
		
		buffer.writeByte(this.guiId);
	}

	@Override
	protected void readData(PacketBuffer buffer) throws IOException {
		
		this.guiId = buffer.readByte();
	}

	@Override
	public void performProcess(EntityPlayer player, Side side) {
						
		player.openGui(AdvancedGUIMain.instance, this.guiId, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);				
	}
}
