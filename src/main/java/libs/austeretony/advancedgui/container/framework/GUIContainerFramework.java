package libs.austeretony.advancedgui.container.framework;

import libs.austeretony.advancedgui.screen.framework.GUIFramework;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Объект-каркас для работы с элементами ГПИ графического интерфейса контейнера.
 * 
 * @author AustereTony
 */
@SideOnly(Side.CLIENT)
public class GUIContainerFramework extends GUIFramework {

	public final GUIEnumPosition slotsPosition;
	
	public final IInventory inventory;
	
	public final int firstSlotIndex, lastSlotIndex, rows, columns, visibleSlots;
	
	private int slotsDistanceHorizontal, slotsDistanceVertical;
	
	private boolean slotBottom, forceUpdate, isTooltipsDisabled;
	
	/** Объект, содержащий элементы ГПИ, связанные со слотами */
	public final GUIContainerSlots slots;
	
	private int slotBottomLayerColor = 0x64000000;

	/**
	 * Основа для настройки ГПИ. Позволяет использовать новые элементы, а 
	 * так же предоставляет более удобную систему работы со слотами. Позволяет использовать 
	 * для слотов позиции, заданные в Container или задать слотам новую позицию
	 * и сформировать блок отображаемых слотов желаемого размера.
	 * 
	 * @param slotsPosition значение, определяющее какие координаты будут использовать слоты
	 * GUIPosition#CONTAINER - координаты, указанные в контейнере (ВНИМАНИЕ! rows и columns должны 
	 * быть равны нулю), GUIPosition#CUSTOM - координаты будут указаны в ГПИ.
	 * @param inventory IInventory, которому принадлежат слоты.
	 * @param firstSlotIndex индекс первого слота (включительно) из последовательности добавляемых.
	 * Индексы задаются в соответствии с их позициями в Container#inventorySlots.
	 * @param lastSlotIndex индекс последнего слота (включительно).
	 * @param rows кол-во отображаемых слотов по горизонтали.
	 * @param columns кол-во слотов отображаемых по вертикали.
	 */
	public GUIContainerFramework(GUIEnumPosition slotsPosition, IInventory inventory, int firstSlotIndex, int lastSlotIndex, int rows, int columns) {
		
		this.slots = new GUIContainerSlots(this);
		
		this.slotsPosition = slotsPosition;	
		this.inventory = inventory;
		this.firstSlotIndex = firstSlotIndex;
		this.lastSlotIndex = lastSlotIndex;
		this.rows = rows;
		this.columns = columns;
		
		this.visibleSlots = rows * columns == 0 ? lastSlotIndex - firstSlotIndex : rows * columns;
		this.slotsDistanceHorizontal = 2;
		this.slotsDistanceVertical = 2;
		
		this.setSize(this.columns * (this.getSlotWidth() + this.slotsDistanceHorizontal), this.rows * (this.getSlotHeight() + this.slotsDistanceVertical));
	}
	
	/**
	 * Устанавливает координаты первого слота фреймворка. 
	 * 
	 * @param xPosition
	 * @param yPosition
	 */
	@Override
	public GUIContainerFramework setPosition(int xPosition, int yPosition) {
		
		if (this.slotsPosition == GUIEnumPosition.CUSTOM) {
			
			super.setPosition(xPosition, yPosition);
		}
		
		return this;
	}
	
	/**
	 * Установка расстояния между слотами (для кастомной конфигурации слотов фреймворка).
	 * 
	 * @param slotsDistanceHorizontal расстояние по горизонтали
	 * @param slotsDistanceVertical расстояние по вертикали
	 */
	public void setSlotDistance(int slotsDistanceHorizontal, int slotsDistanceVertical) {
		
		this.slotsDistanceHorizontal = slotsDistanceHorizontal;
		this.slotsDistanceVertical = slotsDistanceVertical;
		
		this.setSize(this.columns * (this.getSlotWidth() + slotsDistanceHorizontal), this.rows * (this.getSlotHeight() + slotsDistanceVertical));
	}
	
	public int getSlotDistanceHorizontal() {
		
		return this.slotsDistanceHorizontal;
	}
	
	public int getSlotDistanceVertical() {
		
		return this.slotsDistanceVertical;
	}
	
	public boolean isSlotBottomLayerEnabled() {
		
		return this.slotBottom;
	}
	
	/**
	 * Заливка нижнего слоя слотов.
	 */
	public GUIContainerFramework enableSlotBottomLayer() {
		
		this.slotBottom = true;
		
		return this;
	}
	
	public int getSlotBottomLayerColor() {

		return this.slotBottomLayerColor;
	}
	
	/**
	 * Установка цвета подложки под слотом.
	 * 
	 * @param colorHex
	 */
	public void setSlotBottomLayerColor(int colorHex) {
		
		this.slotBottomLayerColor = colorHex;
	}
	
	public int getSlotWidth() {
		
		return this.slots.getSlotRenderer() != null ? this.slots.getSlotRenderer().getWidth() : this.slots.SLOT_SIZE;
	}
	
	public int getSlotHeight() {
		
		return this.slots.getSlotRenderer() != null ? this.slots.getSlotRenderer().getHeight() : this.slots.SLOT_SIZE;
	}
	
	public boolean getForcedToUpdate() {
		
		return this.forceUpdate;
	}		
	
	/**
	 * Заставляет фреймворк обновлять слоты при каждом клике по слоту в ГПИ.
	 */
	public GUIContainerFramework forceUpdateOnEveryClick() {
		
		this.forceUpdate = true;
		
		return this;
	}
	
	public boolean getTooltipsDisabled() {
		
		return this.isTooltipsDisabled;
	}		
	
	/**
	 * Определяет будут ли отображаться тултипы предметов в слотах при наведении курсора.
	 */
	public GUIContainerFramework disableTooltips() {
		
		this.isTooltipsDisabled = true;
		
		return this;
	}
	
	/**
	 * Enum для определения используемых слотами координат при добавлении в ГПИ.
	 */
	@SideOnly(Side.CLIENT)
	public enum GUIEnumPosition {
		
		CONTAINER,
		CUSTOM
	}
}
