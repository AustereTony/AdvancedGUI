package ru.austeretony.advancedgui.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import ru.austeretony.advancedgui.main.AdvancedGUIMain;
import ru.austeretony.advancedgui.main.BlockRegistry;
import ru.austeretony.advancedgui.main.GUIHandler;
import ru.austeretony.advancedgui.main.SoundHandler;
import ru.austeretony.advancedgui.network.NetworkHandler;
import ru.austeretony.advancedgui.player.inventory.capability.ExtendedInventory;
import ru.austeretony.advancedgui.player.inventory.capability.IExtendedInventory;
import ru.austeretony.advancedgui.player.inventory.capability.InventoryStorage;
import ru.austeretony.advancedgui.player.inventory.events.AdvancedGUIInventoryEvents;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
    	
    	NetworkHandler.registerPackets();  
    	
    	BlockRegistry.register();
    }

    public void init(FMLInitializationEvent event) { 
    	
    	CapabilityManager.INSTANCE.register(IExtendedInventory.class, new InventoryStorage(), ExtendedInventory.class);
    	
    	MinecraftForge.EVENT_BUS.register(new AdvancedGUIInventoryEvents());
    	
    	MinecraftForge.EVENT_BUS.register(new SoundHandler());
    	
		NetworkRegistry.INSTANCE.registerGuiHandler(AdvancedGUIMain.instance, new GUIHandler());
    }
    
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		
		return ctx.getServerHandler().player;
	}
}
