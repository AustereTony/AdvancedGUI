package libs.austeretony.advancedgui.guicontainer.slot;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import libs.austeretony.advancedgui.guicontainer.framework.GUIFramework;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.inventory.Slot;

/**
 * Шаблон для пользовательских рендереров слотов, позволит управлять отрисовкой.
 */
@SideOnly(Side.CLIENT)
public abstract class GUISlotRenderer {
	
	protected Minecraft mc;
	
	protected GUIFramework framework;
	
	public final int slotWidth, slotHeight;
	
	/**
	 * Создание рендерера.
	 * @param slotWidth ширина слота
	 * @param slotHeight высота слота
	 */
	public GUISlotRenderer(int slotWidth, int slotHeight) {
		
		this.slotWidth = slotWidth;
		this.slotHeight = slotHeight;
		
        this.mc = Minecraft.getMinecraft();
	}
	
	public final void initFramework(GUIFramework framework) {
		
		this.framework = framework;
	}
	
	/**
	 * Отвечает за отрисовку слоя под слотом.
	 * 
	 * @param slot слот, под которым происходит отрисовка
	 */
	public abstract void drawSlotBottomLayer(Slot slot);
	
	/**
	 * Отрисовка содержимого слота - предмет, кол-во, эффекты и прочее.
	 * 
	 * @param slot слот, содержимое которого отрисовывается
	 * @param renderItem RenderItem для ГПИ
	 */
	public abstract void drawSlot(Slot slot, RenderItem renderItem);
	
	/**
	 *  Отрисовка подсветки при наведении курсора.
	 * 
	 * @param slot слот, на который наведен курсор
	 */
	public abstract void drawSlotHighlighting(Slot slot);
}
