package ru.austeretony.advancedgui.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import ru.austeretony.advancedgui.main.AdvancedGUIMain;
import ru.austeretony.advancedgui.network.server.SPContextActionDestroyStack;
import ru.austeretony.advancedgui.network.server.SPContextActionDropStack;
import ru.austeretony.advancedgui.network.server.SPContextActionEquip;
import ru.austeretony.advancedgui.network.server.SPContextActionSplitStack;
import ru.austeretony.advancedgui.network.server.SPContextActionStowStack;
import ru.austeretony.advancedgui.network.server.SPContextActionUnequip;
import ru.austeretony.advancedgui.network.server.SPContextActionWithdrawStack;
import ru.austeretony.advancedgui.network.server.SPOpenGUI;

public class NetworkHandler {

    private static byte packetId = 0;
	 
	private static final SimpleNetworkWrapper dispatcher = NetworkRegistry.INSTANCE.newSimpleChannel(AdvancedGUIMain.MODID);
	
	public static final void registerPackets() {
		
		registerMessage(SPOpenGUI.class);
		
		registerMessage(SPContextActionDropStack.class);
		registerMessage(SPContextActionDestroyStack.class);
		registerMessage(SPContextActionSplitStack.class);
		registerMessage(SPContextActionStowStack.class);
		registerMessage(SPContextActionWithdrawStack.class);
		registerMessage(SPContextActionUnequip.class);
		registerMessage(SPContextActionEquip.class);
	}
	
	private static final <T extends AbstractMessage<T> & IMessageHandler<T, IMessage>> void registerMessage(Class<T> clazz) {

		if (AbstractMessage.AbstractClientMessage.class.isAssignableFrom(clazz)) {
			
			NetworkHandler.dispatcher.registerMessage(clazz, clazz, packetId++, Side.CLIENT);
		}
		
		else if (AbstractMessage.AbstractServerMessage.class.isAssignableFrom(clazz)) {
			
			NetworkHandler.dispatcher.registerMessage(clazz, clazz, packetId++, Side.SERVER);
		} 
		
		else {

			NetworkHandler.dispatcher.registerMessage(clazz, clazz, packetId, Side.CLIENT);
			NetworkHandler.dispatcher.registerMessage(clazz, clazz, packetId++, Side.SERVER);
		}
    }
    
    public static final void sendToServer(IMessage message) {
     	  
        NetworkHandler.dispatcher.sendToServer(message);
    }
}
