package libs.austeretony.advancedgui.container.framework;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.inventory.Slot;

/**
 * Шаблон для создания сортировщиков содержимого слотов.
 * 
 * @author AustereTony
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
	public abstract boolean isSlotValid(Slot slot);
	
	/**
	 * Определяет, будут ли добавлены пустые слоты после отсортированных.
	 * 
	 * @return true если пустые слоты должны быть добавлены
	 */
	public abstract boolean shouldAddEmptySlotsAfter();
}
