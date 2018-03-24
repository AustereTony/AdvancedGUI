package libs.austeretony.advancedgui.container.slot;

import libs.austeretony.advancedgui.container.framework.GUIContainerFramework;
import libs.austeretony.advancedgui.screen.core.GUIBaseElement;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.inventory.Slot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Шаблон для пользовательских рендереров слотов, позволит управлять отрисовкой.
 * 
 * @author AustereTony
 */
@SideOnly(Side.CLIENT)
public abstract class GUISlotRenderer extends GUIBaseElement<GUISlotRenderer> {
		
	protected GUIContainerFramework framework;
		
	/**
	 * Создание рендерера.
	 * 
	 * @param slotWidth ширина слота
	 * @param slotHeight высота слота
	 */
	public GUISlotRenderer(int slotWidth, int slotHeight) {
		
		super();
		
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
