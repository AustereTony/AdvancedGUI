package libs.austeretony.advancedgui.screen.button;

import libs.austeretony.advancedgui.screen.core.GUIElement;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Кнопка для ГПИ. Может быть автономна или добавлена на панель GUIButtonPanel.
 */
@SideOnly(Side.CLIENT)
public class GUIButton extends GUIElement {
	
	private ResourceLocation popupTexture;
	
	private boolean isPopupBackgroundEabled, isPopupTextureEabled;
	
    private int popupTextureU, popupTextureV;    
	                    
    /**
     * Конструктор для создания автономной кнопки (вне панели).
     * 
     * @param xPosition позиция по x
     * @param yPosition позиция по y
     * @param buttonWidth ширина кнопки (активной зоны)
     * @param buttonHeight высота кнопки (активной зоны)
     * @param scaleFactor коэффициент скалирования
     */
    public GUIButton(int xPosition, int yPosition, int buttonWidth, int buttonHeight, float scaleFactor) {
    	
    	super(scaleFactor);
    	
        this.setPosition(xPosition, yPosition);
        this.setSize(buttonWidth, buttonHeight);
        
        this.setEnabled(true);
        this.setVisible(true);
        this.setTextureEnabled(true);
    }
    
    /**
     * Конструктор для кнопки, добавляемой на панель.
     */
    public GUIButton() {
    	
    	super(1.0F);
        
        this.setEnabled(true);
        this.setVisible(true);
        this.setTextureEnabled(true);
    }
    
    public void draw() {
    	
        if (this.isVisible()) {
        	
            GlStateManager.disableRescaleNormal();        
        	
            GlStateManager.pushMatrix();
            
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);
            
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);
                        
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    	        	
        	if (this.isTextureEnabled()) {
        		
        		int 
        		u = this.getTextureU();
        		
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
                                
                this.mc.fontRenderer.drawStringWithShadow(I18n.format(this.getDisplayText()), this.ZERO + (this.getWidth() - this.mc.fontRenderer.getStringWidth(this.getDisplayText()) / 2) / 2, this.ZERO + (this.getHeight() - 8) / 2, color);
        	}
            
            GlStateManager.popMatrix();

            GlStateManager.enableRescaleNormal();
        }
    }
    
    public void drawPopup(int mouseX, int mouseY) {
    	
    	if (this.isVisible()) {   	    		
    		
    		if (this.getPopupText().length() > 0) {
    			    		
    			if (this.isHovered()) {
        		
                    GlStateManager.disableRescaleNormal();        
                	
                    GlStateManager.pushMatrix();
                    
                    GlStateManager.translate(mouseX, mouseY, 0.0F);
                    
                    GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);
                                
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                                        
    				String[] popupStrings = this.getPopupText().split("/n");
    				
    				int i, 
    				frameWidth = this.mc.fontRenderer.getStringWidth(I18n.format(popupStrings[0])), 
    				frameHeight = 8 * popupStrings.length, 
    				xStart = this.ZERO + (int) (this.getWidth() * this.getScale()),
    				yStart = this.ZERO - popupStrings.length * 10;
    				
    				if (this.isCustomPopup()) {
    					
    					if (this.isPopupTextureEnabled()) {    						    						
    		        		
    		        		this.mc.getTextureManager().bindTexture(this.getPopupTexture());          
    		                
    		                this.drawCustomSizedTexturedRect(xStart, yStart, this.getPopupTextureU(), this.getPopupTextureV(), frameWidth, frameHeight, frameWidth, frameHeight);
    					}
    					
                        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    					
    					if (this.isPopupBackgroundEnabled()) {
    						
    		            	this.drawRect(xStart, yStart, xStart + frameWidth, yStart + frameHeight, this.getPopupBackgroundColor());
    					}
    				}
    			    				
    				else {
                    	
                    	int 
                    	color1 = - 267386864,
                    	color2 = 1347420415,
                    	color3 = (color2 & 16711422) >> 1 | color2 & - 16777216;
                    	                                                
                        this.drawGradientRect(xStart - 3, yStart - 4, xStart + frameWidth + 3, yStart - 3, color1, color1, 300.0);
                        this.drawGradientRect(xStart - 3, yStart + frameHeight + 3, xStart + frameWidth + 3, yStart + frameHeight + 4, color1, color1, 300.0);
                        this.drawGradientRect(xStart - 3, yStart - 3, xStart + frameWidth + 3, yStart + frameHeight + 3, color1, color1, 300.0);
                        this.drawGradientRect(xStart - 4, yStart - 3, xStart - 3, yStart + frameHeight + 3, color1, color1, 300.0);
                        this.drawGradientRect(xStart + frameWidth + 3, yStart - 3, xStart + frameWidth + 4, yStart + frameHeight + 3, color1, color1, 300.0);

                        this.drawGradientRect(xStart - 3, yStart - 3 + 1, xStart - 3 + 1, yStart + frameHeight + 3 - 1, color2, color3, 300.0);
                        this.drawGradientRect(xStart + frameWidth + 2, yStart - 3 + 1, xStart + frameWidth + 3, yStart + frameHeight + 3 - 1, color2, color3, 300.0);
                        this.drawGradientRect(xStart - 3, yStart - 3, xStart + frameWidth + 3, yStart - 3 + 1, color2, color2, 300.0);
                        this.drawGradientRect(xStart - 3, yStart + frameHeight + 2, xStart + frameWidth + 3, yStart + frameHeight + 3, color3, color3, 300.0);
                    }
    			
    				for (i = 0; i < popupStrings.length; i++) {    				
    				
    					this.mc.fontRenderer.drawStringWithShadow(I18n.format(popupStrings[i]), xStart, yStart + 10 * i, this.getPopupTextColor());
    				}
    				
    	            GlStateManager.popMatrix();

    	            GlStateManager.enableRescaleNormal();
    			}   			
    		}
    	}
    }
    
    public boolean isPopupBackgroundEnabled() {
    	
    	return this.isPopupBackgroundEabled;
    }
    
    public void setPopupBackgroundEnabled(boolean isPopupBackgroundEabled) {
    	
    	this.isPopupBackgroundEabled = isPopupBackgroundEabled;
    }
    
    public boolean isPopupTextureEnabled() {
    	
    	return this.isPopupTextureEabled;
    }
    
    public void setPopupTextureEnabled(boolean isPopupTextureEabled) {
    	
    	this.isPopupTextureEabled = isPopupTextureEabled;
    }
    
	public ResourceLocation getPopupTexture() {
		
		return this.popupTexture;
	}
    
    public int getPopupTextureU() {
    	
    	return this.popupTextureU;
    }
    
    public int getPopupTextureV() {
    	
    	return this.popupTextureV;
    }
    
    public void setPopupTexture(ResourceLocation texture) {
    	
    	this.popupTexture = texture;   
    }
    
    public void setPopupTexture(ResourceLocation texture, int u, int v) {
    	
    	this.popupTexture = texture;
    	
    	this.popupTextureU = u;
    	this.popupTextureV = v;
    }
}
