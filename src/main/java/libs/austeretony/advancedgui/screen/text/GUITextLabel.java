package libs.austeretony.advancedgui.screen.text;

import libs.austeretony.advancedgui.screen.core.GUISimpleElement;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Простой графический элемент в виде строки символов.
 * 
 * @author AustereTony
 */
@SideOnly(Side.CLIENT)
public class GUITextLabel extends GUISimpleElement<GUITextLabel> {
	
	public GUITextLabel(int xPosition, int yPosition) {
		
		this.setPosition(xPosition, yPosition);
		
        this.setEnabled(true);
        this.setVisible(true);
	}
	
    @Override
    public void draw() {
    	
        if (this.isVisible()) {
        	
            GlStateManager.disableRescaleNormal();        
        	
            GlStateManager.pushMatrix();
            
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);
            
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);
                                	
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        	
        	if (this.isBackgroundEnabled()) {       		 		
                
                this.drawRect(this.ZERO, this.ZERO, this.ZERO + this.getWidth(), this.ZERO + this.getHeight(), this.getEnabledColor());
        	}
        	
        	if (this.getDisplayText().length() > 0) {
                                
                this.mc.fontRenderer.drawStringWithShadow(this.getDisplayText(), this.ZERO, this.ZERO, this.getEnabledTextColor());
        	}
            
            GlStateManager.popMatrix();

            GlStateManager.enableRescaleNormal();
        }
    }
    
    /**
     * Установка текста, отображаемого элементом. 
     * 
     * @param displayText
     * 
     * @return
     */
    @Override
    public GUITextLabel setDisplayText(String displayText) {
    	
    	super.setDisplayText(displayText);
    	
    	this.setSize(this.mc.fontRenderer.getStringWidth(displayText), this.FONT_HEIGHT);
    	
    	return this;
    }
}
