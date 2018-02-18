package ru.austeretony.advancedgui.main;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.gameevent.InputEvent.MouseInputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import ru.austeretony.advancedgui.network.NetworkHandler;
import ru.austeretony.advancedgui.network.server.SPOpenGui;
import ru.austeretony.advancedgui.player.ExtendedPlayer;

public class KeyHandler {
	
	public static final List<KeyBinding> CONTROLS = new ArrayList<KeyBinding>();
	
	public static final int 
	SHOW_INVENTORY_FIRST = 0,
	SHOW_INVENTORY_SECOND = 1;

	public KeyHandler() {
		
		registerKeyBinding("key.showInventoryFirst", Keyboard.KEY_I, AdvancedGUIMain.NAME);
		registerKeyBinding("key.showInventorySecond", Keyboard.KEY_P, AdvancedGUIMain.NAME);
    }
	
	@SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {
		
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		
		ExtendedPlayer ePlayer = ExtendedPlayer.get(player);
		
		int keyCode = Keyboard.getEventKey();
		
		boolean isDown = Keyboard.getEventKeyState();
		
		if (this.CONTROLS.get(this.SHOW_INVENTORY_FIRST).getIsKeyPressed()) {
			
			NetworkHandler.sendToServer(new SPOpenGui(GUIHandler.INVENTORY_FIRST));	
		}
		
		if (this.CONTROLS.get(this.SHOW_INVENTORY_SECOND).getIsKeyPressed()) {
			
			NetworkHandler.sendToServer(new SPOpenGui(GUIHandler.INVENTORY_SECOND));	
		}
	}
	
	@SubscribeEvent
	public void omMouseInput(MouseInputEvent event) {
		
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		
		ExtendedPlayer ePlayer = ExtendedPlayer.get(player);
	}
	
	private void registerKeyBinding(String name, int keyCode, String category) {
		
		KeyBinding newKeybinding = new KeyBinding(name, keyCode, category);
		
		this.CONTROLS.add(newKeybinding);
		
		ClientRegistry.registerKeyBinding(newKeybinding);		
	}
	
	public KeyBinding getKeyBinding(int index) {
		
		return this.CONTROLS.get(index);	
	}
}
