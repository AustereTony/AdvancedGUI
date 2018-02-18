package libs.austeretony.advancedgui.guicontainer.contextmenu;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import libs.austeretony.advancedgui.guicontainer.button.GUIButtonSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

/**
 * Шаблон для создания пользовательских действий для контекстного меню.
 */
@SideOnly(Side.CLIENT)
public abstract class GUIPresetAction {

    public final String dispalyName;
    
    /**
     * Контекстное действие.
     * 
     * @param name название
     */
    public GUIPresetAction(String name) {
    	
    	this.dispalyName = name;
    }
    
    /**
     * Определяет возможность выполнить данное действие для этого слота.
     * 
     * @param inventory IInventory, для которого вызывается действие
     * @param slot слот, для которого вызывается действие.
     * @param player
     * @return true если действие приминимо.
     */
	public abstract boolean isValidAction(IInventory inventory, Slot slot, EntityPlayer player);
    
	/**
	 * Выполняет действие, если возможно.
	 * 
	 * @param inventory IInventory, для которого вызывается действие
	 * @param slot слот, для которого вызывается действие.
	 * @param player
	 */
    public abstract void performAction(IInventory inventory, Slot slot, EntityPlayer player);
    
    /**
     * Возвращает GUIButtonSound для воспроизведение звука нажатия, null по умолчанию.
     * 
     * @return GUIButtonSound с путём к звуку и громкостью.
     */
    public abstract GUIButtonSound getSound();
}
