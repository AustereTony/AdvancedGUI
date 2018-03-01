package ru.austeretony.advancedgui.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import ru.austeretony.advancedgui.main.BlockRegistry;
import ru.austeretony.advancedgui.main.KeyHandler;

public class ClientProxy extends CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {

    	super.preInit(event);
    	
    	BlockRegistry.registerRender();
    }

    public void init(FMLInitializationEvent event) {

    	super.init(event);   	
    	
    	MinecraftForge.EVENT_BUS.register(new KeyHandler());
    }
    
	@Override
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		
	    return (ctx.side.isClient() ? Minecraft.getMinecraft().player : super.getPlayerEntity(ctx));
	}
}
