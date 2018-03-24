package ru.austeretony.advancedgui.player.inventory.events;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ru.austeretony.advancedgui.main.AdvancedGUIMain;
import ru.austeretony.advancedgui.player.inventory.capability.IExtendedInventory;
import ru.austeretony.advancedgui.player.inventory.capability.InventoryProvider;

public class AdvancedGUIInventoryEvents {

	public static final ResourceLocation INVENTORY_CAP = new ResourceLocation(AdvancedGUIMain.MODID, "ExtendedInventory");
	 
    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent event) {
    	
        if (event.getObject() instanceof EntityPlayer) {
        	
            event.addCapability(this.INVENTORY_CAP, new InventoryProvider());                        
        }
    }
	
	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event) {	
						
        EntityPlayer player = event.getEntityPlayer();
        
        IExtendedInventory newInventory = player.getCapability(InventoryProvider.INVENTORY_CAP, null);       
        IExtendedInventory oldInventory = event.getOriginal().getCapability(InventoryProvider.INVENTORY_CAP, null);
        
        newInventory.copyInventory(oldInventory);
	}
	
	@SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Pre event) {	
        
    	if (event.getType() == ElementType.CROSSHAIRS) {
    		
    		if (Minecraft.getMinecraft().currentScreen != null) {
    			
    			event.setCanceled(true);
    		}
    	}
    }
}
