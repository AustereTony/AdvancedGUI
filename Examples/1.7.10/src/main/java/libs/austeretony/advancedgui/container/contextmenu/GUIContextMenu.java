package libs.austeretony.advancedgui.container.contextmenu;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import libs.austeretony.advancedgui.screen.core.GUIElement;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

/**
 * Контекстное меню для ГПИ.
 */
@SideOnly(Side.CLIENT)
public class GUIContextMenu extends GUIElement {
            
    /** Пользовательские действия */
	private List<GUIContextAction> actions = new ArrayList<GUIContextAction>();

	/**
	 * Контекстное меню.
	 * 
	 * @param scaleFactor коэффициент скалирования
	 */
    public GUIContextMenu(float scaleFactor) {
    	
    	super(scaleFactor);
    	
    	this.setActionBoxSize(50, 10);
    	
    	this.setVisible(true);
    }
    
    /**
     * Метод для добавления действия в меню.
     * 
     * @param action добавляемое действие
     */
    public void addAction(GUIPresetAction presetAction) {
    	
    	GUIContextAction action = new GUIContextAction(presetAction);
    	
    	if (!this.actions.contains(action)) {
    		
    		action.setSize(this.getWidth(), this.getHeight());   		
    		action.setScale(this.getScale());
    		
    		action.setEnabledColor(this.getEnabledColor());
    		action.setDisabledColor(this.getDisabledColor());
    		action.setHoveredColor(this.getHoveredColor());
    		action.setEnabledTextColor(this.getEnabledTextColor());
    		action.setDisabledTextColor(this.getDisabledTextColor());
    		action.setHoveredTextColor(this.getHoveredTextColor());
    		
    		this.actions.add(action);
    	}
    }
  
    public void draw() {
    	
        if (this.isEnabled() && this.isVisible()) {
               	        	            
            for (GUIContextAction action : this.actions) {
            	
            	action.draw();
            }
        }      
    }
    
    public void mouseOver(int mouseX, int mouseY) {
    	
    	if (this.isEnabled()) {
    	
    		for (GUIContextAction action : this.actions) {

    			action.mouseOver(mouseX, mouseY);
    		}
    	}
    }
    
    public boolean mousePressed(IInventory inventory, int mouseX, int mouseY) {
    	
    	if (this.isEnabled()) {

    		for (GUIContextAction action : this.actions) {
    			
    			if (action.performAction(inventory)) {
    				
    				this.close();
    				
    				return true;
    			}       	
    		}   		
    	}
        
		return false;
    }
    
    public void open(IInventory inventory, int mouseX, int mouseY, Slot slot) {
    	
    	this.setPosition(mouseX, mouseY);
    	
        for (GUIContextAction action : this.actions) {
        	        		
        	action.slot = slot;
        
        	action.setPosition(this.getX(), this.getY() + this.actions.indexOf(action) * (int) (this.getHeight() * this.getScale()));
			
        	if (action.isValid(inventory, slot)) {
        		
        		action.setEnabled(true);
        	}
        	
    		action.setVisible(true);
        }
    	
    	this.setEnabled(true);
    }
    
    public void close() {
    	
    	this.setEnabled(false);
    	
        for (GUIContextAction action : this.actions) {
        	
			action.setEnabled(false);
			action.setVisible(false);
        }
    }
    
    /**
     * Устанавливает размеры иконки активного действия.
     * 
     * @param width ширина
     * @param height высота
     */
    public void setActionBoxSize(int width, int height) {
    	
    	this.setSize(width, height);
    }
}
