package libs.austeretony.advancedgui.container.contextmenu;

import libs.austeretony.advancedgui.screen.core.GUIAdvancedElement;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Класс, реализующий работу по отрисовке и обработке действия GUIPresetAction.
 * 
 * @author AustereTony
 */
@SideOnly(Side.CLIENT)
public class GUIContextAction extends GUIAdvancedElement<GUIContextAction> {
		
	public Slot slot;
    
    private GUIPresetAction presetAction;
    
    private GUIContextMenu contextMenu;
        
    /**
     * Оболочка для GUIPresetAction, отвечает за рендер и т.д.
     * 
     * @param presetAction ассоциированное действие
     */
    public GUIContextAction(GUIPresetAction presetAction) {
    	
    	super();

        this.presetAction = presetAction;
        this.setDisplayText(presetAction.dispalyName);
    }

    @Override
    public void draw() {
    	
        if (this.isVisible()) {
        	        	                        
            GlStateManager.disableRescaleNormal();        
        	
            GlStateManager.pushMatrix();
            
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);
            
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);
                        
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    		
        	int color;
        		
            if (!this.isEnabled()) {
                	
                color = this.getDisabledColor();
            }
                
            else if (this.isHovered()) {
                	
                color = this.getHoveredColor();
            }
                
            else {
                	
                color = this.getEnabledColor();
            }     		
                
            this.drawRect(this.ZERO, this.ZERO, this.ZERO + this.getWidth(), this.ZERO + this.getHeight(), color);
        	
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        	
        	if (this.getDisplayText().length() > 0) {
        		
                if (!this.isEnabled()) {
                	
                	color = this.getDisabledTextColor();
                }
                
                else if (this.isHovered()) {
                	                	
                	color = this.getHoveredTextColor();
                }
                
                else {
                	
                	color = this.getEnabledTextColor();
                }
                                
                this.mc.fontRenderer.drawStringWithShadow(this.getDisplayText(), this.ZERO + 4, this.ZERO + (this.getHeight() - this.FONT_HEIGHT) / 2, color);
        	}
        	
            GlStateManager.popMatrix();

            GlStateManager.enableRescaleNormal();
        }
    }
    
    public boolean isValid(IInventory inventory, Slot slot) {
    	
    	return this.presetAction.isValidAction(inventory, slot, this.mc.player);
    }

    public boolean performAction(IInventory inventory) {
    	
    	if (this.isEnabled() && this.isHovered()) {
    		    		
    		this.presetAction.performAction(inventory, this.slot, this.mc.player);
    		
    		if (this.presetAction.getSound() != null) {
    			
    			this.mc.player.playSound(this.presetAction.getSound().soundEvent, this.presetAction.getSound().volume, this.presetAction.getSound().pitch);
    		}
    		
    		return true;
    	}
    	
    	return false;
    }
    
    public GUIContextMenu getContextMenu() {
    	
    	return this.contextMenu;
    }
    
    public void setContextMenu(GUIContextMenu contextMenu) {
    	
    	this.contextMenu = contextMenu;
    }
}

