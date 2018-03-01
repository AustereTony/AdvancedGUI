package ru.austeretony.advancedgui.main;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import ru.austeretony.advancedgui.blocks.BlockAdvancedStorage;
import ru.austeretony.advancedgui.events.AdvancedGUIEvents;
import ru.austeretony.advancedgui.main.tab.TabAdvancedGUI;
import ru.austeretony.advancedgui.network.NetworkHandler;
import ru.austeretony.advancedgui.proxy.CommonProxy;
import ru.austeretony.advancedgui.tiles.TileEntityStorage;

@Mod(modid = AdvancedGUIMain.MODID, name = AdvancedGUIMain.NAME, version = AdvancedGUIMain.VERSION)
public class AdvancedGUIMain {
	
    public static final String 
    MODID = "advancedgui",
    NAME = "Advanced GUI",
    VERSION = "1.0";
    
	@Instance(AdvancedGUIMain.MODID)
	public static AdvancedGUIMain instance;	
	
	@SidedProxy(clientSide = "ru.austeretony.advancedgui.proxy.ClientProxy", serverSide = "ru.austeretony.advancedgui.proxy.CommonProxy")
	public static CommonProxy proxy;
	
	public static final CreativeTabs ADVANCED_GUI = new TabAdvancedGUI(CreativeTabs.getNextID(), "tabAdvancedGUI");
		
	public static final Block 
	BLOCK_STORAGE = new BlockAdvancedStorage().setBlockName("advancedStorage");
	    
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		proxy.preInit(event);
		
		NetworkHandler.registerPackets();
		
		GameRegistry.registerBlock(this.BLOCK_STORAGE, "advancedStorage");		
        GameRegistry.registerTileEntity(TileEntityStorage.class, "tileAdvancedStorage");
	}
	
    @EventHandler
    public void init(FMLInitializationEvent event) {
    	
		proxy.init(event);
		
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GUIHandler());
    	    	
		MinecraftForge.EVENT_BUS.register(new AdvancedGUIEvents());
    }
}
