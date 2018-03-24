package libs.austeretony.advancedgui.screen.button;

import libs.austeretony.advancedgui.screen.core.GUIAdvancedElement;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Кнопка для ГПИ. Может быть автономна или добавлена на панель GUIButtonPanel.
 * 
 * @author AustereTony
 */
@SideOnly(Side.CLIENT)
public class GUIButton extends GUIAdvancedElement<GUIButton> {
				    	                    
    /**
     * Конструктор для создания автономной кнопки (вне панели).
     * 
     * @param xPosition позиция по x
     * @param yPosition позиция по y
     * @param buttonWidth ширина кнопки (активной зоны)
     * @param buttonHeight высота кнопки (активной зоны)
     */
    public GUIButton(int xPosition, int yPosition, int buttonWidth, int buttonHeight) {
    	
        this.setPosition(xPosition, yPosition);
        this.setSize(buttonWidth, buttonHeight);
        
        this.setEnabled(true);
        this.setVisible(true);
    }
    
    /**
     * Конструктор для кнопки, добавляемой на панель. Все параметры определяются панелью при добавлении кнопки.
     */
    public GUIButton() {
        
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
                    	        	
        	if (this.isTextureEnabled()) {
        		
        		int u = this.getTextureU();
        		
        		this.mc.getTextureManager().bindTexture(this.getTexture());          
                
                if (this.isHovered() || this.isToggled()) {
                	
                	u += this.getTextureWidth() * 2;
                }
                
                else {
                	
                	u += this.getTextureWidth();
                }
                
                this.drawCustomSizedTexturedRect(this.ZERO + (this.getWidth() - this.getTextureWidth()) / 2, this.ZERO + (this.getHeight() - this.getTextureHeight()) / 2, u, this.getTextureV(), this.getTextureWidth(), this.getTextureHeight(), this.getTextureWidth() * 3, this.getTextureHeight());
        	}
        	
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        	
        	if (this.isBackgroundEnabled()) {
        		
        		int color;
        		
                if (!this.isEnabled()) {
                	
                	color = this.getDisabledColor();
                }
                
                else if (this.isHovered() || this.isToggled()) {
                	
                	color = this.getHoveredColor();
                }
                
                else {
                	
                	color = this.getEnabledColor();
                }     		
                
                this.drawRect(this.ZERO, this.ZERO, this.ZERO + this.getWidth(), this.ZERO + this.getHeight(), color);
        	}
        	
        	if (this.getDisplayText().length() > 0) {
        		        		
        		int color;
        		
                if (!this.isEnabled()) {
                	
                	color = this.getDisabledTextColor();
                }
                
                else if (this.isHovered() || this.isToggled()) {
                	                	
                	color = this.getHoveredTextColor();
                }
                
                else {
                	
                	color = this.getEnabledTextColor();
                }
                                
                this.mc.fontRenderer.drawStringWithShadow(this.getDisplayText(), this.ZERO + (this.getWidth() - this.getWidth() / 2 - this.mc.fontRenderer.getStringWidth(this.getDisplayText()) / 2), this.ZERO + (this.getHeight() - 8) / 2, color);
        	}
            
            GlStateManager.popMatrix();

            GlStateManager.enableRescaleNormal();
        }
    }
}
