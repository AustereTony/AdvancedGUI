package libs.austeretony.advancedgui.guicontainer.contextmenu;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

/**
 * Контекстное меню для ГПИ.
 */
@SideOnly(Side.CLIENT)
public class GUIContextMenu {
    
	private Minecraft mc;
    
    public int xPosition, yPosition;
    
    private int actionBoxWidth, actionBoxHeight;
    
    private boolean isEnabled, isVisible;
    
    private final float scaleFactor;
    
    /** Пользовательские действия */
	private List<GUIContextAction> actions = new ArrayList<GUIContextAction>();

	/**
	 * Контекстное меню.
	 * 
	 * @param scaleFactor коэффициент скалирования.
	 */
    public GUIContextMenu(float scaleFactor) {
    	
    	this.scaleFactor = scaleFactor;
    	
    	this.actionBoxWidth = 50;
    	this.actionBoxHeight = 10;
    	
    	this.isVisible = true;
    }
    
    /**
     * Метод для добавления действия в меню.
     * 
     * @param action добавляемое действие.
     */
    public void addAction(GUIContextAction action) {
    	
    	if (!this.actions.contains(action)) {
    		
    		action.actionWidth = this.actionBoxWidth;
    		action.actionHeight = this.actionBoxHeight;
    		
    		action.scaleFactor = this.scaleFactor;
    		
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
    
    public void mouseOver(int mouseX, int mouseY, int guiLeft, int guiTop) {
    	
    	if (this.isEnabled()) {
    	
    		for (GUIContextAction action : this.actions) {

    			action.mouseOver(mouseX, mouseY, guiLeft, guiTop);
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
    
    public void open(IInventory inventory, int mouseX, int mouseY, int guiLeft, int guiTop, Slot slot) {
    	
        mouseX -= guiLeft;
        mouseY -= guiTop;
    	
    	this.xPosition = mouseX;
    	this.yPosition = mouseY;
    	
        for (GUIContextAction action : this.actions) {
        	        		
        	action.slot = slot;
        	
        	action.xPosition = this.xPosition;
        	action.yPosition = this.yPosition + this.actions.indexOf(action) * (int) (this.actionBoxHeight * this.scaleFactor);	
			
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
    	
    	this.actionBoxWidth = width;
        this.actionBoxHeight = height;
    }
    
    public boolean isVisible() {
    	
    	return this.isVisible;
    }
    
    public void setVisible(boolean isVisible) {
    	
    	this.isVisible = isVisible;
    }
    
    public boolean isEnabled() {
    	
    	return this.isEnabled;
    }
    
    public void setEnabled(boolean isEnabled) {
    	
    	this.isEnabled = isEnabled;
    }
}
