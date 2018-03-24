package libs.austeretony.advancedgui.container.contextmenu;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import libs.austeretony.advancedgui.screen.button.GUISound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

/**
 * Шаблон для создания пользовательских действий для контекстного меню.
 * 
 * @author AustereTony
 */
@SideOnly(Side.CLIENT)
public abstract class GUIPresetAction {

    public final String dispalyName;
    
    private String popupText;
    
    /**
     * Контекстное действие.
     * 
     * @param name название
     */
    public GUIPresetAction(String dispalyName) {
    	
    	this.dispalyName = dispalyName;
    	this.popupText = "";
    }
    
    /**
     * Определяет возможность выполнить данное действие для этого слота.
     * 
     * @param inventory IInventory, для которого вызывается действие
     * @param slot слот, для которого вызывается действие
     * @param player
     * 
     * @return true если действие приминимо
     */
	public abstract boolean isValidAction(IInventory inventory, Slot slot, EntityPlayer player);
    
	/**
	 * Выполняет действие, если возможно.
	 * 
	 * @param inventory IInventory, для которого вызывается действие
	 * @param slot слот, для которого вызывается действие
	 * @param player
	 */
    public abstract void performAction(IInventory inventory, Slot slot, EntityPlayer player);
    
    /**
     * Возвращает GUISound для воспроизведение звука взаимодействия, null по умолчанию.
     * 
     * @return GUISound 
     */
    public abstract GUISound getSound();
    
    public String getPopupText() {
    	
    	return this.popupText;
    }
    
    public GUIPresetAction setPopupText(String popupText) {
    	
    	this.popupText = popupText;
    	
    	return this;
    }
}
