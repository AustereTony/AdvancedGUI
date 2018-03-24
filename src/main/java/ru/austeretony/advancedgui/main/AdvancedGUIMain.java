package ru.austeretony.advancedgui.main;

import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import ru.austeretony.advancedgui.main.tab.TabAdvancedGUI;
import ru.austeretony.advancedgui.proxy.CommonProxy;

@Mod(modid = AdvancedGUIMain.MODID, name = AdvancedGUIMain.VERSION, version = AdvancedGUIMain.VERSION)
public class AdvancedGUIMain {
	
    public static final String 
    MODID = "advancedgui",
    NAME = "Advanced GUI",
    VERSION = "1.0";
    
    private static Logger logger;
    
	@Instance(AdvancedGUIMain.MODID)
	public static AdvancedGUIMain instance;	
    
    @SidedProxy(clientSide = "ru.austeretony.advancedgui.proxy.ClientProxy", serverSide = "ru.austeretony.advancedgui.proxy.CommonProxy")
    public static CommonProxy proxy;
    
	public static final CreativeTabs ADVANCED_GUI = new TabAdvancedGUI(CreativeTabs.getNextID(), "tab_advancedgui");
    
    public AdvancedGUIMain() {
    	
    	this.instance = this;
    }
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	
        this.logger = event.getModLog();
    	
    	this.proxy.preInit(event);    	  	
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
    	
    	this.proxy.init(event); 	    
    }
    
    public static Logger logger() {
    	
    	return logger;
    }
}
