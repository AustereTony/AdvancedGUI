package ru.austeretony.advancedgui.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
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
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
			
	    if (event.entity instanceof EntityPlayer) {
	    		    	
	    	if (!event.entity.worldObj.isRemote) {
	    		
				EntityPlayer player = (EntityPlayer) event.entity;				
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
}
