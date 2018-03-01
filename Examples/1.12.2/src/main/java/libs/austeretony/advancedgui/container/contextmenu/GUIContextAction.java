package libs.austeretony.advancedgui.container.contextmenu;

import org.lwjgl.input.Mouse;

import libs.austeretony.advancedgui.screen.core.GUIElement;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Класс, реализующий работу по отрисовке и обработке действия GUIPresetAction.
 */
@SideOnly(Side.CLIENT)
public class GUIContextAction extends GUIElement {
		
	public Slot slot;
    
    private GUIPresetAction presetAction;
        
    /**
     * Оболочка для GUIPresetAction, отвечает за рендер и т.д.
     * 
     * @param presetAction ассоциированное действие
     */
    public GUIContextAction(GUIPresetAction presetAction) {
    	
    	super(1.0F);

        this.presetAction = presetAction;
        this.setDisplayText(presetAction.dispalyName);
    }

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
                                
                this.mc.fontRenderer.drawStringWithShadow(I18n.format(this.getDisplayText()), this.ZERO + 4, this.ZERO + (this.getHeight() - 8) / 2, color);
        	}
        	
            GlStateManager.popMatrix();

            GlStateManager.enableRescaleNormal();
        }
    }
    
    public boolean isValid(IInventory inventory, Slot slot) {
    	
    	return this.presetAction.isValidAction(inventory, slot, this.mc.player);
    }

    public boolean performAction(IInventory inventory) {
    	
    	if (Mouse.isButtonDown(0) && this.isHovered()) {
    		
    		this.presetAction.performAction(inventory, this.slot, this.mc.player);
    		
    		if (this.presetAction.getSound() != null) {
    			
    			//TODO Разобраться со звуком
    	        //this.mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(this.presetAction.getSound().fileLocation, this.presetAction.getSound().volume));
    		}
    		
    		return true;
    	}
    	
    	return false;
    }
}

