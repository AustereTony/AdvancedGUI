package libs.austeretony.advancedgui.container.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import libs.austeretony.advancedgui.container.contextmenu.GUIContextMenu;
import libs.austeretony.advancedgui.container.slot.GUISlotRenderer;
import libs.austeretony.advancedgui.screen.browsing.GUIScroller;
import libs.austeretony.advancedgui.screen.text.GUITextField;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Класс для инкапсуляции массивов рабочих слотов ГПИ и элементов, связанных с ними.
 * 
 * @author AustereTony
 */
@SideOnly(Side.CLIENT)
public class GUIContainerSlots {
	
	private final GUIContainerFramework framework;
	
	/** Слоты, которые рендерятся в ГПИ */
	public final List<Slot> visibleSlots = new ArrayList<Slot>();
	public final List<Integer> visibleSlotsIndexes = new ArrayList<Integer>();

	/** Буфер слотов, использующийся для промежуточных операций */
	public final List<Slot> slotsBuffer = new ArrayList<Slot>();
	public final List<Integer> indexesBuffer = new ArrayList<Integer>();
	
	/** Слоты, которые содержат моментальный результат поиска */
	public final List<Slot> searchSlots = new ArrayList<Slot>();
	public final List<Integer> searchIndexes = new ArrayList<Integer>();
	
	public static final int SLOT_SIZE = 16;
	
	private boolean hasScroller, hasSearchField, hasContextMenu, hasSlotRenderer;
	
	/** Сортировщик слотов без фильтрации. Позволяет загрузить содержимое в его действительном виде. */
	public static final GUISorter BASE_SORTER = new GUIBaseSorter();
	
	private GUISorter currentSorter;
	
	private GUIScroller scroller;
    
    private GUITextField searchField;
    
    public final Map<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();   
        
	private GUIContextMenu contextMenu;  
	
	private GUISlotRenderer slotRenderer;
	
	public GUIContainerSlots(GUIContainerFramework framework) {
		
		this.framework = framework;
		
		this.currentSorter = this.BASE_SORTER;
	}
	
    public boolean hasSlotRenderer() {
    	
    	return this.hasSlotRenderer;
    }
	
	/**
	 * Инициализация GUISlotRenderer для cлотов. Все его слоты будут использовать этот рендерер.
	 * 
	 * @param slotRenderer
	 */
	public void initSlotRenderer(GUISlotRenderer slotRenderer) {
		
		this.slotRenderer = this.slotRenderer == null ? slotRenderer : this.slotRenderer;
		
		this.hasSlotRenderer = true;
		
		this.slotRenderer.initFramework(this.framework);
	}
	
	public GUISlotRenderer getSlotRenderer() {
		
		return this.slotRenderer;
	}
	
	public int getSlotWidth() {
		
		return this.slotRenderer != null ? this.slotRenderer.getWidth() : this.SLOT_SIZE;
	}
	
	public int getSlotHeight() {
		
		return this.slotRenderer != null ? this.slotRenderer.getHeight() : this.SLOT_SIZE;
	}
	
	public void setCurrentSorter(GUISorter sorter) {
		
		this.currentSorter = sorter;
	}
	
	public GUISorter getCurrentSorter() {
		
		return this.currentSorter;
	}
	
    public boolean hasScroller() {
    	
    	return this.hasScroller;
    }
	
	/**
	 * Инициализирует объект GUIScroller, добавляющий скроллинг для слотов.
	 * 
	 * @param scroller
	 */
	public void initScroller(GUIScroller scroller) {
		
		this.scroller = this.scroller == null ? scroller : this.scroller;
		
		this.hasScroller = true;
	}
	
	public GUIScroller getScroller() {
		
		return this.scroller;
	}
	
    public boolean hasSearchField() {
    	
    	return this.hasSearchField;
    }
	
	/**
	 * Инициализирует объект GUISearchField, предоставляющий возможность осуществлять 
	 * поиск по содержимому слотов фреймворка.
	 *
	 * @param searchField
	 */
	public void initSearchField(GUITextField searchField) {
		
		this.searchField = this.searchField == null ? searchField : this.searchField;
		
		this.hasSearchField = true;
	}	
	
	public GUITextField getSearchField() {
		
		return this.searchField;
	}
	
    public boolean hasContextMenu() {
    	
    	return this.hasContextMenu;
    }
	
	/**
	 * Инициализирует объект GUIContextMenu, предоставляющий возможность вызова контекстного меню
	 * при нажатии ПКМ на слоте с предметом. 
	 * 
	 * @param contextMenu
	 */
	public void initContextMenu(GUIContextMenu contextMenu) {
		
		this.contextMenu = this.contextMenu == null ? contextMenu : this.contextMenu;
		
		this.hasContextMenu = true;
	}	
	
	public GUIContextMenu getContextMenu() {
		
		return this.contextMenu;
	}
}
