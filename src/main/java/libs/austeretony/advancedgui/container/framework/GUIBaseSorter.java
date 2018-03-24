package libs.austeretony.advancedgui.container.framework;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.inventory.Slot;

/**
 * Сортировщик для отображения слотов без сортировки.
 * 
 * @author AustereTony
 */
@SideOnly(Side.CLIENT)
public class GUIBaseSorter extends GUISorter {
		
	@Override
	public boolean isSlotValid(Slot slot) {
		
		return true;
	}

	@Override
	public boolean shouldAddEmptySlotsAfter() {
		
		return false;
	}
}
