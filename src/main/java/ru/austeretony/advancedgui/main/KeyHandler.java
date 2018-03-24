package ru.austeretony.advancedgui.main;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.MouseInputEvent;
import ru.austeretony.advancedgui.network.NetworkHandler;
import ru.austeretony.advancedgui.network.server.SPOpenGUI;

public class KeyHandler {

public static final List<KeyBinding> CONTROLS = new ArrayList<KeyBinding>();
	
	public static final int 
	SHOW_INVENTORY = 0;

	public KeyHandler() {
		
		this.registerKeyBinding("key.showInventory", Keyboard.KEY_I, AdvancedGUIMain.NAME);
    }
	
	@SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {
		
		EntityPlayer player = Minecraft.getMinecraft().player;
		
		int keyCode = Keyboard.getEventKey();
		
		boolean isDown = Keyboard.getEventKeyState();
		
		if (this.CONTROLS.get(this.SHOW_INVENTORY).isPressed()) {
			
			NetworkHandler.sendToServer(new SPOpenGUI(GUIHandler.INVENTORY));	
		}	
	}
	
	@SubscribeEvent
	public void omMouseInput(MouseInputEvent event) {
		
		EntityPlayer player = Minecraft.getMinecraft().player;
	}
	
	private void registerKeyBinding(String name, int keyCode, String category) {
		
		KeyBinding newKeybinding = new KeyBinding(name, keyCode, category);
		
		this.CONTROLS.add(newKeybinding);
		
		ClientRegistry.registerKeyBinding(newKeybinding);		
	}
	
	public KeyBinding getKeyBinding(int index) {
		
		return CONTROLS.get(index);	
	}
}
