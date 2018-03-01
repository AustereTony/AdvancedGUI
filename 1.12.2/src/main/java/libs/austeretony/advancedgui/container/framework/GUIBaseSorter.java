package libs.austeretony.advancedgui.container.framework;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Сортировщик для отображения слотов без сортировки.
 */
@SideOnly(Side.CLIENT)
public class GUIBaseSorter extends GUISorter {
		
	@Override
	public boolean isSlotValid(Slot slot, EntityPlayer player) {
		
		return true;
	}

	@Override
	public boolean shouldAddEmptySlotsAfter() {
		
		return false;
	}
}
