package libs.austeretony.advancedgui.container.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import libs.austeretony.advancedgui.container.browsing.GUIScroller;
import libs.austeretony.advancedgui.container.button.GUISlider;
import libs.austeretony.advancedgui.container.contextmenu.GUIContextMenu;
import libs.austeretony.advancedgui.container.slot.GUISlotRenderer;
import libs.austeretony.advancedgui.container.text.GUISearchField;
import libs.austeretony.advancedgui.screen.framework.GUIFramework;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Объект-каркас для работы с новыми элементами ГПИ контейнера.
 * 
 * @author AustereTony
 */
@SideOnly(Side.CLIENT)
public class GUIContainerFramework extends GUIFramework {

	public final GUIPosition slotsPosition;
	
	public final IInventory inventory;
	
	public final int firstSlotIndex, lastSlotIndex, rows, columns, visibleSlots;
	
	private int xPosition, yPosition, width, height, slotsDistanceHorizontal, slotsDistanceVertical;
	
	private boolean isHovered, forceUpdate;
	
	public final List<Slot> slots = new ArrayList<Slot>();
	public final List<Integer> slotsIndexes = new ArrayList<Integer>();

	public final List<Slot> slotsBuffer = new ArrayList<Slot>();
	public final List<Integer> indexesBuffer = new ArrayList<Integer>();
	
	public final List<Slot> searchSlots = new ArrayList<Slot>();
	public final List<Integer> searchIndexes = new ArrayList<Integer>();
		
	/** Сортировщик слотов без фильтрации. Позволяет загрузить содержимое в его действительном виде. */
	public static final GUISorter BASE_SORTER = new GUIBaseSorter();
	
	private GUISorter currentSorter;
	
	private GUIScroller scroller;
	
	private GUISlider slider;
    
    private GUISearchField searchField;
    
    public final Map<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();   
        
	private GUIContextMenu contextMenu;  
	
	private GUISlotRenderer slotRenderer;
	
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
	public GUIContainerFramework(GUIPosition slotsPosition, IInventory inventory, int firstSlotIndex, int lastSlotIndex, int rows, int columns) {
		
		this.slotsPosition = slotsPosition;	
		this.inventory = inventory;
		this.firstSlotIndex = firstSlotIndex;
		this.lastSlotIndex = lastSlotIndex;
		this.rows = rows;
		this.columns = columns;
		
		this.visibleSlots = rows * columns == 0 ? lastSlotIndex - firstSlotIndex : rows * columns;
		this.slotsDistanceHorizontal = 2;
		this.slotsDistanceVertical = 2;
		
		this.width = this.columns * (this.getSlotWidth() + this.slotsDistanceHorizontal);
		this.height = this.rows * (this.getSlotHeight() + this.slotsDistanceVertical);
		
		this.currentSorter = this.BASE_SORTER;
	}
	
	/**
	 * Устанавливает координаты первого слота фреймворка. 
	 * 
	 * @param xPosition
	 * @param yPosition
	 */
	public void setPosition(int xPosition, int yPosition) {
		
		if (this.slotsPosition == GUIPosition.CUSTOM) {
			
			this.xPosition = xPosition;
			this.yPosition = yPosition;
		}
	}
	
	public int getXPosition() {
		
		return this.xPosition;
	}
	
	public int getYPosition() {
		
		return this.yPosition;
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
		
		this.width = this.columns * (this.getSlotWidth() + slotsDistanceHorizontal);
		this.height = this.rows * (this.getSlotHeight() + slotsDistanceVertical);
	}
	
	public int getSlotDistanceHorizontal() {
		
		return this.slotsDistanceHorizontal;
	}
	
	public int getSlotDistanceVertical() {
		
		return this.slotsDistanceVertical;
	}
	
	/**
	 * Инициализация GUISlotRenderer для этого фреймворка. Все его слоты будут использовать этот рендерер.
	 * 
	 * @param slotRenderer
	 */
	public void initSlotRenderer(GUISlotRenderer slotRenderer) {
		
		this.slotRenderer = slotRenderer;
		
		this.slotRenderer.initFramework(this);
	}
	
	public GUISlotRenderer getSlotRenderer() {
		
		return this.slotRenderer;
	}
	
	public int getSlotWidth() {
		
		return this.slotRenderer != null ? this.slotRenderer.getWidth() : 16;
	}
	
	public int getSlotHeight() {
		
		return this.slotRenderer != null ? this.slotRenderer.getHeight() : 16;
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
	
	public void setCurrentSorter(GUISorter sorter) {
		
		this.currentSorter = sorter;
	}
	
	public GUISorter getCurrentSorter() {
		
		return this.currentSorter;
	}
	
	/**
	 * Инициализирует объект GUIScroller, добавляющий скроллинг в ГПИ.
	 * 
	 * @param scroller
	 */
	public void initScroller(GUIScroller scroller) {
		
		this.scroller = scroller;
	}
	
	public GUIScroller getScroller() {
		
		return this.scroller;
	}
	
	/**
	 * Инициализирует объект GUISlider, добавляющий слайдер для скроллера.
	 * 
	 * @param slider
	 */
	public void initSlider(GUISlider slider) {
		
		this.slider = slider;
	}
	
	public GUISlider getSlider() {
		
		return this.slider;
	}
	
	/**
	 * Инициализирует объект GUISearchField, предоставляющий возможность осуществлять 
	 * поиск по содержимому слотов фреймворка.
	 *
	 * @param searchField
	 */
	public void initSearchField(GUISearchField searchField) {
		
		this.searchField = searchField;
	}	
	
	public GUISearchField getSearchField() {
		
		return this.searchField;
	}
	
	/**
	 * Инициализирует объект GUIContextMenu, предоставляющий возможность вызова контекстного меню
	 * при нажатии ПКМ на слоте с предметом. 
	 * 
	 * @param contextMenu
	 */
	public void initContextMenu(GUIContextMenu contextMenu) {
		
		this.contextMenu = contextMenu;
	}	
	
	public GUIContextMenu getContextMenu() {
		
		return this.contextMenu;
	}
	
	public void mouseOver(int mouseX, int mouseY) {
    	
    	this.isHovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;   		
    }
	
	public boolean isHovered() {
		
		return this.isHovered;
	}
	
	public boolean getForcedToUpdate() {
		
		return this.forceUpdate;
	}		
	
	/**
	 * Заставляет фреймворк обновлять слоты при каждом клике по любому слоту в ГПИ.
	 */
	public void forceUpdateOnEveryClick() {
		
		this.forceUpdate = true;
	}
	
	/**
	 * Enum для определения используемых слотами координат при добавлении в ГПИ.
	 */
	public enum GUIPosition {
		
		CONTAINER,
		CUSTOM
	}
}
