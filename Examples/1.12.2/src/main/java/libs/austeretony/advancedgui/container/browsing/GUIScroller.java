package libs.austeretony.advancedgui.container.browsing;

import org.lwjgl.input.Mouse;

import libs.austeretony.advancedgui.container.framework.GUIContainerFramework;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Скроллер, позволяющий прокручивать содержимое слотов фреймворка колёсиком мыши.
 */
@SideOnly(Side.CLIENT)
public class GUIScroller {
	
    public final GUIScrollerType scrollerType;

	public final int rowsAmount, rowsVisible;
	
	private int maxPosition;
	
	private boolean ignoreBorders;
	
	private int currentPosition;
	
	/**
	 * Скроллер с кастомными параметрами. Скроллинг возможен только в пределах блока слотов фреймворка 
	 * (для исключения конфликтов нескольких скроллеров), используйте GUIScroller#ignoreFrameworkBorders() 
	 * для снятия этого ограничения (если скроллер только один).
	 * 
	 * @param rowsAmount кол-во строк слотов для указанной конфигурации фреймворка.
	 * Пример: 100 слотов в фреймворке, 30 должны отображаться (6 строк * 5 столбцов).
	 * 100 / 30 * 6 = 20 сток максимум. 6 строк видимых.
	 * @param rowsVisible кол-во видимых строк слотов.
	 */
	public GUIScroller(GUIScrollerType scrollerType, int rowsAmount, int rowsVisible) {
		
    	this.scrollerType = scrollerType;
		this.rowsAmount = rowsAmount;
		this.rowsVisible = rowsVisible;
		
		this.maxPosition = rowsAmount - rowsVisible;
		
		this.currentPosition = 0;
	}
	
	/**
	 * Автоматически расчитываемый скроллер для фреймворка. Скроллинг возможен только в пределах блока слотов фреймворка 
	 * (для исключения конфликтов нескольких скроллеров), используйте GUIScroller#ignoreFrameworkBorders() 
	 * для снятия этого ограничения (если скроллер только один).
	 * 
	 * @param framework фреймворк, для которого создаётся скроллер.
	 */
	public GUIScroller(GUIScrollerType scrollerType, GUIContainerFramework framework) {
		
    	this.scrollerType = scrollerType;
		this.rowsAmount = ((framework.lastSlotIndex - framework.firstSlotIndex + 1) / (framework.rows * framework.columns)) * framework.rows;
		this.rowsVisible = framework.rows;
		
		this.maxPosition = this.rowsAmount - this.rowsVisible;
		
		this.currentPosition = 0;
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
    	
			if (this.decrementPosition()) {  
    	
				return true;			
			}
		}

		if (i < 0) {
    	
			if (this.incrementPosition()) {

				return true;			
			}
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
	public void ignoreFrameworkBorders() {
		
		this.ignoreBorders = true;
	}
	
	public boolean shouldIgnoreBorders() {
		
		return this.ignoreBorders;
	}
	
    /**
     * Определяет способ скроллинга слотов (стандартный (аля креатив) или плавный). ВНИМАНИЕ! Плавный скролл ещё не реализован, используйте STANDARD.
     */
    public enum GUIScrollerType {
    	
    	STANDARD,
    	SMOOTH
    }
}
