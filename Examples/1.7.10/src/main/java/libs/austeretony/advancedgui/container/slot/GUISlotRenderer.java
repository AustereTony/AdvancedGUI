package libs.austeretony.advancedgui.container.slot;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import libs.austeretony.advancedgui.container.framework.GUIContainerFramework;
import libs.austeretony.advancedgui.screen.core.GUIElement;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.inventory.Slot;

/**
 * Шаблон для пользовательских рендереров слотов, позволит управлять отрисовкой.
 */
@SideOnly(Side.CLIENT)
public abstract class GUISlotRenderer extends GUIElement {
		
	protected GUIContainerFramework framework;
		
	/**
	 * Создание рендерера.
	 * @param slotWidth ширина слота
	 * @param slotHeight высота слота
	 */
	public GUISlotRenderer(int slotWidth, int slotHeight) {
		
		super(1.0F);
		
		this.setSize(slotWidth, slotHeight);	
	}
	
	public final void initFramework(GUIContainerFramework framework) {
		
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
