package libs.austeretony.advancedgui.guicontainer.framework;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

/**
 * Шаблон для создания сортировщиков содержимого слотов.
 */
@SideOnly(Side.CLIENT)
public abstract class GUISorter {

	/**
	 * Возвращаемое значение определяет будет ли слот отображаться в ГПИ.
	 * 
	 * @param slot проверяемый слот
	 * @param player 
	 * 
	 * @return true если слот должен отображаться 
	 */
	public abstract boolean isSlotValid(Slot slot, EntityPlayer player);
	
	/**
	 * Определяет, будут ли добавлены пустые слоты после отсортированных.
	 * 
	 * @return true если пустые слоты должны быть добавлены
	 */
	public abstract boolean shouldAddEmptySlotsAfter();
}
