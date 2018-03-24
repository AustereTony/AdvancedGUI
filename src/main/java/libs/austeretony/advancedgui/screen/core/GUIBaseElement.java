package libs.austeretony.advancedgui.screen.core;

import org.lwjgl.input.Mouse;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import libs.austeretony.advancedgui.screen.button.GUISound;
import net.minecraft.client.Minecraft;

/**
 * Класс-основа элементов ГПИ.
 * 
 * @author AustereTony
 */
@SideOnly(Side.CLIENT)
public class GUIBaseElement<T extends GUIBaseElement> {

	protected final Minecraft mc = Minecraft.getMinecraft();
    
	public static final long DOUBLE_CLICK_TIME = 500L;
	
    private int xPosition, yPosition, width, height;
            
    private long lastClickTimeMillis;
    
    private boolean isEnabled, isHovered, isToggled, isDragged, hasSound, canNotBeDragged, needDoubleClick;
    
    private GUISound sound;
    
    public void mouseOver(int mouseX, int mouseY) {  	       	
    	
    	this.setHovered(this.isEnabled() && mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.getWidth() && mouseY < this.getY() + this.getHeight());   
    }
    
    /**
     * Определяет, сделан ли клик по элементу.
     * 
     * @param mouseX 
     * @param mouseY 
     * 
     * @return true если клик совершён
     */
    public boolean mouseClicked(int mouseX, int mouseY) {
    	
    	boolean flag = false;
    	
    	if (this.isDoubleClickRequired()) {
    		
    		if (this.isClickedLately()) {
    			
        		flag = true;
        		
        		this.lastClickTimeMillis = 0L;
    		}
    		
    		else {
    			
    			this.lastClickTimeMillis = Minecraft.getSystemTime();
    		}
    	}
    	
    	else {
    		
    		flag = true;
    	}
    	    	
        return Mouse.isButtonDown(0) && this.isHovered() && flag;
    }
    
    public long getLastClickTime() {
    	
    	return this.lastClickTimeMillis;
    }
    
    public void setLastClickTime(long timeMillis) {
    	
    	this.lastClickTimeMillis = timeMillis;
    }
    
    public boolean isDoubleClickRequired() {
    	
    	return this.needDoubleClick;
    }
    
    /**
     * Определяет необходимость двойного клика для элемента.
     * 
     * @return вызывающий объект
     */
    public T requireDoubleClick() {
    	
    	this.needDoubleClick = true;
    	
    	return (T) this;
    }
    
    public boolean isClickedLately() {
    	
		return (Minecraft.getSystemTime() - this.lastClickTimeMillis) < this.DOUBLE_CLICK_TIME;
    }
    
    public int getX() {
    	
    	return this.xPosition;
    }
    
    public int getY() {
    	
    	return this.yPosition;
    }
    
    /**
     * Установка позиции элемента в рабочем пространстве.
     * 
     * @param xPosition
     * @param yPosition
     * 
     * @return вызывающий объект
     */
    public T setPosition(int xPosition, int yPosition) {
    	
    	this.xPosition = xPosition;
    	this.yPosition = yPosition;
    	
    	return (T) this;
    } 
    
    public int getWidth() {
    	
    	return this.width;
    }
    
    public int getHeight() {
    	
    	return this.height;
    }
    
    /**
     * Установка размера элемента.
     * 
     * @param width
     * @param height
     * 
     * @return вызывающий объект
     */
    public T setSize(int width, int height) {
    	
    	this.width = width;
    	this.height = height;
    	
    	return (T) this;
    }
    
    public boolean isEnabled() {
    	
    	return this.isEnabled;
    }
    
    /**
     * Определяет, можно ли взаимодействовать с элементом.
     * 
     * @param isEnabled
     * 
     * @return вызывающий объект
     */
    public T setEnabled(boolean isEnabled) {
    	
    	this.isEnabled = isEnabled;
    	
    	return (T) this;
    }  
    
    public boolean isHovered() {
    	   	    	    	
        return this.isHovered;
    }
    
    /**
     * Определяет, наведён ли курсор на элемент. Используется в {@link GUIBaseElement#mouseOver(int, int)}.
     * 
     * @param isHovered
     * 
     * @return вызывающий объект
     */
    public T setHovered(boolean isHovered) {
    	
    	this.isHovered = isHovered;
    	
    	return (T) this;
    }
    
    public boolean isToggled() {
    	
        return this.isToggled;
    }
    
    /**
     * "Зажимание" кнопки, метод может быть вызван при создании экземпляра.
     * 
     * @param isToggled
     * 
     * @return вызывающий объект
     */
    public T setToggledAtStart(boolean isToggled) {
    	
    	this.isToggled = isToggled;
    	
    	return (T) this;
    }
    
    /**
     * "Зажимание" кнопки, возвращает переданное логическое значение для удобства.
     * 
     * @param isToggled
     * 
     * @return вызывающий объект
     */
    public boolean setToggled(boolean isToggled) {
    	
    	this.isToggled = isToggled;
    	
    	return isToggled;
    }
    
    public boolean isDragged() {
    	
        return this.isDragged;
    }
    
    /**
     * Определяет, "подцеплен" ли элемент (должен ли курсор управлять позицией элемента).
     * 
     * @param isDragged
     * 
     * @return вызывающий объект
     */
    public T setDragged(boolean isDragged) {
    	
    	if (!this.isCanNotBeDragged()) {
    		
    		this.isDragged = isDragged;    		   		
    	}
    	
    	return (T) this;
    }
    
    public boolean isCanNotBeDragged() {
    	
        return this.canNotBeDragged;
    }
    
    /**
     * Исключает возможность взаимодействия с помощью курсора.
     * 
     * @return вызывающий объект
     */
    public T setCanNotBeDragged() {
    	
    	this.canNotBeDragged = true;
    	
    	return (T) this;
    }
    
    public boolean hasSound() {
    	
    	return this.hasSound;
    }
    
    public GUISound getSound() {
    	
    	return this.sound;
    }
    
    /**
     * Установка звука для интерактивного элемента.
     * 
     * @param sound
     * 
     * @return вызывающий объект
     */
    public T setSound(GUISound sound) {
    	
    	this.sound = sound;
    	
    	this.hasSound = true;
    	
    	return (T) this;
    }
}
