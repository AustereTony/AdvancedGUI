package libs.austeretony.advancedgui.screen.browsing;

import org.lwjgl.input.Mouse;

import libs.austeretony.advancedgui.container.framework.GUIContainerFramework;
import libs.austeretony.advancedgui.screen.button.GUISlider;
import libs.austeretony.advancedgui.screen.panel.GUIButtonPanel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Скроллер, позволяющий прокручивать содержимое некоторых элементов.
 * 
 * @author AustereTony
 */
@SideOnly(Side.CLIENT)
public class GUIScroller {
	
    public final GUIEnumScrollerType scrollerType;

	public final int rowsAmount, rowsVisible;
	
	private int maxPosition;
	
	private boolean ignoreBorders, hasSlider;
	
	private int currentPosition;
	
	private GUISlider slider;
	
	/**
	 * Скроллер с кастомными параметрами. Скроллинг возможен только в пределах блока слотов фреймворка 
	 * (для исключения конфликтов нескольких скроллеров), используйте GUIScroller#ignoreBorders() 
	 * для снятия этого ограничения (если скроллер только один).
	 * @param rowsAmount кол-во строк слотов для указанной конфигурации фреймворка.
	 * Пример: 100 слотов в фреймворке, 30 должны отображаться (6 строк * 5 столбцов).
	 * 100 / 30 * 6 = 20 сток максимум. 6 строк видимых.
	 * @param rowsVisible кол-во видимых строк слотов.
	 */
	public GUIScroller(int rowsAmount, int rowsVisible) {
		
    	this.scrollerType = GUIEnumScrollerType.STANDARD;
		this.rowsAmount = rowsAmount;
		this.rowsVisible = rowsVisible;
		
		this.maxPosition = rowsAmount - rowsVisible;
		
		this.currentPosition = 0;
	}
	
	/**
	 * Автоматически расчитываемый скроллер для фреймворка GUIContainerFramework. Скроллинг возможен только в пределах блока слотов фреймворка 
	 * (для исключения конфликтов нескольких скроллеров), используйте GUIScroller#ignoreBorders() 
	 * для снятия этого ограничения (если скроллер только один).
	 * @param framework фреймворк, для которого создаётся скроллер.
	 */
	public GUIScroller(GUIContainerFramework framework) {
		
    	this.scrollerType = GUIEnumScrollerType.STANDARD;
		this.rowsAmount = (int) (((float) (framework.lastSlotIndex - framework.firstSlotIndex + 1) / (float) (framework.rows * framework.columns)) * (float) framework.rows);
		this.rowsVisible = framework.rows;
		
		this.maxPosition = this.rowsAmount - this.rowsVisible;
		
		this.currentPosition = 0;
	}
	
	/**
	 * Автоматически расчитываемый скроллер для панели кнопок GUIButtonPanel. Скроллинг возможен только в пределах панели 
	 * (для исключения конфликтов нескольких скроллеров), используйте GUIScroller#ignoreBorders() 
	 * для снятия этого ограничения (если скроллер только один).
	 * @param panel панель, для которой создаётся скроллер.
	 */
	public GUIScroller(GUIButtonPanel panel) {
		
    	this.scrollerType = GUIEnumScrollerType.STANDARD;
		this.rowsAmount = panel.getMaxButtonsAmount();
		this.rowsVisible = panel.getVisibleButtonsAmount();
		
		this.maxPosition = this.rowsAmount - this.rowsVisible;
		
		this.currentPosition = 0;
	}
	
	public boolean hasSlider() {
		
		return this.hasSlider;
	}
	
	/**
	 * Инициализирует объект GUISlider, добавляющий слайдер для скроллера.
	 * 
	 * @param slider
	 */
	public GUIScroller initSlider(GUISlider slider) {
		
		this.slider = this.slider == null ? slider : this.slider;
		
		this.hasSlider = true;
		
		this.slider.setScroller(this);
		
		return this;
	}
	
	public GUISlider getSlider() {
		
		return this.slider;
	}
	
	public int getPosition() {
		
		return this.currentPosition;
	}
	
	public boolean incrementPosition() {
		
		if (this.currentPosition < this.maxPosition) {
			
			this.currentPosition++;
			
			return true;
		}
			
		return false;
	}
	
	public boolean decrementPosition() {
		
		if (this.currentPosition > 0) {
			
			this.currentPosition--;
			
			return true;
		}
			
		return false;
	}
	
	public boolean handleScroller() {
				
		int i = Mouse.getEventDWheel();
		
		if (i > 0) {
    	
			return this.decrementPosition();			
		}

		if (i < 0) {

			return this.incrementPosition();			
		}
						
		return false;
	}
	
	public void setPosition(int position) {
		
		this.currentPosition = position >= 0 ? (position <= this.maxPosition ? position : this.maxPosition) : 0;
	}
	
	public void resetPosition() {
		
		this.currentPosition = 0;
	}
	
	public int getMaxPosition() {
		
		return this.maxPosition;
	}
	
	/**
	 * Обеспечивает работу скроллера (прокручивание колёсиком) независимо от положения курсора.
	 */
	public GUIScroller ignoreBorders() {
		
		this.ignoreBorders = true;
		
		return this;
	}
	
	public boolean shouldIgnoreBorders() {
		
		return this.ignoreBorders;
	}
	
    /**
     * Определяет способ скроллинга слотов (стандартный (аля креатив) или плавный). ВНИМАНИЕ! Плавный скролл ещё не реализован, используйте STANDARD.
     */
	@SideOnly(Side.CLIENT)
    public enum GUIEnumScrollerType {
    	
    	STANDARD,
    	SMOOTH
    }
}
