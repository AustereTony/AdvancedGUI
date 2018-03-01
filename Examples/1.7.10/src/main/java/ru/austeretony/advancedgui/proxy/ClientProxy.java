package ru.austeretony.advancedgui.proxy;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import ru.austeretony.advancedgui.main.KeyHandler;

public class ClientProxy extends CommonProxy {
	
	public void preInit(FMLPreInitializationEvent event) {
		
		FMLCommonHandler.instance().bus().register(new KeyHandler());
	}
	
	public void init(FMLInitializationEvent event) {}
	
	@Override
	public EntityPlayer getPlayerEntity(MessageContext ctx) {

	    return (ctx.side.isClient() ? Minecraft.getMinecraft().thePlayer : super.getPlayerEntity(ctx));
	}
}
