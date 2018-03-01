package ru.austeretony.advancedgui.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;

public class CommonProxy {
	
	public void preInit(FMLPreInitializationEvent event) {}
	
	public void init(FMLInitializationEvent event) {}
	
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		
		return ctx.getServerHandler().playerEntity;
	}
}
