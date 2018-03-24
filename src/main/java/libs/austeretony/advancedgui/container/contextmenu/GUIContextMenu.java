package libs.austeretony.advancedgui.container.contextmenu;

import java.util.ArrayList;
import java.util.List;

import libs.austeretony.advancedgui.screen.core.GUIAdvancedElement;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Контекстное меню для ГПИ.
 * 
 * @author AustereTony
 */
@SideOnly(Side.CLIENT)
public class GUIContextMenu extends GUIAdvancedElement<GUIContextMenu> {
            
    /** Пользовательские действия */
	private List<GUIContextAction> actions = new ArrayList<GUIContextAction>();
	
	private int actionBoxWidth, actionBoxHeight;

	/**
	 * Контекстное меню.
	 */
    public GUIContextMenu() {
    	    	
    	this.setActionBoxSize(50, 10);   
    	
    	this.setVisible(true);
    }
    
    /**
     * Метод для добавления действия в меню.
     * 
     * @param action добавляемое действие
     */
    public GUIContextMenu addAction(GUIPresetAction presetAction) {
    	
    	GUIContextAction action = new GUIContextAction(presetAction);
    	
    	if (!this.actions.contains(action)) {
    		
    		action.setContextMenu(this);
    		
    		action.setSize(this.getActionBoxWidth(), this.getActionBoxHeight());   		
    		action.setScale(this.getScale());
    		
    		action.setEnabledColor(this.getEnabledColor());
    		action.setDisabledColor(this.getDisabledColor());
    		action.setHoveredColor(this.getHoveredColor());
    		action.setEnabledTextColor(this.getEnabledTextColor());
    		action.setDisabledTextColor(this.getDisabledTextColor());
    		action.setHoveredTextColor(this.getHoveredTextColor());
    		
    		action.setPopupText(presetAction.getPopupText());
    		
    		action.setPopupAlignment(this.getXPopupOffset(), this.getYPopupOffset());
    		action.setPopupTextColor(this.getPopupTextColor());
    		
    		this.actions.add(action);
    		
    		this.setSize(this.getActionBoxWidth(), this.getActionBoxHeight() * this.actions.size());
    	}
    	
    	return this;
    }
  
    @Override
    public void draw() {
    	
        if (this.isEnabled()) {
               	        	            
            for (GUIContextAction action : this.actions) {
            	
            	action.draw();
            }
        }      
    }
    
    @Override
    public void drawPopup(int mouseX, int mouseY) {
    	
        if (this.isEnabled()) {
               	        	            
            for (GUIContextAction action : this.actions) {
            	
            	action.drawPopup(mouseX, mouseY);
            }
        }      
    }
    
    @Override
    public void mouseOver(int mouseX, int mouseY) {
    	
    	if (this.isEnabled()) {
    	
    		for (GUIContextAction action : this.actions) {

    			action.mouseOver(mouseX, mouseY);
    		}
    		
        	this.setHovered(mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + (int) (this.getWidth() * this.getScale()) && mouseY < this.getY() + (int) (this.getHeight() * this.getScale()));   
    	}   	
    }
    
    public boolean mousePressed(IInventory inventory) {   
    	
    	if (this.isEnabled()) {
    		
    		if (this.isHovered()) {

    			if (this.mouseClicked(0, 0)) {
    				
    				for (GUIContextAction action : this.actions) {
        		
    					if (action.performAction(inventory)) {
        			
    						this.close();
    					
    						return true;
    					}
    				}    
    			}
    			
    			else {
    				
    				return false;
    			}
    		}
    		
    		else {
    			
				this.close();
				
				return true;
    		}
    	}
    	
    	return false;
    }
    
    public void open(IInventory inventory, int mouseX, int mouseY, Slot slot) {
    	
    	this.setPosition(mouseX, mouseY);
    	
        for (GUIContextAction action : this.actions) {
        	        		
        	action.slot = slot;
        
        	action.setPosition(this.getX(), this.getY() + this.actions.indexOf(action) * (int) (this.getActionBoxHeight() * this.getScale()));
			
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
    
    public int getActionBoxWidth() {
    	
    	return this.actionBoxWidth;
    }
    
    public int getActionBoxHeight() {
    	
    	return this.actionBoxHeight;
    }
    
    /**
     * Устанавливает размеры бокса активного действия.
     * 
     * @param width ширина
     * @param height высота
     */
    public void setActionBoxSize(int width, int height) {
    	
    	this.actionBoxWidth = width;
    	this.actionBoxHeight = height;
    }
}
