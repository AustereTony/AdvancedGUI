package ru.austeretony.advancedgui.main;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class SoundHandler {

	public static final SoundEvent 
	CLICK = new SoundEvent(new ResourceLocation(AdvancedGUIMain.MODID + ":" + "click"));
	
	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<SoundEvent> event) {
		
	    event.getRegistry().register(this.CLICK);
	}
}
