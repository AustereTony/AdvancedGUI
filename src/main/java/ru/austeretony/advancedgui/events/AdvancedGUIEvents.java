package ru.austeretony.advancedgui.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import ru.austeretony.advancedgui.player.ExtendedPlayer;

public class AdvancedGUIEvents {
		
	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event) {
		
		if (event.entity instanceof EntityPlayer) {
			
			if (ExtendedPlayer.get((EntityPlayer) event.entity) == null) { 
	    	
				ExtendedPlayer.register((EntityPlayer) event.entity);
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event) {	
		
		ExtendedPlayer eLivingBase = ExtendedPlayer.get(event.entityPlayer);
		
		NBTTagCompound tagCompound = new NBTTagCompound();
		
		ExtendedPlayer.get(event.original).saveNBTData(tagCompound);
		eLivingBase.loadNBTData(tagCompound);	
	}
	
	@SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Pre event) {	
		
    	if (event.type == ElementType.CROSSHAIRS) {
    		
    		if (Minecraft.getMinecraft().currentScreen != null) {
    			
    			event.setCanceled(true);
    		}
    	}
    }
	
	@SubscribeEvent
	public void onBlockClick(PlayerInteractEvent event) {
		
		if (event.action == event.action.LEFT_CLICK_BLOCK) {
			
		    Block block = event.world.getBlock(event.x, event.y, event.z);
		}
	}
}
