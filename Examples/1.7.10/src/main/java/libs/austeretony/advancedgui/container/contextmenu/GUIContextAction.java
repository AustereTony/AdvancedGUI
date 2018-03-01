package libs.austeretony.advancedgui.container.contextmenu;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import libs.austeretony.advancedgui.screen.core.GUIElement;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

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
        	        	                        
        	GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        	
            GL11.glPushMatrix();
            
            GL11.glTranslatef(this.getX(), this.getY(), 0.0F);
            
            GL11.glScalef(this.getScale(), this.getScale(), 0.0F);                           
        	
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    		
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
        	
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        	
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
        	
            GL11.glPopMatrix();

            GL11.glEnable(GL12.GL_RESCALE_NORMAL);    
        }
    }
    
    public boolean isValid(IInventory inventory, Slot slot) {
    	
    	return this.presetAction.isValidAction(inventory, slot, this.mc.thePlayer);
    }

    public boolean performAction(IInventory inventory) {
    	
    	if (Mouse.isButtonDown(0) && this.isHovered()) {
    		
    		this.presetAction.performAction(inventory, this.slot, this.mc.thePlayer);
    		
    		if (this.presetAction.getSound() != null) {
    			
    			//TODO Разобраться со звуком
    	        this.mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(this.presetAction.getSound().fileLocation, this.presetAction.getSound().volume));
    		}
    		
    		return true;
    	}
    	
    	return false;
    }
}

